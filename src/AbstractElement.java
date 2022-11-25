import bagel.Image;
import bagel.util.Rectangle;

abstract public class AbstractElement implements Element{
    @Override
    public boolean collision(Element other) {
        if (other.getRectangle() == null) return false;
        Rectangle box = this.getRectangle();
        if (box.intersects(other.getRectangle())) {
            this.dealWithCollision(other);
            return true;
        }
        return false;
    }

    public Rectangle getRectangle(Image image, double x, double y) {
        return new Rectangle(x, y, image.getWidth(), image.getHeight());
    }

    @Override
    public void dealWithCollision(Element element) {

    }

    @Override
    public void changeVelocity(double velocity) {

    }

    @Override
    public void stop() {

    }

    public void levelChange(String level) {}
}
