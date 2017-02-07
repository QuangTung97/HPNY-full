package GameEngine.Graphics;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import GameEngine.Graphics.Abstract.Shader;
import GameEngine.Graphics.Abstract.VertexBuffer;
import GameEngine.Graphics.Implement.GLFPSCounter;
import GameEngine.Graphics.Implement.GLTimer;
import GameEngine.Graphics.Shader.ShaderRoot;

import GameEngine.Basic.ListHead;

/**
 * Created by Quang Tung on 12/21/2015.
 */
public class RenderThread implements GLSurfaceView.Renderer {
    private GLFPSCounter counter = new GLFPSCounter();

    GLContext context;

    public RenderThread() {

    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        ShaderRoot.onSurfaceCreated();
        GLTimer.onSurfaceCreated();
        TextureRoot.onSufaceCreated();

        GLES20.glEnable(GLES20.GL_DEPTH_TEST);
        //GLES20.glEnable(GLES20.GL_CULL_FACE);
        //GLES20.glFrontFace(GLES20.GL_CCW);

        //GLES20.glEnable(GLES20.GL_BLEND);
        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES20.glViewport(0, 0, width, height);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        GLTimer.update();
        counter.logFrame();

        GLContext context;
        synchronized (this) {
            context = this.context;
        }

        context.doCommands();

        GLES20.glClearColor(0, 0, 0, 1);
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

        for (int i = 0; i < Shader.MAX_PRIORITY; i++) {
            Shader shader = context.shaderTable.get(i);
            if (shader != null) {
                ListHead<VertexBuffer> bufferList = context.VBListTable.get(i);

                shader.bufferList = bufferList;
                shader.onDrawFrame();
            }
        }
    }

    public void setContext(GLContext context) {
        synchronized (this) {
            this.context = context;
        }
    }
}
