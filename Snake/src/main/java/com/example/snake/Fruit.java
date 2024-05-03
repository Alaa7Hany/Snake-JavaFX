package com.example.snake;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Fruit extends Rectangle {
    public Fruit(int random){
        switch (random){
            case 0:
                setHeight(5);
                setWidth(5);
                setFill(Color.WHEAT);
                break;
            case 1:
                setHeight(7);
                setWidth(7);
                setArcHeight(7);
                setArcWidth(7);
                setFill(Color.CYAN);
                break;
            case 2:
                setHeight(10);
                setWidth(10);
                setArcHeight(5);
                setArcWidth(5);
                setFill(Color.GREEN);
                break;
            case 3:
                setHeight(5);
                setWidth(10);
                setFill(Color.YELLOW);
                break;
            case 4:
                setHeight(14);
                setWidth(8);
                setArcHeight(8);
                setArcWidth(8);
                setFill(Color.VIOLET);
                break;
            case 5:
                setHeight(15);
                setWidth(15);
                setArcHeight(8);
                setArcWidth(8);
                setFill(Color.GRAY);
                break;
            default:
                setHeight(14);
                setWidth(14);
                setArcHeight(14);
                setArcWidth(14);
                setFill(Color.HOTPINK);
                break;
        }
    }
}
