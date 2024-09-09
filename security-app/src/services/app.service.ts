import { Logger, Injectable } from '@nestjs/common';
import axios, { AxiosError, AxiosInstance } from 'axios';
import { CreateFileDto } from '../dtos/createFile.dto';
import {
  CreateFileCassandraAppResponse,
  CreateFileResponse,
} from '../interfaces/createFileResponse.interface';
import { ConfigService } from '@nestjs/config';
import CassandraAppException from '../exceptions/cassandraApp.exception';
import { ApiConfig } from '../../config/configuration';
import ForbiddenException from '../exceptions/forbidden.exception';
import { FileMetadata, FileMetadataPrimitives } from '../domain/fileMetadata';
import { RequestsSearchFields } from '../interfaces/requestSearchFields.interface';
import { ListFilesResponse, RecordInfo } from '../dtos/listFilesResponse.dto';
import BadRequestException from '../exceptions/badRequest.exception';
import FileNotFoundException from '../exceptions/fileNotFound.exception';

@Injectable()
export default class AppService {
  private readonly logger;
  private axios: AxiosInstance;
  private readonly cassandraAppUrl: string;
  constructor(private readonly configService: ConfigService<ApiConfig, true>) {
    this.cassandraAppUrl = this.configService.get<string>('cassandraAppUrl');
    this.logger = new Logger(AppService.name);
    this.axios = axios.create({
      timeout: 10000, // Timeout in milliseconds (5 seconds)
    });
  }

  async createFile(
    request: CreateFileDto,
    clientId?: string,
  ): Promise<CreateFileResponse> {
    request.owner = clientId as string;
    try {
      const response = await this.axios.post(this.cassandraAppUrl, request);
      return (response.data as CreateFileCassandraAppResponse).data;
    } catch (error) {
      this.logger.error(error.stack);
      throw new CassandraAppException();
    }
  }

  async readFile(id: string, clientId?: string): Promise<any> {
    const isAccessAllowed = await this.checkAccess(id, clientId);
    if (!isAccessAllowed) throw new ForbiddenException();

    try {
      const response = await this.axios.get(`${this.cassandraAppUrl}/${id}`);
      return response.data;
    } catch (error) {
      if ((error as AxiosError).status === 404)
        throw new FileNotFoundException();
      this.logger.error(error.stack);
      throw new CassandraAppException();
    }
  }

  private async checkAccess(
    id: string,
    clientId: string | undefined,
  ): Promise<boolean> {
    let response;
    try {
      response = await this.axios.get(`${this.cassandraAppUrl}/${id}/metadata`);
    } catch (error) {
      this.logger.error(error.stack);
      throw new CassandraAppException();
    }
    const fileMetadata = FileMetadata.fromPrimitives(
      response.data as FileMetadataPrimitives,
    );

    return fileMetadata.isAccessAllowed(clientId);
  }

  async getFiles(searchObject?: RequestsSearchFields | undefined) {
    try {
      let searchUrlEncoded = '?';
      if (searchObject?.owner)
        searchUrlEncoded = `${searchUrlEncoded}owner=${searchObject.owner}&`;
      if (searchObject?.documentId)
        searchUrlEncoded =
          searchUrlEncoded + `documentId=${searchObject.documentId}&`;
      if (searchObject?.page)
        searchUrlEncoded = searchUrlEncoded + `page=${searchObject.page}&`;
      if (searchObject?.pageSize)
        searchUrlEncoded =
          searchUrlEncoded + `pageSize=${searchObject.pageSize}&`;
      const response = await this.axios.get(
        `${this.cassandraAppUrl}/list${searchUrlEncoded}`,
      );
      const listFiles = response.data as ListFilesResponse;
      listFiles.records = listFiles.records.map((record: RecordInfo) => {
        if (!record.extension) delete record.extension;
        return record;
      });
      return listFiles;
    } catch (error) {
      this.logger.error(error.stack);
      throw new CassandraAppException();
    }
  }
}
