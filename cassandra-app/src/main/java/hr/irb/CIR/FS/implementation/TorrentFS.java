package hr.irb.CIR.FS.implementation;

import hr.irb.CIR.FS.AFS;
@Deprecated
public class TorrentFS extends AFS {
    public TorrentFS(String hostUrl) {
        this.setHostUrl(hostUrl);
//        super(hostUrl);
        connect();
    }

    public void connect()  {
        return ;
    }

    public void disconnect()  {
        return ;
    }

    public String addFile(String sourcePath, String destPath) {
        return null;
    }

    public void renameFile(String existingFileHash, String destFileName) {
        return ;
    }

    public void deleteFile(String fileHashToDelete) {
        return ;
    }

    public String getUrl(String fileHash) {
        return "";
    }

    public boolean exists(String hash) {
        return false;
    }
}
