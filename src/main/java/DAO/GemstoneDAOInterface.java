package DAO;

import java.util.List;
import java.util.Set;

import Classes.Gemstone;

public interface GemstoneDAOInterface {
    List<Gemstone> findAllGemstones();

    Gemstone findGemstoneById(int id);

    boolean deleteGemstoneById(int id);

    boolean insertGemstone(Gemstone gemstone);

    Set<Integer> getIds();
}
