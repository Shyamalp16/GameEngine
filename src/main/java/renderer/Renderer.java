package renderer;

import Core.GameObject;
import components.SpriteRenderer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Renderer {
    private final int maxBatchSize = 1000;
    private List<RenderBatch> batches;

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
            if(batch.hasRoom() && batch.getZIndex() == sprite.gameObject.getZIndex()){
                Texture tex = sprite.getTexture();
                if((tex != null && batch.hasTexture(tex)) || batch.hasTextureRoom() || tex == null){
                    batch.addSprite(sprite);
                    added = true;
                    break;
                }
            }
        }

        if(!added){
            RenderBatch newBatch = new RenderBatch(maxBatchSize, sprite.gameObject.getZIndex());
            newBatch.start();
            batches.add(newBatch);
            newBatch.addSprite(sprite);
            Collections.sort(batches);
        }
    }

    public void render(){
        for(RenderBatch batch : batches){
            batch.render();
        }
    }
}
