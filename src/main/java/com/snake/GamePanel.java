package com.snake;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JPanel;
import javax.swing.Timer;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener  {
    static final int Screen_WIDTH=600;
    static final int Screen_Height=600;
    static final int UNIT_Size=25;
    static final int GAME_UNIT=(Screen_WIDTH*Screen_Height)/UNIT_Size;
    static final int DELAY=85;
    final int x[] = new int [GAME_UNIT];
    final int y[] = new int [GAME_UNIT];
    int bodyPart=6;
    int AppleEatens;
    int Applex;
    int Appley;
    // direction is changable depending on your choice R or L OR Up Or Down
    char direction = 'R';
    boolean Running = false;
    Timer timer;
    Random random;
    GamePanel(){
        random = new Random();
        this.setPreferredSize(new Dimension(Screen_WIDTH,Screen_Height));
        this.setBackground(Color.WHITE);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        startGame();
    }
    public void startGame(){
        newApple();
        Running = true;
        timer = new Timer(DELAY, this);
        timer.start();
    }
    public void paintComponent(Graphics G){
        super.paintComponent(G);
        draw(G);
    }
    public void draw(Graphics G){
        if(Running){
            /* This step was only to see the dimensions of the game by adding a gridlines to the box
            for (int i = 0; i < Screen_Height/UNIT_Size; i++) {
                G.drawLine(i*UNIT_Size, 0, i*UNIT_Size, Screen_Height);
                G.drawLine(0, i*UNIT_Size, Screen_WIDTH, i*UNIT_Size);
            } */
            G.setColor(Color.black);
            G.fillOval(Applex, Appley, UNIT_Size, UNIT_Size);

            for (int i = 0; i <bodyPart; i++) {
                if(i == 0){
                    G.setColor(Color.red);
                    G.fillRect(x[i], y[i], UNIT_Size, UNIT_Size);
             }else{
                   //(here we choose the color of the snake by referring to RGB sites or let it random) G.setColor(new Color(243,41,41));
                    G.setColor(new Color(random.nextInt(255),random.nextInt(255),random.nextInt(255)));
                    G.fillRect(x[i], y[i], UNIT_Size, UNIT_Size);
              }
              G.setColor(Color.DARK_GRAY);
              G.setFont(new Font("Ink Free",Font.BOLD,35));
              FontMetrics metrics= getFontMetrics(G.getFont());
              G.drawString("Score:"+AppleEatens, (Screen_WIDTH - metrics.stringWidth("Score:"+AppleEatens))/2, G.getFont().getSize());
        }
    }else{
        gameOver(G);
    }
    }
    public void newApple(){
        Applex = random.nextInt(Screen_WIDTH/UNIT_Size)*UNIT_Size;
        Appley = random.nextInt(Screen_Height/UNIT_Size)*UNIT_Size;

    }
    public void move(){
        for (int i = bodyPart; i > 0; i--) {
            x[i]= x[i-1];
            y[i]= y[i-1];
        }
        switch (direction) {
            case 'U':
                y[0] = y[0] - UNIT_Size;
                break;
            case 'D':
                y[0] = y[0] + UNIT_Size;
                break;
            case 'L':
                x[0] = x[0] - UNIT_Size;
                break;
            case 'R':
                x[0] = x[0] + UNIT_Size;
                break;
        }
    }
    public void checkApple(){
        if((x[0] == Applex) && (y[0] == Appley)){
            bodyPart++;
            AppleEatens++;
            newApple();
        }
    }
    public void checkCollisions(){
        // as it shown here we check if the snake head collides with body results in Game Over
        for (int i = bodyPart; i >0; i--) {
            if((x[0] == x[i] && y[0] == y[i])){
                Running =false;
            }
        }
        // X-Axis to check the left border and touches head 
        if(x[0] <0){
            Running=false;
        }
        // X-Axis to check the right border and touches head 
        if(x[0] >Screen_WIDTH){
            Running=false;
        }
        // Y-Axis to check the up border and touches head 
        if(y[0] < 0){
            Running = false;
        }
        // Y-Axis to check the  down border and touches head 
        if(y[0] > Screen_Height){
            Running = false;
        }
        if(!Running){
            timer.stop();
        }
    }
    public void gameOver(Graphics G){
        // Game Over Text to just give the user a msg that he did a mistake and game ended
        G.setColor(Color.DARK_GRAY);
        G.setFont(new Font("Ink Free",Font.BOLD,75));
        FontMetrics metrics1= getFontMetrics(G.getFont());
        G.drawString("Game Over", (Screen_WIDTH - metrics1.stringWidth("Game Over"))/2, Screen_Height/2);
        // Score text we can manage to change colors and type of text  
        G.setColor(Color.DARK_GRAY);
        G.setFont(new Font("Ink Free",Font.BOLD,35));
        FontMetrics metrics2= getFontMetrics(G.getFont());
        G.drawString("Score:"+AppleEatens, (Screen_WIDTH - metrics2.stringWidth("Score:"+AppleEatens))/2, G.getFont().getSize());
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if(Running){
            move();
            checkApple();
            checkCollisions();
        }
        repaint();
    }
    public class MyKeyAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e){
            switch (e.getKeyCode()) {
                case KeyEvent.VK_LEFT:
                    if(direction!= 'R'){
                        direction = 'L';
                    }
                    break;
                case KeyEvent.VK_RIGHT:
                    if(direction!= 'L'){
                        direction = 'R';
                    }
                    break;
                case KeyEvent.VK_UP:
                    if(direction!= 'D'){
                        direction = 'U';
                    }
                    break;
                case KeyEvent.VK_DOWN:
                    if(direction!= 'U'){
                        direction = 'D';
                    }
                    break;
                default:
                    break;
            }
        }
    }
}
