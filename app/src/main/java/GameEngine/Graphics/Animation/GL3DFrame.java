package GameEngine.Graphics.Animation;


import android.opengl.GLES20;

import java.io.IOException;
import java.io.InputStream;

import GameEngine.Graphics.Abstract.Program;
import GameEngine.Graphics.Abstract.Shader;

/**
 * Created by Quang Tung on 1/3/2016.
 */

/**
 * Frame used for 3D Geometry
 * With attribute @position, @norml
 * @numVertex: similar to numVertex in Attribute
 * @shader: Shader that own this Frame
 * */
public class GL3DFrame extends GLFrame {
    private Attribute position;
    private Attribute normal;
    private Shader shader;
    private int numVertex;

    /**
     * Initilze 2 Attribute
     * Store numVertex and shader
     * */
    public GL3DFrame(int numVertex, Shader shader) {
        position = new Attribute(numVertex, 3);
        normal = new Attribute(numVertex, 3);

        this.numVertex = numVertex;
        this.shader = shader;
    }

    /**
     * Load data from file through InputStream
     * */
    public void load(InputStream in) throws IOException {
        position.load(in);
        normal.load(in);
    }

    @Override
    public void onDrawFrame(Program program) {
        position.bind(program.getAttribLoc(0));
        normal.bind(program.getAttribLoc(1));

        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, numVertex);
    }
}
