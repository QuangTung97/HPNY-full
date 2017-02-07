package GameEngine.AdvancedTool.Animator;

/**
 * Created by Quang Tung on 1/5/2016.
 */
public interface Runner {

    void onRun(float currentTime, float interpolate);

    void onTerminate();
}
