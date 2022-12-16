package keystrokesmod.client.clickgui.raven.components;

import org.lwjgl.opengl.*;
import org.lwjgl.input.*;
import keystrokesmod.client.main.*;
import keystrokesmod.client.clickgui.raven.*;
import keystrokesmod.client.module.modules.client.*;
import net.minecraft.client.*;
import java.awt.*;

public class BindComponent implements Component
{
    private boolean isBinding;
    private final ModuleComponent p;
    private int o;
    private int x;
    private int y;
    
    public BindComponent(final ModuleComponent b, final int o) {
        this.p = b;
        this.x = b.category.getX() + b.category.getWidth();
        this.y = b.category.getY() + b.o;
        this.o = o;
    }
    
    @Override
    public void draw() {
        GL11.glPushMatrix();
        GL11.glScaled(0.5, 0.5, 0.5);
        this.dr(this.isBinding ? BindStage.binding : (BindStage.bind + ": " + Keyboard.getKeyName(this.p.mod.getKeycode())));
        GL11.glPopMatrix();
    }
    
    @Override
    public void update(final int mousePosX, final int mousePosY) {
        final boolean h = this.i(mousePosX, mousePosY);
        this.y = this.p.category.getY() + this.o;
        this.x = this.p.category.getX();
    }
    
    @Override
    public void mouseDown(final int x, final int y, final int b) {
        if (this.i(x, y) && b == 0 && this.p.po) {
            this.isBinding = !this.isBinding;
            Raven.mc.field_71439_g.func_85030_a("gui.button.press", 1.0f, 1.0f);
            if (this.isBinding) {
                ++ClickGui.binding;
            }
            else {
                --ClickGui.binding;
            }
        }
    }
    
    @Override
    public void mouseReleased(final int x, final int y, final int m) {
    }
    
    @Override
    public void keyTyped(final char t, final int k) {
        if (!this.p.mod.getName().equalsIgnoreCase("AutoConfig") && this.isBinding) {
            if (k == 11 || k == 1) {
                if (this.p.mod instanceof GuiModule) {
                    this.p.mod.setbind(54);
                }
                else {
                    this.p.mod.setbind(0);
                }
            }
            else {
                this.p.mod.setbind(k);
            }
            --ClickGui.binding;
            this.isBinding = false;
        }
    }
    
    @Override
    public void setComponentStartAt(final int n) {
        this.o = n;
    }
    
    public boolean i(final int x, final int y) {
        return x > this.x && x < this.x + this.p.category.getWidth() && y > this.y - 1 && y < this.y + 12;
    }
    
    @Override
    public int getHeight() {
        return 16;
    }
    
    @Override
    public int getY() {
        return this.y;
    }
    
    private void dr(final String s) {
        Minecraft.func_71410_x().field_71466_p.func_175063_a(s, (float)((this.p.category.getX() + 4) * 2), (float)((this.p.category.getY() + this.o + 3) * 2), Color.HSBtoRGB(System.currentTimeMillis() % 3750L / 3750.0f, 0.8f, 0.8f));
    }
}
