# StoneBound

## Main Idea
In order to implement the game, I structured the project into two main folders, which are explained in greater detail below.

The game is played by instantiating a new *Game* object with the required arguments, the decks of the players and the list of input for the games to be played, as well as a JSON to put the output of the game.

In order to implement the minions and heroes, I made an abstract class Card, which is extended by Hero and Minion, which are then extended by instances for each one that is implemented. I made use of polymorphism by overriding methods such as placing a card and using a minion or hero's special ability.

I also structured the project around the Factory design pattern when creating the *Minion* and *Hero* class, for instantiating a new entity with the help of a static method and calling its constructor from there, so as not to repeat the code.

## Project Structure

* **src**/
  * **checker**/ - checker files
  * **fileio**/ - contains classes used to read data from the json files
  * **main**/
      * *Main* - the Main class runs the checker on the project implementation. Add the entry point to your implementation in it. Run Main to test your implementation from the IDE or from command line.
      * *Test* - run the main method from Test class with the name of the input file from the command line and the result will be written to the out.txt file. Thus, you can compare this result with ref.
  * **setup**/
    * *Action* - contains the name of the game command and its arguments
    * *Game* - the basic implementation of a game, which is made up of more matches
    * *Match* - the execution of all commands specific to a match of StoneBound
    * *MatchInfo* - the information about players' decks and heroes for each match
    * *Player* - contains the information about the player's deck, hand, hero and mana count
  * **resources**/
    * **heroes**/ - classes for all the heroes implemented
    * **minions**/ - contains the implementation for minion types and specific minions
        * **regular**/ - all the classes for minions that have no ability and aren't tanks
        * **special**/ - classes for the minions with abilities
        * **tanks**/ - contains the classes for tank minions
        * *Minion* - has the basic implementation of a minion, which extends the abstract class Card and implements helper functions such as reducing health, placing and removing the minion from the board, as well as printing the minion as a JSON
        * *SpecialMinion* - class that extends Minion and adds a function to activate the minion's special ability
        * *Tank* - class that extends Minion and constructs a minion with the isTank field always set to true
    * *Card* - the abstract class that contains the common fields of both Hero and Minion
    * *Coordinates* - contains the coordinates needed to refer to a card's row and column on the board
    * *Decks* - class that keeps track of a player's avaialable decks and number of cards in each one
    * *Hero* - has the basic implementation of a hero, as well as the Factory function to build an instance of a hero and the function to print attributes to a JSON
    * *Placement* - abstract class that contains helper functions that are used in order to place a specific minion on the front row or the back row, meant for extension in case of deciding to switch which row a minion needs to be played on
* **input**/ - contains the tests in JSON format
* **ref**/ - contains all reference output for the tests in JSON format

