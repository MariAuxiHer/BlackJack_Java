package BlackJack_Java;

/* blackjack.java
// Maria Hernandez
// 11/08/2021
// This program allows one user (player) to play a blackjack game with a programmed dealer. Both will work off of the same deck of 52 cards, so no cards will be duplicated between the dealer and player. 
// Each card has a single value from 1 to 10, and for each value, there are four suits. All aces are worth 1 point, then cards 2 to 10 have respective values of 2 to 10 points. 
// Cards' values and suits are abbreviated as AH, AC, AD, AS, 2H...KH, KC, KD, KS.
// The point of the game is to get to the value 21 without going over.  If either the player or the dealer gets 21, the one who gets it automatically wins. 
// If a score is above 21, it is called a "bust" and whoever has a bust automatically loses.
// The player has two options, either "hit" or "stand". If the player hits, the dealer hands them another card. 
// Once the player stands, the dealer hits if the total value of their cards is less than 18, and the dealer keeps hitting until the total value of their cards is greater or equal to 18. At this point, the dealer stands. 
// If the player and dealer have not gone above 21, then the scores are compared. Whoever has the higher score wins. If both have the same score, they tie in a "draw".
Finally, after the result of the game is decided, the programs display to the user a String with the final result. 
*/

import java.util.ArrayList;
import java.util.Scanner;

class blackjack { 
    public static void main(String[] args) {

        // First, we create an instance of the blackjack class called bl and declare one Scanner variable that will allow us to extract inputs from the user.
        blackjack bl = new blackjack();
        Scanner s = new Scanner(System.in);

        // We call the method newGame() through the instance bl to create a new game. Then, we call the method printAllCards two times to print out the first two dealer's and player's cards. 
        bl.newGame();
        bl.printAllCards(DEALER);  
        bl.printAllCards(PLAYER);

        // We prompt the user to either "hit" or "stand" (case insensitive), and we extract this information from the user. 
        System.out.format("%s%6s", "Hit or stand", "? "); 
        String hit_stand; 
        hit_stand = s.next();
   
        // We use a do-while loop to keep asking the user for a response again if they don't type either "hit" or "stand".
        go_to_score:
        do{
        
            // We use a while loop to ensure that while the user's response is equal to "hit," the programmed dealer will keep handing them one more card as long as the total value of the user's cards is less than 21.
            // If the user's cards values are greater or equal than 21, we break out of the outer do-while loop. 
            while (hit_stand.equalsIgnoreCase("hit")){ 

                bl.hit(PLAYER); 

                if(bl.getScore(PLAYER) >= 21){ 
                    break go_to_score;
                }
        
                else{
                    System.out.println();
                    bl.printAllCards(DEALER);
                    bl.printAllCards(PLAYER);
                    System.out.format("%s%6s", "Hit or stand", "? "); 
                    hit_stand = s.next(); 
                }
            }

            // If the user's response is "stand," the dealer will hit as long as the total value of their cards is less than 18. Each time the dealer hits, we display a message indicating that the dealer hit.  
            // The dealer keeps hitting until the total value of their cards is greater or equal to 18. At this point, the dealer stands, and then we display a message indicating that the dealer stands and break out of the outer do-while loop. 
            // If the dealer's cards values are greater or equal to 21, we immediately break out of the outer do-while loop. 
            if(hit_stand.equalsIgnoreCase("stand")){
          
                while(bl.getScore(DEALER) < 18){ 
                    bl.hit(DEALER);
                    System.out.format("\nDealer hits     : %-20s (%2d)", bl.getCards(DEALER), bl.getScore(DEALER));
                
                    if(bl.getScore(DEALER) >= 18){ 
                        if(bl.getScore(DEALER) >= 21){ 
                            System.out.println();
                            break go_to_score;
                        }
                        else{
                            System.out.format("\nDealer stands   : %-20s (%2d)\n", bl.getCards(DEALER), bl.getScore(DEALER)); 
                            break go_to_score;
                        }
                    }
        
                }

                if(bl.getScore(DEALER) >= 18){ 
                    break go_to_score;
                }

            }
            // If the player's response is other than "hit" or "stand" (case insensitive), we prompt them again to enter either "Hit or stand"
            else{
                System.out.format("%s%6s", "Hit or stand", "? ");
                hit_stand = s.next();
            }
        }while(true);


        // After we break out of the outer do-while loop, we display to the user (player) the player's and dealer's final scores.
        System.out.println();
        bl.printAllCards(DEALER);
        bl.printAllCards(PLAYER);
        

        // Finally, we decide the final result and display a message to the user indicating it.  
        if(bl.getScore(PLAYER) > 21){
            System.out.print("Player busts, dealer wins!");
        }
        else if(bl.getScore(DEALER) > 21){
            System.out.print("Dealer busts, player wins!");
        }
        else if((bl.getScore(PLAYER) > bl.getScore(DEALER)) || bl.getScore(PLAYER) == 21){ 
            System.out.print("Player wins!");
        }
        else if((bl.getScore(DEALER) > bl.getScore(PLAYER)) || bl.getScore(DEALER) == 21){
            System.out.print("Dealer wins!");
        }
        else if(bl.getScore(DEALER) == bl.getScore(PLAYER)){ 
            System.out.print("Player and dealer draw.");
        }

        s.close();

    }

    // We create the constructor that allows us to initialize the private fields mCardDeck and random_array of the blackjack class once the instance of the class (bl) is created.  
    public blackjack() {
        mCardDeck = new int[NUM_CARDS_IN_DECK]; 

        // The following initialization of the field random_array is NOT in the template. However, I talked with instructor Camille and she told me that there's not any problem in adding it. 
        random_array = new ArrayList<>(0);
    }

    // We create the method newGame() that allow us to reset all the cards back into the deck and hand out exactly 2 cards to the dealer and 2 cards to the player. 
    public void newGame() {
       
        // To reset all the cards, we assign FREE_CARD (which has value 0) to each card. 
        for(int i = 0; i < mCardDeck.length; i++){
            mCardDeck[i] = FREE_CARD;
        }

        // Then, we use a for loop to hand out exactly 2 cards to the dealer and 2 cards to the player. 
        // Inside the for loop, we call the getRandomIndex() method which returns a random index from 0 to 51. 
        // This index is also different from all the other indexes that contain the cards owned either by the player or the dealer. 
        // Then, we store the value returned by the getRandomIndex() method into the variable random_index. 
        // We hand out the cards by assigning the value PLAYER (1) and DEALER (2) to the element stored in the position of the given random_index. 
        // By doing that, we update these indexes from FREE_CARD (0) to either PLAYER (1) or DEALER (2).

        for(int i = 0; i < 4; i++){
            int random_index =  getRandomIndex();
            if(i == 0 || i == 1){ 
                mCardDeck[random_index] = PLAYER; 
            }
            else{
                mCardDeck[random_index] = DEALER; 
            }
        }   

    }
    
    // We create the method printAllCards() that allow us to print out either the cards of the dealer or the cards of the player. 
    public void printAllCards(int who) {
   
        // Depending on the parameter passed to the method printAllCards() (PLAYER or DEALER), we print out a formatted String with the cards and the total score of either the Player or the Dealer. 
        if(who == PLAYER){
            System.out.format("You have cards  : %-20s (%2d)",getCards(PLAYER), getScore(PLAYER)); 
        }

        else{
            System.out.format("Dealer has cards: %-20s (%2d)",getCards(DEALER), getScore(DEALER));
        }

        System.out.println(); 
    }


    // We create the method hit() that allow us to hand out 1 more card either to the dealer or the player.
    public void hit(int who) {
       
        // To ensure that the random index (that stores a random card) is not repeated, we call the getRandomIndex() method, which returns a random index from 0 to 51. 
        // This index is also different from all the other indexes that contain the cards owned either by the player or the dealer. 
        // Then, we store the value returned by the getRandomIndex() method into the variable random_index.
        // Depending on the parameter passed to the method hit() (PLAYER or DEALER), we hand out an additional card either to the player or the dealer.
        // We hand out the card by assigning the value PLAYER (1) or DEALER (2) to the element stored in the position of the given random_index.
        // By doing that, we update these indexes from FREE_CARD (0) to either PLAYER (1) or DEALER (2).
       
        int random_index =  getRandomIndex();
                 
        if(who == PLAYER){ 
            mCardDeck[random_index] = PLAYER; 
        }
        else if (who == DEALER) {
            mCardDeck[random_index] = DEALER; 
        }
            
    }

    // We create the method getCards() that allow us to get a String with either the cards of the dealer or the cards of the player. 
    public String getCards(int who) { 

        // We create a StringBuilder that will allow us to build the String that contains all the cards of either the player or the dealer. 
        StringBuilder sb = new StringBuilder();  

        // We utilize the array fields CARD_SUITS and CARD_VALUES and two for loops to create the array mCardDeckString.
        // The outer loop loops through the values (A,1,2....K), and the inner loop loops through the suits (H, C, D, S).
        // We use the variable 'a' as a counter to increment the indexes of the mCardDeckString array that contains all the cards with the values and suits (AH, AC, AD, AS, 2H......KH, KC, KD, KS) 
        String[] mCardDeckString; 
        mCardDeckString = new String[NUM_CARDS_IN_DECK]; 
        int a = 0;

        for(int i = 0; i < CARD_VALUES.length; i++){
            for(int j = 0; j < CARD_SUITS.length; j++){    
                mCardDeckString[a] = CARD_VALUES[i] + CARD_SUITS[j]; 
                a += 1;
            }
        }

        // Since both the array of integers mCardDeck and the array of Strings mCardDeckString contain 52 elements, and both of them go from index 0 to index 51,
        // we loop through the mCardDeck to find the cards owned by the player (assigned with a value of 1) or the cards owned by the dealer (assigned with a value of 2).
        // Depending on the parameter passed to the method getCards() (PLAYER or DEALER), we append to the StringBuilder the cards with the value of 2 (the cards owned by the dealer)
        // or the cards with the value of 1 (the cards owned by the player)
        for(int k = 0; k < mCardDeck.length; k++){
            if(mCardDeck[k] == 2 && who == DEALER){
                sb.append(mCardDeckString[k]);
                sb.append(" ");
            }
            else if(mCardDeck[k] == 1 && who == PLAYER){
                sb.append(mCardDeckString[k]);
                sb.append(" ");
            }
        }

        // Then, we convert from a StringBuilder to a String and return the String that contains all the cards of either the player or the dealer.
        return sb.toString();
    }

    // We create the method getScore() that allow us to get an integer that represents either the total value of the cards owned by the dealer or the total value of the cards owned by the player.   
    public int getScore(int who) { 
        int score = 0; 
    
         // We first generate an array that stores the value of each card of the deck. Ace cards are worth 1 point, cards from 2 to 10 are worth their respective value (2 to 10 points), and Jack, Queen, and King are worth 10 points. 
        int[] value_array; 
        value_array = new int[NUM_CARDS_IN_DECK]; 
        int value = 0; 

        for (int i = 0; i <NUM_CARDS_IN_DECK; i+=1){ 
            // We use the math formula i%4 to increment the score by one each 4 cards from the index 0 to the index 35. 
            if(i>=0 && i <= 35){  
                if(i%4 == 0){
                    value += 1; 
                }
                value_array[i] = value; 
            }
            // Then, from the index 36 to the last index all cards are worth 10 points. 
            else{
                value = 10; 
                value_array[i] = value; 
            }    
        }

        // Finally, we loop through the mCardDeck to find the cards owned by the player (assigned with a value of 1) or the cards owned by the dealer (assigned with a value of 2).
        // Depending on the parameter passed to the method getScore() (PLAYER or DEALER), we total up the score of either the cards owned by the dealer (represented with 2),
        // or the cards owned by the player (represented with 1)
        for(int k = 0; k < mCardDeck.length; k++){
            if(mCardDeck[k] == 2 && who == DEALER){
                score += value_array[k];
            }
            else if(mCardDeck[k] == 1 && who == PLAYER){
                score += value_array[k];
            }
        }
        // The we return the score of either the player or the dealer.
        return score; 
    } 

    // The following getRandomIndex() is NOT in the template. However, I talked with instructor Camille and she told me that there's not any problem in adding this function. 

    // To ensure that the random index (that stores a random card) used in the methods newGame() and hit() is not repeated, we create the getRandomIndex() method which return a random index from 0 to 51. 
    // This index is also different from all the other indexes that contain the cards owned either by the player or the dealer. 
    public int getRandomIndex() { 

        // We declare the variable random_index and store there a random index provided by the math formula (int)(Math.random() *  mCardDeck.length).
        int random_index; 
        random_index = (int)(Math.random() *  mCardDeck.length);
    
        // With the following code, we ensure that the random index provided by the above math formula is not repeated. 
        // To do that, we use the ArrayList random_array that stores all the unique random indexes created during the game. 
        // We add the first-ever random index created to the ArrayList (since this is the first index, we know that it won't be a repeated index)
        if(random_array.size() == 0){
            random_array.add(random_index);
        }
        // After the first index, we start checking if the following indexes provided by the above math formula are repeated or not.
        else{    
            // To do that, we utilize two loops. The outer loop will allow us to re-calculate the random index in the case that the index provided is already on the random_array.
            search_available_index:
            do{
                // With the inner loop, we compare the index provided by the above math formula to the other indexes stored in the random_array ArrayList.
                for(int i = 0; i< random_array.size(); i++){
                    // If the index provided is different from the indexes from 0 to the second to the last index in the random_array, we go to the last iteration and compare the index 
                    // provided to the last index in the random_array. If the given index is equal to one of the indexes in the random_array, we break out of the inner loop and re-calculate the index.
                    if(i >= 0 && i < (random_array.size()-1)){
                        if(random_index != random_array.get(i)){
                            continue;
                        }
                        else{
                            break; 
                        }
                    } 
                    // In the last iteration of the loop, we compare the index provided to the last index in the random_array. If the given index is different from the last element in the random_array, 
                    // we add that index to the random_array and break out of the outer do-while loop. 
                    // If the given index is equal to the last index in the random_array, we break out of the inner loop and re-calculate the index.
                    else{ 
                        if(random_index != random_array.get(i)){ 
                            random_array.add(random_index);
                            break search_available_index;
                        }
                        else{
                            break;
                        }
                    } 
                }
                random_index = (int)(Math.random() *  mCardDeck.length);
       
            }while(true); 
        }

        // Finally, we return the last value added to the 'random_array' ArrayList. 
        return (random_array.get(random_array.size()-1)); 
    }

    // The deck of 52 cards
    private int[] mCardDeck; 
    private final static int NUM_CARDS_IN_DECK = 52; 

    // A free card will have the value FREE_CARD
    // A player owned card will have the value PLAYER 
    // A dealer owned card will have the value DEALER
    private final static int FREE_CARD = 0;
    private final static int PLAYER = 1;
    private final static int DEALER = 2;

    // Four suits of cards H: Hearts, C: Clubs,
    //                     D: Diamonds, S: Spades
    private final static String[] CARD_SUITS = { 
        "H", "C", "D", "S" 
    };
    // Ace, 2-10, Jack, Queen, King
    // Ace is 1 point, JQK are each 10 points.
    private final static String[] CARD_VALUES = { 
        "A", "2", "3", "4", "5", "6", "7", 
        "8", "9", "10", "J", "Q", "K" 
    };

    // The following private ArrayList field random_array is NOT in the template. However, I talked with instructor Camille and she told me that there's not any problem in adding this field. 
    private ArrayList<Integer> random_array;
}