import {
  IsBase64,
  IsBoolean,
  IsInt,
  IsOptional,
  IsPositive,
  IsString,
} from 'class-validator';
import { ApiProperty } from '@nestjs/swagger';

export class CreateFileDto {
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
  @IsInt()
  @IsPositive()
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
