package jftp.client;

import jftp.connection.Connection;
import jftp.connection.ConnectionFactory;
import jftp.exception.FtpException;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

public class SftpClient extends Client {

	private static final String SFTP = "sftp";
	private static final String CONNECTION_ERROR_MESSAGE = "Unable to connect to host %s on port %d";

	private JSch jsch;
	private ConnectionFactory connectionFactory;

	private Session session;
	private Channel channel;
	
	public SftpClient() {
		this.jsch = new JSch();
		this.connectionFactory = new ConnectionFactory();
	}

	public Connection connect() {

		session = null;
		channel = null;

		try {

			configureSessionAndConnect();
			openChannelFromSession();

		} catch (JSchException e) {
			throw new FtpException(String.format(CONNECTION_ERROR_MESSAGE, host, port), e);
		}

		return connectionFactory.createSftpConnection(channel);
	}
	
	public void disconnect() {
	    
	    if(null == channel || null == session)
	        throw new FtpException("The underlying connection was never initially made.");
	    
	    channel.disconnect();
	    session.disconnect();
	}

    private void openChannelFromSession() throws JSchException {
        
        channel = session.openChannel(SFTP);
        channel.connect();
    }

    private void configureSessionAndConnect() throws JSchException {
        
        session = jsch.getSession(userCredentials.getUsername(), host, port);
        session.setConfig("StrictHostKeyChecking", "no");
        session.setPassword(userCredentials.getPassword());

        session.connect();
    }
}
