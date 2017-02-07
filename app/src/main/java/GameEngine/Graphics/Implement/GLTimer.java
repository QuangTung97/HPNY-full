package GameEngine.Graphics.Implement;

/**
 * Created by Quang Tung on 1/4/2016.
 */

/**
 * Independent Timer
 * */
public class GLTimer {
    private static long startTime = 0;
    private static long deltaTime = 0;
    private static long time = 0;
    private static int frame = 0;

    public static int getFrame() {
        return frame;
    }

    public static long nanoTime() {
        return time;
    }

    //Delta time in each frame
    public static long getDeltaNanos() {
        return deltaTime;
    }

    public static float getDeltaSecond() {
        return (float)deltaTime / 1000000000.0f;
    }

    public static void onSurfaceCreated() {
        startTime = System.nanoTime();
    }

    public static void update() {
        long currentTime = System.nanoTime();

        deltaTime = currentTime - startTime;
        startTime = currentTime;
        time += deltaTime;

        frame++;
    }

}
