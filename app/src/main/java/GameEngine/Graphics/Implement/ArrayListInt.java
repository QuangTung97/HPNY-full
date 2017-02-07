package GameEngine.Graphics.Implement;

/**
 * Created by Quang Tung on 1/5/2016.
 */
public class ArrayListInt {

    private int[] data;
    private int size = 0;
    private int cap;

    public ArrayListInt() {
        cap = 10;
        data = new int[cap];
    }

    public ArrayListInt(int cap) {
        this.cap = cap;
        data = new int[cap];
    }

    public void add(int e) {
        if (size >= cap) {
            cap *= 2;
            int[] newArray = new int[cap];

            for (int i = 0; i < size; i++)
                newArray[i] = data[i];

            data = newArray;
        }

        data[size] = e;
        size++;
    }


    public int get(int i) {
        if (i >= size)
            return 0;
        else
            return data[i];
    }

    public void clear() {
        size = 0;
    }

    public int size() {
        return size;
    }
}
