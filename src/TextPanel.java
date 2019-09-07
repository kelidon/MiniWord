import javax.swing.*;
import java.awt.*;

public class TextPanel extends JPanel {
    private Image img;

    TextPanel(Image img){
        setImage(img);
    }

    void setImage(Image img){
        this.img = img;
        setPreferredSize(new Dimension(img.getWidth(null),img.getHeight(null)));
        this.repaint();
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        g.drawImage(img,-80,-100,null);
    }
}