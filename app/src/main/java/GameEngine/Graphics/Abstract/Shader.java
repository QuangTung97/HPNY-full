package GameEngine.Graphics.Abstract;

import GameEngine.Graphics.Shader.ShaderRoot;
import GameEngine.Basic.ListHead;

/**
 * Created by Quang Tung on 12/3/2015.
 */

public abstract class Shader {
    public static final int MAX_PRIORITY = 10;
    public int priority = MAX_PRIORITY; // need to set in each Shader Class

    private boolean surfaceCreated = false;

    public ListHead<VertexBuffer> bufferList;

    abstract public void onSurfaceCreated();

    /** Used by Shader Root in On Created Method*/
    public final void create() {
        if (!surfaceCreated) {
            onSurfaceCreated();
            ShaderRoot.add(this);
            surfaceCreated = true;
        }
    }

    abstract public void onDrawFrame();
}
