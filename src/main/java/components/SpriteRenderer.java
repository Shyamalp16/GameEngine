package components;

import Core.Transform;
import engine.CImGui;
import imgui.ImGui;
import org.joml.Vector2f;
import org.joml.Vector4f;
import renderer.Texture;

public class SpriteRenderer extends Component {

    private Vector4f color = new Vector4f(1,1,1,1);
    private Sprite sprite = new Sprite();

    private transient Transform lastTransform;
    private transient boolean isDirty = true;

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
    public void EditorUpdate(float dt){
        if(!this.lastTransform.equals(this.gameObject.transform)){
            this.gameObject.transform.copy(this.lastTransform);
            isDirty = true;
        }
    }

    @Override
    public void imgui(){
        if(CImGui.colorPicker4("Color Picker", this.color)){
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

    public void setTexture(Texture texture){
        this.sprite.setTexture(texture);
    }

    public void setDirty(){
        this.isDirty = true;
    }
}
