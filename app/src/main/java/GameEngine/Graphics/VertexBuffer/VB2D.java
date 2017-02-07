package GameEngine.Graphics.VertexBuffer;

import android.opengl.GLES20;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import GameEngine.Graphics.Abstract.Program;
import GameEngine.Graphics.Abstract.VertexBuffer;
import GameEngine.Graphics.ModelMatrix;
import GameEngine.Graphics.Texture;

/**
 * Created by Quang Tung on 12/26/2015.
 */
public class VB2D extends VertexBuffer {
    private int x, y, w, h;
    private Texture texture;
    public ModelMatrix matrix;

    private FloatBuffer vertexBuffer;
    private FloatBuffer texCoordBuffer;

    private static ShortBuffer indexBuffer = null;

    private static final int NUM_VERTICES = 4;

    private void setUpBuffer() {
        int vertexSize = 4 * 2;
        int texCoordSize = 4 * 2;

        //Set vertex buffer
        ByteBuffer buffer = ByteBuffer.allocateDirect(NUM_VERTICES * vertexSize);
        buffer.order(ByteOrder.nativeOrder());
        vertexBuffer = buffer.asFloatBuffer();

        float[] tmpBuffer = new float[] {
                x, y,
                x, h - 1 + y,
                w - 1 + x, h - 1 + y,
                w - 1 + x, y
        };

        vertexBuffer.clear();
        vertexBuffer.put(tmpBuffer, 0, vertexSize * NUM_VERTICES / 4);
        vertexBuffer.flip();

        //Set tex coord buffer
        buffer = ByteBuffer.allocateDirect(NUM_VERTICES * texCoordSize);
        buffer.order(ByteOrder.nativeOrder());
        texCoordBuffer = buffer.asFloatBuffer();

        float[] tmpBuffer2 = new float[] {
                0, 0,
                0, 1,
                1, 1,
                1, 0
        };

        texCoordBuffer.clear();
        texCoordBuffer.put(tmpBuffer2, 0, texCoordSize * NUM_VERTICES / 4);
        texCoordBuffer.flip();

        //Set index buffer
        if (indexBuffer == null) {
            buffer = ByteBuffer.allocateDirect(6 * 2);
            buffer.order(ByteOrder.nativeOrder());
            indexBuffer = buffer.asShortBuffer();

            short[] indices = new short[] {
                    0, 1, 2,
                    0, 2, 3
            };

            indexBuffer.clear();
            indexBuffer.put(indices, 0, 6);
            indexBuffer.flip();
        }

    }

    //start from 0
    public final void setPosOnImage(int x, int y, int w, int h) {
        float[] tmpBuffer = new float [4 * 2];
        float tmpW = (float)(texture.getWidth() - 1);
        float tmpH = (float)(texture.getHeight() - 1);

        float u0 = (float)x / tmpW;
        float v0 = (float)y / tmpH;

        float u1 = (float)(x + w - 1) / tmpW;
        float v1 = (float)(y + h - 1) / tmpH;

        tmpBuffer[0] = u0;
        tmpBuffer[1] = v0;
        tmpBuffer[2] = u0;
        tmpBuffer[3] = v1;
        tmpBuffer[4] = u1;
        tmpBuffer[5] = v1;
        tmpBuffer[6] = u1;
        tmpBuffer[7] = v0;

        texCoordBuffer.clear();
        texCoordBuffer.put(tmpBuffer, 0, 4 * 2);
        texCoordBuffer.flip();
    }

    public VB2D(int x, int y, int w, int h, String imageFileName) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;

        texture = new Texture(imageFileName, Texture.LINEAR, Texture.LINEAR);
        matrix = new ModelMatrix();

        setUpBuffer();
    }

    public VB2D(int x, int y, int w, int h, Texture texture) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;

        this.texture = texture;
        this.matrix = new ModelMatrix();

        setUpBuffer();
    }



    @Override
    public void onDrawFrame(Program program) {
        texture.bindToUnit0();
        matrix.bind(program.getChangableLoc(0));

        GLES20.glVertexAttribPointer(program.getAttribLoc(0), 2, GLES20.GL_FLOAT, false,
                0, vertexBuffer);
        GLES20.glVertexAttribPointer(program.getAttribLoc(2), 2, GLES20.GL_FLOAT, false,
                0, texCoordBuffer);
        GLES20.glDrawElements(GLES20.GL_TRIANGLES, 6, GLES20.GL_UNSIGNED_SHORT, indexBuffer);
    }
}
