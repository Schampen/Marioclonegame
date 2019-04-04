import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;

public class grafik extends Canvas {

    int x1 = 300;
    int y1 = 300;
    BufferStrategy bs;

    public void Grafik() {
        setSize(800,600);
        JFrame frame = new JFrame("Grafik");
        frame.add(this);
        frame.addKeyListener(new KL());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    public void paint(Graphics g) {
        bs = getBufferStrategy();
        if (bs == null) {
            createBufferStrategy(2);
            return;
        }
        draw(g);
        g.dispose();
        bs.show();
        repaint();
    }

    public void draw(Graphics g) {
        g.fillRect(x1,y1, 50,100);
        g.drawLine(0,400,5000,400);
    }



    private class KL implements KeyListener {
        @Override
        public void keyTyped(KeyEvent e) {

        }

        @Override
        public void keyPressed(KeyEvent keyEvent) {
            if (keyEvent.getKeyChar()=='a') {
                x1-=5;
                repaint();
            } else if (keyEvent.getKeyChar()=='d') {
                x1+=5;
                repaint();
            } else if (keyEvent.getKeyChar()=='w'){
                for (int i = 0 ; i < 10 ; i++) {
                    y1-=10;
                }
                for (int i = 0 ; i < 10 ; i++) {
                    y1+=10;
                }
            }

        }

        @Override
        public void keyReleased(KeyEvent keyEvent) {

        }
    }



}
