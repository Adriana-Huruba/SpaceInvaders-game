package classes;

import java.awt.*;

public class Ship extends Block
{
    private int velocityX;

    public Ship(int x, int y, int width, int height, Image image, int velocityX)
    {
        super(x, y, width, height, image);
        this.velocityX=velocityX;
    }

    public int getX()
    {
        return x;
    }

    public int getY()
    {
        return y;
    }

    public int getWidth()
    {
        return width;
    }

    public void moveLeft()
    {
        if(this.x - this.velocityX >= 0)
            this.x -= this.velocityX;
    }
    public void moveRight(int boardWidth)
    {
        if( this.x + this.width +velocityX < boardWidth)
            this.x += this.velocityX;
    }

    @Override
    public void update() {}
}
