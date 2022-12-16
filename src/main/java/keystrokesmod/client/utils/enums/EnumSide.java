package keystrokesmod.client.utils.enums;

import net.minecraft.client.gui.*;

public enum EnumSide
{
    LEFT, 
    RIGHT;
    
    public static EnumSide getSide(final int x, final ScaledResolution sr) {
        if (sr.func_78326_a() / 2.0f < x) {
            return EnumSide.RIGHT;
        }
        return EnumSide.LEFT;
    }
}
