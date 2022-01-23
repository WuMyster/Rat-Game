# CS230_Rats

I have basically finished this game.

To be able to run the game, you must have Java 17 and JavaFX 17 installed which can be downloaded here: https://gluonhq.com/products/javafx/, with instructions on how to install and run here: https://openjfx.io/openjfx-docs/#install-javafx.

Once you're set up, run Main.java and here you should see a leaderboard along with a textbox to enter your name. You can create a new player or load into a previous user.

Once you're logged in, you can select a level or you can load a previous game. It will now take you onto a game page.

The aim of the game is to kill all the rats within a time and rat number limit. The time limit is shown at the top and the max number of rats before the game ends is on the left. The 2 colours are for the 2 genders. You kill rats using items from the right menu bar.

On the right is most of your UI, from top to bottom: level number, points earned in game so far and some items. There is a maximum of 5 items for each.

---

The Poison item instantly kills a rat on that tile, and is used up.

The StopSign which stops rats from passing through. This will break down after a number of rats have hit it or another item interacts with it.

The Sterile object turns a rat sterile, meaning it cannot reproduce from now on. It takes effect after 5 seconds and has a radius of 3 tiles.

The bomb, after 4 seconds, will blow up the row and column it is on, killing all rats on those tiles.

Blue sex symbol sets the gender of the rat to male, likewise with the pink female sex symbol.

The Gas item spreads over time over a radius of 3 tiles before dissipating, it will instantly kill any rat that enters the gas.

Death Rat are similar to normal rats, however they kill rats. They do not interact with other items other than the StopSign and the Bomb. It will kill 5 rats before dying itself.

The Super Death Rat is a Death Rat but will always move towards the nearest normal rat and kill it. It will also kill 5 rats before it dies.

No item affects other items other than the bomb item and the Super Death Rat.

===

In the options menu option, you can play and pause the game. When the game is paused, you can use the file option to save the game. You can then log out and when you log back in, be sure to hit the load previous game button!

If you win or lose, any saves on your account will be overwritten.
