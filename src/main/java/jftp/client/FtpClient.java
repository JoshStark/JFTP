package jftp.client;

import java.io.IOException;
import java.net.SocketException;

import jftp.connection.Connection;
import jftp.connection.ConnectionFactory;
import jftp.exception.ClientDisconnectionException;
import jftp.exception.ConnectionInitialisationException;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

public class FtpClient extends Client {

    private static final int FIVE_MINUTES = 300;
    private static final String UNABLE_TO_LOGIN_MESSAGE = "Unable to login for user %s";
    private static final String CONNECTION_ERROR_MESSAGE = "Unable to connect to host %s on port %d";
    private static final String STATUS_ERROR_MESSAGE = "The host %s on port %d returned a bad status code.";

    private ConnectionFactory connectionFactory = new ConnectionFactory();

    protected FTPClient ftpClient;

    public FtpClient() {
        
        ftpClient = new FTPClient();
    }

    public Connection connect() {

        try {

            connectClientAndCheckStatus();
            setSpecificModesOnClient();
            login();

        } catch (IOException e) {
            throw new ConnectionInitialisationException(String.format(CONNECTION_ERROR_MESSAGE, host, port), e);
        }

        return connectionFactory.createFtpConnection(ftpClient);
    }
    
    public void disconnect() {
        
        try {
            
            if (null == ftpClient)
                throw new ClientDisconnectionException("The underlying client was null.");
            
            if (ftpClient.isConnected())
                ftpClient.disconnect();
            
        } catch (IOException e) {
            throw new ClientDisconnectionException("There was an unexpected error while trying to disconnect.", e);
        }
    }

    private void login() throws IOException, ConnectionInitialisationException {

        boolean hasLoggedIn = ftpClient.login(userCredentials.getUsername(), userCredentials.getPassword());

        if (!hasLoggedIn)
            throw new ConnectionInitialisationException(String.format(UNABLE_TO_LOGIN_MESSAGE, userCredentials.getUsername()));
    }

    private void setSpecificModesOnClient() {

        ftpClient.enterLocalPassiveMode();
        ftpClient.setControlKeepAliveTimeout(FIVE_MINUTES);
    }

    private void connectClientAndCheckStatus() throws SocketException, IOException, ConnectionInitialisationException {

        ftpClient.connect(host, port);

        if (!FTPReply.isPositiveCompletion(ftpClient.getReplyCode()))
            throw new ConnectionInitialisationException(String.format(STATUS_ERROR_MESSAGE, host, port));
    }
}
