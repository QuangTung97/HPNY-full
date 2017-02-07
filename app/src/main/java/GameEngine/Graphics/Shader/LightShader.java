package GameEngine.Graphics.Shader;


import GameEngine.AdvancedTool.Geometry.vec3;
import GameEngine.Graphics.Abstract.Program;
import GameEngine.Graphics.Abstract.Shader;
import GameEngine.Graphics.Matrices;

/**
 * Created by Quang Tung on 1/2/2016.
 */
public class LightShader extends Shader{
    private static LightShader instance = null;

    /**
     * vec3 of parameters in this shader
     * */
    public vec3 lightPos = new vec3();

    private float[] lightPosData = new float[3];
    private float[] viewPosData = new float[3];

    Program program;

    private LightShader() {
        priority = 1;
        program = new Program("Shader/lightingvs.txt", "Shader/lightingfs.txt");

        lightPos.set(-5, 5, 10);
        lightPos.toArray(lightPosData);
    }

    public static LightShader get() {
        if (instance == null) {
            instance = new LightShader();
        }
        return instance;
    }

    @Override
    public void onSurfaceCreated() {
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

        program.uniform("lightPos", 2, 3, lightPosData);
        program.uniform("viewPos", 3, 3, viewPosData);

        program.texture("tex", 0);
    }

    @Override
    public void onDrawFrame() {
        Matrices.viewPos.toArray(viewPosData);

        program.use();
        program.onDrawFrame(bufferList);
    }
}
