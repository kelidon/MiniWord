import javax.swing.*;
import java.awt.*;
import java.awt.Color;
import java.awt.event.*;
import java.awt.image.BufferedImage;

public class Word extends JFrame {

    private Word() {
        super("Word");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        BufferedImage textImage = new BufferedImage(1, 1, 6);
        panel = new TextPanel(textImage);
        add(new JScrollPane(panel));
        rePaint();


        JTextField textField = new JTextField(text);
        textField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                text = textField.getText();
                rePaint();
            }
        });

        JSlider lightSlider = new JSlider(0, 360);
        lightSlider.addChangeListener(e -> {
            light = lightSlider.getValue();
            rePaint();
        });

        JSlider depthSlider = new JSlider(1, 20);
        depthSlider.addChangeListener(e -> {
            depth = depthSlider.getValue();
            rePaint();
        });

        JSlider sizeSlider = new JSlider(50, 160);
        sizeSlider.addChangeListener(e -> {
            SIZE = sizeSlider.getValue();
            rePaint();
        });

        JButton colourButton = new JButton("colour");
        colourButton.addActionListener(e -> {
            Color temp = JColorChooser.showDialog(null, "colour", colour);
            colour = temp == null ? colour : temp;
            rePaint();
        });
        
        rePaint();

        JPanel toolsPanel = new JPanel(new GridLayout(8, 2));
        toolsPanel.add(textField);
        toolsPanel.add(new JLabel("    light"));
        toolsPanel.add(lightSlider);
        toolsPanel.add(new JLabel("    size"));
        toolsPanel.add(sizeSlider);
        toolsPanel.add(new JLabel("    depth"));
        toolsPanel.add(depthSlider);
        toolsPanel.add(colourButton);
        add(toolsPanel, BorderLayout.WEST);
    }

    private void rePaint() {
        panel.setImage(addDepth());
    }

    private BufferedImage addDepth() {
        BufferedImage text = new BufferedImage(SIZE * 6 + SIZE * this.text.length(), SIZE * 2, 6);
        Graphics2D g = text.createGraphics();
        g.setColor(colour);
        g.setFont(new Font("Arial", Font.BOLD, SIZE));
        g.drawString(this.text, 0, SIZE);
        g.dispose();
        addSideColours(text, colour, light);


        int size = text.getHeight();
        BufferedImage image = new BufferedImage(text.getWidth(null), size * 2, 6);
        g = image.createGraphics();
        for (double i = 0; i < depth; i += 0.2) {
            g.drawImage(text, size + (int) (i * cos), size + (int) (i * sin), null);
        }
        g.dispose();
        return image;
    }

    private void addSideColours(BufferedImage img, Color color, double light) {
        int width = img.getWidth();
        int height = img.getHeight();
        int[][] pixels = new int[height][width];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                pixels[i][j] = img.getRGB(j, i);
            }
        }

        Color darkCol = color.darker();
        Color brightCol = color.brighter();
        int top = shadow(brightCol, darkCol, light / 360);
        int right = shadow(brightCol, darkCol, Math.abs(90 - light) / 360);
        int bottom = shadow(brightCol, darkCol, Math.abs(180 - light) / 360);
        int left = shadow(brightCol, darkCol, Math.abs(270 - light) / 360);

        for (int i = 1; i < pixels[0].length - 1; i++) {
            for (int j = 1; j < pixels.length - 1; j++) {
                if (pixels[j][i] == 0) {
                    if (pixels[j][i - 1] == color.getRGB()) {
                        img.setRGB(i, j, left);
                    }
                    if (pixels[j][i + 1] == color.getRGB()) {
                        img.setRGB(i, j, right);
                    }
                    if (pixels[j - 1][i] == color.getRGB()) {
                        img.setRGB(i, j, top);
                    }
                    if (pixels[j + 1][i] == color.getRGB()) {
                        img.setRGB(i, j, bottom);
                    }
                }
            }
        }
    }

    private static int shadow(Color brightColor, Color darkColor, double part) {
        int redPart = (int) (brightColor.getRed() * part
                + darkColor.getRed() * (1 - part)) / 2;
        int greenPart = (int) (brightColor.getGreen() * part
                + darkColor.getGreen() * (1 - part)) / 2;
        int bluePart = (int) (brightColor.getBlue() * part
                + darkColor.getBlue() * (1 - part)) / 2;
        return new Color(redPart, greenPart, bluePart).getRGB();
    }

    public static void main(String[] args) {
        Word word = new Word();
        word.setSize(1000, 500);
        word.setLocationRelativeTo(null);
        word.setVisible(true);
    }


    private static TextPanel panel;
    private final double ANGLE = 4.223696789826278;
    private double cos = Math.cos(ANGLE);
    private double sin = Math.sin(ANGLE);
    private int depth = 10;
    private String text = "Text";
    private Color colour = Color.WHITE;
    private double light = 180;
    private int SIZE = 100;
}