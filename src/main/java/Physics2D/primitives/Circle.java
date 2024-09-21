package Physics2D.primitives;

import Physics2D.rigidbody.Rigidbody2D;
import org.joml.Vector2f;

public class Circle extends Collider2D{
    private float radius = 1.0f;
    private Rigidbody2D rigidbody = null;

    public float getRadius() {
        return this.radius;
    }

    public Vector2f getCenter(){
        return rigidbody.getPosition();
    }

    public void setRadius(float r){
        this.radius = r;
    }

    public void setRigidbody(Rigidbody2D rb){
        this.rigidbody = rb;
    }
}
