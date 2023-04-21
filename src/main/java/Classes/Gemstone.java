package Classes;

import Enums.Clarity;

import java.util.Collection;

public class Gemstone {
    private final int id;
    private final String gemName;
    private final double carats;
    private final String colour;
    private final Clarity clarity;

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

    public int getId() {
        return id;
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

    public static void printGemstones(Collection<Gemstone> gemstones){
        System.out.printf("|%5s|%15s|%10s|%15s|%10s|\n", "Id", "Name", "Carats", "Colour", "Clarity");
        for(int x=0; x<80; x++){
            System.out.print("-");
        }
        System.out.println();
        for(Gemstone gemstone: gemstones){
            System.out.printf("|%5d|%15s|%10.2f|%15s|%10s|\n", gemstone.id, gemstone.gemName, gemstone.carats, gemstone.colour, gemstone.clarity);
        }
    }
}
