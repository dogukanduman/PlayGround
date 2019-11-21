# PlayGroundService-Player
* First you need to run build.sh to build and generate JARS for Player and PlayGroundService
 `build.sh`
# Player
## What it does
* Player is simple client which communicate with other players via PlayerGroundService
* Players can register  or unregister to PlayGroundService
* Players can message to each other

## How to run
* You need to run 
 `player.sh`
* Also you have to give two parameters
	* Socket port(default:5001)
	* Name(default:PLAYER)
	 `player.sh 5001 PLAYER1`
* It has additional parameters for testing
	* How many message PLAYERX will send(default:20)
	* How frequently PLAYERX will send(in seconds, default:2 s)
	*  What is the message(default:Hi!!!)
	 `player.sh 5001 PLAYER1 20 2 Hi!!`
* For testing player completely , you should run multiple **Player** instances in multiple terminals like below
	 `player.sh 5001 PLAYER1 24 1 Hi!!`
	 `player.sh 5002 PLAYER2 12 3 Hi!!`
	 `player.sh 5003 PLAYER3 6 4 Hi!!`
	 `player.sh 5004 PLAYER4 18 2 Hi!!`
* Most importing thing you should start **PlayGroundService** **first**
* Player closes the connection when can not find anyone to speak in 3 seconds

# PlayGroundService
## What it does
* PlayGround is responsible for registering and messaging between players
* Players can register to or unregister from PlayGroundService
* Players can message to each other via PlayGroundService.

## How to run
* You need to run 
 `playGroundService.sh`
* Socket port(default:5000)
* PlaygroundService is kind of server so it is enough to run it one time
* PlaygroundService checks every **5 seconds** last messaging time
* If there is no messaging last **30 seconds** it will close.
