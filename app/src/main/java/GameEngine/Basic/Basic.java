package GameEngine.Basic;

import android.app.Activity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import GameEngine.Basic.Implement.AndroidAudio;
import GameEngine.Basic.Implement.AndroidFileIO;
import GameEngine.Basic.Implement.AndroidInput;
import GameEngine.Basic.Interfaces.Audio;
import GameEngine.Basic.Interfaces.FileIO;
import GameEngine.Basic.Interfaces.Input;
import GameEngine.Basic.Interfaces.Music;
import GameEngine.Basic.Interfaces.Sound;

/**
 * Created by Quang Tung on 12/20/2015.
 */
public class Basic {

    public Basic(Activity activity, View view, float scaleX, float scaleY) {
        fileIO = new AndroidFileIO(activity);
        input = new AndroidInput(activity, view, scaleX, scaleY);
        audio = new AndroidAudio(activity);
        this.activity = activity;
        this.view = view;

        imm = (InputMethodManager)activity.getSystemService(activity.INPUT_METHOD_SERVICE);
    }

    public static void setFullScreen() {
        activity.requestWindowFeature(Window.FEATURE_NO_TITLE);
        activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        activity.setContentView(view);
    }

    /**
     * Input
     * */
    /*public static List<Input.KeyEvent> getKeyEvents() {
        return input.getKeyEvents();
    }*/

    public static List<Input.TouchEvent> getTouchEvents() {
        return input.getTouchEvents();
    }

    //Accelerometer
    public static float accel_getX() {
        return input.getAccelX();
    }

    public static float accel_getY() {
        return input.getAccelY();
    }

    public static float accel_getZ() {
        return input.getAccelZ();
    }

    public static void showSoftKeyboard() {
        imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
    }

    public static void hideSoftKeyboard() {
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    /**
     * Audio
     * */
    //Music
    public static Music newMusic(String fileName) {
        return audio.newMusic(fileName);
    }

    //Sound
    public static Sound newSound(String fileName) {
        return audio.newSound(fileName);
    }


    /**
     * File IO
     * */
    public static InputStream readAsset(String fileName)throws IOException{
        return fileIO.readAsset(fileName);
    }

    public static InputStream readFile(String fileName) throws IOException{
        return fileIO.readFile(fileName);
    }

    public static InputStream readFile(File dir, String fileName) throws IOException {
        return fileIO.readFile(dir, fileName);
    }

    public static OutputStream writeFile(String fileName) throws IOException {
        return fileIO.writeFile(fileName);
    }

    public static OutputStream writeFile(File dir, String fileName) throws IOException {
        return fileIO.writeFile(dir, fileName);
    }

    public static void deleteFile(File dir, String fileName) {
        fileIO.deleteFile(dir, fileName);
    }

    public static void makeDirectory(File dir, String dirName) {
        fileIO.makeDirectory(dir, dirName);
    }

    public static File openDirectory(File dir, String dirName) {
        return fileIO.openDirectory(dir, dirName);
    }

    public static void deleteDirectory(File dir, String dirName) {
        fileIO.deleteDirectory(dir, dirName);
    }

    public static String[] listDirectory(File dir) {
        return fileIO.listDirectory(dir);
    }

    public static int readInt(InputStream in) throws IOException {
        int a0 = in.read();
        int a8 = in.read();
        int a16 = in.read();
        int a24 = in.read();

        return ((a24 & 0xff) << 24) | ((a16 & 0xff) << 16) |
                ((a8 & 0xff) << 8) | ((a0 & 0xff));
    }

    public static float readFloat(InputStream in) throws IOException {
        int a0 = in.read();
        int a8 = in.read();
        int a16 = in.read();
        int a24 = in.read();

        int result = ((a24 & 0xff) << 24) | ((a16 & 0xff) << 16) |
                ((a8 & 0xff) << 8) | ((a0 & 0xff));
        return Float.intBitsToFloat(result);
    }

    public static void writeInt(OutputStream out, int number) throws IOException {
        int a0 = number & 0xff;
        int a8 = (number & 0xff00) >> 8;
        int a16 = (number & 0xff0000) >> 16;
        int a24 = (number & 0xff000000) >> 24;

        out.write(a0);
        out.write(a8);
        out.write(a16);
        out.write(a24);
    }

    public static void writeFloat(OutputStream out, float num) throws IOException {
        int number = Float.floatToIntBits(num);

        int a0 = number & 0xff;
        int a8 = (number & 0xff00) >> 8;
        int a16 = (number & 0xff0000) >> 16;
        int a24 = (number & 0xff000000) >> 24;

        out.write(a0);
        out.write(a8);
        out.write(a16);
        out.write(a24);
    }

    /**
     * Variables
     * */
    private static Input input;
    private static FileIO fileIO;
    private static Audio audio;
    private static Activity activity;
    private static View view;
    private static InputMethodManager imm;
}
