package GameEngine.AdvancedTool;

import android.app.Activity;

import GameEngine.AdvancedTool.Interface.Initializer;
import GameEngine.Basic.Basic;
import GameEngine.Graphics.GP;

/**
 * Created by Quang Tung on 12/21/2015.
 */
/**
 * Advanced Tool
 * */
public class AT {

    public AT(Activity context, Initializer initializer) {
        new GP(context);
        new Basic(context, GP.get().getView(), 1, 1);
        Basic.setFullScreen();

        logicThread = new LogicThread();

        initializer.onInitialize();
    }

    public static void onResume() {
        GP.get().onResume();
        logicThread.onResume();
    }

    public static void onPause() {
        GP.get().onPause();
        logicThread.onPause();
    }

    public static void onFinish() {
        logicThread.onFinish();
    }

    private static LogicThread logicThread;
}
