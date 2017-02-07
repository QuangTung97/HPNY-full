package GameEngine.Basic.Implement;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.View;

import java.util.List;

import GameEngine.Basic.Interfaces.Input;
import GameEngine.Basic.Interfaces.TouchHandler;


/**
 * Created by Quang Tung on 11/23/2015.
 */
public class AndroidInput implements Input {
    AccelerometerHandler accelHandler;
    KeyboardHandler keyHandler;
    TouchHandler touchHandler;

    public AndroidInput(Context context,  View view, float scaleX, float scaleY) {
        accelHandler = new AccelerometerHandler(context);
        keyHandler = new KeyboardHandler(view);

        if (Build.VERSION.SDK_INT < 5)
            touchHandler = new SingleTouchHandler(view, scaleX, scaleY);
        else
            touchHandler = new MultiTouchHandler(view, scaleX, scaleY);

        if (Build.VERSION.SDK_INT >= 5)
            Log.d("Android Input", "Multi Touch Handler");
        else
            Log.d("Android Input", "Single Touch Handler");
    }

    @Override
    public float getAccelX() {
        return accelHandler.getAccelX();
    }

    @Override
    public float getAccelY() {
        return accelHandler.getAccelY();
    }

    @Override
    public float getAccelZ() {
        return accelHandler.getAccelZ();
    }

    @Override
    public List<KeyEvent> getKeyEvents() {
        return keyHandler.getKeyEvents();
    }

    @Override
    public List<TouchEvent> getTouchEvents() {
        return touchHandler.getTouchEvents();
    }
}
