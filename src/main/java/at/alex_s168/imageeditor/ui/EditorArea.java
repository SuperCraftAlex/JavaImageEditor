package at.alex_s168.imageeditor.ui;

import at.alex_s168.imageeditor.ImageEditor;
import at.alex_s168.imageeditor.PixelStorage;
import at.alex_s168.imageeditor.api.AABB;
import at.alex_s168.imageeditor.api.PixelMap;
import at.alex_s168.imageeditor.features.keybinds.FeatureKeybinds;
import at.alex_s168.imageeditor.util.ClipboardUtil;
import at.alex_s168.imageeditor.util.ColorHelper;
import at.alex_s168.imageeditor.util.ImagePixelHelper;
import static at.alex_s168.imageeditor.util.GLHelper.*;

import at.alex_s168.imageeditor.util.VecUtil;
import de.m_marvin.logicsim.ui.TextRenderer;
import de.m_marvin.univec.impl.Vec2d;
import de.m_marvin.univec.impl.Vec2i;
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

	protected boolean mouseDown = false;

	protected Vec2i initialMousePosition;

	protected Vec2i mousePosition;
	protected Vec2i prevMousePosition;
	protected Vec2i realMousePosition;
	protected Vec2i prevRealMousePosition;

	public AABB selection = new AABB();

	public double scale = 1;

	public List<Vec2i> mouseDragPosCache = new ArrayList<>();

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
		mousePosition=new Vec2i(e.x, e.y);
	}

	public void updateMouseInitialPos(MouseEvent e) {
		initialMousePosition=new Vec2i(e.x, e.y);
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
		FeatureKeybinds.keyReleased(e);
	}
	
	protected void initOpenGL() {
		this.glCanvas.setCurrent();
		this.glCapabilities = GL.createCapabilities();

		GL33.glMatrixMode(GL33.GL_PROJECTION);
		GL33.glLoadIdentity();
		GL33.glOrtho(0,10_000,10_000,0, 0, 1);
		GL33.glEnable(GL33.GL_BLEND);
		GL33.glBlendFunc(GL33.GL_SRC_ALPHA, GL33.GL_ONE_MINUS_SRC_ALPHA);
		GL33.glDisable(GL33.GL_DEPTH_TEST);
		GL33.glDisable(GL33.GL_CULL_FACE);

		GL33.glEnable(GL33.GL_BLEND);
		GL33.glBlendFunc(GL33.GL_SRC_ALPHA, GL33.GL_ONE_MINUS_SRC_ALPHA);

        this.glCanvas.addDisposeListener((e) -> TextRenderer.cleanUpOpenGL());
	    this.resized = true;
	    this.initialized = true;
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

		//todo: scroll
		int x = 1 / (editor.scrollX + 1);
		int y = 1 / (editor.scrollY + 1);

		if(mousePosition != null) {
			realMousePosition = VecUtil.div(mousePosition.sub(x, y), scale);
			if(prevMousePosition == null) {
				prevMousePosition = mousePosition;
			}
			prevRealMousePosition = VecUtil.div(prevMousePosition.sub(x, y), scale);
		}

		PixelStorage.getSelf().tick(x, y, scale);

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
			Vec2i realInitialMousePosition = VecUtil.div(initialMousePosition.sub(x, y), scale);

			if(PixelStorage.getSelf().visibleAABB.inBounds(realInitialMousePosition) && PixelStorage.getSelf().visibleAABB.inBounds(realMousePosition)) {
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
					VecUtil.mul(selection.A.add(x, y), scale),
					VecUtil.mul(selection.B.add(x, y), scale),
					0.2f, 0.2f, 0.35f, 0.5f
			);
		}
		// SELECTION END

		glCanvas.swapBuffers();

		editor.sliderY.setMaximum(Math.max(0, PixelStorage.getSelf().visibleAABB.getHeight() - getSize().y)+1);
		editor.sliderX.setMaximum(Math.max(0, PixelStorage.getSelf().visibleAABB.getWidth()  - getSize().x)+1);

	}

	public void openFile(File filePath) {
		PixelStorage.getSelf().reset(ImagePixelHelper.getPixFromImage(filePath));

		PixelMap m = new PixelMap(100, 100);
		m.pos = new Vec2i(100, 100);
		Arrays.fill(m.pix, ColorHelper.colorConvert(255, 255, 255, 100));

		PixelStorage.getSelf().addLayer(m);

		PixelStorage.getSelf().updateCurrent();
	}

	public void saveFile(File filePath) {
		long timeStartOverlay = System.currentTimeMillis();
		PixelMap m = PixelStorage.getSelf().overlayVisible();
		long timeEndOverlay = System.currentTimeMillis();

		System.out.println("Total overlay time: " + (timeEndOverlay-timeStartOverlay) + "ms");

		int xLenght = m.getWidth();
		int yLength = m.getHeight();

		BufferedImage bi = new BufferedImage(xLenght, yLength, BufferedImage.TYPE_INT_BGR);

		for(int x = 0; x < xLenght; x++) {
			for(int y = 0; y < yLength; y++) {
				bi.setRGB(x, y, m.get(x,y));
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

		Arrays.fill(pix, ColorHelper.colorConvert(255, 255, 255, 255));

		PixelStorage.getSelf().reset(new PixelMap(sizeX, sizeY, pix));

		PixelStorage.getSelf().updateCurrent();
	}

	public void copyToClipboard(AABB aabb) {
		int xLenght = aabb.getWidth();
		int yLength = aabb.getHeight();

		BufferedImage bi = new BufferedImage(xLenght, yLength, BufferedImage.TYPE_INT_BGR);

		for(int x = 0; x < xLenght; x++) {
			for(int y = 0; y < yLength; y++) {
				bi.setRGB(x, y, PixelStorage.getSelf().getCurrentPixelMap().get(x, y, aabb));
			}
		}

		ClipboardUtil.copyToClipboard(bi);
	}

	public static EditorArea getSelf() {
		return ImageEditor.getInstance().getEditor().editorArea;
	}
}
