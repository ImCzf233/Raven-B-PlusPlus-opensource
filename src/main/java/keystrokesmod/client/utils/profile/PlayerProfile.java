package keystrokesmod.client.utils.profile;

import keystrokesmod.client.utils.*;
import com.google.gson.*;

public class PlayerProfile
{
    public boolean isPlayer;
    public boolean nicked;
    public int wins;
    public int losses;
    public int winStreak;
    public String inGameName;
    public String uuid;
    private final Utils.Profiles.DuelsStatsMode statsMode;
    
    public PlayerProfile(final UUID uuid, final Utils.Profiles.DuelsStatsMode mode) {
        this.isPlayer = true;
        this.uuid = uuid.uuid;
        this.statsMode = mode;
    }
    
    public PlayerProfile(final String name, final Utils.Profiles.DuelsStatsMode mode) {
        this.isPlayer = true;
        this.inGameName = name;
        this.statsMode = mode;
    }
    
    public void populateStats() {
        if (this.uuid == null) {
            this.uuid = Utils.Profiles.getUUIDFromName(this.inGameName);
            if (this.uuid.isEmpty()) {
                this.isPlayer = false;
                return;
            }
        }
        final String textFromURL = Utils.URLS.getTextFromURL("https://api.hypixel.net/player?key=" + Utils.URLS.hypixelApiKey + "&uuid=" + this.uuid);
        if (textFromURL.isEmpty()) {
            this.nicked = true;
        }
        else if (textFromURL.equals("{\"success\":true,\"player\":null}")) {
            this.nicked = true;
        }
        else {
            JsonObject d;
            try {
                final JsonObject pr = Utils.Profiles.parseJson(textFromURL).getAsJsonObject("player");
                this.inGameName = pr.get("displayname").getAsString();
                d = pr.getAsJsonObject("stats").getAsJsonObject("Duels");
            }
            catch (NullPointerException var8) {
                return;
            }
            switch (this.statsMode) {
                case OVERALL: {
                    this.wins = Utils.Profiles.getValueAsInt(d, "wins");
                    this.losses = Utils.Profiles.getValueAsInt(d, "losses");
                    this.winStreak = Utils.Profiles.getValueAsInt(d, "current_winstreak");
                    break;
                }
                case BRIDGE: {
                    this.wins = Utils.Profiles.getValueAsInt(d, "bridge_duel_wins");
                    this.losses = Utils.Profiles.getValueAsInt(d, "bridge_duel_losses");
                    this.winStreak = Utils.Profiles.getValueAsInt(d, "current_winstreak_mode_bridge_duel");
                    break;
                }
                case UHC: {
                    this.wins = Utils.Profiles.getValueAsInt(d, "uhc_duel_wins");
                    this.losses = Utils.Profiles.getValueAsInt(d, "uhc_duel_losses");
                    this.winStreak = Utils.Profiles.getValueAsInt(d, "current_winstreak_mode_uhc_duel");
                    break;
                }
                case SKYWARS: {
                    this.wins = Utils.Profiles.getValueAsInt(d, "sw_duel_wins");
                    this.losses = Utils.Profiles.getValueAsInt(d, "sw_duel_losses");
                    this.winStreak = Utils.Profiles.getValueAsInt(d, "current_winstreak_mode_sw_duel");
                    break;
                }
                case CLASSIC: {
                    this.wins = Utils.Profiles.getValueAsInt(d, "classic_duel_wins");
                    this.losses = Utils.Profiles.getValueAsInt(d, "classic_duel_losses");
                    this.winStreak = Utils.Profiles.getValueAsInt(d, "current_winstreak_mode_classic_duel");
                    break;
                }
                case SUMO: {
                    this.wins = Utils.Profiles.getValueAsInt(d, "sumo_duel_wins");
                    this.losses = Utils.Profiles.getValueAsInt(d, "sumo_duel_losses");
                    this.winStreak = Utils.Profiles.getValueAsInt(d, "current_winstreak_mode_sumo_duel");
                    break;
                }
                case OP: {
                    this.wins = Utils.Profiles.getValueAsInt(d, "op_duel_wins");
                    this.losses = Utils.Profiles.getValueAsInt(d, "op_duel_losses");
                    this.winStreak = Utils.Profiles.getValueAsInt(d, "current_winstreak_mode_op_duel");
                    break;
                }
            }
        }
    }
}
