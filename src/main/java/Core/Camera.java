package Core;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class Camera {
    private Matrix4f projectionMatrix, viewMatrix, inverseProjection, inverseView;
    public Vector2f position;
    private float projectWidth = 6;
    private float projectHeight = 3;
    private Vector2f projectionSize = new Vector2f(projectWidth,projectHeight);

    private float zoom = 1.0f;

    public Camera(Vector2f position){
        this.position = position;
//      initialize it with 0 so the starting position is not messed up
        this.projectionMatrix = new Matrix4f();
        this.viewMatrix = new Matrix4f();
        this.inverseProjection = new Matrix4f();
        this.inverseView = new Matrix4f();
        adjustProjection();
    }

    public void adjustProjection(){
//      makes it identity matrix
        projectionMatrix.identity();
        projectionMatrix.ortho(0.0f, projectionSize.x * this.zoom, 0.0f, projectionSize.y * zoom, 0.0f, 100.0f);
        projectionMatrix.invert(inverseProjection);
    }

    public Matrix4f getViewMatrix(){
//      Camera Looking at -1 in z direction and 1 in y direction
        Vector3f cameraFront = new Vector3f(0.0f, 0.0f, -1.0f);
        Vector3f cameraUp = new Vector3f(0.0f, 1.0f, 0.0f);
        this.viewMatrix.identity();
//      Where the camera is located at
        viewMatrix.lookAt(new Vector3f(position.x, position.y, 20.0f), cameraFront.add(position.x, position.y, 0.0f), cameraUp);
        this.viewMatrix.invert(inverseView);
        return this.viewMatrix;
    }

    public Matrix4f getProjectionMatrix(){
        return this.projectionMatrix;
    }

    public Matrix4f getInverseProjection(){
        return this.inverseProjection;
    }

    public Matrix4f getInverseView(){
        return this.inverseView;
    }

    public Vector2f getProjectionSize(){
        return this.projectionSize;
    }

    public float getZoom() {
        return zoom;
    }

    public void setZoom(float zoom) {
        this.zoom = zoom;
    }

    public void addZoom(float value){
        this.zoom += value;
    }
}
