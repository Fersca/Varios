import java.awt.Robot;
import java.awt.event.InputEvent;
import java.util.Random;

public class Mouse {
    
    public static void main(String... args) throws Exception {
    	
        Robot robot = new Robot();
        robot.setAutoDelay(5);

        int i = 0;
        int j = 0;
        Random r = new Random();
        
        for(int a=0;a<100;a++) {
        	i=i+r.nextInt(10);
        	j=j+r.nextInt(10);
            robot.mouseMove(i, j);
        }
        
        robot.mousePress(InputEvent.BUTTON1_MASK);
        robot.mouseRelease(InputEvent.BUTTON1_MASK);
        
        robot.keyPress(45);
        robot.keyPress(46);
        robot.keyPress(47);
                   
        for(int a=0;a<100;a++) {
        	i=i-r.nextInt(5);
        	j=j-r.nextInt(5);
            robot.mouseMove(i, j);
        }
        
    }
}