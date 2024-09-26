package scenes;

import Core.Camera;
import Core.GameObject;
import Core.GameObjectDeserializer;
import Core.Transform;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import components.Component;
import components.ComponentDeserializer;
import org.joml.Vector2f;
import physics2d.Physics2D;
import renderer.Renderer;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Scene {
    private Renderer renderer;
    private Camera camera;
    private SceneInit sceneInit;
    private Physics2D physics;

    private List<GameObject> gameObjects;
    private boolean isRunning;

    public Scene(SceneInit sceneInit){
        this.sceneInit = sceneInit;
        this.physics = new Physics2D();
        this.renderer = new Renderer();
        this.gameObjects = new ArrayList<>();
        this.isRunning = false;
    }

    public void init(){
        this.camera = new Camera(new Vector2f(new Vector2f(-250, 0)));
        this.sceneInit.loadResources(this);
        this.sceneInit.init(this);
    }

    public void start(){
        for(int i = 0; i < gameObjects.size(); i++ ){
            GameObject go = gameObjects.get(i);
            go.start();
            this.renderer.add(go);
            this.physics.add(go);
        }
        isRunning = true;
    }

    public void addGameObjectToScene(GameObject go){
        if(!isRunning){
            gameObjects.add(go);
        }else{
            gameObjects.add(go);
            go.start();
            this.renderer.add(go);
            this.physics.add(go);
        }
    }

    public GameObject getGameObject(int gameObjectId){
        Optional<GameObject> result = this.gameObjects.stream().filter(gameObject -> gameObject.getUid() == gameObjectId).findFirst();
        return result.orElse(null);
    }

    public List<GameObject> getGameObjects(){
        return this.gameObjects;
    }

    public void destroy(){
        for(GameObject go : gameObjects){
            go.destroy();
        }
    }

    public void EditorUpdate(float dt){
        this.camera.adjustProjection();
        for(int i = 0; i < gameObjects.size(); i++){
            GameObject go = gameObjects.get(i);
            go.EditorUpdate(dt);

            if(go.isDead()){
                gameObjects.remove(i);
                this.renderer.destroyGameObject(go);
                this.physics.destroyGameObject(go);
                i--;
            }
        }
    }

    public void update(float dt){
        this.camera.adjustProjection();
        this.physics.update(dt);
        for(int i = 0; i < gameObjects.size(); i++){
            GameObject go = gameObjects.get(i);
            go.update(dt);
            if(go.isDead()){
                gameObjects.remove(i);
                this.renderer.destroyGameObject(go);
                this.physics.destroyGameObject(go);
                i--;
            }
        }
    }

    public void render(){
        this.renderer.render();
    }

    public Camera camera(){
        return this.camera;
    }

    public void imgui(){
        this.sceneInit.imgui();
    }

    public GameObject createGameObject(String name){
        GameObject go = new GameObject(name);
        go.addComponent(new Transform());
        go.transform = go.getComponent(Transform.class);
        return go;
    }

    public void save(){
        Gson gson = new GsonBuilder().setPrettyPrinting().registerTypeAdapter(Component.class, new ComponentDeserializer())
                .registerTypeAdapter(GameObject.class, new GameObjectDeserializer()).create();
        try{
            FileWriter writer = new FileWriter("level.txt");
            List<GameObject> objsToSerialize = new ArrayList<>();
            for(GameObject obj : this.gameObjects){
                if(obj.doSerialization()){
                    objsToSerialize.add(obj);
                }
            }
            writer.write(gson.toJson(objsToSerialize));;
            writer.close();
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public void load(){
        Gson gson = new GsonBuilder().setPrettyPrinting().registerTypeAdapter(Component.class, new ComponentDeserializer())
                .registerTypeAdapter(GameObject.class, new GameObjectDeserializer()).create();
        String inFile = "";
        try{
            inFile = new String(Files.readAllBytes(Paths.get("level.txt")));
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (!inFile.equals("")) {
            int maxGoId = -1;
            int maxCompId = -1;
            GameObject[] objs = gson.fromJson(inFile, GameObject[].class);
            for (int i=0; i < objs.length; i++) {
                addGameObjectToScene(objs[i]);

                for (Component c : objs[i].getAllComponents()) {
                    if (c.getUid() > maxCompId) {
                        maxCompId = c.getUid();
                    }
                }
                if (objs[i].getUid() > maxGoId) {
                    maxGoId = objs[i].getUid();
                }
            }

            maxGoId++;
            maxCompId++;
            GameObject.init(maxGoId);
            Component.init(maxCompId);
        }
    }

}
