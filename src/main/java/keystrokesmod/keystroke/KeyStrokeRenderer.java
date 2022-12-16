package keystrokesmod.keystroke;

import net.minecraft.client.*;
import net.minecraftforge.fml.common.gameevent.*;
import java.io.*;
import net.minecraftforge.fml.common.eventhandler.*;
import net.minecraft.client.gui.*;
import java.awt.*;

public class KeyStrokeRenderer
{
    private static final int[] a;
    private final Minecraft mc;
    private final KeyStrokeKeyRenderer[] b;
    private final KeyStrokeMouse[] c;
    
    public KeyStrokeRenderer() {
        this.mc = Minecraft.func_71410_x();
        this.b = new KeyStrokeKeyRenderer[4];
        this.c = new KeyStrokeMouse[2];
        this.b[0] = new KeyStrokeKeyRenderer(this.mc.field_71474_y.field_74351_w, 26, 2);
        this.b[1] = new KeyStrokeKeyRenderer(this.mc.field_71474_y.field_74368_y, 26, 26);
        this.b[2] = new KeyStrokeKeyRenderer(this.mc.field_71474_y.field_74370_x, 2, 26);
        this.b[3] = new KeyStrokeKeyRenderer(this.mc.field_71474_y.field_74366_z, 50, 26);
        this.c[0] = new KeyStrokeMouse(0, 2, 50);
        this.c[1] = new KeyStrokeMouse(1, 38, 50);
    }
    
    @SubscribeEvent
    public void onRenderTick(final TickEvent.RenderTickEvent e) {
        if (this.mc.field_71462_r != null) {
            if (this.mc.field_71462_r instanceof KeyStrokeConfigGui) {
                try {
                    this.mc.field_71462_r.func_146269_k();
                }
                catch (IOException ex) {}
            }
        }
        else if (this.mc.field_71415_G && !this.mc.field_71474_y.field_74330_P) {
            this.renderKeystrokes();
        }
    }
    
    public void renderKeystrokes() {
        final KeyStroke f = KeyStrokeMod.getKeyStroke();
        if (KeyStroke.enabled) {
            int x = KeyStroke.x;
            int y = KeyStroke.y;
            final int g = this.getColor(KeyStroke.currentColorNumber);
            final boolean h = KeyStroke.showMouseButtons;
            final ScaledResolution res = new ScaledResolution(this.mc);
            final int width = 74;
            final int height = h ? 74 : 50;
            if (x < 0) {
                KeyStroke.x = 0;
                x = KeyStroke.x;
            }
            else if (x > res.func_78326_a() - width) {
                KeyStroke.x = res.func_78326_a() - width;
                x = KeyStroke.x;
            }
            if (y < 0) {
                KeyStroke.y = 0;
                y = KeyStroke.y;
            }
            else if (y > res.func_78328_b() - height) {
                KeyStroke.y = res.func_78328_b() - height;
                y = KeyStroke.y;
            }
            this.drawMovementKeys(x, y, g);
            if (h) {
                this.drawMouseButtons(x, y, g);
            }
        }
    }
    
    private int getColor(final int index) {
        return (index == 6) ? Color.getHSBColor(System.currentTimeMillis() % 3750L / 3750.0f, 1.0f, 1.0f).getRGB() : KeyStrokeRenderer.a[index];
    }
    
    private void drawMovementKeys(final int x, final int y, final int textColor) {
        for (final KeyStrokeKeyRenderer key : this.b) {
            key.renderKey(x, y, textColor);
        }
    }
    
    private void drawMouseButtons(final int x, final int y, final int textColor) {
        for (final KeyStrokeMouse button : this.c) {
            button.n(x, y, textColor);
        }
    }
    
    static {
        a = new int[] { 16777215, 16711680, 65280, 255, 16776960, 11141290 };
    }
}
