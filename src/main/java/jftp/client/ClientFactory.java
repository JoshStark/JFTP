package jftp.client;

public class ClientFactory {

    public enum Protocol {
        FTP, FTPS, SFTP
    }

    public Client createClient(Protocol clientType) {

        if (clientType == Protocol.FTP)
            return new FtpClient();

        if (clientType == Protocol.FTPS)
            return new FtpsClient();

        return new SftpClient();
    }
}
