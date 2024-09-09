import { ApiProperty } from '@nestjs/swagger';

export class RecordInfo {
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
    type: [RecordInfo],
  })
  records: RecordInfo[];
  @ApiProperty()
  currentPage: number;
  @ApiProperty()
  total: number;
  @ApiProperty()
  pageSize: number;
}
