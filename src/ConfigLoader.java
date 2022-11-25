import bagel.Font;

import java.net.URL;
import java.nio.file.Paths;

public class ConfigLoader {
    public enum GAME_STATUS{PLAY, OVER, WIN, NOT_START, LEVEL_UP, LEVEL1};
    public static final String BACKIMAGE = "background.png";
    public static final String PIPE = "pipe.png";
    public static final String []BIRDIMAGE = {"birdWingDown.png" , "birdWingUp.png"};
    public static final String []LEVELSOURCE = {"level-0", "level-1"};
    public static final String FONTFILE = Paths.get("font", "slkscr.ttf").toString();
    public static final int FONTGAP = 75;
    public static final int BIRDINITX = 200;
    public static final int BIRDINITY = 350;
    public static final int WINDOWX = 1024;
    public static final int WINDOWY = 704;
    public static final int WINDOWTOPX = 0;
    public static final int WINDOWTOPY = 0;
    public static final int FONTSIZE = 48;

    public static final URL url = ConfigLoader.class.getClassLoader().getResource("");
    public static final String RESOURCE_DIR = url.getPath().replaceFirst("/", "");
    public static final Font FONT = new Font(Paths.get(RESOURCE_DIR, FONTFILE).toString(), FONTSIZE);

    public static final double[] LOCTYPE = {100, 300, 500};
    public static final double GAP = 168;

    public static final int BIRDFLY = 10;
    public static final double GRAVITY = 0.4;
    public static final double MAXV = 10;
    public static final double VOFY = 15;
    public static final double MINY = 0;
    public static final double SHOOTV = 10;
    public static final double INITIALV = 0;

    // control Board
    public static final int INITIAL = 1;
    public static final int ADD = 1;
    public static final int DES = 1;
    public static final int UP = 5;
    public static final int LOW = 1;
    public static final double INCREASERATE = 0.5;
    public static final double INITRATE = 1.0;
    public static final double VELOCITY = 3;

    public static final int FRAMEGAPINIT = 100;
    public static final int FRAMEGAPRANDOM = 200;


    public static final int LIFEBARX = 100;
    public static final int LIFEBARY = 15;
    public static final int[] LIVES = {3, 6};
    public static final String FULLLIFE = "fullLife.png";
    public static final String NOLIFE = "noLife.png";
    public static final int LEVELUP = 1;

    public static final int SCORELOCX = 100;
    public static final int SCORELOCY = 100;

    public static final int SLEEPTIME = 20;

    public static final String[] ARMS = {"rock.png", "bomb.png"};
    public static final String BOMB = "flame.png";
}
