package keystrokesmod.client.command.commands;

import keystrokesmod.client.command.*;
import keystrokesmod.client.main.*;
import keystrokesmod.client.clickgui.raven.*;
import keystrokesmod.client.utils.version.*;

public class VersionCommand extends Command
{
    public VersionCommand() {
        super("version", "tells you what build of B++ you are using", 0, 0, new String[0], new String[] { "v", "ver", "which", "build", "b" });
    }
    
    @Override
    public void onCall(final String[] args) {
        final Version clientVersion = Raven.versionManager.getClientVersion();
        final Version latestVersion = Raven.versionManager.getLatestVersion();
        Terminal.print("Your build: " + clientVersion);
        Terminal.print("Latest version: " + latestVersion);
    }
}
