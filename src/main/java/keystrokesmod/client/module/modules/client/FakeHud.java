package keystrokesmod.client.module.modules.client;

import keystrokesmod.client.module.*;
import keystrokesmod.client.module.setting.impl.*;
import keystrokesmod.client.module.setting.*;
import com.google.gson.*;
import keystrokesmod.client.main.*;
import keystrokesmod.client.utils.*;
import java.util.*;

public class FakeHud extends Module
{
    public DescriptionSetting description;
    public static List<Module> list;
    
    public FakeHud() {
        super("Fake Hud", ModuleCategory.client);
        this.enableAll();
        this.registerSetting(this.description = new DescriptionSetting("Command: fakehud add/remove <name>"));
    }
    
    @Override
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
        data.addProperty("keycode", (Number)this.keycode);
        data.add("settings", (JsonElement)settings);
        final StringBuilder str = new StringBuilder();
        for (final Module m : FakeHud.list) {
            str.append(m.getName()).append(",");
        }
        data.addProperty("list", str.toString());
        return data;
    }
    
    @Override
    public void applyConfigFromJson(final JsonObject data) {
        try {
            this.keycode = data.get("keycode").getAsInt();
            this.setToggled(data.get("enabled").getAsBoolean());
            final JsonObject settingsData = data.get("settings").getAsJsonObject();
            for (final Setting setting : this.getSettings()) {
                if (settingsData.has(setting.getName())) {
                    setting.applyConfigFromJson(settingsData.get(setting.getName()).getAsJsonObject());
                }
            }
        }
        catch (NullPointerException ex) {}
        try {
            final String str = data.get("list").getAsString();
            final String[] split;
            final String[] strList = split = str.split(",");
            for (final String s : split) {
                addModule(s);
            }
        }
        catch (NullPointerException e) {
            FakeHud.list = new ArrayList<Module>() {
                {
                    this.add(new Module("AntiFbi", ModuleCategory.client));
                    this.add(new Module("AutoBackdoor", ModuleCategory.client));
                    this.add(new Module("DupeMacro", ModuleCategory.client));
                    this.add(new Module("GroomAura", ModuleCategory.client));
                    this.add(new Module("Nostaff", ModuleCategory.client));
                    this.add(new Module("SimonDisabler", ModuleCategory.client));
                    this.add(new Module("Forceop", ModuleCategory.client));
                    this.add(new Module("Twerk", ModuleCategory.client));
                }
            };
        }
    }
    
    @Override
    public void onEnable() {
    }
    
    public static List<Module> getModules() {
        int i = 0;
        final List<Module> enabledList = new ArrayList<Module>();
        for (final Module module : Raven.moduleManager.getModules()) {
            if (module.isEnabled() && FakeHud.list.size() > i) {
                enabledList.add(FakeHud.list.get(i));
                ++i;
            }
        }
        return enabledList;
    }
    
    public static void addModule(final String str) {
        for (final Module module : FakeHud.list) {
            if (module.getName().equals(str)) {
                return;
            }
        }
        final Module m = new Module(str, ModuleCategory.client);
        m.enable();
        FakeHud.list.add(m);
    }
    
    public static void removeModule(final String str) {
        for (final Module module : FakeHud.list) {
            if (module.getName().equals(str)) {
                FakeHud.list.remove(module);
            }
        }
    }
    
    public void enableAll() {
        for (final Module module : FakeHud.list) {
            module.enable();
        }
    }
    
    public static void sortLongShort() {
        FakeHud.list.sort(Comparator.comparingInt(o2 -> Utils.mc.field_71466_p.func_78256_a(o2.getName())));
    }
    
    public static void sortShortLong() {
        FakeHud.list.sort((o1, o2) -> Utils.mc.field_71466_p.func_78256_a(o2.getName()) - Utils.mc.field_71466_p.func_78256_a(o1.getName()));
    }
    
    static {
        FakeHud.list = new ArrayList<Module>();
    }
}
