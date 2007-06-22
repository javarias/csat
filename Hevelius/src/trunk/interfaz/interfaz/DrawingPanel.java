import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import java.util.*;
import javax.media.opengl.*;
import com.sun.opengl.util.*;

public class DrawingPanel extends JPanel
{
	public static int gearDisplayList;
	private Dimension dim;
	private Image img = null;
	private Image rArrow = null;
	private Image lArrow = null;
	private Image tArrow = null;
	private Image bArrow = null;
	private int pointx=0;
	private int pointy=0;
	private JPanel tp;
	private GLCanvas canvas;
	private Listener list;
	public DrawingPanel(LayoutManager l)
	{
		super(l);
		tp = new JPanel();
		tp.setLayout(null);
		add(tp);
		GLCapabilities capabilities = new GLCapabilities();
		canvas = new GLCanvas(capabilities);
		list = new Listener();
		canvas.addGLEventListener(list);
		canvas.setLocation(0,0);
		canvas.setSize(100,100);
		tp.add(canvas);

		JDialog dialog = new JDialog(interfaz.frame,"Telescopio");
		dialog.getContentPane().setLayout(null);
		GLCanvas canvas2 = new GLCanvas(null,null,canvas.getContext(),null);
		canvas2.setSize(600,600);
		canvas2.setLocation(0,0);
		canvas2.addGLEventListener(list);
		dialog.getContentPane().add(canvas2);
		dialog.pack();
		dialog.setSize(600,600);
		dialog.setLocation(100,100);
		//dialog.setResizable(false);
		final Animator animator = new Animator(canvas);
		final Animator animator2 = new Animator(canvas2);
		list.setDialog(dialog,animator2);
		WindowListener windowListener = new WindowAdapter()
		{
			public void windowClosing(WindowEvent e) 
			{
				new Thread(new Runnable() 
						{
							public void run() 
				{
					animator.stop();
					System.exit(0);
				}
				}).start();
			}
		};
		WindowListener windowListener2 = new WindowAdapter()
		{
			public void windowClosing(WindowEvent e)
			{
				new Thread(new Runnable()
						{
							public void run()
				{
					animator2.stop();
				}
				}).start();
			}
		};
		dialog.addWindowListener(windowListener2);
		interfaz.frame.addWindowListener(windowListener);
		animator.start();
		//animator2.start();
	}
	public void setImage(String img, Dimension dim)
	{
		this.img = Toolkit.getDefaultToolkit().getImage(img);
		this.img = this.img.getScaledInstance(dim.width,dim.height,Image.SCALE_FAST);
	}
	public void setArrows(Dimension dim)
	{
		rArrow = Toolkit.getDefaultToolkit().getImage("rArrow.jpg");
		rArrow = Transparency.makeColorTransparent(rArrow, Color.BLACK);
		rArrow = rArrow.getScaledInstance(dim.width,dim.height,Image.SCALE_FAST);
		lArrow = Toolkit.getDefaultToolkit().getImage("lArrow.jpg");
		lArrow = Transparency.makeColorTransparent(lArrow, Color.BLACK);
		lArrow = lArrow.getScaledInstance(dim.width,dim.height,Image.SCALE_FAST);
		tArrow = Toolkit.getDefaultToolkit().getImage("tArrow.jpg");
		tArrow = Transparency.makeColorTransparent(tArrow, Color.BLACK);
		tArrow = tArrow.getScaledInstance(dim.width,dim.height,Image.SCALE_FAST);
		bArrow = Toolkit.getDefaultToolkit().getImage("bArrow.jpg");
		bArrow = Transparency.makeColorTransparent(bArrow, Color.BLACK);
		bArrow = bArrow.getScaledInstance(dim.width,dim.height,Image.SCALE_FAST);
	}
	public void setCompassPoints(double dec)
	{
		int dy = getSize().height;
		int dx = getSize().width;
		int rect_x = dx/2;
		int rect_y = dy/2;
		int cx = 30+(dy-rect_y)/4;
		int cy = (dy-dy/3)+(dy-rect_y)/4-20;
		int r = (dy-rect_y)/4;
		double theta = dec*Math.PI/180;
		pointx = (int)(((double)r)*Math.cos(theta)+(double)cx);
		pointy = (int)(-((double)r)*Math.sin(theta)+(double)cy);
		Rectangle re = new Rectangle(30,dy-dy/3-20,(dy-rect_y)/2,(dy-rect_y)/2);
		paintImmediately(re);
	}
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);

		int dy = getSize().height;
		int dx = getSize().width;
		int rect_x = dx/2;
		int rect_y = dy/2;
		g.setColor(Color.GRAY);

		int oGLx = (dx-rect_x)/2+rect_x+20;
		g.drawRect(oGLx+20,10,dx-oGLx-40,dx-oGLx-40);
		g.fillRect(oGLx+20,10,dx-oGLx-40,dx-oGLx-40);

		g.drawRect((dx-rect_x)/2,(dy-rect_y)/2,rect_x,rect_y);
		g.fillRect((dx-rect_x)/2,(dy-rect_y)/2,rect_x,rect_y);

		g.drawOval(30,dy-dy/3-20,(dy-rect_y)/2,(dy-rect_y)/2);
		g.fillOval(30,dy-dy/3-20,(dy-rect_y)/2,(dy-rect_y)/2);

		g.setColor(Color.BLACK);

		g.fillRect((dx-rect_x)/2+10,(dy-rect_y)/2+10,rect_x-20,rect_y-20);
		g.fillOval(30+5,dy-dy/3+5-20,(dy-rect_y)/2-10,(dy-rect_y)/2-10);
		g.fillRect(oGLx+25,15,dx-oGLx-50,dx-oGLx-50);

		g.drawImage(lArrow, (dx-rect_x)/2-50, dy/2-20,this);
		g.drawImage(rArrow, (dx+rect_x)/2+10, dy/2-20,this);
		g.drawImage(tArrow, dx/2-20, (dy-rect_y)/2-50,this);
		g.drawImage(bArrow, dx/2-20, (dy+rect_y)/2+10,this);
		g.drawImage(img,(dx-rect_x)/2+10,(dy-rect_y)/2+10,this);

		g.setColor(Color.RED);
		int cx = 30+(dy-rect_y)/4;
		int cy = (dy-dy/3)+(dy-rect_y)/4-20;
		g.drawLine(cx,cy,pointx,pointy);
		g.drawLine(cx+1,cy+1,pointx+1,pointy+1);

		g.setColor(Color.BLUE);
		g.drawOval(cx-5,cy-5,10,10);
		g.fillOval(cx-5,cy-5,10,10);
		
		tp.setLocation(oGLx+25,15);
		tp.setSize(dx-oGLx-50,dx-oGLx-50);
		canvas.setSize(dx-oGLx-50,dx-oGLx-50);
	}
	public Dimension getDim()
	{
		return getSize();
	}
	public static synchronized void initializeDisplayList(GL gl) {
		gearDisplayList = gl.glGenLists(1);
		gl.glNewList(gearDisplayList, GL.GL_COMPILE);
		float red[] = { 0.8f, 0.1f, 0.0f, 1.0f };
		gl.glMaterialfv(GL.GL_FRONT, GL.GL_AMBIENT_AND_DIFFUSE, red, 0);
		gear(gl, 1.0f, 4.0f, 1.0f, 20, 0.7f);
		gl.glEndList();
	}

	private static void gear(GL gl,
			float inner_radius,
			float outer_radius,
			float width,
			int teeth,
			float tooth_depth)
	{
		int i;
		float r0, r1, r2;
		float angle, da;
		float u, v, len;
		
		float red[] = { 0.8f, 0.1f, 0.0f, 1.0f };
		float blue[] = { 0.5f, 0.8f, 0.4f, 1.0f };

		r0 = inner_radius;
		r1 = outer_radius - tooth_depth / 2.0f;
		r2 = outer_radius + tooth_depth / 2.0f;

		da = 2.0f * (float) Math.PI / teeth / 4.0f;

		gl.glShadeModel(GL.GL_FLAT);

		gl.glNormal3f(0.0f, 0.0f, 1.0f);

		/* draw front face */
		gl.glBegin(GL.GL_QUAD_STRIP);
		for (i = 0; i <= teeth; i++)
		{
			angle = i * 2.0f * (float) Math.PI / teeth;
			gl.glVertex3f(r0 * (float)Math.cos(angle), r0 * (float)Math.sin(angle), width * 0.5f);
			gl.glVertex3f(r1 * (float)Math.cos(angle), r1 * (float)Math.sin(angle), width * 0.5f);
			if(i < teeth)
			{
				gl.glVertex3f(r0 * (float)Math.cos(angle), r0 * (float)Math.sin(angle), width * 0.5f);
				gl.glVertex3f(r1 * (float)Math.cos(angle + 3.0f * da), r1 * (float)Math.sin(angle + 3.0f * da), width * 0.5f);
			}
		}
		gl.glEnd();

		/* draw front sides of teeth */
		gl.glBegin(GL.GL_QUADS);
		for (i = 0; i < teeth; i++)
		{
			if(i==0)
				gl.glMaterialfv(GL.GL_FRONT, GL.GL_AMBIENT_AND_DIFFUSE, blue, 0);
			angle = i * 2.0f * (float) Math.PI / teeth;
			gl.glVertex3f(r1 * (float)Math.cos(angle), r1 * (float)Math.sin(angle), width * 0.5f);
			gl.glVertex3f(r2 * (float)Math.cos(angle + da), r2 * (float)Math.sin(angle + da), width * 0.5f);
			gl.glVertex3f(r2 * (float)Math.cos(angle + 2.0f * da), r2 * (float)Math.sin(angle + 2.0f * da), width * 0.5f);
			gl.glVertex3f(r1 * (float)Math.cos(angle + 3.0f * da), r1 * (float)Math.sin(angle + 3.0f * da), width * 0.5f);
			if(i==0)
				gl.glMaterialfv(GL.GL_FRONT, GL.GL_AMBIENT_AND_DIFFUSE, red, 0);
		}
		gl.glEnd();

		/* draw back face */
		gl.glBegin(GL.GL_QUAD_STRIP);
		for (i = 0; i <= teeth; i++)
		{
			angle = i * 2.0f * (float) Math.PI / teeth;
			gl.glVertex3f(r1 * (float)Math.cos(angle), r1 * (float)Math.sin(angle), -width * 0.5f);
			gl.glVertex3f(r0 * (float)Math.cos(angle), r0 * (float)Math.sin(angle), -width * 0.5f);
			gl.glVertex3f(r1 * (float)Math.cos(angle + 3 * da), r1 * (float)Math.sin(angle + 3 * da), -width * 0.5f);
			gl.glVertex3f(r0 * (float)Math.cos(angle), r0 * (float)Math.sin(angle), -width * 0.5f);
		}
		gl.glEnd();

		/* draw back sides of teeth */
		gl.glBegin(GL.GL_QUADS);
		for (i = 0; i < teeth; i++)
		{
			angle = i * 2.0f * (float) Math.PI / teeth;
			gl.glVertex3f(r1 * (float)Math.cos(angle + 3 * da), r1 * (float)Math.sin(angle + 3 * da), -width * 0.5f);
			gl.glVertex3f(r2 * (float)Math.cos(angle + 2 * da), r2 * (float)Math.sin(angle + 2 * da), -width * 0.5f);
			gl.glVertex3f(r2 * (float)Math.cos(angle + da), r2 * (float)Math.sin(angle + da), -width * 0.5f);
			gl.glVertex3f(r1 * (float)Math.cos(angle), r1 * (float)Math.sin(angle), -width * 0.5f);
		}
		gl.glEnd();

		/* draw outward faces of teeth */
		gl.glBegin(GL.GL_QUAD_STRIP);
		for (i = 0; i < teeth; i++)
		{
			angle = i * 2.0f * (float) Math.PI / teeth;
			gl.glVertex3f(r1 * (float)Math.cos(angle), r1 * (float)Math.sin(angle), width * 0.5f);
			gl.glVertex3f(r1 * (float)Math.cos(angle), r1 * (float)Math.sin(angle), -width * 0.5f);
			u = r2 * (float)Math.cos(angle + da) - r1 * (float)Math.cos(angle);
			v = r2 * (float)Math.sin(angle + da) - r1 * (float)Math.sin(angle);
			len = (float)Math.sqrt(u * u + v * v);
			u /= len;
			v /= len;
			gl.glNormal3f(v, -u, 0.0f);
			gl.glVertex3f(r2 * (float)Math.cos(angle + da), r2 * (float)Math.sin(angle + da), width * 0.5f);
			gl.glVertex3f(r2 * (float)Math.cos(angle + da), r2 * (float)Math.sin(angle + da), -width * 0.5f);
			gl.glNormal3f((float)Math.cos(angle), (float)Math.sin(angle), 0.0f);
			gl.glVertex3f(r2 * (float)Math.cos(angle + 2 * da), r2 * (float)Math.sin(angle + 2 * da), width * 0.5f);
			gl.glVertex3f(r2 * (float)Math.cos(angle + 2 * da), r2 * (float)Math.sin(angle + 2 * da), -width * 0.5f);
			u = r1 * (float)Math.cos(angle + 3 * da) - r2 * (float)Math.cos(angle + 2 * da);
			v = r1 * (float)Math.sin(angle + 3 * da) - r2 * (float)Math.sin(angle + 2 * da);
			gl.glNormal3f(v, -u, 0.0f);
			gl.glVertex3f(r1 * (float)Math.cos(angle + 3 * da), r1 * (float)Math.sin(angle + 3 * da), width * 0.5f);
			gl.glVertex3f(r1 * (float)Math.cos(angle + 3 * da), r1 * (float)Math.sin(angle + 3 * da), -width * 0.5f);
			gl.glNormal3f((float)Math.cos(angle), (float)Math.sin(angle), 0.0f);
		}
		gl.glVertex3f(r1 * (float)Math.cos(0), r1 * (float)Math.sin(0), width * 0.5f);
		gl.glVertex3f(r1 * (float)Math.cos(0), r1 * (float)Math.sin(0), -width * 0.5f);
		gl.glEnd();

		gl.glShadeModel(GL.GL_SMOOTH);

		/* draw inside radius cylinder */
		gl.glBegin(GL.GL_QUAD_STRIP);
		for (i = 0; i <= teeth; i++)
		{
			angle = i * 2.0f * (float) Math.PI / teeth;
			gl.glNormal3f(-(float)Math.cos(angle), -(float)Math.sin(angle), 0.0f);
			gl.glVertex3f(r0 * (float)Math.cos(angle), r0 * (float)Math.sin(angle), -width * 0.5f);
			gl.glVertex3f(r0 * (float)Math.cos(angle), r0 * (float)Math.sin(angle), width * 0.5f);
		}
		gl.glEnd();
	}
}
