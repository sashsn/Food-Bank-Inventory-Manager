package edu.ucalgary.ensf409;

import java.util.Map;
import java.util.Objects;

/**
 * GROUP 49
 * @author Radia Jannat <a href = "mailto: @ucalgary.ca">@ucalgary.ca</a>,
 *         Rumaisa Shoeb Talukder <a href = "mailto: rumaisashoeb.talukde@ucalgary.ca">rumaisashoeb.talukde@ucalgary.ca</a>, 
 *         Sadman Shahriar Snigdho <a href = "mailto: sadmanshahriar.snigd@ucalgary.ca">sadmanshahriar.snigd@ucalgary.ca</a>,
 *         Shreosi Debnath <a href = "mailto: @ucalgary.ca">@ucalgary.ca</a>
 * @version 1.4
 * @since 1.0
 */

/*
 * SingleClient class saves the client data of a specific client ID.
 */

public class SingleClient {
    private final int CLIENT_ID;
    private final int CALORIES;
    private final int WHOLEGRAINS;
    private final int FRUITVEGGIES;
    private final int PROTEIN;
    private final int OTHER;

    /**
     * @param clientID
     */
    SingleClient(int clientID) {
        if (!validateClientID(clientID)) throw new IllegalArgumentException();
        Inventory inv = new Inventory("jdbc:mysql://localhost:3306/food_inventory", "user1", "ensf");
        inv.initializeConnection();
        Map<String, String> clientNeeds = inv.selectClientNeeds(String.valueOf(clientID));
        inv.close();

        this.CLIENT_ID = clientID;
        this.CALORIES = Integer.parseInt(clientNeeds.get("Calories"));
        this.WHOLEGRAINS = Integer.parseInt(clientNeeds.get("WholeGrains"));
        this.FRUITVEGGIES = Integer.parseInt(clientNeeds.get("FruitVeggies"));
        this.PROTEIN = Integer.parseInt(clientNeeds.get("Protein"));
        this.OTHER = Integer.parseInt(clientNeeds.get("Other"));
    }

    // the client data is accessed via the following getters

    /**
     * returns the calories content
     * @param type
     * @return
     */
    public double getCalories(String type) {
        if (Objects.equals(type, NutritionType.CALORIES.asString()));
        return this.CALORIES;
    }

    /**
     * returns the wholegrains content
     * @param type
     * @return
     */
    public double getWholeGrains(String type) {
        if (Objects.equals(type, NutritionType.WHOLEGRAINS.asString()));
        return this.WHOLEGRAINS;
    }

    /**
     * returns the fruitveggies content
     * @param type
     * @return
     */
    public double getFruitVeggies(String type) {
        if (Objects.equals(type, NutritionType.FRUITVEGGIES.asString()));
        return this.FRUITVEGGIES;
    }

    /**
     * returns the protein content
     * @param type
     * @return
     */
    public double getProtein(String type) {
        if (Objects.equals(type, NutritionType.PROTEIN.asString()));
        return this.PROTEIN;
    }

    /**
     * returns the other content
     * @param type
     * @return
     */
    public double getOther(String type) {
        if (Objects.equals(type, NutritionType.OTHER.asString()));
        return this.OTHER;
    }

    /**
     * validateClientID checks if the client ID exists within the ClientTypes enumeration
     * @param clientID
     * @return
     */
    private boolean validateClientID(int clientID) {
        if (clientID == ClientTypes.MALE.clientID()) return true;
        else if (clientID == ClientTypes.FEMALE.clientID()) return true;
        else if (clientID == ClientTypes.CHILDO8.clientID()) return true;
        else if (clientID == ClientTypes.CHILDU8.clientID()) return true;
        else return false;
    }

    /**
     * getClientID returns the client ID
     * @return
     */
    public int getClientID() {
        return this.CLIENT_ID;
    }
}
