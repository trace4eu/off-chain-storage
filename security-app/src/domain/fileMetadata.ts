const regex = /^true$/i;

export interface FileMetadataPrimitives {
  id: string;
  extension: string;
  owner: string;
  documentId: string;
  isPrivate?: string | undefined;
}

export class FileMetadata {
  private id: string;
  private extension: string;
  private owner: string;
  private documentId: string;
  private isPrivate: boolean;

  static fromPrimitives(
    fileMetadataPrimitives: FileMetadataPrimitives,
  ): FileMetadata {
    const fileMetadata = new FileMetadata();
    fileMetadata.id = fileMetadataPrimitives.id;
    fileMetadata.extension = fileMetadataPrimitives.extension;
    fileMetadata.owner = fileMetadataPrimitives.owner;
    fileMetadata.documentId = fileMetadataPrimitives.documentId;

    if (fileMetadataPrimitives.isPrivate)
      fileMetadata.isPrivate = regex.test(fileMetadataPrimitives.isPrivate);
    return fileMetadata;
  }

  checkAccess(tokenSubject: string | undefined): boolean {
    if (this.isPrivate) {
      return this.owner === tokenSubject;
    }
    return true;
  }
}
