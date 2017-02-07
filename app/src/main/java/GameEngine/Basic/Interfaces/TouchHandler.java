package GameEngine.Basic.Interfaces;

import android.view.View;

import java.util.List;

import GameEngine.Basic.Interfaces.Input.TouchEvent;

/**
 * Created by Quang Tung on 11/23/2015.
 */
public interface TouchHandler extends View.OnTouchListener {
    public boolean isTouchDown(int pointer);

    public int getTouchX(int pointer);

    public int getTouchY(int pointer);

    public List<TouchEvent> getTouchEvents();
}
