package jftp.connection;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import jftp.exception.FtpException;
import jftp.util.FileStreamFactory;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

public class FtpConnection implements Connection {

    private static final String COULD_NOT_FIND_FILE_MESSAGE = "Could not find file: %s";
    private static final String FILE_DOWNLOAD_FAILURE_MESSAGE = "Unable to download file %s";
    private static final String FILE_STREAM_OPEN_FAIL_MESSAGE = "Unable to write to local directory %s";
    private static final String FILE_LISTING_ERROR_MESSAGE = "Unable to list files in directory %s";
    private static final String NO_SUCH_DIRECTORY_MESSAGE = "The directory %s doesn't exist on the remote server.";
    private static final String UNABLE_TO_CD_MESSAGE = "Remote server was unable to change directory.";

    private static final String FILE_SEPARATOR = System.getProperty("file.separator");

    private FTPClient client;
    private FileStreamFactory fileStreamFactory = new FileStreamFactory();

    private String currentDirectory = ".";

    public FtpConnection(FTPClient client) {
        this.client = client;
    }

    @Override
    public void setRemoteDirectory(String directory) {

        try {

            boolean success = client.changeWorkingDirectory(directory);

            if (!success)
                throw new FtpException(String.format(NO_SUCH_DIRECTORY_MESSAGE, directory));

            currentDirectory = client.printWorkingDirectory();

        } catch (IOException e) {

            throw new FtpException(UNABLE_TO_CD_MESSAGE, e);
        }
    }

    @Override
    public List<FtpFile> listFiles() {

        return listFiles(".");
    }

    @Override
    public List<FtpFile> listFiles(String relativePath) {

        List<FtpFile> files = new ArrayList<FtpFile>();

        try {

            FTPFile[] ftpFiles = client.listFiles(relativePath);

            for (FTPFile file : ftpFiles)
                files.add(toFtpFile(file));

        } catch (IOException e) {

            throw new FtpException(String.format(FILE_LISTING_ERROR_MESSAGE, relativePath), e);
        }

        return files;
    }

    @Override
    public void download(FtpFile file, String localDirectory) throws FtpException {

        String localDestination = String.format("%s%s%s", localDirectory, FILE_SEPARATOR, file.getName());

        try {

            OutputStream outputStream = fileStreamFactory.createOutputStream(localDestination);

            boolean hasDownloaded = client.retrieveFile(file.getFullPath(), outputStream);

            outputStream.close();

            ensureFileHasSuccessfullyDownloaded(hasDownloaded);

        } catch (FileNotFoundException e) {
            
            throw new FtpException(String.format(FILE_STREAM_OPEN_FAIL_MESSAGE, localDestination), e);

        } catch (IOException e) {
            
            throw new FtpException(String.format(FILE_DOWNLOAD_FAILURE_MESSAGE, file.getName()), e);
        }
    }

    @Override
    public void upload(String localFilePath, String remoteDirectory)  throws FtpException {

        try {

            InputStream localFileInputStream = fileStreamFactory.createInputStream(localFilePath);

            boolean hasUploaded = client.storeFile(determineRemotePath(localFilePath, remoteDirectory), localFileInputStream);

            localFileInputStream.close();

            ensureFileHasSuccessfullyUploaded(hasUploaded);

        } catch (FileNotFoundException e) {
            
            throw new FtpException(String.format(COULD_NOT_FIND_FILE_MESSAGE, localFilePath), e);
        } catch (IOException e) {
            
            throw new FtpException("Upload may not have completed.", e);
        }

    }

    private String determineRemotePath(String localFilePath, String remoteDirectory) {

        Path remotePath = Paths.get(remoteDirectory);

        String safeRemotePath = remotePath.toString();
        String fileName = Paths.get(localFilePath).getFileName().toString();

        return safeRemotePath + remotePath.getFileSystem().getSeparator() + fileName;
    }

    private void ensureFileHasSuccessfullyUploaded(boolean hasUploaded) {

        if (!hasUploaded)
            throw new FtpException("Upload failed.");
    }

    private void ensureFileHasSuccessfullyDownloaded(boolean hasDownloaded) {

        if (!hasDownloaded)
            throw new FtpException("Server returned failure while downloading.");
    }

    private FtpFile toFtpFile(FTPFile ftpFile) {

        String name = ftpFile.getName();
        long fileSize = ftpFile.getSize();
        String fullPath = String.format("%s%s%s", currentDirectory, FILE_SEPARATOR, ftpFile.getName());
        long mTime = ftpFile.getTimestamp().getTime().getTime();
        boolean isDirectory = ftpFile.isDirectory();

        return new FtpFile(name, fileSize, fullPath, mTime, isDirectory);
    }
}
