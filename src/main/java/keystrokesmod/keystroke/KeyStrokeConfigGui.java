package keystrokesmod.keystroke;

import net.minecraft.client.gui.*;
import java.io.*;
import keystrokesmod.client.utils.*;
import keystrokesmod.client.main.*;

public class KeyStrokeConfigGui extends GuiScreen
{
    private static final String[] colors;
    private GuiButton modeBtn;
    private GuiButton textColorBtn;
    private GuiButton showMouseBtn;
    private GuiButton outlineBtn;
    private boolean d;
    private int lx;
    private int ly;
    
    public void func_73866_w_() {
        final KeyStroke st = KeyStrokeMod.getKeyStroke();
        this.field_146292_n.add(this.modeBtn = new GuiButton(0, this.field_146294_l / 2 - 70, this.field_146295_m / 2 - 28, 140, 20, "Mod: " + (KeyStroke.enabled ? "Enabled" : "Disabled")));
        this.field_146292_n.add(this.textColorBtn = new GuiButton(1, this.field_146294_l / 2 - 70, this.field_146295_m / 2 - 6, 140, 20, "Text color: " + KeyStrokeConfigGui.colors[KeyStroke.currentColorNumber]));
        this.field_146292_n.add(this.showMouseBtn = new GuiButton(2, this.field_146294_l / 2 - 70, this.field_146295_m / 2 + 16, 140, 20, "Show mouse buttons: " + (KeyStroke.showMouseButtons ? "On" : "Off")));
        this.field_146292_n.add(this.outlineBtn = new GuiButton(3, this.field_146294_l / 2 - 70, this.field_146295_m / 2 + 38, 140, 20, "Outline: " + (KeyStroke.outline ? "On" : "Off")));
    }
    
    public void func_73863_a(final int mouseX, final int mouseY, final float partialTicks) {
        KeyStrokeMod.getKeyStrokeRenderer().renderKeystrokes();
        super.func_73863_a(mouseX, mouseY, partialTicks);
    }
    
    protected void func_146284_a(final GuiButton button) {
        final KeyStroke st = KeyStrokeMod.getKeyStroke();
        if (button == this.modeBtn) {
            KeyStroke.enabled = !KeyStroke.enabled;
            this.modeBtn.field_146126_j = "Mod: " + (KeyStroke.enabled ? "Enabled" : "Disabled");
        }
        else if (button == this.textColorBtn) {
            KeyStroke.currentColorNumber = ((KeyStroke.currentColorNumber == 6) ? 0 : (KeyStroke.currentColorNumber + 1));
            this.textColorBtn.field_146126_j = "Text color: " + KeyStrokeConfigGui.colors[KeyStroke.currentColorNumber];
        }
        else if (button == this.showMouseBtn) {
            KeyStroke.showMouseButtons = !KeyStroke.showMouseButtons;
            this.showMouseBtn.field_146126_j = "Show mouse buttons: " + (KeyStroke.showMouseButtons ? "On" : "Off");
        }
        else if (button == this.outlineBtn) {
            KeyStroke.outline = !KeyStroke.outline;
            this.outlineBtn.field_146126_j = "Outline: " + (KeyStroke.outline ? "On" : "Off");
        }
    }
    
    protected void func_73864_a(final int mouseX, final int mouseY, final int button) {
        try {
            super.func_73864_a(mouseX, mouseY, button);
        }
        catch (IOException ex) {}
        if (button == 0) {
            MouseManager.addLeftClick();
            final KeyStroke st = KeyStrokeMod.getKeyStroke();
            final int startX = KeyStroke.x;
            final int startY = KeyStroke.y;
            final int endX = startX + 74;
            final int endY = startY + (KeyStroke.showMouseButtons ? 74 : 50);
            if (mouseX >= startX && mouseX <= endX && mouseY >= startY && mouseY <= endY) {
                this.d = true;
                this.lx = mouseX;
                this.ly = mouseY;
            }
        }
        else if (button == 1) {
            MouseManager.addRightClick();
        }
    }
    
    protected void func_146286_b(final int mouseX, final int mouseY, final int action) {
        super.func_146286_b(mouseX, mouseY, action);
        this.d = false;
    }
    
    protected void func_146273_a(final int mouseX, final int mouseY, final int lastButtonClicked, final long timeSinceMouseClick) {
        super.func_146273_a(mouseX, mouseY, lastButtonClicked, timeSinceMouseClick);
        if (this.d) {
            final KeyStroke st = KeyStrokeMod.getKeyStroke();
            KeyStroke.x = KeyStroke.x + mouseX - this.lx;
            KeyStroke.y = KeyStroke.y + mouseY - this.ly;
            this.lx = mouseX;
            this.ly = mouseY;
        }
    }
    
    public boolean func_73868_f() {
        return false;
    }
    
    public void func_146281_b() {
        Raven.clientConfig.updateKeyStrokesSettings();
    }
    
    static {
        colors = new String[] { "White", "Red", "Green", "Blue", "Yellow", "Purple", "Rainbow" };
    }
}
