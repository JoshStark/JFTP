package jftp.connection;

import java.util.List;


public interface Connection {

	/**
	 * 
	 * @param directory
	 */
	void setRemoteDirectory(String directory);
	
	/**
	 * 
	 * @return
	 */
	List<FtpFile> listFiles();
		
	/**
	 * 
	 * @param file
	 * @param localDirectory
	 */
	void download(FtpFile file, String localDirectory);
}
