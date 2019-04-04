import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;

public class grafik extends Canvas {

    int x,y;
    int x1 = 400;
    int y1 = 300;
    double angle = 0;
    int width = 800;
    int height= 600;
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
        x1 = 300;
        g.fillRect(x1,300, 50,100);
        g.drawLine(0,400,1000,400);
    }



    private class KL implements KeyListener {
        @Override
        public void keyTyped(KeyEvent keyEvent) {
            if (keyEvent.getKeyChar()=='a') {
                x1-=20;
            } else if (keyEvent.getKeyChar()=='d') {
                x1+=20;
                getGraphics().drawRect(100,100,200,200);
            }

        }

        @Override
        public void keyPressed(KeyEvent keyEvent) {


        }

        @Override
        public void keyReleased(KeyEvent keyEvent) {

        }
    }



}
