package dopt.tsp;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.List;

import javax.swing.JPanel;

public class Display extends JPanel {

	private static final long serialVersionUID = 1L;
	private List<Point> points;
	private List<Integer> result;
	private double minX = Double.MAX_VALUE, maxX = Double.MIN_VALUE;
	private double minY = Double.MAX_VALUE, maxY = Double.MIN_VALUE;

	int[] xpos;
	int[] ypos;

	public Display(List<Point> points, List<Integer> result) {
		this.points = points;
		this.result = result;
		for (Point point : points) {
			if (point.x > maxX)
				maxX = point.x;
			if (point.y > maxY)
				maxY = point.y;
			if (point.x < minX)
				minX = point.x;
			if (point.y < minY)
				minY = point.y;
		}

		xpos = new int[points.size()];
		ypos = new int[points.size()];
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		int m = (int) (0.05 * g.getClipBounds().width);
		int gh = g.getClipBounds().height - (2 * m);
		int gw = g.getClipBounds().width - (2 * m);

		double rh = maxY - minY;
		double rw = maxX - minX;

		double uh = gh / rh;
		double uw = gw / rw;

		for (Point point : points) {
			xpos[point.id] = m + (int) Math.round((point.x - minX) * uw);
			ypos[point.id] = m + (int) Math.round((point.y - minY) * uh);
			g.setColor(Color.BLACK);
			g.drawString(Integer.toString(point.id), xpos[point.id] - 5, ypos[point.id] - 5);
			g.setColor(Color.RED);
			g.fillOval(xpos[point.id]-3, ypos[point.id]-3, 6, 6);
		}

		g.setColor(Color.RED);
		
		Integer last = null;
		Integer first = null;

		for (Integer p : result) {
			if (last == null) {
				first = p;
			} else {
				g.drawLine(xpos[last], ypos[last], xpos[p], ypos[p]);
			}
			last = p;
		}
		g.drawLine(xpos[last], ypos[last], xpos[first], ypos[first]);
	}

	public Dimension getPreferredSize() {
		return new Dimension(400, 400);
	}
}
