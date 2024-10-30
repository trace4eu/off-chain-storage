package com.trace4eu.offchain.repository;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.BoundStatement;
import com.datastax.oss.driver.api.core.cql.PreparedStatement;
import com.datastax.oss.driver.api.core.cql.ResultSet;
import com.datastax.oss.driver.api.core.cql.Row;
import com.datastax.oss.protocol.internal.util.Bytes;
import com.trace4eu.offchain.dto.CassandraConnection;
import com.trace4eu.offchain.dto.FileSearchResults;
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

    private CqlSession getSession(){
        if (this.session == null) connect();
        return this.session;
    }


    @Override
    public void disconnect() {
        this.session.close();
        super.disconnect();
    }



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

        if (importData.isPrivate == null ) importData.isPrivate = false;

        UUID hash = fileToCassandra(file,documentId,extension, importData.expirationTime, importData.isPrivate);
        return hash;
    }
    private UUID fileToCassandra(byte[] file,String documentId, String extension, Integer ttl, Boolean isPrivate) throws Exception {
        ByteBuffer data = ByteBuffer.wrap(file);
        UUID guid = UUID.randomUUID();

        if ( isPrivate == null ) isPrivate = false;
        // try (CqlSession session = CqlSession.builder().build()) {
        try {
            PreparedStatement statement;
            String sql = "INSERT INTO ocs.fileStore (id,documentId,data,owner,extension,isPrivate,createdAt) VALUES (?,?,?,?,?,?,toTimestamp(now()))";
            if (!(ttl == null || ttl == 0))
                sql=sql+" USING TTL "+ttl.toString();

            statement = getSession().prepare(sql);

            BoundStatement boundStatement = statement.bind(
                    guid
                    ,documentId
                    ,data
                    ,getOwner()
                    ,extension
                    ,isPrivate);
            session.execute(boundStatement);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
        return guid;
    }
    @Override
    public UUID insertFile(MultipartFile file, String documentId, String extension, Integer ttl, Boolean isPrivate) throws Exception {
        if (file.isEmpty()) return null;
//        if (this.session == null) this.connect();
        byte[] fileBytes = file.getBytes();
        UUID guid = fileToCassandra(fileBytes,documentId,extension, ttl, isPrivate);
        return guid;
    }

    @Override
    public byte[] getFile(UUID fileId) {
//        if (!this.isConnected()) this.connect();
//        if (session == null) session = CqlSession.builder().build();
        String selectQuery = "SELECT data FROM ocs.fileStore WHERE id = ? LIMIT 1";
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
    public FileSearchResults getListOfFilesPaging(String documentId, String owner, Integer pageSize, Integer pageNumber) throws Exception {
        this.session = CassandraConnection.getInstance().getSession();
        String selectQuery;
        PreparedStatement selectStmt;
        if (documentId.isEmpty() && owner.isEmpty())
            throw new Exception("You need to provide a documentid/file name or owner");

//        selectQuery = (!documentId.isEmpty())
//            ? "SELECT id,documentid,owner,extension,isPrivate FROM ocs.fileStore WHERE documentId = ? LIMIT ? ALLOW FILTERING"
//            : "SELECT id,documentid,owner,extension,isPrivate FROM ocs.fileStore WHERE owner = ? LIMIT ? ALLOW FILTERING";
        selectQuery = (!documentId.isEmpty())
            ? "SELECT id,documentid,owner,extension,isPrivate FROM ocs.mv_fileStore_docid WHERE documentId = ? LIMIT ? "
            : "SELECT id,documentid,owner,extension,isPrivate FROM ocs.mv_fileStore_owner WHERE owner = ? LIMIT ? ";

        selectStmt = session.prepare(selectQuery);

        BoundStatement boundStmt = (!documentId.isEmpty())
                ? selectStmt.bind(documentId,(pageNumber+1)*pageSize)
                : selectStmt.bind(owner,(pageNumber+1)*pageSize);

        ResultSet rs = session.execute(boundStmt);

        List<OutputFile> records = new ArrayList<OutputFile>();

        int i =-1;
        for (Row row : rs) {
            i++;
            if (i<pageNumber*pageSize) continue;

            OutputFile outFile = new OutputFile();
            outFile.setId(row.getUuid("id"));
            outFile.setOwner(row.getString("owner"));
            outFile.setDocumentId(row.getString("documentid"));
            outFile.setExtension(row.getString("extension"));
            records.add(outFile);
        }

        Integer total = (!documentId.isEmpty())
            ? this.getFileCountPerDocumentId(documentId)
            : this.getFileCountPerOwner(owner);

        FileSearchResults result = new FileSearchResults();
        result.setFiles(records);
        result.setTotal(total);
        result.setPageSize(pageSize);
        result.setCurrentPage(pageNumber);
        return result;
    }


    public Integer getFileCountPerDocumentId(String documentId){
        //        if (!this.isConnected()) this.connect();
//        if (session == null) session = CqlSession.builder().build();

//        String selectQuery = "SELECT cast(COUNT(id) as int)   as cnt FROM ocs.fileStore WHERE documentId = ? ALLOW FILTERING";
        String selectQuery = "SELECT cast(COUNT(id) as int)   as cnt FROM ocs.mv_fileStore_docid WHERE documentId = ?";

        PreparedStatement selectStmt = session.prepare(selectQuery);

        BoundStatement boundStmt = selectStmt.bind(documentId);

        ResultSet rs = session.execute(boundStmt);

        for (Row row : rs) {
            Integer cnt = row.getInt("cnt");
            return cnt;
        }
        return 0;
    }
    public Boolean fileExists(UUID fileId){
        String selectQuery = "SELECT id FROM ocs.fileStore WHERE id = ?";
        PreparedStatement selectStmt = session.prepare(selectQuery);
        BoundStatement boundStmt = selectStmt.bind(fileId);
        ResultSet rs = session.execute(boundStmt);
        return rs.one() != null;
    }
    public Boolean deleteFile(UUID fileId, String owner){
        if (!fileExists(fileId)) return false;
        String ownerInDb = this.getFileInfo(fileId).get("owner");
        if (ownerInDb != owner) return false;
        String selectQuery = "DELETE FROM ocs.fileStore WHERE Id=? ";
        PreparedStatement selectStmt = session.prepare(selectQuery);
        BoundStatement boundStmt = selectStmt.bind(fileId,owner);
        session.execute(boundStmt);
        return true;
    }
    public Integer getFileCountPerOwner(String owner){
        //        if (!this.isConnected()) this.connect();
//        if (session == null) session = CqlSession.builder().build();

//        String selectQuery = "SELECT cast(count(1) as int) as cnt FROM ocs.fileStore WHERE owner = ? ALLOW FILTERING";
        String selectQuery = "SELECT cast(count(1) as int) as cnt FROM ocs.mv_fileStore_owner WHERE owner = ?";

        PreparedStatement selectStmt = session.prepare(selectQuery);

        BoundStatement boundStmt = selectStmt.bind(owner);

        ResultSet rs = session.execute(boundStmt);

        for (Row row : rs) {
//            BigInteger cnt = row.getBigInteger("cnt");
            Integer cnt = row.getInt("cnt");
            //BigInteger cnt = row.get(0, BigInteger.class);
            return cnt;
        }
        return 0;
    }
    @Override
    public HashMap<String, String> getFileInfo(UUID id) {
//        if (!this.isConnected()) this.connect();
//        if (session == null) session = CqlSession.builder().build();
//        if (session == null) session = CassandraConnection.getInstance().getSession();
        BoundStatement boundStmt;
        PreparedStatement selectStmt;
        selectStmt = session.prepare("SELECT id, isPrivate, documentId, owner,extension FROM ocs.fileStore WHERE id = ? LIMIT 1 ");
        boundStmt = selectStmt.bind(id);

        ResultSet rs = session.execute(boundStmt);

        HashMap<String, String> map = new HashMap<String, String>();
        for (Row row : rs) {
            map.put("extension",row.getString("extension"));
            map.put("id",row.getUuid("id").toString());
            map.put("documentId",row.getString("documentId"));
            map.put("owner",row.getString("owner"));
            map.put("isPrivate",String.valueOf(row.getBoolean("isPrivate")));
        }
        return map;
    }



}
