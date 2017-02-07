package GameEngine.Graphics.Animation;

import java.util.ArrayList;
import java.util.List;

import GameEngine.AdvancedTool.Interface.Notify;
import GameEngine.Graphics.Abstract.Program;
import GameEngine.Graphics.Abstract.VertexBuffer;
import GameEngine.Graphics.Implement.GLTimer;
import GameEngine.Graphics.ModelMatrix;
import GameEngine.Graphics.Texture;

/**
 * Created by Quang Tung on 12/30/2015.
 */

/**
 * The "Boss" of Animation, contain frames
 *
 * @frameList: Array of Frames to be Draw
 * @texCoord: Texture Coordinate Attribute 's used for all Frames
 * @matrix: Model matrix is used for all Frames
 * @texture: Texture Data
 * @numFrames: Number of Frames
 * @loop: Indicate whether Frame 's looped or not
 * @notify: Notify when All of Frames terminate
 * @shader: Shader that own this Vertex Buffer
 * */
public class AnimationObject extends VertexBuffer {
    private List<GLFrame> frameList = new ArrayList<>(50);

    public static final int STYLE_NORMAL = 1;
    public static final int STYLE_LOOP = 3;

    public int style = STYLE_NORMAL;

    //Material
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

    public Attribute texCoord;
    public ModelMatrix matrix = new ModelMatrix();

    private Texture texture;

    private int numFrames;
    private int NANOSECOND_PER_FRAME;
    private long currentTime = 0;
    private Notify notify;

    private int glFrame = 0;

    /**
     * Initialize, take necessary variables
     * */
    public AnimationObject(int numFrames, int fps, boolean loop, Notify notify) {
        this.numFrames = numFrames;
        setMaterial();

        matrix = new ModelMatrix();

        NANOSECOND_PER_FRAME = 1000000000 / fps;
        this.notify = notify;
        if (loop)
            style = STYLE_LOOP;
    }

    /**
     * Load Texture Data from Image File through its file name
     * Must use this function in UI Thread before create any other Threads
     * */
    public final void loadTexture(String textureFileName) {
        texture = new Texture(textureFileName, Texture.LINEAR, Texture.LINEAR);
    }

    public final void addFrame(GLFrame frame) {
        frameList.add(frame);
    }

    public final void reset() {
        currentTime = 0;
    }

    /**
     * Take Nano Time from Animation class
     * calculate current frame
     * Bind Texture Coordinate and Texture data
     * Bind Matrix
     * Draw current Frame
     * if frame reach to the end and not loop then use Notify
     * */

    private int calculateFrame() {
        int frame;
        switch (style) {
            case STYLE_NORMAL:
                frame = (int) (currentTime / NANOSECOND_PER_FRAME);
                if (frame >= numFrames) {
                    frame = numFrames - 1;
                    return frame;
                }

                if  (frame == numFrames - 1) {
                    notify.onNotify(this);
                }
                return frame;

            case STYLE_LOOP:
                frame = (int) (currentTime / NANOSECOND_PER_FRAME) % numFrames;
                if  (frame == numFrames - 1) {
                    currentTime = 0;
                }
                return frame;
        }

        return 0;
    }

    @Override
    public void onDrawFrame(Program program) {
        //If new frame of render thread
        if (glFrame + 1 == GLTimer.getFrame()) {
            currentTime += GLTimer.getDeltaNanos();
        }
        glFrame = GLTimer.getFrame();

        texCoord.bind(program.getAttribLoc(2));
        texture.bindToUnit0();
        matrix.bind(program.getChangableLoc(0));

        //Material
        program.setChangable(1, ambient);
        program.setChangable(2, diffuse);
        program.setChangable(3, specular);

        frameList.get(calculateFrame()).onDrawFrame(program);
    }
}
