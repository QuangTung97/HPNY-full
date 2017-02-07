package GameEngine.Graphics.Shader;

import android.opengl.GLES20;
import android.opengl.Matrix;

import GameEngine.Graphics.Abstract.Program;
import GameEngine.Graphics.Abstract.Shader;
import GameEngine.Graphics.GP;

/**
 * Created by Quang Tung on 12/26/2015.
 */
public class Shader2D extends Shader{
    private static Shader2D instance = null;
    private float[] orthoMatrix = new float[16];

    Program program;

    private Shader2D() {
        priority = MAX_PRIORITY - 1; //highest priority
        program = new Program("Shader/2Dvs.txt", "Shader/2Dfs.txt");
    }

    public static Shader2D get() {
        if (instance == null) {
            instance = new Shader2D();
        }

        return instance;
    }

    @Override
    public void onSurfaceCreated() {
        program.onSurfaceCreated();

        program.attribute("position", 0);
        program.attribute("texCoord", 2);

        program.texture("tex", 0);

        program.uniform("orthoMatrix", 0, 16, orthoMatrix);

        program.changable("modelMatrix", 0, 16);

        int width = GP.get().getWidth();
        int height = GP.get().getHeight();

        Matrix.orthoM(orthoMatrix, 0,
                0, width - 1,
                height - 1, 0,
                -1, 1);
    }

    @Override
    public void onDrawFrame() {
        GLES20.glDisable(GLES20.GL_DEPTH_TEST);
        GLES20.glEnable(GLES20.GL_BLEND);

        program.use();
        program.onDrawFrame(bufferList);

        GLES20.glDisable(GLES20.GL_BLEND);
        GLES20.glEnable(GLES20.GL_DEPTH_TEST);
    }
}
