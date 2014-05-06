package jftp.connection;

import java.util.List;

import jftp.exception.FtpException;


public interface Connection {

	/**
	 * Equivalent of a standard 'cd' on a directory. This will set the current working directory
	 * on the active connection to the given file path.
	 * 
	 * @param 
	 * directory - The directory to change to. Equivalent of a 'cd' command.
     *  
     * @throws
     * FtpException
	 */
	void changeDirectory(String directory);
	
	/**
	 * Reads out the current directory on the server.
	 * 
	 * @return Absolute path purporting to the current working directory.
	 */
	String printWorkingDirectory();
	
	/**
	 * Lists all files and directories under the current working directory.
	 * 
	 * @return 
	 * A list of FtpFiles detailing what is in the current working directory.
     *  
     * @throws
     * FtpException
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
	 *
     * @throws
     * FtpException
	 */
	List<FtpFile> listFiles(String path);
	
	/**
	 * Downloads the given file to the given local directory.
	 * 
	 * @param 
	 * file file/directory to download
	 * 
	 * @param 
	 * localDirectory Local directory to download item to. This should NOT include the file name as this method
	 * will handle the creation of the file for the physical download. 
	 * 
     * @throws
     * FtpException
	 */
	void download(FtpFile file, String localDirectory);
	
	/**
	 * Uploads the given file/directory to the given local directory.
	 * 
	 * @param 
	 * localFilePath Local file/directory to upload.
	 * 
	 * @param 
	 * remoteDirectory This is the remote directory that the local file will be uploaded to. The path supplied must be
	 * a valid directory (not including file name).
	 *  
     * @throws
     * FtpException
	 */
	void upload(String localFilePath, String remoteDirectory);
}
