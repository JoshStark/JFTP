package jftp.connection;

import org.joda.time.DateTime;

public class FtpFile {

	private String name;
	private long size;
	private String fullPath;
	private DateTime lastModified;
	private boolean directory;
	
	public FtpFile(String name, long size, String fullPath, long mTime, boolean isDirectory) {

		this.name = name;
		this.size = size;
		this.fullPath = fullPath;
		this.lastModified = new DateTime(mTime);
		this.directory = isDirectory;
	}

	/**
	 * 
	 * @return
	 */
	public String getName() {
		return name;
	}

	/**
	 * 
	 * @return
	 */
	public long getSize() {
		return size;
	}

	/**
	 * 
	 * @return
	 */
	public String getFullPath() {
		return fullPath;
	}

	/**
	 * 
	 * @return
	 */
	public DateTime getLastModified() {
		return lastModified;
	}

	/**
	 * 
	 * @return
	 */
	public boolean isDirectory() {
		return directory;
	}
}
