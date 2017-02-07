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
import GameEngine.Graphics.Shader.ParticlesShader;
import GameEngine.Graphics.Texture;

/**
 * Created by Quang Tung on 1/29/2016.
 */
public class ParticlesVB extends VertexBuffer {
    private Attribute spos;
    private Attribute v;
    private Attribute time;
    private Attribute scolor;
    private Attribute ecolor;
    private Attribute texCoord;

    private Texture texture;
    public ModelMatrix matrix;

    private float[] curr_time = new float[3];

    private int nrvertex;

    public ParticlesVB(String ptsfile, String imagefile) {
        matrix = new ModelMatrix();
        curr_time[0] = 0;

        try {
            InputStream in = GLBasic.readAsset(ptsfile);

            nrvertex = GLBasic.readInt(in);

            //Acceleration
            GLBasic.readFloat(in);
            GLBasic.readFloat(in);
            GLBasic.readFloat(in);

            spos = new Attribute(nrvertex, 3);
            spos.load(in);

            v = new Attribute(nrvertex, 3);
            v.load(in);

            time = new Attribute(nrvertex, 2);
            time.load(in);

            scolor = new Attribute(nrvertex, 3);
            scolor.load(in);

            ecolor = new Attribute(nrvertex, 3);
            ecolor.load(in);

            texCoord = new Attribute(nrvertex, 2);
            texCoord.load(in);

            in.close();

        } catch (IOException e) {
            throw new RuntimeException();
        }

        texture = new Texture(imagefile, Texture.LINEAR, Texture.LINEAR);
    }

    private ParticlesVB() {
        matrix = new ModelMatrix();
        curr_time[0] = 0;
    }

    @Override
    public void onDrawFrame(Program program) {
        matrix.bind(program.getChangableLoc(0));
        texture.bindToUnit0();

        spos.bind(program.getAttribLoc(0));
        v.bind(program.getAttribLoc(3));
        time.bind(program.getAttribLoc(4));
        scolor.bind(program.getAttribLoc(5));
        ecolor.bind(program.getAttribLoc(6));
        texCoord.bind(program.getAttribLoc(2));

        curr_time[0] += GLTimer.getDeltaSecond();

        if (curr_time[0] > 4)
            curr_time[0] = 0;

        program.setChangable(1, curr_time);

        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, nrvertex);
    }

    public ParticlesVB vbClone() {
        ParticlesVB ins = new ParticlesVB();
        ins.texture = this.texture;

        ins.spos = spos;
        ins.v = v;
        ins.time = time;
        ins.scolor = scolor;
        ins.ecolor = ecolor;
        ins.texCoord = texCoord;

        ins.nrvertex = nrvertex;
        return ins;
    }
}
