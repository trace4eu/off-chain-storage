package com.trace4eu.offchain.repository;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.BoundStatement;
import com.datastax.oss.driver.api.core.cql.PreparedStatement;
import com.datastax.oss.driver.api.core.cql.ResultSet;
import com.datastax.oss.driver.api.core.cql.Row;
import com.datastax.oss.protocol.internal.util.Bytes;
import com.trace4eu.offchain.dto.CassandraConnection;
import com.trace4eu.offchain.dto.OutputFile;
import com.trace4eu.offchain.dto.PutFileDTO;
import org.springframework.web.multipart.MultipartFile;

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
        try {
            if (session == null) session = CassandraConnection.getInstance().getSession();
            this.connected  = (session != null) ? true : false;
        } catch (Exception e) {
            this.connected  = false;
        }
        return  this.isConnected();
    }

//    @Override
//    public boolean connect() {
//        String hostName= getOptions().getHostname();
//        if (hostName==null) hostName="localhost";
//        InetSocketAddress node = new InetSocketAddress(getOptions().getHostname(), getOptions().getPort());
//        try {
//            session = CqlSession.builder()
//                    .withKeyspace(getOptions().getDbName())
//                    .addContactPoint(node)
//                    .withLocalDatacenter(getOptions().getClusterName())
//                    .build();
////            if (session == null) session = CqlSession.builder().build();
//            this.connected  = true;
//        } catch (Exception e) {
//            this.connected  = false;
//        }
//        return  this.isConnected();
//    }

    @Override
    public void disconnect() {
        this.session.close();
        super.disconnect();
    }

    //TODO not implemented yet
//    public UUID insertFile(String jsonData) throws Exception {
//        JSONObject obj = new JSONObject(jsonData);
//
//        this.setOwner(obj.getString("owner"));
//        String fileContentString = obj.getString("file");
//        byte[] file=AIndex.hexStringToByteArray(fileContentString);
//        //(byte[])obj.get("file");
//        if (file.length==0) throw new Exception("File not found");
//        String documentId = obj.getString("documentId");
//        String extension = obj.getString("extension");
//
//        UUID hash = fileToCassandra(file,documentId,extension);
//        return hash;
//    }

    public UUID insertFile(PutFileDTO importData) throws Exception {
        this.setOwner(importData.owner);
        if (importData.owner.isEmpty()) throw new Exception("Owner not available");

        String fileContentString = importData.file;
//        byte[] file=AIndex.hexStringToByteArray(fileContentString);
        byte[] file = Base64.getDecoder().decode(fileContentString);

        if (file.length==0) throw new Exception("File content not available");

        String documentId = importData.documentId;
        if (importData.documentId.isEmpty()) throw new Exception("Document Id or file name not available");

        String extension = importData.extension;

        UUID hash = fileToCassandra(file,documentId,extension);
        return hash;
    }
    private UUID fileToCassandra(byte[] file,String documentId, String extension) throws Exception {
        ByteBuffer data = ByteBuffer.wrap(file);
        UUID guid = UUID.randomUUID();
        // try (CqlSession session = CqlSession.builder().build()) {
        try {
            PreparedStatement statement = session.prepare(
                    "INSERT INTO dap.fileStore (id,documentId,data,owner,extension) VALUES (?,?,?,?,?)"
            );

            BoundStatement boundStatement = statement.bind(
                    guid
                    ,documentId
                    ,data
                    ,getOwner()
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
        UUID guid = fileToCassandra(fileBytes,documentId,extension);
        return guid;
    }

    @Override
    public byte[] getFile(UUID fileId) {
//        if (!this.isConnected()) this.connect();
//        if (session == null) session = CqlSession.builder().build();
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
    public byte[] getFileByOwner(String documentId) {
//        if (!this.isConnected()) this.connect();
//        if (session == null) session = CqlSession.builder().build();
        String selectQuery = "SELECT data FROM dap.fileStore WHERE owner = ? AND documentId = ? LIMIT 1 ALLOW FILTERING";
        PreparedStatement selectStmt = session.prepare(selectQuery);
        BoundStatement boundStmt = selectStmt.bind(getOwner(),documentId);
        ResultSet rs = session.execute(boundStmt);

        for (Row row : rs) {
            ByteBuffer byteBuffer = row.getByteBuffer("data");
            byte[] byteArray = Bytes.getArray(byteBuffer);
            return  byteArray;
        }
        return null;
    }

    @Override
    public List<OutputFile> getListOfFiles(String documentId, String owner) throws Exception {
//        if (!this.isConnected()) this.connect();
//        if (session == null) session = CqlSession.builder().build();

        String selectQuery;
        PreparedStatement selectStmt;
        if (documentId.isEmpty() && owner.isEmpty())
            throw new Exception("You need to provide a documentid/file name or owner");

//        Integer cnt = this.getFileCoundPerOwner(owner);
        if (!documentId.isEmpty()){
            selectQuery = "SELECT id,documentid,owner,extension FROM dap.fileStore WHERE documentId = ? ALLOW FILTERING";
        } else {
            selectQuery = "SELECT id,documentid,owner,extension FROM dap.fileStore WHERE owner = ? ALLOW FILTERING";
        }

        selectStmt = session.prepare(selectQuery);

        BoundStatement boundStmt = (!documentId.isEmpty())
                ? selectStmt.bind(documentId)
                : selectStmt.bind(owner);

        ResultSet rs = session.execute(boundStmt);

        List<OutputFile> results = new ArrayList<OutputFile>();
        for (Row row : rs) {
            OutputFile outFile  = new OutputFile();
            outFile.setId(row.getUuid("id"));
            outFile.setOwner(row.getString("owner"));
            outFile.setDocumentid(row.getString("documentid"));
            outFile.setExtension(row.getString("extension"));
            results.add(outFile);
        }

        return results;
    }



    public List<OutputFile> getListOfFilesPaging(String documentId, String owner, Integer pageSize, Integer pageNumber) throws Exception {
        this.session = CassandraConnection.getInstance().getSession();
//        Integer count = this.getFileCountPerOwner(owner);
        String selectQuery;
        PreparedStatement selectStmt;
        if (documentId.isEmpty() && owner.isEmpty())
            throw new Exception("You need to provide a documentid/file name or owner");

        selectQuery = (!documentId.isEmpty())
            ? "SELECT id,documentid,owner,extension FROM dap.fileStore WHERE documentId = ? LIMIT ? ALLOW FILTERING"
            : "SELECT id,documentid,owner,extension FROM dap.fileStore WHERE owner = ? LIMIT ? ALLOW FILTERING";


        selectStmt = session.prepare(selectQuery);

        BoundStatement boundStmt = (!documentId.isEmpty())
                ? selectStmt.bind(documentId,(pageNumber+1)*pageSize)
                : selectStmt.bind(owner,(pageNumber+1)*pageSize);

        ResultSet rs = session.execute(boundStmt);

        List<OutputFile> results = new ArrayList<OutputFile>();
        int i =-1;
        for (Row row : rs) {
            i++;
            if (i<pageNumber*pageSize) continue;
            //if (i >= pageSize*(1+pageNumber)) break;

            OutputFile outFile = new OutputFile();
            outFile.setId(row.getUuid("id"));
            outFile.setOwner(row.getString("owner"));
            outFile.setDocumentid(row.getString("documentid"));
            outFile.setExtension(row.getString("extension"));
            results.add(outFile);

        }

        return results;
    }
    public Integer getFileCountPerDocumentId(String documentId){
        //        if (!this.isConnected()) this.connect();
//        if (session == null) session = CqlSession.builder().build();

        String selectQuery = "SELECT count(id) as cnt FROM dap.fileStore WHERE documentId = ? ALLOW FILTERING";
        PreparedStatement selectStmt = session.prepare(selectQuery);

        BoundStatement boundStmt = selectStmt.bind(documentId);

        ResultSet rs = session.execute(boundStmt);

        for (Row row : rs) {
            Integer cnt = row.getInt("cnt");
            return cnt;
        }
        return 0;
    }
    public Integer getFileCountPerOwner(String owner){
        //        if (!this.isConnected()) this.connect();
//        if (session == null) session = CqlSession.builder().build();

        String selectQuery = "SELECT count(1) as cnt FROM dap.fileStore WHERE owner = ? ALLOW FILTERING";
        PreparedStatement selectStmt = session.prepare(selectQuery);

        BoundStatement boundStmt = selectStmt.bind(owner);

        ResultSet rs = session.execute(boundStmt);

        for (Row row : rs) {
            Integer cnt = row.getInt("cnt");
            return cnt;
        }
        return 0;
    }
    @Override
    public HashMap<String, String> getFileInfo(UUID id, String documentId) {
//        if (!this.isConnected()) this.connect();
//        if (session == null) session = CqlSession.builder().build();
//        if (session == null) session = CassandraConnection.getInstance().getSession();
        BoundStatement boundStmt;
        PreparedStatement selectStmt;
        if (id != null) {
            selectStmt = session.prepare("SELECT id, documentId, owner,extension FROM dap.fileStore WHERE id = ? LIMIT 1 ALLOW FILTERING ");
            boundStmt = selectStmt.bind(id);
        } else{
            selectStmt = session.prepare("SELECT id, documentId, owner,extension FROM dap.fileStore WHERE owner = ? AND documentId = ? LIMIT 1 ALLOW FILTERING ");
            boundStmt = selectStmt.bind(getOwner(),documentId);
        }

        ResultSet rs = session.execute(boundStmt);

        HashMap<String, String> map = new HashMap<String, String>();
        for (Row row : rs) {
            map.put("extension",row.getString("extension"));
            map.put("id",row.getUuid("id").toString());
            map.put("documentId",row.getString("documentId"));
            map.put("owner",row.getString("owner"));
        }
        return map;
    }



}
