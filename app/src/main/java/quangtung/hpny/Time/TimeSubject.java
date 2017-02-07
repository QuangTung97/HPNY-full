package quangtung.hpny.Time;

/**
 * Created by Quang Tung on 2/5/2016.
 */
public interface TimeSubject {
    void register(TimeObs obs);

    void remove(TimeObs obs);

    void update(float dtime);
}
