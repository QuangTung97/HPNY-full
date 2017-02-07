package GameEngine.Graphics.Shader;

import android.opengl.GLES20;

import GameEngine.AdvancedTool.Geometry.vec3;
import GameEngine.Graphics.Abstract.Program;
import GameEngine.Graphics.Abstract.Shader;
import GameEngine.Graphics.Matrices;

/**
 * Created by Quang Tung on 2/6/2016.
 */
public class PSShader extends Shader {
    private static PSShader instance = null;

    private Program program;
    public vec3 accel = new vec3(0, -0.4f, 0);

    public float[] accelData = new float[3];

    private PSShader() {
        priority = 4;
        program = new Program("Shader/PS vs.txt", "Shader/PS fs.txt");
        accel.toArray(accelData);
    }

    public static PSShader get() {
        if (instance == null)
            instance = new PSShader();
        return instance;
    }

    @Override
    public void onSurfaceCreated() {
        program.onSurfaceCreated();

        program.attribute("a_spos", 0);
        program.attribute("a_texCoord", 2);
        program.attribute("a_time", 3);
        program.attribute("a_v", 4);

        program.uniform("view", 0, 16, Matrices.view);
        program.uniform("projection", 1, 16, Matrices.projection);
        program.uniform("a", 2, 3, accelData);

        program.changable("MMatrix", 0, 16);
        program.changable("utime", 1, 3);
        program.changable("v", 2, 3);
        program.changable("swap", 3, 1);

        program.texture("tex", 0);
    }

    @Override
    public void onDrawFrame() {
        program.use();
        GLES20.glDepthMask(false);
        GLES20.glEnable(GLES20.GL_BLEND);
        program.onDrawFrame(bufferList);
        GLES20.glDisable(GLES20.GL_BLEND);
        GLES20.glDepthMask(true);
    }
}
