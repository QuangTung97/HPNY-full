package GameEngine.Graphics;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.util.DisplayMetrics;

import GameEngine.Graphics.Shader.ShaderRoot;

/**
 * Created by Quang Tung on 12/23/2015.
 */

//Graphics
public class GP {
    private static GP instance;
    private GLSurfaceView view;
    private RenderThread renderThread;
    DisplayMetrics metrics;

    public GP(Activity context) {
        instance = this;
        new GLBasic(context);

        metrics = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(metrics);

        new Matrices();

        view = new GLSurfaceView(context);
        view.setEGLContextClientVersion(2);
        view.setEGLConfigChooser(8, 8, 8, 8, 16, 8);

        renderThread = new RenderThread();
        view.setRenderer(renderThread);

        new ShaderRoot();
        new TextureRoot();
    }

    public static GP get() {
        return instance;
    }

    //Just used to init Basic Packet
    public GLSurfaceView getView() {
        return view;
    }

    public int getWidth() {
        return metrics.widthPixels;
    }

    public int getHeight() {
        return metrics.heightPixels;
    }

    //Used in UI Thread
    public final void onResume() {
        view.onResume();
    }

    //Used in UI Thread
    public final void onPause() {
        view.onPause();
    }

    public final void setContext(GLContext context) {
        renderThread.setContext(context);
    }
}
