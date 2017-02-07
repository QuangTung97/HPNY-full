package quangtung.hpny.Effect;

import GameEngine.AdvancedTool.File.LoadAniFile;
import GameEngine.AdvancedTool.Implement.NullNotify;
import GameEngine.Basic.ListHead;
import GameEngine.Graphics.Animation.AnimationObject;
import GameEngine.Graphics.Shader.LightShader;
import GameEngine.Graphics.Shader.Shader2D;
import GameEngine.Graphics.VertexBuffer.VB2D;
import quangtung.hpny.Time.S;
import quangtung.hpny.Time.TimeObs;

/**
 * Created by Quang Tung on 2/7/2016.
 */
public class TextControl implements TimeObs, RestartObs{
    ListHead<TimeObs> list = new ListHead<TimeObs>(this);

    private float curr_time = 0;

    int stage = 0;

    AnimationObject _2016;
    AnimationObject chuc;
    VB2D name;

    LightShader shader;

    public TextControl() {
        S.subject.register(this);

        shader = LightShader.get();
        _2016 = LoadAniFile.load("2016.ani", shader, false, NullNotify.get());
        _2016.loadTexture("2016.png");

        //Material Gold
        _2016.setAmbient(0.24725f, 0.1995f, 0.0745f);
        _2016.setDiffuse(0.75164f, 0.60648f, 0.22648f);
        _2016.setSpecular(0.628281f, 0.555802f, 0.366065f);

        chuc = LoadAniFile.load("chuc.ani", shader, false, NullNotify.get());
        chuc.loadTexture("2016.png");

        //Copper
        chuc.setAmbient(0.19125f, 0.0735f, 0.0225f);
        chuc.setDiffuse(0.7038f, 0.27048f, 0.0828f);
        chuc.setSpecular(0.256777f, 0.137622f, 0.086014f);
        chuc.matrix.translate(0, -1.4f, 0);

        name = new VB2D(0, 0, 200, 60, "name.png");
    }


    @Override
    public ListHead<TimeObs> getList() {
        return list;
    }

    @Override
    public void update(float dtime) {
        curr_time += dtime;

        switch (stage) {
            case 1: //waiting
                if (curr_time >= 30) {
                    stage = 2;
                    //entry of stage 2
                    O.ops.add(shader, _2016);
                }
                break;
            case 2: //text 1
                if (curr_time >= 34) {
                    stage = 3;
                    O.ops.add(shader, chuc);
                }
                break;
            case 3: //text 2
                if (curr_time >= 39) {
                    stage = 0;
                    O.ops.add(Shader2D.get(), name);
                }
                break;
        }
    }

    @Override
    public void restart() {
        stage = 1;
        curr_time = 0;

        //entry of stage 1
        O.ops.remove(_2016);
        O.ops.remove(chuc);
        O.ops.remove(name);
    }
}
