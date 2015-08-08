gameDotEXE
Team Members:Chris Chin, Geoffrey Longuet, Jinpeng Song, Jiaheng Wang and Wendell
Team 11

Starting a Game
1. Run the main method of game.Main.java
2. Press Server to create a server
3. either 
	Run Server - Creates a new Server
	Load Server - Loads a server from a xml document (choose demoForPresentation.xml for an example)

Server:	StopServer - closes the server
	Update Map - Manually Updates the maps
	Change Map - Changes the display of the current map
	
4. Press Start to create a new Client
	Enter a name for your Character, IP Address and Port
	You can enter an existing Character name(ie Bob from demoForPresentation.xml)
	Or enter a new name to create a new Character and select either Pig Farmer or Swordsman


Playing the Game
Controls
Left - Rotates the map clockwise
Right - Rotates the map anticlockwise

Hold down W - Move Forward
Hold down A - Move Left
Hold down S - Move Right
Hold down D - Move Backward

E - Interact/Use
Hold down F - Attack
Space - Inspect


User Interface
The Compass shows the current direction.
The Bottom Panel is your inventory with 8 slots.
The Right Panel is a Chat Box where you can chat to other people on the server.
It also displays any text messages
You can select items by hovering over them in your inventory and clicking use
You can inspect your items by hovering over them in your inventory and clicking info 
You start off with 1 potion in your inventory. The red bar is your health.

Maps
* You start off in the StartingMap (M)
* There are gaps around the edges of the map. These are teleports which will teleport 
  you to the next map depending on direction
 ___ ___ ____
|_NW_|_N_|_NE_|
|__W_|_M_|__E_| Current Map (M)
|_SW_|_S_|_SE_|

Items/Objects
* You can move around the map attacking barrels and slimes.
* The Slimes attack you and you lose health. But they are passive creatures
  and will not attack you unless provoked.
* You can pick up keys and potions on the ground using the key E
* You can use keys to open chests the pair has the same
  keycode ie (keycode 21 key unlocks a chest with keycode of 21)