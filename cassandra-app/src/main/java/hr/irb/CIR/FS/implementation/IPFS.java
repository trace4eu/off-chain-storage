package hr.irb.CIR.FS.implementation;


import hr.irb.CIR.FS.AFS;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;
@Deprecated
public class IPFS extends AFS {
    private IPFS ipfs;

    public IPFS(String hostUrl) {
//        super(hostUrl);
        this.setHotsUrl(hostUrl);
        connect();
    }

    @Override
    public void connect() {
        if (isConnected()) return;
        ipfs = new IPFS(getHostUrl());
        super.connect();
    }

    @Override
    public void disconnect() {
        if (!isConnected()) return;
        ipfs = null;
        super.disconnect();
    }

    @Override
    public String addFile(String sourcePath, String destPath) {
        //TODO napisat
        // Assuming you have a Java equivalent for file_get_contents
        byte[] content = readContentFromFile(sourcePath);
//        String result = ipfs.add(content, destPath);
        // IPFS HTTP API endpoint
        return null;
    }

    @Override
    public void renameFile(String existingFileHash, String destFileName) {
        //TODO napisat
//        String content = ipfs.get(existingFileHash);
//        String result = ipfs.add(content, destFileName);
        // Handle the result as needed
    }

    @Override
    public void deleteFile(String fileHashToDelete) {
        //TODO napisat
//        ipfs.pin.rm(fileHashToDelete);
//        boolean isDeleted = !ipfs.object.get(fileHashToDelete);
//        if (!isDeleted) {
//            throw new Exception("File " + fileHashToDelete + " not deleted");
//        }
        // Handle the result as needed
    }

    @Override
    public String getUrl(String fileHash) {
        String fileUrl = "https://ipfs.io/ipfs/" + fileHash;
        return fileUrl;
    }

    @Override
    public boolean exists(String hash) {
        try {
//            String content = ipfs.cat(hash);
//            TODO ovdi implementirat
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // Add a method for reading content from a file
    private byte[] readContentFromFile(String sourcePath) {
        // Implement logic to read content from a file in Java
        //TODO ucitat sa IPFSa fajl
        return null;
    }
}
