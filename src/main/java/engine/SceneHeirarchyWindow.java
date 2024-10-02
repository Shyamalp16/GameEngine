package engine;

import Core.GameObject;
import Core.Window;
import imgui.ImGui;
import imgui.flag.ImGuiTreeNodeFlags;

import java.util.List;

public class SceneHeirarchyWindow {
    private static String payloadDragDropType = "SceneHierarchy";
    public void imgui(){
        ImGui.begin("Scene Heirarchy");
        List<GameObject> gameObjectList = Window.getScene().getGameObjects();
        int index = 0;
        for(GameObject obj : gameObjectList){
            if(!obj.doSerialization()){
                continue;
            }

            boolean treeNodeOpen = doTreeNode(obj, index);

            if(treeNodeOpen){
                ImGui.treePop();
            }
            index++;
        }
        ImGui.end(); 
    }

    private boolean doTreeNode(GameObject obj, int index){
        ImGui.pushID(index);
        boolean treeNodeOpen =  ImGui.treeNodeEx(
                obj.name,
                ImGuiTreeNodeFlags.DefaultOpen |
                        ImGuiTreeNodeFlags.FramePadding |
                        ImGuiTreeNodeFlags.OpenOnArrow |
                        ImGuiTreeNodeFlags.SpanAvailWidth,
                obj.name
        );
        ImGui.popID();

        if(ImGui.beginDragDropSource()){
            ImGui.setDragDropPayloadObject(payloadDragDropType, obj);
            ImGui.text(obj.name);
            ImGui.endDragDropSource();
        }

        if(ImGui.beginDragDropTarget()){
            Object paylodObj = ImGui.acceptDragDropPayloadObject(payloadDragDropType);
            if(paylodObj != null){
                if(paylodObj.getClass().isAssignableFrom(GameObject.class)){
                    GameObject playerGameObj = (GameObject)paylodObj;
                    System.out.println("Payload Accepted: '" + playerGameObj.name + "'");
                }
            }
            ImGui.endDragDropTarget();
        }

        return treeNodeOpen;
    }
}
