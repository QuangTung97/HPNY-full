package GameEngine.AdvancedTool.Animator;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Quang Tung on 1/5/2016.
 */
public class AnimatorPool {
    private static List<TemporaryAnimator> pool = new ArrayList<>(50);

    static TemporaryAnimator create(float timeRange, Runner runner) {
        TemporaryAnimator animator;

        int size = pool.size();
        if (size == 0) {
            animator = new TemporaryAnimator(timeRange, runner);
        }
        else {
            animator = pool.remove(size - 1);
            animator.setAttributes(timeRange, runner);
        }

        return animator;
    }

    static void free(TemporaryAnimator animator) {
        pool.add(animator);
    }
}
