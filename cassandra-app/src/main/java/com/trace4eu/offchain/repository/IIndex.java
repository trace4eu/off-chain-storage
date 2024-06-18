package com.trace4eu.offchain.repository;

import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.UUID;

public interface IIndex {
    public Integer getLimit();
    public Integer getOffset();
    public void setLimit(Integer limit);

    public void setOffset(Integer offset) ;
    public void setOwner(String owner);
    public String getOwner() ;

    public boolean connect();
    public String getNodeUrl() ;
    public void setNodeUrl(String nodeUrl) ;
    public boolean isConnected() ;
    public void disconnect() ;
    public DbOptions getOptions() ;

    public void setOptions(DbOptions options) ;

    public UUID insertFile(MultipartFile file, String documentId, String extension) throws Exception;
    public UUID insertFile(String jsonData) throws Exception;
    public byte[] getFile(UUID fileId);
    public byte[] getFileByOwner(String documentId);
    public HashMap<String,String> getFileInfo(UUID id, String documentId);
}
