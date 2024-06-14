package hr.irb.CIR.DAP.index;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface IIndex {
    public Integer getLimit();
    public Integer getOffset();
    public void setLimit(Integer limit);

    public void setOffset(Integer offset) ;
    public void setPublisherKey(UUID publisherKey);
    public UUID getPublisherKey() ;

    public boolean connect();
    public String getNodeUrl() ;
    public void setNodeUrl(String nodeUrl) ;
    public boolean isConnected() ;
    public void disconnect() ;
    public DbOptions getOptions() ;

    public void setOptions(DbOptions options) ;

    public UUID insertFile(MultipartFile file, String documentId, String extension) throws Exception;
    public byte[] getFile(UUID fileId);
    public byte[] getFileByPublishersId(String documentId);
    public HashMap<String,String> getFileInfo(UUID id, String documentId);
}
