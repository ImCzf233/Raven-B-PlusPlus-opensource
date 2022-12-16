package keystrokesmod.client.module.modules.client;

import keystrokesmod.client.module.*;
import keystrokesmod.client.module.setting.impl.*;
import keystrokesmod.client.main.*;
import keystrokesmod.client.module.setting.*;
import keystrokesmod.client.clickgui.raven.components.*;
import java.util.*;
import keystrokesmod.client.utils.*;
import net.minecraft.client.gui.*;
import java.awt.*;

public class GuiModule extends Module
{
    private static SliderSetting backgroundOpacity;
    private static ComboSetting preset;
    private static ComboSetting cnColor;
    private static TickSetting categoryBackground;
    private static TickSetting cleanUp;
    private static TickSetting reset;
    private static TickSetting usePreset;
    private static TickSetting rainbowNotification;
    private static TickSetting notifications;
    private static TickSetting betagui;
    private static TickSetting matchTopWBottomEnabled;
    private static TickSetting matchTopWBottomDisabled;
    private static TickSetting showGradientEnabled;
    private static TickSetting showGradientDisabled;
    private static TickSetting useCustomFont;
    private static RGBSetting enabledTopRGB;
    private static RGBSetting enabledBottomRGB;
    private static RGBSetting enabledTextRGB;
    private static RGBSetting disabledTopRGB;
    private static RGBSetting disabledBottomRGB;
    private static RGBSetting disabledTextRGB;
    private static RGBSetting backgroundRGB;
    private static RGBSetting settingBackgroundRGB;
    private static RGBSetting categoryBackgroundRGB;
    private static RGBSetting categoryNameRGB;
    
    public GuiModule() {
        super("Gui", ModuleCategory.client);
        this.withKeycode(54);
        if (Raven.debugger) {
            this.registerSetting(GuiModule.betagui = new TickSetting("beta gui (VERY BETA)", false));
        }
        this.registerSetting(GuiModule.enabledTopRGB = new RGBSetting("EnabledTopRGB", 0, 200, 50));
        this.registerSetting(GuiModule.enabledBottomRGB = new RGBSetting("EnabledBottomRGB", 0, 200, 50));
        this.registerSetting(GuiModule.enabledTextRGB = new RGBSetting("EnabledTextRGB", 0, 200, 50));
        this.registerSetting(GuiModule.disabledTopRGB = new RGBSetting("DisabledTopRGB", 0, 200, 50));
        this.registerSetting(GuiModule.disabledBottomRGB = new RGBSetting("DisabledBottomRGB", 0, 200, 50));
        this.registerSetting(GuiModule.disabledTextRGB = new RGBSetting("DisabledTextRGB", 0, 200, 50));
        this.registerSetting(GuiModule.backgroundRGB = new RGBSetting("BackgroundRGB", 0, 0, 0));
        this.registerSetting(GuiModule.settingBackgroundRGB = new RGBSetting("SettingBackgroundRGB", 0, 0, 0));
        this.registerSetting(GuiModule.categoryBackgroundRGB = new RGBSetting("CategoryBackgroundRGB", 0, 0, 0));
        this.registerSetting(GuiModule.cnColor = new ComboSetting("Category Name Color", (T)CNColor.STATIC));
        this.registerSetting(GuiModule.categoryNameRGB = new RGBSetting("CategoryNameRGB", 255, 255, 255));
        this.registerSetting(GuiModule.matchTopWBottomEnabled = new TickSetting("Match Top enabled w/ bottom enabled", false));
        this.registerSetting(GuiModule.matchTopWBottomDisabled = new TickSetting("Match Top enabled w/ bottom disabled", false));
        this.registerSetting(GuiModule.showGradientDisabled = new TickSetting("Show gradient when disabled", true));
        this.registerSetting(GuiModule.showGradientEnabled = new TickSetting("Show gradient when enabled", true));
        this.registerSetting(GuiModule.backgroundOpacity = new SliderSetting("Background Opacity %", 43.0, 0.0, 100.0, 1.0));
        this.registerSetting(GuiModule.categoryBackground = new TickSetting("Category Background", true));
        this.registerSetting(GuiModule.useCustomFont = new TickSetting("Smooth Font (BROKEN DONT USE)", false));
        this.registerSetting(GuiModule.cleanUp = new TickSetting("Clean Up", false));
        this.registerSetting(GuiModule.notifications = new TickSetting("Notifications", true));
        this.registerSetting(GuiModule.rainbowNotification = new TickSetting("Rainbow Notifications", true));
        this.registerSetting(GuiModule.reset = new TickSetting("Reset position", false));
        this.registerSetting(GuiModule.usePreset = new TickSetting("Use preset", true));
        this.registerSetting(GuiModule.preset = new ComboSetting("Preset", (T)Preset.Vape));
    }
    
    @Override
    public void guiButtonToggled(final TickSetting setting) {
        if (setting == GuiModule.cleanUp) {
            GuiModule.cleanUp.disable();
            for (final CategoryComponent cc : Raven.clickGui.getCategoryList()) {
                cc.setX(cc.getX() / 50 * 50 + ((cc.getX() % 50 > 25) ? 50 : 0));
                cc.setY(cc.getY() / 50 * 50 + ((cc.getY() % 50 > 25) ? 50 : 0));
            }
        }
        else if (setting == GuiModule.matchTopWBottomEnabled) {
            GuiModule.matchTopWBottomEnabled.disable();
            GuiModule.enabledTopRGB.setColors(GuiModule.enabledBottomRGB.getColors());
        }
        else if (setting == GuiModule.matchTopWBottomDisabled) {
            GuiModule.matchTopWBottomDisabled.disable();
            GuiModule.disabledTopRGB.setColors(GuiModule.disabledBottomRGB.getColors());
        }
        else if (setting == GuiModule.reset) {
            GuiModule.reset.disable();
            Raven.clickGui.resetSort();
        }
    }
    
    @Override
    public void onEnable() {
        if (Utils.Player.isPlayerInGame() && (GuiModule.mc.field_71462_r != Raven.clickGui || GuiModule.mc.field_71462_r != Raven.kvCompactGui)) {
            if (Raven.debugger) {
                Raven.kvCompactGui.onGuiOpen();
                GuiModule.mc.func_147108_a((GuiScreen)Raven.kvCompactGui);
            }
            else {
                GuiModule.mc.func_147108_a((GuiScreen)Raven.clickGui);
                Raven.clickGui.initMain();
            }
        }
        this.disable();
    }
    
    private static Preset getPresetMode() {
        return GuiModule.preset.getMode();
    }
    
    public static int getBackgroundOpacity() {
        return GuiModule.usePreset.isToggled() ? getPresetMode().backgroundOpacity : ((int)(GuiModule.backgroundOpacity.getInput() * 2.55));
    }
    
    public static boolean isCategoryBackgroundToggled() {
        return GuiModule.usePreset.isToggled() ? getPresetMode().categoryBackground : GuiModule.categoryBackground.isToggled();
    }
    
    public static boolean showGradientEnabled() {
        return GuiModule.usePreset.isToggled() ? getPresetMode().showGradientEnabled : GuiModule.showGradientEnabled.isToggled();
    }
    
    public static boolean showGradientDisabled() {
        return GuiModule.usePreset.isToggled() ? getPresetMode().showGradientDisabled : GuiModule.showGradientDisabled.isToggled();
    }
    
    public static boolean useCustomFont() {
        return GuiModule.usePreset.isToggled() ? getPresetMode().useCustomFont : GuiModule.useCustomFont.isToggled();
    }
    
    public static int getEnabledTopRGB() {
        return GuiModule.usePreset.isToggled() ? getPresetMode().enabledTopRGB.getRGB() : GuiModule.enabledTopRGB.getRGB();
    }
    
    public static int getEnabledBottomRGB() {
        return GuiModule.usePreset.isToggled() ? getPresetMode().enabledBottomRGB.getRGB() : GuiModule.enabledBottomRGB.getRGB();
    }
    
    public static int getEnabledTextRGB() {
        return GuiModule.usePreset.isToggled() ? getPresetMode().enabledTextRGB.getRGB() : GuiModule.enabledTextRGB.getRGB();
    }
    
    public static int getDisabledTopRGB() {
        return GuiModule.usePreset.isToggled() ? getPresetMode().disabledTopRGB.getRGB() : GuiModule.disabledTopRGB.getRGB();
    }
    
    public static int getDisabledBottomRGB() {
        return GuiModule.usePreset.isToggled() ? getPresetMode().disabledBottomRGB.getRGB() : GuiModule.disabledBottomRGB.getRGB();
    }
    
    public static int getDisabledTextRGB() {
        return GuiModule.usePreset.isToggled() ? getPresetMode().disabledTextRGB.getRGB() : GuiModule.disabledTextRGB.getRGB();
    }
    
    public static int getBackgroundRGB() {
        return GuiModule.usePreset.isToggled() ? getPresetMode().backgroundRGB.getRGB() : GuiModule.backgroundRGB.getRGB();
    }
    
    public static Color getSettingBackgroundColor() {
        return GuiModule.usePreset.isToggled() ? getPresetMode().settingBackgroundRGB : new Color(GuiModule.settingBackgroundRGB.getRed(), GuiModule.settingBackgroundRGB.getGreen(), GuiModule.settingBackgroundRGB.getBlue(), getBackgroundOpacity());
    }
    
    public static Color getCategoryBackgroundColor() {
        return GuiModule.usePreset.isToggled() ? getPresetMode().categoryBackgroundRGB : new Color(GuiModule.categoryBackgroundRGB.getRed(), GuiModule.categoryBackgroundRGB.getGreen(), GuiModule.categoryBackgroundRGB.getBlue(), getBackgroundOpacity());
    }
    
    public static int getCategoryNameRGB() {
        return GuiModule.usePreset.isToggled() ? getPresetMode().categoryNameRGB.getRGB() : GuiModule.categoryNameRGB.getRGB();
    }
    
    public static CNColor getCNColor() {
        return GuiModule.usePreset.isToggled() ? getPresetMode().cnColor : GuiModule.cnColor.getMode();
    }
    
    public static boolean rainbowNotification() {
        return GuiModule.rainbowNotification.isToggled();
    }
    
    public static boolean notifications() {
        return GuiModule.notifications.isToggled();
    }
    
    public enum Preset
    {
        Vape(true, false, true, true, CNColor.STATIC, new Color(255, 255, 255), new Color(27, 25, 26, 255), new Color(27, 25, 26), new Color(59, 132, 107), new Color(59, 132, 107), new Color(250, 250, 250), new Color(27, 25, 26), new Color(27, 25, 26), new Color(255, 255, 255), new Color(27, 25, 26)), 
        PlusPlus(false, false, true, true, CNColor.STATIC, new Color(255, 255, 255), new Color(176, 103, 255, 153), new Color(176, 103, 255, 153), new Color(0, 0, 0), new Color(0, 0, 0), new Color(255, 0, 194), new Color(0, 0, 0), new Color(0, 0, 0), new Color(255, 255, 255), new Color(173, 0, 233));
        
        public boolean showGradientEnabled;
        public boolean showGradientDisabled;
        public boolean useCustomFont;
        public boolean categoryBackground;
        public int backgroundOpacity;
        public Color categoryNameRGB;
        public Color settingBackgroundRGB;
        public Color categoryBackgroundRGB;
        public Color enabledTopRGB;
        public Color enabledBottomRGB;
        public Color enabledTextRGB;
        public Color disabledTopRGB;
        public Color disabledBottomRGB;
        public Color disabledTextRGB;
        public Color backgroundRGB;
        public CNColor cnColor;
        
        private Preset(final boolean showGradientEnabled, final boolean showGradientDisabled, final boolean useCustomFont, final boolean categoryBackground, final CNColor cnColor, final Color categoryNameRGB, final Color settingBackgroundRGB, final Color categoryBackgroundRGB, final Color enabledTopRGB, final Color enabledBottomRGB, final Color enabledTextRGB, final Color disabledTopRGB, final Color disabledBottomRGB, final Color disabledTextRGB, final Color backgroundRGB) {
            this.showGradientEnabled = showGradientEnabled;
            this.showGradientDisabled = showGradientDisabled;
            this.useCustomFont = useCustomFont;
            this.categoryBackground = categoryBackground;
            this.backgroundOpacity = this.backgroundOpacity;
            this.categoryNameRGB = categoryNameRGB;
            this.settingBackgroundRGB = settingBackgroundRGB;
            this.categoryBackgroundRGB = categoryBackgroundRGB;
            this.enabledTopRGB = enabledTopRGB;
            this.enabledBottomRGB = enabledBottomRGB;
            this.enabledTextRGB = enabledTextRGB;
            this.disabledTopRGB = disabledTopRGB;
            this.disabledBottomRGB = disabledBottomRGB;
            this.disabledTextRGB = disabledTextRGB;
            this.backgroundRGB = backgroundRGB;
            this.cnColor = cnColor;
        }
    }
    
    public enum CNColor
    {
        RAINBOW, 
        STATIC;
    }
}
