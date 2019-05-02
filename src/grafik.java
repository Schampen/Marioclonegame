import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;

public class grafik extends Canvas implements Runnable {

    private int mariox = 300, marioy = 500, blockX = 500, blockY = 400, groundY = 600, block2Y = 400, block2X = 350;

    private boolean isJumping = false;
    private boolean isFalling = false;
    private boolean onBlock = false;
    private boolean onGround = true;
    private int jumpEnergy = 1;
    private boolean energyLeft = false,energyRight = false;


    private Thread thread;
    private boolean running = false;

    void Grafik() {
        setSize(800,600);
        JFrame frame = new JFrame("Grafik");
        frame.add(this);
        frame.addKeyListener(new KL());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    public synchronized void start() {
        running = true;
        thread = new Thread(this);
        thread.start();
    }

    private synchronized void stop() {
        running = false;
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        double ns = 1000000000.0 / 30.0;
        double delta = 0;
        long lastTime = System.nanoTime();

        while (running) {
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;

            while(delta >= 1) {
                // Uppdatera koordinaterna
                update();
                // Rita ut bilden med updaterad data
                render();
                delta--;
            }
        }
        stop();
    }

    private void render() {
        BufferStrategy bs = getBufferStrategy();
        if (bs == null) {
            createBufferStrategy(3);
            return;
        }
        Graphics g = bs.getDrawGraphics();
        // Rita ut den nya bilden
        draw(g);
        g.dispose();
        bs.show();
    }

    private void update() {
        // Uppdaterar gubbens koordinater när man rör på sig
        if (jumpEnergy == 0) {
            isJumping = false;
            isFalling = true;
            marioy+=10;
        } else if (isJumping) {
            marioy-=10;
            jumpEnergy--;
            onGround = false;
        }
        if (marioy+100 == groundY) {
            jumpEnergy = 1;
            isFalling = false;
            onGround=true;
        }
        if (marioy <= blockY+50 && marioy+100>=blockY) {
            if (mariox+50 == blockX) {
                energyRight = false;
            } else if (mariox == blockX+50) {
                energyLeft = false;
            }
        } if (marioy <= block2Y+50 && marioy+100>=block2Y) {
            if (mariox+50 == block2X) {
                energyRight = false;
            } else if (mariox == block2X+50) {
                energyLeft = false;
            }
        } if (energyRight) {
            mariox+=10;
            if (!onGround && !isJumping) {
                jumpEnergy=0;
            }
        } else if (energyLeft) {
            if (mariox == 0) {
                energyLeft = false;
            } else {
                mariox -= 10;
            }
            if (!onGround && !isJumping) {
                jumpEnergy=0;
            }
        }
        // Kollar om gubben och ett objekt kolliderar
        if (mariox+50>blockX && mariox<blockX+50) {
            if (marioy+100==blockY && !isJumping) {
                jumpEnergy=1;
                isFalling=false;
                onBlock=true;
            } else if (blockY+50==marioy) {
                jumpEnergy=0;
                blockY = blockY+1000;
            }
        } else if (mariox+50<=block2X || mariox>=block2X+50 || !onGround) {
            onBlock=false;
        } if (mariox+50>block2X && mariox<block2X+50) {
            if (marioy+100==block2Y && !isJumping) {
                jumpEnergy=1;
                isFalling=false;
                onBlock=true;
            } else if (block2Y+50==marioy) {
                jumpEnergy=0;
                block2Y = block2Y+1000;
            }
        } else if (mariox+50<=block2X || mariox>=block2X+50 || !onGround) {
            onBlock=false;
        }

    }

    private void draw(Graphics g) {
        // Ritar ut bilden
        drawBackground(g);
        drawGround(g);
        drawMario(g);
        drawBlock(g, blockX, blockY);
        drawBlock(g,block2X,block2Y);
    }

    private void drawBlock(Graphics g, int x, int y) {
        g.setColor(new Color(0x8B4423));
        g.fillRect(x,y,50,50);
        g.setColor(new Color(0x00000));
        g.fillRect(x,y+47,50,3);
        g.fillRect(x,y+11,50,3);
        g.fillRect(x,y+23,50,3);
        g.fillRect(x,y+36,50,3);
        g.fillRect(x+21,y,3,12);
        g.fillRect(x+47,y,3,12);
        g.fillRect(x+10,y+11,3,12);
        g.fillRect(x+39,y+11,3,12);
        g.fillRect(x+21,y+24,3,12);
        g.fillRect(x+47,y+24,3,12);
        g.fillRect(x+10,y+37,3,12);
        g.fillRect(x+39,y+37,3,12);
    }

    private void drawBackground(Graphics g) {
        g.setColor(new Color(0x25AAFF));
        g.fillRect(0, 0, 10000, 600);
        g.setColor(new Color(0xFFFFFF));
        g.fillRect(100,200,100,50);
        g.fillRect(600,180,90,50);
        g.fillRect(280,240,130,60);
        g.fillRect(940,340,100,50);
        g.fillRect(1320,80,120,80);
        g.fillRect(1650,360,170,30);
    }

    private void drawGround(Graphics g) {
        g.setColor(new Color(0x8B4513));
        g.fillRect(0,600,2000,600);
        g.setColor(new Color(0xC7430F));
        g.fillRect(0,600,2000,50);
        g.setColor(new Color(0x000000));
        for (int i = 0 ; i < 50 ; i++) {
            g.fillRect(i*50,600,3,50);
        }
    }

    private void drawMario(Graphics g) {
        g.setColor(new Color(0x000000));
        g.fillRect(mariox, marioy, 50,100);
    }


    private class KL implements KeyListener {
        @Override
        public void keyTyped(KeyEvent e) {

        }

        @Override
        public void keyPressed(KeyEvent keyEvent) {
            if (keyEvent.getKeyChar()=='a') {
                    energyLeft = true;

            } else if (keyEvent.getKeyChar()=='d') {
                    energyRight =true;


            } else if (keyEvent.getKeyChar()=='w'){

                if (!isJumping && !isFalling) {
                    isJumping = true;
                    jumpEnergy = 25;
                }
            }

        }

        @Override
        public void keyReleased(KeyEvent keyEvent) {
            if (keyEvent.getKeyChar()=='a') {
                energyLeft = false;
            } else if (keyEvent.getKeyChar()=='d') {
                energyRight = false;
            }
        }
    }

}
