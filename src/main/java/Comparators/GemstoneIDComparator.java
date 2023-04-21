package Comparators;

import Classes.Gemstone;

import java.util.Comparator;

public class GemstoneIDComparator implements Comparator<Gemstone> {
    @Override
    public int compare(Gemstone o1, Gemstone o2) {
        return Integer.compare(o1.getId(), o2.getId());
    }
}
