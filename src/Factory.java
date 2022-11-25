import java.util.List;

public class Factory {
    private int pipeGap = ConfigLoader.FRAMEGAPINIT;
    private int pipe = 1;
    private int arm = 1;
    private int armGap = ConfigLoader.FRAMEGAPINIT * 2;
    public void randomGenPipe(List<Element> elements, List<Element> pipes, String level) {
        if(pipe%pipeGap == 0){
            pipeGap = (int)(Math.random()* ConfigLoader.FRAMEGAPRANDOM + ConfigLoader.FRAMEGAPINIT);
            Pipe obj = new Pipe(level);
            elements.add(obj);
            pipes.add(obj);
            pipe = 1;
        }
        else {
            pipe ++;
        }
    }
    public void randomGenArm(List<Element> elements, List<Element> arms, String level) {
        if(arm% armGap == 0){
            armGap = (int)(Math.random()* ConfigLoader.FRAMEGAPRANDOM + ConfigLoader.FRAMEGAPINIT * 2);
            double prob = Math.random();
            Arm obj = null;
            if (prob > 0.5)  obj = new Arm(ConfigLoader.ARMS[1], level);
            else obj = new Arm(ConfigLoader.ARMS[0], level);
            elements.add(obj);
            arms.add(obj);
            arm = 1;
        }
        else arm ++;
    }
}
