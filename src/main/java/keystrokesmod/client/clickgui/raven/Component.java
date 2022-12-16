package keystrokesmod.client.clickgui.raven;

public interface Component
{
    void draw();
    
    void update(final int p0, final int p1);
    
    void mouseDown(final int p0, final int p1, final int p2);
    
    void mouseReleased(final int p0, final int p1, final int p2);
    
    void keyTyped(final char p0, final int p1);
    
    void setComponentStartAt(final int p0);
    
    int getHeight();
    
    int getY();
}
