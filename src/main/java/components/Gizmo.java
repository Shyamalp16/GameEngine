package components;

import Core.*;
import engine.PropertiesWindow;
import org.joml.Vector2f;
import org.joml.Vector4f;

import static org.lwjgl.glfw.GLFW.*;

public class Gizmo extends Component{
    private transient Vector4f xAxisColor = new Vector4f(1,0.3f,0.3f,1);
    private transient Vector4f xAxisColorHover = new Vector4f(1,0,0,1);
    private transient Vector4f yAxisColor = new Vector4f(0.3f,1,0.3f,1);
    private transient Vector4f yAxisColorHover = new Vector4f(0,1,0,1);
    private PropertiesWindow propertiesWindow;

    private GameObject xAxisObject;
    private GameObject yAxisObject;
    private SpriteRenderer xAxisSprite;
    private SpriteRenderer yAxisSprite;
    protected GameObject activeGameObject = null;

    private transient Vector2f xAxisOffset = new Vector2f(24f/80f, -6f/80f);
    private transient Vector2f yAxisOffset = new Vector2f(-7f/80f, 21f/80f);

    private float gizmoWidth = 16.0f/80.0f;
    private float gizmoHeight = 48.0f/80.0f;

    protected boolean xAxisActive = false;
    protected boolean yAxisActive = false;
    private boolean using = false;

    public Gizmo(Sprite arrowSprite, PropertiesWindow propertiesWindow){
        this.xAxisObject = Prefabs.generateSpriteObject(arrowSprite, gizmoWidth, gizmoHeight);
        this.yAxisObject = Prefabs.generateSpriteObject(arrowSprite, gizmoWidth, gizmoHeight);
        this.xAxisSprite = this.xAxisObject.getComponent(SpriteRenderer.class);
        this.yAxisSprite = this.yAxisObject.getComponent(SpriteRenderer.class);
        this.propertiesWindow = propertiesWindow;
        this.xAxisObject.addComponent(new NonPickable());
        this.yAxisObject.addComponent(new NonPickable());

        Window.getScene().addGameObjectToScene(this.xAxisObject);
        Window.getScene().addGameObjectToScene(this.yAxisObject);
    }

    @Override
    public void start(){
        this.xAxisObject.transform.rotation = 90;
        this.yAxisObject.transform.rotation = 180;
        this.xAxisObject.transform.zIndex = 100;
        this.yAxisObject.transform.zIndex = 100;
        this.xAxisObject.setNoSerialize();
        this.yAxisObject.setNoSerialize();
    }

    @Override
    public void update(float dt){
        if(using){
            this.setInactive();
        }
        xAxisObject.getComponent(SpriteRenderer.class).setColor(new Vector4f(0,0,0,0));
        yAxisObject.getComponent(SpriteRenderer.class).setColor(new Vector4f(0,0,0,0));
    }

    @Override
    public void EditorUpdate(float dt){
        if(!using){
            return;
        }
        this.activeGameObject = this.propertiesWindow.getActiveGameObject();
        if(this.activeGameObject != null){
            this.setActive();
                if(KeyListener.isKeyPressed(GLFW_KEY_LEFT_CONTROL) && (KeyListener.keyBeginPress(GLFW_KEY_D))){
                GameObject newObj = this.activeGameObject.copy();
                Window.getScene().addGameObjectToScene(newObj);
                newObj.transform.position.add(0.1f, 0.1f);
                this.propertiesWindow.setActiveGameObject(newObj);
                return;
            }else if(KeyListener.keyBeginPress(GLFW_KEY_DELETE)){
                    activeGameObject.destroy();
                    this.setInactive();
                    this.propertiesWindow.setActiveGameObject(null);
                    return;
            }
        }else{
            this.setInactive();
            return;
        }

        boolean xAxisHot = checkXHoverState();
        boolean yAxisHot = checkYHoverState();

        if((xAxisHot || xAxisActive) && (MouseListener.isDragging()) && (MouseListener.mouseButtonDown(GLFW_MOUSE_BUTTON_LEFT))){
            xAxisActive = true;
            yAxisActive = false;
        }else if((yAxisHot || yAxisActive) && (MouseListener.isDragging()) && (MouseListener.mouseButtonDown(GLFW_MOUSE_BUTTON_LEFT))){
            xAxisActive = false;
            yAxisActive = true;
        }else{
            xAxisActive = false;
            yAxisActive = false;
        }

        if(this.activeGameObject != null){
            this.xAxisObject.transform.position.set(this.activeGameObject.transform.position);
            this.yAxisObject.transform.position.set(this.activeGameObject.transform.position);
            this.xAxisObject.transform.position.add(xAxisOffset);
            this.yAxisObject.transform.position.add(yAxisOffset);
        }
    }

    private void setActive(){
        this.xAxisSprite.setColor(xAxisColor);
        this.yAxisSprite.setColor(yAxisColor);
    }

    private void setInactive(){
        this.activeGameObject = null;
        this.xAxisSprite.setColor(new Vector4f(0,0,0,0));
        this.yAxisSprite.setColor(new Vector4f(0,0,0,0));
    }

    private boolean checkXHoverState() {
        Vector2f mousePos = MouseListener.getWorld();
        if (mousePos.x <= xAxisObject.transform.position.x + (gizmoHeight / 2.0f) &&
                mousePos.x >= xAxisObject.transform.position.x - (gizmoWidth / 2.0f) &&
                mousePos.y >= xAxisObject.transform.position.y - (gizmoHeight / 2.0f) &&
                mousePos.y <= xAxisObject.transform.position.y + (gizmoWidth / 2.0f)) {
            xAxisSprite.setColor(xAxisColorHover);
            return true;
        }

        xAxisSprite.setColor(xAxisColor);
        return false;
    }

    private boolean checkYHoverState() {
        Vector2f mousePos = MouseListener.getWorld();
        if (mousePos.x <= yAxisObject.transform.position.x + (gizmoWidth/2.0f) &&
                mousePos.x >= yAxisObject.transform.position.x - (gizmoWidth/2.0f) &&
                mousePos.y <= yAxisObject.transform.position.y + (gizmoHeight/2.0f) &&
                mousePos.y >= yAxisObject.transform.position.y - (gizmoHeight/2.0f)) {
            yAxisSprite.setColor(yAxisColorHover);
            return true;
        }

        yAxisSprite.setColor(yAxisColor);
        return false;
    }

    public void setUsing(){
        this.using = true;
    }

    public void setNotUsing(){
        this.using = false;
        this.setInactive();
    }
}
