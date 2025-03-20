package classes;

import javax.sound.sampled.AudioInputStream; //package for capture, mixing and playback of audio(sampled)
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Random;

public class SpaceInvaders extends JPanel implements ActionListener, KeyListener
{
    private boolean gameStarted=false;
    public class Block {
        public int x;
        public int y;
        public int width;
        public int height;
        Image img;

        boolean alive = true; // for aliens //eventual o clasa abstracta pt aliens/bullets si apoi mostenire
        boolean usedbullets = false; // bullets

        Block(int x, int y, int width, int height, Image img) // constructor
        {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.img = img;
        }
    }

    // board settings
    int tileSize = 32;
    int rows = 16; // 16
    int columns = 16;
    int boardWidth = tileSize * columns; // 32*16=512 px
    int boardHeight = tileSize * rows; // 512


    Image shipImage;
    Image alienImage;
    Image orangeAlienImage;
    Image alienMagentaImage;
    Image alienYellowImage;
    Image heartImage;
    ArrayList<Image> alienImg;
    Image backgroundImage;

    int shipWidth = tileSize * 2; // -64 px //shipul de intinde pe 2 patratele, dar inaltimea este de 1 patr
    int shipHeight = tileSize * 2; // 32 px
    int shipX = tileSize * columns / 2 - tileSize; // 1*16/2-1=7, nava incepe de
    // la patr 7, e intinsa pe 7 si 8
    // int shipX = (boardWidth / 2) - (shipWidth / 2);
    int shipY = boardHeight - tileSize * 2; // incep de jos(boarheight) si tre sa urc cu 2 patr(2*sizetile)
    int shipVelocityX = tileSize / 4; // pt miscare stg si dreapta

    Block ship;
    ArrayList<Block> aliens;
    int alienWidth = tileSize;
    int alienHeight = tileSize;
    int alienX = tileSize * 2;
    int alienY = tileSize;

    int alienRows = 2;
    int alienColumns = 3;
    int alientCount = 0; // aliens to defeat
    int alienVelocityX = 1;

    ArrayList<Block> bullets;
    int bulletWidth = tileSize / 8;
    int bulletHeight = tileSize / 2;
    int bulletVelocityY = -10;

    Timer gameLoop;
    int score = 0;
    int lives=3;
    boolean gameOver = false;

    // constructor
    public SpaceInvaders()
    {
        setPreferredSize(new Dimension(boardWidth, boardHeight));
        //setBackground(Color.BLACK);
        setFocusable(true);
        addKeyListener(this);

        shipImage = new ImageIcon(getClass().getResource("/ship.png")).getImage();
        alienImage = new ImageIcon(getClass().getResource("/alien.png")).getImage();
        orangeAlienImage = new ImageIcon(getClass().getResource("/alien-orange.png")).getImage();
        alienMagentaImage = new ImageIcon(getClass().getResource("/alien-magenta.png")).getImage();
        alienYellowImage = new ImageIcon(getClass().getResource("/alien-yellow.png")).getImage();
        backgroundImage = new ImageIcon(getClass().getResource("/background.png")).getImage();
        heartImage = new ImageIcon(getClass().getResource("/heart.png")).getImage();
        alienImg = new ArrayList<Image>();
        alienImg.add(alienImage);
        alienImg.add(orangeAlienImage);
        alienImg.add(alienMagentaImage);
        alienImg.add(alienYellowImage);

        ship = new Block(shipX, shipY, shipWidth, shipHeight, shipImage);
        aliens = new ArrayList<Block>();
        bullets = new ArrayList<Block>();

        gameLoop = new Timer(1000 / 60, this); // 60 frames(actions=this) per second
        createAliens();
        gameLoop.start();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g)
    {
        //background
        g.drawImage(backgroundImage, 0, 0, boardWidth, boardHeight, null);
        // ship
        g.drawImage(ship.img, ship.x, ship.y, ship.width, ship.height, null);

        // aliens
        for (int i = 0; i < aliens.size(); i++)
        {
            Block alien = aliens.get(i);
            if (alien.alive)
            {
                g.drawImage(alien.img, alien.x, alien.y, alien.width, alien.height, null);
            }
        }

        // bullets
        g.setColor(Color.YELLOW);
        for (int i = 0; i < bullets.size(); i++)
        {
            Block bullet = bullets.get(i);
            if (!bullet.usedbullets)
            {
                g.fillRect(bullet.x, bullet.y, bullet.width, bullet.height); // sau g.fillRect
            }
        }

        // score
        g.setColor(Color.WHITE);
        g.setFont(new Font("Serif", Font.PLAIN, 30));
        if (gameOver)
        {
            g.drawString("Game Over: " + String.valueOf(score), boardWidth / 3, boardHeight / 2);
        } else {
            g.drawString("Score:" + score, 10, 35);
            int heartSize=32;
            for(int i=0;i<lives;i++)
            {
                int xPosition=boardWidth - 70 - (i * (heartSize + 5));
                g.drawImage(heartImage,xPosition, 10, heartSize, heartSize, null);
            }
        }
    }

    public void move()
    {
        // aliens
        for (int i = 0; i < aliens.size(); i++)
        {
            Block alien = aliens.get(i);
            if (alien.alive)
            {
                alien.x += alienVelocityX;

                if (alien.x + alien.width >= boardWidth || alien.x <= 0) // daca atinge marginile
                {
                    alienVelocityX *= -1.45;
                    alien.x += alienVelocityX * 4;
                    // cobor cu un tile, un rand
                    for (int j = 0; j < aliens.size(); j++)
                    {
                        aliens.get(j).y += alienHeight;
                    }
                }
                if (alien.y >= ship.y)
                {
                    lives--;
                    if(lives<=0)
                        gameOver = true;
                    resetAliensAndShipPosition();
                }
            }
        }

        // bullets
        for (int i = 0; i < bullets.size(); i++)
        {
            Block bullet = bullets.get(i);
            bullet.y += bulletVelocityY;
            for (int j = 0; j < aliens.size(); j++)
            {
                Block alien = aliens.get(j);
                if (!bullet.usedbullets && alien.alive && checkCollision(bullet, alien))
                {
                    bullet.usedbullets = true;
                    alien.alive = false;
                    alientCount--;
                    score += 100;
                }
            }
        }
        // clear bullets
        while (bullets.size() > 0 && (bullets.get(0).usedbullets || bullets.get(0).y < 0)) // cat timp mai avem bullets
        // si daca a fost fol primul
        // bullet sau primul bullet a
        // iesit din ecran(y-negativ)
        {
            bullets.remove(0); // arraylist intr-un linked list masi eficient?
        }

        // next level
        if (alientCount == 0) {
            // bonus points
            score += alienColumns * alienRows * 100;
            alienColumns = Math.min(alienColumns + 1, columns / 2 - 2); // max 3 col de aliens
            alienRows = Math.min(alienRows + 1, rows - 6); // max 10 randuri
            aliens.clear();
            bullets.clear();
            alienVelocityX = 2;
            createAliens();
        }
    }

    public void createAliens()
    {
        Random random = new Random();
        for (int row = 0; row < alienRows; row++)
        {
            for (int col = 0; col < alienColumns; col++)
            {
                int randomImageIndex = random.nextInt(alienImg.size()); // index de la 0 la 3(4 imagini)
                Block alien = new Block(
                        alienX + col * alienWidth,
                        alienY + row * alienHeight,
                        alienWidth,
                        alienHeight,
                        alienImg.get(randomImageIndex));
                aliens.add(alien);
            }
        }
        alientCount = aliens.size();
    }

    public void playSound(String filePath)
    {
        try {
            AudioInputStream audiostream = AudioSystem.getAudioInputStream(getClass().getResource("/" + filePath));
            Clip clip = AudioSystem.getClip();
            clip.open(audiostream);
            clip.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean checkCollision(Block a, Block b)
    {
        return a.x < b.x + b.width &&
                a.x + a.width > b.x &&
                a.y < b.y + b.height &&
                a.y + a.height > b.y;
    }

    public void resetAliensAndShipPosition()
    {
        ship.x = shipX; // Pune nava la poziția inițială
        ship.y = shipY; // Pune nava la poziția inițială


        aliens.clear();
        createAliens(); // Crează din nou toți extratereștrii
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        move();
        repaint(); // drawing again the comp
        if (gameOver)
            gameLoop.stop();
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e)
    {
        if (e.getKeyCode() == KeyEvent.VK_LEFT && ship.x - shipVelocityX >= 0)
        {
            ship.x -= shipVelocityX;
        } else if (e.getKeyCode() == KeyEvent.VK_RIGHT && ship.x + ship.width + shipVelocityX <= boardWidth)

        {
            ship.x += shipVelocityX;
        }
    }

    @Override
    public void keyReleased(KeyEvent e)
    {
        if (gameOver)
        {
            ship.x = shipX;
            aliens.clear();
            bullets.clear();
            score = 0;
            lives=3;
            alienVelocityX = 1;
            alienColumns = 3;
            alienRows = 2;
            gameOver = false;
            createAliens();
            gameLoop.start();
        }
        if (e.getKeyCode() == KeyEvent.VK_SPACE)
        {
            Block bullet = new Block(ship.x + (shipWidth/2) - (bulletWidth / 2), ship.y,
                    bulletWidth, bulletHeight,
                    null);

            bullets.add(bullet);
            playSound("shootSound.wav");
        }
    }

}
