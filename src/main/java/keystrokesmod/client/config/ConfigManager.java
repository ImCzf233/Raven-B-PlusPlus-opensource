package keystrokesmod.client.config;

import keystrokesmod.client.module.modules.config.*;
import net.minecraft.client.*;
import java.io.*;
import keystrokesmod.client.main.*;
import keystrokesmod.client.module.*;
import com.google.gson.*;
import java.util.*;

public class ConfigManager
{
    public final ConfigModuleManager configModuleManager;
    public static boolean applyingConfig;
    private final File configDirectory;
    private Config config;
    private final ArrayList<Config> configs;
    
    public ConfigManager() {
        this.configDirectory = new File(Minecraft.func_71410_x().field_71412_D + File.separator + "keystrokes" + File.separator + "configs");
        this.configs = new ArrayList<Config>();
        if (!this.configDirectory.isDirectory()) {
            this.configDirectory.mkdirs();
        }
        final File defaultFile = new File(this.configDirectory, "default.bplus");
        this.config = new Config(defaultFile);
        this.discoverConfigs();
        this.configModuleManager = new ConfigModuleManager();
        if (!defaultFile.exists()) {
            this.save();
        }
    }
    
    public static boolean isOutdated(final File file) {
        final JsonParser jsonParser = new JsonParser();
        try (final FileReader reader = new FileReader(file)) {
            final Object obj = jsonParser.parse((Reader)reader);
            final JsonObject data = (JsonObject)obj;
            return false;
        }
        catch (JsonSyntaxException | ClassCastException | IOException ex2) {
            final Exception ex;
            final Exception e = ex;
            e.printStackTrace();
            return true;
        }
    }
    
    public void discoverConfigs() {
        this.configs.clear();
        if (this.configDirectory.listFiles() == null || Objects.requireNonNull(this.configDirectory.listFiles()).length <= 0) {
            return;
        }
        for (final File file : Objects.requireNonNull(this.configDirectory.listFiles())) {
            if (file.getName().endsWith(".bplus") && !isOutdated(file)) {
                this.configs.add(new Config(new File(file.getPath())));
            }
        }
        if (this.configModuleManager != null) {
            this.configModuleManager.updater(this.configs);
        }
    }
    
    public Config getConfig() {
        return this.config;
    }
    
    public void save() {
        final JsonObject data = new JsonObject();
        data.addProperty("version", Raven.versionManager.getClientVersion().getVersion());
        data.addProperty("author", "Unknown");
        data.addProperty("notes", "");
        data.addProperty("intendedServer", "");
        data.addProperty("usedFor", (Number)0);
        data.addProperty("lastEditTime", (Number)System.currentTimeMillis());
        final JsonObject modules = new JsonObject();
        for (final Module module : Raven.moduleManager.getConfigModules()) {
            modules.add(module.getName(), (JsonElement)module.getConfigAsJson());
        }
        data.add("modules", (JsonElement)modules);
        this.config.save(data);
    }
    
    public void setConfig(final Config config) {
        ConfigManager.applyingConfig = true;
        this.config = config;
        final JsonObject data = config.getData().get("modules").getAsJsonObject();
        final List<Module> knownModules = new ArrayList<Module>(Raven.moduleManager.getConfigModules());
        for (final Module module : knownModules) {
            if (!module.isClientConfig()) {
                if (data.has(module.getName())) {
                    module.applyConfigFromJson(data.get(module.getName()).getAsJsonObject());
                }
                else {
                    module.resetToDefaults();
                }
            }
        }
        ConfigManager.applyingConfig = false;
    }
    
    public void loadConfigByName(final String replace) {
        this.discoverConfigs();
        for (final Config config : this.configs) {
            if (config.getName().equals(replace)) {
                this.setConfig(config);
            }
        }
    }
    
    public ArrayList<Config> getConfigs() {
        this.discoverConfigs();
        return this.configs;
    }
    
    public void copyConfig(final Config config, final String s) {
        final File file = new File(this.configDirectory, s);
        final Config newConfig = new Config(file);
        newConfig.save(config.getData());
    }
    
    public void resetConfig() {
        for (final Module module : Raven.moduleManager.getConfigModules()) {
            if (!module.isClientConfig()) {
                module.resetToDefaults();
            }
        }
        this.save();
    }
    
    public void deleteConfig(final Config config) {
        config.file.delete();
        if (config.getName().equals(this.config.getName())) {
            this.discoverConfigs();
            if (this.configs.size() < 2) {
                this.resetConfig();
                final File defaultFile = new File(this.configDirectory, "default.bplus");
                this.config = new Config(defaultFile);
                this.save();
            }
            else {
                this.config = this.configs.get(0);
            }
            this.save();
        }
    }
}
