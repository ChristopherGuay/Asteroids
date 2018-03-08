import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Polygon;

public class VectorSprite {
	double xPos, yPos;
	double xSpeed, ySpeed;
	double angle;
	double speed;
	double turnAmount;
	Polygon shape, drawShape;
	Dimension screenSize;
	Color color;
	boolean active;
	int counter;

	public VectorSprite(Dimension screenSize) {
		this.screenSize = screenSize;
		shape = new Polygon();
		shape.addPoint(0, -25);
		shape.addPoint(15, 10);
		shape.addPoint(-15, 10);
		drawShape = new Polygon();
		drawShape.addPoint(0, -25);
		drawShape.addPoint(15, 10);
		drawShape.addPoint(-15, 10);
		speed = 3;
		counter = 0;
		turnAmount = .1;
		color = (new Color(139, 115, 85));
		active = true;
	}

	public void paint(Graphics g) {
		g.setColor(color);
		g.fillPolygon(drawShape);
	}

	public void updatePosition() {
		counter ++;
		xPos += xSpeed;
		yPos += ySpeed;
		wrapAround();
		int x, y;
		for (int i = 0; i < shape.npoints; i++) {
			x = (int) Math.round(shape.xpoints[i] * Math.cos(angle)
					- shape.ypoints[i] * Math.sin(angle));
			y = (int) Math.round(shape.xpoints[i] * Math.sin(angle)
					+ shape.ypoints[i] * Math.cos(angle));
			drawShape.xpoints[i] = x;
			drawShape.ypoints[i] = y;
		}
		drawShape.translate((int) xPos, (int) yPos);
		drawShape.invalidate();
	}

	public void wrapAround() {
		if (xPos > screenSize.getWidth())
			xPos = 0;
		if (xPos < 0)
			xPos = screenSize.getWidth();
		if (yPos > screenSize.getHeight())
			yPos = 0;
		if (yPos < 0)
			yPos = screenSize.getHeight();

	}

	public boolean collidingWith(VectorSprite v) {
		int x, y;
		for (int i = 0; i < this.drawShape.npoints; i++) {
			x = this.drawShape.xpoints[i];
			y = this.drawShape.ypoints[i];
			if (v.drawShape.contains(x, y)) {
				return true;
			}
		}
		for (int i = 0; i < v.drawShape.npoints; i++) {
			x = v.drawShape.xpoints[i];
			y = v.drawShape.ypoints[i];
			if (this.drawShape.contains(x, y)) {
				return true;
			}
		}
		return false;
	}

}
