package keystrokesmod.client.main;

import keystrokesmod.client.utils.version.*;
import keystrokesmod.client.command.*;
import keystrokesmod.client.config.*;
import keystrokesmod.client.module.*;
import keystrokesmod.client.clickgui.raven.*;
import keystrokesmod.client.clickgui.kv.*;
import net.minecraft.util.*;
import com.google.common.eventbus.*;
import net.minecraft.client.*;
import net.minecraftforge.common.*;
import keystrokesmod.client.event.forge.*;
import keystrokesmod.client.notifications.*;
import keystrokesmod.client.utils.font.*;
import keystrokesmod.client.module.modules.*;
import javax.imageio.*;
import net.minecraft.client.renderer.texture.*;
import java.io.*;
import java.awt.image.*;
import net.minecraftforge.client.event.*;
import keystrokesmod.client.utils.*;
import net.minecraftforge.fml.common.eventhandler.*;
import java.util.concurrent.*;

public class Raven
{
    public static boolean debugger;
    public static final VersionManager versionManager;
    public static CommandManager commandManager;
    public static final String sourceLocation = "https://github.com/K-ov/Raven-bPLUS";
    public static final String downloadLocation = "https://github.com/K-ov/Raven-bPLUS/raw/stable/build/libs/%5B1.8.9%5D%20BetterKeystrokes%20V-1.2.jar";
    public static final String discord = "https://discord.gg/UqJ8ngteud";
    public static String[] updateText;
    public static ConfigManager configManager;
    public static ClientConfig clientConfig;
    public static final ModuleManager moduleManager;
    public static ClickGui clickGui;
    public static KvCompactGui kvCompactGui;
    private static final ScheduledExecutorService ex;
    public static ResourceLocation mResourceLocation;
    public static final String osName;
    public static final String osArch;
    public static final EventBus eventBus;
    public static final Minecraft mc;
    
    public static void init() {
        MinecraftForge.EVENT_BUS.register((Object)new Raven());
        MinecraftForge.EVENT_BUS.register((Object)new DebugInfoRenderer());
        MinecraftForge.EVENT_BUS.register((Object)new MouseManager());
        MinecraftForge.EVENT_BUS.register((Object)new PingChecker());
        MinecraftForge.EVENT_BUS.register((Object)new ForgeEventListener());
        Raven.eventBus.register((Object)NotificationRenderer.notificationRenderer);
        FontUtil.bootstrap();
        Runtime.getRuntime().addShutdownHook(new Thread(Raven.ex::shutdown));
        final InputStream ravenLogoInputStream = HUD.class.getResourceAsStream("/assets/keystrokes/raven.png");
        try {
            assert ravenLogoInputStream != null;
            final BufferedImage bf = ImageIO.read(ravenLogoInputStream);
            Raven.mResourceLocation = Minecraft.func_71410_x().field_71446_o.func_110578_a("raven", new DynamicTexture(bf));
        }
        catch (IOException | IllegalArgumentException | NullPointerException ex2) {
            final Exception ex;
            final Exception noway = ex;
            noway.printStackTrace();
            Raven.mResourceLocation = null;
        }
        Raven.commandManager = new CommandManager();
        Raven.clickGui = new ClickGui();
        Raven.kvCompactGui = new KvCompactGui();
        Raven.configManager = new ConfigManager();
        (Raven.clientConfig = new ClientConfig()).applyConfig();
        Raven.ex.execute(() -> {
            try {
                LaunchTracker.registerLaunch();
            }
            catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
    
    @SubscribeEvent
    public void onChatMessageReceived(final ClientChatReceivedEvent event) {
        if (Utils.Player.isPlayerInGame()) {
            final String msg = event.message.func_150260_c();
            if (msg.startsWith("Your new API key is")) {
                Utils.URLS.hypixelApiKey = msg.replace("Your new API key is ", "");
                Utils.Player.sendMessageToSelf("&aSet api key to " + Utils.URLS.hypixelApiKey + "!");
                Raven.clientConfig.saveConfig();
            }
        }
    }
    
    public static ScheduledExecutorService getExecutor() {
        return Raven.ex;
    }
    
    static {
        versionManager = new VersionManager();
        Raven.updateText = new String[] { "Your version of Raven B++ (" + Raven.versionManager.getClientVersion().toString() + ") is outdated!", "Enter the command update into client CommandLine to open the download page", "or just enable the update module to get a message in chat.", "", "Newest version: " + Raven.versionManager.getLatestVersion().toString() };
        moduleManager = new ModuleManager();
        ex = Executors.newScheduledThreadPool(2);
        eventBus = new EventBus();
        mc = Minecraft.func_71410_x();
        osName = System.getProperty("os.name").toLowerCase();
        osArch = System.getProperty("os.arch").toLowerCase();
    }
}
