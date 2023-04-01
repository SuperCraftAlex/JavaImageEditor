package at.alex_s168.imageeditor;

import org.eclipse.swt.graphics.Color;

public class ToolStorage {

    public Color colorPrimary = new Color(0,0,0);
    public Color colorSecondary = new Color(255,255,255);

    public static ToolStorage getSelf() {
        return ImageEditor.getInstance().getToolStorage();
    }

}
