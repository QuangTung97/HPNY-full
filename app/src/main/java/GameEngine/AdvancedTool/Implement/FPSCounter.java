package GameEngine.AdvancedTool.Implement;


import android.util.Log;

import GameEngine.AdvancedTool.ATTimer;

/**
 * Created by Quang Tung on 1/4/2016.
 */
class FPSCounter {
    long startTime = ATTimer.nanoTime();
    int frames = 0;

    public void logFrame() {
        frames++;
        if(ATTimer.nanoTime() - startTime >= 5000000000.0f) {
            Log.d("Logic Thread", "fps: " + frames / 5);
            frames = 0;
            startTime = ATTimer.nanoTime();
        }
    }
}
