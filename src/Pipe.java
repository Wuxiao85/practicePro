import bagel.DrawOptions;
import bagel.Image;
import bagel.util.Rectangle;

import java.nio.file.Paths;
import java.util.List;

public class Pipe extends AbstractElement{
    private String level;
    private double x;
    private double y;
    private Image imageTop;
    private Image imageBottom;
    private Rectangle rectangleTop;
    private Rectangle rectangleBottom;
    private double v;
    private int score;
    private boolean crossed;
    public Pipe(String level) {
        this.level = level;
        imageTop = new Image(Paths.get(ConfigLoader.RESOURCE_DIR, level, ConfigLoader.PIPE).toString());
        imageBottom = new Image(Paths.get(ConfigLoader.RESOURCE_DIR, level, ConfigLoader.PIPE).toString());
        x = ConfigLoader.WINDOWX;
        y = ConfigLoader.LOCTYPE[(int)(Math.random() * ConfigLoader.LOCTYPE.length)];
        rectangleTop = getRectangle(imageTop, x, - (imageTop.getHeight() - y));
        rectangleBottom = getRectangle(imageBottom, x, y + ConfigLoader.GAP);
        v = - ConfigLoader.VELOCITY;
        score = 1;
        crossed = false;
    }

    @Override
    public void draw() {
        imageTop.drawFromTopLeft(x, - (imageTop.getHeight() - y));
        imageBottom.drawFromTopLeft(x, y+ConfigLoader.GAP, new DrawOptions().setRotation(Math.PI));
    }

    @Override
    public void move() {
        x = x + v;
        rectangleBottom = getRectangle(imageBottom, x, y + ConfigLoader.GAP);
        rectangleTop = getRectangle(imageTop, x,  - (imageTop.getHeight() - y));
    }

    @Override
    public Rectangle getRectangle() {
        return rectangleTop;
    }

    @Override
    public boolean collision(Element other) {
        if (other.getRectangle() == null) return false;
        rectangleTop = getRectangle(imageTop, x, - (imageTop.getHeight() - y));
        rectangleBottom = getRectangle(imageBottom, x, y+ConfigLoader.GAP);
        if (other.getRectangle().intersects(rectangleTop) || other.getRectangle().intersects(rectangleBottom)) {
            other.dealWithCollision(this);
            return true;
        }
        return false;
    }

    @Override
    public void changeVelocity(double velocity) {
        v = - velocity;
    }

    @Override
    public void levelChange(String level) {
        this.level = level;
    }

    public int getScore() {
        if (!crossed) {
            crossed = true;
            return score;
        }
        else return 0;
    }

    public double getX() {
        return x + imageBottom.getWidth();
    }

    @Override
    public void dealWithCollision(Element element) {

    }
}
