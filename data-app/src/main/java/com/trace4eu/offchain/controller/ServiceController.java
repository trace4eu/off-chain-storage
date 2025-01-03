package com.trace4eu.offchain.controller;

import java.util.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.trace4eu.offchain.ArgumentService;
import com.trace4eu.offchain.dto.FileSearchResults;
import com.trace4eu.offchain.dto.OutputFileExtended;
import com.trace4eu.offchain.dto.PutFileDTO;
import com.trace4eu.offchain.repository.DbOptions;
import com.trace4eu.offchain.repository.IIndex;
import com.trace4eu.offchain.repository.IndexFactory;
import com.trace4eu.offchain.repository.IndexerType;
import com.trace4eu.offchain.restservice.RestOut;
import com.trace4eu.offchain.GenericHelper;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.MultipartConfigElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController()
public class ServiceController {
	IIndex indexer;
	private DbOptions options;
	@Autowired
	private MultipartConfigElement multipartConfigElement;

	@Autowired
	public ServiceController(ArgumentService myService) throws Exception {
		String[] args = myService.getArgs().getSourceArgs();
		if (args[0].equals("-i")) {
			DbOptions options = new DbOptions(args[1]);
			this.options = options;
//			Vars.DB_OPTIONS = options;
		}
		this.setUp(); //added
	}

	@PostConstruct
	private void setUp() throws Exception {
		if (this.indexer != null) {
			if (!this.indexer.isConnected())
				this.indexer.connect();
			return;
		}

//		DbOptions dbOptions = Vars.DB_OPTIONS;
		indexer = IndexFactory.createIndexer(IndexerType.Cassandra,this.options);
		indexer.connect();
	}

	@PostMapping("/offchain-storage/api/v1/files/delete")
	public ResponseEntity<RestOut> FileDelete(
			@RequestParam(value = "fileId", defaultValue = "") String fileId
			,@RequestParam(value = "owner", defaultValue = "") String owner
	) throws Exception {
//		this.setUp();
		ObjectNode jsonObject = new ObjectMapper().createObjectNode();
		Boolean isDeleted=true;
		try {
			isDeleted = this.indexer.deleteFile(UUID.fromString(fileId), owner);
		} catch (Exception e) {
			jsonObject.put("error", "File is not found");
		}
		if (isDeleted){
			jsonObject.put("isDeleted", isDeleted);
		}else{
			jsonObject.put("error", "File is not found");
		}
		return ResponseEntity.status(HttpStatus.OK)
				.body(new RestOut(jsonObject));
	}
	@PostMapping("/offchain-storage/api/v1/files")
	public ResponseEntity<RestOut> FilePutJson(
			@RequestBody PutFileDTO data
	) throws Exception {
//		this.setUp();
		ObjectNode jsonObject = new ObjectMapper().createObjectNode();
		try  {
			String id = this.indexer.insertFile(data).toString();
			jsonObject.put("id", id);
		} catch (Exception e) {
			jsonObject.put("error", e.getMessage());
		}
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(new RestOut(jsonObject));
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
			,@RequestParam(value = "expirationTime", defaultValue = "0") Integer ttl
			,@RequestParam(value = "isPrivate", defaultValue = "false") Boolean isPrivate
	) throws Exception {
//		this.setUp();
		ObjectNode jsonObject = new ObjectMapper().createObjectNode();
		if (documentId.isEmpty()) {
			jsonObject.put("error", "documentId not received.");
			return new RestOut(jsonObject);
		}else if (owner.isEmpty()) {
			jsonObject.put("error", "owner not received.");
			return new RestOut(jsonObject);
		}

		if (!file.isEmpty()) {
			this.indexer.setOwner(owner);
			String hash = this.indexer.insertFile(file,documentId, extension, ttl, isPrivate).toString();
			jsonObject.put("hash", hash);
		} else {
			jsonObject.put("error", "No file received.");
		}
		return new RestOut(jsonObject);
	}


	/**
	 * get   file from cassandra
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@GetMapping("/offchain-storage/api/v1/files/{id}")
	public ResponseEntity<byte[]> FileGet(
			@PathVariable(required = true) String id
	) throws Exception {
//		this.setUp();
		byte[] fileContent;
		try {
			if (!GenericHelper.isValidUUID(id))
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);

			fileContent = this.indexer.getFile(UUID.fromString(id));

			HashMap<String, String> fileInfo;

			fileInfo = this.indexer.getFileInfo(UUID.fromString(id));

			String filename = fileInfo.get("documentId") + "." + fileInfo.get("extension");

			HttpHeaders headers = new HttpHeaders();
			String ext = fileInfo.get("extension").toLowerCase();
			MediaType mType = GenericHelper.getMediaType(ext);
			headers.setContentType(mType);
			headers.setContentDisposition(ContentDisposition.attachment().filename(filename).build());
			return ResponseEntity.status(HttpStatus.OK)
					.headers(headers)
					.body(fileContent);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		}
	}

	@GetMapping("/offchain-storage/api/v1/files/list")
	public ResponseEntity<FileSearchResults> FilesList(
			@Parameter(description = "owner") @RequestParam(value = "owner", defaultValue = "") String owner
			,@Parameter(description = "documentId") @RequestParam(value = "documentId", defaultValue = "") String documentId
			,@Parameter(description = "page") @RequestParam(value = "page", defaultValue = "0") Integer page
			,@Parameter(description = "pageSize") @RequestParam(value = "pageSize", defaultValue = "20") Integer pageSize
	) throws Exception {
//		this.setUp();

		if (!owner.isEmpty())
			this.indexer.setOwner(owner);

		FileSearchResults files = this.indexer.getListOfFilesPaging(documentId,owner,pageSize,page);

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		return ResponseEntity.ok()
				.headers(headers)
				.body(files);
	}


	@GetMapping("/offchain-storage/api/v1/files/{id}/metadata")
	public ResponseEntity<OutputFileExtended> FileGetMetadata(
			@PathVariable(required = true) String id
	) throws Exception {
//		this.setUp();
		if (!GenericHelper.isValidUUID(id))
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);

		HashMap<String, String> fileInfo = this.indexer.getFileInfo(UUID.fromString(id));

		if (fileInfo.size()==0)
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);

		OutputFileExtended res = new OutputFileExtended();
		res.setId(UUID.fromString(fileInfo.get("id")));
		res.setPrivate(Boolean.parseBoolean(fileInfo.get("isPrivate")));
		res.setDocumentId(fileInfo.get("documentId"));
		res.setOwner(fileInfo.get("owner"));
		res.setExtension(fileInfo.get("extension"));

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		return ResponseEntity.ok()
				.headers(headers)
				.body(res);
	}
}
