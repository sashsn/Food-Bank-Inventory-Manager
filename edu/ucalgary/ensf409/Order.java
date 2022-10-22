package edu.ucalgary.ensf409;

import java.util.ArrayList;
import java.util.Map;

/**
 * GROUP 49
 * @author Radia Jannat <a href = "mailto: @ucalgary.ca">@ucalgary.ca</a>,
 *         Rumaisa Shoeb Talukder <a href = "mailto: rumaisashoeb.talukde@ucalgary.ca">rumaisashoeb.talukde@ucalgary.ca</a>, 
 *         Sadman Shahriar Snigdho <a href = "mailto: sadmanshahriar.snigd@ucalgary.ca">sadmanshahriar.snigd@ucalgary.ca</a>,
 *         Shreosi Debnath <a href = "mailto: @ucalgary.ca">@ucalgary.ca</a>
 * @version 1.5
 * @since 1.0
 */

/*
 * The order class creates a family class for each hamper and stores them into a family array.
 * The order class is also responsible for deleting all the items after each best hamper is found
 */

public class Order {

    private ClientHamper[] familyHamper;
    private ArrayList<String> usedItemID = new ArrayList<>();
    private boolean finishedOrder = false;

    /**
     * @param orderList
     * @throws ItemShortageException
     * @throws ExistingHamperException
     */
    public Order(ArrayList<Map<String, String>> orderList) throws ItemShortageException, ExistingHamperException {
        if (!validateOrderList(orderList)) throw new IllegalArgumentException();  // throws an error if orderList is not valid

        familyHamper = new ClientHamper[orderList.size()];
        try {
            for (int i = 0; i < orderList.size(); i++) {
                int adultMale = Integer.parseInt(orderList.get(i).get("Male"));
                int adultFemale = Integer.parseInt(orderList.get(i).get("Female"));
                int childO8 = Integer.parseInt(orderList.get(i).get("ChildO8"));
                int childU8 = Integer.parseInt(orderList.get(i).get("ChildU8"));
                familyHamper[i] = new ClientHamper(adultMale, adultFemale, childO8, childU8, usedItemID);
                familyHamper[i].findBestHamper();
            }
            deleteOrderFromDatabase();
            finishedOrder = true;
        } catch (ItemShortageException e) {
            usedItemID = new ArrayList<>();
        }
    }

    /**
     * getFamilyHamper method returns the familyHamper array
     * @return
     */
    public ClientHamper[] getFamilyHamper() {
        return familyHamper;
    }

    /**
     * getOrderCompleted method returns the boolean orderCompleted
     * @return
     */
    public boolean getOrderCompleted() {
        return finishedOrder;
    }

    /**
     * deletes the items of each best hamper that have been previously found
     */
    private void deleteOrderFromDatabase() {
        Inventory db = new Inventory("jdbc:mysql://localhost:3306/food_inventory", "user1", "ensf");
        db.initializeConnection();
        for (String itemID : usedItemID) {
            db.deleteFoodItem(itemID);
        }
        db.close();
    }

    /**
     * validateOrderList checks the validity of the orderList
     * @param orderList
     * @return
     */
    private boolean validateOrderList(ArrayList<Map<String, String>> orderList) {
        if (orderList.isEmpty()) return false;
        else {
            for (Map<String, String> clients : orderList) {
                if (!clients.containsKey("Male")) return false;
                if (!clients.containsKey("Female")) return false;
                if (!clients.containsKey("ChildO8")) return false;
                if (!clients.containsKey("ChildU8")) return false;
                if (Integer.parseInt(clients.get("Male")) < 0) return false;
                if (Integer.parseInt(clients.get("Female")) < 0) return false;
                if (Integer.parseInt(clients.get("ChildU8")) < 0) return false;
                if (Integer.parseInt(clients.get("ChildO8")) < 0) return false;
            }
        }
        return true;
    }
}