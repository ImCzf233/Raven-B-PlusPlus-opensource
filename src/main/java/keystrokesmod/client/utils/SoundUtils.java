package keystrokesmod.client.utils;

import java.util.*;
import javax.sound.sampled.*;

public class SoundUtils
{
    private static final HashMap<String, AudioInputStream> sounds;
    private static Clip clip;
    
    public static void addSound(final String name) {
        try {
            AudioSystem.getAudioInputStream(SoundUtils.class.getResource("/assets/keystrokes/sounds/" + name + ".wav"));
        }
        catch (Exception e) {
            System.out.println("Error loading sound");
            e.printStackTrace();
        }
    }
    
    public static void playSound(final String name) {
        try {
            (SoundUtils.clip = AudioSystem.getClip()).open(AudioSystem.getAudioInputStream(SoundUtils.class.getResource("/assets/keystrokes/sounds/" + name + ".wav")));
            SoundUtils.clip.start();
        }
        catch (Exception e) {
            System.out.println("Error with playing sound.");
            e.printStackTrace();
        }
    }
    
    static {
        sounds = new HashMap<String, AudioInputStream>();
        addSound("click");
    }
}
