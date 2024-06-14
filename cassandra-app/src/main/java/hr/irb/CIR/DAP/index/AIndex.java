package hr.irb.CIR.DAP.index;

import java.util.UUID;

public abstract class AIndex implements  IIndex{
    private Integer limit;
    private Integer offset;
    private String nodeUrl;
    private DbOptions options;
    private UUID publisherKey;
    protected boolean connected =false;

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }

    @Override
    public Integer getLimit() {
        return limit;
    }

    @Override
    public Integer getOffset() {
        return offset;
    }

    @Override
    public String getNodeUrl() {
        return nodeUrl;
    }

    @Override
    public void setNodeUrl(String nodeUrl) {
        this.nodeUrl = nodeUrl;
    }

    @Override
    public boolean isConnected() {
        return connected;
    }

    @Override
    public void disconnect() {
        this.connected = false;
    }

    public DbOptions getOptions() {
        return options;
    }

    public void setOptions(DbOptions options) {
        this.options = options;
    }

    @Override
    public UUID getPublisherKey() {
        return publisherKey;
    }

    @Override
    public void setPublisherKey(UUID publisherKey) {
        this.publisherKey = publisherKey;
    }

    public static String convertBytesToHexString(byte[] bytes) {
        StringBuilder hexString = new StringBuilder();
        hexString.append("0x");
        for (byte b : bytes) {
            String hex = Integer.toHexString(0xFF & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }
}
