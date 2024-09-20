package Physics2D.rigidbody;

import components.Component;
import org.joml.Vector2f;

public class Rigidbody2D extends Component {
    private Vector2f position = new Vector2f();
    private float rotation = 0.0f;

    public Vector2f getPosition() {
        return this.position;
    }

    public void setPosition(Vector2f position) {
        this.position = position;
    }

    public float getRotation() {
        return this.rotation;
    }

    public void setRotation(float rotation) {
        this.rotation = rotation;
    }
}
