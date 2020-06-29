package com.example.securex.data;

import com.example.securex.R;

import java.util.ArrayList;

public class SpinSix extends Spin{

    final int Degree;
    final ArrayList<String> Fruits;
    final ArrayList<String> Colors;
    final int ColorRing;
    final int FruitRing;
    private int PhaseChange;

    public SpinSix() {
        Degree=60;
        PhaseChange=5;
        FruitRing = R.drawable.fruitsix;
        ColorRing = R.drawable.colorsix;

        Fruits = new ArrayList<>();
        Fruits.add("apple");
        Fruits.add("grape");
        Fruits.add("cherry");
        Fruits.add("banana");
        Fruits.add("orange");
        Fruits.add("mango");

        Colors = new ArrayList<>();
        Colors.add("purple");
        Colors.add("pink");
        Colors.add("blue");
        Colors.add("red");
        Colors.add("yellow");
        Colors.add("green");

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

    @Override
    public int getColorRing() {
        return ColorRing;
    }

    @Override
    public int getFruitRing() {
        return FruitRing;
    }
    public int getPhaseChange() {
        return PhaseChange;
    }
}