package GameEngine.Graphics.Shader;

import android.opengl.GLES20;

import GameEngine.AdvancedTool.Geometry.vec3;
import GameEngine.Graphics.Abstract.Program;
import GameEngine.Graphics.Abstract.Shader;
import GameEngine.Graphics.Matrices;

/**
 * Created by Quang Tung on 2/5/2016.
 */
public class ParticlesEx extends Shader {
    private static ParticlesEx instance = null;

    private Program program;
    private vec3 accel = new vec3(0, -0.5f, 0);

    public float[] time = new float[3];
    public float[] accelData = new float[3];

    private ParticlesEx() {
        priority = 3;
        program = new Program("Shader/ParticlesEx vs.txt",
                "Shader/ParticlesEx fs.txt");
        accel.toArray(accelData);
    }

    public static ParticlesEx get() {
        if (instance == null)
            instance = new ParticlesEx();
        return instance;
    }

    @Override
    public void onSurfaceCreated() {
        program.onSurfaceCreated();

        program.attribute("a_spos", 0);
        program.attribute("a_texCoord", 2);
        program.attribute("a_time", 4);

        program.uniform("view", 0, 16, Matrices.view);
        program.uniform("projection", 1, 16, Matrices.projection);
        program.uniform("a", 2, 3, accelData);

        program.changable("MMatrix", 0, 16);
        program.changable("ucolor", 2, 3);
        program.changable("utime", 1, 3);
        program.changable("nrframe", 3, 2);
        program.changable("vel", 4, 3);

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
