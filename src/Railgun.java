import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Polygon;

public class Railgun extends Bullet {

	public Railgun(Dimension screenSize, Double xPos, Double yPos, Double angle) {
		super(screenSize, xPos, yPos, angle);
		speed = 25;// 25
		lifeSpan = 4;// 4
		xSpeed = Math.cos(angle - Math.PI / 2) * speed;
		ySpeed = Math.sin(angle - Math.PI / 2) * speed;
		color = new Color(255, 5, 5);
		shape = new Polygon();
		shape.addPoint(8, (int) speed); //8
		shape.addPoint(8, -10000);
		shape.addPoint(-8, -10000);
		shape.addPoint(-8, (int) speed);
		drawShape = new Polygon();
		drawShape.addPoint(8, (int) speed);
		drawShape.addPoint(8, -10000);
		drawShape.addPoint(-8, -10000);
		drawShape.addPoint(-8, (int) speed);
	}

	public void wrapAround() {

	}

	public void paint(Graphics g) {
		g.setColor(color);
		g.fillPolygon(drawShape);
	}
}
