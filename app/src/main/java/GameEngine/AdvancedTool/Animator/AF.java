package GameEngine.AdvancedTool.Animator;

/**
 * Created by Quang Tung on 1/5/2016.
 */

import GameEngine.Basic.ListHead;

/** ANIMATOR FACTORY */
public class AF {
    public static final int TEMPORARY = 1;
    public static final int PERMANENT = 2;


    public static Animator createAnimator(int type, boolean runImmediate,
                                          float timeRange, Runner runner) {
        Animator animator;

        if (type == TEMPORARY) {
            animator = AnimatorPool.create(timeRange, runner);
        }
        else {
            animator = new PermanentAnimator(runner);
        }

        if (runImmediate)
            animator.run();

        return animator;
    }

    /** Just used by this Advanced Tool */
    public static void runAnimatorList() {
        ListHead<Animator> entry = Animator.header.next;

        while (entry != Animator.header) {
            entry.object.onRun();
            entry = entry.next;
        }
    }
}
