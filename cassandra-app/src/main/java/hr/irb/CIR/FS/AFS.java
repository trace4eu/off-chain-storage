package hr.irb.CIR.FS;

public abstract class AFS implements IFileSystem {
    private boolean connected = false;
    private String hotsUrl="";

    public boolean isConnected() {
        return connected;
    }

    public void connect() {
        connected = true;
    }

    public void disconnect() {
        connected = false;
    }

    private String hostUrl;

    public void setHostUrl(String hostUrl) {
        this.hostUrl = hostUrl;
    }

    public String getHostUrl() {
        return hostUrl;
    }

    public String getHotsUrl() {
        return hotsUrl;
    }

    public void setHotsUrl(String hotsUrl) {
        this.hotsUrl = hotsUrl;
    }
}
