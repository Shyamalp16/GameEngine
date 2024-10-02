package Core;

import components.*;
import org.joml.Vector2f;
import util.AssetPool;

public class Prefabs {
    public static GameObject generateSpriteObject(Sprite sprite, float sizeX, float sizeY){
        GameObject block = Window.getScene().createGameObject("Sprite_Object_Gen");
        block.transform.scale.x = sizeX;
        block.transform.scale.y = sizeY;
        SpriteRenderer renderer = new SpriteRenderer();
        renderer.setSprite(sprite);
        block.addComponent(renderer);
        return block;
    }

    public static GameObject generateLance(){
        Spritesheet playerSprite = AssetPool.getSpritesheet("D:\\GameEngine\\assets\\images\\LanceRunningAnimation.png");
        GameObject lance = generateSpriteObject(playerSprite.getSprite(0), 0.25f, 0.25f);
        AnimationState run = new AnimationState();
        run.title = "Run";
        float defaultFrameTime = 0.2f;
        run.addFrame(playerSprite.getSprite(0), defaultFrameTime);
        run.addFrame(playerSprite.getSprite(1), defaultFrameTime);
        run.addFrame(playerSprite.getSprite(2), defaultFrameTime);
        run.addFrame(playerSprite.getSprite(3), defaultFrameTime);
        run.addFrame(playerSprite.getSprite(4), defaultFrameTime);
        run.addFrame(playerSprite.getSprite(5), defaultFrameTime);
        run.setLoop(true);

        StateMachine stateMachine = new StateMachine();
        stateMachine.addState(run);
        stateMachine.setDefaultState(run.title);
        lance.addComponent(stateMachine);

        return lance;
    }
}
