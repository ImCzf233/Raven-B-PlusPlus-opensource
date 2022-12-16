package keystrokesmod.client.notifications;

import java.awt.*;
import keystrokesmod.client.module.modules.client.*;
import keystrokesmod.client.utils.*;
import net.minecraft.client.*;
import keystrokesmod.client.clickgui.raven.*;
import net.minecraft.client.gui.*;
import net.minecraft.client.renderer.vertex.*;
import net.minecraft.client.renderer.*;

public class Notification
{
    private NotificationType type;
    private String title;
    private String messsage;
    private long start;
    private long fadedIn;
    private long fadeOut;
    private long end;
    
    public Notification(final NotificationType type, final String title, final String messsage, final int length) {
        this.type = type;
        this.title = title;
        this.messsage = messsage;
        this.fadedIn = 200 * length;
        this.fadeOut = this.fadedIn + 500 * length;
        this.end = this.fadeOut + this.fadedIn;
    }
    
    public void show() {
        this.start = System.currentTimeMillis();
    }
    
    public boolean isShown() {
        return this.getTime() <= this.end;
    }
    
    private long getTime() {
        return System.currentTimeMillis() - this.start;
    }
    
    public void render() {
        final int width = 120;
        final int height = 30;
        final long time = this.getTime();
        double offset;
        if (time < this.fadedIn) {
            offset = Math.tanh(time / (double)this.fadedIn * 3.0) * width;
        }
        else if (time > this.fadeOut) {
            offset = Math.tanh(3.0 - (time - this.fadeOut) / (double)(this.end - this.fadeOut) * 3.0) * width;
        }
        else {
            offset = width;
        }
        Color color = new Color(0, 0, 0, 220);
        Color color2;
        if (GuiModule.rainbowNotification()) {
            color2 = new Color(Utils.Client.rainbowDraw(2L, 1200L));
        }
        else if (this.type == NotificationType.INFO) {
            color2 = new Color(0, 26, 169);
        }
        else if (this.type == NotificationType.WARNING) {
            color2 = new Color(204, 193, 0);
        }
        else {
            color2 = new Color(204, 0, 18);
            final int i = Math.max(0, Math.min(255, (int)(Math.sin(time / 100.0) * 255.0 / 2.0 + 127.5)));
            color = new Color(i, 0, 0, 220);
        }
        final FontRenderer fontRenderer = Minecraft.func_71410_x().field_71466_p;
        final int messageWidth = fontRenderer.func_78256_a(this.messsage);
        final int titleWidth = fontRenderer.func_78256_a(this.title);
        offset += Math.floor(Math.max(titleWidth, messageWidth) * 0.62f);
        final ScaledResolution scaledResolution = new ScaledResolution(Minecraft.func_71410_x());
        if (Minecraft.func_71410_x().field_71462_r instanceof ClickGui) {
            drawRect(scaledResolution.func_78326_a() - offset, scaledResolution.func_78328_b() - 22 - height, scaledResolution.func_78326_a(), scaledResolution.func_78328_b() - 22, color.getRGB());
            drawRect(scaledResolution.func_78326_a() - offset, scaledResolution.func_78328_b() - 22 - height, scaledResolution.func_78326_a() - offset + 4.0, scaledResolution.func_78328_b() - 22, color2.getRGB());
            fontRenderer.func_78276_b(this.title, (int)(scaledResolution.func_78326_a() - offset + 8.0), scaledResolution.func_78328_b() - 20 - height, -1);
            final int xBegin = (int)(scaledResolution.func_78326_a() - offset + 8.0);
            final int yBegin = scaledResolution.func_78328_b() - 39;
            final int xEnd = xBegin + titleWidth;
            final int yEnd = yBegin + 1;
            drawRect(xBegin, yBegin, xEnd, yEnd, GuiModule.rainbowNotification() ? Utils.Client.rainbowDraw(2L, 1200L) : new Color(-1).getRGB());
            fontRenderer.func_78276_b(this.messsage, (int)(scaledResolution.func_78326_a() - offset + 8.0), scaledResolution.func_78328_b() - 33, -1);
        }
        else {
            drawRect(scaledResolution.func_78326_a() - offset, scaledResolution.func_78328_b() - 5 - height, scaledResolution.func_78326_a(), scaledResolution.func_78328_b() - 5, color.getRGB());
            drawRect(scaledResolution.func_78326_a() - offset, scaledResolution.func_78328_b() - 5 - height, scaledResolution.func_78326_a() - offset + 4.0, scaledResolution.func_78328_b() - 5, color2.getRGB());
            fontRenderer.func_78276_b(this.title, (int)(scaledResolution.func_78326_a() - offset + 8.0), scaledResolution.func_78328_b() - 2 - height, -1);
            final int xBegin = (int)(scaledResolution.func_78326_a() - offset + 8.0);
            final int yBegin = scaledResolution.func_78328_b() - 22;
            final int xEnd = xBegin + titleWidth;
            final int yEnd = yBegin + 1;
            drawRect(xBegin, yBegin, xEnd, yEnd, GuiModule.rainbowNotification() ? Utils.Client.rainbowDraw(2L, 1200L) : new Color(-1).getRGB());
            fontRenderer.func_78276_b(this.messsage, (int)(scaledResolution.func_78326_a() - offset + 8.0), scaledResolution.func_78328_b() - 15, -1);
        }
    }
    
    public static void drawRect(double left, double top, double right, double bottom, final int color) {
        if (left < right) {
            final double i = left;
            left = right;
            right = i;
        }
        if (top < bottom) {
            final double j = top;
            top = bottom;
            bottom = j;
        }
        final float f3 = (color >> 24 & 0xFF) / 255.0f;
        final float f4 = (color >> 16 & 0xFF) / 255.0f;
        final float f5 = (color >> 8 & 0xFF) / 255.0f;
        final float f6 = (color & 0xFF) / 255.0f;
        final Tessellator tessellator = Tessellator.func_178181_a();
        final WorldRenderer worldrenderer = tessellator.func_178180_c();
        GlStateManager.func_179147_l();
        GlStateManager.func_179090_x();
        GlStateManager.func_179120_a(770, 771, 1, 0);
        GlStateManager.func_179131_c(f4, f5, f6, f3);
        worldrenderer.func_181668_a(7, DefaultVertexFormats.field_181705_e);
        worldrenderer.func_181662_b(left, bottom, 0.0).func_181675_d();
        worldrenderer.func_181662_b(right, bottom, 0.0).func_181675_d();
        worldrenderer.func_181662_b(right, top, 0.0).func_181675_d();
        worldrenderer.func_181662_b(left, top, 0.0).func_181675_d();
        tessellator.func_78381_a();
        GlStateManager.func_179098_w();
        GlStateManager.func_179084_k();
    }
    
    public static void drawRect(final int mode, double left, double top, double right, double bottom, final int color) {
        if (left < right) {
            final double i = left;
            left = right;
            right = i;
        }
        if (top < bottom) {
            final double j = top;
            top = bottom;
            bottom = j;
        }
        final float f3 = (color >> 24 & 0xFF) / 255.0f;
        final float f4 = (color >> 16 & 0xFF) / 255.0f;
        final float f5 = (color >> 8 & 0xFF) / 255.0f;
        final float f6 = (color & 0xFF) / 255.0f;
        final Tessellator tessellator = Tessellator.func_178181_a();
        final WorldRenderer worldrenderer = tessellator.func_178180_c();
        GlStateManager.func_179147_l();
        GlStateManager.func_179090_x();
        GlStateManager.func_179120_a(770, 771, 1, 0);
        GlStateManager.func_179131_c(f4, f5, f6, f3);
        worldrenderer.func_181668_a(mode, DefaultVertexFormats.field_181705_e);
        worldrenderer.func_181662_b(left, bottom, 0.0).func_181675_d();
        worldrenderer.func_181662_b(right, bottom, 0.0).func_181675_d();
        worldrenderer.func_181662_b(right, top, 0.0).func_181675_d();
        worldrenderer.func_181662_b(left, top, 0.0).func_181675_d();
        tessellator.func_78381_a();
        GlStateManager.func_179098_w();
        GlStateManager.func_179084_k();
    }
}
