export interface FileMetadataPrimitives {
  id: string;
  extension?: string;
  owner: string;
  documentId: string;
  private?: boolean | undefined;
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
    fileMetadata.owner = fileMetadataPrimitives.owner;
    fileMetadata.documentId = fileMetadataPrimitives.documentId;

    if (typeof fileMetadataPrimitives.private === 'boolean')
      fileMetadata.isPrivate = fileMetadataPrimitives.private;
    if (fileMetadataPrimitives.extension)
      fileMetadata.extension = fileMetadataPrimitives.extension;
    return fileMetadata;
  }

  toPrimitives(): FileMetadataPrimitives {
    const primitives: FileMetadataPrimitives = {
      id: this.id,
      owner: this.owner,
      documentId: this.documentId,
    }
    if (this.isPrivate) primitives.private = this.isPrivate;
    if (this.extension) primitives.extension = this.extension;
    return primitives;
  }

  isAccessAllowed(tokenSubject?: string | undefined): boolean {
    if (this.isPrivate) {
      return this.owner === tokenSubject;
    }
    return true;
  }
}
