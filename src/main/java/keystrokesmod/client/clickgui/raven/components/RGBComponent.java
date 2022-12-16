package keystrokesmod.client.clickgui.raven.components;

import keystrokesmod.client.clickgui.raven.*;
import keystrokesmod.client.module.setting.impl.*;
import net.minecraft.client.gui.*;
import org.lwjgl.opengl.*;
import net.minecraft.client.*;

public class RGBComponent implements Component
{
    private final RGBSetting setting;
    private final ModuleComponent module;
    private int moduleStartY;
    private final int boxMargin = 4;
    private final int boxHeight = 4;
    private final int textSize = 11;
    private final int x;
    private double barWidth;
    private boolean mouseDown;
    private Helping mode;
    private static RGBComponent helping;
    
    public RGBComponent(final RGBSetting setting, final ModuleComponent module, final int moduleStartY) {
        this.setting = setting;
        this.module = module;
        this.moduleStartY = moduleStartY;
        this.x = module.category.getX() + module.category.getWidth();
    }
    
    @Override
    public void draw() {
        Gui.func_73734_a(this.module.category.getX() + 4, this.module.category.getY() + this.moduleStartY + 11, this.module.category.getX() - 4 + this.module.category.getWidth(), this.module.category.getY() + this.moduleStartY + 11 + 4, -12302777);
        final int[] drawColor = { -65536, -16711936, -16776961 };
        for (int i = 0; i < 3; ++i) {
            final int color = (int)(this.barWidth * this.setting.getColor(i) / 255.0 + this.module.category.getX() + 4.0);
            Gui.func_73734_a(color, this.module.category.getY() + this.moduleStartY + 11 - 1, color + ((color % 2 == 0) ? 2 : 1), this.module.category.getY() + this.moduleStartY + 11 + 4 + 1, drawColor[i]);
        }
        GL11.glPushMatrix();
        GL11.glScaled(0.5, 0.5, 0.5);
        Minecraft.func_71410_x().field_71466_p.func_175063_a(this.setting.getName() + ": " + this.setting.getRed() + ", " + this.setting.getGreen() + ", " + this.setting.getBlue(), (float)(int)((this.module.category.getX() + 4) * 2.0f), (float)(int)((this.module.category.getY() + this.moduleStartY + 3) * 2.0f), -1);
        GL11.glPopMatrix();
    }
    
    @Override
    public void update(final int mousePosX, final int mousePosY) {
        this.barWidth = this.module.category.getWidth() - 8;
        if (RGBComponent.helping != null && RGBComponent.helping != this) {
            return;
        }
        if (this.mouseDown && ((mousePosX > this.module.category.getX() + 4 && mousePosX < this.module.category.getX() + this.module.category.getWidth() - 4 && mousePosY > this.module.category.getY() + this.moduleStartY && mousePosY < this.module.category.getY() + this.moduleStartY + 11 + 4 + 1) || this.mode != Helping.NONE)) {
            float mouseP = (mousePosX - this.module.category.getX() - 4) / (float)this.barWidth;
            mouseP = ((mouseP > 0.0f) ? ((mouseP < 1.0f) ? mouseP : 1.0f) : 0.0f);
            if (this.mode != Helping.NONE) {
                this.setting.setColor(this.mode.id, (int)(mouseP * 255.0f));
                return;
            }
            switch (this.getTick(mouseP)) {
                case 0: {
                    this.mode = Helping.RED;
                    break;
                }
                case 1: {
                    this.mode = Helping.GREEN;
                    break;
                }
                case 2: {
                    this.mode = Helping.BLUE;
                    break;
                }
            }
            this.setting.setColor(this.mode.id, (int)(mouseP * 255.0f));
            RGBComponent.helping = this;
        }
        else {
            this.mode = Helping.NONE;
            if (RGBComponent.helping == this) {
                RGBComponent.helping = null;
            }
        }
    }
    
    @Override
    public void mouseDown(final int x, final int y, final int b) {
        if (this.i(x, y) && b == 0 && this.module.po) {
            this.mouseDown = true;
        }
    }
    
    @Override
    public void mouseReleased(final int x, final int y, final int b) {
        if (b == 0) {
            this.mouseDown = false;
        }
    }
    
    @Override
    public void keyTyped(final char t, final int k) {
    }
    
    @Override
    public void setComponentStartAt(final int moduleStartY) {
        this.moduleStartY = moduleStartY;
    }
    
    @Override
    public int getHeight() {
        return 0;
    }
    
    private int getTick(final float p) {
        int r = 0;
        float c = 1.0f;
        for (int i = 0; i < 3; ++i) {
            if (Math.abs(this.setting.getColor(i) / 255.0f - p) < c) {
                r = i;
                c = Math.abs(this.setting.getColor(i) / 255.0f - p);
            }
        }
        return r;
    }
    
    @Override
    public int getY() {
        return this.moduleStartY;
    }
    
    public boolean i(final int x, final int y) {
        return x > this.module.category.getX() && x < this.module.category.getX() + this.module.category.getWidth() && y > this.moduleStartY && y < this.moduleStartY + 32;
    }
    
    public enum Helping
    {
        RED(0), 
        GREEN(1), 
        BLUE(2), 
        NONE(-1);
        
        private final int id;
        
        private Helping(final int id) {
            this.id = id;
        }
        
        public int getId() {
            return this.id;
        }
    }
}
