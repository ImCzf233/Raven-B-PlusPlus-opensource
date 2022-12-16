package keystrokesmod.client.main;

import net.minecraft.client.*;
import keystrokesmod.client.utils.*;
import keystrokesmod.keystroke.*;
import com.google.gson.*;
import keystrokesmod.client.clickgui.raven.components.*;
import keystrokesmod.client.module.*;
import java.util.*;
import java.io.*;

public class ClientConfig
{
    private static final Minecraft mc;
    public static boolean applyingConfig;
    private final File cfgFile;
    private final String fileName = "clientconfig.kv";
    private JsonObject config;
    
    public ClientConfig() {
        final File cfgDir = new File(Minecraft.func_71410_x().field_71412_D + File.separator + "keystrokes");
        if (!cfgDir.exists()) {
            cfgDir.mkdir();
        }
        this.cfgFile = new File(cfgDir, "clientconfig.kv");
        if (!this.cfgFile.exists()) {
            try {
                this.cfgFile.createNewFile();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        else {
            final JsonParser jsonParser = new JsonParser();
            try (final FileReader reader = new FileReader(this.cfgFile)) {
                final Object obj = jsonParser.parse((Reader)reader);
                this.config = (JsonObject)obj;
            }
            catch (JsonSyntaxException | ClassCastException | IOException ex2) {
                final Exception ex;
                final Exception e2 = ex;
                e2.printStackTrace();
            }
        }
    }
    
    public void applyConfig() {
        ClientConfig.applyingConfig = true;
        try {
            Utils.URLS.hypixelApiKey = this.config.get("apikey").getAsString();
            Utils.URLS.pasteApiKey = this.config.get("pastekey").getAsString();
            this.loadClickGuiCoords(this.config.get("clickgui").getAsJsonObject().get("catPos").getAsJsonObject());
            Raven.configManager.loadConfigByName(this.config.get("currentconfig").getAsString());
            this.loadHudCoords(this.config.get("hud").getAsJsonObject());
            this.loadTerminalCoords(this.config.get("clickgui").getAsJsonObject());
            this.loadModules(this.config.get("modules").getAsJsonObject());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        ClientConfig.applyingConfig = false;
    }
    
    public void applyKeyStrokeSettingsFromConfigFile() {
        try {
            final JsonObject data = this.config.get("keystrokes").getAsJsonObject();
            KeyStroke.x = data.get("x").getAsInt();
            KeyStroke.y = data.get("y").getAsInt();
            KeyStroke.enabled = data.get("enabled").getAsBoolean();
            KeyStroke.showMouseButtons = data.get("mbEnabled").getAsBoolean();
            KeyStroke.currentColorNumber = data.get("color").getAsInt();
            KeyStroke.outline = data.get("outline").getAsBoolean();
        }
        catch (Throwable var4) {
            var4.printStackTrace();
        }
    }
    
    private JsonObject getClickGuiAsJson() {
        final JsonObject data = new JsonObject();
        data.add("catPos", (JsonElement)this.getClickGuiPosAsJson());
        data.addProperty("terminalX", (Number)Raven.clickGui.terminal.getX());
        data.addProperty("terminalY", (Number)Raven.clickGui.terminal.getY());
        data.addProperty("width", (Number)Raven.clickGui.terminal.getWidth());
        data.addProperty("height", (Number)Raven.clickGui.terminal.getHeight());
        data.addProperty("hidden", Boolean.valueOf(Raven.clickGui.terminal.hidden));
        data.addProperty("opened", Boolean.valueOf(Raven.clickGui.terminal.opened));
        return data;
    }
    
    public JsonObject getClickGuiPosAsJson() {
        final JsonObject data = new JsonObject();
        for (final CategoryComponent cat : Raven.clickGui.getCategoryList()) {
            final JsonObject catData = new JsonObject();
            catData.addProperty("X", (Number)cat.getX());
            catData.addProperty("Y", (Number)cat.getY());
            catData.addProperty("visable", Boolean.valueOf(cat.isVisable()));
            catData.addProperty("opened", Boolean.valueOf(cat.isOpened()));
            data.add(cat.categoryName.name(), (JsonElement)catData);
        }
        return data;
    }
    
    public JsonObject getConfigAsJson() {
        final JsonObject data = new JsonObject();
        data.addProperty("apikey", Utils.URLS.hypixelApiKey);
        data.addProperty("pastekey", Utils.URLS.pasteApiKey);
        data.addProperty("currentconfig", Raven.configManager.getConfig().getName());
        data.add("keystrokes", (JsonElement)this.getKeystrokeAsJson());
        data.add("hud", (JsonElement)this.getHudAsJson());
        data.add("clickgui", (JsonElement)this.getClickGuiAsJson());
        data.add("modules", (JsonElement)this.getModulesAsJson());
        return data;
    }
    
    private JsonObject getHudAsJson() {
        final JsonObject data = new JsonObject();
        return data;
    }
    
    private JsonObject getKeystrokeAsJson() {
        final JsonObject data = new JsonObject();
        data.addProperty("x", (Number)KeyStroke.x);
        data.addProperty("y", (Number)KeyStroke.y);
        data.addProperty("enabled", Boolean.valueOf(KeyStroke.enabled));
        data.addProperty("mbEnabled", Boolean.valueOf(KeyStroke.showMouseButtons));
        data.addProperty("color", (Number)KeyStroke.currentColorNumber);
        data.addProperty("outline", Boolean.valueOf(KeyStroke.outline));
        return data;
    }
    
    private JsonObject getModulesAsJson() {
        final JsonObject data = new JsonObject();
        for (final Module m : Raven.moduleManager.getClientConfigModules()) {
            if (!(m instanceof GuiModule)) {
                data.add(m.getName(), (JsonElement)m.getConfigAsJson());
            }
        }
        return data;
    }
    
    private void loadClickGuiCoords(final JsonObject data) {
        for (final CategoryComponent cat : Raven.clickGui.getCategoryList()) {
            final JsonObject catData = data.get(cat.categoryName.name()).getAsJsonObject();
            cat.setX(catData.get("X").getAsInt());
            cat.setY(catData.get("Y").getAsInt());
            cat.setOpened(catData.get("opened").getAsBoolean());
            if (cat.categoryName != Module.ModuleCategory.category) {
                final boolean visable = cat.categoryName == Module.ModuleCategory.category || catData.get("visable").getAsBoolean();
                cat.setVisable(visable);
                Raven.moduleManager.guiModuleManager.getModuleByModuleCategory(cat.categoryName).setToggled(visable);
            }
        }
    }
    
    private void loadHudCoords(final JsonObject data) {
    }
    
    private void loadModules(final JsonObject data) {
        final List<Module> knownModules = new ArrayList<Module>(Raven.moduleManager.getClientConfigModules());
        for (final Module module : knownModules) {
            if (data.has(module.getName())) {
                module.applyConfigFromJson(data.get(module.getName()).getAsJsonObject());
            }
            else {
                module.resetToDefaults();
            }
        }
    }
    
    private void loadTerminalCoords(final JsonObject data) {
        Raven.clickGui.terminal.setLocation(data.get("terminalX").getAsInt(), data.get("terminalY").getAsInt());
        Raven.clickGui.terminal.setSize(data.get("width").getAsInt(), data.get("height").getAsInt());
        Raven.clickGui.terminal.opened = data.get("opened").getAsBoolean();
        Raven.clickGui.terminal.hidden = data.get("hidden").getAsBoolean();
    }
    
    public void saveConfig() {
        if (ClientConfig.applyingConfig) {
            return;
        }
        this.config = this.getConfigAsJson();
        try (final PrintWriter out = new PrintWriter(new FileWriter(this.cfgFile))) {
            out.write(this.config.toString());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void updateKeyStrokesSettings() {
        this.config.add("keystrokes", (JsonElement)this.getKeystrokeAsJson());
        this.saveConfig();
    }
    
    static {
        mc = Minecraft.func_71410_x();
    }
}
