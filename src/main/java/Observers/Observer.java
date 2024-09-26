package Observers;

import Core.GameObject;
import Observers.events.Event;

public interface Observer {
    void onNotify(GameObject go, Event event);
}
