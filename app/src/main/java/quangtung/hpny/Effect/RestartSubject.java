package quangtung.hpny.Effect;

/**
 * Created by Quang Tung on 2/5/2016.
 */
public interface RestartSubject {
    void register(RestartObs obs);

    void remove(RestartObs obs);

    void restart();
}
