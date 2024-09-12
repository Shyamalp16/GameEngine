package jade;

import org.lwjgl.Version;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window {
    private int width;
    private int height;
    private String title;
    private long glfwWindow;

//    Singleton instance
    public static Window window = null;
//  Private cause ion any  anyone else to call this class
    private Window(){
        this.width = 1920;
        this.height = 1080;
        this.title = "Contra!";
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

//      Make the openGl context current
        glfwMakeContextCurrent(glfwWindow);

//      Enable v-sync
        glfwSwapInterval(1);

//      Make the window visible
        glfwShowWindow(glfwWindow);

//      NOTE: Game will break if i dont have this i have no idea why
        GL.createCapabilities();
    }

//      GAME LOOP
    public void loop(){
        while(!glfwWindowShouldClose(glfwWindow)){
//          Poll Events, will keep the mouse events, keyboard events etc in its context
            glfwPollEvents();

//           Sets the color in buffer
            glClearColor(1.0f, 0.0f, 0.0f, 1.0f);
//          Tells the graphic library how to clear the buffer, (Flushes the buffer to the entire screen)
            glClear(GL_COLOR_BUFFER_BIT);

            glfwSwapBuffers(glfwWindow);
        }
    }
}
