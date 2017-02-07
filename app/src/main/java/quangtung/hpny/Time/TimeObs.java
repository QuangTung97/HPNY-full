package quangtung.hpny.Time;

import GameEngine.Basic.ListHead;

/**
 * Created by Quang Tung on 2/5/2016.
 */
public interface TimeObs {
    ListHead<TimeObs> getList();

    void update(float dtime);
}
