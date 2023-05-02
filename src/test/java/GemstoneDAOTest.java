import Classes.Gemstone;
import Comparators.GemstoneNameComparator;
import DAO.GemstoneDAO;
import Enums.Clarity;
import org.junit.Test;
import org.junit.Assert;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GemstoneDAOTest {
    private GemstoneDAO gemstoneDAO;

    @Test
    public void testFind(){
        gemstoneDAO = new GemstoneDAO();

        Gemstone expected = new Gemstone(1, "Amethyst", 2.55, "Siberian", Clarity.VVS2);

        Gemstone actual = gemstoneDAO.findGemstoneById(expected.getId());

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testAdd(){
        gemstoneDAO = new GemstoneDAO();

        Gemstone toAdd = new Gemstone(0, "Test", 0, "Test", Clarity.i1);

        gemstoneDAO.insertGemstone(toAdd);

        Gemstone expected = new Gemstone(gemstoneDAO.getLastId(),"Test", 0, "Test", Clarity.i1);
        Gemstone actual = gemstoneDAO.findGemstoneById(expected.getId());

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testGetIds(){
        gemstoneDAO = new GemstoneDAO();

        List<Gemstone> gemstones = gemstoneDAO.findAllGemstones();
        Set<Integer> expected = new HashSet<>();
        for(Gemstone gemstone: gemstones){
            expected.add(gemstone.getId());
        }

        Set<Integer> actual = gemstoneDAO.getIds();

        Assert.assertEquals(expected, actual);
    }


}
