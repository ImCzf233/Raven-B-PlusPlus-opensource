package keystrokesmod.client.clickgui.raven.components;

import keystrokesmod.client.clickgui.raven.*;
import keystrokesmod.client.module.*;
import keystrokesmod.client.main.*;
import java.util.*;
import net.minecraft.client.*;
import keystrokesmod.client.module.modules.client.*;
import net.minecraft.client.gui.*;
import net.minecraft.client.renderer.*;
import java.awt.*;
import keystrokesmod.client.utils.font.*;

public class CategoryComponent
{
    public ArrayList<Component> modulesInCategory;
    public ArrayList<Component> moduleInCategory;
    public Module.ModuleCategory categoryName;
    public boolean moduleOpened;
    public boolean categoryOpened;
    public boolean inUse;
    private int width;
    private int x;
    private int y;
    private final int bh;
    public int xx;
    public int yy;
    public int chromaSpeed;
    public boolean n4m;
    public boolean pin;
    public String pvp;
    private final double marginY;
    private final double marginX;
    private boolean visable;
    public int scrollheight;
    private int categoryHeight;
    
    public CategoryComponent(final Module.ModuleCategory category) {
        this.modulesInCategory = new ArrayList<Component>();
        this.moduleInCategory = new ArrayList<Component>();
        this.visable = true;
        this.categoryName = category;
        this.width = 92;
        this.x = 5;
        this.y = 5;
        this.bh = 13;
        this.xx = 0;
        this.categoryOpened = false;
        this.inUse = false;
        this.chromaSpeed = 3;
        this.marginX = 80.0;
        this.marginY = 4.5;
        this.loadAllModules();
    }
    
    public void loadAllModules() {
        this.modulesInCategory.clear();
        int tY = this.bh + 3;
        for (final Module mod : Raven.moduleManager.getModulesInCategory(this.categoryName)) {
            final ModuleComponent b = new ModuleComponent(mod, this, tY);
            this.modulesInCategory.add(b);
            tY += 16;
        }
        this.moduleOpened = false;
    }
    
    public void loadSpecificModule(final Component component) {
        this.moduleInCategory.clear();
        this.moduleInCategory.add(component);
        this.moduleOpened = true;
    }
    
    public ArrayList<Component> getModules() {
        return this.moduleOpened ? this.moduleInCategory : this.modulesInCategory;
    }
    
    public void setX(final int n) {
        this.x = n;
        if (Raven.clientConfig != null) {
            Raven.clientConfig.saveConfig();
        }
    }
    
    public void setY(final int y) {
        this.y = y;
        if (Raven.clientConfig != null) {
            Raven.clientConfig.saveConfig();
        }
    }
    
    public void mousePressed(final boolean d) {
        this.inUse = d;
    }
    
    public boolean p() {
        return this.pin;
    }
    
    public void cv(final boolean on) {
        this.pin = on;
    }
    
    public boolean isOpened() {
        return this.categoryOpened;
    }
    
    public void setOpened(final boolean on) {
        this.categoryOpened = on;
        if (Raven.clientConfig != null) {
            Raven.clientConfig.saveConfig();
        }
    }
    
    public void rf() {
        final Minecraft mc = Minecraft.func_71410_x();
        if (!this.visable) {
            return;
        }
        this.width = 92;
        if (!this.getModules().isEmpty() && this.categoryOpened) {
            this.categoryHeight = 0;
            for (final Component moduleRenderManager : this.getModules()) {
                this.categoryHeight += moduleRenderManager.getHeight();
            }
            final Color bgColor = this.moduleOpened ? GuiModule.getCategoryBackgroundColor() : GuiModule.getSettingBackgroundColor();
            Gui.func_73734_a(this.x - 1, this.y, this.x + this.width + 1, this.y + this.bh + this.categoryHeight + 4, bgColor.getRGB());
        }
        if (GuiModule.isCategoryBackgroundToggled()) {
            Gui.func_73734_a(this.x - 2, this.y, this.x + this.width + 2, this.y + this.bh + 3, GuiModule.getBackgroundRGB());
            GlStateManager.func_179117_G();
        }
        int colorCN = 0;
        switch (GuiModule.getCNColor()) {
            case STATIC: {
                colorCN = GuiModule.getCategoryNameRGB();
                break;
            }
            case RAINBOW: {
                colorCN = Color.getHSBColor(System.currentTimeMillis() % (7500L / this.chromaSpeed) / (7500.0f / this.chromaSpeed), 1.0f, 1.0f).getRGB();
                break;
            }
            default: {
                throw new RuntimeException("if this happens, im coming to your house (you broke my code)");
            }
        }
        if (GuiModule.useCustomFont()) {
            FontUtil.two.drawSmoothString(this.n4m ? this.pvp : this.categoryName.getName(), (float)(this.x + 2), (float)(this.y + 4), colorCN);
        }
        else {
            mc.field_71466_p.func_175065_a(this.n4m ? this.pvp : this.categoryName.getName(), (float)(this.x + 2), (float)(this.y + 4), colorCN, false);
        }
        if (!this.n4m) {
            mc.field_71466_p.func_175065_a(this.categoryOpened ? "-" : "+", (float)(this.x + this.marginX), (float)(this.y + this.marginY), Color.white.getRGB(), false);
            if (this.categoryOpened && !this.getModules().isEmpty()) {
                for (final Component c2 : this.getModules()) {
                    c2.draw();
                }
            }
        }
    }
    
    public void updateModules() {
        int tY = this.bh + 3;
        this.modulesInCategory.clear();
        for (final Module mod : Raven.moduleManager.getModulesInCategory(this.categoryName)) {
            final ModuleComponent b = new ModuleComponent(mod, this, tY);
            this.modulesInCategory.add(b);
            tY += 16;
        }
    }
    
    public void r3nd3r() {
        int o = this.bh + 3;
        for (final Component c : this.getModules()) {
            c.setComponentStartAt(o);
            o += c.getHeight();
        }
    }
    
    public int getX() {
        return this.x;
    }
    
    public int getY() {
        return this.y;
    }
    
    public int getWidth() {
        return this.width;
    }
    
    public void up(final int x, final int y) {
        if (this.inUse) {
            this.setX(x - this.xx);
            this.setY(y - this.yy);
        }
    }
    
    public void scroll(final float ss) {
        if (ss > 0.0f || this.getActualHeight() + ss > 100.0f) {
            this.scrollheight += (int)ss;
        }
        if (this.scrollheight <= 0) {
            this.r3nd3r();
        }
        else {
            this.scrollheight = 0;
        }
    }
    
    public boolean i(final int x, final int y) {
        return x >= this.x + 92 - 13 && x <= this.x + this.width && y >= this.y + 2.0f && y <= this.y + this.bh + 1;
    }
    
    public boolean mousePressed(final int x, final int y) {
        return x >= this.x + 77 && x <= this.x + this.width - 6 && y >= this.y + 2.0f && y <= this.y + this.bh + 1;
    }
    
    public boolean insideArea(final int x, final int y) {
        return x >= this.x && x <= this.x + this.width && y >= this.y && y <= this.y + this.bh;
    }
    
    public boolean insideAllArea(final int x, final int y) {
        return x >= this.x && x <= this.x + this.width && y >= this.y && y <= this.y + this.getActualHeight() - this.scrollheight;
    }
    
    public String getName() {
        return String.valueOf(this.getModules());
    }
    
    public int getHeight() {
        return this.bh;
    }
    
    public int getActualHeight() {
        int h = this.bh + 16 + this.categoryHeight;
        for (final Component c : this.getModules()) {
            h += c.getHeight();
        }
        return h;
    }
    
    public void setLocation(final int parseInt, final int parseInt1) {
        this.x = parseInt;
        this.y = parseInt1;
    }
    
    public void setVisable(final boolean vis) {
        this.visable = vis;
    }
    
    public boolean isVisable() {
        return this.visable;
    }
}
