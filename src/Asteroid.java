import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Polygon;
import java.util.Random;

public class Asteroid extends VectorSprite {
	Random random;
	double scale;

	public Asteroid(Dimension screenSize) {
		super(screenSize);
		scale = 5;
		initialize();
		}
	public Asteroid(Dimension screenSize,Double xPos,Double yPos,Double scale){
		super(screenSize);
		this.scale = scale;
		initialize();
		this.xPos=xPos;
		this.yPos=yPos;
	}
	public void initialize(){
		active = true;
		speed = 3;
		turnAmount = (Math.random() - .5)*.05;
		double a, h;
		h = Math.random();
		a = Math.random() * 2 * Math.PI;
		xSpeed = Math.cos(a) * h * speed;
		ySpeed = Math.sin(a) * h * speed;
		random = new Random();
		xPos = Math.random()*screenSize.getWidth();
		yPos = Math.random()*screenSize.getHeight();
		int numPoints = random.nextInt(10) + 5;
		double degDiff = (2 * Math.PI) / numPoints;
		shape = new Polygon();
		drawShape = new Polygon();
		int c = random.nextInt(100)+25;
		color = new Color(c+50, c+20, c);
		for (int i = 0; i<numPoints; i++){
			double radius = (random.nextInt(8)+8)*scale;
			int x = (int)Math.round(Math.cos(degDiff*i)*radius);
			int y = (int)Math.round(Math.sin(degDiff*i)*radius);
			shape.addPoint(x,y);
			drawShape.addPoint(x,y);
		}
	}

	public void paint(Graphics g) {
		g.setColor(color);
		g.fillPolygon(drawShape);
	}
	public void updatePosition(){
		angle += turnAmount;
		super.updatePosition();
	}
}
