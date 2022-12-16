package keystrokesmod.client.clickgui.theme.themes;

import keystrokesmod.client.clickgui.theme.*;
import java.awt.*;

public class Vape implements Theme
{
    @Override
    public String getName() {
        return "VapeV4";
    }
    
    @Override
    public Color getTextColour() {
        return new Color(252, 253, 253);
    }
    
    @Override
    public Color getBackgroundColour() {
        return new Color(33, 31, 28);
    }
    
    @Override
    public Color getSecondBackgroundColour() {
        return new Color(43, 42, 41);
    }
    
    @Override
    public Color getForegroundColour() {
        return this.getTextColour();
    }
    
    @Override
    public Color getSelectionBackgroundColour() {
        return new Color(88, 89, 89);
    }
    
    @Override
    public Color getSelectionForegroundColour() {
        return new Color(40, 38, 38);
    }
    
    @Override
    public Color getButtonColour() {
        return new Color(17, 19, 21);
    }
    
    @Override
    public Color getDisabledColour() {
        return new Color(120, 118, 115);
    }
    
    @Override
    public Color getContrastColour() {
        return new Color(249, 125, 1);
    }
    
    @Override
    public Color getAccentColour() {
        return new Color(17, 209, 169);
    }
    
    @Override
    public Color getActiveColour() {
        return this.getAccentColour();
    }
    
    @Override
    public Color getExcludedColour() {
        return this.getDisabledColour();
    }
    
    @Override
    public Color getNotificationColour() {
        return this.getSecondBackgroundColour();
    }
    
    @Override
    public Color getHeadingColour() {
        return this.getAccentColour();
    }
    
    @Override
    public Color getHighlightColour() {
        return new Color(36, 36, 36);
    }
    
    @Override
    public Color getBorderColour() {
        return this.getBackdropColour();
    }
    
    @Override
    public Color getBackdropColour() {
        return new Color(0, 0, 0, 0);
    }
    
    @Override
    public Color getArrayListColour(final double currentY, final double fullY, final double speed) {
        final long time = System.currentTimeMillis();
        final long l1 = (long)(fullY - currentY) * 10L;
        final float f1 = (float)(time % (l1 / speed));
        final float f2 = l1 / (float)speed;
        final float c = f1 / f2;
        return Color.getHSBColor(c, 1.0f, 1.0f);
    }
}
