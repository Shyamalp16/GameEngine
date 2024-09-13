package Core;

import components.FontRenderer;
import components.SpriteRenderer;
import org.joml.Vector2f;
import org.lwjgl.BufferUtils;
import renderer.Shader;
import renderer.Texture;
import util.Time;

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

//    private float[] vertexArray = {
////       position           //color                        // UV Coordinates
//            100f,   0f, 0.0f,   1.0f, 0.0f, 0.0f, 1.0f,        1,1,               //Bottom right   0
//            0f,   100f, 0.0f,   0.0f, 1.0f, 0.0f, 1.0f,        0,0,               //Top left       1
//            100f, 100f, 0.0f,   1.0f, 0.0f, 1.0f, 1.0f,        1,0,               //Top right      2
//            0f,     0f, 0.0f,   1.0f, 1.0f, 0.0f, 1.0f,        0,1,               //Bottom left    3
//    };

    private float[] vertexArray = {
//       position           //color                        // UV Coordinates
            100f,   0f, 0.0f,   1.0f, 0.0f, 0.0f, 1.0f,        1,1,               //Bottom right   0
            0f,   100f, 0.0f,   0.0f, 1.0f, 0.0f, 1.0f,        0,0,               //Top left       1
            100f, 100f, 0.0f,   1.0f, 0.0f, 1.0f, 1.0f,        1,0,               //Top right      2
            0f,     0f, 0.0f,   1.0f, 1.0f, 0.0f, 1.0f,        0,1,               //Bottom left    3
    };

    private int[] elementArray = {
//      NOTE: HAS TO BE COUNTER CLOCKWISE ORDER
        2, 1, 0,     // Top right triangle
        0, 1, 3     // Bottom left triangle
    };

    private int vaoID, vboID, eboID;
    private Shader defaultShader;
    private Texture testTexture;

    GameObject testObj;
    private boolean firstTime;

    public LevelEditorScene(){

    }

    @Override
    //        Compile and link shaders
    public void init(){
        System.out.println("Creating test object");
        this.testObj = new GameObject("Test Object");
        this.testObj.addComponent(new SpriteRenderer());
        this.testObj.addComponent(new FontRenderer());
        this.addGameObjectToScene(this.testObj);

        this.camera = new Camera(new Vector2f(-200, -300));
//     Compiling the shaders
        defaultShader = new Shader("D:\\GameEngine\\assets\\shaders\\default.glsl");
        defaultShader.compile();
        this.testTexture = new Texture("D:\\GameEngine\\assets\\images\\PlayerCharacterMain.png");
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
        int uvSize = 2;

        int vertexSizeBytes = (positionsSize + colorSize + uvSize) * Float.BYTES;
        glVertexAttribPointer(0, positionsSize, GL_FLOAT, false, vertexSizeBytes, 0);
        glEnableVertexAttribArray(0);

        glVertexAttribPointer(1, colorSize, GL_FLOAT, false, vertexSizeBytes, positionsSize * Float.BYTES);
        glEnableVertexAttribArray(1);

        glVertexAttribPointer(2, uvSize, GL_FLOAT, false, vertexSizeBytes, (positionsSize + colorSize) * Float.BYTES);
        glEnableVertexAttribArray(2);
    }

    @Override
    public void update(float dt) {
        defaultShader.use();
//      Upload Texture to shader
        defaultShader.uploadTexture("TEX_SAMPLER", 0);
        glActiveTexture(GL_TEXTURE0);
        testTexture.bind();

        defaultShader.uploadMat4f("uProjection", camera.getProjectionMatrix());
        defaultShader.uploadMat4f("uView", camera.getViewMatrix());
        defaultShader.uploadFloat("uTime", Time.getTime());
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
        defaultShader.detach();

        if(!firstTime){
            System.out.println("Creating game object in LEvelEditorScene update method");
            GameObject go = new GameObject("Game test 2");
            go.addComponent(new SpriteRenderer());
            this.addGameObjectToScene(go);
            firstTime = true;
        }

        for(GameObject go : this.gameObjects){
            go.update(dt);
        }
    }
}
