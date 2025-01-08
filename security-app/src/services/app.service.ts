import { Injectable, HttpStatus } from '@nestjs/common';
import axios, { AxiosError, AxiosInstance } from 'axios';
import { FileCreation } from '../dtos/fileCreation';
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
import { FilesPaginated, File } from '../dtos/files.dto';
import FileNotFoundException from '../exceptions/fileNotFound.exception';
import { FileData } from '../interfaces/fileData.interface';
import NotFoundException from '../exceptions/notFound.exception';
import BadRequestException from '../exceptions/badRequest.exception';

@Injectable()
export default class AppService {
  private axios: AxiosInstance;
  private readonly cassandraAppUrl: string;
  constructor(private readonly configService: ConfigService<ApiConfig, true>) {
    this.cassandraAppUrl = this.configService.get<string>('cassandraAppUrl');
    this.axios = axios.create({
      timeout: 10000, // Timeout in milliseconds (5 seconds)
    });
  }

  async createFile(
    request: FileCreation,
    clientId?: string,
  ): Promise<CreateFileResponse> {
    request.owner = clientId as string;
    try {
      const response = await this.axios.post(this.cassandraAppUrl, request);
      return (response.data as CreateFileCassandraAppResponse).data;
    } catch (error) {
      throw new CassandraAppException();
    }
  }

  async readFile(id: string, clientId?: string): Promise<FileData> {
    const isAccessAllowed = await this.checkAccess(id, clientId);
    if (!isAccessAllowed) throw new ForbiddenException();

    try {
      const response = await this.axios.get(`${this.cassandraAppUrl}/${id}`, {
        responseType: 'arraybuffer',
      });
      return {
        data: response.data,
        header: {
          'content-disposition':
            response.headers['content-disposition'] ?? undefined,
          'content-type': response.headers['content-type'] ?? undefined,
          'content-length': response.headers['content-length'] ?? undefined,
        },
      };
    } catch (error) {
      if ((error as AxiosError).status === 404)
        throw new FileNotFoundException();
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
      if ((error as AxiosError).status === HttpStatus.NOT_FOUND)
        throw new NotFoundException('File not found');
      throw new ForbiddenException();
    }
    const fileMetadata = FileMetadata.fromPrimitives(
      response.data as FileMetadataPrimitives,
    );

    return fileMetadata.isAccessAllowed(clientId);
  }

  async getMetadata(fileId: string) {
    let response;
    try {
      response = await this.axios.get(
        `${this.cassandraAppUrl}/${fileId}/metadata`,
      );
    } catch (error) {
      if ((error as AxiosError).status === HttpStatus.NOT_FOUND)
        throw new NotFoundException('File not found');
      throw new CassandraAppException();
    }
    const fileMetadata = FileMetadata.fromPrimitives(
      response.data as FileMetadataPrimitives,
    ).toPrimitives();
    delete fileMetadata.private;
    return fileMetadata;
  }

  async getFiles(searchObject?: RequestsSearchFields | undefined) {
    let isFilterQueryValid = false;
    if (searchObject && searchObject.owner && searchObject.owner.length > 0)
      isFilterQueryValid = true;
    if (
      searchObject &&
      searchObject.documentId &&
      searchObject.documentId.length > 0
    )
      isFilterQueryValid = true;
    if (!isFilterQueryValid)
      throw new BadRequestException(
        'at least documentId or owner should be provided',
      );
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
      const listFiles = response.data as FilesPaginated;
      listFiles.files = listFiles.files.map((file: File) => {
        if (!file.extension) delete file.extension;
        return file;
      });
      return listFiles;
    } catch (error) {
      throw new CassandraAppException();
    }
  }
}
