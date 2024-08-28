import { Body, Controller, Get, Param, Post, UseGuards } from '@nestjs/common';
import { ApiOAuth2, ApiResponse, ApiTags } from '@nestjs/swagger';
import { CreateFileDto } from '../dtos/createFile.dto';
import {
  ScopesProtected,
  ValidScopes,
} from '../auth/decorators/scope.decorator';
import { JwtAuthGuardTrace4eu } from '../auth/guards/auth.trace4eu.guard';
import AppService from '../services/app.service';
import { GetEntityData } from '../auth/decorators/getEntityData.decorator';
import { EntityData } from '../auth/strategies/auth.trace4eu.strategy';
import { CreateFileResponseDto } from '../interfaces/createFileResponse.dto';

@ApiTags('files')
@Controller('files')
export class AppController {
  constructor(private readonly appService: AppService) {}

  @ScopesProtected([ValidScopes.ocsWrite])
  @UseGuards(JwtAuthGuardTrace4eu)
  @ApiOAuth2(['ocs:write'])
  @Post()
  @ApiResponse({
    status: 201,
    description: 'The record has been successfully created.',
  })
  createFile(@Body() request: CreateFileDto): Promise<CreateFileResponseDto> {
    return this.appService.createFile(request);
  }

  @ApiOAuth2(['ocs:read'])
  @Get('/:fileId')
  getFile(@Param('fileId') fileId: string) {
    return fileId;
  }

  @Get('/public/:fileId')
  getPublicFile(@Param('fileId') fileId: string) {
    return fileId;
  }

  @Get()
  listFiles(): string {
    return 'hello';
  }
}
