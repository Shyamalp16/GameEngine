package Physics2D.primitives;

import Physics2D.rigidbody.Rigidbody2D;
import org.joml.Vector2f;
import util.VectorMath;


public class Box2D {
    private Vector2f size = new Vector2f();
    private Vector2f halfSize = new Vector2f();
    private Rigidbody2D rigidbody = null;

    public Box2D(){
        this.halfSize = new Vector2f(size).div(2);
    }

    public Box2D(Vector2f min, Vector2f max){
        this.size = new Vector2f(max).sub(min);
        this.halfSize = new Vector2f(size).div(2);
    }

    public Vector2f getLocalMin(){
        return new Vector2f(this.rigidbody.getPosition()).sub(this.halfSize);
    }

    public Vector2f getLocalMax(){
        return new Vector2f(this.rigidbody.getPosition()).add(this.halfSize);
    }

    public Vector2f[] getVertices(){
        Vector2f min = getLocalMin();
        Vector2f max = getLocalMax();

        Vector2f[] vertices = {
                new Vector2f(min.x, min.y), new Vector2f(min.x, max.y),
                new Vector2f(max.x, min.y), new Vector2f(max.x, max.y)
        };

        if(rigidbody.getRotation() != 0.0f){
            for(Vector2f vert : vertices){
                VectorMath.rotate(vert, this.getRigidbody().getRotation(), this.getRigidbody().getPosition());
            }
        }

        return vertices;
    }

    public Rigidbody2D getRigidbody(){
        return this.rigidbody;
    }

    public Vector2f getHalfSize(){
        return this.halfSize;
    }

    public void setSize(Vector2f size){
        this.size.set(size);
        this.halfSize.set(size.x/2, size.y/2);
    }

    public void setRigidbody(Rigidbody2D rb){
        this.rigidbody = rb;
    }
}
