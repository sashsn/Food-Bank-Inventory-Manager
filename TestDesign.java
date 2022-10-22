package edu.ucalgary.ensf409;

import java.util.*;
import org.junit.*;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.HashMap;
import java.util.ArrayList;

public class TestDesign{



    //Tests for SINGLECLIENT.JAVA
	
    /**
     * testFVGetter is a test for the fruits and veggies getter function.
     */
    @Test
    public void testFVGetter() {
        SingleClient testData = new SingleClient(2);
        int idReturned = (int) testData.getFruitVeggies(NutritionType.FRUITVEGGIES.asString());
        int idExpected = 28;
        assertEquals("Wrong fruit and veggies nutritional info returned:", idExpected, idReturned);
    }

    /**
     * testProteinGetter is a test for the proteins getter function.
     */
    @Test
    public void testProteinGetter() {
        SingleClient testData = new SingleClient(2);
        int idReturned = (int) testData.getProtein(NutritionType.PROTEIN.asString());
        int idExpected = 26;
        assertEquals("Wrong protein nutritional info returned:", idExpected, idReturned);
    }

    /**
     * testWGGetter is a test for the whole wheats getter function.
     */
    @Test
    public void testWGGetter() {
        SingleClient testData = new SingleClient(2);
        int idReturned = (int) testData.getWholeGrains(NutritionType.WHOLEGRAINS.asString());
        int idExpected = 16;
        assertEquals("Wrong whole grain nutritional info returned:", idExpected, idReturned);
    }

    /**
     * testCaloriesGetter is a test for the calories getter function.
     */
    @Test
    public void testCaloriesGetter() {
        SingleClient testData = new SingleClient(2);
        int idReturned = (int) testData.getCalories(NutritionType.CALORIES.asString());
        int idExpected = 2000;
        assertEquals("Wrong calories nutritional info returned:", idExpected, idReturned);
    }

    /**
     * testOtherGetter is a test for the other getter function.
     */
    @Test
    public void testOtherGetter() {

        SingleClient testData = new SingleClient(2);
        int idReturned = (int) testData.getOther(NutritionType.OTHER.asString());
        int idExpected = 30;
        assertEquals("Wrong other nutritional info returned:", idExpected, idReturned);
    }

    /**
     * testClientIDGetter is a test for the client ID getter function.
     */
    @Test
    public void testClientIdGetter() {
        SingleClient testData = new SingleClient(2);
        int idReturned = testData.getClientID();
        int idExpected = 2;
        assertEquals("Wrong client ID returned: ", idExpected, idReturned);
    }

    /**
     * testConstructorSingleClient is a test for the constructor of SINGLECLIENT class with valid data
     */
	@Test
    public void testConstructorSingleClient() {
        SingleClient testData = new SingleClient(3);
		
        assertNotNull("Constructor for SingleClient not created when provided with valid data: ", testData);
    }

    /**
     * testConstructorWrongInput1 is a test for the constructor of SINGLECLIENT class with invalid data
     */
    @Test
    public void testConstructorWrongInput1() {
        boolean wrong = false;

        try{
            SingleClient testData = new SingleClient(10);
        }
        catch(IllegalArgumentException e){
            wrong = true;
        }

        assertTrue("Exception not thrown for invalid ID (ID number greater than 4): ", wrong);
    }

    /**
     * testConstructorWrongInput2 is a test for the constructor of SINGLECLIENT class with invalid data
     */
    @Test
    public void testConstructorWrongInput2() {
        boolean wrong = false;

        try{
            SingleClient person = new SingleClient(-7);
        }
        catch(IllegalArgumentException e){
            wrong = true;
        }

        assertTrue("Exception not thrown for invalid ID (Negative number): ", wrong);
    }
	
	


    //Tests for ORDER.JAVA

    /**
     * testOrderConstructorWrongData is a test for the constructor of ORDER class with invalid data
     */
    @Test
    public void testOrderConstructorWrongData() {
        boolean wrong = false;
        try{
            Order testOrder = new Order(-1, -2, -3, -4);
        }
        catch(IllegalArgumentException e){
            wrong = true;
        }

        assertTrue("Exception wasn't thrown by constructor of Order for invalid data: ", wrong);
    }
	
	/**
     * testOrderConstructorCorrectData is a test for the constructor of ORDER class with valid data
     */
    @Test
	public void testOrderConstructorCorrectData() {
		
        Order testOrder = new Order(1,2,3,4);
        assertNotNull("Object wasn't created by constructor of Order for valid data: ", testOrder);
    }
	
    /**
     * testFamiliesGetter is a test for the getter getFamilies
     */
    @Test
    public void testFamiliesGetter() {
		
		Order testOrder = new Order(1,2,3,4);
		ClientHamper[] fam = Order.getFamilies();
		
		assertNotNull("Object of ClientHamper wasn't returned by getFamilies: " , fam);
    }
	
    /**
     * testTotalHampersGetter is a test for the getter getTotalHampers
     */
	@Test
	public void testTotalHampersGetter() {
		
		Order testOrder = new Order(1,2,3,4);
		List<HashMap<String, Integer>> hmap = new ArrayList<HashMap<String, Integer>>();
		
		
		hmap = Order.getTotalHampers();
		
		assertNotNull("Object of Hashmap wasn't returned by getTotalHamper: " ,hmap);
		
		
	}
    



    //Tests for CLIENTHAMPER.JAVA
	
    ArrayList<Map<String, String>> testHamper = new ArrayList<Map<String, String>>();
	
	Map<String, String> firstHamper= new HashMap<String, String>();
	Map<String, String> secondHamper= new HashMap<String, String>();
	Map<String, String> thirdHamper= new HashMap<String, String>();
	
	ArrayList<String> hamperList = new ArrayList<String>();
	
    /**
     * testFamilyConstructorInvalidInput is a test for the constructor of CLIENTHAMPER class with invalid data
     */
	@Test
	public void testFamilyConstructorInvalidInput() throws ExistingHamperException, ItemShortageException{
		
		firstHamper.put("One", "Two");
		firstHamper.put("One", "Two");
		firstHamper.put("One", "Two");
		firstHamper.put("One", "Two");
		secondHamper.put("One", "Two");
		secondHamper.put("One", "Two");
		secondHamper.put("One", "Two");
		thirdHamper.put("One", "Two");
		thirdHamper.put("One", "Two");
		
		testHamper.add(firstHamper);
		testHamper.add(secondHamper);
		testHamper.add(thirdHamper);
		
		for (Map<String, String> foodItem : testHamper) {
            hamperList.add(foodItem.get("One"));
        }
		
		boolean correctException = false;

        try{
			ClientHamper family = new ClientHamper(-1,2,2,2, hamperList);
        }
        catch(IllegalArgumentException e){
            correctException = true;
        }
		
		assertTrue("SingleClient constructor did not throw an IllegalArgumentException when given a count less than 0: ", correctException);
    }
	
    /**
     * testFamilyConstructorValidInput is a test for the constructor of CLIENTHAMPER class with valid data
     */
	@Test
	public void testFamilyConstructorValidInput() throws ExistingHamperException, ItemShortageException{
		
		firstHamper.put("One", "Two");
		firstHamper.put("One", "Two");
		firstHamper.put("One", "Two");
		firstHamper.put("One", "Two");
		secondHamper.put("One", "Two");
		secondHamper.put("One", "Two");
		secondHamper.put("One", "Two");
		thirdHamper.put("One", "Two");
		thirdHamper.put("One", "Two");
		
		testHamper.add(firstHamper);
		testHamper.add(secondHamper);
		testHamper.add(thirdHamper);
		
		for (Map<String, String> foodItem : testHamper) {
            hamperList.add(foodItem.get("One"));
        }
		
        ClientHamper family = new ClientHamper(2,2,2,2, hamperList);
        assertNotNull("ClientHamper constructor did not create an object when given a valid count:", family);
    }
	
	/**
     * testFamilyGetPeople is a test for the getter getPeople
     */
	@Test
    public void testFamilyGetPeople() throws ExistingHamperException, ItemShortageException{
		
		firstHamper.put("One", "Two");
		firstHamper.put("One", "Two");
		firstHamper.put("One", "Two");
		firstHamper.put("One", "Two");
		secondHamper.put("One", "Two");
		secondHamper.put("One", "Two");
		secondHamper.put("One", "Two");
		thirdHamper.put("One", "Two");
		thirdHamper.put("One", "Two");
		
		testHamper.add(firstHamper);
		testHamper.add(secondHamper);
		testHamper.add(thirdHamper);
		
		for (Map<String, String> foodItem : testHamper) {
            hamperList.add(foodItem.get("One"));
        }
		
		ClientHamper nfamily = new ClientHamper(2,2,2,2, hamperList);
		SingleClient[] people = nfamily.getPeople();
		
		assertNotNull("Method getPeople did not return an object of type SingleClient: " , people);
    }
	
    /**
     * testFamilyGetHamper is a test for the getter getHamper
     */
	@Test
	public void testFamilyGetHamper() throws ExistingHamperException, ItemShortageException{
		
		firstHamper.put("One", "Two");
		firstHamper.put("One", "Two");
		firstHamper.put("One", "Two");
		firstHamper.put("One", "Two");
		secondHamper.put("One", "Two");
		secondHamper.put("One", "Two");
		secondHamper.put("One", "Two");
		thirdHamper.put("One", "Two");
		thirdHamper.put("One", "Two");
		
		testHamper.add(firstHamper);
		testHamper.add(secondHamper);
		testHamper.add(thirdHamper);
		
		for (Map<String, String> foodItem : testHamper) {
            hamperList.add(foodItem.get("One"));
        }
		
		ClientHamper nfamily = new ClientHamper(2,2,2,2, hamperList);
		
		
		ArrayList<Map<String, String>> hmap;
		
		hmap = nfamily.getHamper();
		
		assertEquals("Method getHamper did not return the expected result: " ,testHamper, hmap);
		
		
	}



}