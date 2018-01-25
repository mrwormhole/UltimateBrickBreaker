package brickbreaker;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JPanel;
import javax.swing.Timer; 

public class Gameplay extends JPanel implements KeyListener,ActionListener {
    
    private int score = 0;
    private boolean gameOver = true;
    private boolean gameFinished = false;
    final static int frameWidth = 800;
    final static int frameHeight = 600;
   
    private int bricksCount; 
    
    private final Timer timer;
    private final int delay = 10; //ms.This should be less than 16 for fps rate
    
    private int playerPositionX = 350; //half of the paddle is 50
    private int playerPositionY = 550;
    private int ballPositionX = 392; // radius of ball is 8
    private int ballPositionY = 392;
    private int[] ballDirectionXarr = {-1,1};
    //private int[] ballDirectionYarr = {-2,2}; this sucks
    private int ballDirectionX = ballDirectionXarr[(int)(Math.random()*2)];
    //private int ballDirectionY = ballDirectionYarr[(int)(Math.random()*2)];
    private int ballDirectionY = -2;
    //bug:sometimes balls destroy 2 blocks :@
    
    private LevelGenerator levGen;
    
    public Gameplay(){
        levGen = new LevelGenerator(1); 
        bricksCount = levGen.returnBlockNumbersInLevel1(); 
        //levGen.printBricksArray();
        
        addKeyListener(this); //for keyboard inputs
        setFocusable(true); //for superinterface EventListener
        setFocusTraversalKeysEnabled(false); //for superinterface EventListener
        timer = new Timer(delay,this);
        timer.start();
    }
 
    public void paint(Graphics g){
       
        //BACKGROUND
        g.setColor(Color.black);
        g.fillRect(0 ,0, frameWidth, frameHeight);
        
        //MAP
        levGen.draw((Graphics2D)g);
        
        //SCORE
        g.setColor(Color.white);
        g.setFont(new Font("serif",Font.BOLD,25));
        g.drawString("" + score,10,30);
        
        //PLAYER
        g.setColor(Color.green);
        g.fillRect(playerPositionX, 550, 100, 10);
        
        //BALL
        g.setColor(Color.white);
        g.fillOval(ballPositionX, ballPositionY, 16, 16);
        
        if(bricksCount == 0 && !gameOver){
           checkForNextLevel(); 
        }
        
        if(gameOver){
            g.setColor(Color.white);
            g.setFont(new Font("serif",Font.BOLD,25));
            g.drawString("Press ENTER to Continue, R to Restart" ,200,50);
        }
        
        if(gameFinished){
            g.setColor(Color.blue);
            playerPositionY = 550;
            ballDirectionX = 0;
            ballDirectionY = 0;
            g.setFont(new Font("serif",Font.BOLD,25));
            g.drawString("YOU HAVE FINISHED MY GAME, GOOD JOB" ,150,50);
        }
        
        if(ballPositionY > frameHeight){
            gameOver = true; 
            ballDirectionX = 0;
            ballDirectionY = 0;
            g.setColor(Color.yellow);
            g.setFont(new Font("serif",Font.BOLD,50));
            g.drawString("GAME OVER" ,250,250);
        }
        
        g.dispose(); //for memory efficiency
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        //my update game loop
        Rectangle PlayerCollider = new Rectangle(playerPositionX,playerPositionY,100,8);
        Rectangle BallCollider = new Rectangle(ballPositionX,ballPositionY,16,16);
 
        if(!gameOver){
            if(BallCollider.intersects(PlayerCollider)){
                ballDirectionY =- ballDirectionY; //collision between player and ball
            }
            //fix this later(top and bottom), make it more realistic for player
            ballPositionX += ballDirectionX; //initial speed
            ballPositionY += ballDirectionY; //initial speed
            
            applyPhysicsBetweenBallAndBrick();
            
            if(ballPositionX < 0){
                ballDirectionX = - ballDirectionX; //left border
            }
            if(ballPositionX > frameWidth - 20){ 
                ballDirectionX = - ballDirectionX; //right border
            }
            if(ballPositionY < 0){
                ballDirectionY = - ballDirectionY; //top border
            }
        }
        
        repaint();
    }
    
    @Override
    public void keyPressed(KeyEvent e) {
        
        if(e.getKeyCode() == KeyEvent.VK_RIGHT && !gameOver && !gameFinished ){
            if(playerPositionX >= frameWidth - 100){
                playerPositionX = 700;
            }
            else{
                playerPositionX += 10; 
            }
        }
        
        if(e.getKeyCode() == KeyEvent.VK_LEFT && !gameOver && !gameFinished ){
            if(playerPositionX <= 0){
                playerPositionX = 0; 
            }
            else{
                playerPositionX -= 10;
            }
        }
        
        if(e.getKeyCode() == KeyEvent.VK_ENTER){
            gameOver = false; //press enter to start 
        }
        
        if(e.getKeyCode() == KeyEvent.VK_R && gameOver){
                restartGame(); //press r to start from level 1
                repaint();
        }
    }
    
    @Override
    public void keyTyped(KeyEvent e) {}
    @Override
    public void keyReleased(KeyEvent e) {}
    
    public void restartGame(){
        gameOver = false;
        score = 0;
        playerPositionX = 350;
        playerPositionY = 550;  
        ballPositionX = 392;
        ballPositionY = 392;
        ballDirectionX = ballDirectionXarr[(int)(Math.random()*2)];
        //ballDirectionY = ballDirectionYarr[(int)(Math.random()*2)];
        ballDirectionY = -2;
        levGen = new LevelGenerator(1);
        bricksCount =levGen.returnBlockNumbersInLevel1();
    }
    
    public void applyPhysicsBetweenBallAndBrick(){
        
        if(levGen.returnLevel() == 1){
          Z:for(int i = 0; i < levGen.bricks.length;i++){
                for(int j = 0; j<levGen.bricks[0].length;j++){
                    if(levGen.bricks[i][j] == 1){
                        
                        int brickX = j * levGen.brickWidth + 100; 
                        int brickY = i * levGen.brickHeight + 80;
                        int brickWidth = levGen.brickWidth;
                        int brickHeight = levGen.brickHeight;
                        
                        Rectangle brickRect = new Rectangle(brickX,brickY,brickWidth,brickHeight);
                        Rectangle ballRect = new Rectangle(ballPositionX,ballPositionY,16,16);
                        
                        if(ballRect.intersects(brickRect)){
                            levGen.setBrickValue(i,j,0); // set inactive
                            bricksCount--;
                            score += 10;
                    
                            if(ballPositionX + 16 - 1 <= brickRect.x || ballPositionX  + 1 >= brickRect.x + brickRect.width){
                                ballDirectionX = -ballDirectionX;
                            }
                            else{
                                ballDirectionY = -ballDirectionY;
                            }
                            
                            break Z;
                        }
                    }
                }
            }  
        }
        
        if(levGen.returnLevel() == 2){
            Z:for(int i = 0; i < levGen.bricks.length;i++){
                for(int j = 0; j<levGen.bricks[0].length;j++){
                    if(levGen.bricks[i][j] == 1){
                        
                        int brickX = j * levGen.brickWidth + 130; 
                        int brickY = i * levGen.brickHeight + 80;
                        int brickWidth = levGen.brickWidth;
                        int brickHeight = levGen.brickHeight;
                        
                        Rectangle brickRect = new Rectangle(brickX,brickY,brickWidth,brickHeight);
                        Rectangle ballRect = new Rectangle(ballPositionX,ballPositionY,16,16);
                        
                        if(ballRect.intersects(brickRect)){
                            levGen.setBrickValue(i,j,0); // set inactive
                            bricksCount--;
                            score += 10;
                    
                            if(ballPositionX + 16 - 1 <= brickRect.x || ballPositionX  + 1 >= brickRect.x + brickRect.width){
                                ballDirectionX = -ballDirectionX;
                            }
                            else{
                                ballDirectionY = -ballDirectionY;
                            }
                            
                            break Z;
                        }
                    }
                }
            }
        }
        
        if(levGen.returnLevel() == 3){
            Z:for(int i = 0; i < levGen.bricks.length;i++){
                for(int j = 0; j<levGen.bricks[0].length;j++){
                    if(levGen.bricks[i][j] == 1){
                        
                        int brickX = j* levGen.brickWidth + 160; 
                        int brickY = i * levGen.brickHeight + 80;
                        int brickWidth = levGen.brickWidth;
                        int brickHeight = levGen.brickHeight;
                        
                        Rectangle brickRect = new Rectangle(brickX,brickY,brickWidth,brickHeight);
                        Rectangle ballRect = new Rectangle(ballPositionX,ballPositionY,16,16);
                        
                        if(ballRect.intersects(brickRect)){
                            levGen.setBrickValue(i,j,0); // set inactive
                            bricksCount--;
                            score += 10;
                    
                            if(ballPositionX + 16 - 1 <= brickRect.x || ballPositionX  + 1 >= brickRect.x + brickRect.width){
                                ballDirectionX = -ballDirectionX;
                            }
                            else{
                                ballDirectionY = -ballDirectionY;
                            }
                            
                            break Z;
                        }
                    }
                }
            }
        }
        
    }
    
    private void checkForNextLevel(){
        if(bricksCount == 0 && levGen.returnLevel() == 1){ 
            levGen = new LevelGenerator(2);
            bricksCount = levGen.returnBlockNumbersInLevel2();
            playerPositionX = 350;  
            ballPositionX = 392;
            ballPositionY = 392;
            ballDirectionX = ballDirectionXarr[(int)(Math.random()*2)];
            ballDirectionY = -2; //ballDirectionYarr[(int)(Math.random()*2)];
        }
        else if(bricksCount == 0 && levGen.returnLevel() == 2){
            levGen = new LevelGenerator(3);
            bricksCount = levGen.returnBlockNumbersInLevel3();
            playerPositionX = 350;  
            ballPositionX = 392;
            ballPositionY = 392;
            ballDirectionX = ballDirectionXarr[(int)(Math.random()*2)];
            ballDirectionY = -2; //ballDirectionYarr[(int)(Math.random()*2)];
        }
        else if(bricksCount == 0 && levGen.returnLevel() == 3){
            gameFinished = true;
        }
    }
   
}
