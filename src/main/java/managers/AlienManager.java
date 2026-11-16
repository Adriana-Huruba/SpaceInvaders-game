package managers;

import classes.Alien;
import classes.Ship;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class AlienManager
{
    ArrayList<Alien> aliens;
    int alienVelocityX;
    int tileSize, boardWidth;
    boolean movingRight=true;
    int alienRows=2, alienColumns=3;

    public AlienManager(int tileSize, int boardWidth)
    {
        this.tileSize = tileSize;
        this.boardWidth = boardWidth;
        this.alienVelocityX=1;
        aliens = new ArrayList<>();
        createAliens();
    }

    public ArrayList<Alien> getAliens()
    {
        return aliens;
    }

    public void createAliens()
    {
        aliens.clear();
        Random random = new Random();

        ArrayList<Image> aliensImages = new ArrayList<>();

        aliensImages.add(new ImageIcon(getClass().getResource("/alien.png")).getImage());
        aliensImages.add(new ImageIcon(getClass().getResource("/alien-orange.png")).getImage());
        aliensImages.add(new ImageIcon(getClass().getResource("/alien-magenta.png")).getImage());
        aliensImages.add(new ImageIcon(getClass().getResource("/alien-yellow.png")).getImage());

        for (int row = 0; row < alienRows; row++)
        {
            for (int col = 0; col < alienColumns; col++)
            {
                Image alienImage=aliensImages.get(random.nextInt(aliensImages.size()));
                int x = col * tileSize + 50;
                int y = row * tileSize + 50;
                aliens.add(new Alien(x,y, tileSize,tileSize, alienImage, alienVelocityX));
            }
        }
    }

    public void updateAliens(Ship ship, GameManager gameManager)
    {
        ArrayList<Alien> aliensToRemove = new ArrayList<>();
        boolean reachedShip=false;

        for (Alien alien : aliens)
        {
            alien.update();
            if (alien.y + alien.height >= ship.getY())
            {
                reachedShip=true;
                aliensToRemove.add(alien);
            }
        }
        aliens.removeAll(aliensToRemove);

        if(reachedShip)
        {
            gameManager.decreaseLife();
            if (gameManager.getGameOver())
                return;
        }

        if(aliens.isEmpty())
            nextLevel(gameManager);
    }

    public void nextLevel(GameManager gameManager)
    {
        gameManager.addScore(alienColumns * alienRows * 100);
        alienColumns = Math.min(alienColumns + 1, 6);
        alienRows = Math.min(alienRows + 1, 6);
        alienVelocityX = Math.min(alienVelocityX + 1, 5);

        createAliens();
    }

    public void drawAliens(Graphics g)
    {
        for(Alien alien:aliens)
        {
            alien.draw(g);
        }
    }

    public void moveAliens(int boardWidth)
    {
        boolean isAtEdge=false;

        for (Alien alien : aliens)
        {
            if ((movingRight && (alien.x + alien.width + alienVelocityX >= boardWidth)) ||
                    (!movingRight && (alien.x - alienVelocityX <= 0)))
            {
                isAtEdge = true;
                break;
            }
        }

        if (isAtEdge)
        {
            movingRight = !movingRight;
            for (Alien alien : aliens)
            {
                alien.y += tileSize;
            }
        }
        for (Alien alien : aliens)
        {
            alien.x +=movingRight ? tileSize/10 : -tileSize/10;
        }

    }
}
