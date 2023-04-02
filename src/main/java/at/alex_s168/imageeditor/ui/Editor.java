package at.alex_s168.imageeditor.ui;

import at.alex_s168.imageeditor.ImageEditor;
import at.alex_s168.imageeditor.features.image.FeatureImageAdjust;
import at.alex_s168.imageeditor.ui.file.UIFileNew;
import at.alex_s168.imageeditor.ui.image.adjust.UIAdjustBrightnessAndContrast;
import at.alex_s168.imageeditor.ui.image.adjust.UIAdjustColorReplace;
import at.alex_s168.imageeditor.ui.left.UILeftColor;
import at.alex_s168.imageeditor.ui.right.UIRight;
import at.alex_s168.imageeditor.util.Translator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

import java.io.*;
import java.util.Base64;

public class Editor {


    public File openFile;
    protected Shell shell;
    protected Menu titleBar;
    protected ToolBar toolBar;
    protected Tree partSelector;
    public EditorArea editorArea;

    public int scrollX = 0;
    public int scrollY = 0;

    public final Slider sliderX;
    public final Slider sliderY;

    public final Text zoomPercentageInput;
    public final Label fpsLabel;

    public static Image decodeImage(String imageString) {
        return new Image(ImageEditor.getInstance().getDisplay(), new ImageData(new ByteArrayInputStream(Base64.getDecoder().decode(imageString))));
    }

    public Editor(Display display) {
        this.shell = new Shell(display);
        this.shell.setLayout(new BorderLayout());
        this.shell.addFocusListener(new FocusListener() {
            public void focusLost(FocusEvent e) {}
            public void focusGained(FocusEvent e) {}
        });

        // Top menu bar

        this.titleBar = new Menu(shell, SWT.BAR);
        this.shell.setMenuBar(titleBar);

        MenuItem fileTab = new MenuItem (titleBar, SWT.CASCADE);
        fileTab.setText (Translator.translate("editor.menu.file"));
        Menu fileMenu = new Menu(shell, SWT.DROP_DOWN);
        fileTab.setMenu(fileMenu);
        UIFileNew.genFileNewButton(fileMenu, shell, shell, display);
        MenuItem saveAsOpt = new MenuItem(fileMenu, SWT.PUSH);
        saveAsOpt.setText(Translator.translate("editor.menu.file.save_as"));
        saveAsOpt.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                saveFile(true);
            }
        });
        MenuItem saveOpt = new MenuItem(fileMenu, SWT.PUSH);
        saveOpt.setText(Translator.translate("editor.menu.file.save"));
        saveOpt.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                saveFile(false);
            }
        });
        MenuItem loadOpt = new MenuItem(fileMenu, SWT.PUSH);
        loadOpt.setText(Translator.translate("editor.menu.file.load"));
        loadOpt.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                loadFile();
            }
        });
        new MenuItem(fileMenu, SWT.SEPARATOR);
        MenuItem settingsOpt = new MenuItem(fileMenu, SWT.PUSH);
        settingsOpt.setText(Translator.translate("editor.menu.file.settings"));
        settingsOpt.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                //todo
            }
        });

        MenuItem imageTab = new MenuItem (titleBar, SWT.CASCADE);
        imageTab.setText (Translator.translate("editor.menu.image"));
        Menu imageMenu = new Menu(shell, SWT.DROP_DOWN);
        imageTab.setMenu(imageMenu);

        MenuItem imageAdjustTab = new MenuItem (imageMenu, SWT.CASCADE);
        imageAdjustTab.setText (Translator.translate("editor.menu.image.adjust"));
        Menu imageAdjustMenu = new Menu(shell, SWT.DROP_DOWN);
        imageAdjustTab.setMenu(imageAdjustMenu);

        UIAdjustBrightnessAndContrast.genImageBrightnesAndContrastButton(imageAdjustMenu, display, shell);

        new MenuItem(imageAdjustMenu, SWT.SEPARATOR);

        UIAdjustColorReplace.genImageColorReplaceButton(imageAdjustMenu, shell, display);

        new MenuItem(imageAdjustMenu, SWT.SEPARATOR);

        MenuItem imageAdjustInvertOpt = new MenuItem(imageAdjustMenu, SWT.PUSH);
        imageAdjustInvertOpt.setText(Translator.translate("editor.menu.image.adjust.invert"));
        imageAdjustInvertOpt.addSelectionListener(new SelectionAdapter() {@Override public void widgetSelected(SelectionEvent e) {
            FeatureImageAdjust.colorInvert();
        }});

        // Left tool group

        Composite groupLeft = new Composite(shell, SWT.NONE);
        groupLeft.setLayoutData(new BorderData(SWT.LEFT, SWT.DEFAULT, SWT.DEFAULT));
        groupLeft.setLayout(new BorderLayout());

        UILeftColor.genColorSidebar(shell, groupLeft);

        final Composite editor = new Composite(shell, SWT.NONE);
        editor.setLayoutData(new BorderData(SWT.CENTER, SWT.DEFAULT, SWT.DEFAULT));
        editor.setLayout(new BorderLayout());

        // Editor area
        this.editorArea = new EditorArea(editor);
        this.editorArea.setSize(300, 300);
        this.editorArea.setLocation(50, 20);
        this.editorArea.setBackground(new Color(128, 128, 0));

        final Composite groupBottom = new Composite(editor, SWT.NONE);
        groupBottom.setLayoutData(new BorderData(SWT.BOTTOM, SWT.DEFAULT, SWT.DEFAULT));
        GridLayout groupBottomLayout = new GridLayout();
        groupBottomLayout.numColumns = 3;
        groupBottom.setLayout(groupBottomLayout);

        final Composite groupRight = new Composite(editor, SWT.NONE);
        groupRight.setLayoutData(new BorderData(SWT.RIGHT, SWT.DEFAULT, SWT.DEFAULT));
        groupRight.setLayout(new BorderLayout());

        UIRight.genUIRightRight(shell, display, groupRight);

        zoomPercentageInput = new Text(groupBottom, SWT.LEFT);
        zoomPercentageInput.setTextLimit(3);
        zoomPercentageInput.setText("100");
        zoomPercentageInput.addModifyListener(e -> {
            try {
                EditorArea.getSelf().scale = Math.max(0.1, Math.min(9, (double) Integer.parseInt(zoomPercentageInput.getText()) / 100));
            } catch (Exception ignored) {}
        }
        );

        fpsLabel = new Label(groupBottom, SWT.LEFT);
        fpsLabel.setText("-1 FPS");

        // - sliders
        //todo: sliders weird
        sliderX = new Slider(groupBottom, SWT.NONE);
        GridData sliderXgridData = new GridData();
        sliderXgridData.horizontalAlignment = GridData.FILL;
        sliderXgridData.grabExcessHorizontalSpace = true;
        sliderX.setLayoutData(sliderXgridData);
        sliderX.setMinimum(0);
        sliderX.setMaximum(0);
        sliderX.setIncrement(1);
        sliderX.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                scrollX = sliderX.getSelection();
                editorArea.update();
            }
        });

        sliderY = new Slider(groupRight, SWT.VERTICAL);
        sliderY.setLayoutData(new BorderData(SWT.LEFT, SWT.DEFAULT, SWT.DEFAULT));
        sliderY.setMinimum(0);
        sliderY.setMaximum(0);
        sliderY.setIncrement(1);
        sliderY.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                scrollY = sliderY.getSelection();
                editorArea.update();
            }
        });

        this.shell.open();
        updateTitle();

        UIWelcome.genWelcomeScreen(shell, display);
    }

    public void updateTitle() {
        this.shell.setText("ImageWorks - alpha" + (this.openFile != null ? " - " + this.openFile : ""));
    }

    public Shell getShell() {
        return shell;
    }

    public void render() {
        this.editorArea.render();
    }

    public static Editor getSelf() {
        return ImageEditor.getInstance().getEditor();
    }

    public boolean loadFile() {
        FileDialog fileDialog = new FileDialog(shell, SWT.OPEN);
        // TODO File-type specific dialog
        String path = fileDialog.open();
        if (path != null) {
            File filePath = new File(path);
            editorArea.openFile(filePath);
            this.openFile = filePath;
            updateTitle();
            return true;
        }
        return false;
    }

    public void saveFile(boolean saveAs) {
        if(this.openFile != null) {
            if(saveAs) {
                FileDialog fileDialog = new FileDialog(shell, SWT.SAVE);
                String path = fileDialog.open();
                if (path != null) {
                    File filePath = new File(path);
                    if (filePath.exists()) {
                        MessageBox msg = new MessageBox(shell, SWT.YES | SWT.NO | SWT.ICON_QUESTION);
                        msg.setMessage(Translator.translate("editor.window.info.override_request"));
                        msg.setText(Translator.translate("editor.window.info"));
                        if (msg.open() == SWT.NO) return;
                    }
                    this.openFile = filePath;
                    editorArea.saveFile(filePath);
                    updateTitle();
                }
            }
            else {
                editorArea.saveFile(this.openFile);
            }
        }
    }

}
