package GameEngine.AdvancedTool.Implement;

import GameEngine.AdvancedTool.ATTimer;

/**
 * Created by Quang Tung on 12/15/2015.
 */
public class ControlFPS {
    private long startTime = 0;
    private long frameTimeInterval;
    FPSCounter fpsCounter = new FPSCounter();
    Thread logicThread;

    public ControlFPS(int fps, Thread logicThread) {
        frameTimeInterval = 1000000000 / fps;
        this.logicThread = logicThread;
    }

    public void control() {
        long deltaTime = ATTimer.nanoTime() - startTime;

        fpsCounter.logFrame();

        if (deltaTime < frameTimeInterval) {
            try {
                logicThread.sleep((frameTimeInterval - deltaTime) / 1000000);
            } catch (InterruptedException e) {
            }
        }
        startTime += frameTimeInterval;
    }
}
