package GameEngine.Basic.Implement;

import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import GameEngine.Basic.Interfaces.Input;
import GameEngine.Basic.Interfaces.TouchHandler;
import GameEngine.Basic.Pool;


/**
 * Created by Quang Tung on 11/23/2015.
 */
public class MultiTouchHandler implements TouchHandler {

    Pool<Input.TouchEvent> touchEventPool;
    List<Input.TouchEvent> touchEventsBuffer = new ArrayList<>(100);
    List<Input.TouchEvent> touchEvents = new ArrayList<>(100);
    float scaleX;
    float scaleY;

    int primary = 0;
    int size = 1;

    public MultiTouchHandler(View view, float scaleX, float scaleY) {
        Pool.PoolObjectFactory<Input.TouchEvent> factory =
                new Pool.PoolObjectFactory<Input.TouchEvent>() {
            @Override
            public Input.TouchEvent createObject() {
                return new Input.TouchEvent();
            }
        };

        touchEventPool = new Pool<>(factory, 100);
        view.setOnTouchListener(this);

        this.scaleX = scaleX;
        this.scaleY = scaleY;
    }


    @Override // Not use
    public boolean isTouchDown(int pointer) {
        return false;
    }

    @Override // Not use
    public int getTouchX(int pointer) {
        return 0;
    }

    @Override // Not use
    public int getTouchY(int pointer) {
        return 0;
    }

    @Override
    public List<Input.TouchEvent> getTouchEvents() {
        synchronized (this) {
            int len = touchEvents.size();

            for (int i = 0; i < len; i++) {
                Input.TouchEvent event = touchEvents.get(i);
                touchEventPool.free(event);

                if (event.next != null)
                    touchEventPool.free(event.next);
            }

            touchEvents.clear();
            touchEvents.addAll(touchEventsBuffer);
            touchEventsBuffer.clear();
            return touchEvents;
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        synchronized (this) {
            int action = event.getAction() & MotionEvent.ACTION_MASK;
            int pointerIndex = (event.getAction() & MotionEvent.ACTION_POINTER_INDEX_MASK)
                    >> MotionEvent.ACTION_POINTER_INDEX_SHIFT;
            int pointerCount = event.getPointerCount();

            Input.TouchEvent touchEvent; // main touch event
            Input.TouchEvent touchEvent2;

            if (primary == 0) {
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        touchEvent = touchEventPool.newObject();
                        touchEvent.type = Input.TouchEvent.TOUCH_DOWN;
                        touchEvent.x = (int)(scaleX * event.getX(0));
                        touchEvent.y = (int)(scaleY * event.getY(0));
                        touchEvent.next = null;

                        touchEventsBuffer.add(touchEvent);
                        break;

                    case MotionEvent.ACTION_UP:
                        touchEvent = touchEventPool.newObject();
                        touchEvent.type = Input.TouchEvent.TOUCH_UP;
                        touchEvent.x = (int)(scaleX * event.getX(0));
                        touchEvent.y = (int)(scaleY * event.getY(0));
                        touchEvent.next = null;

                        touchEventsBuffer.add(touchEvent);
                        break;

                    case MotionEvent.ACTION_POINTER_DOWN:
                        if (size == 2)
                            break;

                        size++;
                        touchEvent = touchEventPool.newObject();
                        touchEvent2 = touchEventPool.newObject();

                        touchEvent.type = Input.TouchEvent.TOUCH_DOWN;
                        touchEvent.x = (int)(scaleX * event.getX(0));
                        touchEvent.y = (int)(scaleY * event.getY(0));

                        touchEvent2.type = Input.TouchEvent.TOUCH_DOWN;
                        touchEvent2.x = (int)(scaleX * event.getX(1));
                        touchEvent2.y = (int)(scaleY * event.getY(1));

                        touchEvent.next = touchEvent2;
                        touchEvent2.next = null;

                        touchEventsBuffer.add(touchEvent);
                        break;

                    case MotionEvent.ACTION_POINTER_UP:

                        if (event.getPointerId(pointerIndex) >= 2)
                            break;
                        touchEvent = touchEventPool.newObject();
                        touchEvent2 = touchEventPool.newObject();

                        touchEvent.type = Input.TouchEvent.TOUCH_UP;
                        touchEvent.x = (int)(scaleX * event.getX(0));
                        touchEvent.y = (int)(scaleY * event.getY(0));

                        touchEvent2.type = Input.TouchEvent.TOUCH_UP;
                        touchEvent2.x = (int)(scaleX * event.getX(1));
                        touchEvent2.y = (int)(scaleY * event.getY(1));

                        touchEvent.next = touchEvent2;
                        touchEvent2.next = null;

                        size--;

                        touchEventsBuffer.add(touchEvent);

                        primary = 1;
                        break;

                    case MotionEvent.ACTION_MOVE:

                        touchEvent = touchEventPool.newObject();

                        touchEvent.type = Input.TouchEvent.TOUCH_DRAGGED;
                        touchEvent.x = (int)(scaleX * event.getX(0));
                        touchEvent.y = (int)(scaleY * event.getY(0));

                        touchEvent.next = null;

                        if (size == 2) {
                            touchEvent2 = touchEventPool.newObject();

                            touchEvent2.type = Input.TouchEvent.TOUCH_DRAGGED;
                            touchEvent2.x = (int)(scaleX * event.getX(1));
                            touchEvent2.y = (int)(scaleY * event.getY(1));

                            touchEvent.next = touchEvent2;
                            touchEvent2.next = null;
                        }

                        touchEventsBuffer.add(touchEvent);
                        break;

                }
            }
            else {

                switch (action) {
                    case MotionEvent.ACTION_UP:
                        primary = 0;
                        break;

                    case MotionEvent.ACTION_POINTER_DOWN:
                        if (size == 2)
                            break;

                        size++;
                        touchEvent = touchEventPool.newObject();
                        touchEvent2 = touchEventPool.newObject();

                        touchEvent.type = Input.TouchEvent.TOUCH_DOWN;
                        touchEvent.x = (int)(scaleX * event.getX(0));
                        touchEvent.y = (int)(scaleY * event.getY(0));

                        touchEvent2.type = Input.TouchEvent.TOUCH_DOWN;
                        touchEvent2.x = (int)(scaleX * event.getX(1));
                        touchEvent2.y = (int)(scaleY * event.getY(1));

                        touchEvent.next = touchEvent2;
                        touchEvent2.next = null;

                        touchEventsBuffer.add(touchEvent);
                        break;

                    case MotionEvent.ACTION_POINTER_UP:

                        if (event.getPointerId(pointerIndex) >= 2)
                            break;
                        touchEvent = touchEventPool.newObject();
                        touchEvent2 = touchEventPool.newObject();

                        touchEvent.type = Input.TouchEvent.TOUCH_UP;
                        touchEvent.x = (int)(scaleX * event.getX(0));
                        touchEvent.y = (int)(scaleY * event.getY(0));

                        touchEvent2.type = Input.TouchEvent.TOUCH_UP;
                        touchEvent2.x = (int)(scaleX * event.getX(1));
                        touchEvent2.y = (int)(scaleY * event.getY(1));

                        touchEvent.next = touchEvent2;
                        touchEvent2.next = null;

                        size--;

                        touchEventsBuffer.add(touchEvent);
                        break;

                    case MotionEvent.ACTION_MOVE:
                        if (size == 2) {
                            touchEvent = touchEventPool.newObject();

                            touchEvent.type = Input.TouchEvent.TOUCH_DRAGGED;
                            touchEvent.x = (int)(scaleX * event.getX(0));
                            touchEvent.y = (int)(scaleY * event.getY(0));

                            touchEvent2 = touchEventPool.newObject();

                            touchEvent2.type = Input.TouchEvent.TOUCH_DRAGGED;
                            touchEvent2.x = (int)(scaleX * event.getX(1));
                            touchEvent2.y = (int)(scaleY * event.getY(1));

                            touchEvent.next = touchEvent2;
                            touchEvent2.next = null;

                            touchEventsBuffer.add(touchEvent);
                        }
                        break;
                }
            }

            return true;
        }
    }
}
