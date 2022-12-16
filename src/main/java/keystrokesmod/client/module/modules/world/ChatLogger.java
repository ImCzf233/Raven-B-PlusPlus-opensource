package keystrokesmod.client.module.modules.world;

import keystrokesmod.client.module.*;
import java.time.format.*;
import java.time.*;
import java.time.temporal.*;
import keystrokesmod.client.event.impl.*;
import net.minecraftforge.client.event.*;
import java.io.*;
import com.google.common.eventbus.*;

public class ChatLogger extends Module
{
    private final File dir;
    private File chatLog;
    public String fileName;
    public String extension;
    
    public ChatLogger() {
        super("Chat Logger", ModuleCategory.world);
        this.extension = "txt";
        this.dir = new File(ChatLogger.mc.field_71412_D, "keystrokes" + File.separator + "logs");
        if (!this.dir.exists()) {
            this.dir.mkdir();
        }
    }
    
    @Override
    public void onEnable() {
        final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy_HH.mm.ss");
        final LocalDateTime now = LocalDateTime.now();
        this.fileName = dtf.format(now) + "." + this.extension;
        this.chatLog = new File(this.dir, this.fileName);
        if (!this.chatLog.exists()) {
            try {
                this.chatLog.createNewFile();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        super.onEnable();
    }
    
    @Subscribe
    public void onMessageRecieved(final ForgeEvent fe) {
        if (fe.getEvent() instanceof ClientChatReceivedEvent) {
            try (final FileWriter fw = new FileWriter(this.chatLog.getPath(), true);
                 final BufferedWriter bw = new BufferedWriter(fw);
                 final PrintWriter out = new PrintWriter(bw)) {
                out.println(((ClientChatReceivedEvent)fe.getEvent()).message.func_150260_c());
            }
            catch (IOException ex) {}
        }
    }
}
