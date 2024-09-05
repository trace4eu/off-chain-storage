package com.trace4eu.offchain.dto;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class FileSearchResults {
    private List<OutputFile> records = new ArrayList<OutputFile>();
    private Integer currentPage;
    private Integer total;
    private Integer pageSize;

    public Integer getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
    }



    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public List<OutputFile> getRecords() {
        return records;
    }

    public void setRecords(List<OutputFile> records) {
        this.records = records;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }
}
