package at.alex_s168.imageeditor.ui;

import at.alex_s168.imageeditor.util.Translator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.BorderData;
import org.eclipse.swt.layout.BorderLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;

public class UIBottomButtons {

    public static void generateBottomButtons(Shell w, SelectionListener ok) {

        Composite bottomButtons = new Composite(w, SWT.NONE);
        bottomButtons.setLayoutData(new BorderData(SWT.BOTTOM, SWT.DEFAULT, SWT.DEFAULT));
        bottomButtons.setLayout(new BorderLayout());

        final Button okButton = new Button(bottomButtons, SWT.NONE);
        okButton.setLayoutData(new BorderData(SWT.LEFT, SWT.CENTER, SWT.DEFAULT));
        okButton.setText(Translator.translate("window.ok"));
        okButton.addSelectionListener(new SelectionListener() {
            public void widgetSelected(SelectionEvent event) {
                w.close();
            }

            @Override
            public void widgetDefaultSelected(SelectionEvent e) {}
        });
        okButton.addSelectionListener(ok);

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

    }

}
