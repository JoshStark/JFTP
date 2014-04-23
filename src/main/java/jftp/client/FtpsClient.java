package jftp.client;

import org.apache.commons.net.ftp.FTPSClient;

public class FtpsClient extends FtpClient {

    private static final String SSL = "SSL";

    public FtpsClient() {
        
        ftpClient = new FTPSClient(SSL, true);
    }
}
