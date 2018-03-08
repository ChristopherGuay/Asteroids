import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

public class FloatingPoints extends VectorSprite {
	String pointString;
	int lifeSpan;
	int value;

	public FloatingPoints(Dimension screenSize, double xPos, double yPos,
			int value) {
		super(screenSize);
		this.xPos = xPos;
		this.yPos = yPos;
		pointString = "" + value;
		xSpeed = 0;
		ySpeed = -1;
		lifeSpan = 50;
		this.value = value;
	}

	public void paint(Graphics g) {
		if (value > 0){
		g.setColor(new Color(124, 252, 0));
		g.drawString(pointString, (int) xPos, (int) yPos);
		} else {
			g.setColor(Color.red);
			g.drawString(pointString, (int) xPos, (int) yPos);
		}
	}
	public void updatePosition(){
		super.updatePosition();
		if (counter >lifeSpan)
			active = false;
	}
}
