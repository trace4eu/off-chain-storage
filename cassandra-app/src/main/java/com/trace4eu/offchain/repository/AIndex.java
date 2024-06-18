package com.trace4eu.offchain.repository;

public abstract class AIndex implements  IIndex{
    private Integer limit;
    private Integer offset;
    private String nodeUrl;
    private DbOptions options;
    private String owner;
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
    public String getOwner() {
        return owner;
    }

    @Override
    public void setOwner(String owner) {
        this.owner = owner;
    }

    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i+1), 16));
        }
        return data;
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
