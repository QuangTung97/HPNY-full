package GameEngine.Graphics.Shader;

import android.opengl.GLES20;

import GameEngine.AdvancedTool.Geometry.vec3;
import GameEngine.Graphics.Abstract.Program;
import GameEngine.Graphics.Abstract.Shader;
import GameEngine.Graphics.Matrices;

/**
 * Created by Quang Tung on 1/29/2016.
 */
public class ParticlesShader extends Shader {
    private static ParticlesShader instance = null;

    private Program program;

    vec3 accel = new vec3();

    public float[] time = new float[3];
    public float[] accelData = new float[3];

    private ParticlesShader() {
        priority = 5;
        program = new Program("Shader/particles system vs.txt",
                "Shader/particles system fs.txt");

        accel.set(0, -0.4f, 0);
        accel.toArray(accelData);
    }

    public static ParticlesShader get() {
        if (instance == null)
            instance = new ParticlesShader();
        return instance;
    }

    @Override
    public void onSurfaceCreated() {
        program.onSurfaceCreated();

        program.attribute("a_spos", 0);
        program.attribute("a_texCoord", 2);
        program.attribute("a_v", 3);
        program.attribute("a_time", 4);
        program.attribute("a_scolor", 5);
        program.attribute("a_ecolor", 6);

        program.uniform("view", 0, 16, Matrices.view);
        program.uniform("projection", 1, 16, Matrices.projection);
        program.uniform("a", 2, 3, accelData);

        program.changable("MMatrix", 0, 16);
        program.changable("utime", 1, 3);

        program.texture("tex", 0);
    }

    @Override
    public void onDrawFrame() {
        program.use();
        GLES20.glEnable(GLES20.GL_BLEND);
        GLES20.glDepthFunc(GLES20.GL_ALWAYS);
        GLES20.glDepthMask(false);
        program.onDrawFrame(bufferList);
        GLES20.glDepthMask(true);
        GLES20.glDepthFunc(GLES20.GL_LESS);
        GLES20.glDisable(GLES20.GL_BLEND);
    }
}
