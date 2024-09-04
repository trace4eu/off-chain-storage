import { Controller, Get, Param, Query } from '@nestjs/common';
import { ApiTags } from '@nestjs/swagger';
import AppService from '../services/app.service';
import { RequestsSearchFields } from '../interfaces/requestSearchFields.interface';

@ApiTags('files (public)')
@Controller('/public/files')
export class FilesPublicController {
  constructor(private readonly appService: AppService) {}

  @Get('/:fileId')
  getPublicFile(@Param('fileId') fileId: string) {
    return this.appService.readFile(fileId);
  }

  @Get()
  listFiles(@Query() searchObject?: RequestsSearchFields): Promise<any> {
    return this.appService.getFiles(searchObject);
  }
}
