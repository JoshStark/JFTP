package jftp.client.auth;

public class UserCredentials {

    private static final String ANONYMOUS_USER = "anonymous";
    
    private String username;
    private String password;

    public static final UserCredentials ANONYMOUS = new UserCredentials(ANONYMOUS_USER, "jftp@github.com");
    
    public UserCredentials(String username, String password)
    {
        this.username = username;
        this.password = password;
    }
    
    public String getUsername() {
        return username;
    }
    
    public String getPassword() {
        return password;
    }
}
