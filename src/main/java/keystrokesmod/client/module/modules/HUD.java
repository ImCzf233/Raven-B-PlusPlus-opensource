package keystrokesmod.client.module.modules;

import keystrokesmod.client.module.*;
import keystrokesmod.client.hud.*;
import java.util.*;
import keystrokesmod.client.module.setting.*;
import net.minecraft.util.*;
import keystrokesmod.client.module.setting.impl.*;
import keystrokesmod.client.hud.impl.*;
import keystrokesmod.client.event.impl.*;
import net.minecraft.client.renderer.*;
import com.google.common.eventbus.*;
import keystrokesmod.client.utils.*;

public class HUD extends Module
{
    public static ComboSetting<WatermarkMode> watermarkMode;
    public static ComboSetting<ColorMode> watermarkColorMode;
    public static TickSetting watermarkOnlyFirstChar;
    public static TickSetting generalMcFont;
    public static TickSetting generalMcFontShadow;
    public static SliderSetting generalRainbowSaturation;
    public static SliderSetting generalRainbowBrightness;
    public static SliderSetting generalRainbowSeconds;
    public static SliderSetting generalRainbowOffset;
    public static RGBSetting generalColor;
    private static HUD instance;
    public final String clientName = "Raven";
    private HudComponent dragging;
    private int dragX;
    private int dragY;
    public final Map<Class<? extends HudComponent>, HudComponent> comps;
    
    public HUD() {
        super("HUD", ModuleCategory.render);
        this.comps = new HashMap<Class<? extends HudComponent>, HudComponent>();
        this.showInHud = false;
        (HUD.instance = this).registerSettings(new DescriptionSetting(EnumChatFormatting.GRAY + "Watermark Settings"), HUD.watermarkMode = new ComboSetting<WatermarkMode>("Mode" + EnumChatFormatting.GRAY + EnumChatFormatting.RESET, WatermarkMode.Normal), HUD.watermarkColorMode = new ComboSetting<ColorMode>("Color Mode" + EnumChatFormatting.GRAY + EnumChatFormatting.RESET, ColorMode.Static), HUD.watermarkOnlyFirstChar = new TickSetting("Only Color First Char" + EnumChatFormatting.GRAY + EnumChatFormatting.RESET, true), new DescriptionSetting(" "), new DescriptionSetting(EnumChatFormatting.GRAY + "General Settings"), HUD.generalMcFont = new TickSetting("Minecraft Font" + EnumChatFormatting.RED + EnumChatFormatting.RESET, true), HUD.generalMcFontShadow = new TickSetting("MC Font Shadow" + EnumChatFormatting.RED + EnumChatFormatting.RESET, true), HUD.generalColor = new RGBSetting("Color" + EnumChatFormatting.RED + EnumChatFormatting.RESET, 107, 105, 214), HUD.generalRainbowBrightness = new SliderSetting("Rainbow Brightness" + EnumChatFormatting.RED + EnumChatFormatting.RESET, 1.0, 0.0, 1.0, 0.01), HUD.generalRainbowSaturation = new SliderSetting("Rainbow Saturation" + EnumChatFormatting.RED + EnumChatFormatting.RESET, 1.0, 0.0, 1.0, 0.01), HUD.generalRainbowOffset = new SliderSetting("Rainbow Offset" + EnumChatFormatting.RED + EnumChatFormatting.RESET, 150.0, 100.0, 1000.0, 10.0));
        this.add(new WatermarkComponent());
    }
    
    @Subscribe
    public void onRender2D(final Render2DEvent e) {
        if (HUD.mc.field_71439_g != null && HUD.mc.field_71441_e != null) {
            this.comps.forEach((compC, comp) -> {
                GlStateManager.func_179117_G();
                comp.draw(false);
            });
        }
    }
    
    private void add(final HudComponent comp) {
        this.comps.put(comp.getClass(), comp);
    }
    
    public static HUD getInstance() {
        return HUD.instance;
    }
    
    public void setDrag(final HudComponent comp, final int dragX, final int dragY) {
        getInstance().dragging = comp;
        getInstance().dragX = dragX;
        getInstance().dragY = dragY;
    }
    
    public void updateDrag(final int mouseX, final int mouseY) {
        if (this.dragging != null) {
            this.dragging.setX(mouseX - this.dragX).setY(mouseY - this.dragY);
        }
    }
    
    public void endDrag(final int mouseX, final int mouseY) {
        this.updateDrag(mouseX, mouseY);
        getInstance().dragging = null;
    }
    
    public void handleClick(final int mouseX, final int mouseY, final int mouseButton) {
        if (mouseButton == 0) {
            this.comps.forEach((compC, comp) -> comp.onClick(mouseX, mouseY));
        }
    }
    
    public enum WatermarkMode
    {
        Normal, 
        CSGO, 
        Power;
    }
    
    public enum ColorMode
    {
        Static(in -> HUD.generalColor.getRGB()), 
        Fade(in -> RenderUtils.blend(HUD.generalColor.getColor(), HUD.generalColor.getColor().darker(), in / 100.0).getRGB()), 
        Rainbow(in -> RenderUtils.hsbRainbow((float)HUD.generalRainbowSaturation.getInput(), (float)HUD.generalRainbowBrightness.getInput(), (int)(in * HUD.generalRainbowOffset.getInput()), (int)System.currentTimeMillis())), 
        Astolfo(in -> Utils.Client.astolfoColorsDraw(100, 100));
        
        private final ColorSupplier color;
        
        private ColorMode(final ColorSupplier color) {
            this.color = color;
        }
        
        public ColorSupplier getColor() {
            return this.color;
        }
    }
    
    public static class HudFontUtils
    {
        private final HUD hud;
        
        public HudFontUtils(final HUD hud) {
            this.hud = hud;
        }
    }
}
