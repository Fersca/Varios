import java.awt.Robot;
import java.util.Random;

public class Mouse {
    
    public static void main(String... args) throws Exception {
    	
        Robot robot = new Robot();

        int i = 0;
        int j = 0;
        Random r = new Random();
        
        for(int a=0;a<100;a++) {
        	i=i+r.nextInt(10);
        	j=j+r.nextInt(10);
            robot.mouseMove(i, j);
            //int num = r.nextInt(30);
            Thread.sleep(5);
        }
        for(int a=0;a<100;a++) {
        	i=i-r.nextInt(5);
        	j=j-r.nextInt(5);
            robot.mouseMove(i, j);
            //int num = r.nextInt(30);
            Thread.sleep(5);
        }
        
    }
}