package jftp.client;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import jftp.client.ClientFactory;
import jftp.client.FtpClient;
import jftp.client.SftpClient;
import jftp.client.ClientFactory.Protocol;

import org.junit.Test;


public class ClientFactoryTest {

	private ClientFactory factory = new ClientFactory();
	
	@Test
	public void factoryShouldReturnNewStfpClientWhenSwitchStringIsSftp() {
		assertThat(factory.createClient(Protocol.SFTP), is(instanceOf(SftpClient.class)));
	}

	@Test
	public void factoryShouldReturnNewFtpClientWhenSwitchedToFtp() {
		assertThat(factory.createClient(Protocol.FTP), is(instanceOf(FtpClient.class)));
	}
	
	@Test
	public void factoryShouldReturnNewFtpsClientWhenSwitchedToFtps() {
	    assertThat(factory.createClient(Protocol.FTPS), is(instanceOf(FtpsClient.class)));
	}
}
