/**
 * This is a brick breaking game where the objective is to control a ball using a paddle to break bricks.
 * Isiah Castro
 * 7/9/22
 * I pledge that this is my own work.
 */
package breakout;

import acm.graphics.*;
import acm.program.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class Breakout extends GraphicsProgram {

    /**
     * Width of the game display (all coordinates are in pixels)
     */
    private static final int WIDTH = 400;
    /**
     * Height of the game display
     */
    private static final int HEIGHT = 600;
    /**
     * Width of the paddle
     */
    private static final int PADDLE_WIDTH = 50;
    /**
     * Height of the paddle
     */
    private static final int PADDLE_HEIGHT = 10;
    /**
     * Offset of the paddle up from the bottom
     */
    private static final int PADDLE_Y_OFFSET = 50;
    /**
     * Number of bricks per row
     */
    private static final int NBRICKS_PER_ROW = 7;
    /**
     * Number of rows of bricks
     */
    private static final int NBRICK_ROWS = 4;
    /**
     * Separation between bricks
     */
    private static final int BRICK_SEP = 5;
    /**
     * Width of a brick
     */
    private static final int BRICK_WIDTH
            = WIDTH / NBRICKS_PER_ROW - BRICK_SEP;
    /**
     * Height of a brick
     */
    private static final int BRICK_HEIGHT = 8;
    /**
     * Radius of the ball in pixels
     */
    private static final int BALL_RADIUS = 10;
    /**
     * Offset of the top brick row from the top
     */
    private static final int BRICK_Y_OFFSET = 70;
    /**
     * Number of turns
     */
    private static final int ATTEMPTS = 3;
    /**
     * Pause between each step in the animation
     */
    private static final int PAUSE_TIME = 3;

    private static final int PAUSE_RESET = 1000;

    /**
     * Creating the paddle object and setting default location and size
     */
    private final GRect paddle = new GRect(WIDTH / 2.0 - PADDLE_WIDTH / 2.0,
            HEIGHT, PADDLE_WIDTH, PADDLE_HEIGHT);

    public static void main(String[] args) {
        String[] sizeArgs = {"width=" + WIDTH, "height=" + HEIGHT};
        new Breakout().start(sizeArgs);
    }

    @Override
    public void mouseMoved(MouseEvent me) {
        // The paddle does not move in the y direction, so you only need to 
        // pay attention to the mouse's x coordinate.
        // Change the variable name if you wish.
        int mouseX = me.getX() - 25;

        //mouse bounds
        if (mouseX > WIDTH - PADDLE_WIDTH || mouseX < 0) {
            return;
        }
        //setting paddle location
        paddle.setLocation(mouseX, HEIGHT - PADDLE_Y_OFFSET);

    }

    /**
     * @param position_x to check if it's hitting wall bounds.
     * @param position_y to check if it's hitting wall bounds.
     * @param velocity   current velocity value to be updated if needed.
     * @return returns modified velocity array
     */
    public double[] wallCheck(double position_x, double position_y, double[] velocity) {
        // collision bottom ball detection
        GObject collisionBottom = getElementAt(position_x, position_y + (BALL_RADIUS));

        //right wall hit detection
        if (position_x >= WIDTH - BALL_RADIUS && velocity[0] > 0) {
            velocity[0] = -velocity[0];
        }
        //left wall hit detection
        if (position_x <= 0 && velocity[0] < 0) {
            velocity[0] = -velocity[0];
        }
        //bottom wall hit detection
        if (position_y >= HEIGHT - (BALL_RADIUS * 3) && velocity[1] > 0 || collisionBottom == paddle) {
            velocity[1] = -velocity[1];
        }
        //top wall hit detection
        if (position_y <= 0 && velocity[1] < 0) {
            velocity[1] = -velocity[1];
        }
        //return modified velocity
        return velocity;
    }
    /**
     * Method: run() Runs the Breakout program.
     */
    public void run() {
        /*
         *  drawing paddle to screen and setting fill
         */
        paddle.setFilled(true);
        add(paddle);

        /*
         * Adding GOVal as "ball" and setting its default spawn point (if no .setlocation is found) to middle of screen.
         * setFill
         * setColor
         * adds ball
         */
        GOval ball = new GOval(WIDTH / 2.0 - BALL_RADIUS / 2.0, HEIGHT / 2.0, BALL_RADIUS, BALL_RADIUS);
        ball.setFilled(true);
        ball.setFillColor(Color.BLACK);
        add(ball);

        //game logic variable not in use yet
        boolean isDone = false;

        /*
         * iteration variables for brick counting
         *  r is used to count brick rows printed
         *  i is used to count amount of bricks per row printed
         */
        int i = 0;
        int r = 0;
        /*
         * brickSpace used to calculate the space between each brick drawn
         * brickRowOffset used to calculate each row offset
         */
        double brickSpace = BRICK_SEP / 2.0;
        int brickRowOffset = BRICK_Y_OFFSET;

        //brick printing logic
        while (r != NBRICK_ROWS) {
            while (i < NBRICKS_PER_ROW) {
                // GRect location set to the iterating brickSpace and brickRowOffset for x and y respectively
                GRect brick = new GRect(brickSpace, brickRowOffset, BRICK_WIDTH, BRICK_HEIGHT);
                brick.setFilled(true);
                brick.setColor(Color.blue);
                add(brick);
                /*
                 * brickSpace iteration code, each time loop is iterated brickSpace = brickSpace + BRICK_WIDTH + BRICK_SEP
                 */
                brickSpace = brickSpace + BRICK_WIDTH + BRICK_SEP;

                //i iteration to count how many bricks have been printed
                i++;
            }
            /*
             * new row print logic
             * i reset so that i < NBRICKS_PER_ROW and will print new line of bricks
             * r iteration to count number of rows
             */
            i = 0;
            r++;

            //brickRowOffset calculations, each time it iterates it moves down by BRICK_HEIGHT + BRICK_SEP
            brickRowOffset += BRICK_HEIGHT + BRICK_SEP;
            //resetting brick space to default value, so it can begin in the correct position on new line
            brickSpace = BRICK_SEP / 2.0;
        }

        //adding mouse listener
        addMouseListeners();

        //game variables
        Random randGen = new Random();

        //new velocity array
        double[] velocity = new double[2];

        //setting velocity[0] to generate random x value
        velocity[0] = randGen.nextDouble() * 3.0 + 1.0;

        //randomly setting velocity[0] value to positive or negative
        if (randGen.nextBoolean()) {
            velocity[0] = -velocity[0];
        }

        //setting y default velocity value
        velocity[1] = 1;

        //new position array
        double[] position = new double[2];
        /*
         * position [0] == X ball location
         * position [1] == Y ball location
         */
        position[0] = WIDTH / 2.0 - (BALL_RADIUS / 2.0);
        position[1] = HEIGHT / 2.0;

        //game doesn't start until startState == false
        boolean startState = false;

        //attempt counter
        int attempts = ATTEMPTS;

        //brick counter
        int numBricks = NBRICK_ROWS * NBRICKS_PER_ROW;

        //game msg label x location
        double endMsgLabelX;

        //game win state variable
        boolean brickWin = false;

        // repeating game code
        while (!isDone) {

            //collision objects
            GObject collisionBottomLeft = getElementAt(position[0], position[1] + (BALL_RADIUS * 2));
            GObject collisionBottomRight = getElementAt(position[0] + (BALL_RADIUS * 2), position[1] + (BALL_RADIUS * 2));
            GObject collisionTopLeft = getElementAt(position[0], position[1]);
            GObject collisionTopRight = getElementAt(position[0] + (BALL_RADIUS * 2), position[1]);

            //collision brick detection & deletion
            if (collisionBottomLeft != null && collisionBottomLeft != paddle) {
                remove(collisionBottomLeft);
                velocity[1] = velocity[1] * -1;
                numBricks--;
                System.out.println("collisionBottomLeft = " + collisionBottomLeft);
            } else if (collisionTopLeft != null && collisionTopLeft != paddle) {
                remove(collisionTopLeft);
                velocity[1] = velocity[1] * -1;
                numBricks--;
                System.out.println("collisionTopLeft = " + collisionTopLeft);

            } else if (collisionBottomRight != null && collisionBottomRight != paddle) {
                remove(collisionBottomRight);
                System.out.println("collisionBottomRight = " + collisionBottomRight);
                velocity[1] = -velocity[1] * -1;
                numBricks--;
            } else if (collisionTopRight != null && collisionTopRight != paddle) {
                remove(collisionTopRight);
                System.out.println("collisionTopRight = " + collisionTopRight);
                velocity[1] = velocity[1] * -1;
                numBricks--;
            }

            //reset & pause state
            while (paddle.getX() == WIDTH / 2.0 - PADDLE_WIDTH / 2.0 && !startState) {
                ball.setLocation(position[0], position[1]);
                if (attempts >= 3) {
                    paddle.setLocation(WIDTH / 2.0 - PADDLE_WIDTH / 2.0, HEIGHT - PADDLE_Y_OFFSET);
                }
                if (paddle.getX() != WIDTH / 2.0 - PADDLE_WIDTH / 2.0) {
                    startState = true;
                }
            }
            //attempt counting and reset
            if (position[1] == HEIGHT - (BALL_RADIUS * 3)) {
                attempts = attempts - 1;
                startState = false;
                position[0] = WIDTH / 2.0 - (BALL_RADIUS / 2.0);
                position[1] = HEIGHT / 2.0;
                velocity[1] = 1;
                ball.setLocation(position[0], position[1]);
                if(attempts != 0) {
                    pause(PAUSE_RESET);
                }
            }

            //setting position to iterate using velocity values
            position[0] += velocity[0];
            position[1] += velocity[1];

            //setting ball location
            ball.setLocation(position[0], position[1]);

            /*
             * main wall detection functions
             * updating velocity to = wallCheck functions, which based on position inverses velocity
             */
            velocity = wallCheck(position[0], position[1], velocity);

            if (attempts == 0) {
                isDone = true;
            }

            if (numBricks == 0){
                brickWin = true;
                isDone = true;
            }

            pause(PAUSE_TIME);

        }

        /*
         *when isDone = true;
         * game end msg's depending on win or loss
         */
        remove(ball);
        remove(paddle);
        if(!brickWin) {
            GLabel gameOver = new GLabel("Game over");
            gameOver.setFont("Times-20");
            gameOver.setColor(Color.RED);
            add(gameOver);
            endMsgLabelX = (WIDTH - gameOver.getWidth()) / 2;
            gameOver.setLocation(endMsgLabelX, HEIGHT / 2.0);

        } else {
            GLabel gameWin = new GLabel("You win!");
            gameWin.setFont("Times-20");
            endMsgLabelX = (WIDTH - gameWin.getWidth()) / 2;
            gameWin.setLocation(endMsgLabelX,HEIGHT / 2.0);
            gameWin.setColor(Color.GREEN);
            add(gameWin);

        }

    }

}


