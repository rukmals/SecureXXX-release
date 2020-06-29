package com.example.securex.data;

import java.util.ArrayList;

public class Spin {

    private int Degree;
    private ArrayList<String> Fruits;
    private ArrayList<String> Colors;
    private int ColorRing;
    private int FruitRing;
    private int PhaseChange;


    public int getDegree(){
        return Degree;
    };

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
