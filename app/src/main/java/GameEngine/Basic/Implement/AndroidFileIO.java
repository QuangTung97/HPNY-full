package GameEngine.Basic.Implement;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Environment;
import android.util.Log;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import GameEngine.Basic.Interfaces.FileIO;


/**
 * Created by Quang Tung on 11/23/2015.
 */
public class AndroidFileIO implements FileIO {
    Context context;
    AssetManager asset;
    File internal;

    public AndroidFileIO(Context context) {
        this.context = context;
        this.asset = context.getAssets();
        internal = context.getFilesDir();
    }

    @Override
    public InputStream readAsset(String fileName) throws IOException {
        return asset.open(fileName);
    }

    @Override
    public InputStream readFile(String fileName) throws IOException {
        return context.openFileInput(fileName);
    }

    @Override
    public InputStream readFile(File dir, String fileName) throws IOException {
        if (dir == null) {
            return context.openFileInput(fileName);
        }

        File file = new File(dir, fileName);
        return new FileInputStream(file);
    }

    @Override
    public OutputStream writeFile(String fileName) throws IOException {
        return context.openFileOutput(fileName, context.MODE_PRIVATE);
    }

    @Override
    public OutputStream writeFile(File dir, String fileName) throws IOException {
        if (dir == null) {
            return context.openFileOutput(fileName, context.MODE_PRIVATE);
        }

        File file = new File(dir, fileName);
        if (!file.exists())
            file.createNewFile();

        return new FileOutputStream(file);
    }

    @Override
    public void deleteFile(File dir, String fileName) {
        if (dir == null) {
            File file = new File(context.getFilesDir(), fileName);
            file.delete();
        }

        File file = new File(dir, fileName);
        file.delete();
    }

    @Override
    public void makeDirectory(File dir, String directoryName) {
        if (dir == null) {
            File file = new File(context.getFilesDir(), directoryName);
            file.mkdir();
            return;
        }

        File file = new File(dir, directoryName);
        file.mkdir();
    }

    @Override
    public File openDirectory(File dir, String dirName) {
        if (dir == null) {
            File file = new File(context.getFilesDir(), dirName);
            return file;
        }

        File file = new File(dir, dirName);
        return file;
    }

    @Override
    public void deleteDirectory(File dir, String dirName) {
        if (dir == null) {
            File file = new File(context.getFilesDir(), dirName);
            file.delete();
        }

        File file = new File(dir, dirName);
        file.delete();
    }

    @Override
    public String[] listDirectory(File dir) {
        if (dir == null) {
            return context.getFilesDir().list();
        }

        return dir.list();
    }
}
