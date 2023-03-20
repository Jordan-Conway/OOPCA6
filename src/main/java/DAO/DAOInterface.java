package DAO;

import java.util.List;
import Classes.Gemstone;

public interface DAOInterface {
    List<Gemstone> findAllGemstones();

    Gemstone findGemstoneById(int id);

    void deleteGemstoneById(int id);

    void insertGemstone(Gemstone gemstone);
}
