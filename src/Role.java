import bagel.Image;
import bagel.util.Rectangle;

import java.nio.file.Paths;
import java.util.List;

public class Role extends AbstractElement {
    private String level;
    private int frame = 0;
    private int status = 0;
    private Arm arm = null;
    private final double x;
    private double y;
    private Image image;
    private double v;
    public Role(String level) {
        this.level = level;
        x = ConfigLoader.BIRDINITX;
        y = ConfigLoader.BIRDINITY;
        v = ConfigLoader.INITIALV;
        image = new Image(Paths.get(ConfigLoader.RESOURCE_DIR, level, ConfigLoader.BIRDIMAGE[status]).toString());
        status = 1;
    }

    public void attack(List<Element> elements) {
        if (arm == null) return;
        elements.add(arm);
        arm.shoot(x, y, ConfigLoader.SHOOTV);
        arm = null;
    }

    public void flying() {
        y -= ConfigLoader.VOFY;
        v = ConfigLoader.INITIALV;
    }


    @Override
    public void draw() {
        frame = (frame + 1) % ConfigLoader.BIRDFLY;
        if (frame == 0) {
            image = new Image(Paths.get(ConfigLoader.RESOURCE_DIR, level, ConfigLoader.BIRDIMAGE[status]).toString());
            status = (status + 1) %  ConfigLoader.BIRDIMAGE.length;
        }
        image.drawFromTopLeft(x, y);
    }

    @Override
    public void move() {
        if(v<ConfigLoader.MAXV){
            v += ConfigLoader.GRAVITY;
        }
        y += v;
    }

    @Override
    public Rectangle getRectangle() {
        return super.getRectangle(image, x, y);
    }

    @Override
    public void dealWithCollision(Element element) {
        if (element.getClass().equals(Pipe.class)) {
            Game.getGame().lifeDecrease();
        }
        else if (element.getClass().equals(Arm.class)) {
            this.arm = (Arm) element;
        }
    }

    @Override
    public void levelChange(String level) {
        this.level = level;
    }

    public void cross(List<Element> elements) {
        Rectangle box = getRectangle();
        for (Element element: elements) {
            if (element.equals(this)) continue;
            if (element.getClass().equals(Pipe.class)) {
                if (x > ((Pipe) element).getX())
                Game.getGame().scoreIncrease(((Pipe)element).getScore());
            }
        }
    }
}
