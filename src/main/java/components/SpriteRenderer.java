package components;

import Core.Component;
import Core.Transform;
import imgui.ImGui;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import renderer.Texture;

public class SpriteRenderer extends Component {

    private Vector4f color = new Vector4f(1,1,1,1);
    private Sprite sprite = new Sprite();

    private transient Transform lastTransform;
    private transient boolean isDirty = false;

//    public SpriteRenderer(Vector4f color){
//        this.color = color;
//        this.sprite = new Sprite(null);
//        this.isDirty = true;
//    }
//
//    public SpriteRenderer(Sprite sprite){
//        this.sprite = sprite;
//        this.color = new Vector4f(1,1,1,1);
//        this.isDirty = true;
//    }



    public Texture getTexture(){
        return sprite.getTexture();
    }

    public Vector2f[] getTexCoords() {

        return sprite.getTexCoords();
    }

    @Override
    public void start(){
        this.lastTransform = gameObject.transform.copy();
    }

    @Override
    public void update(float dt){
        if(!this.lastTransform.equals(this.gameObject.transform)){
            this.gameObject.transform.copy(this.lastTransform);
            isDirty = true;
        }
    }

    @Override
    public void imgui(){
        float[] imColor = {color.x, color.y, color.z, color.w};
        if(ImGui.colorPicker4("Color Picker", imColor)){
            this.color.set(imColor[0], imColor[1], imColor[2], imColor[3]);
            this.isDirty = true;
        }
    }

    public Vector4f getColor(){
        return this.color;
    }
    
    public void setSprite(Sprite sprite){
        if(!this.sprite.equals(sprite)){
            this.sprite = sprite;
            this.isDirty = true;
        }
    }

    public boolean isDirty(){
        return isDirty;
    }

    public void setClean(){
        this.isDirty = false;
    }


    public void setColor(Vector4f color){
        if(!this.color.equals(color)){
            this.isDirty = true;
            this.color.set(color);
        }
    }
}
