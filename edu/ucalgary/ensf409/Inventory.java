package edu.ucalgary.ensf409;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * GROUP 49
 * @author Radia Jannat <a href = "mailto: @ucalgary.ca">@ucalgary.ca</a>,
 *         Rumaisa Shoeb Talukder <a href = "mailto: rumaisashoeb.talukde@ucalgary.ca">rumaisashoeb.talukde@ucalgary.ca</a>, 
 *         Sadman Shahriar Snigdho <a href = "mailto: sadmanshahriar.snigd@ucalgary.ca">sadmanshahriar.snigd@ucalgary.ca</a>,
 *         Shreosi Debnath <a href = "mailto: @ucalgary.ca">@ucalgary.ca</a>
 * @version 1.6
 * @since 1.0
 */

/*
 * The Inventory class accesses the SQL database to import the data for clients and food items
 * It also deletes used items from the stock
 *
 * The class takes in the database url, username and password
 * The intended URL to be used is: jdbc:mysql://localhost:3306/food_inventory
 * The intended username to be used is: user1
 * The intended password to be used is: ensf409
 */

public class Inventory {

    private final String DBURL;
    private final String USERNAME;
    private final String PASSWORD;
    private int itemLength;

    private Connection dbConnect;
    private ResultSet results;

    public Inventory(String url, String user, String pw) {
        this.DBURL = url;
        this.USERNAME = user;
        this.PASSWORD = pw;
    }

    // initializeConnection initializes a connection with the database
    public void initializeConnection() {
        try {
            dbConnect = DriverManager.getConnection(this.DBURL, this.USERNAME, this.PASSWORD);
            updateItemLength();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    String getDburl() {
        return this.DBURL;
    }

    String getUsername() {
        return this.USERNAME;
    }

    String getPassword() {
        return this.PASSWORD;
    }

    /**
     * selectClientNeeds takes a client ID as an argument and returns a map of the data for the client with the given ID
     * @param string
     * @return
     */
    public Map<String, String> selectClientNeeds(String string) {
        Map<String, String> map = new HashMap<String, String>();

        try {
            Statement myStmt = dbConnect.createStatement();

            String query = String.format("SELECT * FROM DAILY_CLIENT_NEEDS WHERE (ClientID = %s)", string);
            results = myStmt.executeQuery(query);

            if (!results.next()) throw new IllegalArgumentException(string + " is an invalid client ID");
            else {
                String calories = results.getString(NutritionType.CALORIES.asString());
                String wholeGrains = results.getString(NutritionType.WHOLEGRAINS.asString());
                String protein = results.getString(NutritionType.PROTEIN.asString());
                String fruitsVeggies = results.getString(NutritionType.FRUITVEGGIES.asString());
                String other = results.getString(NutritionType.OTHER.asString());

                map.put(NutritionType.CALORIES.asString(), calories);
                map.put(NutritionType.WHOLEGRAINS.asString(), wholeGrains);
                map.put(NutritionType.PROTEIN.asString(), protein);
                map.put(NutritionType.FRUITVEGGIES.asString(), fruitsVeggies);
                map.put(NutritionType.OTHER.asString(), other);
                map.put("ClientID", string);
            }

            myStmt.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return map;
    }

     /**
      * selectAvailableFood method takes a food ID as an argument and returns a map of the data for the food item with the given ID
      * @param foodID
      * @return
      */
    public Map<String, String> selectAvailableFood(String foodID) {
        Map<String, String> map = new HashMap<String, String>();

        try {
            Statement myStmt = dbConnect.createStatement();

            String query = String.format("SELECT * FROM AVAILABLE_FOOD WHERE (ItemID = %s)", foodID);
            results = myStmt.executeQuery(query);

            if (!results.next()) throw new IllegalArgumentException(foodID + " is an invalid food ID");
            else {
                String grainContent = results.getString("GrainContent");
                String fvContent = results.getString("FVContent");
                String proContent = results.getString("ProContent");
                String other = results.getString("Other");
                String calories = results.getString("Calories");
                String name = results.getString("Name");

                map.put(NutritionType.WHOLEGRAINS.asString(), grainContent);
                map.put(NutritionType.FRUITVEGGIES.asString(), fvContent);
                map.put(NutritionType.PROTEIN.asString(), proContent);
                map.put(NutritionType.OTHER.asString(), other);
                map.put(NutritionType.CALORIES.asString(), calories);
                map.put("ItemID", foodID);
                map.put("Name", name);
            }
            myStmt.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return map;
    }

    // updateItemLength updates the length after checking the number of items currently left in the stock
    private void updateItemLength() {
        int length = 0;
        try {
            String query = "SELECT * FROM AVAILABLE_FOOD";

            Statement myStmt = dbConnect.createStatement();
            results = myStmt.executeQuery(query);

            while (results.next()) {
                length++;
            }

            myStmt.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        this.itemLength = length;
    }

    /**
     * getItemLength returns itemLength
     * @return
     */
    public int getItemLength() {
        return this.itemLength;
    }

    /**
     * deleteFoodItem deletes the food item of a given ID from the database
     * @param foodID
     */
    public void deleteFoodItem(String foodID) {
        try {
            String query = "DELETE FROM AVAILABLE_FOOD WHERE ItemID = ?";

            PreparedStatement preparedStmt = dbConnect.prepareStatement(query);
            preparedStmt.setString(1, foodID);
            preparedStmt.execute();

            preparedStmt.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * getItemIDs returns an ArrayList of all food item IDs found in the SQL database
     * @return
     */
    public ArrayList<String> getItemIDs() {
        ArrayList<String> itemIDs = new ArrayList<>();
        try {
            String query = "SELECT * FROM AVAILABLE_FOOD";

            Statement myStmt = dbConnect.createStatement();
            results = myStmt.executeQuery(query);

            while (results.next()) {
                itemIDs.add(results.getString("ItemID"));
            }

            myStmt.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return itemIDs;
    }

    // close method closes the SQL database connection
    public void close() {
        try {
            results.close();
            dbConnect.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
