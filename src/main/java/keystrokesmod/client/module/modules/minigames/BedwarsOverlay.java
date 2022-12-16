package keystrokesmod.client.module.modules.minigames;

import keystrokesmod.client.module.*;
import keystrokesmod.client.module.setting.impl.*;
import keystrokesmod.client.module.setting.*;
import net.minecraftforge.client.event.*;
import keystrokesmod.client.utils.*;
import com.google.common.eventbus.*;
import keystrokesmod.client.event.impl.*;
import net.minecraft.client.*;
import net.minecraft.client.network.*;
import java.util.*;
import keystrokesmod.client.main.*;
import com.google.gson.*;
import net.minecraft.client.gui.*;

public class BedwarsOverlay extends Module
{
    public static SliderSetting overlayX;
    public static SliderSetting overlayY;
    public static SliderSetting margin;
    public static SliderSetting marginTextY;
    public static SliderSetting marginTextX;
    public static DescriptionSetting bombiesMomento;
    public static boolean active;
    public static boolean reset;
    public static double overlayWidth;
    public static double overlayHeight;
    public static double textY;
    public static int mainTextColour;
    public static int backgroundColour;
    public static int linesDrawn;
    public static int errorColour;
    public static HashMap<String, int[]> playerStats;
    public static HashMap<StatType, Integer> statStart;
    
    public BedwarsOverlay() {
        super("Bedwars Overlay", ModuleCategory.minigames);
        this.registerSetting(BedwarsOverlay.bombiesMomento = new DescriptionSetting("B0MBIES moment"));
        BedwarsOverlay.overlayHeight = 170.0;
        BedwarsOverlay.overlayWidth = 300.0;
        this.registerSetting(BedwarsOverlay.overlayX = new SliderSetting("X", 4.0, 0.0, BedwarsOverlay.mc.field_71443_c, 1.0));
        this.registerSetting(BedwarsOverlay.overlayY = new SliderSetting("Y", 4.0, 0.0, BedwarsOverlay.mc.field_71440_d, 1.0));
        this.registerSetting(BedwarsOverlay.margin = new SliderSetting("Margin", 4.0, 0.0, 100.0, 1.0));
        this.registerSetting(BedwarsOverlay.marginTextX = new SliderSetting("Margin Text X", 21.0, 0.0, 100.0, 1.0));
        this.registerSetting(BedwarsOverlay.marginTextY = new SliderSetting("Margin Text Y", 8.0, 0.0, 100.0, 1.0));
        BedwarsOverlay.mainTextColour = -80411;
        BedwarsOverlay.backgroundColour = -1875099839;
        BedwarsOverlay.errorColour = -65485;
    }
    
    @Subscribe
    public void onForgeEvent(final ForgeEvent fe) {
        if (fe.getEvent() instanceof ClientChatReceivedEvent && Utils.Player.isPlayerInGame() && Utils.Java.str(((ClientChatReceivedEvent)fe.getEvent()).message.func_150260_c()).startsWith("Sending you to")) {
            BedwarsOverlay.playerStats.clear();
        }
    }
    
    @Subscribe
    public void onRender2D(final Render2DEvent e) {
        if (!BedwarsOverlay.active) {
            return;
        }
        if (BedwarsOverlay.mc.field_71462_r != null) {
            return;
        }
        if (!BedwarsOverlay.mc.field_71415_G || BedwarsOverlay.mc.field_71474_y.field_74330_P) {
            return;
        }
        final ScaledResolution sr = new ScaledResolution(Minecraft.func_71410_x());
        final FontRenderer fr = Minecraft.func_71410_x().field_71466_p;
        BedwarsOverlay.linesDrawn = 0;
        this.drawMain(sr, fr);
        ++BedwarsOverlay.linesDrawn;
        if (this.drawError(sr, fr)) {
            ++BedwarsOverlay.linesDrawn;
        }
        for (final NetworkPlayerInfo player : Utils.Client.getPlayers()) {
            this.drawStats(player, fr);
        }
        BedwarsOverlay.overlayHeight = BedwarsOverlay.margin.getInput() * 2.0 + fr.field_78288_b * BedwarsOverlay.linesDrawn + BedwarsOverlay.marginTextY.getInput() * --BedwarsOverlay.linesDrawn;
        fr.func_78276_b("", 0, 0, -1);
    }
    
    private void drawStats(final NetworkPlayerInfo player, final FontRenderer fr) {
        final String name = player.func_178845_a().getName();
        final String UUID = player.func_178845_a().getId().toString();
        if (Utils.URLS.hypixelApiKey.isEmpty()) {
            fr.func_78276_b(name, (int)BedwarsOverlay.statStart.get(StatType.PLAYER_NAME), (int)BedwarsOverlay.textY, -16399395);
            BedwarsOverlay.textY += fr.field_78288_b + BedwarsOverlay.marginTextY.getInput();
            ++BedwarsOverlay.linesDrawn;
        }
        else {
            if (!BedwarsOverlay.playerStats.containsKey(UUID)) {
                Raven.getExecutor().execute(() -> this.getBedwarsStats(UUID));
                BedwarsOverlay.playerStats.put(UUID, new int[] { -16 });
                return;
            }
            final int[] stats = BedwarsOverlay.playerStats.get(UUID);
            if (stats.length == 1 && stats[0] == -16) {
                return;
            }
            final double bbblr = (stats[4] != 0) ? Utils.Java.round(stats[3] / (double)stats[4], 2) : stats[3];
            final double fkdr = (stats[6] != 0) ? Utils.Java.round(stats[5] / (double)stats[6], 2) : stats[5];
            final double wlr = (stats[8] != 0) ? Utils.Java.round(stats[7] / (double)stats[8], 2) : stats[7];
            fr.func_78276_b(stats[0] + "", (int)BedwarsOverlay.statStart.get(StatType.LEVEL), (int)BedwarsOverlay.textY, getStarColour(stats[0]));
            fr.func_78276_b(name, (int)BedwarsOverlay.statStart.get(StatType.PLAYER_NAME), (int)BedwarsOverlay.textY, -1);
            if (stats[1] == 0) {
                fr.func_78276_b("  -", (int)BedwarsOverlay.statStart.get(StatType.NICKED), (int)BedwarsOverlay.textY, -5592406);
            }
            else {
                fr.func_78276_b("  +", (int)BedwarsOverlay.statStart.get(StatType.NICKED), (int)BedwarsOverlay.textY, -5636096);
            }
            fr.func_78276_b(stats[2] + "", (int)BedwarsOverlay.statStart.get(StatType.WS), (int)BedwarsOverlay.textY, this.getWSColour(stats[2]));
            fr.func_78276_b(bbblr + "", (int)BedwarsOverlay.statStart.get(StatType.BBBLR), (int)BedwarsOverlay.textY, this.getBBBLRColour(bbblr));
            fr.func_78276_b(fkdr + "", (int)BedwarsOverlay.statStart.get(StatType.FKDR), (int)BedwarsOverlay.textY, this.getFKDRColour(fkdr));
            fr.func_78276_b(wlr + "", (int)BedwarsOverlay.statStart.get(StatType.WLR), (int)BedwarsOverlay.textY, this.getWLRColour(wlr));
            fr.func_78276_b(stats[6] + "", (int)BedwarsOverlay.statStart.get(StatType.FINALS), (int)BedwarsOverlay.textY, this.getFinalColour(stats[6]));
            fr.func_78276_b(stats[7] + "", (int)BedwarsOverlay.statStart.get(StatType.WINS), (int)BedwarsOverlay.textY, this.getFinalColour(stats[7]));
            BedwarsOverlay.textY += BedwarsOverlay.marginTextY.getInput() + fr.field_78288_b;
            ++BedwarsOverlay.linesDrawn;
        }
    }
    
    private int getTreatColour(final String bad) {
        if (bad.equalsIgnoreCase("very high")) {
            return -5636096;
        }
        if (bad.equalsIgnoreCase("high")) {
            return -22016;
        }
        if (bad.equalsIgnoreCase("moderate")) {
            return -171;
        }
        if (bad.equalsIgnoreCase("LOW")) {
            return -16733696;
        }
        if (bad.equalsIgnoreCase("very low")) {
            return -5592406;
        }
        return -5592406;
    }
    
    private int getFinalColour(final int stat) {
        if (stat < 50) {
            return -5592406;
        }
        if (stat < 100) {
            return -1;
        }
        if (stat < 150) {
            return -16733696;
        }
        if (stat < 200) {
            return -11141121;
        }
        if (stat < 500) {
            return -171;
        }
        if (stat < 1000) {
            return -22016;
        }
        if (stat < 5000) {
            return -5636096;
        }
        if (stat >= 5000) {
            return -5635926;
        }
        return -5635926;
    }
    
    private int getFKDRColour(final double stat) {
        if (stat < 0.31) {
            return -5592406;
        }
        if (stat < 0.51) {
            return -1;
        }
        if (stat < 1.0) {
            return -16733696;
        }
        if (stat < 1.5) {
            return -11141121;
        }
        if (stat < 2.5) {
            return -171;
        }
        if (stat < 4.0) {
            return -22016;
        }
        if (stat < 10.0) {
            return -5636096;
        }
        if (stat >= 20.0) {
            return -5635926;
        }
        return -5635926;
    }
    
    private int getBBBLRColour(final double stat) {
        if (stat < 0.31) {
            return -5592406;
        }
        if (stat < 0.51) {
            return -1;
        }
        if (stat < 1.0) {
            return -16733696;
        }
        if (stat < 1.5) {
            return -11141121;
        }
        if (stat < 2.5) {
            return -171;
        }
        if (stat < 4.0) {
            return -22016;
        }
        if (stat < 10.0) {
            return -5636096;
        }
        if (stat >= 20.0) {
            return -5635926;
        }
        return -5635926;
    }
    
    private int getWLRColour(final double stat) {
        if (stat < 0.51) {
            return -5592406;
        }
        if (stat < 1.01) {
            return -1;
        }
        if (stat < 1.5) {
            return -16733696;
        }
        if (stat < 2.0) {
            return -11141121;
        }
        if (stat < 4.0) {
            return -171;
        }
        if (stat < 8.0) {
            return -22016;
        }
        if (stat < 15.0) {
            return -5636096;
        }
        if (stat >= 15.0) {
            return -5635926;
        }
        return -5635926;
    }
    
    private int getWSColour(final int stat) {
        if (stat < 5) {
            return -5592406;
        }
        if (stat < 10) {
            return -1;
        }
        if (stat < 15) {
            return -16733696;
        }
        if (stat < 20) {
            return -11141121;
        }
        if (stat < 30) {
            return -171;
        }
        if (stat < 50) {
            return -22016;
        }
        if (stat < 100) {
            return -5636096;
        }
        if (stat >= 100) {
            return -5635926;
        }
        return -5635926;
    }
    
    private void getBedwarsStats(final String uuid) {
        final int[] stats = new int[9];
        final String connection = Utils.URLS.getTextFromURL("https://api.hypixel.net/player?key=" + Utils.URLS.hypixelApiKey + "&uuid=" + uuid);
        if (connection.isEmpty()) {
            return;
        }
        if (connection.equals("{\"success\":true,\"player\":null}")) {
            stats[0] = -1;
            BedwarsOverlay.playerStats.put(uuid, stats);
        }
        JsonObject bw;
        JsonObject ach;
        try {
            final JsonObject profile = Utils.Java.getStringAsJson(connection).getAsJsonObject("player");
            bw = profile.getAsJsonObject("stats").getAsJsonObject("Bedwars");
            ach = profile.getAsJsonObject("achievements");
        }
        catch (NullPointerException er) {
            BedwarsOverlay.playerStats.put(uuid, stats);
            return;
        }
        stats[0] = Utils.Java.getValue(ach, "bedwars_level");
        stats[1] = ((stats[0] < 0) ? -1 : 0);
        stats[2] = Utils.Java.getValue(bw, "winstreak");
        stats[3] = Utils.Java.getValue(bw, "beds_broken_bedwars");
        stats[4] = Utils.Java.getValue(bw, "beds_lost_bedwars");
        stats[6] = Utils.Java.getValue(bw, "final_deaths_bedwars");
        stats[5] = Utils.Java.getValue(bw, "final_kills_bedwars");
        stats[7] = Utils.Java.getValue(bw, "wins_bedwars");
        stats[8] = Utils.Java.getValue(bw, "losses_bedwars");
        BedwarsOverlay.playerStats.put(uuid, stats);
    }
    
    private boolean drawError(final ScaledResolution sr, final FontRenderer fr) {
        final String noApiKey = "API key is not set!";
        final String noPlayers = "No players in lobby!";
        if (Utils.URLS.hypixelApiKey.isEmpty()) {
            fr.func_78276_b(noApiKey, (int)(BedwarsOverlay.overlayWidth + BedwarsOverlay.overlayX.getInput() - BedwarsOverlay.overlayWidth / 2.0 - fr.func_78256_a(noApiKey) / 2), (int)BedwarsOverlay.textY, BedwarsOverlay.errorColour);
            BedwarsOverlay.textY += fr.field_78288_b + BedwarsOverlay.marginTextY.getInput();
            return true;
        }
        if (!Utils.Client.othersExist()) {
            fr.func_78276_b(noPlayers, (int)(BedwarsOverlay.overlayWidth + BedwarsOverlay.overlayX.getInput() - BedwarsOverlay.overlayWidth / 2.0 - fr.func_78256_a(noPlayers) / 2), (int)BedwarsOverlay.textY, BedwarsOverlay.errorColour);
            BedwarsOverlay.textY += fr.field_78288_b + BedwarsOverlay.marginTextY.getInput();
            return true;
        }
        return false;
    }
    
    private void drawMain(final ScaledResolution sr, final FontRenderer fr) {
        Gui.func_73734_a((int)BedwarsOverlay.overlayX.getInput(), (int)BedwarsOverlay.overlayY.getInput(), (int)(BedwarsOverlay.overlayWidth + BedwarsOverlay.overlayX.getInput()), (int)(BedwarsOverlay.overlayHeight + BedwarsOverlay.overlayY.getInput()), BedwarsOverlay.backgroundColour);
        double textX = BedwarsOverlay.margin.getInput() + BedwarsOverlay.overlayX.getInput();
        BedwarsOverlay.textY = BedwarsOverlay.margin.getInput() + BedwarsOverlay.overlayY.getInput();
        int stringWidth = 0;
        for (final StatType statType : StatType.values()) {
            fr.func_78276_b(statType + "", (int)textX, (int)BedwarsOverlay.textY, BedwarsOverlay.mainTextColour);
            BedwarsOverlay.statStart.put(statType, (int)textX);
            stringWidth = fr.func_78256_a(statType + "");
            textX += stringWidth + BedwarsOverlay.marginTextX.getInput();
        }
        BedwarsOverlay.textY += BedwarsOverlay.marginTextY.getInput() + fr.field_78288_b;
        BedwarsOverlay.overlayWidth = textX + BedwarsOverlay.margin.getInput() - BedwarsOverlay.marginTextX.getInput() - BedwarsOverlay.overlayX.getInput();
    }
    
    public static int getStarColour(final int stat) {
        if (stat < 20) {
            return -5592406;
        }
        if (stat < 50) {
            return -1;
        }
        if (stat < 150) {
            return -16733696;
        }
        if (stat < 200) {
            return -11141121;
        }
        if (stat < 400) {
            return -171;
        }
        if (stat < 500) {
            return -22016;
        }
        if (stat < 1000) {
            return -5636096;
        }
        if (stat >= 1000) {
            return -5635926;
        }
        return -5635926;
    }
    
    @Override
    public void onEnable() {
        super.onEnable();
        BedwarsOverlay.active = true;
    }
    
    @Override
    public void onDisable() {
        super.onDisable();
        BedwarsOverlay.active = false;
    }
    
    static {
        BedwarsOverlay.playerStats = new HashMap<String, int[]>();
        BedwarsOverlay.statStart = new HashMap<StatType, Integer>();
    }
    
    public enum StatType
    {
        LEVEL, 
        PLAYER_NAME, 
        NICKED, 
        WS, 
        BBBLR, 
        FKDR, 
        WLR, 
        FINALS, 
        WINS, 
        OVERALLTHREAT;
    }
    
    public static class Colours
    {
        public static final int GREY = -5592406;
        public static final int WHITE = -1;
        public static final int GREEN = -16733696;
        public static final int AQUA = -11141121;
        public static final int YELLOW = -171;
        public static final int ORANGE = -22016;
        public static final int RED = -5636096;
        public static final int PURPLE = -5635926;
    }
}
