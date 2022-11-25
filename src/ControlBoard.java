import bagel.Image;
import bagel.util.Rectangle;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class ControlBoard extends AbstractElement{
    private int timescale;
    private double currentRate;
    private List<Element> objects;
    private int currentLife = 0;
    private int currentScore = 0;
    private List<Image> lifeBar;
    private int level;
    public ControlBoard(List<Element> objects, int level) {
        this.objects = objects;
        timescale = ConfigLoader.INITIAL;
        currentRate = ConfigLoader.INITRATE;
        this.level = level;
        currentLife = ConfigLoader.LIVES[level];
        lifeBar = new ArrayList<>();
        generateLifeBar();
    }
    private void generateLifeBar() {
        lifeBar.clear();
        for (int i = 0; i < currentLife; i ++) {
            lifeBar.add(new Image(
                    Paths.get(ConfigLoader.RESOURCE_DIR, ConfigLoader.FULLLIFE).toString()
            ));
        }
        for (int i = currentLife; i < ConfigLoader.LIVES[level]; i++) {
            lifeBar.add(new Image(
                    Paths.get(ConfigLoader.RESOURCE_DIR, ConfigLoader.NOLIFE).toString()
            ));
        }
    }
    public void setVelocity() {
        for (Information information: objects) information.changeVelocity(timescale * ConfigLoader.VELOCITY);
    }
    public void increase(){
        if (timescale < ConfigLoader.UP){
            timescale += ConfigLoader.ADD;
            currentRate = currentRate * (1 + ConfigLoader.INCREASERATE);
        }
        for (Information information: objects) information.changeVelocity(timescale * ConfigLoader.VELOCITY);
    }

    public void decrease(){
        if (timescale > ConfigLoader.LOW) {
            timescale -= ConfigLoader.DES;
            currentRate = currentRate / (1 + ConfigLoader.INCREASERATE);
        }
        for (Information information: objects) information.changeVelocity(timescale * ConfigLoader.VELOCITY);
    }
    public boolean dreaseLife() {
        if (currentLife == 0) return true;
        currentLife --;
        if (currentLife == 0) {
            generateLifeBar();
            return true;
        }
        generateLifeBar();
        return false;
    }
    public void getScore(int score) {
        currentScore += score;
    }

    public int getScore() {
        return currentScore;
    }

    public boolean checkScore() {
        if (currentScore >= ConfigLoader.LEVELUP && Game.getGame().getLevel().equals(ConfigLoader.LEVELSOURCE[0])) {
            for (Element element: objects) element.levelChange(ConfigLoader.LEVELSOURCE[1]);
            currentLife = ConfigLoader.LIVES[1];
            level = 1;
            generateLifeBar();
            return true;
        }
        return false;
    }

    @Override
    public void draw() {
        ConfigLoader.FONT.drawString("SCORE: "+ currentScore, ConfigLoader.SCORELOCX, ConfigLoader.SCORELOCY);
        for (int i = 0; i < lifeBar.size(); i++) {
            Image life = lifeBar.get(i);
            life.drawFromTopLeft(ConfigLoader.LIFEBARX+ i * life.getWidth(), ConfigLoader.LIFEBARY);
        }
    }

    @Override
    public void move() {

    }

    @Override
    public Rectangle getRectangle() {
        return null;
    }
}
