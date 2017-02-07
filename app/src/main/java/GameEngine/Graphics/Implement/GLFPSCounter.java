package GameEngine.Graphics.Implement;

import android.util.Log;

/**
 * Created by Quang Tung on 11/28/2015.
 */
public class GLFPSCounter {
    long startTime = GLTimer.nanoTime();
    int frames = 0;
    public void logFrame() {
        frames++;
        if(GLTimer.nanoTime() - startTime >= 5000000000.0f) {
            Log.d("GLFPSCounter", "fps: " + frames / 5);
            frames = 0;
            startTime = GLTimer.nanoTime();
        }
    }
}
