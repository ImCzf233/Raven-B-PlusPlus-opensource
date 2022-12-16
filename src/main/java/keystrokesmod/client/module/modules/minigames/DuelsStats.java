package keystrokesmod.client.module.modules.minigames;

import keystrokesmod.client.module.*;
import keystrokesmod.client.module.setting.impl.*;
import keystrokesmod.client.utils.*;
import keystrokesmod.client.module.setting.*;
import keystrokesmod.client.event.impl.*;
import net.minecraft.client.*;
import net.minecraft.scoreboard.*;
import keystrokesmod.client.main.*;
import java.util.*;
import com.google.common.eventbus.*;
import keystrokesmod.client.utils.profile.*;
import org.apache.http.client.methods.*;
import java.nio.charset.*;
import org.apache.http.impl.client.*;
import java.io.*;
import com.google.gson.*;

public class DuelsStats extends Module
{
    public static ComboSetting selectedGameMode;
    public static TickSetting sendIgnOnJoin;
    private final List<String> queue;
    
    public DuelsStats() {
        super("Duels Stats", ModuleCategory.minigames);
        this.queue = new ArrayList<String>();
        this.registerSetting(DuelsStats.selectedGameMode = new ComboSetting("Stats for mode:", (T)Utils.Profiles.DuelsStatsMode.OVERALL));
        this.registerSetting(DuelsStats.sendIgnOnJoin = new TickSetting("Send ign on join", false));
    }
    
    @Override
    public void onEnable() {
        if (DuelsStats.mc.field_71439_g == null) {
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
                            if (DuelsStats.sendIgnOnJoin.isToggled()) {
                                Utils.Player.sendMessageToSelf("&eOpponent found: &3" + s);
                            }
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
            final Utils.Profiles.DuelsStatsMode dm = DuelsStats.selectedGameMode.getMode();
            final PlayerProfile playerProfile2;
            final PlayerProfile playerProfile;
            double wlr;
            final Utils.Profiles.DuelsStatsMode duelsStatsMode;
            Raven.getExecutor().execute(() -> {
                new PlayerProfile(new UUID(uuid), DuelsStats.selectedGameMode.getMode());
                playerProfile = playerProfile2;
                playerProfile.populateStats();
                if (!(!playerProfile.isPlayer)) {
                    if (playerProfile.nicked) {
                        Utils.Player.sendMessageToSelf("&3" + playerName + " &eis nicked!");
                    }
                    else {
                        if (DuelsStats.sendIgnOnJoin.isToggled()) {
                            Utils.Player.sendMessageToSelf("&eOpponent found: &3" + playerProfile.inGameName);
                        }
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
