package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import Classes.Gemstone;
import Enums.Clarity;

public class DAO extends DAOBase implements DAOInterface {

    @Override
    public List<Gemstone> findAllGemstones(){
        ArrayList<Gemstone> allGemstones = new ArrayList<>();
        try(Connection connection = this.getConnection()){
            String query = "SELECT * FROM gemstones";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet =preparedStatement.executeQuery();

            allGemstones = processResults(resultSet);
            closeConnection(connection);
        }
        catch(SQLException e){
            System.out.println("SQLException when preparing statement");
            e.printStackTrace();
        }
        return allGemstones;
    }

    @Override
    public Gemstone findGemstoneById(int id) {
        Gemstone stone = null;
        try(Connection connection = this.getConnection()){
            String query = "SELECT * FROM GEMSTONES WHERE id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                stone = processResults(resultSet).get(0);
            }
            closeConnection(connection);
        }
        catch(SQLException e){
            System.out.println("SQL exception when getting by id");
        }

        return stone;
    }

    @Override
    public void deleteGemstoneById(int id) {
        try(Connection connection = this.getConnection()){
            String query = "DELETE FROM gemstones WHERE id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, id);
            preparedStatement.execute();
        }
        catch(SQLException e){
            System.out.println("SQL exception when deleting");
        }
    }

    @Override
    public void insertGemstone(Gemstone gemstone) {

    }

    private ArrayList<Gemstone> processResults(ResultSet resultSet){
        ArrayList<Gemstone> toReturn = new ArrayList<>();
        try{
            while(resultSet.next())
            {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("gemName");
                double carats = resultSet.getDouble("carats");
                String colour = resultSet.getString("colour");
                Clarity clarity = Clarity.valueOf(resultSet.getString("clarity"));
                Gemstone newGem = new Gemstone(id, name, carats, colour, clarity);
                toReturn.add(newGem);
            }
        }
        catch(SQLException e){
            System.out.println("SQL Exception while processing results");
        }

        return toReturn;
    }
}
