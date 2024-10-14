import { ApiProperty } from '@nestjs/swagger';

export class FileMetadata {
  @ApiProperty()
  id: string;
  @ApiProperty()
  owner: string;
  @ApiProperty()
  documentId: string;
  @ApiProperty()
  extension: string;
}
