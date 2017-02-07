package GameEngine.Graphics.Animation;

import android.opengl.GLES20;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.ShortBuffer;

/**
 * Created by Quang Tung on 2/6/2016.
 */
public class Index {
    private int count;
    private ShortBuffer index;

    public Index(int nrvertex) {
        count = nrvertex / 4 * 6;

        ByteBuffer buffer = ByteBuffer.allocateDirect(count * 2);
        buffer.order(ByteOrder.nativeOrder());
        index = buffer.asShortBuffer();

        short[] data = new short[count];
        for (int i = 0; i < count; i += 6) {
            short cnst = (short)(i * 4);
            data[i] = cnst;
            data[i+1] = (short)(cnst + 1);
            data[i+2] = (short)(cnst + 2);
            data[i+3] = (short)(cnst + 2);
            data[i+4] = (short)(cnst + 1);
            data[i+5] = (short)(cnst + 3);
        }

        index.put(data, 0, count);
        index.flip();
    }

    public final void draw() {
        GLES20.glDrawElements(GLES20.GL_TRIANGLES, count, GLES20.GL_UNSIGNED_SHORT, index);
    }
}
