package edu.ucalgary.ensf409;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

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
 * The GUI class is used to take the client's input for their order and give them their order ID along with the best hamper(s)
 * It also contains the main method for our program
 */

public class GUI extends JFrame implements ActionListener, MouseListener {

    private int adultMale;
    private int adultFemale;
    private int childO8;
    private int childU8;
    
    private JLabel instructions;
    private JLabel aMLabel;
    private JLabel aFLabel;
    private JLabel cO8Label;
    private JLabel cU8Label;
    
    private JTextField aMInput;
    private JTextField aFInput;
    private JTextField cO8Input;
    private JTextField cU8Input;
    
    private ArrayList<Map<String, String>> orderSummary = new ArrayList<>();

    // constructor sets up the GUI box
    public GUI(){
        super("ORDER FORM");
        setupGUI();
        setSize(650,300);       // set size
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);     //terminate program when closed
    }

    // main method to run the GUI
    public static void main(String[] args) {
        
        EventQueue.invokeLater(() -> {
            new GUI().setVisible(true);     //to see the window  
        });
    }

    // setupGUI method sets up the GUI with the appropriate boxes and buttons.
    public void setupGUI(){
        
        instructions = new JLabel("Please enter client(s) information to generate an order.");
        aMLabel = new JLabel("Adult Males:");
        aFLabel = new JLabel("Adult Females:");
        cO8Label = new JLabel("Children over 8:");
        cU8Label = new JLabel("Children under 8:");
        
        aMInput = new JTextField("e.g. 2", 5);
        aFInput = new JTextField("e.g. 2", 5);
        cO8Input = new JTextField("e.g. 2", 5);
        cU8Input = new JTextField("e.g. 2", 5);    
        
        aMInput.addMouseListener(this);
        aFInput.addMouseListener(this);
        cO8Input.addMouseListener(this);
        cU8Input.addMouseListener(this);

        // The submitHamper button adds the client input into a hashmap and puts it in orderSummary
        JButton submitHamper = new JButton("Submit Hamper Order");
        submitHamper.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (validateInput()) {
                    Map<String, String> family = new HashMap<>();
                    family.put("Male", Integer.toString(getadultMale()));
                    family.put("Female", Integer.toString(getadultFemale()));
                    family.put("ChildO8", Integer.toString(getChildO8()));
                    family.put("ChildU8", Integer.toString(getChildU8()));
                    orderSummary.add(family);
                    JOptionPane.showMessageDialog(submitHamper, "Successfully submitted hamper!");
                }
            }
        });

        // The finishOrder button submits the complet order and creates a new Order object with the orderSummary as an argument
        JButton finishOrder = new JButton("Proceed to Checkout");
        finishOrder.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (!orderSummary.isEmpty()) {
                    try {
                        String orderID = generateOrderID();
                        JOptionPane.showMessageDialog(finishOrder, "Order Submitted Successfully.\nYour order ID is " + orderID);
                        finishOrder.setEnabled(false);
                        submitHamper.setEnabled(false);
                        Order order = new Order(orderSummary);

                        if (order.getOrderCompleted()) {
                            JOptionPane.showMessageDialog(finishOrder, "Succesfully created order form!");
                            createOrderForm(order, orderID);   // calling the method to create the order form
                            JOptionPane.showMessageDialog(finishOrder, createBestHamperString(order));
                        } else JOptionPane.showMessageDialog(finishOrder, "Error: Insufficient stock to complete order");

                    } catch (ExistingHamperException ex) {
                        JOptionPane.showMessageDialog(finishOrder, "Error: Hamper already exists");
                    } catch (ItemShortageException ignore) {} catch (IOException ex) {
                        ex.printStackTrace();
                    }
                } else JOptionPane.showMessageDialog(finishOrder, "Error: The order list is empty");

                finishOrder.setEnabled(true);
                submitHamper.setEnabled(true);
                orderSummary = new ArrayList<>();
            }
        });

        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new FlowLayout());
        
        JPanel clientPanel = new JPanel();
        clientPanel.setLayout(new FlowLayout());

        JPanel submitPanel = new JPanel();
        submitPanel.setLayout(new FlowLayout());

        headerPanel.add(instructions);
        clientPanel.add(aMLabel);
        clientPanel.add(aMInput);
        clientPanel.add(aFLabel);
        clientPanel.add(aFInput);
        clientPanel.add(cO8Label);
        clientPanel.add(cO8Input);
        clientPanel.add(cU8Label);
        clientPanel.add(cU8Input);
        submitPanel.add(submitHamper);
        submitPanel.add(finishOrder);

        this.add(headerPanel, BorderLayout.NORTH);
        this.add(clientPanel, BorderLayout.CENTER);
        this.add(submitPanel, BorderLayout.PAGE_END);
    }

    public void mouseClicked(MouseEvent event) {
        if(event.getSource().equals(aMInput))
            aMInput.setText("");

        if(event.getSource().equals(aFInput))
            aFInput.setText("");

        if(event.getSource().equals(cO8Input))
            cO8Input.setText("");

        if(event.getSource().equals(cU8Input))
            cU8Input.setText("");
                
    }

    /**
     * generateOrderID generates a random 8-digit number that is used for as the Order ID
     * Order ID is used to differentiate and identify each order
     * 
     * @return
     */
    private String generateOrderID() {
        String clientID = idProcessing(); // last 4 digits are the client's order
        Random ran = new Random();
        int pre = ran.nextInt(9999) + 1000;
        String orderID = String.valueOf(pre) + clientID; 
        return orderID;
    }

    /**
     * used by generateOrderID to get the last 4 digits of the client's order ID
     * @return
     */
    private String idProcessing(){
        String clientID = new String(String.valueOf(adultMale) + String.valueOf(adultFemale)
                          + String.valueOf(childO8) + String.valueOf(childU8));
        return clientID;
    }
    
    /**
     * validateInput is used to validate non-digit inputs (e.g. entering letters, symbols) and invalid digits (-1, -5)
     * it also checks if the order is null (all clients are 0)
     * the method also throws error messages if the inputs are invalid
     * 
     * @return
     */
    private boolean validateInput(){
        
        boolean allInputValid = true;
        String maleText = aMInput.getText();
        String femaleText = aFInput.getText();
        String cO8Text = cO8Input.getText();
        String cU8Text = cU8Input.getText();

        if (validateDigits(maleText) && Integer.parseInt(maleText) >= 0) { adultMale = Integer.parseInt(maleText); }
        else {
            JOptionPane.showMessageDialog(this, "Please enter a valid number of adult male clients (e.g. 2).");
            allInputValid = false; }
        
        if (validateDigits(femaleText) && Integer.parseInt(femaleText) >= 0) { adultFemale = Integer.parseInt(femaleText); }
        else {
            JOptionPane.showMessageDialog(this, "Please enter a valid number of adult female clients (e.g. 2).");
            allInputValid = false; }
        
        if (validateDigits(cO8Text) && Integer.parseInt(cO8Text) >= 0) { childO8 = Integer.parseInt(cO8Text); }
        else  {
            JOptionPane.showMessageDialog(this, "Please enter a valid number of children over 8 (e.g. 2).");
            allInputValid = false; }

        if (validateDigits(cU8Text) && Integer.parseInt(cU8Text) >= 0) { childU8 = Integer.parseInt(cU8Text); }
        else {
            JOptionPane.showMessageDialog(this, "Please enter a valid number of children under 8 (e.g. 2).");
            allInputValid = false; }

        // checking if there is no input given for any clients - that is, there is no order (all clients 0)
        if (adultMale == 0 && adultFemale == 0 && childO8 == 0 && childU8 == 0) {
            JOptionPane.showMessageDialog(this, "Hamper cannot be created for 0 clients. Please enter the number of clients for each category.");
            allInputValid = false;
        }
        return allInputValid;
    }

    /**
     * validateDigits is used to check if input is a number or not
     * 
     * @param input
     * @return
     */
    private boolean validateDigits(String input) {
        try {
             Integer.parseInt(input);
             return true;
        } catch (NumberFormatException e) {
             return false;
        }
    }

    /**
     * createBestHamperString generates a formatted string of all the best hampers
     * 
     * @param order
     * @return
     */
    private String createBestHamperString(Order order) {
        StringBuilder bestHamperString = new StringBuilder();
        for (int i = 0; i < order.getFamilyHamper().length; i++) {
            if (i > 0) bestHamperString.append("\n\n");
            bestHamperString.append("Hamper " + (i + 1) + " items:\n");
            ArrayList<Map<String,String>> bestHamper = order.getFamilyHamper()[i].getHamper();
            for (Map<String, String> foodItem : bestHamper) {
                String name = foodItem.get("Name");
                String itemID = foodItem.get("ItemID");
                bestHamperString.append(itemID).append("\t\t").append(name).append("\n");
            }
        }
        return bestHamperString.toString();
    }

    public int getadultMale() {       // getadultMale method returns the male count
        return adultMale;
    }

    public int getadultFemale() {         // getadultFemale method returns the female count
        return adultFemale;
    }

    public int getChildO8() {          // getChildO8 method returns the childO8 count
        return childO8;
    }

    public int getChildU8() {             // getChildU8 method returns the childU8 count
        return childU8;
    }

    public ArrayList<Map<String,String>> getorderSummary() {        // getorderSummary returns the orderSummary
        return this.orderSummary;
    }

    /**
     * createOrderForm creates the orderform output file
     * 
     * @param order
     * @param orderID
     * @throws IOException
     */
    public void createOrderForm(Order order, String orderID) throws IOException {
        if (order.getFamilyHamper() == null) throw new IllegalArgumentException();

        FileWriter writer = new FileWriter("orderform--" + orderID + ".txt");
        writer.write("Example Food Bank" + "\n");
        writer.write("Hamper Order Form " + orderID + "\n\n");
        writer.write("Name: " + "\n");
        writer.write("Date: " + "\n\n");
        for (int i = 0; i < order.getFamilyHamper().length; i++) {
            writer.write("Hamper " + (i + 1) + ": " + formatFamilyString(order.getFamilyHamper()[i]) + "\n");
        }

        writer.write("\n");
        for (int i = 0; i < order.getFamilyHamper().length; i++) {
            if (i > 0) writer.write("\n\n");
            writer.write("Hamper " + (i + 1) + " items:\n");
            ArrayList<Map<String,String>> bestHamper = order.getFamilyHamper()[i].getHamper();
            for (Map<String, String> foodItem : bestHamper) {
                String name = foodItem.get("Name");
                String itemID = foodItem.get("ItemID");
                writer.write(itemID + "\t\t" + name + "\n");
            }
        }
        writer.close();
    }

    /**
     * formatFamilyString method creates a string of the client counts
     * 
     * @param family
     * @return
     */
    private String formatFamilyString(ClientHamper family) {
        if (family.getClientCount(ClientTypes.MALE.asString()) < 0) throw new IllegalArgumentException();
        StringBuilder formattedString = new StringBuilder();
        if (family.getClientCount(ClientTypes.MALE.asString()) > 0) {
            formattedString.append("Adult male: ").append(family.getClientCount(ClientTypes.MALE.asString())).append(", ");
        }
        if (family.getClientCount(ClientTypes.FEMALE.asString()) > 0) {
            formattedString.append("Adult female: ").append(family.getClientCount(ClientTypes.FEMALE.asString())).append(", ");
        }
        if (family.getClientCount(ClientTypes.CHILDU8.asString()) > 0) {
            formattedString.append("Child under eight: ").append(family.getClientCount(ClientTypes.CHILDU8.asString())).append(", ");
        }
        if (family.getClientCount(ClientTypes.CHILDO8.asString()) > 0) {
            formattedString.append("Child over eight: ").append(family.getClientCount(ClientTypes.CHILDO8.asString())).append(", ");
        }
        formattedString.deleteCharAt(formattedString.length() - 2);
        return formattedString.toString();
    }

    public void mouseEntered(MouseEvent event) { }

    public void mouseExited(MouseEvent event) { }

    public void mousePressed(MouseEvent event) { }

    public void mouseReleased(MouseEvent event) { }

    public void actionPerformed(ActionEvent event) { }
}
