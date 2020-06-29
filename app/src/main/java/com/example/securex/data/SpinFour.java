package com.example.securex.data;

import com.example.securex.R;

import java.util.ArrayList;

public class SpinFour extends Spin {

    final int Degree;
    final ArrayList<String> Fruits;
    final ArrayList<String> Colors;
    final int ColorRing;
    final int FruitRing;

    @Override
    public int getColorRing() {
        return ColorRing;
    }

    @Override
    public int getFruitRing() {
        return FruitRing;
    }

    private int PhaseChange;

    public SpinFour() {
        Degree=90;
        PhaseChange=3;

        FruitRing = R.drawable.fruitfour;
        ColorRing = R.drawable.colorfour;



        Fruits = new ArrayList<>();
        Fruits.add("apple");
        Fruits.add("banana");
        Fruits.add("orange");
        Fruits.add("mango");

        Colors = new ArrayList<>();
        Colors.add("blue");
        Colors.add("yellow");
        Colors.add("green");
        Colors.add("red");

    }

    public int getDegree() {
        return Degree;
    }

    public ArrayList<String> getFruits() {
        return Fruits;
    }
    public ArrayList<String> getColors() {
        return Colors;
    }

    public int getPhaseChange() {
        return PhaseChange;
    }
}
