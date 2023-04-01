package at.alex_s168.imageeditor.features.keybinds;

import at.alex_s168.imageeditor.ImageEditor;
import at.alex_s168.imageeditor.PixelStorage;
import at.alex_s168.imageeditor.ui.Editor;
import at.alex_s168.imageeditor.ui.EditorArea;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;

public class FeatureKeybinds {

    public static void keyReleased(KeyEvent e) {
        String key = Character.toString(e.character);
        if(key.equals("+")) {
            EditorArea.getSelf().scale = Math.min(9.99, 1.1 * EditorArea.getSelf().scale);
            Editor.getSelf().zoomPercentageInput.setText("" + (int) (EditorArea.getSelf().scale * 100));
            PixelStorage.getSelf().update();
        }
        if(key.equals("-")) {
            EditorArea.getSelf().scale = Math.max(0.1, 0.9 * EditorArea.getSelf().scale);
            Editor.getSelf().zoomPercentageInput.setText("" + (int) (EditorArea.getSelf().scale * 100));
            PixelStorage.getSelf().update();
        }
        if(e.keyCode == SWT.ESC) {
            EditorArea.getSelf().selection.clear();
        }

        if(((e.stateMask & SWT.CTRL) == SWT.CTRL) && (e.keyCode == 's')) {
            EditorArea.getSelf().saveFile(ImageEditor.getInstance().getEditor().openFile);
        }

        if(((e.stateMask & SWT.CTRL) == SWT.CTRL) && (e.keyCode == 'c')) {
            if(EditorArea.getSelf().selection.sqArea()>0) {
                EditorArea.getSelf().copyToClipboard(EditorArea.getSelf().selection);
            }
        }
    }

}
