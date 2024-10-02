package Core;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector4f;

import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

public class MouseListener {
    private static MouseListener instance;
    private double scrollX, scrollY;
    private double xPos, yPos, worldX, worldY;
    private final boolean[] mouseButtonPressed = new boolean[9];
    private boolean isDragging;

    private int mouseButtonsDown = 0;

    private Vector2f gameViewPortPos = new Vector2f();
    private Vector2f gameViewPortSize = new Vector2f();

//    Singleton
    private MouseListener(){
        this.scrollX = 0.0;
        this.scrollY = 0.0;
        this.xPos = 0.0;
        this.yPos = 0.0;
    }

    public static MouseListener get(){
        if(MouseListener.instance == null){
            MouseListener.instance = new MouseListener();
        }

        return MouseListener.instance;
    }

    public static void mousePosCallback(long window, double xpos, double ypos){
        if(get().mouseButtonsDown > 0){
            get().isDragging = true;
        }

//      Change instance variable values
        get().xPos = xpos;
        get().yPos = ypos;
    }

    public static void mouseButtonCallback(long window, int button, int action, int mods){
//        Check if button is pressed or not and modify instance value
        if(action == GLFW_PRESS){
        get().mouseButtonsDown++;
//        Safety for if the mouse has extra buttons that are pressed
            if(button < get().mouseButtonPressed.length){
                get().mouseButtonPressed[button] = true;
            }
        }else if(action == GLFW_RELEASE){
            get().mouseButtonsDown--;
            if(button < get().mouseButtonPressed.length){
                get().mouseButtonPressed[button] = false;
                get().isDragging = false;
            }
        }
    }

    public static void mouseScrollCallback(long window, double xOffeset, double yOffeset){
        get().scrollX = xOffeset;
        get().scrollY = yOffeset;
    }

    public static void endFrame(){
        get().scrollX = 0;
        get().scrollY = 0;
    }

//  GETTERS BELOW
    public static float getX(){
        return (float)get().xPos;
    }

    public static float getY(){
        return (float)get().yPos;
    }

    public static float getScrollX(){
        return (float)(get().scrollX);
    }

    public static float getScrollY(){
        return (float)(get().scrollY);
    }

    public static boolean isDragging(){
        return get().isDragging;
    }

    public static boolean mouseButtonDown(int button){
        if(button < get().mouseButtonPressed.length){
            return get().mouseButtonPressed[button];
        }else{
            return false;
        }
    }

    public static float getWorldX(){
        return getWorld().x;
    }

    public static float getWorldY(){
        return getWorld().y;
    }

    public static Vector2f getWorld(){
        float currentX = getX() - get().gameViewPortPos.x;
        float currentY = getY() - get().gameViewPortPos.y;
        currentX = (currentX/ get().gameViewPortSize.x) * 2.0f - 1.0f;
        currentY = -((currentY/ get().gameViewPortSize.y) * 2.0f - 1.0f);
        Vector4f tmp = new Vector4f(currentX, currentY, 0, 1);

        Camera camera = Window.getScene().camera();
        Matrix4f inverseView = new Matrix4f(camera.getInverseView());
        Matrix4f inverseProjection = new Matrix4f(camera.getInverseProjection());

        tmp.mul(inverseView.mul(inverseProjection));

        return new Vector2f(tmp.x, tmp.y);
    }

    public static float getScreenX(){
        return getScreen().x;
    }

    public static float getScreenY(){
        return getScreen().y;
    }

    public static Vector2f getScreen(){
        float currentX = getX() - get().gameViewPortPos.x;
        float currentY = (getY() - get().gameViewPortPos.y);

        currentX = (currentX / get().gameViewPortSize.x) * 1920.0f;
        currentY = 1080.0f - ((currentY / get().gameViewPortSize.y) * 1080.0f);

        return new Vector2f(currentX, currentY);
    }


    public static void setGameViewPortSize(Vector2f gameViewPortSize) {
        get().gameViewPortSize.set(gameViewPortSize);
    }

    public static void setGameViewPortPos(Vector2f gameViewPortPos) {
        get().gameViewPortPos.set(gameViewPortPos) ;
    }


}
