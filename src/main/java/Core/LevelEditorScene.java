package Core;


import components.Sprite;
import components.SpriteRenderer;
import components.Spritesheet;
import org.joml.Vector2f;
import org.joml.Vector4f;
import util.AssetPool;

public class LevelEditorScene extends Scene {
    public LevelEditorScene(){

    }
    @Override
    public void init() {
        loadResources();
        this.camera = new Camera(new Vector2f(new Vector2f(-250, 0)));
        Spritesheet sprites = AssetPool.getSpritesheet("D:\\GameEngine\\assets\\images\\playerSpriteSheet4.png");
        GameObject obj1 = new GameObject("Obj1", new Transform(new Vector2f(100, 100), new Vector2f(256, 256)));
//        obj1.addComponent(new SpriteRenderer(new Sprite(AssetPool.getTexture("D:\\GameEngine\\assets\\images\\PlayerCharacterMain.png"))));
        obj1.addComponent(new SpriteRenderer(sprites.getSprite(3)));
        this.addGameObjectToScene(obj1);

        GameObject obj2 = new GameObject("Obj2", new Transform(new Vector2f(400, 100), new Vector2f(256, 256)));
        obj2.addComponent(new SpriteRenderer(sprites.getSprite(9)));
        this.addGameObjectToScene(obj2);
    }

    private void loadResources(){
        AssetPool.getShader("D:\\GameEngine\\assets\\shaders\\default.glsl");
        AssetPool.addSpriteSheet("D:\\GameEngine\\assets\\images\\playerSpriteSheet4.png", new Spritesheet(AssetPool.getTexture("D:\\GameEngine\\assets\\images\\playerSpriteSheet4.png"), 22, 35, 12, 0 ));
    }

    @Override
    public void update(float dt) {
        System.out.println("FPS " + (1.0f/dt));
        for(GameObject go : this.gameObjects){
            go.update(dt);
        }

        this.renderer.render();
    }
}
