package jftp.client;

import java.io.IOException;
import java.net.SocketException;

import jftp.connection.Connection;
import jftp.connection.ConnectionFactory;
import jftp.exception.FtpException;

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
            throw new FtpException(String.format(CONNECTION_ERROR_MESSAGE, host, port), e);
        }

        return connectionFactory.createFtpConnection(ftpClient);
    }
    
    public void disconnect() {
        
        try {
            
            if (null == ftpClient)
                throw new FtpException("The underlying client was null.");
            
            if (ftpClient.isConnected())
                ftpClient.disconnect();
            
        } catch (IOException e) {
            throw new FtpException("There was an unexpected error while trying to disconnect.", e);
        }
    }

    private void connectClientAndCheckStatus() throws SocketException, IOException, FtpException {

        ftpClient.connect(host, port);

        if (!FTPReply.isPositiveCompletion(ftpClient.getReplyCode()))
            throw new FtpException(String.format(STATUS_ERROR_MESSAGE, host, port));
    }

    private void login() throws IOException, FtpException {

        boolean hasLoggedIn = ftpClient.login(userCredentials.getUsername(), userCredentials.getPassword());

        if (!hasLoggedIn)
            throw new FtpException(String.format(UNABLE_TO_LOGIN_MESSAGE, userCredentials.getUsername()));
        
        ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
    }

    private void setSpecificModesOnClient() throws IOException {

        ftpClient.enterLocalPassiveMode();
        ftpClient.setControlKeepAliveTimeout(FIVE_MINUTES);
        ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
    }
}
