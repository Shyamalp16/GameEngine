package Physics2D.rigidbody;

import Physics2D.primitives.*;
import org.joml.Vector2f;
import org.lwjgl.system.CallbackI;
import renderer.Line2D;
import util.VectorMath;

import javax.swing.*;

public class IntersectionDetector2D {

//  ------------------------------------ POINT VS PRIMITIVE ------------------------------------------------------------
    public static boolean pointOnLine(Vector2f point, Line2D line){
        float dY = line.getEnd().y - line.getStart().y;
        float dX = line.getEnd().x - line.getStart().x;
        if(dX == 0f){
            return VectorMath.compare(point.x, line.getStart().x);
        }
        float slope = dY/dX;

        float yIntersept = line.getEnd().y - (slope * line.getEnd().x);
//      Using the line equation
        return point.y == slope * point.x + yIntersept;
    }

    public static boolean pointInCircle(Vector2f point, Circle circle){
        Vector2f circleCenter = circle.getCenter();
        Vector2f centerToPoint = new Vector2f(point).sub(circleCenter);

        return centerToPoint.lengthSquared() <= circle.getRadius() * circle.getRadius();
    }


    public static boolean pointInAABB(Vector2f point, AABB box){
        Vector2f min = box.getMin();
        Vector2f max = box.getMax();

        return point.x <= max.x
                && min.x <= point.x
                && point.y <= max.y
                && min.y <= point.y;
    }

    public static boolean pointInBox2D(Vector2f point, Box2D box){
//        Translate points to local space
        Vector2f localPoint = new Vector2f(point);
        VectorMath.rotate(localPoint, box.getRigidbody().getRotation(), box.getRigidbody().getPosition());
        Vector2f min = box.getMin();
        Vector2f max = box.getMax();

        return localPoint.x <= max.x
                && min.x <= localPoint.x
                && localPoint.y <= max.y
                && min.y <= localPoint.y;
    }

//  ------------------------------------ LINES VS PRIMITIVE ------------------------------------------------------------
    public static boolean lineAndCircle(Line2D line, Circle circle){
        if(pointInCircle(line.getStart(), circle) || pointInCircle(line.getEnd(), circle)){
            return true;
        }

        Vector2f ab = new Vector2f(line.getEnd().sub(line.getStart()));
//      Find dot product
        Vector2f circleCenter = circle.getCenter();
        Vector2f centerToLineStart = new Vector2f(circleCenter).sub(line.getStart());
        float t = centerToLineStart.dot(ab) / ab.dot(ab);

        if(t < 0.0f || t > 1.0f){
            return false;
        }

//      Find closest point
        Vector2f closestPoint = new Vector2f(line.getStart()).add(ab.mul(t));
        return pointInCircle(closestPoint, circle);
    }

//  ------------------------------------ LINES VS BOX2D ----------------------------------------------------------------
    public static boolean lineAndAABB(Line2D line, AABB box){
        if(pointInAABB(line.getStart(), box) || pointInAABB((line.getEnd()), box)){
            return true;
        }
//        I HAVE NO IDEA WHAT IM DOING RN
        Vector2f unitVector = new Vector2f(line.getEnd()).sub(line.getStart());
        unitVector.normalize();
        unitVector.x = (unitVector.x != 0 ) ? 1.0f / unitVector.x : 0f;
        unitVector.y = (unitVector.y != 0 ) ? 1.0f / unitVector.y : 0f;

        Vector2f min = box.getMin();
        min.sub(line.getStart()).mul(unitVector);

        Vector2f max = box.getMax();
        max.sub(line.getStart()).mul(unitVector);

        float tmin = Math.max(Math.min(min.x, max.x), Math.min(min.y, max.y));
        float tmax = Math.min(Math.max(min.x, max.x), Math.max(min.y, max.y));

        if(tmax < 0 || tmin > tmax){
            return false;
        }

        float t = (tmin < 0f) ? tmax : tmin;
        return t > 0 && t * t < line.lengthSquared();
    }

    public static  boolean lineAndBox2D(Line2D line, Box2D box){

//      Rotate the box
        float theta = -box.getRigidbody().getRotation();
        Vector2f center = box.getRigidbody().getPosition();
        Vector2f localStart = new Vector2f(line.getStart());
        Vector2f localEnd = new Vector2f(line.getEnd());


        VectorMath.rotate(localStart, theta, center);
        VectorMath.rotate(localEnd, theta, center);

//      Construct a new line and send it to the function above
        Line2D localLine = new Line2D(localStart, localEnd);
        AABB aabb = new AABB(box.getMin(), box.getMax());
        return lineAndAABB(localLine, aabb);
    }

//  ------------------------------------ RAYCAST -----------------------------------------------------------------------
    public static boolean Raycast(Circle circle, Ray2D ray, RaycastResult result){
        RaycastResult.reset(result);
        Vector2f originToCircle = new Vector2f(circle.getCenter()).sub(ray.getOrigin());
        float radiusSquared = circle.getRadius() * circle.getRadius();
        float originToCircleLengthSquared = originToCircle.lengthSquared();

//      Project the vector from the ray origin onto the direction or ray
        float a = originToCircle.dot(ray.getDirection());
        float bSquared = originToCircleLengthSquared - (a*a);
        if((radiusSquared - bSquared) < 0.0f){
//          Negative
            return false;
        }

        float f = (float)Math.sqrt(radiusSquared - bSquared);
        float t = 0;
        if(originToCircleLengthSquared < radiusSquared){
//            Ray starts inside the circle
            t = a + f;
        }else{
            t = a - f;
        }

        if(result != null){
            Vector2f point = new Vector2f(ray.getOrigin()).add(new Vector2f(ray.getDirection()).mul(t));
            Vector2f normal = new Vector2f(point).sub(circle.getCenter());
            result.init(point, normal, t, true);
        }
        return true;
    }

    public static boolean Raycast(AABB box, Ray2D ray, RaycastResult result){
        RaycastResult.reset(result);
        Vector2f unitVector = ray.getDirection();
        unitVector.normalize();
        unitVector.x = (unitVector.x != 0 ) ? 1.0f / unitVector.x : 0f;
        unitVector.y = (unitVector.y != 0 ) ? 1.0f / unitVector.y : 0f;

        Vector2f min = box.getMin();
        min.sub(ray.getOrigin()).mul(unitVector);

        Vector2f max = box.getMax();
        max.sub(ray.getOrigin()).mul(unitVector);

        float tmin = Math.max(Math.min(min.x, max.x), Math.min(min.y, max.y));
        float tmax = Math.min(Math.max(min.x, max.x), Math.max(min.y, max.y));

        if(tmax < 0 || tmin > tmax){
            return false;
        }

        float t = (tmin < 0f) ? tmax : tmin;
        boolean hit = t > 0; // t * t < ray.getMaximum();
        if(!hit){
            return false;
        }

        if(result != null){
            Vector2f point = new Vector2f(ray.getOrigin()).add(new Vector2f(ray.getDirection()).mul(t));
            Vector2f normal = new Vector2f(ray.getOrigin()).sub(point);
            normal.normalize();
            result.init(point, normal, t, true);
        }
        return true;
    }

    public static boolean Raycast(Box2D box, Ray2D ray, RaycastResult result){
        RaycastResult.reset(result);
        Vector2f size = box.getHalfSize();
        Vector2f xAxis = new Vector2f(1, 0);
        Vector2f yAxis = new Vector2f(0, 1);
        VectorMath.rotate(xAxis, -box.getRigidbody().getRotation(), new Vector2f(0, 0));
        VectorMath.rotate(yAxis, -box.getRigidbody().getRotation(), new Vector2f(0, 0));

        Vector2f p = new Vector2f(box.getRigidbody().getPosition()).sub(ray.getOrigin());

//      Project the direction of ray onto each axis of the box
        Vector2f f = new Vector2f(xAxis.dot(ray.getDirection()), yAxis.dot(ray.getDirection()));

//      Project p onto every axis of the box
        Vector2f e = new Vector2f(xAxis.dot(p), yAxis.dot(p));

        float[] tArr = {0,0,0,0};
        for(int i=0; i < 2; i++){
            if(VectorMath.compare(f.get(i), 0)){
//                If ray is parallel to current axis and if origin is not inside, no hit
                if(((-e.get(i) - size.get(i)) > 0) || ((-e.get(i) + size.get(i) < 0))){
                    return false;
                }
                f.setComponent(i, 0.00001f); // set to small to avoid divide by 0
            }
            tArr[i*2] = (e.get(i) + size.get(i)) / f.get(i);
            tArr[i*2+1] = (e.get(i) - size.get(i)) / f.get(i);
        }

        float tmin = Math.max(Math.min(tArr[0], tArr[1]), Math.min(tArr[2], tArr[3]));
        float tmax = Math.min(Math.max(tArr[0], tArr[1]), Math.max(tArr[2], tArr[3]));

        float t = (tmin < 0f) ? tmax : tmin;
        boolean hit = t > 0; // t * t < ray.getMaximum();
        if(!hit){
            return false;
        }

        if(result != null){
            Vector2f point = new Vector2f(ray.getOrigin()).add(new Vector2f(ray.getDirection()).mul(t));
            Vector2f normal = new Vector2f(ray.getOrigin()).sub(point);
            normal.normalize();
            result.init(point, normal, t, true);
        }
        return true;
    }

//  ----------------------------------- CIRCLE VS CIRCLE ---------------------------------------------------------------
    public static boolean circleAndLine(Circle circle, Line2D line){
        return lineAndCircle(line, circle);
    }

    public static boolean circleAndCircle(Circle c1, Circle c2){
        Vector2f vectorBetweenCircles = new Vector2f(c1.getCenter()).sub(c2.getCenter());
        float radiiSum = c1.getRadius() + c2.getRadius();
        return vectorBetweenCircles.lengthSquared() <= radiiSum * radiiSum;
    }

//  ----------------------------------- CIRCLE VS AABB -----------------------------------------------------------------
    public static boolean circleAndAABB(Circle c1, AABB box){
        Vector2f min = box.getMin();
        Vector2f max = box.getMax();

        Vector2f closestPointToCircle = new Vector2f(c1.getCenter());
        if(closestPointToCircle.x  < min.x){
            closestPointToCircle.x = min.x;
        }else if(closestPointToCircle.x > max.x){
            closestPointToCircle.x = max.x;
        }

        if(closestPointToCircle.y  < min.y){
            closestPointToCircle.y = min.y;
        }else if(closestPointToCircle.y > max.y){
            closestPointToCircle.y = max.y;
        }

        Vector2f circleToBox = new Vector2f(c1.getCenter()).sub(closestPointToCircle);
        return circleToBox.lengthSquared() <= c1.getRadius() * c1.getRadius();
    }

//  ----------------------------------- CIRCLE VS BOX2D ----------------------------------------------------------------
    public static boolean circleAndBox2D(Circle c1, Box2D box){
//      Treat the box like AABB after rotating it
        Vector2f min = new Vector2f();
        Vector2f max = new Vector2f(box.getHalfSize()).mul(2.0f);

//      Create circle in boxes local space
        Vector2f r = new Vector2f(c1.getCenter()).sub(box.getRigidbody().getPosition());
        VectorMath.rotate(r, -box.getRigidbody().getRotation(), new Vector2f(0,0));
        Vector2f localCirclePos = new Vector2f(r).add(box.getHalfSize());

        Vector2f closestPointToCircle = new Vector2f(localCirclePos);
        if(closestPointToCircle.x  < min.x){
            closestPointToCircle.x = min.x;
        }else if(closestPointToCircle.x > max.x){
            closestPointToCircle.x = max.x;
        }

        if(closestPointToCircle.y  < min.y){
            closestPointToCircle.y = min.y;
        }else if(closestPointToCircle.y > max.y){
            closestPointToCircle.y = max.y;
        }

        Vector2f circleToBox = new Vector2f(localCirclePos).sub(closestPointToCircle);
        return circleToBox.lengthSquared() <= c1.getRadius() * c1.getRadius();
    }

//  ----------------------------------- AABB VS PRIMITIVES -------------------------------------------------------------
    public static boolean AABBAndCricle(AABB box,Circle c1){
        return circleAndAABB(c1, box);
    }

    public static boolean AABBAndAABB(AABB box1,AABB box2){
        Vector2f axisToTest[] = {new Vector2f(0,1), new Vector2f(1,0)};
        for(int i=0; i < axisToTest.length; i++){
            if(!overlapOnAxis(box1, box2, axisToTest[i])){
                return false;
            }
        }
        return true;
    }

    public static boolean AABBAndBox2D(AABB box1, Box2D box2){
        Vector2f axisToTest[] = {new Vector2f(0,1), new Vector2f(1,0),
                                 new Vector2f(0, 1), new Vector2f(1,0)
        };

        VectorMath.rotate(axisToTest[2], box2.getRigidbody().getRotation(), box2.getRigidbody().getPosition());
        VectorMath.rotate(axisToTest[2], box2.getRigidbody().getRotation(), new Vector2f());

        for(int i=0; i < axisToTest.length; i++){
            if(!overlapOnAxis(box1, box2, axisToTest[i])){
                return false;
            }
        }
        return true;
    }


//  ----------------------------------- Separating Axis Theorem Helpers ------------------------------------------------
    private static boolean  overlapOnAxis(AABB b1, AABB b2, Vector2f axis){
        Vector2f interval1 = getInterval(b1, axis);
        Vector2f interval2 = getInterval(b2, axis);
        return((interval2.x <= interval1.y) && (interval1.x <= interval2.x));
    }

    private static boolean  overlapOnAxis(AABB b1, Box2D b2, Vector2f axis){
        Vector2f interval1 = getInterval(b1, axis);
        Vector2f interval2 = getInterval(b2, axis);
        return((interval2.x <= interval1.y) && (interval1.x <= interval2.x));
    }

    private static boolean  overlapOnAxis(Box2D b1, Box2D b2, Vector2f axis){
        Vector2f interval1 = getInterval(b1, axis);
        Vector2f interval2 = getInterval(b2, axis);
        return((interval2.x <= interval1.y) && (interval1.x <= interval2.x));
    }

    private static Vector2f getInterval(AABB rect, Vector2f axis){
        Vector2f result = new Vector2f(0, 0);
        Vector2f min = rect.getMin();
        Vector2f max = rect.getMax();

        Vector2f[] vertices = {
            new Vector2f(min.x, min.y), new Vector2f(min.x, max.y),
            new Vector2f(max.x, min.y), new Vector2f(max.x, max.y),
        };

        result.x = axis.dot(vertices[0]);
        result.y = result.x;

        for(int i=1; i < 4; i++){
            float projection = axis.dot(vertices[i]);
            if(projection < result.x){
                result.x = projection;
            }
            if(projection > result.y){
                result.y = projection;
            }
        }
        return result;
    }

    private static Vector2f getInterval(Box2D rect, Vector2f axis){
        Vector2f result = new Vector2f(0, 0);

        Vector2f[] vertices = rect.getVertices();

        result.x = axis.dot(vertices[0]);
        result.y = result.x;

        for(int i=1; i < 4; i++){
            float projection = axis.dot(vertices[i]);
            if(projection < result.x){
                result.x = projection;
            }
            if(projection > result.y){
                result.y = projection;
            }
        }
        return result;
    }

}
