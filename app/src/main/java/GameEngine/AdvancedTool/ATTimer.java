package GameEngine.AdvancedTool;

/**
 * Created by Quang Tung on 1/4/2016.
 */
public class ATTimer {

    private static long startTime = 0;
    private static long deltaTime = 0;
    private static long time = 0;

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

    public static void onResume() {
        startTime = System.nanoTime();
    }

    public static void update() {
        long currentTime = System.nanoTime();

        deltaTime = currentTime - startTime;
        startTime = currentTime;
        time += deltaTime;
    }
}
