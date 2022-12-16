package keystrokesmod.client.clickgui.raven.components;

import keystrokesmod.client.clickgui.raven.*;
import keystrokesmod.client.module.setting.impl.*;
import java.awt.*;
import org.lwjgl.opengl.*;
import net.minecraft.client.*;

public class DescriptionComponent implements Component
{
    private final int c;
    private final DescriptionSetting desc;
    private final ModuleComponent p;
    private int o;
    
    public DescriptionComponent(final DescriptionSetting desc, final ModuleComponent b, final int o) {
        this.c = new Color(226, 83, 47).getRGB();
        this.desc = desc;
        this.p = b;
        final int x = b.category.getX() + b.category.getWidth();
        final int y = b.category.getY() + b.o;
        this.o = o;
    }
    
    @Override
    public void draw() {
        GL11.glPushMatrix();
        GL11.glScaled(0.5, 0.5, 0.5);
        Minecraft.func_71410_x().field_71466_p.func_175065_a(this.desc.getDesc(), (float)((this.p.category.getX() + 4) * 2), (float)((this.p.category.getY() + this.o + 4) * 2), this.c, true);
        GL11.glPopMatrix();
    }
    
    @Override
    public void update(final int mousePosX, final int mousePosY) {
    }
    
    @Override
    public void mouseDown(final int x, final int y, final int b) {
    }
    
    @Override
    public void mouseReleased(final int x, final int y, final int m) {
    }
    
    @Override
    public void keyTyped(final char t, final int k) {
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
    public int getY() {
        return (this.p.category.getY() + this.o + 4) * 2;
    }
}
