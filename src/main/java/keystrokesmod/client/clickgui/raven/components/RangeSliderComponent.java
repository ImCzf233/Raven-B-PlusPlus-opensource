package keystrokesmod.client.clickgui.raven.components;

import keystrokesmod.client.clickgui.raven.*;
import keystrokesmod.client.module.setting.impl.*;
import net.minecraft.client.gui.*;
import keystrokesmod.client.utils.*;
import org.lwjgl.opengl.*;
import net.minecraft.client.*;
import java.math.*;

public class RangeSliderComponent implements Component
{
    private final DoubleSliderSetting doubleSlider;
    private final ModuleComponent module;
    private double barWidth;
    private double blankWidth;
    private int sliderStartX;
    private int sliderStartY;
    private int moduleStartY;
    private boolean mouseDown;
    private boolean inMotion;
    private Helping mode;
    private final int boxMargin = 4;
    private final int boxHeight = 4;
    private final int textSize = 11;
    
    public RangeSliderComponent(final DoubleSliderSetting doubleSlider, final ModuleComponent module, final int moduleStartY) {
        this.mode = Helping.NONE;
        this.doubleSlider = doubleSlider;
        this.module = module;
        this.sliderStartX = this.module.category.getX() + 4;
        this.sliderStartY = moduleStartY + module.category.getY();
        this.moduleStartY = moduleStartY;
    }
    
    @Override
    public void draw() {
        Gui.func_73734_a(this.module.category.getX() + 4, this.module.category.getY() + this.moduleStartY + 11, this.module.category.getX() - 4 + this.module.category.getWidth(), this.module.category.getY() + this.moduleStartY + 11 + 4, -12302777);
        final int startToDrawFrom = this.module.category.getX() + 4 + (int)this.blankWidth;
        final int finishDrawingAt = startToDrawFrom + (int)this.barWidth;
        final int middleThing = (int)Utils.Java.round(this.barWidth / 2.0, 0) + this.module.category.getX() + (int)this.blankWidth + 4 - 1;
        Gui.func_73734_a(startToDrawFrom, this.module.category.getY() + this.moduleStartY + 11, finishDrawingAt, this.module.category.getY() + this.moduleStartY + 11 + 4, Utils.Client.astolfoColorsDraw(14, 10));
        Gui.func_73734_a(middleThing, this.module.category.getY() + this.moduleStartY + 11 - 1, middleThing + ((middleThing % 2 == 0) ? 2 : 1), this.module.category.getY() + this.moduleStartY + 11 + 4 + 1, -14869217);
        GL11.glPushMatrix();
        GL11.glScaled(0.5, 0.5, 0.5);
        Minecraft.func_71410_x().field_71466_p.func_175063_a(this.doubleSlider.getName() + ": " + this.doubleSlider.getInputMin() + ", " + this.doubleSlider.getInputMax(), (float)(int)((this.module.category.getX() + 4) * 2.0f), (float)(int)((this.module.category.getY() + this.moduleStartY + 3) * 2.0f), -1);
        GL11.glPopMatrix();
    }
    
    @Override
    public void setComponentStartAt(final int posY) {
        this.moduleStartY = posY;
    }
    
    @Override
    public int getHeight() {
        return 0;
    }
    
    @Override
    public void update(final int mousePosX, final int mousePosY) {
        this.sliderStartY = this.module.category.getY() + this.moduleStartY;
        this.sliderStartX = this.module.category.getX() + 4;
        final double mousePressedAt = Math.min(this.module.category.getWidth() - 8, Math.max(0, mousePosX - this.sliderStartX));
        this.blankWidth = (this.module.category.getWidth() - 8) * (this.doubleSlider.getInputMin() - this.doubleSlider.getMin()) / (this.doubleSlider.getMax() - this.doubleSlider.getMin());
        this.barWidth = (this.module.category.getWidth() - 8) * (this.doubleSlider.getInputMax() - this.doubleSlider.getInputMin()) / (this.doubleSlider.getMax() - this.doubleSlider.getMin());
        if (this.mouseDown) {
            if (mousePressedAt > this.blankWidth + this.barWidth / 2.0 || this.mode == Helping.MAX) {
                if (this.mode == Helping.NONE) {
                    this.mode = Helping.MAX;
                }
                if (this.mode == Helping.MAX) {
                    if (mousePressedAt <= this.blankWidth) {
                        this.doubleSlider.setValueMax(this.doubleSlider.getInputMin());
                    }
                    else {
                        final double n = r(mousePressedAt / (this.module.category.getWidth() - 8) * (this.doubleSlider.getMax() - this.doubleSlider.getMin()) + this.doubleSlider.getMin(), 2);
                        this.doubleSlider.setValueMax(n);
                    }
                }
            }
            if (mousePressedAt < this.blankWidth + this.barWidth / 2.0 || this.mode == Helping.MIN) {
                if (this.mode == Helping.NONE) {
                    this.mode = Helping.MIN;
                }
                if (this.mode == Helping.MIN) {
                    if (mousePressedAt == 0.0) {
                        this.doubleSlider.setValueMin(this.doubleSlider.getMin());
                    }
                    else if (mousePressedAt >= this.barWidth + this.blankWidth) {
                        this.doubleSlider.setValueMin(this.doubleSlider.getMax());
                    }
                    else {
                        final double n = r(mousePressedAt / (this.module.category.getWidth() - 8) * (this.doubleSlider.getMax() - this.doubleSlider.getMin()) + this.doubleSlider.getMin(), 2);
                        this.doubleSlider.setValueMin(n);
                    }
                }
            }
        }
        else if (this.mode != Helping.NONE) {
            this.mode = Helping.NONE;
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
        if (this.u(x, y) && b == 0 && this.module.po) {
            this.mouseDown = true;
        }
        if (this.i(x, y) && b == 0 && this.module.po) {
            this.mouseDown = true;
        }
    }
    
    @Override
    public void mouseReleased(final int x, final int y, final int m) {
        this.mouseDown = false;
    }
    
    @Override
    public void keyTyped(final char t, final int k) {
    }
    
    public boolean u(final int x, final int y) {
        return x > this.sliderStartX && x < this.sliderStartX + this.module.category.getWidth() / 2 + 1 && y > this.sliderStartY && y < this.sliderStartY + 16;
    }
    
    public boolean i(final int x, final int y) {
        return x > this.sliderStartX + this.module.category.getWidth() / 2 && x < this.sliderStartX + this.module.category.getWidth() && y > this.sliderStartY && y < this.sliderStartY + 16;
    }
    
    @Override
    public int getY() {
        return this.moduleStartY;
    }
    
    public enum Helping
    {
        MIN, 
        MAX, 
        NONE;
    }
}
