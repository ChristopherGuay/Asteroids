import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Polygon;

public class Bullet extends VectorSprite {
	int lifeSpan = 200; // Normal span 200

	public Bullet(Dimension screenSize, Double xPos, Double yPos, Double angle) {
		super(screenSize);
		this.xPos = xPos;
		this.yPos = yPos;
		this.angle = angle;
		speed = 7; // Normal 7
		xSpeed = Math.cos(angle - Math.PI / 2) * speed;
		ySpeed = Math.sin(angle - Math.PI / 2) * speed;
		color = new Color(255, 5, 5);
		shape = new Polygon();
		shape.addPoint(0, -3);
		shape.addPoint(3, 0);
		shape.addPoint(3, 5);
		shape.addPoint(-3, 5);
		shape.addPoint(-3, 0);
		drawShape = new Polygon();
		drawShape.addPoint(0, -3);
		drawShape.addPoint(3, 0);
		drawShape.addPoint(3, 5);
		drawShape.addPoint(-3, 5);
		drawShape.addPoint(-3, 0);

	}

	public void paint(Graphics g) {
		super.paint(g);
		g.setColor(new Color(127, 255, 0));
		g.drawPolygon(drawShape);
	}
	public void updatePosition() {
		super.updatePosition();
		if (counter > lifeSpan)
			active = false;
	}
}
