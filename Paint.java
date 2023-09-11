import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

/** A class that allows the user to draw on a blank canvas, change the color of the brush
 * (by random, rgb values, or hex code), fill the canvas, clear the canvas, or erase what has been drawn.
 *
 * @author Elisa Loo
 * @version November 15, 2022
 */

public class Paint extends JComponent implements Runnable {
    Image image; // the canvas
    Graphics2D graphics2D;  // this will enable drawing
    int curX; // current mouse x coordinate
    int curY; // current mouse y coordinate
    int oldX; // previous mouse x coordinate
    int oldY; // previous mouse y coordinate
    JButton clrButton; // a button to clear canvas to white background
    JButton fillButton; // a button to fill the background with the current pen color
    JButton eraseButton; // a button to set the pen color equal to the current background color
    JButton randomButton; // a button to change the pen color to a randomly generated color
    //Hex TextField, Hex Button, R TextField, G TextField, B TextField, RGB Button
    JTextField hexText;
    JButton hexButton;
    JTextField rText;
    JTextField gText;
    JTextField bText;
    JButton rgbButton;
    Paint paint; // variable of the type SimplePaint
    Color backgroundColor;

    public Color getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    /**The Image and Graphics2D fields are specific to our purpose of creating a drawing
     * application. You do not need to use them in every Complex GUI program you write.
     * Keep that in mind for future assignments.
     */


    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Paint());
    }

    /* action listener for buttons */
    ActionListener actionListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == clrButton) {
                paint.clear();
                hexText.setText("#");
                rText.setText("");
                gText.setText("");
                bText.setText("");
            }
            if (e.getSource() == fillButton) {
                String hexString = hexText.getText();
                paint.fill(hexString);
                hexText.setText("#");
                rText.setText("");
                gText.setText("");
                bText.setText("");
            }
            if (e.getSource() == eraseButton) {
                paint.erase();
            }
            if (e.getSource() == randomButton) {
                Color rgbColor = paint.random();
                rText.setText(String.valueOf(rgbColor.getRed()));
                gText.setText(String.valueOf(rgbColor.getGreen()));
                bText.setText(String.valueOf(rgbColor.getBlue()));
                hexText.setText(String.format("#%02x%02x%02x", rgbColor.getRed(),
                        rgbColor.getGreen(), rgbColor.getBlue()));
            }
            if (e.getSource() == hexButton) {
                String hexString = hexText.getText();
                try {
                    Color hexColor = paint.hex(hexString);
                    rText.setText(String.valueOf(hexColor.getRed()));
                    gText.setText(String.valueOf(hexColor.getGreen()));
                    bText.setText(String.valueOf(hexColor.getBlue()));
                } catch (NumberFormatException error) {
                    JOptionPane.showMessageDialog(null, "Not a valid Hex Value" , "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
            if (e.getSource() == rgbButton) {
                if (rText.getText().isEmpty()) {
                    rText.setText("0");
                }
                if (gText.getText().isEmpty()) {
                    gText.setText("0");
                }
                if (bText.getText().isEmpty()) {
                    bText.setText("0");
                }
                try {
                    int redColor = Integer.parseInt(rText.getText());
                    int greenColor = Integer.parseInt(gText.getText());
                    int blueColor = Integer.parseInt(bText.getText());
                    paint.rgb(redColor, greenColor, blueColor);
                    hexText.setText(String.format("#%02x%02x%02x", redColor, greenColor, blueColor));
                } catch (NumberFormatException error) {
                    JOptionPane.showMessageDialog(null, "Not a valid RGB Value", "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    };

    /* set up paint colors */
    public void clear() {
        graphics2D.setPaint(Color.white);
        setBackgroundColor(Color.white);
        graphics2D.fillRect(0, 0, getSize().width, getSize().height);
        graphics2D.setPaint(Color.black);
        repaint();
    }

    public void fill(String hexString) {
        graphics2D.setPaint(Color.getColor(hexString));
        setBackgroundColor(graphics2D.getColor());
        graphics2D.fillRect(0, 0, getSize().width, getSize().height);
        graphics2D.setPaint(Color.black);
        repaint();
    }

    public void erase() {
        if (getBackgroundColor() == null) {
            setBackgroundColor(Color.white);
            graphics2D.setPaint(Color.white);
        } else {
            graphics2D.setPaint(getBackgroundColor());
        }
    }

    public Color random() {
        Random random = new Random();
        int redColor;
        int greenColor;
        int blueColor;
        do {
            redColor = random.nextInt();
        } while (redColor > 256 || redColor < 0);

        do {
            greenColor = random.nextInt();
        } while (greenColor > 256 || greenColor < 0);

        do {
            blueColor = random.nextInt();
        } while (blueColor > 256 || blueColor < 0);
        Color rgbColor = new Color(redColor, greenColor, blueColor);
        graphics2D.setPaint(rgbColor);
        return rgbColor;
    }

    public Color hex(String hexString) throws NumberFormatException {
        Color hexColor = Color.decode(hexString);
        graphics2D.setPaint(hexColor);
        return hexColor;
    }

    public void rgb(int red, int green, int blue) {
        if (red > 256 || green > 256 || blue > 256) {
            JOptionPane.showMessageDialog(null, "Not a valid RGB Value", "Error",
                    JOptionPane.ERROR_MESSAGE);
        } else {
            try {
                Color rgbColor = new Color(red, green, blue);
                graphics2D.setPaint(rgbColor);
            } catch (IllegalArgumentException e) {
                JOptionPane.showMessageDialog(null, "Not a valid RGB Value", "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public void run() {
        JFrame frame = new JFrame();
        frame.setTitle("Paint Challenge 12");
        Container content = frame.getContentPane();
        content.setLayout(new BorderLayout());
        paint = new Paint();
        content.add(paint, BorderLayout.CENTER);
        content.add(paint);

        clrButton = new JButton("Clear");
        clrButton.addActionListener(actionListener);
        fillButton = new JButton("Fill");
        fillButton.addActionListener(actionListener);
        eraseButton = new JButton("Erase");
        eraseButton.addActionListener(actionListener);
        randomButton = new JButton("Random");
        randomButton.addActionListener(actionListener);

        hexText = new JTextField(10);
        hexButton = new JButton("Hex");
        hexButton.addActionListener(actionListener);
        rText = new JTextField(5);
        gText = new JTextField(5);
        bText = new JTextField(5);
        rgbButton = new JButton("RGB");
        rgbButton.addActionListener(actionListener);
        hexText.setText("#");

        frame.setSize(600, 400);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setVisible(true);

        JPanel topPanel = new JPanel();
        topPanel.add(clrButton);
        topPanel.add(fillButton);
        topPanel.add(eraseButton);
        topPanel.add(randomButton);
        frame.add(topPanel, BorderLayout.NORTH);

        JPanel bottomPanel = new JPanel();
        bottomPanel.add(hexText);
        bottomPanel.add(hexButton);
        bottomPanel.add(rText);
        bottomPanel.add(gText);
        bottomPanel.add(bText);
        bottomPanel.add(rgbButton);
        frame.add(bottomPanel, BorderLayout.SOUTH);
    }

    protected void paintComponent(Graphics g) {
        if (image == null) {
            image = createImage(getSize().width, getSize().height);

            // this lets us draw on the image (i.e. the canvas)
            graphics2D = (Graphics2D) image.getGraphics();

            // gives us better rendering quality for the drawing lines
            graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);

            // set canvas to white with default paint color
            graphics2D.setPaint(Color.white);
            graphics2D.fillRect(0, 0, getSize().width, getSize().height);
            graphics2D.setPaint(Color.black);
            repaint();
        }
        g.drawImage(image, 0, 0, null);
        graphics2D.setStroke(new BasicStroke(5));
    }

    public Paint() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                // set oldX and oldY coordinates to beginning mouse press
                oldX = e.getX();
                oldY = e.getY();
            }
        });

        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                // set current coordinates to where mouse is being dragged
                curX = e.getX();
                curY = e.getY();

                // draw the line between old coordinates and new ones
                graphics2D.drawLine(oldX, oldY, curX, curY);

                // refresh frame and reset old coordinates
                repaint();
                oldX = curX;
                oldY = curY;

            }
        });
    }
}
