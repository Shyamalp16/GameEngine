package components;

import Core.Component;

public class SpriteRenderer extends Component {
    private boolean firstTime = false;

    @Override
    public void start(){
        System.out.println("STARTING");
    }

    @Override
    public void update(float dt){
        if(!firstTime){
            System.out.println("UPDATING");
            firstTime = true;
        }

    }
}
