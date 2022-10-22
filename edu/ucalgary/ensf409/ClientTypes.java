package edu.ucalgary.ensf409;

/**
 * GROUP 49
 * @author Radia Jannat <a href = "mailto: @ucalgary.ca">@ucalgary.ca</a>,
 *         Rumaisa Shoeb Talukder <a href = "mailto: rumaisashoeb.talukde@ucalgary.ca">rumaisashoeb.talukde@ucalgary.ca</a>, 
 *         Sadman Shahriar Snigdho <a href = "mailto: sadmanshahriar.snigd@ucalgary.ca">sadmanshahriar.snigd@ucalgary.ca</a>,
 *         Shreosi Debnath <a href = "mailto: @ucalgary.ca">@ucalgary.ca</a>
 * @version 1.1
 * @since 1.0
 */

/*
 * ClientTypes is an enumeration that is used by different classes to get the ClientIDs of the different clients
 */

public enum ClientTypes {
    MALE {
        public int clientID() {
            return 1;
        }
        public String asString() { return "Male"; }
    }, FEMALE {
        public int clientID() {
            return 2;
        }
        public String asString() { return "Female"; }
    }, CHILDO8 {
        public int clientID() {
            return 3;
        }
        public String asString() { return "ChildO8"; }
    }, CHILDU8 {
        public int clientID() {
            return 4;
        }
        public String asString() { return "ChildU8"; }

    };
    public abstract int clientID();
    public abstract String asString();
}