package GameEngine.Graphics.Implement;

/**
 * Created by Quang Tung on 12/26/2015.
 */
public class GLStack<E> {
    private int cap;
    private E[] list;
    private int size = 0;

    public GLStack() {
        cap = 10;
        list = (E[]) new Object[cap];
    }

    public GLStack(int cap) {
        this.cap = cap;
        list = (E[]) new Object[cap];
    }

    public final void push(E e) {
        if (size == cap) {
            cap *= 4;
            E[] newList = (E[]) new Object[cap];

            for (int i = 0; i < size; i++) {
                newList[i] = list[i];
            }

            list = newList;
        }
        list[size] = e;
        size++;
    }

    /**
     * if size == 0 then return null object
     * */
    public final E pop() {
        if (size > 0) {
            size--;
            return list[size];
        }
        else
            return null;
    }
    
    public final int size() {
        return this.size;
    }
}
