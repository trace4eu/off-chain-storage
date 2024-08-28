import { Logger, Injectable } from '@nestjs/common';
import axios, { AxiosInstance } from 'axios';
import { CreateFileDto } from '../dtos/createFile.dto';
import {
  CreateFileCassandraAppResponseDto,
  CreateFileResponseDto,
} from '../interfaces/createFileResponse.dto';
import { ConfigService } from '@nestjs/config';
import CassandraAppException from '../exceptions/cassandraApp.exception';
import { ApiConfig } from '../../config/configuration';

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

  async createFile(request: CreateFileDto): Promise<CreateFileResponseDto> {
    try {
      const response = await this.axios.post(this.cassandraAppUrl, request);
      return (response.data as CreateFileCassandraAppResponseDto).data;
    } catch (error) {
      this.logger.error(error.stack);
      throw new CassandraAppException();
    }
  }
}
