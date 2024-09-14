package renderer;

import org.joml.*;
import org.lwjgl.BufferUtils;

import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL20.glGetShaderInfoLog;

public class Shader {

    private int shaderProgramID;
    private boolean beingUsed = false;
    private String vertexSrc;
    private String fragmentSrc;
    private String filePath;

    public Shader(String filePath){
        this.filePath = filePath;
        try{

//          NOTE: Separating #type vertex and #type fragment
            String source = new String(Files.readAllBytes(Paths.get(filePath)));
            String[] splitString = source.split("(#type)( )+([a-zA-z]+)");
            int index = source.indexOf("#type")+6;
            int eol = source.indexOf("\r\n", index);

//          trim after the first #type and get the keyword
            String firstPattern = source.substring(index, eol).trim();

//          Do the same for the second #type
            index = source.indexOf("#type", eol)+6;
            eol = source.indexOf("\r\n", index);
            String secondPattern = source.substring(index, eol).trim();

            if(firstPattern.equals("vertex")){
                vertexSrc = splitString[1];
            }else if(firstPattern.equals("fragment")){
                fragmentSrc = splitString[1];
            }else{
                throw new IOException("Unexpected token " + firstPattern);
            }

            if(secondPattern.equals("vertex")){
                vertexSrc = splitString[2];
            }else if(secondPattern.equals("fragment")){
                fragmentSrc = splitString[2];
            }else{
                throw new IOException("Unexpected token " + secondPattern);
            }
        }catch(IOException e){
            e.printStackTrace();
            assert false : "Error: Couldnt open shader files" + filePath + "!";
        }
    }

    public void compile(){
        int vertexID, fragmentID;

        // load and compile vertex shader
        vertexID = glCreateShader(GL_VERTEX_SHADER);
        // pass the shader src to the gpu
        glShaderSource(vertexID, vertexSrc);;
        glCompileShader(vertexID);

        // Check for errors in the process
        int success = glGetShaderi(vertexID, GL_COMPILE_STATUS);
        if(success == GL_FALSE){
            int len = glGetShaderi(vertexID, GL_INFO_LOG_LENGTH);
            System.out.println("ERROR: '" + filePath + "'\n\tVertex Shader compilaition failed");
            System.out.println(glGetShaderInfoLog(vertexID, len));
            assert false : "";
        }

        // load and compile fragment shader
        fragmentID = glCreateShader(GL_FRAGMENT_SHADER);
        // pass the fragment shader src to the gpu
        glShaderSource(fragmentID, fragmentSrc);;
        glCompileShader(fragmentID);

        // Check for errors in the process
        success = glGetShaderi(fragmentID, GL_COMPILE_STATUS);
        if(success == GL_FALSE){
            int len = glGetShaderi(fragmentID, GL_INFO_LOG_LENGTH);
            System.out.println("ERROR: '"+ filePath + "'\n\tFragment Shader compilaition failed");
            System.out.println(glGetShaderInfoLog(fragmentID, len));
            assert false : "";
        }


        // Link shaders and check for errors
        shaderProgramID = glCreateProgram();
        glAttachShader(shaderProgramID, vertexID);
        glAttachShader(shaderProgramID, fragmentID);
        glLinkProgram(shaderProgramID);

        success = glGetProgrami(shaderProgramID, GL_LINK_STATUS);
        if(success == GL_FALSE){
            int len = glGetProgrami(shaderProgramID, GL_INFO_LOG_LENGTH);
            System.out.println("ERROR: '" + filePath + "'\n\tLinking shaders failed");
            System.out.println(glGetProgramInfoLog(shaderProgramID, len));
            assert false : "";
        }
    }

    public void use(){
        if(!beingUsed){
    //      Bind shader program
            glUseProgram(shaderProgramID);
            beingUsed = true;
        }
    }

    public void detach(){
        glUseProgram(0);
        beingUsed = false;
    }

    public void uploadMat4f(String varName, Matrix4f mat4){
//      get the location of variable shaderProgramID
        int varLocation = glGetUniformLocation(shaderProgramID, varName);
        use();
//      Create a buffer 16 variables wide (4*4)
        FloatBuffer matBuffer = BufferUtils.createFloatBuffer(16);
//      Flatten the buffer and Make it 1D array [1,1,1,1..]
        mat4.get(matBuffer);
        glUniformMatrix4fv(varLocation, false, matBuffer);
    }

    public void uploadMat3f(String varName, Matrix3f mat3){
        int varLocation = glGetUniformLocation(shaderProgramID, varName);
        use();
        FloatBuffer matBuffer = BufferUtils.createFloatBuffer(9);
        mat3.get(matBuffer);
        glUniformMatrix3fv(varLocation, false, matBuffer);
    }

//  Upload vec, float and int
    public void uploadVec4f(String varName, Vector4f vec){
        int varLocation = glGetUniformLocation(shaderProgramID, varName);
        use();
        glUniform4f(varLocation, vec.x, vec.y, vec.z, vec.w);
    }

    public void uploadVec3f(String varName, Vector3f vec){
        int varLocation = glGetUniformLocation(shaderProgramID, varName);
        use();
        glUniform3f(varLocation, vec.x, vec.y, vec.z);
    }

    public void uploadVec2f(String varName, Vector2f vec){
        int varLocation = glGetUniformLocation(shaderProgramID, varName);
        use();
        glUniform2f(varLocation, vec.x, vec.y);
    }

    public void uploadFloat(String varName, float val){
        int varLocation = glGetUniformLocation(shaderProgramID, varName);
        use();
        glUniform1f(varLocation, val);
    }

    public void uploadInt(String varName, int val){
        int varLocation = glGetUniformLocation(shaderProgramID, varName);
        use();
        glUniform1i(varLocation, val);
    }

    public void uploadTexture(String varName, int slot){
        int varLocation = glGetUniformLocation(shaderProgramID, varName);
        use();
        glGetUniformi(varLocation, slot);
    }

    public void uploadIntArray(String varName, int[] array){
        int varLocation = glGetUniformLocation(shaderProgramID, varName);
        use();
        glUniform1iv(varLocation, array);
    }
}
