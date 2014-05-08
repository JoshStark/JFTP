package jftp.connection;

import java.util.List;

import jftp.exception.FtpException;


public interface Connection {

	void changeDirectory(String directory) throws FtpException;

	void download(String remoteFilePath, String localDirectory) throws FtpException;
	
	List<FtpFile> listFiles() throws FtpException;

	List<FtpFile> listFiles(String path) throws FtpException;
	
	String printWorkingDirectory() throws FtpException;

	void upload(String localFilePath, String remoteDirectory) throws FtpException;
}
