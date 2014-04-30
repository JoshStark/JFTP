package jftp.client;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import jftp.connection.Connection;

import org.junit.Test;


public class ClientTest {

    private Client client = new TestableClient();
    
    @Test
    public void usernameAndPasswordShouldDefaultToAnonymousWhenClassInstantiated() {
        
       assertThat(client.userCredentials.getUsername(), is(equalTo("anonymous")));
    }
    
    class TestableClient extends Client {

        @Override
        public Connection connect() {
            return null;
        }

        @Override
        public void disconnect() {
            
        }
    }
}
