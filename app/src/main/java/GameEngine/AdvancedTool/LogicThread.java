package GameEngine.AdvancedTool;

import android.util.Log;

import GameEngine.AdvancedTool.Animator.AF;
import GameEngine.AdvancedTool.Implement.ControlFPS;

/**
 * Created by Quang Tung on 12/15/2015.
 */
public class LogicThread implements Runnable {
    private Thread thread; // this thread
    private final Object syncObject = new Object();

    private static final int INITIALIZE_CMD = 4;
    private static final int RESUME_CMD = 1;
    private static final int PAUSE_CMD = 2;
    private static final int FINISH_CMD = 3;

    private ControlFPS controlFPS;

    //Sync variable
    private int command;

    public LogicThread() {
        thread = new Thread(this);
        controlFPS = new ControlFPS(40, thread);

        command = INITIALIZE_CMD;

        //Start logic thread
        //But sleep it before
        thread.start();
    }

    /**Run in UI Thread*/
    public void onResume() {
        // Logic Thread Sleeping
        command = RESUME_CMD;

        ATTimer.onResume();
        wakeUpThread();
    }

    /**Run in UI Thread*/
    public void onPause() {
        synchronized (syncObject) {
            command = PAUSE_CMD;
        }
    }

    /**Run in UI Thread*/
    public void onFinish() {
        Log.d("Logic Thread", "Finish");

        synchronized (syncObject) {
            command = FINISH_CMD;
        }
    }

    @Override
    public void run() {
        while (true) {
            /** COMMANDS */
            synchronized (syncObject) {
                switch (command) {
                    case INITIALIZE_CMD:
                        sleepThread();
                        continue;

                    case RESUME_CMD:
                        Screen.current.onResume();
                        break;

                    case PAUSE_CMD:
                        Screen.current.onPause();
                        sleepThread();
                        continue;

                    case FINISH_CMD:
                        Screen.current.onFinish();
                        return;

                    default:
                        break;
                }
                command = 0;
            }

            //Update Timer used for Logic Thread
            ATTimer.update();
            //Animation
            AF.runAnimatorList();
            //Control fps of this loop
            controlFPS.control();  //FPSCounter is 40

            Screen.current.onUpdate();
        }
    }

    //Used in Logic Thread
    public final void sleepThread() {
        Log.d("Logic Thread", "Sleep Thread");

        for (;;) {
            try {
                thread.sleep(10000);
            } catch (InterruptedException e) {
                break;
            }
        }
    }

    //Used in UI Thread
    public final void wakeUpThread() {
        Log.d("Logic Thread", "Wake up thread");
        thread.interrupt();
    }
}
