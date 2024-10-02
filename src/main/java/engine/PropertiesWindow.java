package engine;

import Core.GameObject;
import Core.MouseListener;
import components.NonPickable;
import imgui.ImGui;
import physics2d.components.Box2dCollider;
import physics2d.components.CircleCollider;
import physics2d.components.Rigidbody2D;
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
        if(!MouseListener.isDragging() && MouseListener.mouseButtonDown(GLFW_MOUSE_BUTTON_LEFT) && debounce < 0){
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

            if(ImGui.beginPopupContextWindow("Component Adder")){
                if(ImGui.menuItem("Add Rigidbody")){
                    if(activeGameObject.getComponent(Rigidbody2D.class) == null){
                        activeGameObject.addComponent(new Rigidbody2D());
                    }
                }

                if(ImGui.menuItem("Add Box Collider")){
                    if((activeGameObject.getComponent(Box2dCollider.class) == null) && (activeGameObject.getComponent(CircleCollider.class) == null)){
                        activeGameObject.addComponent(new Box2dCollider());
                    }
                }

                if(ImGui.menuItem("Add Circle Collider")){
                    if((activeGameObject.getComponent(CircleCollider.class) == null) && (activeGameObject.getComponent(Box2dCollider.class) == null)){
                        activeGameObject.addComponent(new CircleCollider());
                    }
                }
                ImGui.endPopup();
            }
            activeGameObject.imgui();
            ImGui.end();
        }
    }

    public GameObject getActiveGameObject(){
        return this.activeGameObject;
    }

    public void setActiveGameObject(GameObject go){
        this.activeGameObject = go;
    }
}
