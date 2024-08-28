import { Body, Controller, Get, Param, Post, UseGuards } from '@nestjs/common';
import { HttpService } from '@nestjs/axios';
import { ApiOAuth2, ApiResponse, ApiTags } from '@nestjs/swagger';
import { CreateFileDto } from '../dtos/createFile.dto';
import {
  ScopesProtected,
  ValidScopes,
} from '../auth/decorators/scope.decorator';
import { JwtAuthGuardTrace4eu } from '../auth/guards/auth.trace4eu.guard';

@ApiTags('files')
@Controller('files')
export class AppController {
  constructor(private readonly httpService: HttpService) {}

  @ScopesProtected([ValidScopes.ocsWrite])
  @UseGuards(JwtAuthGuardTrace4eu)
  @ApiOAuth2(['ocs:write'])
  @Post()
  @ApiResponse({
    status: 201,
    description: 'The record has been successfully created.',
  })
  createFile(
    @Body() request: CreateFileDto,
    // @GetEntityData() entityData: EntityData,
  ): string {
    return 'hello';
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
