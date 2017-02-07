package GameEngine.AdvancedTool.Animator;

import GameEngine.Basic.ListHead;

/**
 * Created by Quang Tung on 1/5/2016.
 */
public abstract class Animator {
    static ListHead<Animator> header = new ListHead<>(null);

    protected ListHead<Animator> list = new ListHead<>(this);
    protected float currentTime = 0.0f;
    protected Runner runner;
    protected boolean linked = false;

    abstract void onRun();

    public final void run() {
        if (!linked) {
            header.addLast(list);
            linked = true;
        }
        currentTime = 0;
    }

}
