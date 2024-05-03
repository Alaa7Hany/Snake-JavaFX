package com.example.snake;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;

public class HelloApplication extends Application {

    final double HEIGHT = 600;
    final double WIDTH = 600;
    int score, topScore, level = 1;
    boolean up, right, down, left, working;
    Line leftLine, uptLine, rightLine, downLine;
    Rectangle rectScore;
    double radius = 7, speed = 7;
    double xCenter, yCenter, xBody1, yBody1, xBody2, yBody2, randomX, randomY;
    Circle head = new Circle(radius, Color.WHITE);
    Pane root = new Pane();
    Line[] walls = new Line[4];
    VBox scoreLevelBox;
    Label scoreLabel, levelLabel, topScoreLabel, startLabel, gameOverLabel;
    Timeline gameplay;

    ArrayList<Body> bodyParts = new ArrayList<>();
    Fruit fruit;
    Scene scene = new Scene(root, WIDTH, HEIGHT);


    @Override
    public void start(Stage stage) {
        setLayout();
        setGameplay();
        controlMovement();
        stage.setTitle("Snake");
        stage.setScene(scene);
        // disable maximize button
        stage.maximizedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue)
                stage.setMaximized(false);
        });
        stage.show();
    }

    void setLayout() {
        // setting the walls
        String backColor = String.valueOf(Color.DARKSLATEGRAY).substring(2);
        String scoreColor = String.valueOf(Color.BLACK).substring(2);
        root.setStyle("-fx-background-color: #" + backColor);
        leftLine = new Line(0, 0, 0, HEIGHT);
        uptLine = new Line(0, 0, WIDTH - 100, 0);
        rightLine = new Line(WIDTH - 100, 0, WIDTH - 100, HEIGHT);
        downLine = new Line(0, HEIGHT, WIDTH - 100, HEIGHT);
        walls[0] = leftLine;
        walls[1] = uptLine;
        walls[2] = rightLine;
        walls[3] = downLine;
        for (Line i : walls) {
            i.setStrokeWidth(10);
            i.setStroke(Color.BLACK);
            root.getChildren().add(i);
        }

        // setting the score side
        rectScore = new Rectangle(WIDTH - 100, 0, WIDTH, HEIGHT);
        rectScore.setStyle("-fx-background-color: #" + scoreColor);
        root.getChildren().add(rectScore);
        scoreLabel = new Label("Score: " + score);
        levelLabel = new Label("Level: " + level);
        topScoreLabel = new Label("Top: " + topScore);
        scoreLabel.setTextFill(Color.WHITE);
        levelLabel.setTextFill(Color.WHITE);
        topScoreLabel.setTextFill(Color.WHITE);
        scoreLabel.setFont(new Font(20));
        levelLabel.setFont(new Font(20));
        topScoreLabel.setFont(new Font(20));
        scoreLevelBox = new VBox(20, scoreLabel, levelLabel, topScoreLabel);
        scoreLevelBox.setLayoutX(WIDTH - 100);
        scoreLevelBox.setTranslateY(20);
        root.getChildren().add(scoreLevelBox);

        // adding the head
        head.setCenterX((WIDTH - 100) / 2);
        head.setCenterY(HEIGHT / 2);
        root.getChildren().add(head);

        // setting the start and game over labels
        startLabel = new Label("Press enter to start");
        startLabel.setFont(new Font(30));
        startLabel.setLayoutX(120);
        startLabel.setTranslateY(20);
        startLabel.setTextFill(Color.WHITE);
        root.getChildren().add(startLabel);
        gameOverLabel = new Label("GAME OVER\npress enter to restart");
        gameOverLabel.setTextAlignment(TextAlignment.CENTER);
        gameOverLabel.setFont(new Font(30));
        gameOverLabel.setLayoutX(110);
        gameOverLabel.setTranslateY(20);
        gameOverLabel.setTextFill(Color.WHITE);
    }

    void setGameplay() {
        /* It's essential that the radius and the speed are equal so that the body can follow the
        head and forming the shape of the snake*/
        /* The main idea for the snake movement is before the head moves we store its position
         and give it to the next part and before this part moves we store its position
         and give it to the part after it and so on */
        gameplay = new Timeline(new KeyFrame(Duration.millis(35), e -> {
            // moving the body
            try {
                xBody1 = bodyParts.get(0).getCenterX();
                yBody1 = bodyParts.get(0).getCenterY();
                bodyParts.get(0).setCenterX(xCenter);
                bodyParts.get(0).setCenterY(yCenter);
                for (int i = 1; i < bodyParts.size(); i++) {
                    xBody2 = bodyParts.get(i).getCenterX();
                    yBody2 = bodyParts.get(i).getCenterY();
                    bodyParts.get(i).setCenterX(xBody1);
                    bodyParts.get(i).setCenterY(yBody1);
                    xBody1 = xBody2;
                    yBody1 = yBody2;
                }
            } catch (Exception exception) {
            }
            xCenter = head.getCenterX();
            yCenter = head.getCenterY();
            // moving the head
            if (right)
                head.setCenterX(head.getCenterX() + speed);
            if (left)
                head.setCenterX(head.getCenterX() - speed);
            if (up)
                head.setCenterY(head.getCenterY() - speed);
            if (down)
                head.setCenterY(head.getCenterY() + speed);

            // check for hitting the walls
            for (Line i : walls) {
                if (i.intersects(head.getBoundsInParent())) {
                    gameOver();
                    System.out.println("crashed");
                }
            }
            // check for eating itself
            for (int i = 1; i < bodyParts.size(); i++) {
                if (head.getCenterX() == bodyParts.get(i).getCenterX()
                        && head.getCenterY() == bodyParts.get(i).getCenterY()) {
                    gameOver();
                    System.out.println("ate itself");
                }
            }
            // check for eating a fruit
            if (head.intersects(fruit.getBoundsInParent())) {
                root.getChildren().remove(fruit);
                score++;
                scoreLabel.setText("Score: " + score);
                addFruit();
                // growing the body
                growBody();
            }
        }));
        gameplay.setCycleCount(Timeline.INDEFINITE);
    }
    void growBody(){
        Body b1 = new Body(radius);
        b1.setCenterX(1000);
        b1.setCenterY(1000);
        root.getChildren().add(b1);
        bodyParts.add(b1);
    }

    void gameOver() {
        gameplay.stop();
        working = false;
        root.getChildren().remove(fruit);
        root.getChildren().add(gameOverLabel);
    }

    void restart() {
        System.out.println("restart");
        head.setCenterX((WIDTH - 100) / 2);
        head.setCenterY(HEIGHT / 2);
        root.getChildren().remove(gameOverLabel);
        root.getChildren().remove(startLabel);
        for (Body i : bodyParts)
            root.getChildren().remove(i);
        bodyParts.clear();
        addFruit();
        if (topScore < score) {
            topScore = score;
            topScoreLabel.setText("Top: " + topScore);
        }
        score = 0;
        scoreLabel.setText("Score: " + score);
        gameplay.play();
        working = true;
        growBody();
        growBody();
    }

    void addFruit() {
        fruit = new Fruit((int) (Math.random() * 7));
        do {
            randomX = Math.random() * WIDTH;
        } while (randomX < 5 || randomX > (WIDTH - 100 - fruit.getWidth() - 5));
        do {
            randomY = (int) (Math.random() * HEIGHT);
        } while (randomY < 5 || randomY > (HEIGHT - fruit.getHeight() - 5));
        fruit.setX(randomX);
        fruit.setY(randomY);
        root.getChildren().add(fruit);
    }


    void controlMovement() {
        scene.setOnKeyPressed(e -> {
            if (working) {

                if (e.getCode() == KeyCode.UP && !down) {
                    up = true;
                    right = false;
                    left = false;

                }
                if (e.getCode() == KeyCode.RIGHT && !left) {
                    right = true;
                    up = false;
                    down = false;
                }
                if (e.getCode() == KeyCode.DOWN && !up) {
                    down = true;
                    right = false;
                    left = false;
                }
                if (e.getCode() == KeyCode.LEFT && !right) {
                    left = true;
                    up = false;
                    down = false;
                }
                // for testing purposes
//                if (e.getCode() == KeyCode.SPACE) {
//                    Body b1 = new Body(radius);
//                    b1.setCenterX(1000);
//                    b1.setCenterY(1000);
//                    root.getChildren().add(b1);
//                    bodyParts.add(b1);
//                }
            } else {
                if (e.getCode() == KeyCode.ENTER){
                    restart();
                    up = true;
                    right = false;
                    left = false;
                    down = false;
                }
            }
        });
    }


    public static void main(String[] args) {
        launch();
    }
}