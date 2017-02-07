package GameEngine.Graphics.Implement;

/**
 * Created by Quang Tung on 1/5/2016.
 */
public class ArrayListObject {

    private Object[] data;
    private int size = 0;
    private int cap;

    public ArrayListObject() {
        cap = 10;
        data = new Object[cap];
    }

    public ArrayListObject(int cap) {
        this.cap = cap;
        data = new Object[cap];
    }

    public void add(Object e) {
        if (size >= cap) {
            cap *= 2;
            Object[] newArray = new Object[cap];

            for (int i = 0; i < size; i++)
                newArray[i] = data[i];

            data = newArray;
        }

        data[size] = e;
        size++;
    }

    public Object get(int i) {
        if (i >= size)
            return null;
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
