package GameEngine.Basic.Interfaces;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Quang Tung on 11/23/2015.
 */
public interface FileIO {
    InputStream readAsset(String fileName) throws IOException;

    InputStream readFile(String fileName) throws IOException;

    InputStream readFile(File dir, String fileName) throws IOException;

    OutputStream writeFile(String fileName) throws IOException;

    OutputStream writeFile(File dir, String fileName) throws IOException;

    void deleteFile(File dir, String fileName);

    void makeDirectory(File dir, String directoryName);

    File openDirectory(File dir, String dirName);

    void deleteDirectory(File dir, String dirName);

    String[] listDirectory(File dir);
}
