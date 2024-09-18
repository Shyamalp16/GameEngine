package Core;


import components.Sprite;
import components.SpriteRenderer;
import components.Spritesheet;
import org.joml.Vector2f;
import org.joml.Vector4f;
import util.AssetPool;

public class LevelEditorScene extends Scene {

    private GameObject obj1, obj2;
    private Spritesheet sprites;
    public LevelEditorScene(){

    }
    @Override
    public void init() {
        loadResources();
        this.camera = new Camera(new Vector2f(new Vector2f(-250, 0)));
        sprites = AssetPool.getSpritesheet("D:\\GameEngine\\assets\\images\\LanceRunningAnimation.png");
        obj1 = new GameObject("Obj1", new Transform(new Vector2f(600, 100), new Vector2f(128, 128)), -1);
//        obj1.addComponent(new SpriteRenderer(new Sprite(AssetPool.getTexture("D:\\GameEngine\\assets\\images\\1.png"))));
        obj1.addComponent(new SpriteRenderer(sprites.getSprite(0)));
        this.addGameObjectToScene(obj1);

        obj2 = new GameObject("Obj2", new Transform(new Vector2f(300, 100), new Vector2f(128, 128)), -1);
        obj2.addComponent(new SpriteRenderer(sprites.getSprite(1)));
        this.addGameObjectToScene(obj2);
    }

    private void loadResources(){
        AssetPool.getShader("D:\\GameEngine\\assets\\shaders\\default.glsl");
        AssetPool.addSpriteSheet("D:\\GameEngine\\assets\\images\\LanceRunningAnimation.png", new Spritesheet(AssetPool.getTexture("D:\\GameEngine\\assets\\images\\LanceRunningAnimation.png"), 20, 33, 6, 0 ));
    }

    private int spriteIndex = 0;
    private float spriteFlipTime = 0.15f;
    private float spriteFlipTimeLeft = 0.0f;

    @Override
    public void update(float dt) {
        spriteFlipTimeLeft -= dt;
        if(spriteFlipTimeLeft <= 0){
            spriteFlipTimeLeft = spriteFlipTime;
            spriteIndex++;
            if(spriteIndex > 5){
                spriteIndex = 0;
            }
            obj1.getComponent(SpriteRenderer.class).setSprite(sprites.getSprite(spriteIndex));
        }

        obj1.transform.position.x -= 80 * dt;
        System.out.println("FPS " + (1.0f/dt));
        for(GameObject go : this.gameObjects){
            go.update(dt);
        }

        this.renderer.render();
    }
}
