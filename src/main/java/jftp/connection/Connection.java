package jftp.connection;

import java.io.File;
import java.util.List;


public interface Connection {

	/**
	 * Sets the working directory on the server.
	 * 
	 * @param directory - The directory to change to. Equivalent of a 'cd' command.
	 */
	void setRemoteDirectory(String directory);
	
	/**
	 * Lists all files and directories under the current working directory.
	 * 
	 * @return A list of FtpFiles.
	 */
	List<FtpFile> listFiles();
		
	/**
	 * Lists all files in the given relative path, based on current working directory.
	 * 
	 * E.g. If currently in /foo - listFiles('bar') will list files in /foo/bar.
	 * 
	 * @param relativePath
	 * @return A list of FtpFiles within relative path.
	 */
	List<FtpFile> listFiles(String relativePath);
	
	/**
	 * Downloads the given file/directory to the given local directory.
	 * 
	 * @param file - File/Directory to download
	 * @param localDirectory - Local directory to download item to.
	 */
	void download(FtpFile file, String localDirectory);
	
	/**
	 * Uploads the given file/directory to the given local directory.
	 * 
	 * @param file - Local file/directory to upload.
	 * @param remoteDirectory
	 */
	void upload(File file, String remoteDirectory);
}
