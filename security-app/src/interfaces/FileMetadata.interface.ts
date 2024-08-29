export interface FileMetadataCassandraApp {
  data: FileMetadata;
}

export interface FileMetadata {
  id: string;
  extension: string;
  owner: string;
  documentId: string;
  isPrivate?: boolean;
}
