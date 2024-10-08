package engine;

import Core.MouseListener;
import Core.Window;
import Observers.EventSystem;
import Observers.Observer;
import Observers.events.Event;
import Observers.events.EventType;
import imgui.ImGui;
import imgui.ImVec2;
import imgui.flag.ImGuiWindowFlags;
import org.joml.Vector2f;

public class GameViewWindow {
    private float leftX, rightX, topY, bottomY;
    private boolean isPlaying = false;

    public void imgui(){
        ImGui.begin("Game Viewport", ImGuiWindowFlags.NoScrollbar | ImGuiWindowFlags.NoScrollWithMouse | ImGuiWindowFlags.MenuBar);

        ImGui.beginMenuBar();
        if(ImGui.menuItem("Play", "", isPlaying, !isPlaying)){
            isPlaying = true;
            EventSystem.notify(null, new Event(EventType.GameEngineStartPlay));
        }

        if(ImGui.menuItem("Stop", "", !isPlaying, isPlaying)){
            isPlaying = false;
            EventSystem.notify(null, new Event(EventType.GameEngineStopPlay));
        }
        ImGui.endMenuBar();


        ImGui.setCursorPos(ImGui.getCursorPosX(), ImGui.getCursorPosY());
        ImVec2 windowSize = getLargestSizeForViewport();
        ImVec2 windowPos = getCenteredPositionForViewport(windowSize);
        ImGui.setCursorPos(windowPos.x, windowPos.y);

        leftX = windowPos.x + 10;
        bottomY = windowPos.y;
        rightX = windowPos.x + windowSize.x + 10;
        topY = windowPos.y + windowSize.y;

        int textureId = Window.getFrameBuffer().getTextureId();
        ImGui.image(textureId, windowSize.x, windowSize.y, 0, 1, 1, 0);

        MouseListener.setGameViewPortPos(new Vector2f(windowPos.x + 10, windowPos.y));
        MouseListener.setGameViewPortSize(new Vector2f(windowSize.x, windowSize.y));
        ImGui.end();
    }

    private ImVec2 getLargestSizeForViewport(){
        ImVec2 windowSize = new ImVec2();
        ImGui.getContentRegionAvail(windowSize);

        float aspectWidth = windowSize.x;
        float aspectHeight = aspectWidth / Window.getTargetAspectRatio();

        if(aspectHeight > windowSize.y){
            aspectHeight = windowSize.y;
            aspectWidth = aspectHeight * Window.getTargetAspectRatio();
        }
        return new ImVec2(aspectWidth, aspectHeight);
    }

    private ImVec2 getCenteredPositionForViewport(ImVec2 aspectSize){
        ImVec2 windowSize = new ImVec2();
        ImGui.getContentRegionAvail(windowSize);

        float viewPortX = (windowSize.x / 2.0f) - (aspectSize.x / 2.0f);
        float viewPortY = (windowSize.y / 2.0f) - (aspectSize.y / 2.0f);

        return new ImVec2(viewPortX + ImGui.getCursorPosX(), viewPortY + ImGui.getCursorPosY()  );
    }

    public boolean getWantCaptureMouse(){
        return MouseListener.getX() >= leftX && MouseListener.getX() <= rightX &&
                MouseListener.getY() >= bottomY && MouseListener.getY() <= topY;
    }

}
