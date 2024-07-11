package com.trace4eu.offchain.dto;

public class PutFileDTO {
    public String owner;
    public String extension;
    public String documentId;
    public String file; //base64 or hexstring
    public Integer expirationTime;
}
