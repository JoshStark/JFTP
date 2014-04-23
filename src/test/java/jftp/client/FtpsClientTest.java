package jftp.client;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;

import org.apache.commons.net.ftp.FTPSClient;
import org.junit.Test;

public class FtpsClientTest {

    private FtpClient client = new FtpsClient();
    
    @Test
    public void newFtpsClientShouldCreateFTPSClientInstance() {
        
        assertThat(client.ftpClient, instanceOf(FTPSClient.class));
    }
}
