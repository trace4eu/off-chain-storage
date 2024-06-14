package hr.irb.CIR.DAP.restservice;

import java.util.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import hr.irb.CIR.Auth.LookupAuth;
import hr.irb.CIR.DAP.index.*;
import hr.irb.Vars;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class ServiceController {
	IIndex indexer;
	LookupAuth auth = new LookupAuth(null);
	private void setUp() throws Exception {

		if (this.indexer != null) {
			if (!this.indexer.isConnected())
				this.indexer.connect();
			return;
		}

//		DbOptions dbOptions = new DbOptions();
//		dbOptions.setDbName("dap");
//		dbOptions.setHostname("localhost");
//		dbOptions.setUrl("http://localhost/dap"); //neznam port koji koristi cassandra
		DbOptions dbOptions = Vars.CASSANDRA_DB_OPTIONS;
		indexer = IndexFactory.createIndexer(IndexerType.Cassandra,dbOptions);
		indexer.connect();
	}

	/**
	 * put file to CASSANDRA
	 * @param key
	 * @param file
	 * @return
	 * @throws Exception
	 */
	@PostMapping("/FilePut")
	public RestOut FilePut(
		@RequestParam(value = "key", defaultValue = "") String key
		,@RequestParam("file") MultipartFile file
		,@RequestParam(value = "extension", defaultValue = "") String extension
		,@RequestParam(value = "documentId", defaultValue = "") String documentId
	) throws Exception {
		this.setUp();
		ObjectNode jsonObject = new ObjectMapper().createObjectNode();
		if (!file.isEmpty()) {
			this.indexer.setPublisherKey(UUID.fromString(key));
			String hash = this.indexer.insertFile(file,documentId, extension).toString();
			jsonObject.put("hash", hash);
		} else {
			jsonObject.put("error", "No file received.");
		}
		return new RestOut(jsonObject);
	}

	/**
	 * get   file from cassandra
	 * @param hash
	 * @return
	 * @throws Exception
	 */
	@GetMapping("/FileGet")
	public ResponseEntity<byte[]> FileGet(
			@RequestParam(value = "hash", defaultValue = "") String hash
			,@RequestParam(value = "key", defaultValue = "") String key
			,@RequestParam(value = "documentId", defaultValue = "") String documentId
	) throws Exception {
		this.setUp();
		byte[] fileContent;

		if (!key.isEmpty())
			this.indexer.setPublisherKey(UUID.fromString(key));

		fileContent =  (!hash.isEmpty())
			? this.indexer.getFile(UUID.fromString(hash))
			: this.indexer.getFileByPublishersId(documentId);

		HashMap<String,String> fileInfo;

		fileInfo =(hash.isEmpty())
			? this.indexer.getFileInfo(null, documentId)
			: this.indexer.getFileInfo(UUID.fromString(hash), documentId);

		String filename = fileInfo.get("id")+"."+fileInfo.get("extension");
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
		headers.setContentDisposition(ContentDisposition.attachment().filename(filename).build());

		return ResponseEntity.ok()
				.headers(headers)
				.body(fileContent);
	}

	/**
	 * gets info of file @ CASSANDRA
	 * @param hash
	 * @param key
	 * @param documentId
	 * @return
	 * @throws Exception
	 */
	@GetMapping("/FileGetInfo")
	public RestOut FileGetInfo(
			@RequestParam(value = "hash", defaultValue = "") String hash
			,@RequestParam(value = "key", defaultValue = "") String key
			,@RequestParam(value = "documentId", defaultValue = "") String documentId
	) throws Exception {
		this.setUp();

		ObjectMapper object  = new ObjectMapper();
		ObjectNode jsonObject = object.createObjectNode();
		HashMap<String, String> fileInfo = new HashMap<>();

		if (!hash.isEmpty()){
			fileInfo = this.indexer.getFileInfo(UUID.fromString(hash),null);
		}else{
			this.indexer.setPublisherKey(UUID.fromString(key));
			fileInfo = this.indexer.getFileInfo(null,documentId);
		}
		jsonObject.put("id",fileInfo.get("id"));
		jsonObject.put("extension",fileInfo.get("extension"));
		jsonObject.put("publisherId",fileInfo.get("publisherId"));
		jsonObject.put("documentId",fileInfo.get("documentId"));
		return new RestOut(jsonObject);
	}
}
