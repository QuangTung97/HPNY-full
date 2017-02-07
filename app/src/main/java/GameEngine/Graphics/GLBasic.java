package GameEngine.Graphics;

import android.content.Context;
import android.content.res.AssetManager;

import java.io.IOException;
import java.io.InputStream;


/**
 * Created by Quang Tung on 12/15/2015.
 */

/**
 * Read Files in Assets
 * With funtions read integer and float variable
 * from file through InputStream
 * */

public class GLBasic {
    private static AssetManager asset;

    public GLBasic(Context context) {
        asset = context.getAssets();
    }

    public static InputStream readAsset(String fileName) throws IOException{
        return asset.open(fileName);
    }

    public static int readInt(InputStream in) throws IOException{
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
}
