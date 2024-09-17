package com.trace4eu.offchain.repository;


import com.trace4eu.offchain.dto.FileSearchResults;
import com.trace4eu.offchain.dto.OutputFile;
import com.trace4eu.offchain.dto.PutFileDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
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

    public UUID insertFile(MultipartFile file, String documentId, String extension, Integer ttl, Boolean isPrivate) throws Exception;
    public UUID insertFile(PutFileDTO importData) throws Exception;
    public byte[] getFile(UUID fileId);
    public HashMap<String,String> getFileInfo(UUID id, String documentId);
    public FileSearchResults getListOfFilesPaging(String documentId, String owner, Integer pageSize, Integer pageNumber) throws Exception ;
    public Boolean deleteFile(UUID fileId, String owner);
    public Boolean fileExists(UUID fileId);
    }
