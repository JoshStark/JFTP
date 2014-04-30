package jftp.client.auth;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.Test;


public class UserCredentialsTest {

    private UserCredentials userCredentials;
    
    @Test
    public void credentialsCanBeCreatedAndObtainedOnceInstantiated() {
        
        userCredentials = new UserCredentials("testUser", "testPassword");
        
        assertThat(userCredentials.getUsername(), is(equalTo("testUser")));
        assertThat(userCredentials.getPassword(), is(equalTo("testPassword")));
    }
    
    @Test
    public void staticallyAvailableAnonymousCredentialsShouldContainCorrectValues() {
        
        UserCredentials anonCredentials = UserCredentials.ANONYMOUS;
        
        assertThat(anonCredentials.getUsername(), is(equalTo("anonymous")));
        assertThat(anonCredentials.getPassword(), is(equalTo("jftp@github.com")));
    }
}
