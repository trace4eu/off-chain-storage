package hr.irb.CIR.DAP.index;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class IndexerFields {
    private UUID id;
    private String documentId;
    private UUID  publisherKey;
    private String title;
    private String abstr;
    private Set<String> keywords;
    private String authors;
    private String fileUrl;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public UUID getPublisherKey() {
        return publisherKey;
    }

    public void setPublisherKey(UUID publisherKey) {
        this.publisherKey = publisherKey;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAbstr() {
        return abstr;
    }

    public void setAbstr(String abstr) {
        this.abstr = abstr;
    }

    public Set<String> getKeywords() {
        return keywords;
    }

    public void setKeywords(Set<String> keywords) {
        this.keywords = keywords;
    }

    public String getAuthors() {
        return authors;
    }

    public void setAuthors(String authors) {
        this.authors = authors;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }
}
