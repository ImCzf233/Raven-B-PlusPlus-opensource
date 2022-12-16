package keystrokesmod.client.module.modules.minigames;

import keystrokesmod.client.module.*;
import keystrokesmod.client.module.setting.impl.*;
import net.minecraft.util.*;
import keystrokesmod.client.module.setting.*;
import net.minecraft.entity.*;
import net.minecraft.entity.player.*;
import net.minecraft.entity.item.*;
import keystrokesmod.client.utils.*;
import net.minecraft.init.*;
import java.util.*;
import net.minecraft.item.*;
import com.google.common.eventbus.*;
import keystrokesmod.client.event.impl.*;
import net.minecraftforge.client.event.*;
import net.minecraftforge.event.entity.*;
import java.awt.*;
import net.minecraftforge.fml.client.config.*;
import java.io.*;
import net.minecraft.client.gui.*;

public class BridgeInfo extends Module
{
    public static DescriptionSetting a;
    public static TickSetting ep;
    private static final int rgb;
    private static int hudX;
    private static int hudY;
    private String en;
    private BlockPos g1p;
    private BlockPos g2p;
    private boolean q;
    private double d1;
    private double d2;
    private int blc;
    
    public BridgeInfo() {
        super("Bridge Info", ModuleCategory.minigames);
        this.en = "";
        this.registerSetting(BridgeInfo.a = new DescriptionSetting("Only for solos."));
        this.registerSetting(BridgeInfo.ep = new TickSetting("Edit position", false));
    }
    
    @Override
    public void onDisable() {
        this.rv();
    }
    
    @Override
    public void guiButtonToggled(final TickSetting b) {
        if (b == BridgeInfo.ep) {
            BridgeInfo.ep.disable();
            BridgeInfo.mc.func_147108_a((GuiScreen)new eh());
        }
    }
    
    @Subscribe
    public void onTick(final TickEvent ev) {
        if (!this.en.isEmpty() && this.ibd()) {
            EntityPlayer enem = null;
            for (final Entity e : BridgeInfo.mc.field_71441_e.field_72996_f) {
                if (e instanceof EntityPlayer) {
                    if (!e.func_70005_c_().equals(this.en)) {
                        continue;
                    }
                    enem = (EntityPlayer)e;
                }
                else {
                    if (!(e instanceof EntityArmorStand)) {
                        continue;
                    }
                    final String g2t = "Jump in to score!";
                    final String g1t = "Defend!";
                    if (e.func_70005_c_().contains(g1t)) {
                        this.g1p = e.func_180425_c();
                    }
                    else {
                        if (!e.func_70005_c_().contains(g2t)) {
                            continue;
                        }
                        this.g2p = e.func_180425_c();
                    }
                }
            }
            if (this.g1p != null && this.g2p != null) {
                this.d1 = Utils.Java.round(BridgeInfo.mc.field_71439_g.func_70011_f((double)this.g2p.func_177958_n(), (double)this.g2p.func_177956_o(), (double)this.g2p.func_177952_p()) - 1.4, 1);
                if (this.d1 < 0.0) {
                    this.d1 = 0.0;
                }
                this.d2 = ((enem == null) ? 0.0 : Utils.Java.round(enem.func_70011_f((double)this.g1p.func_177958_n(), (double)this.g1p.func_177956_o(), (double)this.g1p.func_177952_p()) - 1.4, 1));
                if (this.d2 < 0.0) {
                    this.d2 = 0.0;
                }
            }
            int blc2 = 0;
            for (int i = 0; i < 9; ++i) {
                final ItemStack stack = BridgeInfo.mc.field_71439_g.field_71071_by.func_70301_a(i);
                if (stack != null && stack.func_77973_b() instanceof ItemBlock && ((ItemBlock)stack.func_77973_b()).field_150939_a.equals(Blocks.field_150406_ce)) {
                    blc2 += stack.field_77994_a;
                }
            }
            this.blc = blc2;
        }
    }
    
    @Subscribe
    public void onRender2D(final Render2DEvent ev) {
        if (Utils.Player.isPlayerInGame() && this.ibd()) {
            if (BridgeInfo.mc.field_71462_r != null || BridgeInfo.mc.field_71474_y.field_74330_P) {
                return;
            }
            final String t1 = "Enemy: ";
            BridgeInfo.mc.field_71466_p.func_175065_a(t1 + this.en, (float)BridgeInfo.hudX, (float)BridgeInfo.hudY, BridgeInfo.rgb, true);
            final String t2 = "Distance to goal: ";
            BridgeInfo.mc.field_71466_p.func_175065_a(t2 + this.d1, (float)BridgeInfo.hudX, (float)(BridgeInfo.hudY + 11), BridgeInfo.rgb, true);
            final String t3 = "Enemy distance to goal: ";
            BridgeInfo.mc.field_71466_p.func_175065_a(t3 + this.d2, (float)BridgeInfo.hudX, (float)(BridgeInfo.hudY + 22), BridgeInfo.rgb, true);
            final String t4 = "Blocks: ";
            BridgeInfo.mc.field_71466_p.func_175065_a(t4 + this.blc, (float)BridgeInfo.hudX, (float)(BridgeInfo.hudY + 33), BridgeInfo.rgb, true);
        }
    }
    
    @Subscribe
    public void onForgeEvent(final ForgeEvent fe) {
        if (fe.getEvent() instanceof ClientChatReceivedEvent) {
            final ClientChatReceivedEvent c = (ClientChatReceivedEvent)fe.getEvent();
            if (Utils.Player.isPlayerInGame()) {
                final String s = Utils.Java.str(c.message.func_150260_c());
                if (s.startsWith(" ")) {
                    final String qt = "First player to score 5 goals wins";
                    if (s.contains(qt)) {
                        this.q = true;
                    }
                    else if (this.q && s.contains("Opponent:")) {
                        String n = s.split(":")[1].trim();
                        if (n.contains("[")) {
                            n = n.split("] ")[1];
                        }
                        this.en = n;
                        this.q = false;
                    }
                }
            }
        }
        else if (fe.getEvent() instanceof EntityJoinWorldEvent && ((EntityJoinWorldEvent)fe.getEvent()).entity == BridgeInfo.mc.field_71439_g) {
            this.rv();
        }
    }
    
    private boolean ibd() {
        if (Utils.Client.isHyp()) {
            for (final String s : Utils.Client.getPlayersFromScoreboard()) {
                final String s2 = s.toLowerCase();
                final String bd = "the brid";
                if (s2.contains("mode") && s2.contains(bd)) {
                    return true;
                }
            }
        }
        return false;
    }
    
    private void rv() {
        this.en = "";
        this.q = false;
        this.g1p = null;
        this.g2p = null;
        this.d1 = 0.0;
        this.d2 = 0.0;
        this.blc = 0;
    }
    
    static {
        rgb = new Color(0, 200, 200).getRGB();
        BridgeInfo.hudX = 5;
        BridgeInfo.hudY = 70;
    }
    
    static class eh extends GuiScreen
    {
        final String a = "Enemy: Player123-Distance to goal: 17.2-Enemy distance to goal: 16.3-Blocks: 98";
        GuiButtonExt rp;
        boolean d;
        int miX;
        int miY;
        int maX;
        int maY;
        int aX;
        int aY;
        int laX;
        int laY;
        int lmX;
        int lmY;
        
        eh() {
            this.aX = 5;
            this.aY = 70;
        }
        
        public void func_73866_w_() {
            super.func_73866_w_();
            this.field_146292_n.add(this.rp = new GuiButtonExt(1, this.field_146294_l - 90, 5, 85, 20, "Reset position"));
            this.aX = BridgeInfo.hudX;
            this.aY = BridgeInfo.hudY;
        }
        
        public void func_73863_a(final int mX, final int mY, final float pt) {
            func_73734_a(0, 0, this.field_146294_l, this.field_146295_m, -1308622848);
            final int miX = this.aX;
            final int miY = this.aY;
            final int maX = miX + 140;
            final int maY = miY + 41;
            final FontRenderer field_71466_p = this.field_146297_k.field_71466_p;
            this.getClass();
            this.d(field_71466_p, "Enemy: Player123-Distance to goal: 17.2-Enemy distance to goal: 16.3-Blocks: 98");
            this.miX = miX;
            this.miY = miY;
            this.maX = maX;
            this.maY = maY;
            BridgeInfo.hudX = miX;
            BridgeInfo.hudY = miY;
            final ScaledResolution res = new ScaledResolution(this.field_146297_k);
            final int x = res.func_78326_a() / 2 - 84;
            final int y = res.func_78328_b() / 2 - 20;
            Utils.HUD.drawColouredText("Edit the HUD position by dragging.", '-', x, y, 2L, 0L, true, this.field_146297_k.field_71466_p);
            try {
                this.func_146269_k();
            }
            catch (IOException ex) {}
            super.func_73863_a(mX, mY, pt);
        }
        
        private void d(final FontRenderer fr, final String t) {
            final int x = this.miX;
            int y = this.miY;
            final String[] var5 = t.split("-");
            final int var6 = var5.length;
            for (final String s : var5) {
                fr.func_175065_a(s, (float)x, (float)y, BridgeInfo.rgb, true);
                y += fr.field_78288_b + 2;
            }
        }
        
        protected void func_146273_a(final int mX, final int mY, final int b, final long t) {
            super.func_146273_a(mX, mY, b, t);
            if (b == 0) {
                if (this.d) {
                    this.aX = this.laX + (mX - this.lmX);
                    this.aY = this.laY + (mY - this.lmY);
                }
                else if (mX > this.miX && mX < this.maX && mY > this.miY && mY < this.maY) {
                    this.d = true;
                    this.lmX = mX;
                    this.lmY = mY;
                    this.laX = this.aX;
                    this.laY = this.aY;
                }
            }
        }
        
        protected void func_146286_b(final int mX, final int mY, final int s) {
            super.func_146286_b(mX, mY, s);
            if (s == 0) {
                this.d = false;
            }
        }
        
        public void func_146284_a(final GuiButton b) {
            if (b == this.rp) {
                this.aX = (BridgeInfo.hudX = 5);
                this.aY = (BridgeInfo.hudY = 70);
            }
        }
        
        public boolean func_73868_f() {
            return false;
        }
    }
}
