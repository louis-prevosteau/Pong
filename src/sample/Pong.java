package sample;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.Random;

public class Pong extends Application {
    private static final int width = 800;
    private static final int height = 600;
    private static final int PLAYER_WIDTH = 20;
    private static final int PLAYER_HEIGHT = 100;
    private static final double BALL_R = 20;
    private int ballXSpeed = 1;
    private int ballYSpeed = 1;
    private double P1YPos = height / 2;
    private double P2YPos = height / 2;
    private double BallXPos = width / 2;
    private double BallYPos = width / 2;
    private int scoreP1 = 0;
    private int scoreP2 = 0;
    private boolean start;
    private double P1XPos = 0;
    private double P2XPos = height - PLAYER_WIDTH;

    public void start(Stage stage) throws Exception{
        stage.setTitle("Pong");
        Canvas canvas = new Canvas(width,height);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        Timeline t1 = new Timeline(new KeyFrame(Duration.millis(10), e ->run(gc)));
        t1.setCycleCount(Timeline.INDEFINITE);

        canvas.setOnMouseMoved(event -> P1YPos = event.getY());
        canvas.setOnMouseClicked(event -> start = true);
        stage.setScene(new Scene(new StackPane(canvas)));
        stage.show();
        t1.play();
    }

    private void run(GraphicsContext gc) {
        gc.setFill(Color.GREEN);
        gc.fillRect(0,0,width,height);
        gc.setFill(Color.WHITE);
        gc.setFont(Font.font(20));

        if (start){
            BallXPos += ballXSpeed;
            BallYPos += ballYSpeed;

            if (BallXPos <= width - width / 4){
                P2YPos = BallYPos - PLAYER_HEIGHT / 2;
            }else {
                P2YPos = BallYPos > P2YPos + PLAYER_HEIGHT / 2 ?P2YPos += 1: P2YPos -1;
            }
            gc.fillOval(BallXPos,BallYPos,BALL_R,BALL_R);
        }else {
            gc.setStroke(Color.WHITE);
            gc.setTextAlign(TextAlignment.CENTER);
            gc.strokeText("Start",width / 2, height / 2);

            BallXPos = width / 2;
            BallYPos = height / 2;

            ballXSpeed = new Random().nextInt(2) == 0 ? 1: -1;
            ballYSpeed = new Random().nextInt(2) == 0 ? 1: -1;
        }

        if (BallYPos > height || BallYPos < 0) ballYSpeed *= -1;

        if (BallXPos < P1XPos - PLAYER_WIDTH){
            scoreP2++;
            start = false;
        }

        if (BallXPos > P2XPos + PLAYER_WIDTH){
            scoreP1++;
            start = false;
        }

        if (((BallXPos + BALL_R > P2XPos) && BallYPos >= P2YPos && BallYPos <= P2YPos + PLAYER_HEIGHT) ||
        ((BallXPos < P1XPos + PLAYER_WIDTH) && BallYPos >= P1YPos && BallYPos <= P1YPos + PLAYER_HEIGHT)){
            ballYSpeed += 1 * Math.signum(ballYSpeed);
            ballXSpeed += 1 * Math.signum(ballXSpeed);
            ballXSpeed *= -1;
            ballYSpeed *= -1;
        }

        gc.fillText(scoreP1 + "\t\t\t\t\t" + scoreP2, width / 2, 100);
        gc.fillRect(P2XPos,P2YPos,PLAYER_WIDTH,PLAYER_HEIGHT);
        gc.fillRect(P1XPos,P1YPos,PLAYER_WIDTH,PLAYER_HEIGHT);
    }

}
