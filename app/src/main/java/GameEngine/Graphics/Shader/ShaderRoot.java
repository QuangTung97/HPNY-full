package GameEngine.Graphics.Shader;

import java.util.ArrayList;
import java.util.List;

import GameEngine.Graphics.Abstract.Shader;

/**
 * Created by Quang Tung on 12/26/2015.
 */
//Where all Shader are stored
public class ShaderRoot {
    private static List<Shader> shaderList  = new ArrayList<>(20);

    public ShaderRoot() {
    }

    public static void add(Shader shader) {
        shaderList.add(shader);
    }

    public static void onSurfaceCreated() {
        int size = shaderList.size();

        for (int i = 0; i < size; i++) {
            Shader shader = shaderList.get(i);
            shader.onSurfaceCreated();
        }
    }
}
