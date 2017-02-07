package quangtung.hpny.Effect;

/**
 * Created by Quang Tung on 2/5/2016.
 */
public class FireWorkQueue {
    private static FireWorkQueue instance = null;
    private static int MAX = 40;

    private int left, right;
    private FireWork queue[] = new FireWork[MAX];

    private FireWorkQueue() {
        for (int i = 0; i < MAX; i++)
            queue[i] = null;

        left = right = 0;
    }

    public static FireWorkQueue get() {
        if (instance == null)
            instance = new FireWorkQueue();
        return instance;
    }

    public FireWork take() {
        FireWork tmp =  queue[left++];
        left = left % MAX;
        return tmp;
    }

    public void push(FireWork fw) {
        queue[right++] = fw;
        right = right % MAX;
    }

}
