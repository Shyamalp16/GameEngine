package engine;

import Core.GameObject;
import Core.MouseListener;
import imgui.ImGui;
import renderer.PickingTexture;
import scenes.Scene;

import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT;

public class PropertiesWindow {
    private GameObject activeGameObject = null;
    private PickingTexture pickingTexture;

    public PropertiesWindow(PickingTexture pickingTexture){
        this.pickingTexture = pickingTexture;
    }

    public void update(float dt, Scene currentScene){
        if(MouseListener.mouseButtonDown(GLFW_MOUSE_BUTTON_LEFT)){
            int x = (int)MouseListener.getScreenX();
            int y = (int)MouseListener.getScreenY();
            activeGameObject = currentScene.getGameObject(pickingTexture.readPixel(x,y));
        }
    }

    public void imgui(){
        if(activeGameObject != null){
            ImGui.begin("Properties");
            activeGameObject.imgui();
            ImGui.end();
        }
    }
}
