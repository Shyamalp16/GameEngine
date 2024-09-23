package scenes;


import Core.*;
import Physics2D.PhysicsSystem2D;
import Physics2D.primitives.Circle;
import Physics2D.rigidbody.Rigidbody2D;
import components.*;
import imgui.ImGui;
import imgui.ImVec2;
import org.joml.Vector2f;
import util.AssetPool;

public class LevelEditorScene extends Scene {
    private Spritesheet sprites;

    GameObject levelEditorStuff = new GameObject("LevelEditor", new Transform(new Vector2f()), 0);
    PhysicsSystem2D physics = new PhysicsSystem2D(1.0f/10.f, new Vector2f(0, -10));
    Transform obj1, obj2, obj3;
    Rigidbody2D rb1, rb2, rb3;

    public LevelEditorScene(){

    }

    @Override
    public void init() {
        levelEditorStuff.addComponent(new MouseControls());
        levelEditorStuff.addComponent(new GridLines());

        obj1 = new Transform(new Vector2f(120,500));
        obj2 = new Transform(new Vector2f(140,300));
        obj3 = new Transform(new Vector2f(100,300));

        rb1 = new Rigidbody2D();
        rb2 = new Rigidbody2D();
        rb3 = new Rigidbody2D();

        rb1.setRawTransform(obj1);
        rb2.setRawTransform(obj2);
        rb3.setRawTransform(obj3);

        rb1.setMass(100.0f);
        rb2.setMass(200.0f);
        rb3.setMass(200.0f);

        Circle c1 = new Circle();
        c1.setRadius(10.0f);
        c1.setRigidbody(rb1);

        Circle c2 = new Circle();
        c2.setRadius(20.0f);
        c2.setRigidbody(rb2);

        Circle c3 = new Circle();
        c3.setRadius(20.0f);
        c3.setRigidbody(rb3);

        rb1.setCollider(c1);
        rb2.setCollider(c2);
        rb3.setCollider(c3);

        physics.addRigidbody(rb1, true);
        physics.addRigidbody(rb2, false);
        physics.addRigidbody(rb3, false);

        loadResources();
        this.camera = new Camera(new Vector2f(new Vector2f(-250, 0)));
        sprites = AssetPool.getSpritesheet("D:\\GameEngine\\assets\\images\\decorationsAndBlocks.png");

        if(levelLoaded){
            if(gameObjects.size() > 0){
                this.activeGameObject = gameObjects.get(0);
            }
            return;
        }
//        sprites = AssetPool.getSpritesheet("D:\\GameEngine\\assets\\images\\decorationsAndBlocks.png");
    }

    private void loadResources(){
        AssetPool.getShader("D:\\GameEngine\\assets\\shaders\\default.glsl");
        AssetPool.addSpriteSheet("D:\\GameEngine\\assets\\images\\decorationsAndBlocks.png", new Spritesheet(AssetPool.getTexture("D:\\GameEngine\\assets\\images\\decorationsAndBlocks.png"), 16, 16, 81, 0 ));

        for(GameObject go : gameObjects){
            if(go.getComponent(SpriteRenderer.class) != null){
                SpriteRenderer spr = go.getComponent(SpriteRenderer.class);
                if(spr.getTexture() != null){
                    spr.setTexture(AssetPool.getTexture(spr.getTexture().getFilePath()));
                }
            }
        }
    }

    float x = 0.0f;
    float y = 0.0f;
    @Override
    public void update(float dt) {
//        System.out.println("FPS " + (1.0f/dt));
        levelEditorStuff.update(dt);

        for(GameObject go : this.gameObjects){
            go.update(dt);
        }

//        DebugDraw.addCircle2D(obj1.position, 10.0f, new Vector3f(1,0,0), 1);
//        DebugDraw.addCircle2D(obj2.position, 20.0f, new Vector3f(0,1,0), 1);
//        DebugDraw.addCircle2D(obj3.position, 20.0f, new Vector3f(0,0,1), 1);
//        physics.update(dt);
    }

    @Override
    public void render(){
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
