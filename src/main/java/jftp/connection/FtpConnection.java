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

    public FtpConnection(FTPClient client) {
        this.client = client;
    }

    @Override
    public void changeDirectory(String directory) throws FtpException {

        try {

            boolean success = client.changeWorkingDirectory(directory);

            if (!success)
                throw new FtpException(String.format(NO_SUCH_DIRECTORY_MESSAGE, directory));

        } catch (IOException e) {

            throw new FtpException(UNABLE_TO_CD_MESSAGE, e);
        }
    }

    @Override
    public void download(String remoteFilePath, String localDirectory) throws FtpException {

        String localDestination = determinePath(remoteFilePath, localDirectory);

        try {

            OutputStream outputStream = fileStreamFactory.createOutputStream(localDestination);

            boolean hasDownloaded = client.retrieveFile(remoteFilePath, outputStream);

            outputStream.close();

            ensureFileHasSuccessfullyDownloaded(hasDownloaded);

        } catch (FileNotFoundException e) {

            throw new FtpException(String.format(FILE_STREAM_OPEN_FAIL_MESSAGE, localDestination), e);

        } catch (IOException e) {

            throw new FtpException(String.format(FILE_DOWNLOAD_FAILURE_MESSAGE, remoteFilePath), e);
        }
    }

    @Override
    public List<FtpFile> listFiles() throws FtpException {

        return listFiles(printWorkingDirectory());
    }

    @Override
    public List<FtpFile> listFiles(String remotePath) throws FtpException {

        List<FtpFile> files = new ArrayList<FtpFile>();

        try {

            String originalWorkingDirectory = printWorkingDirectory();

            changeDirectory(remotePath);

            String newWorkingDirectory = printWorkingDirectory();

            FTPFile[] ftpFiles = client.listFiles(newWorkingDirectory);

            for (FTPFile file : ftpFiles)
                files.add(toFtpFile(file, newWorkingDirectory));

            changeDirectory(originalWorkingDirectory);

        } catch (IOException e) {

            throw new FtpException(String.format(FILE_LISTING_ERROR_MESSAGE, remotePath), e);
        }

        return files;
    }

    @Override
    public String printWorkingDirectory() throws FtpException {

        try {

            return client.printWorkingDirectory();

        } catch (IOException e) {

            throw new FtpException("Unable to print the working directory", e);
        }
    }

    @Override
    public void upload(String localFilePath, String remoteDirectory) throws FtpException {

        try {

            InputStream localFileInputStream = fileStreamFactory.createInputStream(localFilePath);

            boolean hasUploaded = client.storeFile(determinePath(localFilePath, remoteDirectory), localFileInputStream);

            localFileInputStream.close();

            ensureFileHasSuccessfullyUploaded(hasUploaded);

        } catch (FileNotFoundException e) {

            throw new FtpException(String.format(COULD_NOT_FIND_FILE_MESSAGE, localFilePath), e);
        } catch (IOException e) {

            throw new FtpException("Upload may not have completed.", e);
        }
    }

    private String determinePath(String sourcePathWithName, String targetPathWithoutName) {

        Path targetPath = Paths.get(targetPathWithoutName);

        String safePath = targetPath.toString();
        String fileName = Paths.get(sourcePathWithName).getFileName().toString();

        return safePath + targetPath.getFileSystem().getSeparator() + fileName;
    }

    private void ensureFileHasSuccessfullyDownloaded(boolean hasDownloaded) {

        if (!hasDownloaded)
            throw new FtpException("Server returned failure while downloading.");
    }

    private void ensureFileHasSuccessfullyUploaded(boolean hasUploaded) {

        if (!hasUploaded)
            throw new FtpException("Upload failed.");
    }

    private FtpFile toFtpFile(FTPFile ftpFile, String filePath) throws IOException {

        String name = ftpFile.getName();
        long fileSize = ftpFile.getSize();
        String fullPath = filePath + FILE_SEPARATOR + ftpFile.getName();
        long mTime = ftpFile.getTimestamp().getTime().getTime();
        boolean isDirectory = ftpFile.isDirectory();

        return new FtpFile(name, fileSize, fullPath, mTime, isDirectory);
    }
}
