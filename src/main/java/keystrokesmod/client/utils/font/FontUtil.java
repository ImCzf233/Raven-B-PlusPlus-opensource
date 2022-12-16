package keystrokesmod.client.utils.font;

import java.awt.*;
import keystrokesmod.client.module.modules.*;
import java.io.*;
import java.util.*;

public class FontUtil
{
    public static volatile int completed;
    public static FontRenderer normal;
    public static FontRenderer two;
    public static FontRenderer small;
    private static Font normal_;
    private static Font two_;
    private static Font small_;
    
    private static Font getFont(final Map<String, Font> locationMap, final String location, final int size, final int fonttype) {
        Font font = null;
        try {
            if (locationMap.containsKey(location)) {
                font = locationMap.get(location).deriveFont(0, (float)size);
            }
            else {
                final InputStream is = HUD.class.getResourceAsStream("/assets/keystrokes/fonts/" + location);
                assert is != null;
                font = Font.createFont(0, is);
                locationMap.put(location, font);
                font = font.deriveFont(fonttype, (float)size);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error loading font");
            font = new Font("comfortaa", 0, size);
        }
        return font;
    }
    
    public static boolean hasLoaded() {
        return FontUtil.completed >= 3;
    }
    
    public static void bootstrap() {
        final HashMap<String, Font> locationMap;
        new Thread(() -> {
            locationMap = new HashMap<String, Font>();
            FontUtil.normal_ = getFont(locationMap, "gilroy.otf", 19, 0);
            FontUtil.two_ = getFont(locationMap, "gilroy.otf", 30, 0);
            FontUtil.small_ = getFont(locationMap, "gilroybold.otf", 14, 1);
            ++FontUtil.completed;
            return;
        }).start();
        final HashMap<String, Font> locationMap2;
        new Thread(() -> {
            locationMap2 = new HashMap<String, Font>();
            ++FontUtil.completed;
            return;
        }).start();
        final HashMap<String, Font> locationMap3;
        new Thread(() -> {
            locationMap3 = new HashMap<String, Font>();
            ++FontUtil.completed;
            return;
        }).start();
        while (!hasLoaded()) {
            try {
                Thread.sleep(5L);
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        FontUtil.normal = new FontRenderer(FontUtil.normal_, true, true);
        FontUtil.two = new FontRenderer(FontUtil.normal_, true, true);
        FontUtil.small = new FontRenderer(FontUtil.small_, true, true);
    }
}
