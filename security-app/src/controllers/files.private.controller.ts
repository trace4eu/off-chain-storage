import { Body, Controller, Get, Param, Post, UseGuards } from '@nestjs/common';
import { ApiBearerAuth, ApiResponse, ApiTags } from '@nestjs/swagger';
import { CreateFileDto } from '../dtos/createFile.dto';
import {
  ScopesProtected,
  ValidScopes,
} from '../auth/decorators/scope.decorator';
import { JwtAuthGuardTrace4eu } from '../auth/guards/auth.trace4eu.guard';
import AppService from '../services/app.service';
import { CreateFileResponse } from '../interfaces/createFileResponse.interface';
import { GetEntityData } from '../auth/decorators/getEntityData.decorator';
import { EntityData } from '../auth/strategies/auth.trace4eu.strategy';

@ApiTags('files (private)')
@ApiBearerAuth()
@Controller('files')
export class FilesPrivateController {
  constructor(private readonly appService: AppService) {}

  @ScopesProtected([ValidScopes.ocsWrite])
  @UseGuards(JwtAuthGuardTrace4eu)
  @Post()
  @ApiResponse({
    status: 201,
    description: 'The record has been successfully created.',
  })
  createFile(@Body() request: CreateFileDto): Promise<CreateFileResponse> {
    return this.appService.createFile(request);
  }

  @ScopesProtected([ValidScopes.ocsRead])
  @UseGuards(JwtAuthGuardTrace4eu)
  @Get('/:fileId')
  getFile(
    @Param('fileId') fileId: string,
    @GetEntityData() entityData: EntityData,
  ): Promise<any> {
    return this.appService.readFile(fileId, entityData.sub);
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
