package GameEngine.Graphics.Shader;

import android.opengl.GLES20;
import android.opengl.Matrix;

import GameEngine.AdvancedTool.Geometry.vec3;
import GameEngine.Graphics.Abstract.Program;
import GameEngine.Graphics.Abstract.Shader;
import GameEngine.Graphics.GP;
import GameEngine.Graphics.Matrices;

/**
 * Created by Quang Tung on 1/9/2016.
 */
public class LightShadow extends Shader{
    private static LightShadow instance = null;

    private Program shadowMap;
    private Program program; //Main program

    private int framebuffer;
    private int depthMap;

    /**
     * vec3 of parameters in this shader
     * */
    public vec3 lightPos = new vec3();


    private float[] lightPosData = new float[3];
    private float[] viewPosData = new float[3];

    private float[] shadowVP = new float[16];
    private float[] shadowVPMatrix = new float[16];
    private float[] lightMatrix = new float[16];

    private LightShadow() {
        priority = 0;
        shadowMap = new Program("Shader/shadowvs.txt", "Shader/shadowfs.txt");
        program = new Program("Shader/shadow mapping vs.txt",
                "Shader/shadow mapping fs.txt");

        lightPos.set(-5, 10, 5);

        Matrix.setLookAtM(lightMatrix, 0,
                lightPos.x, lightPos.y, lightPos.z,
                0, 0, 0,
                0, 1, 0);
    }

    public static LightShadow get() {
        if (instance == null)
            instance = new LightShadow();
        return instance;
    }

    private void initShadowMap() {
        shadowMap.onSurfaceCreated();

        shadowMap.attribute("a_position", 0);
        shadowMap.changable("MMatrix", 0, 16);
        shadowMap.uniform("VPMatrix", 0, 16, shadowVPMatrix);

        /**
         * Setting projection matrix
         * Setting view matrix
         * Calculate View Projection Matrix
         * */
        float ratio = 1.0f * (float) GP.get().getHeight() / (float)GP.get().getWidth();
        Matrix.frustumM(shadowVP, 0, -1.0f, 1.0f, -ratio, ratio, 1, 100);
        Matrix.multiplyMM(shadowVPMatrix, 0, shadowVP, 0, lightMatrix, 0);
    }

    private void initMainProgram() {
        program.onSurfaceCreated();

        program.attribute("a_position", 0);
        program.attribute("a_normal", 1);
        program.attribute("a_texCoord", 2);

        program.changable("MMatrix", 0, 16);
        program.changable("lightAmbient", 1, 3);
        program.changable("lightDiffuse", 2, 3);
        program.changable("lightSpecular", 3, 3);


        program.uniform("View", 0, 16, Matrices.view);
        program.uniform("Projection", 1, 16, Matrices.projection);
        program.uniform("shadowVPMatrix", 2, 16, shadowVPMatrix);

        program.uniform("lightPos", 3, 3, lightPosData);
        program.uniform("viewPos", 4, 3, viewPosData);

        program.texture("tex", 0);
        program.texture("shadow", 1);

        lightPos.toArray(lightPosData);
    }

    @Override
    public void onSurfaceCreated() {
        initShadowMap();

        prepareRenderToTexture();

        initMainProgram();
        //NullScreen.buffer2.texture.textureId = depthMap;
    }

    private void prepareRenderToTexture() {
        int renderBuffer;
        final int[] buffer = new int[2];

        int width, height;
        width = GP.get().getWidth();
        height =  GP.get().getHeight();

        GLES20.glGenFramebuffers(1, buffer, 0);
        framebuffer = buffer[0];

        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, framebuffer);

        GLES20.glGenRenderbuffers(1, buffer, 0);
        renderBuffer = buffer[0];

        GLES20.glBindRenderbuffer(GLES20.GL_RENDERBUFFER, renderBuffer);
        GLES20.glRenderbufferStorage(GLES20.GL_RENDERBUFFER, GLES20.GL_DEPTH_COMPONENT16,
                width, height);

        GLES20.glFramebufferRenderbuffer(GLES20.GL_FRAMEBUFFER, GLES20.GL_DEPTH_ATTACHMENT,
                GLES20.GL_RENDERBUFFER, renderBuffer);

        GLES20.glGenTextures(1, buffer, 0);
        depthMap = buffer[0];

        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, depthMap);
        GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D, 0, GLES20.GL_RGBA, width, height,
                0, GLES20.GL_RGBA, GLES20.GL_UNSIGNED_BYTE, null);

        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);

        GLES20.glFramebufferTexture2D(GLES20.GL_FRAMEBUFFER, GLES20.GL_COLOR_ATTACHMENT0,
                GLES20.GL_TEXTURE_2D, depthMap, 0);

        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);

        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0);
    }

    private void bindShadowToUnit1() {
        GLES20.glActiveTexture(GLES20.GL_TEXTURE1);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, depthMap);
    }

    @Override
    public void onDrawFrame() {
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, framebuffer);
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
        shadowMap.use();
        shadowMap.onDrawFrame(bufferList);

        Matrices.viewPos.toArray(viewPosData);

        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0);
        program.use();
        bindShadowToUnit1();
        program.onDrawFrame(bufferList);
    }
}
