package GameEngine.Graphics.VertexBuffer;

import android.opengl.GLES20;

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
 * Created by Quang Tung on 2/6/2016.
 */
public class PSVB extends VertexBuffer {
    private Attribute spos;
    private Attribute time;
    private Attribute v;
    private Attribute texCoord;

    private Texture texture;
    public ModelMatrix matrix;

    private int nrvertex;

    private float[] curr_time = new float[3];
    private float[] velocity = new float[3];
    private float[] swap = new float[1];

    public void setVelocity(float x, float y, float z) {
        velocity[0] = x;
        velocity[1] = y;
        velocity[2] = z;
    }

    public final void enableSwap() {
        swap[0] = 1;
    }

    public final void restart() {
        curr_time[0] = 0;
    }

    public PSVB(String ftFile, String imageFile) {
        matrix = new ModelMatrix();
        curr_time[0] = 0;
        setVelocity(0, 0, 0);
        swap[0] = 0;

        try {
            InputStream in = GLBasic.readAsset(ftFile);
            nrvertex = GLBasic.readInt(in);

            spos = new Attribute(nrvertex, 3);
            time = new Attribute(nrvertex, 2);
            v = new Attribute(nrvertex, 3);
            texCoord = new Attribute(nrvertex, 2);

            spos.load(in);
            time.load(in);
            v.load(in);
            texCoord.load(in);

            in.close();
        } catch (IOException e) {
            throw new RuntimeException();
        }

        texture = new Texture(imageFile, Texture.LINEAR, Texture.LINEAR);
    }

    private PSVB() {
        matrix = new ModelMatrix();
        curr_time[0] = 0;
        setVelocity(0, 0, 0);
        swap[0] = 0;
    }

    @Override
    public void onDrawFrame(Program program) {
        matrix.bind(program.getChangableLoc(0));
        texture.bindToUnit0();

        spos.bind(program.getAttribLoc(0));
        time.bind(program.getAttribLoc(3));
        v.bind(program.getAttribLoc(4));
        texCoord.bind(program.getAttribLoc(2));

        curr_time[0] += GLTimer.getDeltaSecond();
        // delete
        if (curr_time[0] > 4)
            restart();

        program.setChangable(1, curr_time);
        program.setChangable(2, velocity);
        program.setChangable(3, swap);

        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, nrvertex);
    }

    public PSVB vbClone() {
        PSVB ins = new PSVB();
        ins.texture = this.texture;

        ins.spos = this.spos;
        ins.time = this.time;
        ins.v = this.v;
        ins.texCoord = this.texCoord;
        ins.nrvertex = this.nrvertex;

        return ins;
    }
}
