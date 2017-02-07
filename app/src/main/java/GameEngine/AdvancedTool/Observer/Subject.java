package GameEngine.AdvancedTool.Observer;

import GameEngine.Basic.ListHead;

/**
 * Created by Quang Tung on 1/16/2016.
 */
public class Subject {
    private ListHead<Observer> header = new ListHead<>(null);

    public final void add(Observer obs) {
        header.addLast(obs.getObserverList());
    }

    public final void remove(Observer obs) {
        obs.getObserverList().remove();
    }

    public final void onNotify() {
        ListHead<Observer> entry = header.next;

        while (entry != header) {
            entry.object.onNotify();
            entry = entry.next;
        }
    }
}
