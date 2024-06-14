package hr.irb.CIR.Auth;

public abstract class AAuth implements IAuth {
    private String login;
    private boolean loggedIn;
    private String userId;

    public String getLogin() {
        return login;
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }

    public void logOut() {
        loggedIn = false;
    }

    public String getUserId() {
        return userId;
    }
}
