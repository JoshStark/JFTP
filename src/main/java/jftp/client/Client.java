package jftp.client;

import jftp.client.auth.UserCredentials;
import jftp.connection.Connection;

public abstract class Client {

	protected String host;
	protected int port;
	
	protected String proxyHostAddress;
	protected int proxyPort;
	
	protected UserCredentials userCredentials = UserCredentials.ANONYMOUS;
	
	/**
	 * 
	 * @param userCredentials
	 */
	public void setCredentials(UserCredentials userCredentials) {
		this.userCredentials = userCredentials;
	}

	/**
	 * 
	 * @param host
	 */
	public void setHost(String host) {
		this.host = host;
	}

	/**
	 * 
	 * @param port
	 */
	public void setPort(int port) {
		this.port = port;
	}
	
	/**
	 * 
	 * @param proxyHostAddress
	 * @param proxyPort
	 */
	public void setProxy(String proxyHostAddress, int proxyPort) {
	    
	    this.proxyHostAddress = proxyHostAddress;
	    this.proxyPort = proxyPort;
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
	
	/**
	 * 
	 */
	public abstract void disconnect();
}
