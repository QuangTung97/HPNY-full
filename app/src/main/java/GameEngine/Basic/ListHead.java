package GameEngine.Basic;

/**
 * Created by Quang Tung on 12/9/2015.
 */
public class ListHead<E> {
    public ListHead<E> next, prev;
    public E object;

    public ListHead(E object) {
        this.object = object;
        next = prev = this;
    }

    public final void addLast(ListHead element) {
        ListHead last = this.prev;
        last.next = element;
        element.prev = last;
        element.next = this;
        this.prev = element;
    }

    public final void addFirst(ListHead element) {
        ListHead first = this.next;
        first.prev = element;
        this.next = element;
        element.next = first;
        element.prev = this;
    }

    public final void remove() {
        ListHead next, prev;
        next = this.next;
        prev = this.prev;
        prev.next = next;
        next.prev = prev;
    }

    public final void moveToFirst(ListHead element) {
        element.remove();
        addFirst(element);
    }

    public final void moveToLast(ListHead element) {
        element.remove();
        addLast(element);
    }

    public final void replaceHeader(ListHead header) {
        ListHead next, prev;
        next = this.next;
        prev = this.prev;

        if (next == this) {
            header.next = header.prev = header;
            return;
        }

        header.next = next;
        header.prev = prev;
        next.prev = header;
        prev.next = header;

        this.next = this;
        this.prev = this;
    }

    public final void replace(ListHead list) {
        ListHead next, prev;
        next = this.next;
        prev = this.prev;

        if (next == this) {
            list.next = list.prev = list;
            return;
        }

        list.next = next;
        list.prev = prev;
        next.prev = list;
        prev.next = list;
    }
}