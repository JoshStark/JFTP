package jftp.client;

public class ClientFactory {

    public enum ClientType {
        FTP, FTPS, SFTP
    }

    public Client createClient(ClientType clientType) {

        if (clientType == ClientType.FTP)
            return new FtpClient();

        if (clientType == ClientType.FTPS)
            return new FtpsClient();

        return new SftpClient();
    }
}
