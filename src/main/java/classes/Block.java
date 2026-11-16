package classes;

import java.awt.*;

abstract class Block
{
    public int x;
    public int y;
    public int width;
    public int height;
    Image img;

    boolean alive = true;
    Block(int x, int y, int width, int height, Image img)
    {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.img = img;
    }
    public abstract void update();
    public void draw(Graphics g)
    {
        if(img!=null)
            g.drawImage(img, x, y, width, height, null);
    }
    public Rectangle getBounds()
    {
        return new Rectangle(x, y, width, height);
    }
}
