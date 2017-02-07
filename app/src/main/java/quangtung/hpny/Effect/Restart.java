package quangtung.hpny.Effect;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Quang Tung on 2/5/2016.
 */
public class Restart implements RestartSubject {
    List<RestartObs> list = new ArrayList<>(10);

    @Override
    public void register(RestartObs obs) {
        list.add(obs);
    }

    @Override
    public void remove(RestartObs obs) {
        list.remove(obs);
    }

    @Override
    public void restart() {
        for (int i = 0; i < list.size(); i++)
            list.get(i).restart();
    }
}
