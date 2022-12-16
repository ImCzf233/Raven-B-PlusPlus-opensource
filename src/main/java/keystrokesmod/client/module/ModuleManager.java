package keystrokesmod.client.module;

import keystrokesmod.client.module.modules.*;
import keystrokesmod.client.module.modules.movement.*;
import keystrokesmod.client.module.modules.hotkey.*;
import keystrokesmod.client.module.modules.minigames.*;
import keystrokesmod.client.module.modules.client.*;
import keystrokesmod.client.module.modules.config.*;
import keystrokesmod.client.module.modules.minigames.Sumo.*;
import keystrokesmod.client.module.modules.player.*;
import keystrokesmod.client.module.modules.other.*;
import keystrokesmod.client.module.modules.world.*;
import keystrokesmod.client.module.modules.render.*;
import keystrokesmod.client.module.modules.combat.*;
import java.util.stream.*;
import java.util.*;
import keystrokesmod.client.main.*;
import net.minecraft.client.gui.*;

public class ModuleManager
{
    private Map<Class<? extends Module>, Module> modules;
    public static boolean initialized;
    public GuiModuleManager guiModuleManager;
    
    public ModuleManager() {
        this.modules = new HashMap<Class<? extends Module>, Module>();
        System.out.println(Module.ModuleCategory.values());
        if (ModuleManager.initialized) {
            return;
        }
        this.guiModuleManager = new GuiModuleManager();
        this.addModule(new ChestStealer());
        this.addModule(new AutoArmour());
        this.addModule(new LeftClicker());
        this.addModule(new RightClicker());
        this.addModule(new AimAssist());
        this.addModule(new ClickAssist());
        this.addModule(new DelayRemover());
        this.addModule(new HitBox());
        this.addModule(new Reach());
        this.addModule(new Velocity());
        this.addModule(new Boost());
        this.addModule(new Fly());
        this.addModule(new InvMove());
        this.addModule(new KeepSprint());
        this.addModule(new NoSlow());
        this.addModule(new Sprint());
        this.addModule(new StopMotion());
        this.addModule(new LegitSpeed());
        this.addModule(new Timer());
        this.addModule(new VClip());
        this.addModule(new AutoJump());
        this.addModule(new AutoPlace());
        this.addModule(new BedAura());
        this.addModule(new FallSpeed());
        this.addModule(new FastPlace());
        this.addModule(new Freecam());
        this.addModule(new NoFall());
        this.addModule(new SafeWalk());
        this.addModule(new AntiBot());
        this.addModule(new AntiShuffle());
        this.addModule(new Chams());
        this.addModule(new ChestESP());
        this.addModule(new Nametags());
        this.addModule(new PlayerESP());
        this.addModule(new Tracers());
        this.addModule(new HUD());
        this.addModule(new BridgeInfo());
        this.addModule(new DuelsStats());
        this.addModule(new MurderMystery());
        this.addModule(new SumoFences());
        this.addModule(new SlyPort());
        this.addModule(new FakeChat());
        this.addModule(new NameHider());
        this.addModule(new WaterBucket());
        this.addModule(new Terminal());
        this.addModule(new GuiModule());
        this.addModule(new SelfDestruct());
        this.addModule(new ChatLogger());
        this.addModule(new BridgeAssist());
        this.addModule(new Fullbright());
        this.addModule(new UpdateCheck());
        this.addModule(new AutoHeader());
        this.addModule(new AutoTool());
        this.addModule(new Blocks());
        this.addModule(new Ladders());
        this.addModule(new Weapon());
        this.addModule(new Pearl());
        this.addModule(new Armour());
        this.addModule(new Healing());
        this.addModule(new Trajectories());
        this.addModule(new WTap());
        this.addModule(new BlockHit());
        this.addModule(new STap());
        this.addModule(new AutoWeapon());
        this.addModule(new BedwarsOverlay());
        this.addModule(new ShiftTap());
        this.addModule(new FPSSpoofer());
        this.addModule(new AutoBlock());
        this.addModule(new MiddleClick());
        this.addModule(new Projectiles());
        this.addModule(new FakeHud());
        this.addModule(new ConfigSettings());
        this.addModule(new SumoBot());
        this.addModule(new SumoClicker());
        this.addModule(new Parkour());
        this.addModule(new Disabler());
        this.addModule(new JumpReset());
        this.addModule(new KvAura());
        this.addModule(new Spin());
        this.addModule(new Scaffold());
        this.addModule(new XRay());
        this.addModule(new Aura());
        ModuleManager.initialized = true;
    }
    
    public void addModule(final Module m) {
        this.modules.put(m.getClass(), m);
    }
    
    public void removeModuleByName(final String s) {
        final Module m = this.getModuleByName(s);
        this.modules.remove(m.getClass());
        m.component.category.r3nd3r();
    }
    
    public Module getModuleByName(final String name) {
        if (!ModuleManager.initialized) {
            return null;
        }
        for (final Module module : this.modules.values()) {
            if (module.getName().replaceAll(" ", "").equalsIgnoreCase(name) || module.getName().equalsIgnoreCase(name)) {
                return module;
            }
        }
        return null;
    }
    
    public Module getModuleByClazz(final Class<? extends Module> c) {
        if (!ModuleManager.initialized) {
            return null;
        }
        return this.modules.get(c);
    }
    
    public List<Module> getEnabledModules() {
        return this.modules.values().stream().filter(Module::isEnabled).collect((Collector<? super Module, ?, List<Module>>)Collectors.toList());
    }
    
    public List<Module> getModules() {
        final ArrayList<Module> allModules = new ArrayList<Module>(this.modules.values());
        try {
            allModules.addAll(Raven.configManager.configModuleManager.getConfigModules());
        }
        catch (NullPointerException ex) {}
        try {
            allModules.addAll(this.guiModuleManager.getModules());
        }
        catch (NullPointerException ex2) {}
        return allModules;
    }
    
    public List<Module> getConfigModules() {
        final List<Module> modulesOfC = new ArrayList<Module>();
        for (final Module mod : this.getModules()) {
            if (!mod.isClientConfig()) {
                modulesOfC.add(mod);
            }
        }
        return modulesOfC;
    }
    
    public List<Module> getClientConfigModules() {
        final List<Module> modulesOfCC = new ArrayList<Module>();
        for (final Module mod : this.getModules()) {
            if (mod.isClientConfig()) {
                modulesOfCC.add(mod);
            }
        }
        return modulesOfCC;
    }
    
    public List<Module> getModulesInCategory(final Module.ModuleCategory categ) {
        final ArrayList<Module> modulesOfCat = new ArrayList<Module>();
        for (final Module mod : this.getModules()) {
            if (mod.moduleCategory().equals(categ)) {
                modulesOfCat.add(mod);
            }
        }
        return modulesOfCat;
    }
    
    public int numberOfModules() {
        return this.modules.size();
    }
    
    public int getLongestActiveModule(final FontRenderer fr) {
        int length = 0;
        for (final Module mod : this.modules.values()) {
            if (mod.isEnabled() && fr.func_78256_a(mod.getName()) > length) {
                length = fr.func_78256_a(mod.getName());
            }
        }
        return length;
    }
    
    public int getBoxHeight(final FontRenderer fr, final int margin) {
        int length = 0;
        for (final Module mod : this.modules.values()) {
            if (mod.isEnabled()) {
                length += fr.field_78288_b + margin;
            }
        }
        return length;
    }
}
