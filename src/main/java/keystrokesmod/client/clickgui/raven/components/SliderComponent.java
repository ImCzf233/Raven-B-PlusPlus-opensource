package keystrokesmod.client.clickgui.raven.components;

import keystrokesmod.client.clickgui.raven.*;
import keystrokesmod.client.module.setting.impl.*;
import net.minecraft.client.gui.*;
import java.awt.*;
import org.lwjgl.opengl.*;
import net.minecraft.client.*;
import java.math.*;

public class SliderComponent implements Component
{
    private final SliderSetting v;
    private final ModuleComponent p;
    private int o;
    private int x;
    private int y;
    private boolean d;
    private double w;
    private final int msl = 84;
    
    public SliderComponent(final SliderSetting v, final ModuleComponent b, final int o) {
        this.v = v;
        this.p = b;
        this.x = b.category.getX() + b.category.getWidth();
        this.y = b.category.getY() + b.o;
        this.o = o;
    }
    
    @Override
    public void draw() {
        Gui.func_73734_a(this.p.category.getX() + 4, this.p.category.getY() + this.o + 11, this.p.category.getX() + 4 + this.p.category.getWidth() - 8, this.p.category.getY() + this.o + 15, -12302777);
        final int l = this.p.category.getX() + 4;
        int r = this.p.category.getX() + 4 + (int)this.w;
        if (r - l > 84) {
            r = l + 84;
        }
        Gui.func_73734_a(l, this.p.category.getY() + this.o + 11, r, this.p.category.getY() + this.o + 15, Color.getHSBColor(System.currentTimeMillis() % 11000L / 11000.0f, 0.75f, 0.9f).getRGB());
        GL11.glPushMatrix();
        GL11.glScaled(0.5, 0.5, 0.5);
        Minecraft.func_71410_x().field_71466_p.func_175063_a(this.v.getName() + ": " + this.v.getInput(), (float)(int)((this.p.category.getX() + 4) * 2.0f), (float)(int)((this.p.category.getY() + this.o + 3) * 2.0f), -1);
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
        this.y = this.p.category.getY() + this.o;
        this.x = this.p.category.getX();
        final double d = Math.min(this.p.category.getWidth() - 8, Math.max(0, mousePosX - this.x));
        this.w = (this.p.category.getWidth() - 8) * (this.v.getInput() - this.v.getMin()) / (this.v.getMax() - this.v.getMin());
        if (this.d) {
            if (d == 0.0) {
                this.v.setValue(this.v.getMin());
            }
            else {
                final double n = r(d / (this.p.category.getWidth() - 8) * (this.v.getMax() - this.v.getMin()) + this.v.getMin(), 2);
                this.v.setValue(n);
            }
        }
    }
    
    private static double r(final double v, final int p) {
        if (p < 0) {
            return 0.0;
        }
        BigDecimal bd = new BigDecimal(v);
        bd = bd.setScale(p, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
    
    @Override
    public void mouseDown(final int x, final int y, final int b) {
        if (this.u(x, y) && b == 0 && this.p.po) {
            this.d = true;
        }
        if (this.i(x, y) && b == 0 && this.p.po) {
            this.d = true;
        }
    }
    
    @Override
    public void mouseReleased(final int x, final int y, final int m) {
        this.d = false;
    }
    
    @Override
    public void keyTyped(final char t, final int k) {
    }
    
    public boolean u(final int x, final int y) {
        return x > this.x && x < this.x + this.p.category.getWidth() / 2 + 1 && y > this.y && y < this.y + 16;
    }
    
    public boolean i(final int x, final int y) {
        return x > this.x + this.p.category.getWidth() / 2 && x < this.x + this.p.category.getWidth() && y > this.y && y < this.y + 16;
    }
    
    @Override
    public int getY() {
        return this.y;
    }
}
