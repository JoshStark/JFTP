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
    private static final String FILE_SEPARATOR = System.getProperty("file.separator");

    private static final int MILLIS = 1000;

    private ChannelSftp channel;
    private String currentDirectory = ".";

    private FileStreamFactory fileStreamFactory = new FileStreamFactory();

    public SftpConnection(ChannelSftp channel) {
        this.channel = channel;
    }

    @Override
    public void setRemoteDirectory(String directory) {

        try {

            channel.cd(directory);
            currentDirectory = channel.pwd();

        } catch (SftpException e) {

            throw new FtpException(String.format(DIRECTORY_DOES_NOT_EXIST_MESSAGE, directory), e);
        }
    }

    @Override
    public List<FtpFile> listFiles() {

        return listFiles(".");
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<FtpFile> listFiles(String relativePath) {

        List<FtpFile> files = new ArrayList<FtpFile>();

        try {

            Vector<LsEntry> lsEntries = channel.ls(relativePath);

            for (LsEntry entry : lsEntries)
                files.add(toFtpFile(entry));

        } catch (SftpException e) {

            throw new FtpException("Unable to list files in directory " + currentDirectory, e);
        }

        return files;
    }

    @Override
    public void download(FtpFile file, String localDirectory)  throws FtpException {

        try {

            channel.get(file.getName(), localDirectory);

        } catch (SftpException e) {

            throw new FtpException("Unable to download file " + file.getName(), e);
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

        return safeRemotePath + remotePath.getFileSystem().getSeparator() + uploadAs;
    }

    private FtpFile toFtpFile(LsEntry lsEntry) throws SftpException {

        String name = lsEntry.getFilename();
        long fileSize = lsEntry.getAttrs().getSize();
        String fullPath = String.format("%s%s%s", channel.pwd(), FILE_SEPARATOR, lsEntry.getFilename());
        int mTime = lsEntry.getAttrs().getMTime();
        boolean directory = lsEntry.getAttrs().isDir();

        return new FtpFile(name, fileSize, fullPath, (long) mTime * MILLIS, directory);
    }
}
