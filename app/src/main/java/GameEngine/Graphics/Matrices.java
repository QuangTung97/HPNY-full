package GameEngine.Graphics;

import android.opengl.Matrix;

import GameEngine.AdvancedTool.Geometry.vec3;

/**
 * Created by Quang Tung on 1/29/2016.
 */
public class Matrices {
    public static float[] view = new float[16];
    public static float[] projection = new float[16];

    public static vec3 viewPos = new vec3(0, 0, 5);
    public static vec3 lookAt = new vec3(0, 0, 0);

    public Matrices() {
        Matrix.setLookAtM(view, 0,
                viewPos.x, viewPos.y, viewPos.z,
                lookAt.x, lookAt.y, lookAt.z,
                0, 1, 0);

        float aspect = (float)GP.get().getHeight() / (float)GP.get().getWidth();
        Matrix.frustumM(projection, 0, -1, 1, -aspect, aspect, 1, 100);
    }

    public static void recaculateView() {
        Matrix.setLookAtM(view, 0,
                viewPos.x, viewPos.y, viewPos.z,
                lookAt.x, lookAt.y, lookAt.z,
                0, 1, 0);
    }
}
