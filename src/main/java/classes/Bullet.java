package classes;

public class Bullet extends Block
{
    private int velocityY;
    private boolean active=true;

    public Bullet(int x, int y, int width, int height, int velocityY)
    {
        super(x, y, width, height, null);
        this.velocityY = velocityY;
    }

    @Override
    public void update()
    {
        y+=velocityY;
    }

    public boolean isActive()
    {
        return active;
    }
}
