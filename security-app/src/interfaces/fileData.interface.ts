interface Header {
  'content-disposition': string | undefined;
  'content-type': string | undefined;
  'content-length': string | undefined;
}

export interface FileData {
  header: Header;
  data: any;
}
