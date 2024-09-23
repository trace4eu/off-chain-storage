import {
  Body,
  Controller,
  Get,
  Param, ParseUUIDPipe,
  Post,
  Res,
  UseGuards,
} from '@nestjs/common';
import {
  ApiBearerAuth,
  ApiOperation,
  ApiResponse,
  ApiTags,
} from '@nestjs/swagger';
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
import { Response } from 'express';

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
  @ApiOperation({
    summary: 'Create a file',
    description: 'This creates a file to be stored in the ocs component',
  })
  createFile(
    @Body() request: CreateFileDto,
    @GetEntityData() entityData: EntityData,
  ): Promise<CreateFileResponse> {
    return this.appService.createFile(request, entityData.sub);
  }

  @ScopesProtected([ValidScopes.ocsRead])
  @UseGuards(JwtAuthGuardTrace4eu)
  @Get('/:fileId')
  @ApiOperation({
    summary: 'Get a file',
    description: 'Get a file from the ocs component',
  })
  @ApiResponse({
    status: 200,
    description:
      "The binary content of the file will be returned. If it's a json, content-type will be application/json so the content-type is returned accordingly with the content. If no mapping can be done, the default one is application/octet-stream",
  })
  async getFile(
    @Param('fileId', ParseUUIDPipe) fileId: string,
    @GetEntityData() entityData: EntityData,
    @Res() response: Response,
  ) {
    const data = await this.appService.readFile(fileId, entityData.sub);
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
}
