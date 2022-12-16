package keystrokesmod.client.clickgui.kv;

public class KvComponent
{
    protected int x;
    protected int y;
    protected int width;
    protected int height;
    protected boolean isHovering;
    
    public void draw(final int mouseX, final int mouseY) {
        this.isHovering = this.isMouseOver(mouseX, mouseY);
    }
    
    public boolean mouseDown(final int x, final int y, final int button) {
        return false;
    }
    
    public void mouseReleased(final int x, final int y, final int button) {
    }
    
    public void setCoords(final int x, final int y) {
        this.x = x;
        this.y = y;
    }
    
    public void setDimensions(final int width, final int height) {
        this.width = width;
        this.height = height;
    }
    
    public boolean isMouseOver(final int x, final int y) {
        return x > this.x && x < this.x + this.width && y > this.y && y < this.y + this.height;
    }
    
    public int getHeight() {
        return this.height;
    }
    
    public int getWidth() {
        return this.width;
    }
    
    public int getY() {
        return this.y;
    }
    
    public int getX() {
        return this.x;
    }
}
