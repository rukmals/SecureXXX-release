package com.example.securex.registration;

public class Colors {


        String[] colorsFour;
        String[] colorsSix;
        String[] colorsEight;

        public Colors() {

            colorsFour = new String[]{"blue","red","green","yellow"};
            colorsSix = new String[]{"blue","red","green","yellow","pink","purple"};
            colorsEight = new String[]{"blue","red","green","yellow","pink","purple","black","orange"};

        }

        public String[] getColorsFour() {
            return colorsFour;
        }

        public String[] getColorsSix() {
            return colorsSix;
        }

        public String[] getColorsEight() {
            return colorsEight;
        }
    }
