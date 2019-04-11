import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;

public class grafik extends Canvas implements Runnable {

    private int mariox = 300;
    private int marioy = 500;

    private boolean isJumping = false;
    private boolean isFalling = false;
    private int jumpEnergy = 1;

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
        if (jumpEnergy == 0) {
            isJumping = false;
            isFalling = true;
            marioy+=10;
        } else if (isJumping) {
            marioy-=10;
            jumpEnergy--;
        }
        if (marioy == 500) {
            jumpEnergy = 1;
            isFalling = false;
        }

    }

    private void draw(Graphics g) {
        drawBackground(g);
        drawGround(g);
        drawMario(g);
    }

    private void drawBackground(Graphics g) {
        g.setColor(new Color(0xffffff));
        g.fillRect(0, 0, 10000, 600);
    }

    private void drawGround(Graphics g) {
        g.setColor(new Color(0x8B4513));
        g.fillRect(0,600,2000,600);
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
                if (mariox > 0) {
                    mariox-=5;
                } else if (mariox == 0) {
                }
            } else if (keyEvent.getKeyChar()=='d') {
                mariox+=5;
            } else if (keyEvent.getKeyChar()=='w'){
                if (!isJumping && !isFalling) {
                    isJumping = true;
                    jumpEnergy = 10;
                }
            }

        }

        @Override
        public void keyReleased(KeyEvent keyEvent) {

        }
    }

}
