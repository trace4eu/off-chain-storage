package hr.irb.CIR.DAP.index;

import hr.irb.CIR.GenericHelper;
import org.json.JSONObject;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.*;
import java.util.*;
@Deprecated
public class PgIndex extends AIndex{
    public PgIndex(DbOptions options){
        this.setOptions(options);
        this.connect();
    }
    private Connection connection;
    //@Override
    public String ingest(String jsonData) throws Exception {
        JSONObject obj = new JSONObject(jsonData);
//        StringBuilder sql = new StringBuilder();
//        System.out.println(obj.getString("name")); //John
        List<String> keys = GenericHelper.stringIterator2List(obj.keys());
        insertKeys(keys);

        String hash = obj.getString("hash");
        String isEbsi =obj.getString("isEbsi");
        String documentId = obj.getString("documentId");
        String descr = obj.getString("descr");
        insertHashes(hash, documentId,Boolean.parseBoolean(isEbsi),descr);

        String publisher = obj.getString("publisher");
        String publisherDescription = obj.getString("publisherDescr");
        insertPublisher(publisher, publisherDescription);

        for (String key : keys) {
            String sql = "insert into doc_metadata(document_id,key_id,data,publisher_id)" +
                    "select" +
                    "    ? as document_id," +
                    "    ,(select id from keys where key_name=?)" +
                    "    ? as data " +
                    "    (select id from publishers where publisher=?) " +
                    "    returning Id" ;
//                    "   ON CONFLICT DO NOTHING";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, documentId);
            preparedStatement.setString(2, key);
            preparedStatement.setString(3, publisher);
            preparedStatement.execute();
        }
        return null;
    }

    //@Override
    public UUID ingest(IndexerFields fields) throws Exception {
        return null;
    }

    public void insertPublisher(String publisher, String descr){
        try{
            String sql = "INSERT INTO publishers (publisher,descr) " +
                    "VALUES (?,?) " +
                    "ON CONFLICT DO NOTHING";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, publisher);
            preparedStatement.setString(2, descr);
            preparedStatement.execute();
        }catch (Exception e){

        }
    }

    private void insertHashes(String hash, String documentUid,boolean isEbsi,String descr){
        String sql = "INSERT INTO hashes (document_id,transaction_id,is_ebsi,descr) " +
            "VALUES (?,?,?,?) " +
            "ON CONFLICT DO NOTHING";
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, hash);
            preparedStatement.setString(2, documentUid);
            preparedStatement.setString(3, isEbsi?"true":"false");
            preparedStatement.setString(4, descr);
            preparedStatement.execute();
        }catch (Exception e){

        }
    }
    private void insertKeys(List<String> keysList)  {
        try{
            for (String key: keysList) {
                String sql = "INSERT INTO keys (key_name) VALUES (?) ON CONFLICT DO NOTHING";
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setString(1, key);
                preparedStatement.execute();
            }
        }catch (Exception e){

        }
    }

    //@Override
    public List<Map<String,String>> search(String searchString) throws Exception {
        String searchQuery = "select document_id, key_id, data " +
                "from doc_metadata" +
                "where document_id in ( " +
                    "SELECT document_id " +
                    "FROM doc_metadata " +
                    "WHERE data ~ ? " +
                    "order by document_id " +
                    "limit ? offset ? " +
                ")  " +
                "order by document_id, key_id " ;
        List<Map<String,String>> resultList = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement(searchQuery)) {
            preparedStatement.setString(1, searchString);
            preparedStatement.setString(2, this.getLimit().toString());
            preparedStatement.setString(3, this.getOffset().toString());
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Map<String,String> row = new HashMap<String,String>();
                    row.put("document_id",resultSet.getString("document_id"));
                    row.put("key_id",resultSet.getString("key_id"));
                    row.put("data",resultSet.getString("data"));
                    resultList.add(row);
                }
            }
        }
        return resultList;
    }

    //@Override
    @Deprecated
    public List<IndexerFields> simpleSearch(String searchString) throws Exception {
        return null;
    }


    @Override
    public boolean connect() {
        try {
            Class.forName("org.postgresql.Driver");
            Connection connection = DriverManager.getConnection(this.getOptions().getUrl(), this.getOptions().getUsername(), this.getOptions().getPassword());
            this.connected = true;
        } catch (Exception e) {
            this.connected = false;
            return false;
        }
        return  true;
    }

    @Override
    @Deprecated
    public UUID insertFile(MultipartFile file, String documentId, String extension) throws Exception
    {
        return null;
    }

    @Override
    @Deprecated
    public byte[] getFile(UUID fileId) {
        return new byte[0];
    }

    @Override
    @Deprecated
    public byte[] getFileByPublishersId( String documentId) {
        return new byte[0];
    }

    @Override
    @Deprecated
    public HashMap<String, String> getFileInfo(UUID id, String documentId) {
        return null;
    }


}
