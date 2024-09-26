package engine;

import Observers.EventSystem;
import Observers.events.Event;
import Observers.events.EventType;
import imgui.ImGui;

public class MenuBar {

    public void imgui(){
        ImGui.beginMainMenuBar();
        if(ImGui.beginMenu("File")){
            if(ImGui.menuItem("Save", "Ctrl+S")){
                EventSystem.notify(null, new Event(EventType.SaveLevel));
            }

            if(ImGui.menuItem("Load", "Ctrl+O")){
                EventSystem.notify(null, new Event(EventType.LoadLevel));
            }
            ImGui.endMenu();
        }
        ImGui.endMainMenuBar();
    }
}
