package quangtung.hpny;


import GameEngine.AdvancedTool.ATTimer;
import GameEngine.AdvancedTool.Screen;
import GameEngine.Basic.ListHead;
import GameEngine.Graphics.Abstract.Shader;
import GameEngine.Graphics.Abstract.VertexBuffer;
import quangtung.hpny.Effect.FWControl;
import quangtung.hpny.Effect.O;
import quangtung.hpny.Effect.Restart;
import quangtung.hpny.Effect.TextControl;
import quangtung.hpny.Effect.VBOps;
import quangtung.hpny.Time.S;
import quangtung.hpny.Time.TimeObs;
import quangtung.hpny.Time.TimeSubject;

/**
 * Created by Quang Tung on 2/1/2016.
 */
public class HPNYScreen extends Screen implements TimeSubject, VBOps{
    FWControl control;
    TextControl text;

    private void setting() {
        S.subject = this;
        O.ops = this;
        O.rs = new Restart();

        control = new FWControl();
        text = new TextControl();
    }

    public HPNYScreen() {
        super();
        setting();

        control.restart();
        text.restart();
    }

    @Override
    public void onResume() {

    }

    @Override
    public void onPause() {

    }

    @Override
    public void onFinish() {

    }

    @Override
    public void onUpdate() {
        update(ATTimer.getDeltaSecond());
        CrazyWork.get().onTouch();
    }

//Time Control
    ListHead<TimeObs> list = new ListHead<>(null);
    @Override
    public void register(TimeObs obs) {
        list.addLast(obs.getList());
    }

    @Override
    public void remove(TimeObs obs) {
        obs.getList().remove();
    }

    @Override
    public void update(float dtime) {
        ListHead<TimeObs> entry = list.next;
        while (entry != list) {
            entry.object.update(dtime);
            entry = entry.next;
        }
    }

    @Override
    public void add(Shader shader, VertexBuffer vb) {
        addVB(shader, vb);
    }

    @Override
    public void remove(VertexBuffer vb) {
        removeVB(vb);
    }
}
