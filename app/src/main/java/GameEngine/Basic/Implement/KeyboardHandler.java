package GameEngine.Basic.Implement;

import android.util.Log;
import android.view.KeyEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import GameEngine.Basic.Interfaces.Input;
import GameEngine.Basic.Pool;


/**
 * Created by Quang Tung on 11/23/2015.
 */
public class KeyboardHandler implements View.OnKeyListener {

    Pool<Input.KeyEvent> keyEventPool;
    List<Input.KeyEvent> keyEventsBuffer = new ArrayList<Input.KeyEvent>();
    List<Input.KeyEvent> keyEvents = new ArrayList<Input.KeyEvent>();

    public KeyboardHandler(View view) {
        Pool.PoolObjectFactory<Input.KeyEvent> factory = new Pool.PoolObjectFactory<Input.KeyEvent>() {

            @Override
            public Input.KeyEvent createObject() {
                return new Input.KeyEvent();
            }
        };

        keyEventPool = new Pool<>(factory, 100);

        /**
         * Reserve
         * */
        //view.setOnKeyListener(this);
        view.setFocusableInTouchMode(true);
        view.requestFocus();

    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {

        if (event.getAction() == KeyEvent.ACTION_MULTIPLE)
            return false;

        Input.KeyEvent keyEvent = keyEventPool.newObject();
        keyEvent.keyCode = keyCode;
        keyEvent.keyChar = (char)event.getUnicodeChar();

        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            keyEvent.type = Input.KeyEvent.KEY_DOWN;
        }

        if (event.getAction() == KeyEvent.ACTION_UP) {
            keyEvent.type = Input.KeyEvent.KEY_UP;
        }

        synchronized (this) {
            keyEventsBuffer.add(keyEvent);
        }
        return true;
    }

    public List<Input.KeyEvent> getKeyEvents() {
        synchronized (this) {
            int len = keyEvents.size();

            for (int i = 0; i < len; i++) {
                keyEventPool.free(keyEvents.get(i));
            }
            keyEvents.clear();
            keyEvents.addAll(keyEventsBuffer);
            keyEventsBuffer.clear();
            return keyEvents;
        }
    }
}
