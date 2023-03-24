package at.alex_s168.imageeditor.util;

import at.alex_s168.imageeditor.api.PixelMap;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImagePixelHelper {

    public static PixelMap getPixFromImage(File f) {
        BufferedImage img = null;
        try {
            img = ImageIO.read(f);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
        int[] pix = new int[img.getWidth()*img.getHeight()];

        {
            int x,y,rgb;
            for (y = 0;y < img.getHeight();y++) {
                for (x = 0;x < img.getWidth();x++) {
                    rgb = img.getRGB(x,y);
                    pix[y*img.getWidth()+x] = rgb;
                }
            }
        }

        return new PixelMap(img.getWidth(), img.getHeight(), pix);
    }

}
