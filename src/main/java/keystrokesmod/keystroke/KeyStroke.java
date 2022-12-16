package keystrokesmod.keystroke;

public class KeyStroke
{
    public static int x;
    public static int y;
    public static int currentColorNumber;
    public static boolean showMouseButtons;
    public static boolean enabled;
    public static boolean outline;
    
    public KeyStroke() {
        KeyStroke.x = 0;
        KeyStroke.y = 0;
        KeyStroke.currentColorNumber = 0;
        KeyStroke.showMouseButtons = false;
        KeyStroke.enabled = true;
        KeyStroke.outline = false;
    }
}
