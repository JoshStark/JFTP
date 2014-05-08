package jftp.connection;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import jftp.exception.FtpException;
import jftp.util.FileStreamFactory;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.ChannelSftp.LsEntry;
import com.jcraft.jsch.SftpException;

public class SftpConnection implements Connection {

    private static final String COULD_NOT_FIND_FILE_MESSAGE = "Could not find file: %s";
    private static final String DIRECTORY_DOES_NOT_EXIST_MESSAGE = "Directory %s does not exist.";
    private static final String FILE_LISTING_ERROR_MESSAGE = "Unable to list files in directory %s";
    private static final String FILE_SEPARATOR = "/";

    private static final int MILLIS = 1000;

    private ChannelSftp channel;

    private FileStreamFactory fileStreamFactory = new FileStreamFactory();

    public SftpConnection(ChannelSftp channel) {
        this.channel = channel;
    }

    @Override
    public void changeDirectory(String directory) throws FtpException {

        try {

            channel.cd(directory);

        } catch (SftpException e) {

            throw new FtpException(String.format(DIRECTORY_DOES_NOT_EXIST_MESSAGE, directory), e);
        }
    }

    @Override
    public void download(String remoteFilePath, String localDirectory)  throws FtpException {

        try {

            channel.get(remoteFilePath, localDirectory);

        } catch (SftpException e) {

            throw new FtpException("Unable to download file " + remoteFilePath, e);
        }
    }
    
    @Override
    public List<FtpFile> listFiles() throws FtpException {

        return listFiles(printWorkingDirectory());
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<FtpFile> listFiles(String remotePath) throws FtpException {

        try {
            
            List<FtpFile> files = new ArrayList<FtpFile>();

            String originalWorkingDirectory = printWorkingDirectory();
            
            changeDirectory(remotePath);

            String newWorkingDirectory = printWorkingDirectory();

            Vector<LsEntry> lsEntries = channel.ls(newWorkingDirectory);
            
            for (LsEntry entry : lsEntries)
                files.add(toFtpFile(entry, newWorkingDirectory));

            changeDirectory(originalWorkingDirectory);
            
            return files;
            
        } catch (SftpException e) {

            throw new FtpException(String.format(FILE_LISTING_ERROR_MESSAGE, remotePath), e);
        }
    }

    @Override
    public String printWorkingDirectory() throws FtpException {
        
        try {
            
            return channel.pwd();
            
        } catch (SftpException e) {

            throw new FtpException("Unable to print the working directory", e);
        }
    }

    @Override
    public void upload(String localFilePath, String remoteDirectory) throws FtpException {

        try {

            FileInputStream localFileInputStream = fileStreamFactory.createInputStream(localFilePath);

            channel.put(localFileInputStream, determineRemotePath(localFilePath, remoteDirectory));

            localFileInputStream.close();

        } catch (FileNotFoundException e) {

            throw new FtpException(String.format(COULD_NOT_FIND_FILE_MESSAGE, localFilePath), e);
        } catch (SftpException e) {

            throw new FtpException("Upload failed to complete.", e);
        } catch (IOException e) {

            throw new FtpException("Upload may not have completed.", e);
        }
    }

    private String determineRemotePath(String localFilePath, String remoteDirectory) {
        
        Path remotePath = Paths.get(remoteDirectory);

        String safeRemotePath = remotePath.toString();
        String uploadAs = Paths.get(localFilePath).getFileName().toString();

        return safeRemotePath + FILE_SEPARATOR + uploadAs;
    }

    private FtpFile toFtpFile(LsEntry lsEntry, String filePath) throws SftpException {

        String name = lsEntry.getFilename();
        long fileSize = lsEntry.getAttrs().getSize();
        String fullPath = String.format("%s%s%s", filePath, FILE_SEPARATOR, lsEntry.getFilename());
        int mTime = lsEntry.getAttrs().getMTime();
        boolean directory = lsEntry.getAttrs().isDir();

        return new FtpFile(name, fileSize, fullPath, (long) mTime * MILLIS, directory);
    }
}
