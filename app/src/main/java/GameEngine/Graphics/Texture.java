package GameEngine.Graphics;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLUtils;

import java.io.IOException;
import java.io.InputStream;

import GameEngine.Basic.ListHead;


/**
 * Created by Quang Tung on 12/4/2015.
 */
public class Texture {
    public static boolean allowInitialize = true;

    public ListHead<Texture> list = new ListHead<>(this);

    public static final int NEAREST = 0;
    public static final int LINEAR = 1;

    public int textureId; // texture id in openGL
    int minFilter; // min filter for texture
    int magFilter; // mag filter for texture
    int width; // width of image loaded
    int height; // height of image loaded

    private Bitmap bitmap;


    public Texture(String fileName, int minFilter, int magFilter) {
        if (!allowInitialize)
            return;

        if (minFilter == NEAREST)
            this.minFilter = GLES20.GL_NEAREST;
        else
            this.minFilter = GLES20.GL_LINEAR;

        if (magFilter == NEAREST)
            this.magFilter = GLES20.GL_NEAREST;
        else
            this.magFilter = GLES20.GL_LINEAR;

        //Read File
        InputStream in;
        try {
            in = GLBasic.readAsset(fileName);
            bitmap = BitmapFactory.decodeStream(in);

            width = bitmap.getWidth();
            height = bitmap.getHeight();

            in.close();
        } catch (IOException e) {
             throw new RuntimeException("Couldn't load '" + fileName + "' from asset!");
        }

        TextureRoot.addTexture(this);
    }

    /**
     * Generate Texture
     * Load Bitmap from Assets
     * Setting MIN FILTER, MAG FILTER
     */
    public final void onSurfaceCreated() {
        final int tex[] = new int[1];
        GLES20.glGenTextures(1, tex, 0);
        textureId = tex[0];

        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId);

        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, minFilter);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, magFilter);

        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
    }

    public final void bindToUnit0() {
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId);
    }

    public final void bindToUnit1() {
        GLES20.glActiveTexture(GLES20.GL_TEXTURE1);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId);
    }

    public final int getWidth() {
        return width;
    }

    public final int getHeight() {
        return height;
    }
}
