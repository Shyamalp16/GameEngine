package renderer;

import Core.GameObject;
import components.SpriteRenderer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Renderer {
    private final int maxBatchSize = 1000;
    private List<RenderBatch> batches;
    private static Shader currentShader;

    public Renderer(){
        this.batches = new ArrayList<>();
    }

    public void add(GameObject go){
        SpriteRenderer spr = go.getComponent(SpriteRenderer.class);
        if(spr != null){
            add(spr);
        }
    }

    private void add(SpriteRenderer sprite){
        boolean added = false;
        for(RenderBatch batch : batches){
            if(batch.hasRoom() && batch.getZIndex() == sprite.gameObject.transform.zIndex){
                Texture tex = sprite.getTexture();
                if((tex != null && batch.hasTexture(tex)) || batch.hasTextureRoom() || tex == null){
                    batch.addSprite(sprite);
                    added = true;
                    break;
                }
            }
        }

        if(!added){
            RenderBatch newBatch = new RenderBatch(maxBatchSize, sprite.gameObject.transform.zIndex);
            newBatch.start();
            batches.add(newBatch);
            newBatch.addSprite(sprite);
            Collections.sort(batches);
        }
    }

    public static void bindShader(Shader shader){
        currentShader = shader;
    }

    public static Shader getBoundShader(){
        return currentShader;
    }

    public void render(){
        currentShader.use();
        for (int i = 0; i < batches.size(); i++) {
            RenderBatch batch = batches.get(i);
            batch.render();
        }
    }

    public void destroyGameObject(GameObject go){
        if(go.getComponent(SpriteRenderer.class) == null){
            return;
        }
        for(RenderBatch batch : batches){
            if(batch.destroyIfExists(go)){
                return;
            }
        }
    }
}
