package dopt.tsp;

import java.util.List;

import javax.swing.JFrame;

import com.google.common.base.Joiner;

class Result {
	final double distance;
	final List<Integer> route;
	final List<Point> points;
	final Joiner joiner = Joiner.on(" ");

	Result(double distance, List<Integer> route, List<Point> points) {
		this.distance = distance;
		this.route = route;
		this.points = points;
	}

	void display() {
		JFrame pane = new JFrame();
		pane.add(new TSPDisplay(points, route));
		pane.pack();
		pane.setVisible(true);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(distance).append(" 0\n");
		builder.append(joiner.join(route));
		return builder.toString();
	}
}