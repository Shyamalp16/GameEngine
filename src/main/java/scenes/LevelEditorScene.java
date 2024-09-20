package scenes;


import Core.*;
import components.*;
import imgui.ImGui;
import imgui.ImVec2;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.system.CallbackI;
import renderer.DebugDraw;
import util.AssetPool;

public class LevelEditorScene extends Scene {

    private GameObject obj1, obj2;
    private Spritesheet sprites;
    private SpriteRenderer obj1Sprite;

    GameObject levelEditorStuff = new GameObject("LevelEditor", new Transform(new Vector2f()), 0);

    public LevelEditorScene(){

    }
    @Override
    public void init() {
        levelEditorStuff.addComponent(new MouseControls());
        levelEditorStuff.addComponent(new GridLines());
        loadResources();
        this.camera = new Camera(new Vector2f(new Vector2f(-250, 0)));
        sprites = AssetPool.getSpritesheet("D:\\GameEngine\\assets\\images\\decorationsAndBlocks.png");
        if(levelLoaded){
            this.activeGameObject = gameObjects.get(0);
            this.activeGameObject.addComponent(new Rigidbody());
            return;
        }

        sprites = AssetPool.getSpritesheet("D:\\GameEngine\\assets\\images\\decorationsAndBlocks.png");

//        obj1 = new GameObject("Obj1", new Transform(new Vector2f(600, 100), new Vector2f(128, 128)), 2);
////        obj1.addComponent(new SpriteRenderer(new Sprite(AssetPool.getTexture("D:\\GameEngine\\assets\\images\\1.png"))));
//        obj1Sprite = new SpriteRenderer();
//        obj1Sprite.setColor(new Vector4f(0,1,0,1));
//        obj1.addComponent(obj1Sprite);
//        obj1.addComponent(new Rigidbody());
//        this.addGameObjectToScene(obj1);
//        this.activeGameObject = obj1;
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
        AssetPool.addSpriteSheet("D:\\GameEngine\\assets\\images\\decorationsAndBlocks.png", new Spritesheet(AssetPool.getTexture("D:\\GameEngine\\assets\\images\\decorationsAndBlocks.png"), 16, 16, 81, 0 ));
    }

    float x = 0.0f;
    float y = 0.0f;
    @Override
    public void update(float dt) {
//        System.out.println("FPS " + (1.0f/dt));
        levelEditorStuff.update(dt);
        DebugDraw.addCircle2D(new Vector2f(x, y), 64, new Vector3f(0, 1, 0), 1);
        x+=50.0f*dt;
        y+=50.0f*dt;
        for(GameObject go : this.gameObjects){
            go.update(dt);
        }

        this.renderer.render();
    }

    @Override
    public void imgui(){
        ImGui.begin("Sprites Window");
        ImVec2 windowPos = new ImVec2();
        ImGui.getWindowPos(windowPos);
        ImVec2 windowSize = new ImVec2();
        ImGui.getWindowSize(windowSize);

        ImVec2 itemSpacing = new ImVec2();
        ImGui.getStyle().getItemSpacing(itemSpacing);

        float windowX2 = windowPos.x + windowSize.x;
        for(int i=0; i < sprites.size(); i++){
            Sprite sprite = sprites.getSprite(i);
            float spriteWidth = sprite.getWidth() * 4;
            float spriteHeight = sprite.getHeight() * 4;
            int id = sprite.getTexId();
            Vector2f[] texCoords = sprite.getTexCoords();

            ImGui.pushID(i);
            if(ImGui.imageButton(id, spriteWidth, spriteHeight, texCoords[2].x, texCoords[0].y, texCoords[0].x, texCoords[2].y)){
                GameObject object = Prefabs.generateSpriteObject(sprite, 32, 32);
                levelEditorStuff.getComponent(MouseControls.class).pickupObject(object);
            }
            ImGui.popID();

            ImVec2 lastButtonPos = new ImVec2();
            ImGui.getItemRectMax(lastButtonPos);
            float lastButtonX2 = lastButtonPos.x;
            float nextBUttonX2 = lastButtonX2 + itemSpacing.x + spriteWidth;
            if(i+1 < sprites.size() && nextBUttonX2 < windowX2){
                ImGui.sameLine();
            }
        }
        ImGui.end();
    }
}
