package com.example.securex.data;

import com.example.securex.R;

import java.util.ArrayList;

public class SpinEight extends Spin{
    final int Degree;
    final ArrayList<String> Fruits;
    final ArrayList<String> Colors;
    final int ColorRing;
    final int FruitRing;
    private int PhaseChange;

    public SpinEight() {
        Degree=45;
        PhaseChange=7;


        FruitRing = R.drawable.fruiteight;
        ColorRing = R.drawable.coloreight;


        Fruits = new ArrayList<>();
        Fruits.add("stawberry");
        Fruits.add("apple");
        Fruits.add("grape");
        Fruits.add("cherry");
        Fruits.add("banana");
        Fruits.add("orange");
        Fruits.add("mango");
        Fruits.add("melon");


        Colors = new ArrayList<>();
        Colors.add("purple");
        Colors.add("pink");
        Colors.add("orange");
        Colors.add("blue");
        Colors.add("red");
        Colors.add("black");
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

    public int getColorRing() {
        return ColorRing;
    }

    public int getFruitRing() {
        return FruitRing;
    }
    public int getPhaseChange() {
        return PhaseChange;
    }

}

