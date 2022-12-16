package keystrokesmod.client.command.commands;

import keystrokesmod.client.command.*;
import keystrokesmod.client.utils.*;
import keystrokesmod.client.clickgui.raven.*;
import keystrokesmod.client.main.*;
import keystrokesmod.client.utils.profile.*;

public class Duels extends Command
{
    public Duels() {
        super("duels", "Fetches a player's stats", 1, 2, new String[] { "Player name", "overall/uhc/bridge/skywars/sumo/classic/op" }, new String[] { "d", "duel", "stat", "stats", "check" });
    }
    
    @Override
    public void onCall(final String[] args) {
        if (Utils.URLS.hypixelApiKey.isEmpty()) {
            Terminal.print("API Key is empty! Run \"setkey api_key\".");
            return;
        }
        if (args.length == 0) {
            this.incorrectArgs();
            return;
        }
        if (args.length == 1) {
            final String n = args[0];
            Terminal.print("Retrieving data...");
            final String name;
            final PlayerProfile playerProfile;
            double wlr;
            Raven.getExecutor().execute(() -> {
                playerProfile = new PlayerProfile(name, Utils.Profiles.DuelsStatsMode.OVERALL);
                playerProfile.populateStats();
                if (!playerProfile.isPlayer) {
                    Terminal.print(name + " does not exist");
                }
                else if (playerProfile.nicked) {
                    Terminal.print(name + " is nicked");
                }
                else {
                    wlr = ((playerProfile.losses != 0) ? Utils.Java.round(playerProfile.wins / (double)playerProfile.losses, 2) : playerProfile.wins);
                    Terminal.print(name + " overall stats:");
                    Terminal.print("Wins: " + playerProfile.wins);
                    Terminal.print("Losses: " + playerProfile.losses);
                    Terminal.print("WLR: " + wlr);
                    Terminal.print("Winstreak: " + playerProfile.winStreak);
                }
            });
        }
        else if (args.length == 2) {
            final String stringGamemode = args[1];
            Utils.Profiles.DuelsStatsMode gameMode = null;
            for (final Utils.Profiles.DuelsStatsMode mode : Utils.Profiles.DuelsStatsMode.values()) {
                if (String.valueOf(mode).equalsIgnoreCase(stringGamemode)) {
                    gameMode = mode;
                }
            }
            if (gameMode == null) {
                Terminal.print(stringGamemode + " is not a known gamemode. See \"help duels\" for a known list of gamemode");
            }
            else {
                final String n2 = args[0];
                Terminal.print("Retrieving data...");
                final Utils.Profiles.DuelsStatsMode finalGameMode = gameMode;
                final String name2;
                final Utils.Profiles.DuelsStatsMode mode2;
                final PlayerProfile playerProfile2;
                double wlr2;
                Raven.getExecutor().execute(() -> {
                    playerProfile2 = new PlayerProfile(name2, mode2);
                    playerProfile2.populateStats();
                    if (!playerProfile2.isPlayer) {
                        Terminal.print(name2 + " does not exist");
                    }
                    else if (playerProfile2.nicked) {
                        Terminal.print(name2 + " is nicked");
                    }
                    else {
                        wlr2 = ((playerProfile2.losses != 0) ? Utils.Java.round(playerProfile2.wins / (double)playerProfile2.losses, 2) : playerProfile2.wins);
                        Terminal.print(name2 + " " + mode2 + " stats:");
                        Terminal.print("Wins: " + playerProfile2.wins);
                        Terminal.print("Losses: " + playerProfile2.losses);
                        Terminal.print("WLR: " + wlr2);
                        Terminal.print("Winstreak: " + playerProfile2.winStreak);
                    }
                });
            }
        }
    }
}
