import bagel.util.Rectangle;

import java.util.List;

public interface Element extends Information {
    public void draw();
    public void move();
    public boolean collision(Element element);
    public Rectangle getRectangle();
    public void dealWithCollision(Element element);
}
