package keystrokesmod.client.utils.version;

import java.util.*;

public class Version
{
    private final String version;
    private final String branchName;
    private final int branchCommit;
    private final ArrayList<Integer> versionNumbers;
    
    public Version(final String version, final String branchName, final int branchCommit) {
        this.versionNumbers = new ArrayList<Integer>();
        this.version = version.replace("-", ".");
        this.branchName = branchName;
        this.branchCommit = branchCommit;
        for (final String number : this.version.split("\\.")) {
            this.versionNumbers.add(Integer.parseInt(number));
        }
    }
    
    public String getVersion() {
        return this.version;
    }
    
    public String getBranchName() {
        return this.branchName;
    }
    
    public int getBranchCommit() {
        return this.branchCommit;
    }
    
    public ArrayList<Integer> getVersionNumbers() {
        return this.versionNumbers;
    }
    
    public boolean isNewerThan(final Version versionToCompare) {
        return versionToCompare.getVersionNumbers().get(0) < this.getVersionNumbers().get(0) || (versionToCompare.getVersionNumbers().get(0) <= this.getVersionNumbers().get(0) && (versionToCompare.getVersionNumbers().get(1) < this.getVersionNumbers().get(1) || (versionToCompare.getVersionNumbers().get(1) <= this.getVersionNumbers().get(1) && versionToCompare.getVersionNumbers().get(2) < this.getVersionNumbers().get(2))));
    }
    
    public boolean equals(final Version version) {
        final ArrayList<Integer> now = this.getVersionNumbers();
        final ArrayList<Integer> nvw = version.getVersionNumbers();
        return now.get(0).equals(nvw.get(0)) && now.get(1).equals(nvw.get(1)) && now.get(2).equals(nvw.get(2));
    }
    
    @Override
    public String toString() {
        return this.version + " " + this.branchName + " " + this.branchCommit;
    }
}
