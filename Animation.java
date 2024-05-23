import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Animation extends JFrame {
    private static final int ARRAY_SIZE = 50;
    private static final int ARRAY_MAX_VALUE = 500;
    private static final int WINDOW_WIDTH = 800;
    private static final int WINDOW_HEIGHT = 600;
    private static final int BAR_WIDTH = 10;
    private static final int BAR_GAP = 2;
    private static final int ANIMATION_DELAY = 10;

    private int[] arr;
    private boolean isSorting;

    private Image offScreenImage;
    private Graphics offScreenGraphics;

    public Animation() {
        setTitle("Sorting Animation");
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        arr = new int[ARRAY_SIZE];
        for (int i = 0; i < ARRAY_SIZE; i++)
            arr[i] = (int) (Math.random() * ARRAY_MAX_VALUE);

        JButton startButton = new JButton("Start");
        JButton stopButton = new JButton("Stop");
        JButton resetButton = new JButton("Reset");

        startButton.addActionListener(e -> startSorting());
        stopButton.addActionListener(e -> stopSorting());
        resetButton.addActionListener(e -> resetArray());

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(startButton);
        buttonPanel.add(stopButton);
        buttonPanel.add(resetButton);

        add(buttonPanel, BorderLayout.NORTH);

        JPanel animationPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawBars(g);
            }
        };

        add(animationPanel, BorderLayout.CENTER);

        setVisible(true);
    }

    private void startSorting() {
        if (!isSorting) {
            isSorting = true;
            new Thread(() -> {
                bubbleSort();
                isSorting = false;
            }).start();
        }
    }

    private void stopSorting() {
        isSorting = false;
    }

    private void resetArray() {
        if (!isSorting) {
            for (int i = 0; i < ARRAY_SIZE; i++)
                arr[i] = (int) (Math.random() * ARRAY_MAX_VALUE);
            repaint();
        }
    }

    private void bubbleSort() {
        for (int i = 0; i < ARRAY_SIZE - 1 && isSorting; i++) {
            boolean sorted = true;
            for (int j = 0; j < ARRAY_SIZE - i - 1; j++) {
                if (arr[j] > arr[j + 1]) {
                    int temp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = temp;
                    sorted = false;
                    try {
                        Thread.sleep(ANIMATION_DELAY);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                    repaint();
                }
            }
            if (sorted) break;
        }
    }

    private void drawBars(Graphics g) {
        for (int i = 0; i < ARRAY_SIZE; i++) {
            Color barColor = (i < ARRAY_SIZE - 1 && arr[i] > arr[i + 1]) ? Color.RED : Color.BLACK;
            g.setColor(barColor);
            int barHeight = (int) ((double) arr[i] / ARRAY_MAX_VALUE * (WINDOW_HEIGHT - 100));
            int x = i * (BAR_WIDTH + BAR_GAP) + 50;
            int y = WINDOW_HEIGHT - barHeight - 50;
            g.fillRect(x, y, BAR_WIDTH, barHeight);
        }
    }

    public static void main(String[] args) {
        new Animation();
    }
}
