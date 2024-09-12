package util;

public class Time {
    public static float timeStarted = System.nanoTime();

    public static float getTime(){
//      NOTE: Convert from Nano Seconds to Seconds and return
        return (float)((System.nanoTime() - timeStarted) * 1E-9);
    }
}
