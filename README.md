JFTP
====

JFTP is a FTP client library that combines the two most well-known FTP libraries to create an all-in-one, simple, way to connect to FTP servers in Java. By using the principle of abstraction and strong interfacing, JFTP should allow you to create and interact with FTP connections, regardless of protocol, with relative ease.

Quick FTP Example
-----------------
```java
Client client = new ClientFactory().createClient(ClientType.FTP);
client.setHost("a.host.name");
client.setPort(21);
client.setCredentials("username", "password");

Connection connection = client.connect();
connection.setRemoteDirectory("files/todownload");
  
List<FtpFile> remoteFiles = connection.listFiles();
  
for (FtpFile file : remoteFiles)
    connection.download(file, "local/file/directory");
    
client.disconnect();
```

***Thanks to:***

JSch http://www.jcraft.com/jsch/

Commons Net FTP: http://commons.apache.org/proper/commons-net/
