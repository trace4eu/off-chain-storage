import { Controller, Get, Param } from '@nestjs/common';
import { ApiTags } from '@nestjs/swagger';
import AppService from '../services/app.service';

@ApiTags('files (public)')
@Controller('files')
export class FilesPublicController {
  constructor(private readonly appService: AppService) {}

  @Get('/public/:fileId')
  getPublicFile(@Param('fileId') fileId: string) {
    return fileId;
  }

  @Get()
  listFiles(): string {
    return 'hello';
  }
}
