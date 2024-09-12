package jade;

import java.awt.event.KeyEvent;

public class LevelEditorScene extends Scene {
    private boolean changingScene = false;
    private float timeToChangeScene = 2.0f;

    public LevelEditorScene(){
        System.out.println("Inside LevelEditorScene");
    }

    @Override
    public void update(float dt) {
        System.out.println("" + (1.0f/dt) + " FPS");
//      If key is pressed, change state to true
        if(!changingScene && KeyListener.isKeyPressed(KeyEvent.VK_SPACE)){
            changingScene = true;
        }

//      if state is true and thers time to change it, decrease the time left and change it when the time hits 0!
        if(changingScene && timeToChangeScene > 0){
            timeToChangeScene -= dt;
            Window.get().r -= dt * 5.0f;
            Window.get().g -= dt * 5.0f;
            Window.get().b -= dt * 5.0f;
            Window.get().a -= dt * 5.0f;
        }else if(changingScene){
            Window.changeScene(1);
        }
    }
}
