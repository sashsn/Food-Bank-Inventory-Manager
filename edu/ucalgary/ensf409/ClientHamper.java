package edu.ucalgary.ensf409;

import java.util.*;

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
 * The ClientHamper class is used to store a group of SingleClient objects and to create the best hamper for the objects
 */

public class ClientHamper{
    private final SingleClient[] PEOPLE;
	private final int ADULTMALE;
	private final int ADULTFEMALE;
	private final int CHILDO8;
	private final int CHILDU8;
    private ArrayList<String> currUsedItemID;
    private ArrayList<Map<String, String>> bestHamper;

    /**
     * the constructor takes in the count of each type of client
     * 
     * @param maleCount
     * @param femaleCount
     * @param childUECount
     * @param childOECount
     * @param currUsedItemID
     */
	public ClientHamper(int adultMCount, int adultFCount, int childO8Count, int childU8Count, ArrayList<String> currUsedItemID) {
        if (!validateInput(adultMCount, adultFCount, childO8Count, childU8Count)) throw new IllegalArgumentException();
        this.ADULTMALE = adultMCount;
        this.ADULTFEMALE = adultFCount;
        this.CHILDO8 = childO8Count;
        this.CHILDU8 = childU8Count;
        this.currUsedItemID = currUsedItemID;

        int j = 0;
        PEOPLE = new SingleClient[adultMCount + adultFCount + childO8Count + childU8Count];
		for (int i = 0; i < ADULTMALE; i++) {
            PEOPLE[j] = new SingleClient(ClientTypes.MALE.clientID());
            j++;
        }
        for (int i = 0; i < ADULTFEMALE; i++) {
            PEOPLE[j] = new SingleClient(ClientTypes.FEMALE.clientID());
            j++;
        }
        for (int i = 0; i < CHILDO8; i++) {
            PEOPLE[j] = new SingleClient(ClientTypes.CHILDO8.clientID());
            j++;
        }
        for (int i = 0; i < CHILDU8; i++) {
            PEOPLE[j] = new SingleClient(ClientTypes.CHILDU8.clientID());
            j++;
        }
	}

    /**
     * findBestHamper uses an algorithm object to find the best hamper and store it
     * 
     * @throws ItemShortageException
     * @throws ExistingHamperException
     */
    public void findBestHamper() throws ItemShortageException, ExistingHamperException {
        Algorithm algoObj = new Algorithm(PEOPLE, currUsedItemID);
        this.bestHamper = algoObj.getBestHamper();

        for (Map<String, String> foodItem : bestHamper) {
            currUsedItemID.add(foodItem.get("ItemID"));
        }
    }

    /**
     * getPeople returns the SingleClient array
     * @return
     */
	public SingleClient[] getPeople(){
		return this.PEOPLE;
	}

    /**
     * getHamper returns the bestHamper
     * @return
     */
	public ArrayList<Map<String, String>> getHamper(){
		return this.bestHamper;
	}

    /**
     * getClientCount takes in type of client and returns the number of that client in the family
     * 
     * @param type
     * @return
     */
    public int getClientCount(String type) {
        if (type.equals(ClientTypes.MALE.asString())) return this.ADULTMALE;
        else if (type.equals(ClientTypes.FEMALE.asString())) return this.ADULTFEMALE;
        else if (type.equals(ClientTypes.CHILDO8.asString())) return this.CHILDO8;
        else if (type.equals(ClientTypes.CHILDU8.asString())) return this.CHILDU8;
        else throw new IllegalArgumentException("Error: Invalid input!");
    }

    /**
     * getCurrtUsedItemIDs returns the IDs of items currently used
     * 
     * @return
     */
    public ArrayList<String> getCurrtUsedItemIDs() {
        return this.currUsedItemID;
    }

    /**
     * validateInput returns true if all client counts>=0 (the client order isnt null)
     * @param male
     * @param female
     * @param childOE
     * @param childUE
     * @return
     */
    private boolean validateInput(int male, int female, int childO8, int childU8) {
        return male >= 0 && female >= 0 && childO8 >= 0 && childU8 >= 0;
    }
}