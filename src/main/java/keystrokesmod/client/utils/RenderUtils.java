package keystrokesmod.client.utils;

import java.awt.*;
import org.lwjgl.opengl.*;
import net.minecraft.client.*;
import net.minecraftforge.fml.relauncher.*;
import java.lang.reflect.*;
import net.minecraft.util.*;
import net.minecraft.client.renderer.vertex.*;
import net.minecraft.client.renderer.*;
import net.minecraft.client.gui.*;

public class RenderUtils
{
    public static int hsbRainbow(final float s, final float b, final int offset, final int seconds) {
        final float shit = seconds * 1000.0f / 360.0f;
        final float h = 360.0f * ((System.currentTimeMillis() + offset) % (seconds * 1000.0f) / shit);
        return Color.HSBtoRGB(h, s, b);
    }
    
    public static void stopDrawing() {
        GL11.glDisable(3042);
        GL11.glEnable(3553);
        GL11.glDisable(2848);
        GL11.glDisable(3042);
        GL11.glEnable(2929);
    }
    
    public static void startDrawing() {
        GL11.glEnable(3042);
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(2848);
        GL11.glDisable(3553);
        GL11.glDisable(2929);
        try {
            final Method m = ReflectionHelper.findMethod((Class)EntityRenderer.class, (Object)Minecraft.func_71410_x().field_71460_t, new String[] { "func_78479_a", "setupCameraTransform" }, new Class[] { Float.TYPE, Integer.TYPE });
            m.setAccessible(true);
            m.invoke(Minecraft.func_71410_x().field_71460_t, Utils.Client.getTimer().field_74281_c, 0);
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public static Color blend(final Color color, final Color color1, final double d0) {
        final float f = (float)d0;
        final float f2 = 1.0f - f;
        final float[] afloat = new float[3];
        final float[] afloat2 = new float[3];
        color.getColorComponents(afloat);
        color1.getColorComponents(afloat2);
        return new Color(afloat[0] * f + afloat2[0] * f2, afloat[1] * f + afloat2[1] * f2, afloat[2] * f + afloat2[2] * f2);
    }
    
    public static void drawImage(final ResourceLocation image, final float x, final float y, final float width, final float height) {
        GL11.glDisable(2929);
        GL11.glEnable(3042);
        GL11.glDepthMask(false);
        OpenGlHelper.func_148821_a(770, 771, 1, 0);
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        Utils.mc.func_110434_K().func_110577_a(image);
        Gui.func_146110_a((int)x, (int)y, 0.0f, 0.0f, (int)width, (int)height, width, height);
        GL11.glDepthMask(true);
        GL11.glDisable(3042);
        GL11.glEnable(2929);
    }
    
    public static void drawRect(double d0, double d1, double d2, double d3, final int i) {
        if (d0 < d2) {
            final double d4 = d0;
            d0 = d2;
            d2 = d4;
        }
        if (d1 < d3) {
            final double d4 = d1;
            d1 = d3;
            d3 = d4;
        }
        final float f = (i >> 24 & 0xFF) / 255.0f;
        final float f2 = (i >> 16 & 0xFF) / 255.0f;
        final float f3 = (i >> 8 & 0xFF) / 255.0f;
        final float f4 = (i & 0xFF) / 255.0f;
        final Tessellator tessellator = Tessellator.func_178181_a();
        final WorldRenderer worldrenderer = tessellator.func_178180_c();
        GlStateManager.func_179147_l();
        GlStateManager.func_179090_x();
        GlStateManager.func_179120_a(770, 771, 1, 0);
        GlStateManager.func_179131_c(f2, f3, f4, f);
        worldrenderer.func_181668_a(7, DefaultVertexFormats.field_181705_e);
        worldrenderer.func_181662_b(d0, d3, 0.0).func_181675_d();
        worldrenderer.func_181662_b(d2, d3, 0.0).func_181675_d();
        worldrenderer.func_181662_b(d2, d1, 0.0).func_181675_d();
        worldrenderer.func_181662_b(d0, d1, 0.0).func_181675_d();
        tessellator.func_78381_a();
        GlStateManager.func_179098_w();
        GlStateManager.func_179084_k();
    }
    
    public static void drawBorderedRect(final float f, final float f1, final float f2, final float f3, final float f4, final int i, final int j) {
        drawRect(f, f1, f2, f3, j);
        final float f5 = (i >> 24 & 0xFF) / 255.0f;
        final float f6 = (i >> 16 & 0xFF) / 255.0f;
        final float f7 = (i >> 8 & 0xFF) / 255.0f;
        final float f8 = (i & 0xFF) / 255.0f;
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(2848);
        GL11.glPushMatrix();
        GL11.glColor4f(f6, f7, f8, f5);
        GL11.glLineWidth(f4);
        GL11.glBegin(1);
        GL11.glVertex2d((double)f, (double)f1);
        GL11.glVertex2d((double)f, (double)f3);
        GL11.glVertex2d((double)f2, (double)f3);
        GL11.glVertex2d((double)f2, (double)f1);
        GL11.glVertex2d((double)f, (double)f1);
        GL11.glVertex2d((double)f2, (double)f1);
        GL11.glVertex2d((double)f, (double)f3);
        GL11.glVertex2d((double)f2, (double)f3);
        GL11.glEnd();
        GL11.glPopMatrix();
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glDisable(2848);
    }
    
    public static void setColor(final int color) {
        final float a = (color >> 24 & 0xFF) / 255.0f;
        final float r = (color >> 16 & 0xFF) / 255.0f;
        final float g = (color >> 8 & 0xFF) / 255.0f;
        final float b = (color & 0xFF) / 255.0f;
        GL11.glColor4f(r, g, b, a);
    }
    
    public static void drawRoundedRect(float x, float y, float x1, float y1, final float radius, final int color) {
        GL11.glPushAttrib(0);
        GL11.glScaled(0.5, 0.5, 0.5);
        x *= 2.0;
        y *= 2.0;
        x1 *= 2.0;
        y1 *= 2.0;
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glEnable(2848);
        setColor(color);
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
        GL11.glDisable(2848);
        GL11.glScaled(2.0, 2.0, 2.0);
        GL11.glEnable(3042);
        GL11.glPopAttrib();
        GlStateManager.func_179131_c(1.0f, 1.0f, 1.0f, 1.0f);
    }
    
    public static void drawRoundedOutline(float x, float y, float x1, float y1, final float radius, final float lineWidth, final int color) {
        GL11.glPushAttrib(0);
        GL11.glScaled(0.5, 0.5, 0.5);
        x *= 2.0;
        y *= 2.0;
        x1 *= 2.0;
        y1 *= 2.0;
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        setColor(color);
        GL11.glEnable(2848);
        GL11.glLineWidth(lineWidth);
        GL11.glBegin(2);
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
        GL11.glLineWidth(1.0f);
        GlStateManager.func_179131_c(1.0f, 1.0f, 1.0f, 1.0f);
    }
    
    public static void drawOutlinedString(final String text, final int x, final int y, final int color, final int outlineColor) {
        final FontRenderer fr = Minecraft.func_71410_x().field_71466_p;
        fr.func_78276_b(text, x + 1, y, outlineColor);
        fr.func_78276_b(text, x - 1, y, outlineColor);
        fr.func_78276_b(text, x, y + 1, outlineColor);
        fr.func_78276_b(text, x, y - 1, outlineColor);
        fr.func_78276_b(text, x, y, color);
    }
    
    public static void drawOutlinedString(final String text, final int x, final int y, final int color) {
        drawOutlinedString(text, x, y, color, 0);
    }
}
