package GameEngine.AdvancedTool.Animator;

import GameEngine.AdvancedTool.ATTimer;

/**
 * Created by Quang Tung on 1/5/2016.
 */
public class PermanentAnimator extends Animator {

    PermanentAnimator(Runner runner) {
        this.runner = runner;
    }

    @Override
    void onRun() {
        currentTime += ATTimer.getDeltaSecond();

        runner.onRun(currentTime, 0);
    }
}
