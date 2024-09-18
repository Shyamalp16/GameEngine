package Core;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import components.Rigidbody;
import components.Sprite;
import components.SpriteRenderer;
import components.Spritesheet;
import imgui.ImGui;
import org.joml.Vector2f;
import org.joml.Vector4f;
import util.AssetPool;

public class LevelEditorScene extends Scene {

    private GameObject obj1, obj2;
    private Spritesheet sprites;
    private SpriteRenderer obj1Sprite;
    public LevelEditorScene(){

    }
    @Override
    public void init() {
        loadResources();
        this.camera = new Camera(new Vector2f(new Vector2f(-250, 0)));
        if(levelLoaded){
            this.activeGameObject = gameObjects.get(0);
            return;
        }

        sprites = AssetPool.getSpritesheet("D:\\GameEngine\\assets\\images\\LanceRunningAnimation.png");

        obj1 = new GameObject("Obj1", new Transform(new Vector2f(200, 100), new Vector2f(128, 128)), 2);
//        obj1.addComponent(new SpriteRenderer(new Sprite(AssetPool.getTexture("D:\\GameEngine\\assets\\images\\1.png"))));
        obj1Sprite = new SpriteRenderer();
        obj1Sprite.setColor(new Vector4f(0,1,0,1));
        obj1.addComponent(obj1Sprite);
        obj1.addComponent(new Rigidbody());
        this.addGameObjectToScene(obj1);
        this.activeGameObject = obj1;
//        obj1.addComponent(new SpriteRenderer(sprites.getSprite(0)));


//        obj2 = new GameObject("Obj2", new Transform(new Vector2f(300, 100), new Vector2f(128, 128)), 1);
//        SpriteRenderer obj2SpriteRenderer = new SpriteRenderer();
//        Sprite obj2Sprite = new Sprite();
//        obj2Sprite.setTexture(AssetPool.getTexture("D:\\GameEngine\\assets\\images\\LanceRunningAnimation.png"));
//        obj2SpriteRenderer.setSprite(obj2Sprite);
//        obj2.addComponent(obj2SpriteRenderer);
//        this.addGameObjectToScene(obj2);
    }

    private void loadResources(){
        AssetPool.getShader("D:\\GameEngine\\assets\\shaders\\default.glsl");
        AssetPool.addSpriteSheet("D:\\GameEngine\\assets\\images\\LanceRunningAnimation.png", new Spritesheet(AssetPool.getTexture("D:\\GameEngine\\assets\\images\\LanceRunningAnimation.png"), 20, 35, 6, 0 ));
    }

    private int spriteIndex = 0;
    private float spriteFlipTime = 0.2f;
    private float spriteFlipTimeLeft = 0.0f;

    @Override
    public void update(float dt) {
//        spriteFlipTimeLeft -= dt;
//        if(spriteFlipTimeLeft <= 0){
//            spriteFlipTimeLeft = spriteFlipTime;
//            spriteIndex++;
//            if(spriteIndex > 5){
//                spriteIndex = 0;
//            }
//            obj1.getComponent(SpriteRenderer.class).setSprite(sprites.getSprite(spriteIndex));
//        }

//        obj1.transform.position.x -= 80 * dt;
//        System.out.println("FPS " + (1.0f/dt));
        for(GameObject go : this.gameObjects){
            go.update(dt);
        }

        this.renderer.render();
    }
}
