package at.alex_s168.imageeditor.ui;

import at.alex_s168.imageeditor.ImageEditor;
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


    protected File openFile;
    protected Shell shell;
    protected Menu titleBar;
    protected ToolBar toolBar;
    protected Tree partSelector;
    protected EditorArea editorArea;

    public Color colorPrimary = new Color(0, 0, 0, 255);

    public Color colorSecondary = new Color(255, 255, 255, 255);

    public int scrollX = 0;
    public int scrollY = 0;

    public int sliderXMax = 0;
    public int sliderYMax = 0;

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

        MenuItem imageTab = new MenuItem (titleBar, SWT.CASCADE);
        imageTab.setText (Translator.translate("editor.menu.image"));
        Menu imageMenu = new Menu(shell, SWT.DROP_DOWN);
        imageTab.setMenu(imageMenu);

        MenuItem imageColorTab = new MenuItem (imageMenu, SWT.CASCADE);
        imageColorTab.setText (Translator.translate("editor.menu.image.color"));
        Menu imageColorMenu = new Menu(shell, SWT.DROP_DOWN);
        imageColorTab.setMenu(imageColorMenu);

        MenuItem imageColorInvertOpt = new MenuItem(imageColorMenu, SWT.PUSH);
        imageColorInvertOpt.setText(Translator.translate("editor.menu.image.color.invert"));
        imageColorInvertOpt.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {

            }
        });

        MenuItem imageColorReplaceOpt = new MenuItem(imageColorMenu, SWT.PUSH);
        imageColorReplaceOpt.setText(Translator.translate("editor.menu.image.color.replace"));
        imageColorReplaceOpt.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                Shell w = new Shell(display);
                w.setSize(shell.getSize().x / 3, shell.getSize().y / 3);
                w.setText(Translator.translate("editor.window.image.color.replace"));
                w.setLayout(new BorderLayout());

                w.addFocusListener(new FocusListener() {
                    public void focusLost(FocusEvent e) {}
                    public void focusGained(FocusEvent e) {}
                });

                final Color[] c1 = {new Color(255, 255, 255)};
                final Color[] c2 = {new Color(255, 255, 255)};
                final int[] tolerance = {0};
                final boolean[] smartReplace = {false};

                Composite bottomButtons = new Composite(w, SWT.NONE);
                bottomButtons.setLayoutData(new BorderData(SWT.BOTTOM, SWT.DEFAULT, SWT.DEFAULT));
                bottomButtons.setLayout(new BorderLayout());

                final Button okButton = new Button(bottomButtons, SWT.NONE);
                okButton.setLayoutData(new BorderData(SWT.LEFT, SWT.CENTER, SWT.DEFAULT));
                okButton.setText(Translator.translate("window.ok"));
                okButton.addSelectionListener(new SelectionListener() {
                    public void widgetSelected(SelectionEvent event) {
                        editorArea.replace(c1[0], c2[0], tolerance[0], smartReplace[0]);
                    }

                    @Override
                    public void widgetDefaultSelected(SelectionEvent e) {}
                });

                final Button cancelButton = new Button(bottomButtons, SWT.NONE);
                cancelButton.setLayoutData(new BorderData(SWT.RIGHT, SWT.CENTER, SWT.DEFAULT));
                cancelButton.setText(Translator.translate("window.cancel"));
                cancelButton.addSelectionListener(new SelectionListener() {
                    public void widgetSelected(SelectionEvent event) {
                        w.close();
                    }

                    @Override
                    public void widgetDefaultSelected(SelectionEvent e) {}
                });

                //

                Composite colors = new Composite(w, SWT.NONE);
                colors.setLayoutData(new BorderData(SWT.UP, SWT.DEFAULT, SWT.DEFAULT));
                colors.setLayout(new BorderLayout());

                final Label color1Label = new Label(colors, SWT.TOP);
                color1Label.setLayoutData(new BorderData(SWT.TOP));
                color1Label.setText("                              ");
                color1Label.setBackground(c1[0]);

                Button color1Button = new Button(colors, SWT.PUSH);
                color1Button.setText(Translator.translate("editor.menu.color.change"));
                color1Button.setLayoutData(new BorderData(SWT.TOP));
                color1Button.addSelectionListener(new SelectionAdapter() {
                    public void widgetSelected(SelectionEvent event) {ColorDialog dlg = new ColorDialog(shell);dlg.setRGB(color1Label.getBackground().getRGB()); dlg.setText(Translator.translate("editor.window.color.select"));RGB rgb = dlg.open(); if (rgb != null) {c1[0].dispose(); c1[0] = new Color(shell.getDisplay(), rgb);color1Label.setBackground(c1[0]);} w.setActive(); }
                });

                final Label color2Label = new Label(colors, SWT.BOTTOM);
                color2Label.setLayoutData(new BorderData(SWT.BOTTOM));
                color2Label.setText("                              ");
                color2Label.setBackground(c1[0]);

                Button color2Button = new Button(colors, SWT.PUSH);
                color2Button.setText(Translator.translate("editor.menu.color.change"));
                color2Button.setLayoutData(new BorderData(SWT.BOTTOM));
                color2Button.addSelectionListener(new SelectionAdapter() {
                    public void widgetSelected(SelectionEvent event) {ColorDialog dlg = new ColorDialog(shell);dlg.setRGB(color1Label.getBackground().getRGB()); dlg.setText(Translator.translate("editor.window.color.select"));RGB rgb = dlg.open(); if (rgb != null) {c2[0].dispose(); c2[0] = new Color(shell.getDisplay(), rgb);color2Label.setBackground(c2[0]);} w.setActive(); }
                });

                //

                Composite settings = new Composite(w, SWT.NONE);
                settings.setLayoutData(new BorderData(SWT.CENTER, SWT.DEFAULT, SWT.CENTER));
                settings.setLayout(new BorderLayout());

                final Label t = new Label(settings, SWT.BORDER);
                t.setText(Translator.translate("tolerance"));

                final Slider toleranceSlide = new Slider(settings, SWT.HORIZONTAL);
                toleranceSlide.setMinimum(0);
                toleranceSlide.setMaximum(780);
                toleranceSlide.setIncrement(1);

                toleranceSlide.addSelectionListener(new SelectionAdapter() {
                    public void widgetSelected(SelectionEvent e) {
                        tolerance[0] = toleranceSlide.getSelection();
                    }
                });

                Button smartReplaceCheckBox = new Button(settings, SWT.CHECK);
                smartReplaceCheckBox.setLayoutData(new BorderData(SWT.BOTTOM, SWT.DEFAULT, SWT.DEFAULT));
                smartReplaceCheckBox.setText(Translator.translate("editor.window.image.color.replace.smartreplace"));
                smartReplaceCheckBox.addSelectionListener(new SelectionAdapter() {
                    @Override
                    public void widgetSelected(SelectionEvent event) {
                        Button btn = (Button) event.getSource();
                        smartReplace[0] = btn.getSelection();
                    }
                });

                w.open();
            }
        });

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
        final Slider sliderX = new Slider(groupBottom, SWT.BOTTOM);
        groupColorSecondary.setLayoutData(new BorderData(SWT.BOTTOM));
        groupColorSecondary.setLayout(new BorderLayout());
        sliderX.setMinimum(0);
        sliderX.setMaximum(sliderXMax);
        sliderX.setIncrement(1);
        sliderX.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                scrollX = sliderX.getSelection();
            }
        });

        final Slider sliderY = new Slider(groupRight, SWT.VERTICAL);
        groupColorSecondary.setLayoutData(new BorderData(SWT.VERTICAL));
        groupColorSecondary.setLayout(new BorderLayout());
        sliderY.setMinimum(0);
        sliderY.setMaximum(sliderYMax);
        sliderY.setIncrement(1);
        sliderY.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                scrollY = sliderY.getSelection();
            }
        });

        this.shell.open();
        updateTitle();
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

    public void loadFile() {
        FileDialog fileDialog = new FileDialog(shell, SWT.OPEN);
        // TODO File-type specific dialog
        String path = fileDialog.open();
        if (path != null) {
            File filePath = new File(path);
            editorArea.openFile(filePath);
            this.openFile = filePath;
            updateTitle();
        }
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
