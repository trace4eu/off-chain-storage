import {
  Controller,
  Get,
  Param,
  ParseUUIDPipe,
  Query,
  Res,
} from '@nestjs/common';
import { ApiOperation, ApiQuery, ApiResponse, ApiTags } from '@nestjs/swagger';
import AppService from '../services/app.service';
import { RequestsSearchFields } from '../interfaces/requestSearchFields.interface';
import { ListFilesResponse } from '../dtos/listFilesResponse.dto';
import { Response } from 'express';

@ApiTags('files (public)')
@Controller('/public/files')
export class FilesPublicController {
  constructor(private readonly appService: AppService) {}

  @ApiOperation({
    summary: 'Get a file',
    description: 'Get a file from the ocs component',
  })
  @ApiResponse({
    status: 200,
    description:
      "The binary content of the file will be returned. If it's a json, content-type will be application/json so the content-type is returned accordingly with the content. If no mapping can be done, the default one is application/octet-stream",
  })
  @Get('/:fileId')
  async getPublicFile(
    @Param('fileId', ParseUUIDPipe) fileId: string,
    @Res() response: Response,
  ) {
    const data = await this.appService.readFile(fileId);
    if (data.header['content-disposition'])
      response.setHeader(
        'content-disposition',
        data.header['content-disposition'],
      );
    if (data.header['content-length'])
      response.setHeader('content-length', data.header['content-length']);
    if (data.header['content-type'])
      response.setHeader('content-type', data.header['content-type']);
    return response.send(data.data);
  }

  @ApiQuery({ name: 'owner', type: 'string', required: false })
  @ApiQuery({ name: 'documentId', type: 'string', required: false })
  @ApiQuery({ name: 'page', type: 'string', required: false })
  @ApiQuery({ name: 'pageSize', type: 'string', required: false })
  @ApiOperation({
    summary: 'List files',
    description: 'List files using the available filters',
  })
  @ApiResponse({
    status: 200,
    type: ListFilesResponse,
  })
  @Get()
  listFiles(
    @Query() searchObject?: RequestsSearchFields,
  ): Promise<ListFilesResponse> {
    return this.appService.getFiles(searchObject);
  }
}
