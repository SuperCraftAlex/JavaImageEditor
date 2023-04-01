package at.alex_s168.imageeditor.ui;

import at.alex_s168.imageeditor.ImageEditor;
import at.alex_s168.imageeditor.features.image.FeatureImageColor;
import at.alex_s168.imageeditor.ui.File.UIFileNew;
import at.alex_s168.imageeditor.ui.image.adjust.UIAdjustBrightnessAndContrast;
import at.alex_s168.imageeditor.ui.image.adjust.UIAdjustColorReplace;
import at.alex_s168.imageeditor.util.Translator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.BorderData;
import org.eclipse.swt.layout.BorderLayout;
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

    public Color colorPrimary = new Color(0, 0, 0, 255);

    public Color colorSecondary = new Color(255, 255, 255, 255);

    public int scrollX = 0;
    public int scrollY = 0;

    public final Slider sliderX;
    public final Slider sliderY;

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
        imageAdjustInvertOpt.addSelectionListener(new SelectionAdapter() {@Override public void widgetSelected(SelectionEvent e) {FeatureImageColor.colorInvert();}});


        MenuItem imageColorTab = new MenuItem (imageAdjustMenu, SWT.CASCADE);
        imageColorTab.setText (Translator.translate("editor.menu.image.color"));
        Menu imageColorMenu = new Menu(shell, SWT.DROP_DOWN);
        imageColorTab.setMenu(imageColorMenu);

        MenuItem imageColorChannelTab = new MenuItem (imageColorMenu, SWT.CASCADE);
        imageColorChannelTab.setText (Translator.translate("editor.menu.image.color.channel"));
        Menu imageColorChannelMenu = new Menu(shell, SWT.DROP_DOWN);
        imageColorChannelTab.setMenu(imageColorChannelMenu);

        MenuItem imageColorChannelRotateTab = new MenuItem (imageColorChannelMenu, SWT.CASCADE);
        imageColorChannelRotateTab.setText (Translator.translate("editor.menu.image.color.channel.rotate"));
        Menu imageColorChannelRotateMenu = new Menu(shell, SWT.DROP_DOWN);
        imageColorChannelRotateTab.setMenu(imageColorChannelRotateMenu);

        MenuItem imageColorChannelRotateCWOpt = new MenuItem(imageColorChannelRotateMenu, SWT.PUSH);
        imageColorChannelRotateCWOpt.setText(Translator.translate("editor.menu.image.color.channel.rotate.CW"));
        imageColorChannelRotateCWOpt.addSelectionListener(new SelectionAdapter() {@Override public void widgetSelected(SelectionEvent e) {
            FeatureImageColor.channelRotateCW();}});

        MenuItem imageColorChannelRotateCCWOpt = new MenuItem(imageColorChannelRotateMenu, SWT.PUSH);
        imageColorChannelRotateCCWOpt.setText(Translator.translate("editor.menu.image.color.channel.rotate.CCW"));
        imageColorChannelRotateCCWOpt.addSelectionListener(new SelectionAdapter() {@Override public void widgetSelected(SelectionEvent e) {FeatureImageColor.channelRotateCCW();}});


        MenuItem imageColorChannelRemoveTab = new MenuItem (imageColorChannelMenu, SWT.CASCADE);
        imageColorChannelRemoveTab.setText (Translator.translate("editor.menu.image.color.channel.remove"));
        Menu imageColorChannelRemoveMenu = new Menu(shell, SWT.DROP_DOWN);
        imageColorChannelRemoveTab.setMenu(imageColorChannelRemoveMenu);

        MenuItem imageColorChannelRemoveROpt = new MenuItem(imageColorChannelRemoveMenu, SWT.PUSH);
        imageColorChannelRemoveROpt.setText(Translator.translate("editor.menu.image.color.channel.remove.R"));
        imageColorChannelRemoveROpt.addSelectionListener(new SelectionAdapter() {@Override public void widgetSelected(SelectionEvent e) {FeatureImageColor.channelRemoveR();}});

        MenuItem imageColorChannelRemoveGOpt = new MenuItem(imageColorChannelRemoveMenu, SWT.PUSH);
        imageColorChannelRemoveGOpt.setText(Translator.translate("editor.menu.image.color.channel.remove.G"));
        imageColorChannelRemoveGOpt.addSelectionListener(new SelectionAdapter() {@Override public void widgetSelected(SelectionEvent e) {FeatureImageColor.channelRemoveG();}});

        MenuItem imageColorChannelRemoveBOpt = new MenuItem(imageColorChannelRemoveMenu, SWT.PUSH);
        imageColorChannelRemoveBOpt.setText(Translator.translate("editor.menu.image.color.channel.remove.B"));
        imageColorChannelRemoveBOpt.addSelectionListener(new SelectionAdapter() {@Override public void widgetSelected(SelectionEvent e) {FeatureImageColor.channelRemoveB();}});

        // Left tool group

        Composite groupLeft = new Composite(shell, SWT.NONE);
        groupLeft.setLayoutData(new BorderData(SWT.LEFT, SWT.DEFAULT, SWT.DEFAULT));
        groupLeft.setLayout(new BorderLayout());

        // - color
        final Group groupColor = new Group(groupLeft, SWT.NONE);
        groupColor.setLayoutData(new BorderData(SWT.BOTTOM));
        groupColor.setLayout(new BorderLayout());

        final Group groupColorPrimary = new Group(groupColor, SWT.NONE);
        groupColorPrimary.setLayoutData(new BorderData(SWT.TOP));
        groupColorPrimary.setLayout(new BorderLayout());
        groupColorPrimary.setText(Translator.translate("editor.menu.color.primary"));

        final Label colorPrimaryLabel = new Label(groupColorPrimary, SWT.NONE);
        colorPrimaryLabel.setText("                              ");
        colorPrimaryLabel.setBackground(colorPrimary);

        Button colorPrimaryButton = new Button(groupColorPrimary, SWT.PUSH);
        colorPrimaryButton.setText(Translator.translate("editor.menu.color.change"));
        colorPrimaryButton.setLayoutData(new BorderData(SWT.RIGHT));
        colorPrimaryButton.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent event) {
                ColorDialog dlg = new ColorDialog(shell);
                dlg.setRGB(colorPrimaryLabel.getBackground().getRGB());
                dlg.setText(Translator.translate("editor.window.color.select"));
                RGB rgb = dlg.open();
                if (rgb != null) {
                    colorPrimary.dispose();
                    colorPrimary = new Color(shell.getDisplay(), rgb);
                    colorPrimaryLabel.setBackground(colorPrimary);
                }
            }
        });

        //todo: secondary color stuff not shown
        final Group groupColorSecondary = new Group(groupColor, SWT.NONE);
        groupColorSecondary.setLayoutData(new BorderData(SWT.BOTTOM));
        groupColorSecondary.setLayout(new BorderLayout());
        groupColorSecondary.setText(Translator.translate("editor.menu.color.secondary"));

        final Label colorSecondaryLabel = new Label(groupColorSecondary, SWT.NONE);
        colorSecondaryLabel.setText("                              ");
        colorSecondaryLabel.setBackground(colorSecondary);

        Button colorSecondaryButton = new Button(groupColorSecondary, SWT.PUSH);
        colorSecondaryButton.setText(Translator.translate("editor.menu.color.change"));
        colorSecondaryButton.setLayoutData(new BorderData(SWT.RIGHT));
        colorSecondaryButton.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent event) {
                ColorDialog dlg = new ColorDialog(shell);
                dlg.setRGB(colorSecondaryLabel.getBackground().getRGB());
                dlg.setText(Translator.translate("editor.window.color.select"));
                RGB rgb = dlg.open();
                if (rgb != null) {
                    colorSecondary.dispose();
                    colorSecondary = new Color(shell.getDisplay(), rgb);
                    colorSecondaryLabel.setBackground(colorSecondary);
                }
            }
        });

        final Composite editor = new Composite(shell, SWT.NONE);
        editor.setLayoutData(new BorderData(SWT.CENTER, SWT.DEFAULT, SWT.DEFAULT));
        editor.setLayout(new BorderLayout());

        // Editor area
        this.editorArea = new EditorArea(editor);
        this.editorArea.setSize(300, 300);
        this.editorArea.setLocation(50, 20);
        this.editorArea.setBackground(new Color(128, 128, 0));

        // - sliders
        final Composite groupBottom = new Composite(editor, SWT.NONE);
        groupBottom.setLayoutData(new BorderData(SWT.BOTTOM, SWT.DEFAULT, SWT.DEFAULT));
        groupBottom.setLayout(new BorderLayout());

        final Composite groupRight = new Composite(editor, SWT.NONE);
        groupRight.setLayoutData(new BorderData(SWT.RIGHT, SWT.DEFAULT, SWT.DEFAULT));
        groupRight.setLayout(new BorderLayout());

        //todo: sliders weird
        sliderX = new Slider(groupBottom, SWT.BOTTOM);
        groupColorSecondary.setLayoutData(new BorderData(SWT.BOTTOM));
        groupColorSecondary.setLayout(new BorderLayout());
        sliderX.setMinimum(0);
        sliderX.setMaximum(1);
        sliderX.setIncrement(1);
        sliderX.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                scrollX = sliderX.getSelection();
                editorArea.update();
            }
        });

        sliderY = new Slider(groupRight, SWT.VERTICAL);
        groupColorSecondary.setLayoutData(new BorderData(SWT.VERTICAL));
        groupColorSecondary.setLayout(new BorderLayout());
        sliderY.setMinimum(0);
        sliderY.setMaximum(1);
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
        this.shell.setText("ImageEditor - alpha" + (this.openFile != null ? " - " + this.openFile.toString() : ""));
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
