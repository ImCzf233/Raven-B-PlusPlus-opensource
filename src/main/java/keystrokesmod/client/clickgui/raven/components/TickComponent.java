package keystrokesmod.client.clickgui.raven.components;

import keystrokesmod.client.clickgui.raven.*;
import keystrokesmod.client.module.*;
import keystrokesmod.client.module.setting.impl.*;
import java.awt.*;
import org.lwjgl.opengl.*;
import net.minecraft.client.*;
import keystrokesmod.client.main.*;

public class TickComponent implements Component
{
    private final int c;
    private final int boxC;
    private final Module mod;
    private final TickSetting cl1ckbUtt0n;
    private final ModuleComponent module;
    private int o;
    private int x;
    private int y;
    private final int boxSize = 6;
    
    public TickComponent(final Module mod, final TickSetting op, final ModuleComponent b, final int o) {
        this.c = new Color(20, 255, 0).getRGB();
        this.boxC = new Color(169, 169, 169).getRGB();
        this.mod = mod;
        this.cl1ckbUtt0n = op;
        this.module = b;
        this.x = b.category.getX() + b.category.getWidth();
        this.y = b.category.getY() + b.o;
        this.o = o;
    }
    
    public static void e() {
        GL11.glDisable(2929);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glDepthMask(true);
        GL11.glEnable(2848);
        GL11.glHint(3154, 4354);
        GL11.glHint(3155, 4354);
    }
    
    public static void renderMain() {
        GL11.glEnable(3553);
        GL11.glEnable(2929);
        GL11.glDisable(2848);
        GL11.glHint(3154, 4352);
        GL11.glHint(3155, 4352);
    }
    
    public static void renderMain(final float x, final float y, final float x1, final float y1, final int c) {
        e();
        colour(c);
        renderMain(x, y, x1, y1);
        renderMain();
    }
    
    public static void renderMain(final float x, final float y, final float x1, final float y1) {
        GL11.glBegin(7);
        GL11.glVertex2f(x, y1);
        GL11.glVertex2f(x1, y1);
        GL11.glVertex2f(x1, y);
        GL11.glVertex2f(x, y);
        GL11.glEnd();
    }
    
    public static void colour(final int h) {
        final float a1pha = (h >> 24 & 0xFF) / 350.0f;
        GL11.glColor4f(0.0f, 0.0f, 0.0f, a1pha);
    }
    
    @Override
    public void draw() {
        GL11.glPushMatrix();
        GL11.glScaled(0.5, 0.5, 0.5);
        Minecraft.func_71410_x().field_71466_p.func_175065_a(this.cl1ckbUtt0n.isToggled() ? ("[+]  " + this.cl1ckbUtt0n.getName()) : ("[-]  " + this.cl1ckbUtt0n.getName()), (float)((this.module.category.getX() + 4) * 2), (float)((this.module.category.getY() + this.o + 5) * 2), this.cl1ckbUtt0n.isToggled() ? this.c : -1, false);
        GL11.glPopMatrix();
    }
    
    @Override
    public void setComponentStartAt(final int n) {
        this.o = n;
    }
    
    @Override
    public int getHeight() {
        return 0;
    }
    
    @Override
    public void update(final int mousePosX, final int mousePosY) {
        this.y = this.module.category.getY() + this.o;
        this.x = this.module.category.getX();
    }
    
    @Override
    public void mouseDown(final int x, final int y, final int b) {
        if (this.i(x, y) && b == 0 && this.module.po) {
            this.cl1ckbUtt0n.toggle();
            this.mod.guiButtonToggled(this.cl1ckbUtt0n);
            Raven.mc.field_71439_g.func_85030_a("gui.button.press", 1.0f, 1.0f);
        }
    }
    
    @Override
    public void mouseReleased(final int x, final int y, final int m) {
    }
    
    @Override
    public void keyTyped(final char t, final int k) {
    }
    
    public boolean i(final int x, final int y) {
        return x > this.x && x < this.x + this.module.category.getWidth() && y > this.y && y < this.y + 11;
    }
    
    @Override
    public int getY() {
        return this.y;
    }
}
