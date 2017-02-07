package GameEngine.Graphics.VertexBuffer;

import android.opengl.GLES20;

import java.io.IOException;
import java.io.InputStream;

import GameEngine.Graphics.Abstract.Program;
import GameEngine.Graphics.Abstract.VertexBuffer;
import GameEngine.Graphics.Animation.Attribute;
import GameEngine.Graphics.GLBasic;
import GameEngine.Graphics.ModelMatrix;
import GameEngine.Graphics.Texture;

/**
 * Created by Quang Tung on 1/4/2016.
 */
public class Fixed3DVB extends VertexBuffer {

    private Attribute position;
    private Attribute normal;
    private Attribute texCoord;

    private Texture texture;

    public ModelMatrix matrix;
    private int numVertex;

    private float[] ambient = new float[3];
    private float[] diffuse = new float[3];
    private float[] specular = new float[3];

    public void setAmbient(float r, float g, float b) {
        ambient[0] = r;
        ambient[1] = g;
        ambient[2] = b;
    }
    public void setDiffuse(float r, float g, float b) {
        diffuse[0] = r;
        diffuse[1] = g;
        diffuse[2] = b;
    }
    public void setSpecular(float r, float g, float b) {
        specular[0] = r;
        specular[1] = g;
        specular[2] = b;
    }

    private void setMaterial() {
        setAmbient(0.4f, 0.4f, 0.4f);
        setDiffuse(0.6f, 0.6f, 0.6f);
        setSpecular(0.8f, 0.8f, 0.8f);
    }

    public Fixed3DVB(String fileName, String textureFileName) {
        matrix = new ModelMatrix();
        setMaterial();

        try {
            InputStream in = GLBasic.readAsset(fileName);

            numVertex = GLBasic.readInt(in);

            position = new Attribute(numVertex, 3);
            position.load(in);

            normal = new Attribute(numVertex, 3);
            normal.load(in);

            texCoord = new Attribute(numVertex, 2);
            texCoord.load(in);

            in.close();

        } catch (IOException e) {
            throw new RuntimeException();
        }

        texture = new Texture(textureFileName, Texture.LINEAR, Texture.LINEAR);
    }

    public final void setMatrix(ModelMatrix matrix) {
        this.matrix = matrix;
    }

    private Fixed3DVB() {

    }

    @Override
    public void onDrawFrame(Program program) {
        matrix.bind(program.getChangableLoc(0));
        texture.bindToUnit0();

        position.bind(program.getAttribLoc(0));
        normal.bind(program.getAttribLoc(1));
        texCoord.bind(program.getAttribLoc(2));

        program.setChangable(1, ambient);
        program.setChangable(2, diffuse);
        program.setChangable(3, specular);

        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, numVertex);
    }
}
