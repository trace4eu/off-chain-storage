package hr.irb.CIR.FS;


public interface IFileSystem {
    void connect();
    boolean isConnected();
    void disconnect();
    String addFile(String sourcePath, String destPath);
    void renameFile(String existingFileHash, String destFileName);
    void deleteFile(String fileHashToDelete);

    String getUrl(String fileHash);
    boolean exists(String hash);
    String getHostUrl();
    void setHostUrl(String hostUrl);
}