package GameEngine.Graphics.Animation;

import android.opengl.GLES20;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * Created by Quang Tung on 1/3/2016.
 */

/**
 * Attribute similiar to Attribute in OpenGL ES
 * @numberVertex: number of vertex is packed
 * @SoE: Size of Each Element in float or int
 * @buffer: Byte Buffer of Vertex Data
 * */
public class Attribute {
    private int SoE;
    private ByteBuffer buffer;
    private int numVertex;

    /**
     * Init variables
     * Allocate vertex data in GPU
     * */
    public Attribute(int numVertex, int SizeofElement) {
        this.SoE = SizeofElement;
        this.numVertex = numVertex;

        buffer = ByteBuffer.allocateDirect(numVertex * SoE * 4);
        buffer.order(ByteOrder.nativeOrder());
    }

    /**
     * Load data from file by using InputStream
     * Put that data to Vertex in GPU
     * */
    public final void load(InputStream in) throws IOException{
        int size = numVertex * SoE * 4;
        byte[] data = new byte[65536];
        int nr = size / 65536;
        int offset = size % 65536;
        if (offset < 0)
            offset += 65536;

        for (int i = 0; i < nr; i++) {
            in.read(data);
            buffer.put(data);
        }

        data = new byte[offset];
        in.read(data);
        buffer.put(data);

        buffer.flip();
    }

    /**
     * Bind this attribute to OpenGL ES Pipeline
     * */
    public final void bind(int location) {
        GLES20.glVertexAttribPointer(location, SoE, GLES20.GL_FLOAT, false, 0, buffer);
    }
}
