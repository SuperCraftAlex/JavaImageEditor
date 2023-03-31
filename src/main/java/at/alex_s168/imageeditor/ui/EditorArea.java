package at.alex_s168.imageeditor.ui;

import at.alex_s168.imageeditor.ImageEditor;
import at.alex_s168.imageeditor.api.AABB;
import at.alex_s168.imageeditor.api.PixelMap;
import at.alex_s168.imageeditor.util.ClipboardUtil;
import at.alex_s168.imageeditor.util.ColorHelper;
import at.alex_s168.imageeditor.util.ImagePixelHelper;
import static at.alex_s168.imageeditor.util.GLHelper.*;
import de.m_marvin.logicsim.ui.TextRenderer;
import de.m_marvin.univec.impl.Vec2d;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.*;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.opengl.GLCanvas;
import org.eclipse.swt.opengl.GLData;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL33;
import org.lwjgl.opengl.GLCapabilities;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EditorArea extends Canvas implements MouseListener, MouseMoveListener, KeyListener {
	
	protected GLData glData;
	protected GLCanvas glCanvas;
	protected GLCapabilities glCapabilities;
	protected boolean resized;
	protected boolean initialized = false;
	protected int textureId;

	protected boolean mouseDown = false;

	protected Vec2d initialMousePosition;

	protected Vec2d mousePosition;
	protected Vec2d prevMousePosition;
	protected Vec2d realMousePosition;
	protected Vec2d prevRealMousePosition;

	protected AABB selection = new AABB();

	protected double scale = 1;

	public PixelMap rOut = new PixelMap();

	protected boolean toUpdate = false;

	public List<Vec2d> mouseDragPosCache = new ArrayList<>();

	public EditorArea(Composite parent) {
		super(parent, SWT.NONE);
		this.setLayout(new FillLayout());
		this.glData = new GLData();
		this.glData.doubleBuffer = true;
		this.glCanvas = new GLCanvas(this, SWT.None, glData);
		this.glCanvas.addListener(SWT.Resize, (event) -> this.resized = true);
		this.glCanvas.addMouseListener(this);
		this.glCanvas.addMouseMoveListener(this);
		this.glCanvas.addKeyListener(this);
		initOpenGL();
	}

	public void updateMousePos(MouseEvent e) {
		prevMousePosition = mousePosition;
		mousePosition=new Vec2d(e.x, e.y);
	}

	public void updateMouseInitialPos(MouseEvent e) {
		initialMousePosition=new Vec2d(e.x, e.y);
	}

	@Override public void mouseUp(MouseEvent event) { mouseDown=false; updateMousePos(event); }
	@Override public void mouseDown(MouseEvent event) { mouseDown=true; updateMousePos(event); updateMouseInitialPos(event); }
	@Override public void mouseMove(MouseEvent event) { updateMousePos(event); }

	@Override
	public void mouseDoubleClick(MouseEvent event) {}

	@Override
	public void keyPressed(KeyEvent event) {}

	@Override
	public void keyReleased(KeyEvent e) {
		String key = Character.toString(e.character);
		if(key.equals("+")) {
			scale += 0.1;
			update();
		}
		if(key.equals("-")) {
			scale -= 0.1;
			update();
		}
		if(e.keyCode == SWT.ESC) {
			selection.clear();
		}

		if(((e.stateMask & SWT.CTRL) == SWT.CTRL) && (e.keyCode == 's')) {
			saveFile(ImageEditor.getInstance().getEditor().openFile);
		}

		if(((e.stateMask & SWT.CTRL) == SWT.CTRL) && (e.keyCode == 'c')) {
			if(selection.sqArea()>0) {
				copyToClipboard(selection);
			}
		}

	}

	public void update() {
		this.toUpdate = true;
	}
	
	protected void initOpenGL() {
		this.glCanvas.setCurrent();
		this.glCapabilities = GL.createCapabilities();

		textureId = GL33.glGenTextures();
		GL33.glEnable(GL33.GL_TEXTURE_2D);
		GL33.glBindTexture(GL33.GL_TEXTURE_2D, textureId);
		GL33.glTexParameterf(GL33.GL_TEXTURE_2D, GL33.GL_TEXTURE_MIN_FILTER, GL33.GL_NEAREST);
		GL33.glTexParameterf(GL33.GL_TEXTURE_2D, GL33.GL_TEXTURE_MAG_FILTER, GL33.GL_NEAREST);
		GL33.glBindTexture(GL33.GL_TEXTURE_2D, 0);

		GL33.glMatrixMode(GL33.GL_PROJECTION);
		GL33.glLoadIdentity();
		GL33.glOrtho(0,1000,1000,0, 0, 1);
		GL33.glEnable(GL33.GL_BLEND);
		GL33.glBlendFunc(GL33.GL_SRC_ALPHA, GL33.GL_ONE_MINUS_SRC_ALPHA);
		GL33.glDisable(GL33.GL_DEPTH_TEST);
		GL33.glDisable(GL33.GL_CULL_FACE);

		GL33.glEnable(GL33.GL_BLEND);
		GL33.glBlendFunc(GL33.GL_SRC_ALPHA, GL33.GL_ONE_MINUS_SRC_ALPHA);

		update();

        this.glCanvas.addDisposeListener((e) -> TextRenderer.cleanUpOpenGL());
	    this.resized = true;
	    this.initialized = true;
	}

	public void updateImage(int[] pixels, int width, int height) {
		GL33.glBindTexture(GL33.GL_TEXTURE_2D, textureId);
		GL33.glTexImage2D(GL33.GL_TEXTURE_2D, 0, GL33.GL_RGBA8, width, height, 0,  GL33.GL_BGRA, GL33.GL_UNSIGNED_INT_8_8_8_8_REV, pixels);
		GL33.glBindTexture(GL33.GL_TEXTURE_2D, 0);
	}

	public void updateImage(PixelMap m) {
		updateImage(m.pix, m.getWidth(), m.getHeight());
	}

	public void render() {

		if (this.isDisposed() || this.glCanvas == null) return;
		
		this.glCanvas.setCurrent();

		if (!initialized) {
			initOpenGL();
		}

		GL33.glClear(GL33.GL_COLOR_BUFFER_BIT);

		if (resized) {
			GL11.glViewport(0, 0, this.getSize().x, this.getSize().y);
	        GL11.glLoadIdentity();
		    GL11.glOrtho(0.0, this.getSize().x, this.getSize().y, 0.0, 0.0, 1.0);
			GL11.glClearColor(0, 0, 0, 1);
		}

		Editor editor = ImageEditor.getInstance().getEditor();

		int w = (int) (rOut.getWidth() * scale);
		int h = (int) (rOut.getHeight() * scale);

		int x = editor.sliderX.getMaximum() - editor.scrollX;
		int y = editor.sliderY.getMaximum() - editor.scrollY;

		if(mousePosition != null) {
			realMousePosition = mousePosition.sub((double) x, (double) y).div(scale);
			if(prevMousePosition == null) {
				prevMousePosition = mousePosition;
			}
			prevRealMousePosition = prevMousePosition.sub((double) x, (double) y).div(scale);
		}

		if (toUpdate) {
			updateNowImage(x,y,w,h);

			toUpdate = false;
		}
		updateNowRender(x, y,w,h);

		if(mouseDown) {
			mouseDragPosCache.add(realMousePosition);

			if(mouseDragPosCache.size() > 5) {

				// TEST TOOL STUFF ---->
				/*
				int c = colorConvert(ImageEditor.getInstance().getEditor().colorPrimary);
				for(Vec2d p : mouseDragPosCache) {
					rOut.set(p.add(1d,0d), c);
					rOut.set(p.add(0d,1d), c);
					rOut.set(p.add(-1d,0d), c);
					rOut.set(p.add(0d,-1d), c);
					rOut.set(p, c);
				}
				*/
				// TEST TOOL STUFF <-----

				mouseDragPosCache.clear();
			}

			// SELECTION START
			Vec2d realInitialMousePosition = initialMousePosition.sub((double) x, (double) y).div(scale);

			if(rOut.getBounds().inBounds(realInitialMousePosition) && rOut.getBounds().inBounds(realMousePosition)) {
				drawRectangleNew(initialMousePosition, mousePosition, 0.2f, 0.2f, 0.35f, 0.5f);
				selection.A = realInitialMousePosition;
				selection.B = realMousePosition;
			}
		}

		if(selection.sqArea() == 0) {
			selection.clear();
		}

		if(!selection.empty()) {
			drawRectangleNew(
					selection.A.mul(scale).add((double) x, (double) y),
					selection.B.mul(scale).add((double) x, (double) y),
					.2f, 0.2f, 0.35f, 0.5f
			);
		}
		// SELECTION END

		glCanvas.swapBuffers();

		editor.sliderY.setMaximum(Math.max(0, h - getSize().y)+1);
		editor.sliderX.setMaximum(Math.max(0, w - getSize().x)+1);

	}

	private void updateNowImage(int x, int y, int w, int h) {
		updateImage(rOut);
	}

	private void updateNowRender(int x, int y, int w, int h) {
		GL33.glBindTexture(GL33.GL_TEXTURE_2D, textureId);

		GL33.glColor4f(1, 1, 1, 1f);

		GL33.glBegin(GL33.GL_QUADS);
		GL33.glTexCoord2f(0, 0);
		GL33.glVertex2f(x, y);
		GL33.glTexCoord2f(1, 0);
		GL33.glVertex2f(x + w, y);
		GL33.glTexCoord2f(1, 1);
		GL33.glVertex2f(x + w, y + h);
		GL33.glTexCoord2f(0, 1);
		GL33.glVertex2f(x, y + h);
		GL33.glEnd();
	}

	public void openFile(File filePath) {
		this.rOut = ImagePixelHelper.getPixFromImage(filePath);

		update();
	}

	public void saveFile(File filePath) {
		int xLenght = rOut.getWidth();
		int yLength = rOut.getHeight();

		BufferedImage bi = new BufferedImage(xLenght, yLength, BufferedImage.TYPE_INT_BGR);

		for(int x = 0; x < xLenght; x++) {
			for(int y = 0; y < yLength; y++) {
				bi.setRGB(x, y, rOut.get(x,y));
			}
		}
		try {
			ImageIO.write(bi, filePath.getName().split("\\.")[filePath.getName().split("\\.").length-1], filePath);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void newFile(int sizeX, int sizeY) {
		int[] pix = new int[sizeX * sizeY];

		Arrays.fill(pix, ColorHelper.colorConvert(255, 0, 0));

		this.rOut = new PixelMap(sizeX, sizeY, pix);

		update();
	}

	public void copyToClipboard(AABB aabb) {
		int xLenght = (int) aabb.getWidth();
		int yLength = (int) aabb.getHeight();

		BufferedImage bi = new BufferedImage(xLenght, yLength, BufferedImage.TYPE_INT_BGR);

		for(int x = 0; x < xLenght; x++) {
			for(int y = 0; y < yLength; y++) {
				bi.setRGB(x, y, rOut.get(x, y, aabb));
			}
		}

		ClipboardUtil.copyToClipboard(bi);
	}

	public static EditorArea getSelf() {
		return ImageEditor.getInstance().getEditor().editorArea;
	}
}
