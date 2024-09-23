import { ApiProperty } from '@nestjs/swagger';

export class File {
  @ApiProperty()
  id: string;
  @ApiProperty()
  documentId: string;
  @ApiProperty()
  extension?: string;
  @ApiProperty()
  owner: string;
}
export class ListFilesResponse {
  @ApiProperty({
    type: [File],
  })
  files: File[];
  @ApiProperty()
  currentPage: number;
  @ApiProperty()
  total: number;
  @ApiProperty()
  pageSize: number;
}
