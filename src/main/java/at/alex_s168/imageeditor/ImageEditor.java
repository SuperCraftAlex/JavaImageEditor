package at.alex_s168.imageeditor;

import at.alex_s168.imageeditor.ui.Editor;
import at.alex_s168.imageeditor.util.Translator;
import de.m_marvin.commandlineparser.CommandLineParser;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.widgets.Display;

public class ImageEditor {

    private static ImageEditor INSTANCE;

    protected boolean shouldTerminate;
    protected Display display;
    protected Editor editor;

    public static void main(String... args) {

        ImageEditor editor = new ImageEditor();

        CommandLineParser parser = new CommandLineParser();
        parser.parseInput(args);
        // logicSim.subCircuitFolder = new File(parser.getOption("sub-circuit-folder"));

        editor.start();

    }

    public ImageEditor() {
        INSTANCE = this;
    }

    public Editor getEditor() {
        return editor;
    }

    public static ImageEditor getInstance() {
        return INSTANCE;
    }

    public Device getDisplay() {
        return this.display;
    }

    public boolean shouldTerminate() {
        return shouldTerminate;
    }

    public void terminate() {
        this.shouldTerminate = true;
    }

    private void start() {

        Translator.setLangFolder("/lang");
        Translator.changeLanguage("lang_en");

        this.display = new Display();

        editor = new Editor(display);

        while (!shouldTerminate()) {
            update();
            render();
        }

        this.display.dispose();

    }

    private void update() {
        if (editor.getShell().isDisposed() || this.shouldTerminate) {System.exit(0);}

        this.display.readAndDispatch();
    }

    private void render() {
        this.editor.render();
    }

}
