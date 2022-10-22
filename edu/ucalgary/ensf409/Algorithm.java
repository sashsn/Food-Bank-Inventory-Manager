package edu.ucalgary.ensf409;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * GROUP 49
 * @author Radia Jannat <a href = "mailto: @ucalgary.ca">@ucalgary.ca</a>,
 *         Rumaisa Shoeb Talukder <a href = "mailto: rumaisashoeb.talukde@ucalgary.ca">rumaisashoeb.talukde@ucalgary.ca</a>, 
 *         Sadman Shahriar Snigdho <a href = "mailto: sadmanshahriar.snigd@ucalgary.ca">sadmanshahriar.snigd@ucalgary.ca</a>,
 *         Shreosi Debnath <a href = "mailto: @ucalgary.ca">@ucalgary.ca</a>
 * @version 1.7
 * @since 1.0
 */

/*
 * Algorithm class is used to find the best hamper possible given a database of food items.
 * 
 */

class Algorithm {
    private double totalNutrients;
    private double wholeGrainsCalories;
    private double fruitVeggiesCalories;
    private double proteinCalories;
    private double otherCalories;
    private ArrayList<String> usedItemID;
    private ArrayList<Map<String, String>> bestHamper = new ArrayList<>();

    /**
     * the constructor takes array of client objects to calculate the total amount of nutritional contents
     * @param people
     * @param usedItemID
     * @throws ItemShortageException
     */
    public Algorithm(SingleClient[] people, ArrayList<String> usedItemID) throws ItemShortageException, ExistingHamperException {
        if (people.length == 0) throw new IllegalArgumentException();

        this.usedItemID = usedItemID;
        for (SingleClient client : people) {
            double calories = client.getCalories(NutritionType.CALORIES.asString());
            totalNutrients += calories;
            wholeGrainsCalories += 0.01 * client.getWholeGrains(NutritionType.WHOLEGRAINS.asString()) * calories*7;
            fruitVeggiesCalories += 0.01 * client.getFruitVeggies(NutritionType.FRUITVEGGIES.asString()) * calories*7;
            proteinCalories += 0.01 * client.getProtein(NutritionType.PROTEIN.asString()) * calories*7;
            otherCalories += 0.01 * client.getOther(NutritionType.OTHER.asString()) * calories*7;
        }

        Inventory inv = new Inventory("jdbc:mysql://localhost:3306/food_inventory", "user1", "ensf");
        inv.initializeConnection();
        findBestHamper(inv);
        inv.close();
    }

    /**
     * getFamilyNutritionCalories is a getter for multiple nutrient variables, returning the nutrient content of that particular variable
     * @param type
     * @return
     */
    public double getFamilyCalories(String type) {
        if (Objects.equals(type, NutritionType.CALORIES.asString())) return this.totalNutrients;
        else if (Objects.equals(type, NutritionType.WHOLEGRAINS.asString())) return this.wholeGrainsCalories;
        else if (Objects.equals(type, NutritionType.FRUITVEGGIES.asString())) return this.fruitVeggiesCalories;
        else if (Objects.equals(type, NutritionType.PROTEIN.asString())) return this.proteinCalories;
        else if (Objects.equals(type, NutritionType.OTHER.asString())) return this.otherCalories;
        else throw new IllegalArgumentException("Error: Invalid input!");
    }

    /**
     * getBestHamper returns the best hamper
     * @return
     */
    public ArrayList<Map<String, String>> getBestHamper() {
        return this.bestHamper;
    }

    /**
     * getusedItemID returns the ID of used items
     * @return
     */
    public ArrayList<String> getUsedItemID() {
        return usedItemID;
    }

    /**
     * findBestHamper generates the best hamper possible.
     * it also checks if there is sufficient stock
     * if a hamper is possible, it deletes the used items from the database
     * 
     * @param inv
     * @throws ItemShortageException
     */
    private void findBestHamper(Inventory inv) throws ItemShortageException, ExistingHamperException {
        if (this.bestHamper.isEmpty()) {
            ArrayList<Map<String, String>> combination = new ArrayList<>();

            for (int combinationLength = 1; combinationLength <= inv.getItemLength() - usedItemID.size(); combinationLength++) {
                findFoodCombo(inv, combination, 0, inv.getItemLength() - usedItemID.size() - 1, 0, combinationLength);
            }

            if (this.bestHamper.isEmpty())
                throw new ItemShortageException("Error: Insufficient stock!");
        } else {
            throw new ExistingHamperException("Error: Best hamper already exists!");
        }
    }

    /**
     * checkExistingCombo checks if the surplus of a specific combination is less than that of the existing best hamper
     * if specific combination has less surplus, that is made the best hamper
     * 
     * @param combination
     */
    private void checkExistingCombo(ArrayList<Map<String, String>> combination) {
        if (this.bestHamper.isEmpty()) this.bestHamper = new ArrayList<>(combination);
        else {
            Map<String, String> combineBestHamper = combineHamperNutrition(this.bestHamper);
            Map<String, String> combinedCombination = combineHamperNutrition(combination);

            double bestHamperDiff = Double.parseDouble(combineBestHamper.get(NutritionType.CALORIES.asString())) - this.totalNutrients;
            double combinationDiff = Double.parseDouble(combinedCombination.get(NutritionType.CALORIES.asString())) - this.totalNutrients;
            if (combinationDiff < bestHamperDiff) {
                this.bestHamper = new ArrayList<>(combination);
            }
        }
    }

    /**
     * checkValidCombo method checks if the nutritional values are greater or equal to the nutritional values of the inputted family
     * 
     * @param combination
     * @return
     */
    private boolean checkValidCombo(ArrayList<Map<String, String>> combination) {
        Map<String, String> combinedHamper = combineHamperNutrition(combination);

        boolean validWG = Double.parseDouble(combinedHamper.get(NutritionType.WHOLEGRAINS.asString())) >= this.wholeGrainsCalories;
        boolean validFV = Double.parseDouble(combinedHamper.get(NutritionType.FRUITVEGGIES.asString())) >= this.fruitVeggiesCalories;
        boolean validPro = Double.parseDouble(combinedHamper.get(NutritionType.PROTEIN.asString())) >= this.proteinCalories;
        boolean validOth = Double.parseDouble(combinedHamper.get(NutritionType.OTHER.asString())) >= this.otherCalories;

        return validWG && validFV && validPro && validOth;
    }

    /**
     * findFoodCombo method generates all possible combinations of the food items
     * @param inv
     * @param combination
     * @param start
     * @param end
     * @param ind
     * @param comboLength
     */
    private void findFoodCombo(Inventory inv, ArrayList<Map<String, String>> combination, int start, int end, int ind, int comboLength) {

        if (ind == comboLength) {
            if (checkValidCombo(combination)) 
            checkExistingCombo(combination);
            return;
        }

        ArrayList<String> itemIDs = inv.getItemIDs();
        itemIDs.removeAll(usedItemID);

        try {
            combination.set(ind, inv.selectAvailableFood(itemIDs.get(start)));
        } catch (IndexOutOfBoundsException e) {
            combination.add(inv.selectAvailableFood(itemIDs.get(start)));
        } catch (IllegalArgumentException ignore) {
        }
        findFoodCombo(inv, combination, start + 1, end, ind + 1, comboLength);
    }

    /**
     * percentToCal method calculates the calories of a given nutrition
     * @param foodItem
     * @param nutrition
     * @return
     */
    private double percentToCal(Map<String, String> foodItem, String nutrition) {
        return 0.01 * Integer.parseInt(foodItem.get(nutrition)) * Integer.parseInt(foodItem.get(NutritionType.CALORIES.asString()));
    }

    /**
     * combineHamperNutrition method combines an ArrayList of foodItems into one HashMap
     * @param combination
     * @return
     */
    private Map<String, String> combineHamperNutrition(ArrayList<Map<String, String>> combination) {
        Map<String, String> combineHamper = new HashMap<>();

        double calories = 0;
        double grainContent = 0;
        double fvContent = 0;
        double proContent = 0;
        double other = 0;

        for (Map<String, String> foodItem : combination) {
            calories += Double.parseDouble(foodItem.get(NutritionType.CALORIES.asString()));
            grainContent += percentToCal(foodItem, NutritionType.WHOLEGRAINS.asString());
            proContent += percentToCal(foodItem, NutritionType.PROTEIN.asString());
            fvContent += percentToCal(foodItem, NutritionType.FRUITVEGGIES.asString());
            other += percentToCal(foodItem, NutritionType.OTHER.asString());
        }

        combineHamper.put(NutritionType.CALORIES.asString(), String.valueOf(calories));
        combineHamper.put(NutritionType.WHOLEGRAINS.asString(), String.valueOf(grainContent));
        combineHamper.put(NutritionType.PROTEIN.asString(), String.valueOf(proContent));
        combineHamper.put(NutritionType.FRUITVEGGIES.asString(), String.valueOf(fvContent));
        combineHamper.put(NutritionType.OTHER.asString(), String.valueOf(other));

        return combineHamper;
    }
}