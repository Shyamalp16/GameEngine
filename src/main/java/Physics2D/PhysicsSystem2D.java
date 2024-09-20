package Physics2D;

import Physics2D.forces.ForceRegistry;
import Physics2D.forces.Gravity2D;
import Physics2D.rigidbody.Rigidbody2D;
import org.joml.Vector2f;

import java.util.ArrayList;
import java.util.List;

public class PhysicsSystem2D {
    private ForceRegistry forceRegistry;
    private List<Rigidbody2D> rigidBodies;
    private Gravity2D gravity;
    private float fixedUpdate;

    public PhysicsSystem2D(float fixedUpdateDt, Vector2f gravity){
        this.forceRegistry = new ForceRegistry();
        this.rigidBodies = new ArrayList<>();
        this.gravity = new Gravity2D(gravity);
        this.fixedUpdate = fixedUpdateDt;
    }

    public void update(float dt){
        fixedUpdate();
    }

    public void fixedUpdate(){
        forceRegistry.updateForces(fixedUpdate);

//      Update velocities of all rigidbodies
        for(int i = 0; i < rigidBodies.size(); i++){
            rigidBodies.get(i).physicsUpdate(fixedUpdate);
        }
    }

    public void addRigidbody(Rigidbody2D body){
        this.rigidBodies.add(body);
        this.forceRegistry.add(body, gravity);
    }
}
