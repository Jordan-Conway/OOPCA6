package DAO;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import Classes.Gemstone;

public class DAO implements DAOInterface {
    private Connection connection;

    @Override
    public List<Gemstone> findAllGemstones(){
        ArrayList<Gemstone> allGemstones = new ArrayList<>();

        return allGemstones;
    }

    @Override
    public Gemstone findGemstoneById() {
        return null;
    }

    @Override
    public void deleteGemstoneById() {

    }

    @Override
    public void insertGemstone(Gemstone gemstone) {

    }
}
