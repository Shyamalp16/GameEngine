package engine;

import Core.GameObject;
import Core.MouseListener;
import components.NonPickable;
import imgui.ImGui;
import renderer.PickingTexture;
import scenes.Scene;

import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT;

public class PropertiesWindow {
    private GameObject activeGameObject = null;
    private PickingTexture pickingTexture;
    private float debounce = 0.2f;

    public PropertiesWindow(PickingTexture pickingTexture){
        this.pickingTexture = pickingTexture;
    }

    public void update(float dt, Scene currentScene){
        debounce -= dt;
        if(MouseListener.mouseButtonDown(GLFW_MOUSE_BUTTON_LEFT) && debounce < 0){
            int x = (int)MouseListener.getScreenX();
            int y = (int)MouseListener.getScreenY();
            int gameObjectId = pickingTexture.readPixel(x,y);
            GameObject pickedObject = currentScene.getGameObject(gameObjectId);
            if(pickedObject != null && pickedObject.getComponent(NonPickable.class) == null){
                activeGameObject = currentScene.getGameObject(gameObjectId);
            }else if(pickedObject == null && !MouseListener.isDragging()){
                activeGameObject = null;
            }
            this.debounce = 0.2f;
        }
    }

    public void imgui(){
        if(activeGameObject != null){
            ImGui.begin("Properties");
            activeGameObject.imgui();
            ImGui.end();
        }
    }

    public GameObject getActiveGameObject(){
        return this.activeGameObject;
    }
}
