package hr.irb.CIR.DAP.index;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.BoundStatement;
import com.datastax.oss.driver.api.core.cql.PreparedStatement;
import com.datastax.oss.driver.api.core.cql.ResultSet;
import com.datastax.oss.driver.api.core.cql.Row;
import com.datastax.oss.protocol.internal.util.Bytes;
import org.json.JSONObject;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.util.*;

public class CassandraIndex extends AIndex{
    private CqlSession session;
    public  CassandraIndex(DbOptions options){
        this.setOptions(options);
        if (this.session == null) this.connect();
    }
    @Override
    public boolean connect() {
        String hostName= getOptions().getHostname();
        if (hostName==null) hostName="localhost";
        InetSocketAddress node = new InetSocketAddress(hostName, 9042);
        try {
            session = CqlSession.builder()
                    .withKeyspace(getOptions().getDbName())
                    .addContactPoint(node)
                    .build();
//            if (session == null) session = CqlSession.builder().build();
            this.connected  = true;
        } catch (Exception e) {
            this.connected  = false;
        }
        return  this.isConnected();
    }

    @Override
    public void disconnect() {
        this.session.close();
        super.disconnect();
    }

    //TODO not implemented yet
    public UUID insertFile(String jsonData) throws Exception {
        JSONObject obj = new JSONObject(jsonData);

        this.setPublisherKey(UUID.fromString(obj.getString("key")));
        String key = getPublisherKey().toString();
        String fileContentString = obj.getString("file");
        byte[] file=AIndex.hexStringToByteArray(fileContentString);
        //(byte[])obj.get("file");
        if (file.length==0) throw new Exception("File not found");
        String documentId = obj.getString("documentId");
        String extension = obj.getString("extension");

        UUID hash = fileToCassandra(file,documentId,extension,key);
        return hash;
    }
    private UUID fileToCassandra(byte[] file,String documentId, String extension, String publisherId) throws Exception {
        ByteBuffer data = ByteBuffer.wrap(file);
        UUID guid = UUID.randomUUID();
        try (CqlSession session = CqlSession.builder().build()) {
            PreparedStatement statement = session.prepare(
                    "INSERT INTO dap.fileStore (id,documentId,data,publisherId,extension) VALUES (?,?,?,?,?)"
            );

            BoundStatement boundStatement = statement.bind(
                    guid
                    ,documentId
                    ,data
                    ,getPublisherKey().toString()
                    ,extension);
            session.execute(boundStatement);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
        return guid;
    }
    @Override
    public UUID insertFile(MultipartFile file, String documentId, String extension) throws Exception {
        if (file.isEmpty()) return null;
//        if (this.session == null) this.connect();
        byte[] fileBytes = file.getBytes();
        UUID guid = fileToCassandra(fileBytes,documentId,extension,getPublisherKey().toString());
        return guid;
    }

    @Override
    public byte[] getFile(UUID fileId) {
        if (!this.isConnected()) this.connect();
        if (session == null) session = CqlSession.builder().build();
        String selectQuery = "SELECT data FROM dap.fileStore WHERE id = ? LIMIT 1";
        PreparedStatement selectStmt = session.prepare(selectQuery);
        BoundStatement boundStmt = selectStmt.bind(fileId);
        ResultSet rs = session.execute(boundStmt);

        for (Row row : rs) {
            ByteBuffer byteBuffer = row.getByteBuffer("data");
            byte[] byteArray = Bytes.getArray(byteBuffer);
            return  byteArray;
        }
        return null;
    }

    @Override
    public byte[] getFileByPublishersId( String documentId) {
        if (!this.isConnected()) this.connect();
        if (session == null) session = CqlSession.builder().build();
        String selectQuery = "SELECT data FROM dap.fileStore WHERE publisherId = ? AND documentId = ? LIMIT 1 ALLOW FILTERING";
        PreparedStatement selectStmt = session.prepare(selectQuery);
        BoundStatement boundStmt = selectStmt.bind(getPublisherKey().toString(),documentId);
        ResultSet rs = session.execute(boundStmt);

        for (Row row : rs) {
            ByteBuffer byteBuffer = row.getByteBuffer("data");
            byte[] byteArray = Bytes.getArray(byteBuffer);
            return  byteArray;
        }
        return null;
    }

    @Override
    public HashMap<String, String> getFileInfo(UUID id, String documentId) {
        if (!this.isConnected()) this.connect();
        if (session == null) session = CqlSession.builder().build();
        BoundStatement boundStmt;
        PreparedStatement selectStmt;
        if (id != null) {
            selectStmt = session.prepare("SELECT id, documentId, publisherId,extension FROM dap.fileStore WHERE id = ? LIMIT 1 ALLOW FILTERING ");
            boundStmt = selectStmt.bind(id);
        } else{
            selectStmt = session.prepare("SELECT id, documentId, publisherId,extension FROM dap.fileStore WHERE publisherId = ? AND documentId = ? LIMIT 1 ALLOW FILTERING ");
            boundStmt = selectStmt.bind(getPublisherKey().toString(),documentId);
        }

        ResultSet rs = session.execute(boundStmt);

        HashMap<String, String> map = new HashMap<String, String>();
        for (Row row : rs) {
            map.put("extension",row.getString("extension"));
            map.put("id",row.getUuid("id").toString());
            map.put("documentId",row.getString("documentId"));
            map.put("publisherId",row.getString("publisherId"));
        }
        return map;
    }



}
