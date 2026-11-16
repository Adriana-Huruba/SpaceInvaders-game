package managers;

import javax.swing.*;
import java.awt.*;

public class GameManager
{
    int score=0;
    int lives=3;
    boolean gameOver=false;

    public void increaseScore()
    {
        score+=50;
    }
    public void decreaseLife()
    {
        lives--;
        if(lives<=0)
            gameOver=true;
    }
    public boolean getGameOver()
    {
        return gameOver;
    }

    public void addScore(int points)
    {
        score+=points;
    }

    public void drawInfo(Graphics g, int boardWidth, Image heartImage)
    {
        g.setColor(Color.WHITE); //serif
        g.setFont(new Font("Comic Sans MS", Font.PLAIN, 30));
        if (gameOver)
        {
            g.drawString("Game Over: " + String.valueOf(score), boardWidth / 3, boardWidth / 2);
        } else
        {
            g.drawString("Score:" + score, 10, 35);
            int heartSize=32;
            for(int i=0;i<lives;i++)
            {
                int xPosition=boardWidth - 70 - (i * (heartSize + 5));
                g.drawImage(heartImage,xPosition, 10, heartSize, heartSize, null);
            }
        }
    }
}
