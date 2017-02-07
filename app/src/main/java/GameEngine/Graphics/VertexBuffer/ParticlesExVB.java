package GameEngine.Graphics.VertexBuffer;

import android.opengl.GLES20;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;

import GameEngine.Graphics.Abstract.Program;
import GameEngine.Graphics.Abstract.VertexBuffer;
import GameEngine.Graphics.Animation.Attribute;
import GameEngine.Graphics.GLBasic;
import GameEngine.Graphics.Implement.GLTimer;
import GameEngine.Graphics.ModelMatrix;
import GameEngine.Graphics.Texture;

/**
 * Created by Quang Tung on 2/5/2016.
 */
public class ParticlesExVB extends VertexBuffer {
    private Attribute spos;
    private Attribute time;
    private Attribute texCoord;

    private Texture texture;
    public ModelMatrix matrix;

    private float[] time_frame = new float[3];
    private float[] color = new float[3];
    private float[] nrframe = new float[2];
    private float[] velocity = new float[3];

    private int nrvertex;

    public void setColor(float r, float g, float b) {
        color[0] = r;
        color[1] = g;
        color[2] = b;
    }

    public void setVelocity(float x, float y, float z) {
        velocity[0] = x;
        velocity[1] = y;
        velocity[2] = z;
    }

    public ParticlesExVB(String pexFile, String imageFile) {
        matrix = new ModelMatrix();
        time_frame[0] = 0;
        setColor(1, 1, 1);
        setVelocity(0, 0, 0);

        try {
            InputStream in = GLBasic.readAsset(pexFile);

            /**
             * - number of vertex : int
             * - number of frame x : int
             * - number of frame y : int
             *
             * - spos
             * - time
             * - texCoord
             * */

            nrvertex = GLBasic.readInt(in);
            nrframe[0] = GLBasic.readInt(in);
            nrframe[1] = GLBasic.readInt(in);

            spos = new Attribute(nrvertex, 3);
            spos.load(in);

            time = new Attribute(nrvertex, 2);
            time.load(in);

            texCoord = new Attribute(nrvertex, 2);
            texCoord.load(in);
            
            in.close();
        }
        catch (IOException e) {
            throw new RuntimeException();
        }
        
        texture = new Texture(imageFile, Texture.LINEAR, Texture.LINEAR);
    }

    public void restart() {
        time_frame[0] = 0;
        time_frame[1] = 0;
    }

    @Override
    public void onDrawFrame(Program program) {
        matrix.bind(program.getChangableLoc(0));
        texture.bindToUnit0();
        
        spos.bind(program.getAttribLoc(0));
        texCoord.bind(program.getAttribLoc(2));
        time.bind(program.getAttribLoc(4));

        time_frame[0] += GLTimer.getDeltaSecond();
        time_frame[1] = (int)(time_frame[0] * 24.0f);

        program.setChangable(2, color);
        program.setChangable(1, time_frame);
        program.setChangable(3, nrframe);
        program.setChangable(4, velocity);

        GLES20.glEnable(GLES20.GL_BLEND);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, nrvertex);
        GLES20.glDisable(GLES20.GL_BLEND);
    }
}
