package Core;

import org.lwjgl.Version;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window {
    private int width;
    private int height;
    private final String title;
    private long glfwWindow;
    private ImGuiLayer imGuiLayer;



    public float r;
    public float g;
    public float b;
    public float a;

//    Singleton instance
    public static Window window = null;
    private static Scene CurrentScene;

//  Private cause ion any  anyone else to call this class
    private Window(){
        this.width = 1920;
        this.height = 1080;
        this.title = "Contra!";
        r=1;
        g=1;
        b=1;
        a=1;
    }

    public static void changeScene(int newScene){
//        Scene changer!!
        switch(newScene){
            case 0:
                CurrentScene = new LevelEditorScene();
                CurrentScene.init();
                CurrentScene.start();
                break;
            case 1:
                CurrentScene = new LevelScene();
                CurrentScene.init();
                CurrentScene.start();
                break;
            default:
                assert false: "Unknown Scene" + newScene + "!";
                break;
        }
    }

    public static Window get(){
        if(Window.window == null){
            Window.window = new Window();
        }
        return Window.window;
    }

    public static Scene getScene(){
        return CurrentScene;
    }

    public void run(){
        System.out.println("Hello " + Version.getVersion());
        init();
        loop();

//      Free the memory at the end
        glfwFreeCallbacks(glfwWindow);
        glfwDestroyWindow(glfwWindow);

//      Terminate GLFW and free the error callbacks
        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }

    public void init(){
//      Setup the error callback. NOTE: same as System.err("Print");
        GLFWErrorCallback.createPrint(System.err).set();

//      Init GLFW
        if(!glfwInit()){
            throw new IllegalStateException("Unable to initialize GLFW");
        }

//      Setup GLFW
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
        glfwWindowHint(GLFW_MAXIMIZED, GLFW_TRUE);

//      Create window
        glfwWindow = glfwCreateWindow(this.width, this.height, this.title, NULL, NULL);

//      Throw exception if it didnt create a window
        if(glfwWindow == NULL){
            throw new IllegalStateException("Failed to create a window");
        }

//      Forwards the position, buttonpress, scroll callbacks to the function
//      NOTE: docs glfw.org/docs/3.3/input_guide.html

        glfwSetCursorPosCallback(glfwWindow, MouseListener::mousePosCallback);
        glfwSetMouseButtonCallback(glfwWindow, MouseListener::mouseButtonCallback);
        glfwSetScrollCallback(glfwWindow, MouseListener::mouseScrollCallback);
        glfwSetWindowSizeCallback(glfwWindow, (w, newWidth, newHeight) -> {
            Window.setWidth(newWidth);
            Window.setHeight(newHeight);
        });

        glfwSetKeyCallback(glfwWindow, KeyListener::keyCallback);

//      Make the openGl context current
        glfwMakeContextCurrent(glfwWindow);

//      Enable v-sync
        glfwSwapInterval(1);

//      Make the window visible
        glfwShowWindow(glfwWindow);

//      NOTE: Game will break if i dont have this i have no idea why
//      basically binds our project to openGL
        GL.createCapabilities();

//      Z-indexing and blending
        glEnable(GL_BLEND);
        glBlendFunc(GL_ONE, GL_ONE_MINUS_SRC_ALPHA);

        this.imGuiLayer = new ImGuiLayer(glfwWindow);
        this.imGuiLayer.initImGui();

        Window.changeScene(0);
    }

//      GAME LOOP
    public void loop(){
//      frame start and end time
        float beginTime = (float)glfwGetTime();
        float endTime = (float)glfwGetTime();
        float dt = -1.0f;

        while(!glfwWindowShouldClose(glfwWindow)){
//          Poll Events, will keep the mouse events, keyboard events etc in its context
            glfwPollEvents();

//          Sets the color in buffer
            glClearColor(r,g,b,a);
//          Tells the graphic library how to clear the buffer, (Flushes the buffer to the entire screen)
            glClear(GL_COLOR_BUFFER_BIT);

            if(dt >= 0){
                CurrentScene.update(dt);
            }
            this.imGuiLayer.update(dt);
            glfwSwapBuffers(glfwWindow);

//          Get end time and find dT and loop it back to beginTime
            endTime = (float)glfwGetTime();
            dt = endTime - beginTime;
            beginTime = endTime;
        }
    }

    public static int getWidth(){
        return get().width;
    }

    public static int getHeight(){
        return get().height;
    }

    public static void setWidth(int newW){
        get().width = newW;
    }

    public static void setHeight(int newH){
        get().height = newH;
    }
}
