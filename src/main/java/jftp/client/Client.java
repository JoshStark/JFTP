package jftp.client;

import jftp.client.auth.UserCredentials;
import jftp.connection.Connection;

public abstract class Client {

	protected String host;
	protected int port;
	
	protected UserCredentials userCredentials = UserCredentials.ANONYMOUS;
	
	public void setCredentials(UserCredentials userCredentials) {
		this.userCredentials = userCredentials;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public void setPort(int port) {
		this.port = port;
	}
	
	/**
	 * Opens a connection to the given host and port.
	 * 
	 * @return
	 * An active connection matching the protocol set by the client. All activity and
	 * communication should be handled using this connection.
	 * 
	 * @throws 
	 * ConnectionInitialisationException
	 */
	public abstract Connection connect();
	
	public abstract void disconnect();
}
