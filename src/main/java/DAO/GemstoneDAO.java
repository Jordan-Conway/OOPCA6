package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import Classes.Gemstone;
import Enums.Clarity;

public class GemstoneDAO extends DAOBase implements DAOInterface {

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

    /**
     *
     * @param id the id of the gemstone to be found
     * @return a Gemstone Object representing the first entry in the database with the matching id or null if no entries are found
     */
    @Override
    public Gemstone findGemstoneById(int id) {
        Gemstone stone = null;
        try(Connection connection = this.getConnection()){
            String query = "SELECT * FROM GEMSTONES WHERE id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            if(resultSet.isBeforeFirst()){ //If results are found
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
    public boolean deleteGemstoneById(int id) {
        boolean success = false;
        try(Connection connection = this.getConnection()){
            String query = "DELETE FROM gemstones WHERE id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, id);
            if(preparedStatement.executeUpdate() > 0){
                success = true;
            }
        }
        catch(SQLException e){
            System.out.println("SQL exception when deleting");
        }
        return success;
    }

    @Override
    public boolean insertGemstone(Gemstone gemstone) {
        boolean success = false;
        try(Connection connection = this.getConnection()){
            String query = "INSERT INTO gemstones VALUES (null, ?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(2, gemstone.getGemName());
            preparedStatement.setDouble(3, gemstone.getCarats());
            preparedStatement.setString(4, gemstone.getColour());
            preparedStatement.setString(5, gemstone.getClarity().toString());

            if(preparedStatement.executeUpdate() > 0){
                success = true;
            }
        }
        catch(SQLException e){
            System.out.println("SQL exception when inserting");
        }

        return success;
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
