package keystrokesmod.client.module.modules.client;

import keystrokesmod.client.module.*;
import net.minecraft.client.gui.*;
import keystrokesmod.client.main.*;
import net.minecraftforge.common.*;
import keystrokesmod.keystroke.*;
import keystrokesmod.client.utils.*;
import java.util.*;

public class SelfDestruct extends Module
{
    public SelfDestruct() {
        super("Self Destruct", ModuleCategory.client);
    }
    
    @Override
    public void onEnable() {
        this.disable();
        SelfDestruct.mc.func_147108_a((GuiScreen)null);
        for (final Module module : Raven.moduleManager.getModules()) {
            if (module != this && module.isEnabled()) {
                module.disable();
            }
        }
        MinecraftForge.EVENT_BUS.unregister((Object)new Raven());
        MinecraftForge.EVENT_BUS.unregister((Object)new DebugInfoRenderer());
        MinecraftForge.EVENT_BUS.unregister((Object)new MouseManager());
        MinecraftForge.EVENT_BUS.unregister((Object)new KeyStrokeRenderer());
        MinecraftForge.EVENT_BUS.unregister((Object)new PingChecker());
    }
}
