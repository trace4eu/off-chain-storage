import {
  IsBase64,
  IsBoolean,
  IsNumber,
  IsOptional,
  IsString,
  Min,
} from 'class-validator';
import { ApiProperty } from '@nestjs/swagger';

export class FileCreation {
  owner: string;

  @IsString()
  @ApiProperty({
    description: 'extension of the file: json, pdf, ...',
  })
  extension: string;

  @IsString()
  @ApiProperty({
    description: 'identifier or name for the file',
  })
  documentId: string;

  @IsBase64()
  @ApiProperty({
    description: 'file encoded in base64',
  })
  file: string;

  @IsOptional()
  @IsNumber()
  @Min(0)
  @ApiProperty({
    required: false,
    description: 'TTL in seconds',
  })
  expirationTime: number;

  @IsOptional()
  @IsBoolean()
  @ApiProperty({
    required: false,
    description:
      'if True the file will remain with private access (only creator can access to the file)',
  })
  isPrivate: boolean;
}
