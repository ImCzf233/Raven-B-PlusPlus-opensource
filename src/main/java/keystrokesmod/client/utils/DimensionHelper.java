package keystrokesmod.client.utils;

import net.minecraft.client.*;

public class DimensionHelper
{
    public static boolean isPlayerInNether() {
        return Utils.Player.isPlayerInGame() && Minecraft.func_71410_x().field_71439_g.field_71093_bK == DIMENSIONS.NETHER.getDimensionID();
    }
    
    public static boolean isPlayerInEnd() {
        return Utils.Player.isPlayerInGame() && Minecraft.func_71410_x().field_71439_g.field_71093_bK == DIMENSIONS.END.getDimensionID();
    }
    
    public static boolean isPlayerInOverworld() {
        return Utils.Player.isPlayerInGame() && Minecraft.func_71410_x().field_71439_g.field_71093_bK == DIMENSIONS.OVERWORLD.getDimensionID();
    }
    
    enum DIMENSIONS
    {
        NETHER(-1), 
        OVERWORLD(0), 
        END(1);
        
        private final int dimensionID;
        
        private DIMENSIONS(final int n) {
            this.dimensionID = n;
        }
        
        public int getDimensionID() {
            return this.dimensionID;
        }
    }
}
