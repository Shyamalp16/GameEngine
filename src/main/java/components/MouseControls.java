package components;

import Core.GameObject;
import Core.KeyListener;
import Core.MouseListener;
import Core.Window;
import org.joml.Vector4f;
import util.Settings;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE;
import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT;

public class MouseControls extends Component{
    GameObject holdingObject = null;
    private float debounceTime = 0.05f;
    private float debounce = debounceTime;

    public void pickupObject(GameObject go){
        if(this.holdingObject != null){
            this.holdingObject.destroy();
        }
        this.holdingObject = go;
        this.holdingObject.getComponent(SpriteRenderer.class).setColor(new Vector4f(0.8f, 0.8f, 0.8f, 0.5f));
        this.holdingObject.addComponent(new NonPickable());
        Window.getScene().addGameObjectToScene(go);
    }

    public void place(){
        GameObject newobj = this.holdingObject.copy();
        if(newobj.getComponent(StateMachine.class) != null){
            newobj.getComponent(StateMachine.class).refreshTextures();
        }
        newobj.getComponent(SpriteRenderer.class).setColor(new Vector4f(1,1,1,1));
        newobj.removeComponent(NonPickable.class);
        Window.getScene().addGameObjectToScene(newobj);
        this.holdingObject = null;
    }

    @Override
    public void EditorUpdate(float dt){
        debounce -= dt;
        if(holdingObject != null && debounce <= 0){
            holdingObject.transform.position.x = MouseListener.getWorldX();
            holdingObject.transform.position.y = MouseListener.getWorldY();
            holdingObject.transform.position.x = ((int)Math.floor(holdingObject.transform.position.x / Settings.GRID_WIDTH) * Settings.GRID_HEIGHT) + Settings.GRID_WIDTH / 2.0f;
            holdingObject.transform.position.y = ((int)Math.floor(holdingObject.transform.position.y / Settings.GRID_HEIGHT) * Settings.GRID_WIDTH) + Settings.GRID_HEIGHT / 2.0f;

            if(MouseListener.mouseButtonDown(GLFW_MOUSE_BUTTON_LEFT)){
                place();
                debounce = debounceTime;
            }

            if(KeyListener.isKeyPressed(GLFW_KEY_ESCAPE)){
                holdingObject.destroy();
                holdingObject = null;
            }
        }
    }
}
