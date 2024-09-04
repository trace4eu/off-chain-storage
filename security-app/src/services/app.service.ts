import { Logger, Injectable } from '@nestjs/common';
import axios, { AxiosInstance } from 'axios';
import { CreateFileDto } from '../dtos/createFile.dto';
import {
  CreateFileCassandraAppResponse,
  CreateFileResponse,
} from '../interfaces/createFileResponse.interface';
import { ConfigService } from '@nestjs/config';
import CassandraAppException from '../exceptions/cassandraApp.exception';
import { ApiConfig } from '../../config/configuration';
import ForbiddenException from '../exceptions/forbidden.exception';
import { FileMetadata } from '../domain/fileMetadata';
import { FileServiceResponseData } from '../interfaces/FileServiceResponseData.interface';
import { RequestsSearchFields } from '../interfaces/requestSearchFields.interface';

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
      (response.data as FileServiceResponseData).data,
    );
    return fileMetadata.checkAccess(clientId);
  }

  async getFiles(searchObject?: RequestsSearchFields) {
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
      return response.data;
    } catch (error) {
      this.logger.error(error.stack);
      throw new CassandraAppException();
    }
  }
}
