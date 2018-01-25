package brickbreaker;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;

public class LevelGenerator {
    public int brickWidth = 60;
    public int brickHeight = 30;
    public int[][] bricks;
    
    private int numberOfBlocksInLevel1 = 60;
    private int numberOfBlocksInLevel2 = 50;
    private int numberOfBlocksInLevel3 = 32;
    
    private boolean startedLevel1 = false;
    private boolean startedLevel2 = false;
    private boolean startedLevel3 = false;
    
    public LevelGenerator(int n){
        if(n == 1){
            generateMapLevel1(6,10);
            startedLevel1 = true;
        }
        if(n == 2){
           generateMapLevel2(10,10); 
           startedLevel2 = true; 
        } 
        if(n == 3){
            generateMapLevel3(8,8); 
            startedLevel3 = true;
        }
    }
    
    public void draw(Graphics2D g){
        
        if(startedLevel1){
          for(int i = 0; i < bricks.length;i++){
            for(int j = 0; j<bricks[0].length;j++){
                if(bricks[i][j] == 1 ){
                    //inside color of the brick
                    g.setColor(Color.blue);
                    g.fillRect( j * brickWidth + 100, i * brickHeight + 80, brickWidth, brickHeight);
                    //border color of the brick
                    g.setColor(Color.yellow);
                    g.setStroke(new BasicStroke(2));
                    g.drawRect( j * brickWidth + 100, i * brickHeight + 80, brickWidth, brickHeight);
                }
            }
          }  
        }
        
        if(startedLevel2){
          for(int i = 0; i < bricks.length;i++){
            for(int j = 0; j<bricks[0].length;j++){
                if(bricks[i][j] == 1 ){
                    //inside color of the brick
                    g.setColor(Color.red);
                    g.fillRect( j* brickWidth + 130, i * brickHeight + 80, brickWidth, brickHeight);
                    //border color of the brick
                    g.setColor(Color.white);
                    g.setStroke(new BasicStroke(2));
                    g.drawRect( j * brickWidth + 130, i * brickHeight + 80, brickWidth, brickHeight);
                }
            }
          } 
        }
        
        if(startedLevel3){
          for(int i = 0; i < bricks.length;i++){
            for(int j = 0; j<bricks[0].length;j++){
                if(bricks[i][j] == 1 ){
                    //inside color of the brick
                    g.setColor(Color.PINK);
                    g.fillRect( j* brickWidth + 160, i * brickHeight + 80, brickWidth, brickHeight);
                    //border color of the brick
                    g.setColor(Color.YELLOW);
                    g.setStroke(new BasicStroke(2));
                    g.drawRect( j * brickWidth + 160, i * brickHeight + 80, brickWidth, brickHeight);
                }
            }
          } 
        }
        
    }
    
    public void setBrickValue(int r,int c,int val){
        bricks[r][c] = val;
    }
    
    public void resetBrickValues(int r,int c){
        for(int i = 0; i < r;i++){
            for(int j = 0; j<c;j++){
                bricks[i][j] = 0; //0 means not active
            }
        }
    }
    
    public int returnLevel(){
        if(startedLevel1){
            return 1;
        }
        else if(startedLevel2){
            return 2;
        }
        else if(startedLevel3){
            return 3;
        }
        else{
           return 0; 
        }
        
    }
    
    public void generateMapLevel1(int r,int c){
        bricks = new int[r][c];
        for(int i = 0; i < r;i++){
            for(int j = 0; j<c;j++){
                bricks[i][j] = 1; //1 means active
            }
        }  
    }
    
    public int returnBlockNumbersInLevel1(){
        return numberOfBlocksInLevel1;
    }
    
    public void generateMapLevel2(int r,int c){
        bricks = new int[r][c];
        for(int i = 0; i < r;i++){
            for(int j = 0; j<c;j++){
                if( j % 2 == 0){
                  bricks[i][j] = 1; //1 means active  
                }     
            }
        }  
    }
    
    public int returnBlockNumbersInLevel2(){
        return numberOfBlocksInLevel2;
    }
    
    public void generateMapLevel3(int r,int c){ 
        bricks = new int[r][c];
        for(int i = 0; i < r;i++){
            for(int j = 0; j<c;j++){
                if(  (i+ j) % 2 == 0 ){
                  bricks[i][j] = 1; //1 means active  
                }     
            }
        }  
    }
    
    public int returnBlockNumbersInLevel3(){
        return numberOfBlocksInLevel3;
    }
    
    public void printBricksArray(int r, int c){
        for(int i = 0;i<r;i++){
            for(int j = 0;j<c;j++){
                System.out.print(bricks[i][j] + " ");
            }
            System.out.println();
        }
    }
    

}
