import bagel.Image;
import bagel.util.Rectangle;

import java.nio.file.Paths;
import java.util.List;

public class Arm extends AbstractElement {
    private double velocity;
    private double locX;
    private double locY;
    private Image image;
    private String imageFile;
    private boolean used = false;
    private String level;
    public Arm(String imageFile, String level) {
        this.imageFile = Paths.get(ConfigLoader.RESOURCE_DIR, level,imageFile).toString();
        image = new Image(this.imageFile);
        locX = ConfigLoader.WINDOWX;
        locY = (int)(Math.random() * (ConfigLoader.WINDOWY - this.image.getHeight())) + this.image.getHeight();
        velocity = -ConfigLoader.VELOCITY;
        this.level = level;
    }
    public void shoot(double x, double y, double velocity) {
        this.velocity = velocity;
        locX = x;
        locY = y;
        used = true;
    }
    @Override
    public void draw() {
        image.drawFromTopLeft(locX, locY);
    }

    public boolean isUsed() {
        return used;
    }

    @Override
    public void move() {
        locX = locX + velocity;
    }


    @Override
    public Rectangle getRectangle() {
        return super.getRectangle(image, locX, locY);
    }

    @Override
    public void dealWithCollision(Element element) {
        if (element.getClass().equals(Pipe.class) && used) {
            if (imageFile.equals(ConfigLoader.ARMS[1])) {
                Image bomb = new Image(Paths.get(ConfigLoader.RESOURCE_DIR, level, ConfigLoader.BOMB).toString());
                bomb.drawFromTopLeft(locX, locY);
            }
        }
    }

    @Override
    public void changeVelocity(double velocity) {
        if (!used) this.velocity = -velocity;
    }

    @Override
    public boolean collision(Element other) {
        if (other.getRectangle() == null) return false;
        Rectangle box = getRectangle();
        if (box.intersects(other.getRectangle())) {
            if (other.getClass().equals(Pipe.class) && used) {
                System.out.println("武器撞上柱子了！！！");
                this.dealWithCollision(other);
                return true;
            }
            else if(other.getClass().equals(Role.class) && !used){
                other.dealWithCollision(this);
                return true;
            }
        }
        return false;
    }
}
