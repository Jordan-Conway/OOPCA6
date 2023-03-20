package Classes;

import Enums.Clarity;

public class Gemstone {
    private String gemName;
    private double carats;
    private String colour;
    private Clarity clarity;

    public Gemstone(String name, double carats, String colour, Clarity clarity){
        this.gemName = name;
        this.carats = carats;
        this.colour = colour;
        this.clarity = clarity;
    }


}
