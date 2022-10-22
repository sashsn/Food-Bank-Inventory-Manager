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
 * NutritionTypes is an enumeration that is used by different classes to get the each nutrition type
 */

public enum NutritionType {
    CALORIES {
        public String asString() {
            return "Calories";
        }
    }, WHOLEGRAINS {
        public String asString() {
            return "WholeGrains";
        }
    }, FRUITVEGGIES {
        public String asString() {
            return "FruitVeggies";
        }
    }, PROTEIN {
        public String asString() {
            return "Protein";
        }
    }, OTHER {
        public String asString () {
            return "Other";
        }
    };
    public abstract String asString();
}
