import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;

// we implemented the ActionListener for the snake to repainted every 100ms
//  and the keyListener for the program to listen to the arrow keys events
public class SnakeGame extends JPanel implements ActionListener , KeyListener{
    
    //this class is used to keep track of all the x and y positions for each tile 
    private class Tile{
        int x;
        int y;

        Tile(int x , int y){
            this.x=x;
            this.y=y;
        }
    }
    int boardWidth;
    int boardHeight;
    int tileSize=25;

    // Snake
    Tile snakeHead;
    // Food
    Tile food;
    Random random;

    // game logic
    Timer gameLoop;
    int velocityX;
    int velocityY;

    SnakeGame(int boardWidth, int boardHeight){
        this.boardWidth = boardWidth;
        this.boardHeight = boardHeight;

        setPreferredSize(new Dimension(this.boardWidth , this.boardHeight));
        setBackground(Color.BLACK);
        addKeyListener(this);
        setFocusable(true);

        snakeHead = new Tile(5,5);

        food = new Tile(10,10);
        random = new Random();
        placeFood();

        velocityX=0;
        velocityY=0;

        gameLoop = new Timer(100 , this);
        gameLoop.start();
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g){
        // Grid
        for(int i = 0 ; i < boardWidth/tileSize ; i++){
            // we need a starting point and an end point that's why we have
            // (x1 , y1 , x2 , y2)
            // the first line here is for vertical lines , from y=0 to the height (x goes from left to right)
            g.drawLine(i*tileSize , 0 , i*tileSize , boardHeight);
            //  the second line we are drawing horizontally starts from x=0 to the board width so we re changing the y coordinate 
            g.drawLine(0, i*tileSize, boardWidth, i*tileSize);
        }
        // Food
        g.setColor(Color.RED);
        g.fillRect(food.x*tileSize, food.y*tileSize, tileSize, tileSize);
        // Snake
        g.setColor(Color.green);
        g.fillRect(snakeHead.x * tileSize, snakeHead.y * tileSize, tileSize, tileSize);
    }
    public void placeFood(){
        food.x = random.nextInt(boardWidth/tileSize); //600/25 = 24 
        food.y = random.nextInt(boardHeight/tileSize);
    }

    public void move(){
        snakeHead.x += velocityX;
        snakeHead.y += velocityY;
    }

    @Override
    public void actionPerformed(ActionEvent e){
        // what we're doing here is that actionPerformed will run repaint every 100ms in loog 
        // and repaint itself will basically call draw over and over again
        move();
        repaint();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        // if we press the up arrow we move the snake on the Y moving towards the 0 so negative
        if(e.getKeyCode()==KeyEvent.VK_UP ){
            velocityX = 0;
            velocityY = -1;
        }
        // down arrow moving down y axis
        else if (e.getKeyCode() == KeyEvent.VK_DOWN){
            velocityX = 0;
            velocityY = 1;
        }
        // left arrow moving on the x axis to the left (negative)
        else if (e.getKeyCode() == KeyEvent.VK_LEFT){
            velocityX = -1;
            velocityY = 0;
        }
        // right arrow moving forward on the x axis 
        else if (e.getKeyCode() == KeyEvent.VK_RIGHT){
            velocityX = 1;
            velocityY = 0;
        }
    }

    // not needed 
    @Override
    public void keyReleased(KeyEvent e) {
    }
    // not needed
    @Override
    public void keyTyped(KeyEvent e) {
    }
}