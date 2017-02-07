package GameEngine.AdvancedTool.Animator;

import GameEngine.AdvancedTool.ATTimer;

/**
 * Created by Quang Tung on 1/5/2016.
 */
public class TemporaryAnimator extends Animator {
    private float timeRange = 0;

    TemporaryAnimator(float timeRange, Runner runner) {
        this.timeRange = timeRange;
        this.runner = runner;
    }

    void setAttributes(float timeRange, Runner runner) {
        this.timeRange = timeRange;
        this.runner = runner;
    }

    @Override
    void onRun() {
        currentTime += ATTimer.getDeltaSecond();

        if (currentTime > timeRange) {
            currentTime = 0;
            list.remove();
            linked = false;

            AnimatorPool.free(this);

            runner.onTerminate();
            return;
        }
        runner.onRun(currentTime, currentTime / timeRange);
    }
}
