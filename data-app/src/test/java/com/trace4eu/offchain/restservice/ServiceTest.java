package com.trace4eu.offchain.restservice;

import com.trace4eu.offchain.GenericHelper;
import com.trace4eu.offchain.Vars;
import com.trace4eu.offchain.dto.PutFileDTO;
import com.trace4eu.offchain.repository.CassandraIndex;
import com.trace4eu.offchain.repository.DbOptions;
import com.trace4eu.offchain.repository.IIndex;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;
//@SpringBootTest
//@TestPropertySource("classpath:application.properties")
public class ServiceTest {
    private DbOptions settings;
    private IIndex indexer;
    private String configurationPath="c:\\Users\\IRB\\Documents\\projekti\\trace4eu\\configCassandra\\config.properties";
//    @Value("${configuration.path}")
//    private String configurationPath;
    @Before
    public void setUp() throws Exception {
        settings = new DbOptions(configurationPath);
        Vars.DB_OPTIONS = settings;
        indexer = new CassandraIndex(settings);
    }

    private UUID insertFile(Integer expiration) throws Exception {
        PutFileDTO importData = new PutFileDTO();

        importData.isPrivate = true;
        importData.expirationTime = expiration;
        importData.file = "aGVsbG8gd29ybGQ=";
        importData.documentId = "someDocumentId";
        importData.owner="some owner";
        importData.extension="txt";
        return indexer.insertFile(importData);
    }

//    @Test
    public void testGetFileContent() throws Exception {
        this.setUp();
        UUID fileId = insertFile(20);
        byte[] expectedFileContent = "hello world".getBytes();
        byte[] fileInStorage = indexer.getFile(fileId);
        assertArrayEquals(expectedFileContent,fileInStorage);
    }

//    @Test
    public void testInsertFile() throws Exception {
        this.setUp();
        UUID fileId = this.insertFile(20);
        assertNotNull(fileId);
        boolean exists = indexer.fileExists(fileId);
        assertTrue(exists);

        Map<String,String> fileInfo = indexer.getFileInfo(fileId);
        assertNotEquals(fileInfo.size(),0);
        assertEquals("txt",fileInfo.get("extension"));
        assertEquals(fileId.toString(), fileInfo.get("id"));
        assertEquals("someDocumentId", fileInfo.get("documentId"));
        assertEquals("some owner", fileInfo.get("owner"));
        assertEquals("true", fileInfo.get("isPrivate"));

        TimeUnit.SECONDS.sleep(20);
        exists = indexer.fileExists(fileId);
        assertFalse(exists);
    }

    //@Test
    public void testConnection() throws Exception {
        this.setUp();
        indexer.connect();
        assertTrue(indexer.isConnected());
    }

//    @Test
    public void testUpload200kb() throws Exception {
        this.setUp();
        indexer.connect();

        int size = 102400;
        StringBuilder sb = new StringBuilder(size);

        for (int i = 0; i < size; i++) {
            sb.append('a');
        }

        String largeString = sb.toString();
        String encodedString = GenericHelper.toHexString(largeString)+"==";

        PutFileDTO importData = new PutFileDTO();

        importData.isPrivate = true;
        importData.expirationTime = 600;
        importData.file = encodedString;
        importData.documentId = "someDocumentId";
        importData.owner="some owner";
        importData.extension="txt";
        UUID uid = indexer.insertFile(importData);

    }

//    @Test
    public void testUpload5MB() throws Exception {
        this.setUp();
        indexer.connect();

        int size = 5*10^6;
        StringBuilder sb = new StringBuilder(size);

        for (int i = 0; i < size; i++) {
            sb.append('a');
        }

        String largeString = sb.toString();
        String encodedString = GenericHelper.toHexString(largeString)+"==";

        PutFileDTO importData = new PutFileDTO();

        importData.isPrivate = true;
        importData.expirationTime = 600;
        importData.file = encodedString;
        importData.documentId = "doc 5MB test";
        importData.owner="some owner";
        importData.extension="txt";
        UUID uid = indexer.insertFile(importData);

    }
}
