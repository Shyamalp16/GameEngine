package renderer;

import static org.lwjgl.opengl.GL30.*;

public class FrameBuffer {
    private int fboID = 0;
    private Texture texture = null;

    public FrameBuffer(int width, int height){
//        Generate framebuffer
        fboID = glGenFramebuffers();
        glBindFramebuffer(GL_FRAMEBUFFER, fboID);

//      Create texture to render data to and attach to framebuffer
        this.texture = new Texture(width, height);
        glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D, this.texture.getId(), 0);

//      Generate RenderBuffer
        int rboID = glGenRenderbuffers();
        glBindRenderbuffer(GL_RENDERBUFFER, rboID);
        glRenderbufferStorage(GL_RENDERBUFFER, GL_DEPTH_COMPONENT32, width, height);
        glFramebufferRenderbuffer(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, GL_RENDERBUFFER, rboID);

        if(glCheckFramebufferStatus(GL_FRAMEBUFFER) != GL_FRAMEBUFFER_COMPLETE){
            assert false : "Error (FrameBuffer.java): FrameBuffer is not complete";
        }
        glBindFramebuffer(GL_FRAMEBUFFER, 0);
    }

    public int getFboID() {
        return fboID;
    }

    public int getTextureId() {
        return texture.getId();
    }

    public void bind(){
        glBindFramebuffer(GL_FRAMEBUFFER, fboID);
    }

    public void unbind(){
        glBindFramebuffer(GL_FRAMEBUFFER, 0);
    }
}
