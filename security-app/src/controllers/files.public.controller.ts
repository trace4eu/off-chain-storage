import { Controller, Get, Param, Query } from '@nestjs/common';
import {
  ApiOperation,
  ApiQuery,
  ApiResponse,
  ApiTags,
  getSchemaPath,
} from '@nestjs/swagger';
import AppService from '../services/app.service';
import { RequestsSearchFields } from '../interfaces/requestSearchFields.interface';
import { ListFilesResponse } from '../dtos/listFilesResponse.dto';

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
  getPublicFile(@Param('fileId') fileId: string) {
    return this.appService.readFile(fileId);
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
