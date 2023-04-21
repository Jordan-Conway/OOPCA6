package Comparators;

import Classes.Gemstone;

import java.util.Comparator;

public class GemstoneNameComparator implements Comparator<Gemstone> {
    @Override
    public int compare(Gemstone o1, Gemstone o2) {
        return (o1.getGemName().compareTo(o2.getGemName()));
    }
}
