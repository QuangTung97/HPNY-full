package GameEngine.AdvancedTool.File;

import java.io.IOException;
import java.io.InputStream;

import GameEngine.Graphics.Abstract.Shader;
import GameEngine.Graphics.Animation.AnimationObject;
import GameEngine.Graphics.Animation.Attribute;
import GameEngine.Graphics.Animation.GL3DFrame;
import GameEngine.AdvancedTool.Interface.Notify;
import GameEngine.Graphics.GLBasic;

/**
 * Created by Quang Tung on 1/3/2016.
 */
public class LoadAniFile {
    public static AnimationObject load(String fileName, Shader shader, boolean loop, Notify notify) {
        AnimationObject animationObject;
        try {
            InputStream in = GLBasic.readAsset(fileName);

            int version;
            int fps;
            int numFrame;
            int numVertex;

            version = GLBasic.readInt(in);
            fps = GLBasic.readInt(in);
            numFrame = GLBasic.readInt(in);
            numVertex = GLBasic.readInt(in);

            if (version != 1)
                throw new RuntimeException("File error: " + fileName);

            animationObject = new AnimationObject(numFrame, fps, loop, notify);
            animationObject.texCoord = new Attribute(numVertex, 2);
            animationObject.texCoord.load(in);

            for (int i = 0; i < numFrame; i++) {
                GL3DFrame frame = new GL3DFrame(numVertex, shader);
                frame.load(in);
                animationObject.addFrame(frame);
            }


        } catch (IOException e) {
            throw new RuntimeException("Can't load file: " + fileName);
        }


         return animationObject;
    }

}
