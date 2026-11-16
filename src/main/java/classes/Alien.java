package classes;

import java.awt.*;

public class Alien extends Block
{
    private int velocityX;
    private boolean alive=true;

    public Alien(int x, int y, int width, int height, Image image, int velocityX)
    {
        super(x, y, width, height, image);
        this.velocityX=velocityX;
    }
    public int getX()
    {
        return x;
    }
    public void setX(int x)
    {
        this.x=x;
    }
    public int getY()
    {
        return y;
    }
    public void setY(int y)
    {
        this.y=y;
    }

    @Override
    public void update()
    {
        if(alive)
            x+=velocityX;
    }
}
