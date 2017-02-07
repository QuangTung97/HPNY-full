package GameEngine.AdvancedTool.Observer;

import GameEngine.Basic.ListHead;

/**
 * Created by Quang Tung on 1/16/2016.
 */
public interface Observer {
    void onNotify();

    ListHead<Observer> getObserverList();
}
