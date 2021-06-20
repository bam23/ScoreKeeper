import java.awt.LayoutManager;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Rectangle;
import java.util.Vector;


public class Layout implements LayoutManager {
	private Vector<Rectangle> rectangle = new Vector<Rectangle>();
	private Vector<Component> componets = new Vector <Component>();
	
	@Override
	public void addLayoutComponent(String name, Component comp) {
		
		int i = name.indexOf(" ");
		
		if (i == -1) {
			return;
		}
		
		int x = Integer.valueOf(name.substring(0,i)).intValue();
		
		if(x < 0 || x > 100) {
			return ;
		}

		int i2 = name.indexOf(" ", i+1);
		
		if(i2 == -1) {
			return;
		}
		
		int y = Integer.valueOf(name.substring(i+1,i2)).intValue();
		
		if(y < 0 || y > 100) {
			return ;
		}

		int i3 = name.indexOf(" ", i2+1);
		
		if(i3 ==  -1) {
			return;
		}
		
		int x2 = Integer.valueOf(name.substring(i2+1,i3)).intValue();
		
		if(x2 < 0 || x2 > 100) {
			return ;
		}

		int y2 = Integer.valueOf(name.substring(i3+1,name.length())).intValue();
		
		if(y2 < 0 || y2 > 100) {
			return ;
		}
		
		rectangle.addElement(new Rectangle(x,y,x2,y2));
		componets.addElement(comp);
	
	}

	@Override
	public void layoutContainer(Container comp) {
		Rectangle r;
		Insets insets = comp.getInsets();
		int w = comp.getSize().width - (insets.left + insets.right);
		int h = comp.getSize().height - (insets.top + insets.bottom);
		int x = insets.left;
		int y = insets.top;
		int x2;
		int y2;
		int h2;
		int w2;
		
		for (int i = 0; i < componets.size(); i++){
			r = (Rectangle)rectangle.elementAt(i);
			x2=x + ((w*r.x)/100);
			y2=y + ((h*r.y)/100);
			w2=(x + ((w*r.width)/100))-(x+((w*r.x)/100));
			h2=(y + ((h*r.height)/100))-(y+((h*r.y)/100));
			
			((Component)componets.elementAt(i)).setBounds(x2,y2,w2,h2);
		}
	}

	@Override
	public Dimension minimumLayoutSize(Container parent) {
		int xsize = 0;
		int ysize = 0;
		Dimension d;

		
		for(int i = 0; i< componets.size(); i++){
			d = ((Component) componets.elementAt(i)).getMinimumSize();
			
			if(d.width > xsize) { 
				xsize = d.width;
			}
			
			if(d.height > ysize) {
				ysize = d.height;
			}
		}
		
		Insets insets = parent.getInsets();
		
		int w = xsize + insets.left + insets.right;
		int h = ysize + insets.top + insets.bottom;
		
		return new Dimension(w,h);
	}

	@Override
	public Dimension preferredLayoutSize(Container parent) {
		int xsize = 0;
		int ysize = 0;
		
		Dimension dimension;
		for(int i = 0; i < componets.size(); i++)
		{
			dimension = ((Component) componets.elementAt(i)).getPreferredSize();
			
			if(dimension.width > xsize) {
				xsize = dimension.width;
			}
			
			if(dimension.height > ysize) {
				ysize = dimension.height;
			}
		}
		
		Insets insects = parent.getInsets();
		int w = xsize + insects.left + insects.right;
		int h = ysize + insects.top + insects.bottom;
		
		return new Dimension(w,h);
	}

	@Override
	public void removeLayoutComponent(Component comp) {
		int i = componets.indexOf(comp);
		
		try{
				componets.removeElementAt(i);
				rectangle.removeElementAt(i);
				
		}catch (ArrayIndexOutOfBoundsException e){
			System.err.println("Error " + e);
		}
		
	}

}
