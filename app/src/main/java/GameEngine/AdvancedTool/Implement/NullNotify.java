package GameEngine.AdvancedTool.Implement;

import GameEngine.AdvancedTool.Interface.Notify;

/**
 * Created by Quang Tung on 1/3/2016.
 */
public class NullNotify implements Notify {
    private static NullNotify instance = null;

    private NullNotify() {

    }

    @Override
    public void onNotify(Object object) {

    }

    public static NullNotify get() {
        if (instance == null)
            instance = new NullNotify();
        return instance;
    }
}
