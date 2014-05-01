package jftp.connection;

import java.io.FileNotFoundException;
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

    @SuppressWarnings("unchecked")
    @Override
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
    public void download(FtpFile file, String localDirectory) {

        try {

            channel.get(file.getName(), localDirectory);

        } catch (SftpException e) {

            throw new FtpException("Unable to download file " + file.getName(), e);
        }
    }

    @Override
    public void upload(String localFilePath, String remoteDirectory) {
        
        try {
            
            channel.put(fileStreamFactory.createInputStream(localFilePath), remoteDirectory);
            
        } catch (FileNotFoundException e) {
            
            throw new FtpException(String.format(COULD_NOT_FIND_FILE_MESSAGE, localFilePath), e);
        } catch (SftpException e) {
            
            throw new FtpException("Upload failed to complete.", e);
        }
    }

    private FtpFile toFtpFile(LsEntry lsEntry) {

        String name = lsEntry.getFilename();
        long fileSize = lsEntry.getAttrs().getSize();
        String fullPath = String.format("%s%s%s", currentDirectory, FILE_SEPARATOR, lsEntry.getFilename());
        int mTime = lsEntry.getAttrs().getMTime();
        boolean directory = lsEntry.getAttrs().isDir();

        return new FtpFile(name, fileSize, fullPath, (long) mTime * MILLIS, directory);
    }
}
