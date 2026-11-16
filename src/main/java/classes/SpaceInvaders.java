package classes;

import managers.AlienManager;
import managers.BulletManager;
import managers.GameManager;

import javax.sound.sampled.AudioInputStream; //package for capture, mixing and playback of audio(sampled)
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class SpaceInvaders extends JPanel implements ActionListener, KeyListener
{
    private Ship ship;
    private AlienManager alienManager;
    private BulletManager bulletManager;
    GameManager gameManager;

    private Timer gameLoop;
    private Image heartImage, backgroundImage;
    private boolean canShoot;

    int tileSize;
    int rows;
    int columns;
    int boardWidth;
    int boardHeight;

    // constructor
    public SpaceInvaders()
    {
        // board settings
        tileSize = 32;
        rows = 16;
        columns = 16;
        boardWidth = tileSize * columns; // 32*16=512 px
        boardHeight = tileSize * rows; // 512
        setPreferredSize(new Dimension(boardWidth, boardHeight));
        setFocusable(true);
        addKeyListener(this);

        backgroundImage = new ImageIcon(getClass().getResource("/background.png")).getImage();
        heartImage = new ImageIcon(getClass().getResource("/heart.png")).getImage();
        Image shipImage= new ImageIcon(getClass().getResource("/ship.png")).getImage();

        int shipWidth = tileSize * 2;
        int shipHeight = tileSize * 2;
        int shipX = tileSize * columns / 2 - tileSize;
        int shipY = boardHeight - tileSize * 2;
        int shipVelocityX = tileSize / 4;

        ship = new Ship(shipX, shipY, shipWidth, shipHeight, shipImage, shipVelocityX);
        alienManager=new AlienManager(tileSize,boardWidth);
        bulletManager=new BulletManager();
        gameManager=new GameManager();
        gameLoop = new Timer(1000 / 60, this); // 60 frames(actions=this) per second

        gameLoop.start();
    }

    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        g.drawImage(backgroundImage, 0, 0, boardWidth, boardHeight, null);    //background
        ship.draw(g);
        alienManager.drawAliens(g);
        bulletManager.drawBullets(g);
        gameManager.drawInfo(g,boardWidth, heartImage);
    }

    public void playSound(String filePath)
    {
        try
        {
            AudioInputStream audiostream = AudioSystem.getAudioInputStream(getClass().getResource("/" + filePath));
            Clip clip = AudioSystem.getClip();
            clip.open(audiostream);
            clip.start();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void resetGame()
    {
        gameManager = new GameManager();  // Resetează scorul și viețile
        ship = new Ship(boardWidth / 2 - tileSize, boardHeight - tileSize * 2, tileSize * 2, tileSize * 2,
                new ImageIcon(getClass().getResource("/ship.png")).getImage(), tileSize / 4);
        alienManager = new AlienManager(tileSize, boardWidth); // Resetează extratereștrii
        bulletManager = new BulletManager(); // Resetează gloanțele
        gameLoop.start(); // Repornește bucla jocului
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
       if(gameManager.getGameOver())
       {
           gameLoop.stop();
           return;
       }

       alienManager.moveAliens(boardWidth);
       alienManager.updateAliens(ship, gameManager);

       bulletManager.updateBullets();
       bulletManager.checkCollisions(alienManager.getAliens(), gameManager);

       if(alienManager.getAliens().isEmpty())
           alienManager.createAliens();
       repaint();
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e)
    {
        if(gameManager.getGameOver())
        {
            resetGame();
            return;
        }

        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT:
                ship.moveLeft();
                break;

            case KeyEvent.VK_RIGHT:
                ship.moveRight(boardWidth);
                break;

            case KeyEvent.VK_SPACE:
                if (canShoot) {
                    bulletManager.shoot(ship.getX() + ship.getWidth() / 2, ship.getY());
                    playSound("shootSound.wav");
                    canShoot = false; // one bullet at a time
                }
                break;

            default:
                break;
        }
        /*if (e.getKeyCode() == KeyEvent.VK_LEFT)
        {
            ship.moveLeft();
        } else if (e.getKeyCode() == KeyEvent.VK_RIGHT)
        {
            ship.moveRight(boardWidth);
        }
        else if(e.getKeyCode() == KeyEvent.VK_SPACE && canShoot)
        {
            bulletManager.shoot(ship.getX()+ ship.getWidth()/2, ship.getY());
            playSound("shootSound.wav");
            canShoot=false; //one bullet at a time
        }*/
    }

    @Override
    public void keyReleased(KeyEvent e)
    {
        if(e.getKeyCode()==KeyEvent.VK_SPACE)
            canShoot=true;
    }
}
