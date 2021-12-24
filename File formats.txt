//Map template  
12 24 //width height of the map 
42 // max number of rats before game lost condition 
136 //expected time to eradicate all rats
//Map 
GPTTTPGGTTTTTPPTG //Map, G for grass, P for path, T for tunnel, creating tiles in accordance to the size limit above 
//Rats 
7 true 4 13 90 false 27 false	 //age/sex/position/direction/pregnancy/hp/sterile etc. 
//Inventory 
5 //item total cooldown length 
3 //current item cooldown (time until next item given) 
>Template for map  


//Player profile/ save file 
10 //Max level 
3 //Curr level player is playing on
23 //Points accumulated so far in this level 
Map_name //Points to current map 
17 //current timer of the game currently 
//Rats 
27 false 14 1 90 true 27 true	 //age/sex/position/direction/pregnancy/hp/sterile etc. 
//Item 
bomb 5 14 7 //every item that is currently placed on the map, alongside how much hp the item has remaining. For example: type x-pos y-pos hp 
//Inventory 
5 20 2 11 //items currently possessed (not on the map, index will indicate item, number will indicate quantity; e.g. 5 20 2 11 will mean 5 bombs, 20 poison gas, 2 stop signs etc.) 

//Player profile/ save file
2 // Max level
// Nothing else is included to show there is no ongoing game