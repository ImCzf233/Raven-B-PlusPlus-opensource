package keystrokesmod.keystroke;

import net.minecraftforge.fml.common.*;
import net.minecraftforge.fml.common.event.*;
import net.minecraftforge.client.*;
import net.minecraft.command.*;
import net.minecraftforge.common.*;
import keystrokesmod.client.main.*;
import net.minecraftforge.fml.common.gameevent.*;
import net.minecraft.client.*;
import net.minecraft.client.gui.*;
import net.minecraftforge.fml.common.eventhandler.*;

@Mod(modid = "keystrokesmod", name = "KeystrokesMod", version = "KMV5", acceptedMinecraftVersions = "[1.8.9]", clientSideOnly = true)
public class KeyStrokeMod
{
    private static KeyStroke keyStroke;
    private static final KeyStrokeRenderer keyStrokeRenderer;
    private static boolean isKeyStrokeConfigGuiToggled;
    
    @Mod.EventHandler
    public void init(final FMLInitializationEvent event) {
        ClientCommandHandler.instance.func_71560_a((ICommand)new KeyStrokeCommand());
        MinecraftForge.EVENT_BUS.register((Object)new KeyStrokeRenderer());
        MinecraftForge.EVENT_BUS.register((Object)this);
        Raven.init();
        Raven.clientConfig.applyKeyStrokeSettingsFromConfigFile();
    }
    
    public static KeyStroke getKeyStroke() {
        return KeyStrokeMod.keyStroke;
    }
    
    public static KeyStrokeRenderer getKeyStrokeRenderer() {
        return KeyStrokeMod.keyStrokeRenderer;
    }
    
    public static void toggleKeyStrokeConfigGui() {
        KeyStrokeMod.isKeyStrokeConfigGuiToggled = true;
    }
    
    @SubscribeEvent
    public void onTick(final TickEvent.ClientTickEvent e) {
        if (KeyStrokeMod.isKeyStrokeConfigGuiToggled) {
            KeyStrokeMod.isKeyStrokeConfigGuiToggled = false;
            Minecraft.func_71410_x().func_147108_a((GuiScreen)new KeyStrokeConfigGui());
        }
    }
    
    static {
        keyStrokeRenderer = new KeyStrokeRenderer();
    }
}
