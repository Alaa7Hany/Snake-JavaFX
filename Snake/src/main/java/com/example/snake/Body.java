package com.example.snake;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Body extends Circle {
    private double radius;
    public Body(double radius){
        this.radius = radius;
        setRadius(radius);
        setFill(Color.RED);
    }

}
