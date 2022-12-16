package keystrokesmod.client.module;

import keystrokesmod.client.module.setting.*;
import keystrokesmod.client.clickgui.raven.components.*;
import net.minecraft.client.*;
import com.google.gson.*;
import org.lwjgl.input.*;
import keystrokesmod.client.main.*;
import keystrokesmod.client.notifications.*;
import keystrokesmod.client.module.setting.impl.*;
import keystrokesmod.client.clickgui.raven.*;
import java.util.*;

public class Module
{
    protected ArrayList<Setting> settings;
    private final String moduleName;
    private final ModuleCategory moduleCategory;
    protected boolean hasBind;
    protected boolean showInHud;
    protected boolean clientConfig;
    protected boolean enabled;
    protected boolean defaultEnabled;
    protected int keycode;
    protected int defualtKeyCode;
    protected ModuleComponent component;
    protected static Minecraft mc;
    private boolean isToggled;
    private String description;
    private boolean registered;
    
    public void guiUpdate() {
    }
    
    public Module(final String name, final ModuleCategory moduleCategory) {
        this.hasBind = true;
        this.showInHud = true;
        this.defaultEnabled = this.enabled;
        this.defualtKeyCode = this.keycode;
        this.description = "";
        this.moduleName = name;
        this.moduleCategory = moduleCategory;
        this.settings = new ArrayList<Setting>();
        Module.mc = Minecraft.func_71410_x();
    }
    
    protected <E extends Module> E withKeycode(final int i) {
        this.keycode = i;
        this.defualtKeyCode = i;
        return (E)this;
    }
    
    protected <E extends Module> E withEnabled(final boolean i) {
        this.enabled = i;
        this.defaultEnabled = i;
        try {
            this.setToggled(i);
        }
        catch (Exception ex) {}
        return (E)this;
    }
    
    public <E extends Module> E withDescription(final String i) {
        this.description = i;
        return (E)this;
    }
    
    public JsonObject getConfigAsJson() {
        final JsonObject settings = new JsonObject();
        for (final Setting setting : this.settings) {
            if (setting != null) {
                final JsonObject settingData = setting.getConfigAsJson();
                settings.add(setting.settingName, (JsonElement)settingData);
            }
        }
        final JsonObject data = new JsonObject();
        data.addProperty("enabled", Boolean.valueOf(this.enabled));
        if (this.hasBind) {
            data.addProperty("keycode", (Number)this.keycode);
        }
        data.addProperty("showInHud", Boolean.valueOf(this.showInHud));
        data.add("settings", (JsonElement)settings);
        return data;
    }
    
    public void applyConfigFromJson(final JsonObject data) {
        try {
            if (this.hasBind) {
                this.keycode = data.get("keycode").getAsInt();
            }
            this.setToggled(data.get("enabled").getAsBoolean());
            final JsonObject settingsData = data.get("settings").getAsJsonObject();
            for (final Setting setting : this.getSettings()) {
                if (settingsData.has(setting.getName())) {
                    setting.applyConfigFromJson(settingsData.get(setting.getName()).getAsJsonObject());
                }
            }
            this.showInHud = data.get("showInHud").getAsBoolean();
        }
        catch (NullPointerException ex) {}
        this.postApplyConfig();
    }
    
    public void postApplyConfig() {
    }
    
    public void keybind() {
        if (this.keycode != 0 && this.canBeEnabled()) {
            if (!this.isToggled && Keyboard.isKeyDown(this.keycode)) {
                this.toggle();
                this.isToggled = true;
            }
            else if (!Keyboard.isKeyDown(this.keycode)) {
                this.isToggled = false;
            }
        }
    }
    
    public boolean canBeEnabled() {
        return true;
    }
    
    public boolean showInHud() {
        return this.showInHud;
    }
    
    public void enable() {
        this.enabled = true;
        this.onEnable();
        if (this.enabled && !this.registered) {
            Raven.eventBus.register((Object)this);
            this.registered = true;
        }
        NotificationRenderer.moduleStateChanged(this);
    }
    
    public void disable() {
        this.enabled = false;
        if (this.registered) {
            Raven.eventBus.unregister((Object)this);
            this.registered = false;
        }
        this.onDisable();
        NotificationRenderer.moduleStateChanged(this);
    }
    
    public void setToggled(final boolean enabled) {
        if (enabled) {
            this.enable();
        }
        else {
            this.disable();
        }
    }
    
    public boolean isBindable() {
        return this.hasBind;
    }
    
    public String getName() {
        return this.moduleName;
    }
    
    public ArrayList<Setting> getSettings() {
        return this.settings;
    }
    
    public Setting getSettingByName(final String name) {
        for (final Setting setting : this.settings) {
            if (setting.getName().equalsIgnoreCase(name)) {
                return setting;
            }
        }
        return null;
    }
    
    public void registerSetting(final Setting setting) {
        this.settings.add(setting);
    }
    
    public void registerSettings(final Setting... settings) {
        Collections.addAll(this.settings, settings);
    }
    
    public void setVisableInHud(final boolean vis) {
        this.showInHud = vis;
    }
    
    public ModuleCategory moduleCategory() {
        return this.moduleCategory;
    }
    
    public boolean isEnabled() {
        return this.enabled;
    }
    
    public void onEnable() {
    }
    
    public void onDisable() {
    }
    
    public void toggle() {
        if (this.enabled) {
            this.disable();
        }
        else {
            this.enable();
        }
    }
    
    public void guiButtonToggled(final TickSetting b) {
    }
    
    public void guiButtonToggled(final ComboSetting b) {
    }
    
    public void guiButtonToggled(final TickSetting b, final Component c) {
    }
    
    public int getKeycode() {
        return this.keycode;
    }
    
    public void setbind(final int keybind) {
        this.keycode = keybind;
    }
    
    public void resetToDefaults() {
        this.keycode = this.defualtKeyCode;
        this.setToggled(this.defaultEnabled);
        for (final Setting setting : this.settings) {
            setting.resetToDefaults();
        }
    }
    
    public void setModuleComponent(final ModuleComponent component) {
        this.component = component;
    }
    
    public void onGuiClose() {
    }
    
    public String getBindAsString() {
        return (this.keycode == 0) ? "None" : Keyboard.getKeyName(this.keycode);
    }
    
    public void clearBinds() {
        this.keycode = 0;
    }
    
    public boolean isClientConfig() {
        return this.clientConfig;
    }
    
    public enum ModuleCategory
    {
        category(true, (ModuleCategory)null, "Raven B++"), 
        combat(false, ModuleCategory.category, "Combat"), 
        movement(false, ModuleCategory.category, "Movement"), 
        player(false, ModuleCategory.category, "Player"), 
        world(false, ModuleCategory.category, "World"), 
        render(false, ModuleCategory.category, "Render"), 
        minigames(false, ModuleCategory.category, "Minigames"), 
        other(false, ModuleCategory.category, "Other"), 
        client(false, ModuleCategory.category, "Client"), 
        hotkey(false, ModuleCategory.category, "Hotkey"), 
        config(false, ModuleCategory.client, "Config"), 
        sumo(false, ModuleCategory.minigames, "Sumo");
        
        private final boolean defaultShown;
        private final ModuleCategory topCategory;
        private final String name;
        private List<ModuleCategory> childCategories;
        
        private ModuleCategory(final boolean defaultShown, final ModuleCategory topCategory, final String name) {
            this.childCategories = new ArrayList<ModuleCategory>();
            if (topCategory != null) {
                topCategory.addChildCategory(this);
            }
            this.defaultShown = defaultShown;
            this.topCategory = topCategory;
            this.name = name;
        }
        
        public void addChildCategory(final ModuleCategory moduleCategory) {
            this.childCategories.add(moduleCategory);
        }
        
        public List<ModuleCategory> getChildCategories() {
            return this.childCategories;
        }
        
        public String getName() {
            return this.name;
        }
        
        public boolean isShownByDefault() {
            return this.defaultShown;
        }
        
        public ModuleCategory getParentCategory() {
            return this.topCategory;
        }
    }
}
