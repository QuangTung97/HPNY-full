package GameEngine.AdvancedTool;

import GameEngine.Graphics.Abstract.Shader;
import GameEngine.Graphics.Abstract.VertexBuffer;
import GameEngine.Graphics.GLContext;
import GameEngine.Graphics.GP;


/**
 * Created by Quang Tung on 1/4/2016.
 */
public abstract class Screen {
    private GLContext context;
    static Screen current = null;

    public Screen() {
        context = new GLContext();
        if (current == null)
            setCurrent(this);
    }

    protected final void addVB(Shader shader, VertexBuffer buffer) {
        context.addVB(shader, buffer);
    }

    protected final void removeVB(VertexBuffer vb) {
        context.removeVB(vb);
    }

    protected final void replaceVB(VertexBuffer oldBuffer, VertexBuffer newBuffer) {
        context.replaceVB(oldBuffer, newBuffer);
    }

    public static void setCurrent(Screen screen) {
        GP.get().setContext(screen.context);
        current = screen;
    }

    public abstract void onResume();

    public abstract void onPause();

    public abstract void onFinish();

    public abstract void onUpdate();
}
