package jade;

import org.lwjgl.BufferUtils;

import java.awt.event.KeyEvent;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class LevelEditorScene extends Scene {
    private String vertexShaderSrc = "#version 330 core\n" +
            "\n" +
            "layout(location=0) in vec3 aPos;\n" +
            "layout(location=1) in vec4 aColor;\n" +
            "\n" +
            "out vec4 fColor;\n" +
            "\n" +
            "void main(){\n" +
            "    fColor = aColor;\n" +
            "    gl_Position = vec4(aPos, 1.0);\n" +
            "}";

    private String fragmentShaderSrc = "#version 330 core\n" +
            "\n" +
            "in vec4 fColor;\n" +
            "out vec4 color;\n" +
            "\n" +
            "void main(){\n" +
            "    color = fColor;\n" +
            "}";

    private int vertexID, fragmentID, shaderProgram;
    private float[] vertexArray = {
        //position           //color
         0.5f, -0.5f, 0.0f,   1.0f, 0.0f, 0.0f, 1.0f, //Bottom right   0
        -0.5f, 0.5f, 0.0f,    0.0f, 1.0f, 0.0f, 1.0f, //Top left       1
         0.5f, 0.5f, 0.0f,    0.0f, 0.0f, 1.0f, 1.0f,  //Top right     2
        -0.5f, -0.5f, 0.0f,   1.0f, 1.0f, 0.0f, 1.0f //Bottom left    3
    };

    private int[] elementArray = {
//      NOTE: HAS TO BE COUNTER CLOCKWISE ORDER
        2, 1, 0,     // Top right triangle
        0, 1, 3     // Bottom left triangle
    };

    private int vaoID, vboID, eboID;

    public LevelEditorScene(){

    }

    @Override
    //        Compile and link shaders
    public void init(){
    // load and compile vertex shader
        vertexID = glCreateShader(GL_VERTEX_SHADER);
    // pass the shader src to the gpu
        glShaderSource(vertexID, vertexShaderSrc);;
        glCompileShader(vertexID);

    // Check for errors in the process
        int success = glGetShaderi(vertexID, GL_COMPILE_STATUS);
        if(success == GL_FALSE){
            int len = glGetShaderi(vertexID, GL_INFO_LOG_LENGTH);
            System.out.println("ERROR: 'defaultShader.glsl'\n\tVertex Shader compilaition failed");
            System.out.println(glGetShaderInfoLog(vertexID, len));
            assert false : "";
        }

        // load and compile fragment shader
        fragmentID = glCreateShader(GL_FRAGMENT_SHADER);
        // pass the fragment shader src to the gpu
        glShaderSource(fragmentID, fragmentShaderSrc);;
        glCompileShader(fragmentID);

        // Check for errors in the process
        success = glGetShaderi(fragmentID, GL_COMPILE_STATUS);
        if(success == GL_FALSE){
            int len = glGetShaderi(fragmentID, GL_INFO_LOG_LENGTH);
            System.out.println("ERROR: 'defaultShader.glsl'\n\tFragment Shader compilaition failed");
            System.out.println(glGetShaderInfoLog(fragmentID, len));
            assert false : "";
        }

        // Link shaders and check for errors
        shaderProgram = glCreateProgram();
        glAttachShader(shaderProgram, vertexID);
        glAttachShader(shaderProgram, fragmentID);
        glLinkProgram(shaderProgram);

        success = glGetProgrami(shaderProgram, GL_LINK_STATUS);
        if(success == GL_FALSE){
            int len = glGetProgrami(shaderProgram, GL_INFO_LOG_LENGTH);
            System.out.println("ERROR: 'defaultShader.glsl'\n\tLinking shaders failed");
            System.out.println(glGetProgramInfoLog(shaderProgram, len));
            assert false : "";
        }

//      Generate VAO (Vertex Array Object), VBO (Vertex Buffer Object), EBO (Element Buffer Object) buffers -> Send them to the gpu
        vaoID = glGenVertexArrays();
        glBindVertexArray(vaoID);

//      Create a float buffer of size and populate it
        FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(vertexArray.length);
        vertexBuffer.put(vertexArray).flip();

//      Create the VBO and populate it with vertexBuffer
        vboID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferData(GL_ARRAY_BUFFER, vertexBuffer, GL_STATIC_DRAW);

//      Create indices and upload
        IntBuffer elementBuffer = BufferUtils.createIntBuffer(elementArray.length);
        elementBuffer.put(elementArray).flip();
        eboID = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboID);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, elementBuffer, GL_STATIC_DRAW);

//      Add vertex attribute pointers
        int positionsSize = 3;
        int colorSize = 4;
        int floatSizeBytes = 4;
        int vertexSizeBytes = (positionsSize + colorSize) * floatSizeBytes;
        glVertexAttribPointer(0, positionsSize, GL_FLOAT, false, vertexSizeBytes, 0);
        glEnableVertexAttribArray(0);

        glVertexAttribPointer(1, colorSize, GL_FLOAT, false, vertexSizeBytes, positionsSize*floatSizeBytes);
        glEnableVertexAttribArray(1);

    }

    @Override
    public void update(float dt) {
//      Bind shader program
        glUseProgram(shaderProgram);
//      Bind VAO
        glBindVertexArray(vaoID);
//      Enable vertex attribute pointer
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
        glDrawElements(GL_TRIANGLES, elementArray.length, GL_UNSIGNED_INT, 0);

//      Unbind Everything
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);

        glBindVertexArray(0);
        glUseProgram(0);

    }
}
