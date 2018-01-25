package brickbreaker;

import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.JFrame;
      
public class BrickBreaker {
    
    final static int frameWidth = 800;
    final static int frameHeight = 600;
    
    public static void main(String[] args) {
        
        JFrame frame = new JFrame();
        frame.setBounds(0,0,frameWidth,frameHeight);
        frame.setTitle("Ultimate Brick Breaker");
        frame.setResizable(false); 
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setLocation(dim.width/2-frameWidth/2, dim.height/2-frameHeight/2);

        Gameplay gameplay = new Gameplay();
        frame.add(gameplay);
        
        frame.setVisible(true);
    } 
}
