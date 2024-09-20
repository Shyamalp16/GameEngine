package Physics2D.primitives;

import Physics2D.rigidbody.Rigidbody2D;
import org.joml.Vector2f;


//Axis aligned bounding box (NOT ROTATED)
public class AABB {
    private Vector2f size = new Vector2f();
    private Rigidbody2D rigidbody = null;
    private Vector2f halfSize = new Vector2f();

    public AABB(){
        this.halfSize = new Vector2f(size).div(2);
    }

    public AABB(Vector2f min, Vector2f max){
        this.size = new Vector2f(max).sub(min);
        this.halfSize = new Vector2f(size).div(2);
    }

    public Vector2f getMin(){
        return new Vector2f(this.rigidbody.getPosition()).sub(this.halfSize);
    }

    public Vector2f getMax(){
        return new Vector2f(this.rigidbody.getPosition()).add(this.halfSize);
    }

    public void setRigidbody(Rigidbody2D rb){
        this.rigidbody = rb;
    }

    public void setSize(Vector2f size){
        this.size.set(size);
        this.halfSize.set(size.x/2, size.y/2);
    }
}
