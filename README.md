# Rats-Game

Final submission to this course work for uni.
Have continued on this project in the branch Main-J and is much more complete/ closer to the final product.

To be able to run the game, you must have Java 17 and JavaFX 17 installed which can be downloaded here: https://gluonhq.com/products/javafx/, with instructions on how to install and run here: https://openjfx.io/openjfx-docs/#install-javafx.

Once you're set up, run Main.java and will take you onto a game page.

On the right is most of your UI, from top to bottom: level number, points earned in game so far and some items.

---

The StopSign which stops rats from passing through. This will break down after a number of rats have hit it or another item interacts with it.

The bomb, after 4 seconds, will blow up the row and column it is on, killing all rats on those tiles.

The Poison item instantly kills a rat on that tile, and is used up.

Blue sex symbol sets the gender of the rat to male, likewise with the pink female sex symbol.

The Sterile object turns a rat sterile, meaning it cannot reproduce from now on and will be used up.

Death Rat are similar to normal rats, however they kill rats. They do not interact with other items other than the StopSign and the Bomb. It will kill 5 rats before dying itself.

==

At the top, you can save the game, with the potential to load from it and next to it is the message of the day
