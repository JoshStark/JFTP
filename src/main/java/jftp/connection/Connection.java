package jftp.connection;

import java.util.List;


public interface Connection {

	/**
	 * Equivalent of a standard 'cd' on a directory. This will set the current working directory
	 * on the active connection to the given file path.
	 * 
	 * @param 
	 * directory - The directory to change to. Equivalent of a 'cd' command.
	 */
	void setRemoteDirectory(String directory);
	
	/**
	 * Lists all files and directories under the current working directory.
	 * 
	 * @return 
	 * A list of FtpFiles detailing what is in the current working directory.
	 */
	List<FtpFile> listFiles();
		
	/**
	 * Lists all files in the given path. This will accept either relative or absolute paths.
	 * 
	 * E.g. If currently in /foo - listFiles('bar') will list files in /foo/bar.
	 * E.g. If currently in /foo - listFiles('/bar') will list files in /bar.
	 * 
	 * @param 
	 * path
	 * 
	 * @return 
	 * A list of FtpFiles within relative path.
	 */
	List<FtpFile> listFiles(String path);
	
	/**
	 * Downloads the given file to the given local directory.
	 * 
	 * @param 
	 * file File/Directory to download
	 * 
	 * @param 
	 * localDirectory Local directory to download item to.
	 */
	void download(FtpFile file, String localDirectory);
	
	/**
	 * Uploads the given file/directory to the given local directory.
	 * 
	 * @param 
	 * localFilePath Local file/directory to upload.
	 * 
	 * @param 
	 * remoteDirectory
	 */
	void upload(String localFilePath, String remoteDirectory);
}
