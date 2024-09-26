package components;

import Core.MouseListener;
import engine.PropertiesWindow;

public class ScaleGizmo extends Gizmo {
    public ScaleGizmo(Sprite scaleSprite, PropertiesWindow propertiesWindow){
        super(scaleSprite, propertiesWindow);
    }

    @Override
    public void EditorUpdate(float dt){
        if(activeGameObject != null){
            if(xAxisActive  && !yAxisActive){
                activeGameObject.transform.scale.x -= MouseListener.getWorldDx();
            }else if(yAxisActive && !xAxisActive){
                activeGameObject.transform.scale.y -= MouseListener.getWorldDy();
            }
        }
        super.update(dt);
    }
}
