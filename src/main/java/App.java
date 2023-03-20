import Classes.Gemstone;
import DAO.DAO;

import java.util.List;

public class App {
    public static void main(String[] args) {
        App app = new App();
        app.start();
    }

    public void start(){
        DAO dao = new DAO();
        List<Gemstone> gemstones = dao.findAllGemstones();
        for(Gemstone gemstone : gemstones){
            System.out.println(gemstone);
        }

        Gemstone foundById = dao.findGemstoneById(1);
        System.out.println(foundById);
    }
}
