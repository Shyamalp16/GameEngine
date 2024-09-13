package renderer;

import org.lwjgl.BufferUtils;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.stb.STBImage.stbi_image_free;
import static org.lwjgl.stb.STBImage.stbi_load;

public class Texture {
    private String filePath;
    private int texID;

    public Texture(String filePath){
        this.filePath = filePath;

//      Generate and Bind Texture on GPU
        texID = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, texID);

//      Set texture parameters
//      Repeat image in both direction if UV Coords > texture width WRAP_S = x axis, WRAP_T = y axis
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);

//      When we're stretching the image, pixelate so it doesnt blur, it will look for the nearest pixel and interpolate
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);

//      Same but shrinking the image this time around
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

//      Loding the image, getting RGB data
        IntBuffer width = BufferUtils.createIntBuffer(1);
        IntBuffer height = BufferUtils.createIntBuffer(1);
        IntBuffer channels = BufferUtils.createIntBuffer(1);

//      Load the image
        ByteBuffer image = stbi_load(filePath, width, height, channels,0);

        if(image != null){
//          Check if the image has 3 channels (RGB) or 4 channels (RGBA)
            if(channels.get(0) == 3){
                glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, width.get(0), height.get(0), 0, GL_RGB, GL_UNSIGNED_BYTE, image);
            }else if(channels.get(0) == 4){
    //          Loading image to the GPU to render
                glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width.get(0), height.get(0), 0, GL_RGBA, GL_UNSIGNED_BYTE, image);
            }else{
                assert false: "Error: (Texture.java) Unknown number of channels " + channels.get(0) + "!";
            }

        }else{
            assert false : "Error: (Texture.java) loading image " + filePath + ".";
        }

//      Free the allocated memory after uploading it to the GPU
        stbi_image_free(image);
    }

    public void bind(){
        glBindTexture(GL_TEXTURE_2D, texID);
    }

    public void unbind(){
        glBindTexture(GL_TEXTURE_2D, 0);
    }
}
