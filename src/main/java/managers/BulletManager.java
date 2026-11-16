package managers;

import classes.Alien;
import classes.Bullet;

import java.awt.*;
import java.util.ArrayList;

public class BulletManager
{
    ArrayList<Bullet> bullets;
    int bulletVelocity=-10;

    public BulletManager()
    {
        bullets = new ArrayList<>();
    }

    public ArrayList<Bullet> getBullets()
    {
        return bullets;
    }

    public void shoot(int x, int y)
    {
        bullets.add(new Bullet(x,y,5,15, bulletVelocity));
    }

    public void updateBullets()
    {
        bullets.removeIf(bullet-> !bullet.isActive());
        for(Bullet bullet: bullets)
        {
            bullet.update();
        }
    }

    public void drawBullets(Graphics g)
    {
        g.setColor(Color.YELLOW);
        for(Bullet bullet: bullets)
        {
            g.fillRect(bullet.x, bullet.y, bullet.width, bullet.height);
        }
    }

    public boolean checkCollisions(ArrayList<Alien> aliens, GameManager game)
    {
        boolean collision = false;
        ArrayList<Bullet> bulletsToRemove = new ArrayList<>();
        ArrayList<Alien> aliensToRemove = new ArrayList<>();

        for (Bullet bullet : bullets)
        {
            for (Alien alien : aliens)
            {
                if (bullet.x < alien.x + alien.width &&
                        bullet.x + bullet.width > alien.x &&
                        bullet.y < alien.y + alien.height &&
                        bullet.y + bullet.height > alien.y)
                {
                    bulletsToRemove.add(bullet);
                    aliensToRemove.add(alien);
                    collision = true;
                    game.increaseScore();
                    break;
                }
            }
        }
        bullets.removeAll(bulletsToRemove);
        aliens.removeAll(aliensToRemove);

        return collision;
    }
}
