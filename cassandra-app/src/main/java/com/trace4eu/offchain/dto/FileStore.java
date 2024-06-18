package com.trace4eu.offchain.dto;

import java.sql.Blob;
import java.util.UUID;

public class FileStore extends OutputFile{
    private Blob data ;

    public Blob getData() {
        return data;
    }

    public void setData(Blob data) {
        this.data = data;
    }
}
