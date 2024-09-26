package scenes;


import Core.*;
import components.*;
import imgui.ImGui;
import imgui.ImVec2;
import org.joml.Vector2f;
import util.AssetPool;

public class LevelEditorSceneInit extends SceneInit {
    private Spritesheet sprites;
    private GameObject levelEditorStuff;

    public LevelEditorSceneInit(){

    }

    @Override
    public void init(Scene scene) {
        sprites = AssetPool.getSpritesheet("D:\\GameEngine\\assets\\images\\decorationsAndBlocks.png");
        Spritesheet gizmos = AssetPool.getSpritesheet("D:\\GameEngine\\assets\\images\\gizmos.png");
        levelEditorStuff = scene.createGameObject("LevelEditor");
        levelEditorStuff.setNoSerialize();
        levelEditorStuff.addComponent(new MouseControls());
        levelEditorStuff.addComponent(new GridLines());
        levelEditorStuff.addComponent(new EditorCamera(scene.camera()));
        levelEditorStuff.addComponent(new GizmoSystem(gizmos));
        scene.addGameObjectToScene(levelEditorStuff);
//        if(levelLoaded){
//            if(gameObjects.size() > 0){
//                this.activeGameObject = gameObjects.get(0);
//            }
//        }
//        sprites = AssetPool.getSpritesheet("D:\\GameEngine\\assets\\images\\decorationsAndBlocks.png");
    }

    @Override
    public void loadResources(Scene scene){
        AssetPool.getShader("D:\\GameEngine\\assets\\shaders\\default.glsl");
        AssetPool.addSpriteSheet("D:\\GameEngine\\assets\\images\\decorationsAndBlocks.png", new Spritesheet(AssetPool.getTexture("D:\\GameEngine\\assets\\images\\decorationsAndBlocks.png"), 16, 16, 81, 0 ));
        AssetPool.addSpriteSheet("D:\\GameEngine\\assets\\images\\gizmos.png", new Spritesheet(AssetPool.getTexture("D:\\GameEngine\\assets\\images\\gizmos.png"), 24,48, 3, 0));

        for(GameObject go : scene.getGameObjects()){
            if(go.getComponent(SpriteRenderer.class) != null){
                SpriteRenderer spr = go.getComponent(SpriteRenderer.class);
                if(spr.getTexture() != null){
                    spr.setTexture(AssetPool.getTexture(spr.getTexture().getFilePath()));
                }
            }
        }
    }

    @Override
    public void imgui(){
        ImGui.begin("Level Editor Stuff");
        levelEditorStuff.imgui();
        ImGui.end();

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
