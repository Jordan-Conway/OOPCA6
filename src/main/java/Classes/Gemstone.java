package Classes;

import Enums.Clarity;

public class Gemstone {
    private int id;
    private String gemName;
    private double carats;
    private String colour;
    private Clarity clarity;

    public Gemstone(int id, String name, double carats, String colour, Clarity clarity){
        this.id = id;
        this.gemName = name;
        this.carats = carats;
        this.colour = colour;
        this.clarity = clarity;
    }

    @Override
    public String toString() {
        return "Gemstone{" +
                "id=" + id +
                ", gemName='" + gemName + '\'' +
                ", carats=" + carats +
                ", colour='" + colour + '\'' +
                ", clarity=" + clarity +
                '}';
    }

    public String getGemName() {
        return gemName;
    }

    public double getCarats() {
        return carats;
    }

    public String getColour() {
        return colour;
    }

    public Clarity getClarity() {
        return clarity;
    }
}
