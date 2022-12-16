package keystrokesmod.client.clickgui.raven.components;

import keystrokesmod.client.clickgui.raven.*;
import keystrokesmod.client.module.*;
import keystrokesmod.client.module.setting.*;
import keystrokesmod.client.module.setting.impl.*;
import java.util.*;
import org.lwjgl.opengl.*;
import java.awt.*;
import net.minecraft.client.*;
import net.minecraft.client.gui.*;
import keystrokesmod.client.module.modules.client.*;
import net.minecraft.client.renderer.*;
import keystrokesmod.client.utils.font.*;
import keystrokesmod.client.main.*;

public class ModuleComponent implements Component
{
    public Module mod;
    public CategoryComponent category;
    public int o;
    private ArrayList<Component> settings;
    public boolean po;
    private static int sf;
    
    public ModuleComponent(final Module mod, final CategoryComponent p, final int o) {
        this.mod = mod;
        this.category = p;
        this.o = o;
        this.settings = new ArrayList<Component>();
        this.po = false;
        mod.setModuleComponent(this);
        this.updateSettings();
    }
    
    public void updateSettings() {
        final ArrayList<Component> newSettings = new ArrayList<Component>();
        int y = this.o + 12;
        if (!this.mod.getSettings().isEmpty()) {
            for (final Setting v : this.mod.getSettings()) {
                if (v instanceof SliderSetting) {
                    final SliderSetting n = (SliderSetting)v;
                    final SliderComponent s = new SliderComponent(n, this, y);
                    newSettings.add(s);
                    y += 16;
                }
                else if (v instanceof TickSetting) {
                    final TickSetting b = (TickSetting)v;
                    final TickComponent c = new TickComponent(this.mod, b, this, y);
                    newSettings.add(c);
                    y += 12;
                }
                else if (v instanceof DescriptionSetting) {
                    final DescriptionSetting d = (DescriptionSetting)v;
                    final DescriptionComponent m = new DescriptionComponent(d, this, y);
                    newSettings.add(m);
                    y += 12;
                }
                else if (v instanceof DoubleSliderSetting) {
                    final DoubleSliderSetting n2 = (DoubleSliderSetting)v;
                    final RangeSliderComponent s2 = new RangeSliderComponent(n2, this, y);
                    newSettings.add(s2);
                    y += 16;
                }
                else if (v instanceof ComboSetting) {
                    final ComboSetting n3 = (ComboSetting)v;
                    final ModeComponent s3 = new ModeComponent(n3, this, y);
                    newSettings.add(s3);
                    y += 12;
                }
                else {
                    if (!(v instanceof RGBSetting)) {
                        continue;
                    }
                    final RGBSetting n4 = (RGBSetting)v;
                    final RGBComponent s4 = new RGBComponent(n4, this, y);
                    newSettings.add(s4);
                    y += 12;
                }
            }
        }
        if (this.mod.isBindable()) {
            newSettings.add(new BindComponent(this, y));
        }
        this.settings = newSettings;
        if (this.po) {
            this.category.r3nd3r();
        }
    }
    
    @Override
    public void setComponentStartAt(final int n) {
        this.o = n;
        int y = this.o + 16 + this.category.scrollheight;
        for (final Component c : this.settings) {
            c.setComponentStartAt(y);
            if (c instanceof SliderComponent || c instanceof RangeSliderComponent || c instanceof RGBComponent) {
                y += 16;
            }
            else {
                if (!(c instanceof TickComponent) && !(c instanceof DescriptionComponent) && !(c instanceof ModeComponent) && !(c instanceof BindComponent)) {
                    continue;
                }
                y += 12;
            }
        }
    }
    
    public static void e() {
        GL11.glDisable(2929);
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glDepthMask(true);
        GL11.glEnable(2848);
        GL11.glHint(3154, 4354);
        GL11.glHint(3155, 4354);
    }
    
    public static void f() {
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glEnable(2929);
        GL11.glDisable(2848);
        GL11.glHint(3154, 4352);
        GL11.glHint(3155, 4352);
        GL11.glEdgeFlag(true);
    }
    
    public static void g(final int rgb) {
        final Color c = new Color(rgb);
        final float a = 255.0f;
        final float r = c.getRed() / 255.0f;
        final float g = c.getGreen() / 255.0f;
        final float b = c.getBlue() / 255.0f;
        GL11.glColor4f(r, g, b, a);
    }
    
    public static void v(final float x, final float y, final float x1, final float y1, final int t, final int b) {
        e();
        GL11.glShadeModel(7425);
        GL11.glBegin(7);
        g(t);
        GL11.glVertex2f(x, y1);
        GL11.glVertex2f(x1, y1);
        g(b);
        GL11.glVertex2f(x1, y);
        GL11.glVertex2f(x, y);
        GL11.glEnd();
        GL11.glShadeModel(7424);
        f();
    }
    
    @Override
    public void draw() {
        final ScaledResolution sr = new ScaledResolution(Minecraft.func_71410_x());
        ModuleComponent.sf = sr.func_78325_e();
        if (GuiModule.showGradientEnabled() && this.mod.isEnabled()) {
            v((float)this.category.getX(), (float)(this.category.getY() + this.o), (float)(this.category.getX() + this.category.getWidth()), (float)(this.category.getY() + 15 + this.o), GuiModule.getEnabledTopRGB(), GuiModule.getEnabledBottomRGB());
        }
        else if (GuiModule.showGradientDisabled() && !this.mod.isEnabled()) {
            v((float)this.category.getX(), (float)(this.category.getY() + this.o), (float)(this.category.getX() + this.category.getWidth()), (float)(this.category.getY() + 15 + this.o), GuiModule.getDisabledTopRGB(), GuiModule.getDisabledBottomRGB());
        }
        GL11.glPushMatrix();
        int button_rgb;
        if (this.mod.isEnabled()) {
            button_rgb = GuiModule.getEnabledTextRGB();
        }
        else if (this.mod.canBeEnabled()) {
            button_rgb = GuiModule.getDisabledTextRGB();
        }
        else {
            button_rgb = new Color(102, 102, 102).getRGB();
        }
        if (GuiModule.useCustomFont()) {
            GlStateManager.func_179117_G();
            FontUtil.normal.drawCenteredString(this.mod.getName(), (float)(this.category.getX() + this.category.getWidth() / 2), (float)(this.category.getY() + this.o + 4), button_rgb);
        }
        else {
            GlStateManager.func_179117_G();
            Minecraft.func_71410_x().field_71466_p.func_175063_a(this.mod.getName(), (float)(this.category.getX() + this.category.getWidth() / 2 - Minecraft.func_71410_x().field_71466_p.func_78256_a(this.mod.getName()) / 2), (float)(this.category.getY() + this.o + 4), button_rgb);
        }
        GL11.glPopMatrix();
        if (this.po && !this.settings.isEmpty()) {
            GL11.glPushMatrix();
            GL11.glEnable(3089);
            GL11.glScissor(this.category.getX() * ModuleComponent.sf, (sr.func_78328_b() - this.category.getY() - this.getHeight() - this.category.getHeight()) * ModuleComponent.sf, this.category.getWidth() * ModuleComponent.sf, (this.getHeight() - this.o - 4) * ModuleComponent.sf);
            for (final Component c : this.settings) {
                c.draw();
            }
            GL11.glDisable(3089);
            GL11.glPopMatrix();
        }
    }
    
    @Override
    public int getHeight() {
        if (!this.po) {
            return 16;
        }
        int h = 16;
        for (final Component c : this.settings) {
            if (c instanceof SliderComponent || c instanceof RangeSliderComponent || c instanceof RGBComponent) {
                h += 16;
            }
            else {
                if (!(c instanceof TickComponent) && !(c instanceof DescriptionComponent) && !(c instanceof ModeComponent) && !(c instanceof BindComponent)) {
                    continue;
                }
                h += 12;
            }
        }
        h += this.category.scrollheight;
        return h;
    }
    
    @Override
    public void update(final int mousePosX, final int mousePosY) {
        if (!this.settings.isEmpty()) {
            for (final Component c : this.settings) {
                c.update(mousePosX, mousePosY);
            }
        }
    }
    
    @Override
    public void mouseDown(final int x, final int y, final int b) {
        if (this.mod.canBeEnabled() && this.ii(x, y) && b == 0) {
            this.mod.toggle();
            Raven.mc.field_71439_g.func_85030_a("gui.button.press", 1.0f, 1.0f);
        }
        if (this.ii(x, y) && b == 1) {
            if (!this.po) {
                if (!this.settings.isEmpty()) {
                    this.category.loadSpecificModule(this);
                    this.po = true;
                }
            }
            else if (this.po) {
                this.po = false;
                this.category.moduleOpened = false;
                this.category.scrollheight = 0;
            }
            this.category.r3nd3r();
            Raven.mc.field_71439_g.func_85030_a("gui.button.press", 1.0f, 1.0f);
        }
        for (final Component c : this.settings) {
            if (c.getY() > this.getY()) {
                c.mouseDown(x, y, b);
            }
        }
    }
    
    @Override
    public void mouseReleased(final int x, final int y, final int m) {
        for (final Component c : this.settings) {
            c.mouseReleased(x, y, m);
        }
    }
    
    @Override
    public void keyTyped(final char t, final int k) {
        for (final Component c : this.settings) {
            c.keyTyped(t, k);
        }
    }
    
    public boolean ii(final int x, final int y) {
        return x > this.category.getX() && x < this.category.getX() + this.category.getWidth() && y > this.category.getY() + this.o && y < this.category.getY() + 16 + this.o;
    }
    
    @Override
    public int getY() {
        return this.category.getY() + this.o + 4;
    }
}
