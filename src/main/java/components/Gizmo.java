package components;

import Core.GameObject;
import Core.MouseListener;
import Core.Prefabs;
import Core.Window;
import engine.PropertiesWindow;
import org.joml.Vector2f;
import org.joml.Vector4f;

import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT;

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

    private transient Vector2f xAxisOffset = new Vector2f(67.0f, 13.0f);
    private transient Vector2f yAxisOffset = new Vector2f(26.0f, 65.0f);

    private int gizmoWidth = 16;
    private int gizmoHeight = 48;

    protected boolean xAxisActive = false;
    protected boolean yAxisActive = false;
    private boolean using = false;

    public Gizmo(Sprite arrowSprite, PropertiesWindow propertiesWindow){
        this.xAxisObject = Prefabs.generateSpriteObject(arrowSprite, 16, 48);
        this.yAxisObject = Prefabs.generateSpriteObject(arrowSprite, 16, 48);
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
        this.xAxisObject.setNoSerialize();
        this.yAxisObject.setNoSerialize();
    }


    @Override
    public void update(float dt){
        if(!using){
            return;
        }
        this.activeGameObject = this.propertiesWindow.getActiveGameObject();
        if(this.activeGameObject != null){
            this.setActive();
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
        Vector2f mousePos = new Vector2f(MouseListener.getOrthoX(), MouseListener.getOrthoY());
        if (mousePos.x <= xAxisObject.transform.position.x + gizmoHeight &&
                mousePos.x >= xAxisObject.transform.position.x - gizmoWidth &&
                mousePos.y >= xAxisObject.transform.position.y - gizmoHeight &&
                mousePos.y <= xAxisObject.transform.position.y + gizmoWidth) {
            xAxisSprite.setColor(xAxisColorHover);
            return true;
        }

        xAxisSprite.setColor(xAxisColor);
        return false;
    }

    private boolean checkYHoverState() {
        Vector2f mousePos = new Vector2f(MouseListener.getOrthoX(), MouseListener.getOrthoY());
        if (mousePos.x <= yAxisObject.transform.position.x + gizmoWidth &&
                mousePos.x >= yAxisObject.transform.position.x - gizmoWidth &&
                mousePos.y <= yAxisObject.transform.position.y + gizmoHeight &&
                mousePos.y >= yAxisObject.transform.position.y - gizmoHeight) {
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
