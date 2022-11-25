import bagel.*;
import bagel.Image;
import bagel.util.Rectangle;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class Game extends AbstractGame implements Information {
    private static final Game game = new Game(ConfigLoader.WINDOWX, ConfigLoader.WINDOWY);

    private ConfigLoader.GAME_STATUS gameStatus;
    private String level;
    private Image backImage;
    private List<Element> elements;
    private List<Element> arms;
    private List<Element> pipes;
    private Role role;
    private Factory factory;
    private int frame;
    private ControlBoard controlBoard;
    private int beforeNewGame = 1;
    private Game(int windowX, int windowY){
        super(windowX, windowY, "Flappy older.Bird");
        factory = new Factory();
        level = ConfigLoader.LEVELSOURCE[0];
        init();
    }
    public static Game getGame() {
        return game;
    }

    private void init() {
        elements = new ArrayList<>();
        arms = new ArrayList<>();
        pipes = new ArrayList<>();
        controlBoard = new ControlBoard(elements, Arrays.binarySearch(ConfigLoader.LEVELSOURCE, level));
        gameStatus = ConfigLoader.GAME_STATUS.NOT_START;
        backImage = new Image(Paths.get(ConfigLoader.RESOURCE_DIR, level, ConfigLoader.BACKIMAGE).toAbsolutePath().toString());
        frame = 0;
        elements.add(controlBoard);
    }

    private void showWord(List<String> words) {
        int i = 0;
        for (String str: words) {
            ConfigLoader.FONT.drawString(str,
                    (backImage.getWidth()-ConfigLoader.FONT.getWidth(str))/2,
                    backImage.getHeight()/2 + i * ConfigLoader.FONTGAP);
            i ++;
        }
    }
    public void lifeDecrease() {
        if (controlBoard.dreaseLife()) {
            stop();
        }
        else {
            int index = elements.indexOf(role);
            role = new Role(level);
            elements.set(index, role);
        }
    }

    private void startPage(Input input) {
        if (level.equals(ConfigLoader.LEVELSOURCE[0])) {
            showWord(Arrays.asList("PRESS SPACE TO START"));
        }
        else {
            showWord(Arrays.asList("PRESS SPACE TO START", "PRESS ’S’ TO SHOOT"));
        }
        if (beforeNewGame % ConfigLoader.SLEEPTIME == 0) {
            if (input.wasPressed(Keys.SPACE)) {
                role = new Role(level);
                elements.add(role);
                gameStatus = ConfigLoader.GAME_STATUS.PLAY;
                beforeNewGame = 1;
            }
        }
        else {
            beforeNewGame ++;
        }
    }

    private void playPage(Input input) {
        frame ++;
        if (input.wasPressed(Keys.SPACE)) {
            role.flying();
            role.draw();
        }
        else if (input.wasPressed(Keys.L)) {
            controlBoard.decrease();
        }
        else if (input.wasPressed(Keys.K)) {
            controlBoard.increase();
        }
        else if (input.wasPressed(Keys.S)) {
            role.attack(arms);
        }
        else if (controlBoard.checkScore()) {
            levelChange(ConfigLoader.LEVELSOURCE[1]);
            gameStatus = ConfigLoader.GAME_STATUS.LEVEL_UP;
            return;
        }
        else if (frame % 3 == 0){
            role.move();
        }
        if (level.equals(ConfigLoader.LEVELSOURCE[1])) {
            factory.randomGenArm(elements, arms, level);
        }

        factory.randomGenPipe(elements, pipes, level);
        controlBoard.setVelocity();
        if (role.getRectangle().bottom() < 0 || role.getRectangle().top() > ConfigLoader.WINDOWY
        ||role.getRectangle().left() > ConfigLoader.WINDOWX || role.getRectangle().right() < 0) {
            lifeDecrease();
        }
        Iterator<Element> armsIterator = arms.listIterator();
        while (armsIterator.hasNext()) {
            Element arm = armsIterator.next();
            if (((Arm)arm).isUsed() && !elements.contains(arm)) {
                elements.add(arm);
            }
            Iterator<Element> pipeIterator = pipes.listIterator();
            while (pipeIterator.hasNext()) {
                Element pipe = pipeIterator.next();
                if (pipe.collision(arm)) {
                    System.out.println("子弹打到管道，管道消失");
                    elements.remove(pipe);
                    elements.remove(arm);
                    pipeIterator.remove();
                    armsIterator.remove();
                }
            }
        }
        Iterator<Element> iterator = elements.listIterator();
        while (iterator.hasNext()) {
            Element element = iterator.next();
            if (element.equals(role)) continue;
            if (!element.equals(controlBoard) &&element.collision(role)) {
                if (pipes.contains(element)) pipes.remove(element);
                else if (arms.contains(element) && !((Arm)element).isUsed()) arms.remove(element);
                System.out.println("人物撞到管道/武器，消失");
                iterator.remove();
            }
            else if (!element.equals(controlBoard) &&(
                     element.getRectangle().top() > ConfigLoader.WINDOWY
                    || element.getRectangle().bottom() < 0
                    || element.getRectangle().left() > ConfigLoader.WINDOWX
                    || element.getRectangle().right() < 0
                    )) {
                if (pipes.contains(element)) pipes.remove(element);
                else if (arms.contains(element) && !((Arm)element).isUsed()) arms.remove(element);
                System.out.println("管道/武器出画，消失");
                iterator.remove();
            }
            else {
                element.move();
                element.draw();
            }
        }
        role.cross(elements);
        role.draw();
    }
    private void overPage(Input input) {
        showWord(Arrays.asList("GAME OVER", "FINAL SCORE: "+controlBoard.getScore()));
        if (beforeNewGame % ConfigLoader.SLEEPTIME == 0) {
            if (input.wasPressed(Keys.SPACE)) {
                gameStatus = ConfigLoader.GAME_STATUS.PLAY;
                level = ConfigLoader.LEVELSOURCE[0];
                init();
                beforeNewGame = 1;
            }
        }
        else {
            beforeNewGame ++;
        }
    }
    public void levelUp(Input input) {
        showWord(Arrays.asList("LEVEL UP!!"));
        if (beforeNewGame % ConfigLoader.SLEEPTIME == 0) {
            if (input.wasPressed(Keys.SPACE)) {
                elements.clear();
                backImage = new Image(Paths.get(ConfigLoader.RESOURCE_DIR, level, ConfigLoader.BACKIMAGE).toAbsolutePath().toString());
                role = new Role(level);
                frame = 0;
                elements.add(controlBoard);
                gameStatus = ConfigLoader.GAME_STATUS.NOT_START;
                beforeNewGame = 1;
            }
        }
        else {
            beforeNewGame ++;
        }
    }
    @Override
    protected void update(Input input) {
        backImage.drawFromTopLeft(ConfigLoader.WINDOWTOPX, ConfigLoader.WINDOWTOPY);
        if (gameStatus.equals(ConfigLoader.GAME_STATUS.NOT_START)) {
            startPage(input);
        }
        else if (gameStatus.equals(ConfigLoader.GAME_STATUS.PLAY)) {
            playPage(input);
        }
        else if (gameStatus.equals(ConfigLoader.GAME_STATUS.OVER)) {
            overPage(input);
        }
        else if (gameStatus.equals(ConfigLoader.GAME_STATUS.LEVEL_UP)) {
            levelUp(input);
        }
    }

    @Override
    public void changeVelocity(double velocity) {

    }

    @Override
    public void stop() {
        gameStatus = ConfigLoader.GAME_STATUS.OVER;
        for (Element element : elements) {
            if (element.equals(this)) continue;
            element.stop();
        }
    }

    @Override
    public void levelChange(String level) {
        this.level = level;
    }

    public String getLevel() {
        return level;
    }
    public void scoreIncrease(int score) {
        controlBoard.getScore(score);
    }
}
