import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;

public class FlappyBird extends JPanel implements ActionListener,KeyListener{
    int boardWidth=360;
    int boardHeight=640;
 
    //Images
    Image backgroundImg;
    Image birdImg;
    Image topPipeImg;
    Image bottomPipeImg;


    //bird
    int birdX= boardWidth/8;
    int birdY=boardHeight/2;
    int birdWidth=34;
    int birdHeight=24;

    class Bird {
        int x=birdX;
        int y=birdY;
        int width=birdWidth;
        int height=birdHeight;
        Image img;

        Bird(Image img){
            this.img=img;
        }
        }

        //Pipes
        int pipeX=boardWidth;
        int pipeY=0;
        int pipeWidth=64;
        int pipeHeight=512;

       class Pipe{
        int x=pipeX;
        int y=pipeY;
        int width = pipeWidth;
        int height=pipeHeight;
        Image img;
        boolean passed=false;

        Pipe(Image img){
            this.img=img;
        }

       }

        //game logic
        Bird bird;
        int velocityX=-4; //mving pipe to the left
        int velocityY=0; // moving bird up and down speed
        int gravity=1;

        ArrayList<Pipe> pipes; //since we have multiples pipes so we store it in a list
        Random random = new Random();

        Timer gameLoop;
        Timer placePipesTimer;

        boolean gameOver=false;

        double score=0;


    FlappyBird(){
        // System.out.println("Constructor");
        setPreferredSize(new Dimension(boardWidth,boardHeight));
        // setBackground(Color.blue);

        setFocusable(true);
        addKeyListener(this);
        // load images

    backgroundImg=new ImageIcon(getClass().getResource("/flappybirdbg.png")).getImage();
    birdImg=new ImageIcon(getClass().getResource("/flappybird.png")).getImage();
    topPipeImg=new ImageIcon(getClass().getResource("/toppipe.png")).getImage();
    bottomPipeImg=new ImageIcon(getClass().getResource("/bottompipe.png")).getImage();
    
    //bird
    bird=new Bird(birdImg);
    pipes =new ArrayList<Pipe>();
    //place pipes timesr
    placePipesTimer=new Timer(1500,new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e){
            placePipes();
        }
    });

    placePipesTimer.start();

    //game timer
    gameLoop=new Timer(1000/60,this);
    gameLoop.start();

    }

    public void placePipes(){
        int randomPipeY=(int)(pipeY-pipeHeight/4-Math.random()*(pipeHeight/2));
        int openingSpace=boardHeight/4;
        Pipe topPipe=new Pipe(topPipeImg);
        topPipe.y=randomPipeY;
        pipes.add(topPipe);

        Pipe bottomPipe=new Pipe(bottomPipeImg);
        bottomPipe.y=topPipe.y+pipeHeight+openingSpace;
        pipes.add(bottomPipe);
    }


public void paintComponent(Graphics g){
    super.paintComponent(g);
    draw(g);

}

public void draw(Graphics g){

    // System.out.println("draw");

    //backgrnd
    g.drawImage(backgroundImg,0, 0, boardWidth,boardHeight,null);

    //bird
    g.drawImage(birdImg, bird.x, bird.y, bird.width, bird.height, null);

    //pipes
    for(int i=0;i<pipes.size();i++){
        Pipe pipe=pipes.get(i);
        g.drawImage(pipe.img,pipe.x,pipe.y,pipe.width,pipe.height,null);
    }

    //score
    g.setColor(Color.white);
    g.setFont(new Font("Arial",Font.PLAIN,32));
    if(gameOver){
        g.drawString("Game over: " +String.valueOf((int) score),10,35);
    }else{
        g.drawString(String.valueOf((int) score),10,35);
    }
}

public void move(){

    //bird
    velocityY+=gravity;
    bird.y+=velocityY;
    bird.y=Math.max(bird.y,0);

    //pipes
    for(int i=0;i<pipes.size();i++){
        Pipe pipe =pipes.get(i);
        pipe.x+=velocityX;

        if(!pipe.passed && bird.x> pipe.x+pipe.width){
            pipe.passed=true;
            score+=0.5; //since two pipes, so 1 point for each pipes
        }


        if(collision(bird, pipe)){
            gameOver=true;
        }
        
    }

    if(bird.y>boardHeight){
        gameOver=true;
    }
   
}

public boolean collision(Bird a,Pipe b){
    return a.x<b.x+b.width && 
           a.x+a.width>b.x &&
           a.y<b.y+b.height &&
           a.y+a.height>b.y;

}

@Override
public void actionPerformed(ActionEvent e) {
    move();
    repaint();
    if(gameOver){
        placePipesTimer.stop();
        gameLoop.stop();
    }
}

@Override
public void keyPressed(KeyEvent e) {
   if(e.getKeyCode()==KeyEvent.VK_SPACE){
    velocityY=-9;
    if(gameOver){
        //resetting the conditions to default value
        bird.y=birdY;
        velocityY=0;
        pipes.clear();
        score=0;
        gameOver=false;
        gameLoop.start();
        placePipesTimer.start();
    }
    // System.out.println("Space");
   }
   
}

@Override
public void keyTyped(KeyEvent e) {
  
}


@Override
public void keyReleased(KeyEvent e) {
   
}

}

//syed mehadee