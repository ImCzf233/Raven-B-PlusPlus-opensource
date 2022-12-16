package keystrokesmod.client.clickgui.kv;

import keystrokesmod.client.module.*;
import net.minecraft.client.gui.*;
import keystrokesmod.client.clickgui.kv.components.*;
import java.util.*;
import org.lwjgl.opengl.*;
import net.minecraft.client.renderer.*;

public class KvCompactGui extends GuiScreen
{
    private int containerX;
    private int containerY;
    private int containerWidth;
    private int containerHeight;
    private final List<KvCategoryComponent> topCategories;
    private final int padding = 5;
    public KvCategoryComponent currentCategory;
    
    public KvCompactGui() {
        this.topCategories = new ArrayList<KvCategoryComponent>();
        final Module.ModuleCategory[] values2;
        final Module.ModuleCategory[] values = values2 = Module.ModuleCategory.values();
        for (final Module.ModuleCategory moduleCategory : values2) {
            if (moduleCategory.getParentCategory() == Module.ModuleCategory.category) {
                this.topCategories.add(new KvCategoryComponent(moduleCategory));
            }
        }
        this.currentCategory = this.topCategories.get(2);
    }
    
    public void initMain() {
    }
    
    public void onGuiOpen() {
        this.renderModules();
    }
    
    public void func_73863_a(final int mouseX, final int mouseY, final float partialTicks) {
        this.containerWidth = (int)(this.field_146294_l / 1.5);
        this.containerHeight = (int)(this.field_146295_m / 1.5);
        this.containerX = this.field_146294_l / 2 - this.containerWidth / 2;
        this.containerY = this.field_146295_m / 2 - this.containerHeight / 2;
        drawBorderedRoundedRect(this.containerX, this.containerY, this.containerX + this.containerWidth, this.containerY + this.containerHeight, 7, 3, -2143208391, -2143208391);
        Gui.func_73734_a(this.containerX, this.containerY + this.containerHeight / 6, this.containerX + this.containerWidth, this.containerY + this.containerHeight / 6 + 1, -50119);
        Gui.func_73734_a(this.containerX + this.containerWidth / 4, this.containerY + this.containerHeight / 6, this.containerX + this.containerWidth / 4 + 1, this.containerY + this.containerHeight, -50119);
        for (final KvCategoryComponent categoryComponent : this.topCategories) {
            categoryComponent.draw(mouseX, mouseY);
        }
        for (final KvModuleComponent module : this.currentCategory.getModules()) {
            module.draw(mouseX, mouseY);
        }
    }
    
    public static void drawRoundedRect(int x, int y, int x1, int y1, final int radius, final int color) {
        GL11.glPushAttrib(0);
        GL11.glScaled(0.5, 0.5, 0.5);
        x *= (int)2.0;
        y *= (int)2.0;
        x1 *= (int)2.0;
        y1 *= (int)2.0;
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        color(color);
        GL11.glEnable(2848);
        GL11.glBegin(9);
        for (int i = 0; i <= 90; i += 3) {
            GL11.glVertex2d(x + radius + Math.sin(i * 3.141592653589793 / 180.0) * radius * -1.0, y + radius + Math.cos(i * 3.141592653589793 / 180.0) * radius * -1.0);
        }
        for (int i = 90; i <= 180; i += 3) {
            GL11.glVertex2d(x + radius + Math.sin(i * 3.141592653589793 / 180.0) * radius * -1.0, y1 - radius + Math.cos(i * 3.141592653589793 / 180.0) * radius * -1.0);
        }
        for (int i = 0; i <= 90; i += 3) {
            GL11.glVertex2d(x1 - radius + Math.sin(i * 3.141592653589793 / 180.0) * radius, y1 - radius + Math.cos(i * 3.141592653589793 / 180.0) * radius);
        }
        for (int i = 90; i <= 180; i += 3) {
            GL11.glVertex2d(x1 - radius + Math.sin(i * 3.141592653589793 / 180.0) * radius, y + radius + Math.cos(i * 3.141592653589793 / 180.0) * radius);
        }
        GL11.glEnd();
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glDisable(2848);
        GL11.glDisable(3042);
        GL11.glEnable(3553);
        GL11.glScaled(2.0, 2.0, 2.0);
        GL11.glPopAttrib();
    }
    
    public static void color(final int color) {
        final float alpha = (color >> 24 & 0xFF) / 255.0f;
        final float r = (color >> 16 & 0xFF) / 255.0f;
        final float g = (color >> 8 & 0xFF) / 255.0f;
        final float b = (color & 0xFF) / 255.0f;
        GL11.glColor4d((double)r, (double)g, (double)b, (double)alpha);
    }
    
    public static void drawBorderedRoundedRect(final int x, final int y, final int x1, final int y1, final int borderSize, final int borderC, final int insideC) {
        drawRoundedRect(x, y, x1, y1, borderSize, borderC);
        drawRoundedRect(x + 1, y + 1, x1 - 1, y1 - 1, borderSize, insideC);
    }
    
    public static void drawBorderedRoundedRect(final int x, final int y, final int x1, final int y1, final int radius, final int borderSize, final int borderC, final int insideC) {
        drawRoundedRect(x - borderSize, y - borderSize, x1 + borderSize, y1 + borderSize, radius, insideC);
        drawRoundedRect(x, y, x1, y1, radius + borderSize, borderC);
    }
    
    public static void resetColor() {
        GlStateManager.func_179131_c(1.0f, 1.0f, 1.0f, 1.0f);
    }
    
    public void func_73864_a(final int mouseX, final int mouseY, final int mouseButton) {
        for (final KvCategoryComponent component : this.topCategories) {
            if (component.mouseDown(mouseX, mouseY, mouseButton)) {
                return;
            }
        }
    }
    
    public void setCurrentCategory(final KvCategoryComponent cc) {
        this.currentCategory = cc;
        this.renderModules();
    }
    
    private void renderModules() {
        int xOffSet = 0;
        int yOffSet = 0;
        int iterations = 0;
        for (final KvModuleComponent module : this.currentCategory.getModules()) {
            ++iterations;
            module.setCoords(this.containerX + this.containerWidth / 4 + 5 + xOffSet, this.containerY + this.containerHeight / 6 + 5 + yOffSet);
            module.setDimensions((this.containerWidth - this.containerWidth / 4 - 30) / 3, (this.containerWidth - this.containerWidth / 4 - 20) / 3);
            xOffSet += (this.containerWidth - this.containerWidth / 4) / 3;
            if (iterations == 3) {
                iterations = 0;
                xOffSet = 0;
                yOffSet += (this.containerWidth - this.containerWidth / 4) / 3;
            }
        }
        int categoryHeight = 0;
        for (final KvCategoryComponent categoryComponent : this.topCategories) {
            categoryComponent.setCoords(this.containerX + 5, this.containerY + this.containerHeight / 6 + 5 + categoryHeight);
            categoryComponent.setDimensions(this.containerWidth / 4, this.containerHeight / 12);
            categoryHeight += categoryComponent.getHeight();
        }
    }
    
    public boolean func_73868_f() {
        return false;
    }
}
