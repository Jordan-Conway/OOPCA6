package DAO;

import java.util.List;
import Classes.Gemstone;

public interface DAOInterface {
    List<Gemstone> findAllGemstones();

    Gemstone findGemstoneById();

    void deleteGemstoneById();

    void insertGemstone(Gemstone gemstone);
}
