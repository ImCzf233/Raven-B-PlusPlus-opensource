package keystrokesmod.client.config;

import com.google.gson.*;
import java.io.*;

public class Config
{
    public final File file;
    public final long creationDate;
    
    public Config(final File pathToFile) {
        this.file = pathToFile;
        long creationDate1 = 0L;
        Label_0066: {
            if (!this.file.exists()) {
                creationDate1 = System.currentTimeMillis();
                try {
                    this.file.createNewFile();
                    break Label_0066;
                }
                catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            try {
                creationDate1 = this.getData().get("creationTime").getAsLong();
            }
            catch (NullPointerException e2) {
                creationDate1 = 0L;
            }
        }
        this.creationDate = creationDate1;
    }
    
    public String getName() {
        return this.file.getName().replace(".bplus", "");
    }
    
    public JsonObject getData() {
        final JsonParser jsonParser = new JsonParser();
        try (final FileReader reader = new FileReader(this.file)) {
            final JsonElement obj = jsonParser.parse((Reader)reader);
            return obj.isJsonNull() ? null : obj.getAsJsonObject();
        }
        catch (JsonSyntaxException | ClassCastException | IOException ex2) {
            final Exception ex;
            final Exception e = ex;
            e.printStackTrace();
            return null;
        }
    }
    
    public void save(final JsonObject data) {
        data.addProperty("creationTime", (Number)this.creationDate);
        try (final PrintWriter out = new PrintWriter(new FileWriter(this.file))) {
            out.write(data.toString());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
