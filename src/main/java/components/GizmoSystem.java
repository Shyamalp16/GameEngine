package components;

import Core.KeyListener;
import Core.Window;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_E;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_R;

public class GizmoSystem extends Component {


    private Spritesheet gizmos;
    private transient int usingGizmo = 0;

    public GizmoSystem(Spritesheet gizmoSprites){
        gizmos = gizmoSprites;
    }

    @Override
    public void start(){
        gameObject.addComponent(new TranslateGizmo(gizmos.getSprite(1), Window.getImGuiLayer().getPropertiesWindow()));
        gameObject.addComponent(new ScaleGizmo(gizmos.getSprite(2), Window.getImGuiLayer().getPropertiesWindow()));
    }

    @Override
    public void update(float dt){
        if(usingGizmo == 0){
            gameObject.getComponent(TranslateGizmo.class).setUsing();
            gameObject.getComponent(ScaleGizmo.class).setNotUsing();
        }else if(usingGizmo == 1){
            gameObject.getComponent(ScaleGizmo.class).setUsing();
            gameObject.getComponent(TranslateGizmo.class).setNotUsing();
        }

        if(KeyListener.isKeyPressed(GLFW_KEY_E)){
            usingGizmo = 0;
        }else if(KeyListener.isKeyPressed(GLFW_KEY_R)){
            usingGizmo = 1;
        }
    }

}
