package jade;

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
    private String title;
    private long glfwWindow;

    private float r,g,b,a;

//    Singleton instance
    public static Window window = null;
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

    public static Window get(){
        if(Window.window == null){
            Window.window = new Window();
        }

        return Window.window;
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
    }

//      GAME LOOP
    public void loop(){
        while(!glfwWindowShouldClose(glfwWindow)){
//          Poll Events, will keep the mouse events, keyboard events etc in its context
            glfwPollEvents();

//          Sets the color in buffer
            glClearColor(r,g,b,a);
//          Tells the graphic library how to clear the buffer, (Flushes the buffer to the entire screen)
            glClear(GL_COLOR_BUFFER_BIT);
            glfwSwapBuffers(glfwWindow);
        }
    }
}
