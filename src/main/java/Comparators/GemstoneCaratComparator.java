package Comparators;

import Classes.Gemstone;

import java.util.Comparator;

public class GemstoneCaratComparator implements Comparator<Gemstone> {

    @Override
    public int compare(Gemstone o1, Gemstone o2) {
        return Double.compare(o2.getCarats(), o1.getCarats());
    }
}
