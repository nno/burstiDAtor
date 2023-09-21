package burstiDAtor;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Vector;
import java.math.BigDecimal;

import javax.imageio.ImageIO;

public class Histogram {
    Vector<Double> bins;
    Vector<Integer> counts;

    public Histogram() {
        this.bins = new Vector<Double>();
        this.counts = new Vector<Integer>();
    }

    public static int getMaxCount(Vector<Integer> counts) {
        if (counts.size() == 0) {
            return 0;
        }
        int max = counts.get(0);
        for (int c : counts) {
            if (c > max) {
                max = c;
            }
        }
        return max;
    }

    public static BufferedImage getPlot(Vector<Double> bins,
            Vector<Integer> counts,
            String ylabel) {

        int mx0 = 40, mx2 = 10;
        int my0 = 20, my2 = 40;

        final int approx_width = 800;
        final int approx_height = 400;
        final int font_size = 12;
        final int tickminor = 3, tickmajor = 7;

        // number of bins
        int n_bins = bins.size() - 1;

        int ntick_x = 20;
        if (n_bins < ntick_x) {
            ntick_x = n_bins;
        }
        int tickx_major = (int) (n_bins / ntick_x);

        // step in x direction
        double dx = ((double) approx_width) / n_bins;

        // max y value
        int max_y = (int) (getMaxCount(counts) * 1.1);

        // step in y direction
        double dy = ((double) approx_height) / max_y;

        int ticky_major = (int) Math.pow(10, Math.ceil(Math.log10(max_y)) - 1);
        int ticky_minor = (int) Math.pow(10, Math.ceil(Math.log10(max_y)) - 2);

        if (ticky_major <= 0) {
            ticky_major = 1;
        }

        if (ticky_minor <= 0) {
            ticky_minor = 1;
        }

        // width of plot
        int mx1 = (int) dx * n_bins;

        // height of plot
        int my1 = (int) (dy * max_y);

        int width = mx0 + mx1 + mx2;
        int height = my0 + my1 + my2;

        BufferedImage bufferedImage = new BufferedImage(width, height,
                BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = bufferedImage.createGraphics();

        Map<?, ?> desktopHints = (Map<?, ?>) Toolkit.getDefaultToolkit()
                .getDesktopProperty("awt.font.desktophints");

        if (desktopHints != null) {
            g2d.setRenderingHints(desktopHints);
        }

        g2d.setColor(Color.black);

        Font font = new Font("Arial", Font.PLAIN, font_size);
        g2d.setFont(font);
        FontMetrics fm = g2d.getFontMetrics();

        // plot x axis
        for (int i = 0; i < n_bins; i++) {
            int count = counts.get(i);
            int bar_height = (int) (count * dy);
            int x = (int) (mx0 + i * dx);
            int y = my0 + (my1 - bar_height);

            // draw rectangle
            g2d.setColor(Color.CYAN);
            g2d.fillRect(x, y, (int) dx, bar_height);

            // fill bar
            g2d.setColor(Color.BLUE);
            g2d.drawRect(x, y, (int) dx, bar_height);

            boolean draw_count = n_bins <= 50;
            if (draw_count) {
                String label = "" + count;
                int w = fm.stringWidth(label);
                int h = font_size;

                g2d.drawString(label, (int) (x - (w / 2) + (dx / 2)),
                        y - h / 2);
            }

            // draw tick line
            g2d.setColor(Color.BLACK);
            boolean is_major = (i % tickx_major == 0);

            g2d.drawLine(x, my0 + my1, x,
                    my0 + my1 + (is_major ? tickmajor : tickminor));

            if (is_major) {
                // draw label
                String label = i == 0 ? "0"
                        : String.format("%.1f", bins.get(i).doubleValue());
                int w = fm.stringWidth(label);
                int h = font_size;

                g2d.drawString(label, x - w / 2, my0 + my1 + tickmajor + h);
            }
        }

        g2d.drawString(ylabel, mx0, my0 + my1 + tickmajor + 2 * font_size);

        // plot y axis
        for (int c = 0; c <= max_y; c++) {
            int y = (int) (my0 + my1 - c * dy);
            int x = mx0;

            boolean is_major = (c % ticky_major == 0);
            boolean is_minor = (c % ticky_minor == 0) && !is_major;

            if (is_major || is_minor) {
                g2d.drawLine(x, y, x - (is_major ? tickmajor : tickminor), y);
            }

            if (is_major) {
                String label = String.format("%d", c);
                int w = fm.stringWidth(label);
                int h = font_size;

                g2d.drawString(label, x - tickmajor - w - tickminor,
                        y + h / 2);
            }
        }

        g2d.drawLine(mx0, my0, mx0, my0 + my1);

        g2d.dispose();
        return bufferedImage;

    }

    public BufferedImage getPlot(String ylabel) {
        return getPlot(bins, counts, ylabel);
    }

    public void writeOutputFile(File file, BufferedImage img)
            throws Exception {
        try {
            ImageIO.write(img, "png", file);
        } catch (IOException e) {
            System.err.println("Unable to write to " + file);
        }

    }
}
