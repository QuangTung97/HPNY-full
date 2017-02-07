package quangtung.hpny;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Quang Tung on 2/7/2016.
 */
public class RandomList<VB> {
    private List<VB> list = new ArrayList<>();
    public final void add(VB vb) {
        list.add(vb);
    }

    public final VB getRandom(Random random) {
        int size = list.size();
        int nr = random.nextInt() % size;
        if (nr < 0)
            nr += size;
        return list.get(nr);
    }
}
