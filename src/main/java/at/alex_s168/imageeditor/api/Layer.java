package at.alex_s168.imageeditor.api;

public class Layer {

    public PixelMap pixelMap;
    public int textureID;
    public boolean visible = true;

    public Layer() {
        pixelMap = new PixelMap();
    }

    public Layer(PixelMap m) {
        pixelMap = m;
    }

    public Layer(PixelMap m, int textureIDIn) {
        pixelMap = m;
        textureID = textureIDIn;
    }

}
