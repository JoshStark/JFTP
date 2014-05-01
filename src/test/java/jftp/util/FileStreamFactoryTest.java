package jftp.util;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.MatcherAssert.assertThat;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class FileStreamFactoryTest {

    private static final String TEST_DOWNLOAD_FILE = "jUnit_Mock_File.txt";
    private static final String FILE_SEPARATOR = System.getProperty("file.separator");
    
    private FileStreamFactory streamFactory = new FileStreamFactory();
    
    private File jUnitTestFile;

    @Before
    public void setUp() throws IOException {
        jUnitTestFile = new File("." + FILE_SEPARATOR + TEST_DOWNLOAD_FILE);
        jUnitTestFile.createNewFile();
    }
    
    @After
    public void tearDown() {
        jUnitTestFile.delete();
    }
    
    @Test
    public void createFileInputStreamShouldReturnStreamOfGivenFile() throws FileNotFoundException {
        
        assertThat(streamFactory.createInputStream(TEST_DOWNLOAD_FILE), instanceOf(FileInputStream.class));
    }
    
    @Test
    public void createFileOutputStreamShouldReturnStreamOfGivenFile() throws FileNotFoundException {
        
        assertThat(streamFactory.createOutputStream(TEST_DOWNLOAD_FILE), instanceOf(FileOutputStream.class));
    }
}
