package keystrokesmod.client.utils.version;

import java.util.*;
import java.net.*;
import java.io.*;

public class VersionManager
{
    private final String versionFilePath = "/assets/keystrokes/version";
    private final String branchFilePath = "/assets/keystrokes/branch";
    private final String versionUrl = "https://raw.githubusercontent.com/K-ov/Raven-bPLUS/stable/src/main/resources/assets/keystrokes/version";
    private final String branchUrl = "https://raw.githubusercontent.com/K-ov/Raven-bPLUS/stable/src/main/resources/assets/keystrokes/branch";
    private Version latestVersion;
    private Version clientVersion;
    
    public VersionManager() {
        this.createClientVersion();
        this.createLatestVersion();
    }
    
    private void createLatestVersion() {
        String version = "1.0.0";
        String branch = "";
        int branchCommit = 0;
        try {
            final URL url = new URL("https://raw.githubusercontent.com/K-ov/Raven-bPLUS/stable/src/main/resources/assets/keystrokes/version");
            final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(url.openStream()));
            final Scanner scanner = new Scanner(bufferedReader);
            version = scanner.nextLine();
        }
        catch (MalformedURLException e) {
            e.printStackTrace();
        }
        catch (IOException e2) {
            e2.printStackTrace();
        }
        try {
            final URL url = new URL("https://raw.githubusercontent.com/K-ov/Raven-bPLUS/stable/src/main/resources/assets/keystrokes/branch");
            final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(url.openStream()));
            final Scanner scanner = new Scanner(bufferedReader);
            final String[] line = scanner.nextLine().split("-");
            branch = line[0];
            branchCommit = Integer.parseInt(line[1]);
        }
        catch (MalformedURLException e) {
            e.printStackTrace();
        }
        catch (IOException e2) {
            e2.printStackTrace();
        }
        catch (NumberFormatException e3) {
            e3.printStackTrace();
        }
        this.latestVersion = new Version(version, branch, branchCommit);
    }
    
    private void createClientVersion() {
        String version = "1.0.0";
        String branch = "";
        int branchCommit = 0;
        InputStream input = VersionManager.class.getResourceAsStream("/assets/keystrokes/version");
        assert input != null;
        Scanner scanner = new Scanner(input);
        version = scanner.nextLine();
        input = VersionManager.class.getResourceAsStream("/assets/keystrokes/branch");
        scanner = new Scanner(input);
        final String[] line = scanner.nextLine().split("-");
        branch = line[0];
        try {
            branchCommit = Integer.parseInt(line[1]);
        }
        catch (NumberFormatException ex) {}
        this.clientVersion = new Version(version, branch, branchCommit);
    }
    
    public Version getClientVersion() {
        return this.clientVersion;
    }
    
    public Version getLatestVersion() {
        return this.latestVersion;
    }
}
