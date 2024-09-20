package Physics2D.forces;

import Physics2D.rigidbody.Rigidbody2D;

public interface ForceGenerator {
    void updateForce(Rigidbody2D body, float dt);
}
