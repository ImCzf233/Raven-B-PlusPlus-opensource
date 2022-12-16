package keystrokesmod.client.module.modules.minigames.Sumo;

import keystrokesmod.client.module.*;
import keystrokesmod.client.module.setting.impl.*;
import keystrokesmod.client.module.setting.*;
import keystrokesmod.client.event.impl.*;
import net.minecraft.client.*;
import net.minecraft.scoreboard.*;
import keystrokesmod.client.main.*;
import java.util.*;
import com.google.common.eventbus.*;
import keystrokesmod.client.utils.*;
import keystrokesmod.client.utils.profile.*;
import org.apache.http.client.methods.*;
import java.nio.charset.*;
import org.apache.http.impl.client.*;
import java.io.*;
import com.google.gson.*;

public class SumoStats extends Module
{
    public SliderSetting ws;
    public SliderSetting wlr;
    public String playerNick;
    private final List<String> queue;
    
    public SumoStats() {
        super("Sumo Stats", ModuleCategory.sumo);
        this.playerNick = "";
        this.queue = new ArrayList<String>();
        this.registerSetting(this.ws = new SliderSetting("wlr", 2.0, 0.0, 10.0, 0.1));
        this.registerSetting(this.ws = new SliderSetting("ws", 4.0, 0.0, 30.0, 1.0));
    }
    
    @Override
    public void onEnable() {
        if (SumoStats.mc.field_71439_g == null) {
            this.disable();
        }
    }
    
    @Override
    public void onDisable() {
        this.queue.clear();
    }
    
    @Subscribe
    public void onTick(final TickEvent e) {
        if (!this.isDuel()) {
            return;
        }
        for (final ScorePlayerTeam team : Minecraft.func_71410_x().field_71441_e.func_96441_U().func_96525_g()) {
            for (final String playerName : team.func_96670_d()) {
                if (!this.queue.contains(playerName) && team.func_96668_e().equals("¡ì7¡ìk") && !playerName.equalsIgnoreCase(Minecraft.func_71410_x().field_71439_g.getDisplayNameString())) {
                    this.queue.add(playerName);
                    final String s;
                    final String id;
                    Raven.getExecutor().execute(() -> {
                        id = this.getPlayerUUID(s);
                        if (!id.isEmpty()) {
                            this.getAndDisplayStatsForPlayer(id, s);
                        }
                    });
                }
            }
        }
    }
    
    private void getAndDisplayStatsForPlayer(final String uuid, final String playerName) {
        if (Utils.URLS.hypixelApiKey.isEmpty()) {
            Utils.Player.sendMessageToSelf("&cAPI Key is empty!");
        }
        else {
            final Utils.Profiles.DuelsStatsMode dm = Utils.Profiles.DuelsStatsMode.SUMO;
            final PlayerProfile playerProfile2;
            final PlayerProfile playerProfile;
            double wlr;
            final Utils.Profiles.DuelsStatsMode duelsStatsMode;
            SumoBot sb;
            Raven.getExecutor().execute(() -> {
                new PlayerProfile(new UUID(uuid), Utils.Profiles.DuelsStatsMode.SUMO);
                playerProfile = playerProfile2;
                playerProfile.populateStats();
                if (!(!playerProfile.isPlayer)) {
                    if (playerProfile.nicked) {
                        Utils.Player.sendMessageToSelf("&3" + playerName + " &eis nicked!");
                    }
                    else {
                        wlr = ((playerProfile.losses != 0) ? Utils.Java.round(playerProfile.wins / (double)playerProfile.losses, 2) : playerProfile.wins);
                        Utils.Player.sendMessageToSelf("&7&m-------------------------");
                        if (duelsStatsMode != Utils.Profiles.DuelsStatsMode.OVERALL) {
                            Utils.Player.sendMessageToSelf("&eMode: &3" + duelsStatsMode.name());
                        }
                        Utils.Player.sendMessageToSelf("&eOpponent: &3" + playerName);
                        Utils.Player.sendMessageToSelf("&eWins: &3" + playerProfile.wins);
                        Utils.Player.sendMessageToSelf("&eLosses: &3" + playerProfile.losses);
                        Utils.Player.sendMessageToSelf("&eWLR: &3" + wlr);
                        Utils.Player.sendMessageToSelf("&eWS: &3" + playerProfile.winStreak);
                        Utils.Player.sendMessageToSelf("&7&m-------------------------");
                        if (wlr > this.wlr.getInput() || playerProfile.winStreak > this.ws.getInput()) {
                            sb = (SumoBot)Raven.moduleManager.getModuleByName("Sumo Bot");
                            sb.reQueue();
                        }
                    }
                }
            });
        }
    }
    
    private boolean isDuel() {
        if (Utils.Client.isHyp()) {
            int l = 0;
            for (final String s : Utils.Client.getPlayersFromScoreboard()) {
                if (s.contains("Map:")) {
                    ++l;
                }
                else {
                    if (!s.contains("Players:") || !s.contains("/2")) {
                        continue;
                    }
                    ++l;
                }
            }
            return l == 2;
        }
        return false;
    }
    
    private String getPlayerUUID(final String username) {
        String playerUUID = "";
        try (final CloseableHttpClient client = HttpClients.createDefault()) {
            final HttpGet request = new HttpGet(String.format("https://api.mojang.com/users/profiles/minecraft/%s", username));
            try (final InputStream is = client.execute((HttpUriRequest)request).getEntity().getContent()) {
                final JsonParser parser = new JsonParser();
                final JsonObject object = parser.parse((Reader)new InputStreamReader(is, StandardCharsets.UTF_8)).getAsJsonObject();
                playerUUID = object.get("id").getAsString();
            }
            catch (NullPointerException ex2) {
                System.out.println("Null or invalid player provided by the server.");
            }
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
        return playerUUID;
    }
}
