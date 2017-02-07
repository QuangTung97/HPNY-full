package quangtung.hpny;

import java.util.List;

import GameEngine.AdvancedTool.Geometry.vec3;
import GameEngine.Basic.Basic;
import GameEngine.Basic.Interfaces.Input;
import GameEngine.Graphics.GP;
import GameEngine.Graphics.Matrices;

/**
 * Created by Quang Tung on 12/25/2015.
 */
public class CrazyWork {
    private static CrazyWork instance;

    private static final float MIN_DISTANT = 3;
    private static final float MAX_DISTANT = 10;

    float sign = 1;
    float radius = 5;
    float phi = 0;
    float teta = 0;

    public CrazyWork() {
        instance = this;
    }

    public static CrazyWork get() {
        return instance;
    }

    //Rotate
    float startX, startY;
    //Zoom
    float len;
    float prevRadius;
    float prevPhi = 0;
    float prevTeta = 0;

    static final float PI = 3.14159265f;

    public void onTouch() {
        List<Input.TouchEvent> events = Basic.getTouchEvents();

        int size = events.size();

        for (int i = 0; i < size; i++) {
            Input.TouchEvent event = events.get(i);

            /* Multi Touch */
            if (event.next != null) {
                Input.TouchEvent event2 = event.next;

                if (event.type == Input.TouchEvent.TOUCH_DOWN) {
                    float dx, dy;
                    dx = event2.x - event.x;
                    dy = event2.y - event.y;

                    len = (float)Math.sqrt(dx * dx + dy * dy);

                    prevRadius = radius;
                }
                else if (event.type == Input.TouchEvent.TOUCH_DRAGGED) {
                    float dx, dy;
                    float len;

                    dx = event2.x - event.x;
                    dy = event2.y - event.y;
                    len = (float)Math.sqrt(dx * dx + dy * dy);

                    radius = prevRadius - (len - this.len)/50.0f;
                    radius = clamp(radius, MIN_DISTANT, MAX_DISTANT);

                    if (radius == MIN_DISTANT || radius == MAX_DISTANT) {
                        this.len = len;
                        prevRadius = radius;
                    }

                    recaculate();
                }

                continue;
            }

            if (event.type == event.TOUCH_DOWN) {
                startX = event.x;
                startY = event.y;

                prevPhi = phi;
                prevTeta = teta;
            }
            else if (event.type == event.TOUCH_DRAGGED) {
                final vec3 vec = new vec3();

                vec.x = -(float)event.x + startX;
                vec.y = (float)event.y - startY;

                if (vec.x == 0 && vec.y == 0)
                    continue;

                float height = GP.get().getHeight();

                teta = prevTeta + vec.y / height * PI / 3.0f; // 60
                phi = prevPhi + vec.x / height * PI / 3.0f;

                float maxTeta = PI / 2.0f - 0.025f;
                if (teta > maxTeta) {
                    teta = maxTeta;
                }
                else if (teta < -maxTeta) {
                    teta = -maxTeta;
                }
                else
                    sign = 1;

                phi = adjust(phi);
                prevPhi = adjust(prevPhi);

                recaculate();
            }
        }
    }

    private float clamp(float n, float a, float b) {
        if (n > b)
            return b;

        if (n < a)
            return a;

        return n;
    }

    private float adjust(float angle) {
        if (angle > 2 * PI)
            angle -= 2 * PI;
        else if (angle < 0)
            angle += 2 * PI;
        return angle;
    }

    private void recaculate() {
        float x, y, z;
        float rXZ = radius * (float)Math.cos(teta);
        y = Matrices.lookAt.y + radius * (float)Math.sin(teta);
        z = rXZ * (float)Math.cos(phi);
        x = rXZ * (float)Math.sin(phi);

        Matrices.viewPos.set(x, y, z);
        Matrices.recaculateView();
    }
}
