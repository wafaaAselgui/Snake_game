import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;
import javax.swing.border.LineBorder;

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
    ArrayList<Tile> snakeBody;

    // Food
    Tile food;
    Random random;

    // game logic
    Timer gameLoop;
    int velocityX;
    int velocityY;

    boolean gameOver = false; 
    JButton replayButton;

    SnakeGame(int boardWidth, int boardHeight){
        this.boardWidth = boardWidth;
        this.boardHeight = boardHeight;

        setPreferredSize(new Dimension(this.boardWidth , this.boardHeight));
        setBackground(Color.BLACK);
        addKeyListener(this);
        setFocusable(true);
        //  replay button
        setFocusable(true);
        setLayout(null);

        replayButton = new JButton("Replay");
        replayButton.setBounds(250, 330, 100, 40);
        replayButton.setVisible(false);
        replayButton.setFont(new Font("Arial", Font.PLAIN ,18));
        replayButton.setBackground(Color.GREEN);
        replayButton.setForeground(Color.WHITE);
        replayButton.setFocusable(false);
        replayButton.addActionListener(e -> restartGame());

        add(replayButton);

        snakeHead = new Tile(5,5);
        snakeBody = new ArrayList<Tile>();

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
        // for(int i = 0 ; i < boardWidth/tileSize ; i++){
        //     // we need a starting point and an end point that's why we have
        //     // (x1 , y1 , x2 , y2)
        //     // the first line here is for vertical lines , from y=0 to the height (x goes from left to right)
        //     g.drawLine(i*tileSize , 0 , i*tileSize , boardHeight);
        //     //  the second line we are drawing horizontally starts from x=0 to the board width so we re changing the y coordinate 
        //     g.drawLine(0, i*tileSize, boardWidth, i*tileSize);
        // }



        // Food
        g.setColor(Color.RED);
        g.fillRect(food.x*tileSize, food.y*tileSize, tileSize, tileSize);
        // Snake head
        g.setColor(Color.green);
        g.fillRect(snakeHead.x * tileSize, snakeHead.y * tileSize, tileSize, tileSize);
        // Snake body
        for(int i = 0; i < snakeBody.size() ; i++){
            Tile snakePart = snakeBody.get(i);
            g.fillRect(snakePart.x * tileSize , snakePart.y * tileSize , tileSize , tileSize);
        }


        // Score
        
        if (gameOver) {
            // YOU LOST
            g.setColor(Color.RED);
            g.setFont(new Font("Courier New", Font.BOLD, 50));

            String lostText = "YOU LOST BOII !! ";

            FontMetrics metrics = g.getFontMetrics();
            int textWidth = metrics.stringWidth(lostText);

            int textX = (boardWidth - textWidth) / 2;
            int textY = 270;

            g.drawString(lostText, textX, textY);

            // Score
            g.setFont(new Font("Courier New", Font.BOLD, 18));

            String scoreText = "SCORE: " + snakeBody.size();

            metrics = g.getFontMetrics();
            int scoreWidth = metrics.stringWidth(scoreText);

            int scoreX = (boardWidth - scoreWidth) / 2;

            g.drawString(scoreText, scoreX, textY + 35);

        } 
        else {
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.PLAIN, 16));

            g.drawString(
                "SCORE: " + snakeBody.size(),
                tileSize - 16,
                tileSize
            );
        }
    }
    public void placeFood(){
        food.x = random.nextInt(boardWidth/tileSize); //600/25 = 24 
        food.y = random.nextInt(boardHeight/tileSize);
    }

    // function to detect the collision between the snake's head and the food
    public boolean collision(Tile  tile1 , Tile tile2){
        return tile1.x == tile2.x && tile1.y == tile2.y;
    }

    public void move(){
        // eat food 
        if (collision(snakeHead , food)){
            snakeBody.add(new Tile(food.x , food.y));
            placeFood();
        }

        // each tile needs to follow the one before it that that s why we're going
        // to iterate through the arrayList backwards 
        //  Snake Body
        for(int i = snakeBody.size()-1 ; i >= 0 ; i--){
            Tile snakePart = snakeBody.get(i);
            if(i == 0){
                snakePart.x = snakeHead.x;
                snakePart.y = snakeHead.y;
            }
            else{
                Tile prevSnakePart = snakeBody.get(i-1);
                snakePart.x = prevSnakePart.x;
                snakePart.y = prevSnakePart.y;
            }
        }


        // snake head
        snakeHead.x += velocityX;
        snakeHead.y += velocityY;

        // game over conditions
        for(int i = 0 ; i < snakeBody.size() ; i++){
            Tile snakePart = snakeBody.get(i);
            if(collision(snakeHead , snakePart)){
                gameOver = true;
            }
        }


        if (snakeHead.x*tileSize < 0 || snakeHead.x*tileSize >boardWidth
            || snakeHead.y *tileSize < 0 || snakeHead.y*tileSize > boardHeight){
                gameOver=true;
            }
    }

    @Override
    public void actionPerformed(ActionEvent e){
        // what we're doing here is that actionPerformed will run repaint every 100ms in loog 
        // and repaint() itself will basically call draw() over and over again
        move();
        repaint();
        if (gameOver){
            gameLoop.stop();
            replayButton.setVisible(true);
        }
    }

    public void restartGame(){

        // Reset snake position
        snakeHead = new Tile(5, 5);

        // Remove the old body
        snakeBody.clear();

        // Reset food
        placeFood();

        // Reset movement
        velocityX = 0;
        velocityY = 0;

        // Reset game state
        gameOver = false;

        // Hide replay button
        replayButton.setVisible(false);

        // Restart timer
        gameLoop.start();

        // Make sure the panel receives keyboard input
        requestFocusInWindow();

        repaint();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        // if we press the up arrow we move the snake on the Y moving towards the 0 so negative
        if(e.getKeyCode()==KeyEvent.VK_UP && velocityY != 1){
            velocityX = 0;
            velocityY = -1;
        }
        // down arrow moving down y axis
        else if (e.getKeyCode() == KeyEvent.VK_DOWN && velocityY != -1){
            velocityX = 0;
            velocityY = 1;
        }
        // left arrow moving on the x axis to the left (negative)
        else if (e.getKeyCode() == KeyEvent.VK_LEFT && velocityX != 1){
            velocityX = -1;
            velocityY = 0;
        }
        // right arrow moving forward on the x axis 
        else if (e.getKeyCode() == KeyEvent.VK_RIGHT && velocityX != -1){
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