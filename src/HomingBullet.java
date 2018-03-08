import java.awt.Color;
import java.awt.Dimension;
import java.awt.Polygon;

public class HomingBullet extends Bullet {
	VectorSprite target;
	double targetAngle;

	public HomingBullet(Dimension screenSize, Double xPos, Double yPos,
			Double angle, VectorSprite target) {
		super(screenSize, xPos, yPos, angle - Math.PI / 2);
		this.target = target;
		lifeSpan = 500;
		turnAmount = 0.075;//Default 0.075
		speed = 7; //Default 7
		xSpeed = Math.cos(angle - Math.PI / 2) * speed;
		ySpeed = Math.sin(angle - Math.PI / 2) * speed;
		color = new Color(5, 5, 255);
		shape = new Polygon();
		shape.addPoint(3, 0);
		shape.addPoint(0, 3);
		shape.addPoint(-5,3);
		shape.addPoint(-5, -3);
		shape.addPoint(0, -3);
		drawShape = new Polygon();
		drawShape.addPoint(3, 0);
		drawShape.addPoint(0, 3);
		drawShape.addPoint(-5,3);
		drawShape.addPoint(-5, -3);
		drawShape.addPoint(0, -3);
	}

	public void updatePosition() {
		if (angle > Math.PI) {
			angle -= Math.PI * 2;
		} else if (angle < -Math.PI * 2) {
			angle += Math.PI * 2;
		}
		targetAngle = Math.atan2(target.yPos - yPos, target.xPos - xPos);
		targetAngle -= angle;
		if (targetAngle > Math.PI) {
			targetAngle -= Math.PI * 2;
		} else if (targetAngle < -Math.PI * 2) {
			targetAngle += Math.PI * 2;
		}
		xSpeed = Math.cos(angle) * speed;
		ySpeed = Math.sin(angle) * speed;
		angle += targetAngle * turnAmount;
		if (!target.active)
		this.active = false;
		super.updatePosition();
	}
	public void wrapAround(){
		
	}
}
