package quangtung.hpny.Effect;

import GameEngine.Graphics.Abstract.Shader;
import GameEngine.Graphics.Abstract.VertexBuffer;

/**
 * Created by Quang Tung on 2/5/2016.
 */
public interface VBOps {

    void add(Shader shader, VertexBuffer vb);

    void remove(VertexBuffer vb);
}
