package components;

import java.util.ArrayList;
import java.util.List;

public class AnimationState {
    public String title;
    public List<Frame> animationFrame = new ArrayList<>();
    private static Sprite defaultSprite = new Sprite();
    private transient float timeTracker = 0.0f;
    private transient int currentSprite = 0;
    public boolean doesLoop = false;

    public void addFrame(Sprite sprite, float frameTime){
        animationFrame.add(new Frame(sprite, frameTime));
    }

    public void setLoop(boolean doesLoop){
        this.doesLoop = doesLoop;
    }

    public void update(float dt){
        if(currentSprite < animationFrame.size()){
            timeTracker -= dt;
            if(timeTracker <= 0){
                if(!(currentSprite != animationFrame.size() -1 || doesLoop)){
                    currentSprite = (currentSprite + 1) % animationFrame.size();
                }
                timeTracker = animationFrame.get(currentSprite).frameTime;
            }
        }
    }

    public Sprite getCurrentSprite(){
        if(currentSprite < animationFrame.size()){
            return animationFrame.get(currentSprite).sprite;
        }
        return defaultSprite;
    }

}
