package keystrokesmod.client.module.modules.client;

import keystrokesmod.client.module.*;
import keystrokesmod.client.module.setting.impl.*;
import keystrokesmod.client.module.setting.*;
import keystrokesmod.client.main.*;
import keystrokesmod.client.event.impl.*;
import keystrokesmod.client.utils.*;
import keystrokesmod.client.clickgui.raven.*;
import com.google.common.eventbus.*;
import com.google.gson.*;
import java.util.*;

public class Terminal extends Module
{
    public static boolean b;
    public static Timer animation;
    public static SliderSetting opacity;
    
    public Terminal() {
        super("Terminal", ModuleCategory.client);
        this.withEnabled(true);
        this.registerSetting(Terminal.opacity = new SliderSetting("Terminal background opacity", 100.0, 0.0, 255.0, 1.0));
    }
    
    @Override
    public void onEnable() {
        Raven.clickGui.terminal.show();
        (Terminal.animation = new Timer(500.0f)).start();
    }
    
    @Subscribe
    public void onGameLoop(final GameLoopEvent e) {
        if (Utils.Player.isPlayerInGame() && Terminal.mc.field_71462_r instanceof ClickGui && Raven.clickGui.terminal.hidden()) {
            Raven.clickGui.terminal.show();
        }
    }
    
    @Override
    public void onDisable() {
        Raven.clickGui.terminal.hide();
        if (Terminal.animation != null) {
            Terminal.animation.start();
        }
    }
    
    @Override
    public void applyConfigFromJson(final JsonObject data) {
        try {
            this.keycode = data.get("keycode").getAsInt();
            final JsonObject settingsData = data.get("settings").getAsJsonObject();
            for (final Setting setting : this.getSettings()) {
                if (settingsData.has(setting.getName())) {
                    setting.applyConfigFromJson(settingsData.get(setting.getName()).getAsJsonObject());
                }
            }
        }
        catch (NullPointerException ex) {}
    }
    
    @Override
    public void resetToDefaults() {
        this.keycode = this.defualtKeyCode;
        for (final Setting setting : this.settings) {
            setting.resetToDefaults();
        }
    }
}
