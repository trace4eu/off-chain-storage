package com.trace4eu.offchain.controller;

import java.util.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.trace4eu.offchain.dto.OutputFile;
import com.trace4eu.offchain.dto.PutFileDTO;
import com.trace4eu.offchain.repository.DbOptions;
import com.trace4eu.offchain.repository.IIndex;
import com.trace4eu.offchain.repository.IndexFactory;
import com.trace4eu.offchain.repository.IndexerType;
import com.trace4eu.offchain.restservice.RestOut;
import hr.irb.CIR.GenericHelper;
import hr.irb.Vars;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController()
public class ServiceController {
	IIndex indexer;
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
		DbOptions dbOptions = Vars.DB_OPTIONS;
		indexer = IndexFactory.createIndexer(IndexerType.Cassandra,dbOptions);
		indexer.connect();
	}


//	@PostMapping("/FilePutJson")
	@PostMapping("/offchain-storage/api/v1/files")
	public RestOut FilePutJson(
			// @RequestBody(value = "jsonData", defaultValue = "") String jsonData
			@RequestBody PutFileDTO data
	) throws Exception {
		this.setUp();
		ObjectNode jsonObject = new ObjectMapper().createObjectNode();
		try  {
			 String hash = this.indexer.insertFile(data).toString();
			//String hash = this.indexer.insertFile("{\"isPrivate\": true,\"owner\":\"onwerId\",\"documentId\":\"documentId3\",\"extension\":\"json\",\"file\":\"fileStringBase64\"}").toString();
			jsonObject.put("hash", hash);
		} catch (Exception e) {
			jsonObject.put("error", e.getMessage());
		}
		return new RestOut(jsonObject);
	}

	/**
	 * put file to CASSANDRA
	 * @param owner
	 * @param file
	 * @return
	 * @throws Exception
	 */
	@PostMapping("/FilePut")
	public RestOut FilePut(
		@RequestParam(value = "owner", defaultValue = "") String owner
		,@RequestParam("file") MultipartFile file
		,@RequestParam(value = "extension", defaultValue = "") String extension
		,@RequestParam(value = "documentId", defaultValue = "") String documentId
	) throws Exception {
		this.setUp();
		ObjectNode jsonObject = new ObjectMapper().createObjectNode();
		if (!file.isEmpty()) {
			this.indexer.setOwner(owner);
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
//	@GetMapping("/FileGet")
	@GetMapping("/offchain-storage/api/v1/files")
	public ResponseEntity<byte[]> FileGet(
			@RequestParam(value = "hash", defaultValue = "") String hash
			,@RequestParam(value = "owner", defaultValue = "") String owner
			,@RequestParam(value = "documentId", defaultValue = "") String documentId
	) throws Exception {
		this.setUp();
		byte[] fileContent;

		if (!owner.isEmpty())
			this.indexer.setOwner(owner);

		fileContent =  (!hash.isEmpty())
			? this.indexer.getFile(UUID.fromString(hash))
			: this.indexer.getFileByOwner(documentId);

		HashMap<String,String> fileInfo;

		fileInfo =(hash.isEmpty())
			? this.indexer.getFileInfo(null, documentId)
			: this.indexer.getFileInfo(UUID.fromString(hash), documentId);

		String filename = fileInfo.get("id")+"."+fileInfo.get("extension");
		HttpHeaders headers = new HttpHeaders();

		String ext = fileInfo.get("extension").toLowerCase();
		MediaType mType = GenericHelper.getMediaType(ext);
		//headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
		headers.setContentType(mType);
		headers.setContentDisposition(ContentDisposition.attachment().filename(filename).build());

		return ResponseEntity.ok()
				.headers(headers)
				.body(fileContent);
	}

	@GetMapping("/offchain-storage/api/v1/files/list")
	public ResponseEntity<List<OutputFile>> FilesList(
			@RequestParam(value = "owner", defaultValue = "") String owner
			,@RequestParam(value = "documentId", defaultValue = "") String documentId
	) throws Exception {
		this.setUp();

		if (!owner.isEmpty())
			this.indexer.setOwner(owner);

		List<OutputFile> files = this.indexer.getListOfFiles(documentId,owner);

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		return ResponseEntity.ok()
				.headers(headers)
				.body(files);
	}
	/**
	 * gets info of file @ CASSANDRA
	 * @param hash
	 * @param owner
	 * @param documentId
	 * @return
	 * @throws Exception
	 */
	@GetMapping("/FileGetInfo")
	public RestOut FileGetInfo(
			@RequestParam(value = "hash", defaultValue = "") String hash
			,@RequestParam(value = "owner", defaultValue = "") String owner
			,@RequestParam(value = "documentId", defaultValue = "") String documentId
	) throws Exception {
		this.setUp();

		ObjectMapper object  = new ObjectMapper();
		ObjectNode jsonObject = object.createObjectNode();
		HashMap<String, String> fileInfo = new HashMap<>();

		if (!hash.isEmpty()){
			fileInfo = this.indexer.getFileInfo(UUID.fromString(hash),null);
		}else{
			this.indexer.setOwner(owner);
			fileInfo = this.indexer.getFileInfo(null,documentId);
		}
		jsonObject.put("id",fileInfo.get("id"));
		jsonObject.put("extension",fileInfo.get("extension"));
		jsonObject.put("owner",fileInfo.get("owner"));
		jsonObject.put("documentId",fileInfo.get("documentId"));
		return new RestOut(jsonObject);
	}
}
