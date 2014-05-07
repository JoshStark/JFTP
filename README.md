[![Build Status](https://travis-ci.org/JAGFin1/JFTP.png?branch=master)](https://travis-ci.org/JAGFin1/JFTP)

JFTP
====

JFTP is an FTP client library that combines the two most well-known FTP libraries to create an all-in-one, simple, way to connect to FTP servers in Java. 

The main goal of JFTP is to give you a way to create and interact with FTP connections, regardless of protocol, with relative ease.

Features
--------

JFTP has the ability to:

- Connect to FTP, FTPS and SFTP servers
- Navigate directories relative to the FTP user on the server
- Download files to a given local directory
- Upload files to a given remote directory
- List all files and directories for a given folder (or current folder) on the server


What is isn't
-------------

JFTP is ***not*** a comprehensive solution to current FTP libraries. It was designed with the sole purpose of being a small and simple to use library that gives nothing other than ***basic*** FTP functionality. The FTP client is set to always use a PASSIVE connection, and the SFTP client is set to always check on passwords over SSH keys.


Quick FTP Example
-----------------
```java
Client client = new ClientFactory().createClient(ClientType.FTP); 
// or new FtpClient(); new SftpClient(); new FtpsClient()

client.setHost("a.host.name");
client.setPort(21);
client.setCredentials(new UserCredentials("username", "password"));

Connection connection = client.connect();
connection.changeDirectory("files/todownload");
  
List<FtpFile> remoteFiles = connection.listFiles();
  
for (FtpFile file : remoteFiles)
    connection.download(file, "local/file/directory");
    
client.disconnect();
```

***Thanks to:***

JSch http://www.jcraft.com/jsch/

Commons Net FTP: http://commons.apache.org/proper/commons-net/
