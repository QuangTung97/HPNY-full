package GameEngine.Graphics.Abstract;

import GameEngine.Basic.ListHead;

/**
 * Created by Quang Tung on 12/22/2015.
 */
public abstract class VertexBuffer {
    public ListHead<VertexBuffer> list = new ListHead<>(this);

    public abstract void onDrawFrame(Program program);
}
