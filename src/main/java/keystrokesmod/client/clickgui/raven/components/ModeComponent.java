package keystrokesmod.client.clickgui.raven.components;

import keystrokesmod.client.clickgui.raven.*;
import keystrokesmod.client.module.setting.impl.*;
import java.awt.*;
import org.lwjgl.opengl.*;
import net.minecraft.client.*;
import keystrokesmod.client.main.*;

public class ModeComponent implements Component
{
    private final int c;
    private final ComboSetting mode;
    private final ModuleComponent module;
    private int x;
    private int y;
    private int o;
    private final boolean registeredClick = false;
    private final boolean md = false;
    
    public ModeComponent(final ComboSetting desc, final ModuleComponent b, final int o) {
        this.c = new Color(30, 144, 255).getRGB();
        this.mode = desc;
        this.module = b;
        this.x = b.category.getX() + b.category.getWidth();
        this.y = b.category.getY() + b.o;
        this.o = o;
    }
    
    @Override
    public void draw() {
        GL11.glPushMatrix();
        GL11.glScaled(0.5, 0.5, 0.5);
        final int bruhWidth = (int)(Minecraft.func_71410_x().field_71466_p.func_78256_a(this.mode.getName() + ": ") * 0.5);
        Minecraft.func_71410_x().field_71466_p.func_175065_a(this.mode.getName() + ": ", (float)((this.module.category.getX() + 4) * 2), (float)((this.module.category.getY() + this.o + 4) * 2), -1, true);
        Minecraft.func_71410_x().field_71466_p.func_175065_a(String.valueOf(this.mode.getMode()), (float)((this.module.category.getX() + 4 + bruhWidth) * 2), (float)((this.module.category.getY() + this.o + 4) * 2), this.c, true);
        GL11.glPopMatrix();
    }
    
    @Override
    public void update(final int mousePosX, final int mousePosY) {
        this.y = this.module.category.getY() + this.o;
        this.x = this.module.category.getX();
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
    public void mouseDown(final int x, final int y, final int b) {
        if (this.i(x, y)) {
            this.mode.nextMode();
            this.module.mod.guiButtonToggled(this.mode);
            Raven.mc.field_71439_g.func_85030_a("gui.button.press", 1.0f, 1.0f);
        }
    }
    
    @Override
    public void mouseReleased(final int x, final int y, final int m) {
    }
    
    @Override
    public void keyTyped(final char t, final int k) {
    }
    
    @Override
    public int getY() {
        return this.y;
    }
    
    private boolean i(final int x, final int y) {
        return x > this.x && x < this.x + this.module.category.getWidth() && y > this.y && y < this.y + 11;
    }
}
