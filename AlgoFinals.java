import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


public class AlgoFinals extends JFrame {

    private static int PIXEL_SIZE = 10;
    private static int CANVAS_WIDTH = 80;
    private static final int CANVAS_HEIGHT = 60;

    private static Color currentColor;

    public AlgoFinals() {
        setTitle("PAINT FINALS");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(true);

        currentColor = Color.BLACK;
        
        
        Canvas canvas = new Canvas();
        canvas.setPreferredSize(new Dimension(800, 600));
        
        canvas.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                int x = e.getX() / PIXEL_SIZE;
                int y = e.getY() / PIXEL_SIZE;
                canvas.setPixelColor(x, y, currentColor);
            }
        });
        

        Palette palette = new Palette();

        palette.addButton(Color.BLACK);
        palette.addButton(Color.RED);
        palette.addButton(Color.GREEN);
        palette.addButton(Color.BLUE);
        palette.addButton(Color.YELLOW);
        palette.addButton(Color.CYAN);
        palette.addButton(Color.MAGENTA);
        palette.addButton(Color.WHITE);
        palette.addEraser();

        palette.addPaletteListener(color -> currentColor = color);
        

        getContentPane().setLayout(new BorderLayout());

        getContentPane().add(canvas, BorderLayout.CENTER);
        getContentPane().add(palette, BorderLayout.SOUTH);
        // getContentPane().add(eraser, BorderLayout.AFTER_LAST_LINE);
   

        pack();
    }

    private static class Canvas extends JPanel {

        private Color[][] pixels;
    
        public Canvas() {
            pixels = new Color[CANVAS_WIDTH][CANVAS_HEIGHT];
            clearCanvas();
    
            MouseAdapter mouseAdapter = new MouseAdapter() {
    
                @Override
                public void mouseDragged(MouseEvent e) {
                    int x = e.getX() / PIXEL_SIZE;
                    int y = e.getY() / PIXEL_SIZE;
    
                    if (SwingUtilities.isLeftMouseButton(e)) {
                        setPixelColor(x, y, currentColor);
                    } else if (SwingUtilities.isRightMouseButton(e)) {
                        fill(x, y, currentColor);
                    }
                }
            };
            addMouseMotionListener(mouseAdapter);
        }
    
        public void clearCanvas() {
            for (int x = 0; x < CANVAS_WIDTH; x++) {
                for (int y = 0; y < CANVAS_HEIGHT; y++) {
                    pixels[x][y] = Color.WHITE;
                }
            }
            repaint();
        }
   
        public void setPixelColor(int x, int y, Color color) {
            pixels[x][y] = color;
            repaint();
        }
    
        public void fill(int x, int y, Color color) {
            Color targetColor = pixels[x][y];
    
            if (targetColor.equals(color)) {
                return;
            }
    
            floodFill(x, y, targetColor, color);// applying the algorithm in the fill function 
            repaint();
        }
    
        //Flood Fill Algorithm implementation
        private void floodFill(int x, int y, Color targetColor, Color replacementColor) {
            if (x < 0 || x >= CANVAS_WIDTH || y < 0 || y >= CANVAS_HEIGHT) {
                return;
            }
         
            if (!pixels[x][y].equals(targetColor)) {
                return;
            }
    
            pixels[x][y] = replacementColor;
            
            floodFill(x - 1, y, targetColor, replacementColor); // go north
            floodFill(x + 1, y, targetColor, replacementColor); // go south
            floodFill(x, y - 1, targetColor, replacementColor); // go west
            floodFill(x, y + 1, targetColor, replacementColor); // go east
        }
    
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
    
            for (int x = 0; x < CANVAS_WIDTH; x++) {
                for (int y = 0; y < CANVAS_HEIGHT; y++) {
                    int xPos = x * PIXEL_SIZE;
                    int yPos = y * PIXEL_SIZE;
                    g.setColor(pixels[x][y]);
                    g.fillRect(xPos, yPos, PIXEL_SIZE, PIXEL_SIZE);
                }
            }
        }
    }
    

    private static class Palette extends JPanel {

        private static final int BUTTON_SIZE = 20;

        private Color selectedColor;
        private PaletteListener listener;

        public Palette() {
            setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
            selectedColor = Color.BLACK;
        }
        
        public void addEraser() {
            JButton button = new JButton(); 
            button.setText("Eraser");
            button.addActionListener(e -> {
                selectedColor = Color.WHITE;
                if (listener != null) {
                    listener.onColorSelected(selectedColor);
                }
            });
            add(button);
        }

        public void addButton(Color color) {
            JButton button = new JButton();
            button.setPreferredSize(new Dimension(BUTTON_SIZE, BUTTON_SIZE));
            button.setBackground(color);
            button.addActionListener(e -> {
                selectedColor = color;
                if (listener != null) {
                    listener.onColorSelected(selectedColor);
                }
            });
            add(button);
        }

        public void addPaletteListener(PaletteListener listener) {
            this.listener = listener;
        }

        public interface PaletteListener {
            void onColorSelected(Color color);
        }
    }
   
}
