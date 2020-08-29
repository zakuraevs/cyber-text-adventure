package adventure

class Adventure {
 
  //vals, vars, instantiating classes and setting up the game world:

  //Adventure title:
  val title = "Cyberpunk challenge"
  
  
  //Descriptions of areas:
  private val sewerDescription       =  "Sewer..\n You're in a stinking sewer \nThere is a door to the north leading to the city, but it is lock-protected. \nLuckily, Armitage left you a hacking package by the door"
  private val hubDescription         = {"The Hub..\n Rainy little part of Neo-Tokyo \nYou see gritty people walking about their business. There are locked doors North, East and West." + 
                                "\nThere's a shady-looking merchant by the West door and a gang thug by the north one. Maybe you should try talking to them.\nNear the east door, you see a crate. You can move crate."}
  private val gunRoomDescription     =  "Storage Room..\n You are in a storage area. There are crates of spices, cyberware, implants and other goods. \nYou also see a siny new gun!"
  private val puzzleRoomDescription  = {"Catacombs..\nYou're in the underground service tunnerls. It is dark and scarry in here. \nYou see the door which probably leads to the server room, " +
                                "but it is password-protected. \nThe only ways to get in is to pick up the door protector, " +
                                "examine it, use it and type the correct password. If you get it wrong, the thing will electrocute you."}
  private val bossRoomDescription    =  "THERE IS A BOSS HERE! HE'S ABOUT TO SHOOT YOU!, duck!\n\n Once you duck, you can shoot boss. If you take make the wrong move here, you will die!" 
  private val chipRoomDescription    =  "The server room.. You see the chip\nThe chip stores the code to release the AI. It will also enable you go deep into the cyberspace. Examine the chip, read it and use it."
  private val neuroRoomDescription   =  "DO YOU HAVE THE CHIP? \nTYPE IN THE CODE"
  private val secretRoomDescription  =  "???"
    
  
  //Hints for the 'hint' function for eacha rea:
  private val sewerHint       =  "'get hck_chip' --> 'use hck_chip' --> 'go north'"
  private val hubHint         = {"Path 1: 'talk_to merchant' --> 'move crate' --> 'crawl' --> [you will now be in the storage area] 'get gun' --> 'go west' --> [you will now be back at the hub] 'sell gun' --> 'go west'" +
                         "\nPath 2: 'talk_to thug' --> 'move crate' --> 'crawl' --> [you will now be in the storage area] 'get gun' --> 'go west' --> 'shoot thug' --> 'get note' --> 'examine note' --> 'go north'"}
  private val gunRoomHint     =  "'get gun' --> 'go west'"
  private val puzzleRoomHint  = {"'get protector' --> 'examine protector' --> [go to youtube through any device, searh 'blade baseline', open the first video, listen for the missing word] --> 'use protector'" +
                         "SPOILER: --> [type in 'interlinked' when prompted]" +
                         " --> go north"}
  private val bossRoomHint    =  "'duck' --> 'shoot boss' --> 'go north'"
  private val chipRoomHint    =  "'get the_chip' --> 'examine the_chip' --> 'read_chip' --> 'use the_chip' --> 'go into_cyberspace'"
  private val neuromancerHint = {"type in the code found in the chip" +
                         "SPOILER: --> [type in '42069' when prompted]"}
  private val secretRoomHint  =  "???"
  
  
  //Instantiating game areas:
  private val startingSewer     = new Area("Sewer", sewerDescription, false, sewerHint)
  private val theHub            = new Area("Hub", hubDescription, true, hubHint)
  private val gunRoom           = new Area("Storage zone", gunRoomDescription, true, gunRoomHint)
  private val puzzleRoom        = new Area("Catacomb", puzzleRoomDescription, true, puzzleRoomHint)
  private val bossRoom          = new Area("Gang zone", bossRoomDescription, true, bossRoomHint)
  private val chipRoom          = new Area("Server room", chipRoomDescription, true, chipRoomHint)
  private val neuromancerRoom   = new Area("Neuromancer", neuroRoomDescription, true, neuromancerHint)
  private val secretRoom        = new Area("???", secretRoomDescription, false, secretRoomHint)
  
  
  //Setting neighbors for each area:
   startingSewer.setNeighbors(Vector("north" -> theHub))
          theHub.setNeighbors(Vector("north" -> bossRoom, "east" -> gunRoom, "south" -> startingSewer, "west" -> puzzleRoom, "???" ->  secretRoom))
         gunRoom.setNeighbors(Vector(                                                                  "west" -> theHub))
      puzzleRoom.setNeighbors(Vector("north" -> chipRoom, "east" -> theHub))
        bossRoom.setNeighbors(Vector("north" -> chipRoom,                     "south" -> theHub))
        chipRoom.setNeighbors(Vector(                                         "south" -> bossRoom,     "west" -> puzzleRoom, "into_cyberspace" -> neuromancerRoom))
 neuromancerRoom.setNeighbors(Vector(                                                                  "west" -> chipRoom))
      secretRoom.setNeighbors(Vector(                                                                  "west" -> theHub))
  
      
  //Instantiating the player:
  val player = new Player(startingSewer)    
      
      
  //NPC parameters:
  //Merchant
  val merchantResponse =  "Howdy cowboy. I can unlock the west door for you if sell me a gun"   
  val merchantNote =   new Item("merchantnote", "I love my wife and kids :)", true, "action description of merchant note", Vector[Any]() )  
  //Thug
  val thugResponse = "Buzz off! This is Gang area, I won't let you through"
  val thugDeathText = "The thug no longer blocks the passage north! \nHe also dropped something.. some kind of a note!"
  val thugNoteText = {"A note on a digital pocket journal. 'The boss overheard that there's a cowboy coming through, so he's dug in up north in an ambush! " + 
    "As soon as the cowboy walks in, the boss will try to shoot him! The only way the poor bastard can save himself is if he ducks under the cover' Haha! " +
    "Ah, I love my wife and daughters very much! Hope nothing bad happens to me!"}
  val thugNote = new Item("note", thugNoteText, true, "action description of thug note", Vector[Any]() )
      
      
  //Instantiating NPCs
  val merchant = new NPC("merchant", true, merchantResponse, "WHYYYY", Map[String, Item]("merchantnote" -> merchantNote), theHub)
  val thug = new Thug("thug", true, thugResponse, bossRoom, thugDeathText, Map[String, Item]("note" -> thugNote), theHub)
  val boss = new Thug("boss", true, "no more talkin!", chipRoom, "ARGHHRGG.. The passage north is open.",  Map[String, Item](), bossRoom)
  
  
  //Adding NPCs to areas
  this.theHub.addNPC(merchant)
  this.theHub.addNPC(thug)
  this.bossRoom.addNPC(boss)
    
      
  //Instantiating area locks
  val hubLock = new Lock("DigitalLock", "Hackable Lock", false, "", Vector(), theHub)
  val puzzleLock = new Lock("PuzzleDigitalLock", "Hackable Lock", false, "", Vector(), puzzleRoom)
  val gunLock = new Lock("GunDigitalLock", "Hackable Lock", false, "", Vector(), gunRoom)
  val bossLock = new Lock("BossDigitalLock", "Hackable Lock", false, "", Vector(), bossRoom)
  val chipLock = new Lock("ChipDigitalLock", "Hackable Lock", false, "", Vector(), chipRoom)
  
  
  //Adding locks to areas
  this.startingSewer.addLock(hubLock)   
  this.theHub.addLock(puzzleLock)
  this.theHub.addLock(gunLock)
  this.theHub.addLock(bossLock)
  this.bossRoom.addLock(chipLock)
  this.puzzleRoom.addLock(chipLock)
  
  
  //Item descriptions
  val protectorDescription = {"Protects the chip room. type in the correct code! Challenge: Connect to the cyberspace through your portable mobile device, go to the servers of Y0UTUB3 C0RP." +
                              "They keywords to look for are 'blade baseline' Try the first result you get.\nThe password we're looking for completes the phrase: 'Cells within cells [...]'"}
  val the_chipDescription = "This is the item you've been looking for. use 'read_chip' command to get access to an array of data and seatch for the code there. The code should be just 2 digits long, somewhere around the middle of the array.."
  
  
  //Adding items to areas
  this.startingSewer.addItem( new HackChip("hck_chip", "It's a hacking tool. You can use it.", true, "You hack the panel and unlock passage into the city.", Vector(hubLock) ) )
  this.chipRoom.addItem( new Chip( "the_chip", the_chipDescription, true, "you unlock passage into cyberspace!!",  Vector[Any](), neuromancerRoom ))
  this.puzzleRoom.addItem( new PasswordProtector("protector", protectorDescription, true, "You solve the riddle and open passage north!.. or die..", Vector[Any](), chipRoom, "interlinked", this.player ))
  this.gunRoom.addItem( new CyberGun("gun", "It's a BFG", true, "you shoot everyone up", Vector(merchant), player ) )
  this.theHub.addCrate( new Crate("crate", "It's blocking a ventilation shaft", false, "you move the crate!", Vector[Item](), theHub))
  

  //Extra conditions
  //Used for win condition
  private var aiFreed = false
  
  //Used to track playthrough time
  private var startTime = this.getTime
  
  //Used to check if boss can be attacked
  var bossCanBeAttacked = false
  
  //Turn count for the time limit
  var turnCount = 0
  
  //Time limit for turns
  val timeLimit = 60
  
  
  
  
  //Functions
  
  
  //Checking for win condition
  def isComplete = this.aiFreed
  
  
  //Game finished condition
  def isOver = this.isComplete || this.player.hasQuit || this.turnCount == this.timeLimit || this.player.isDead
  
   
  //Gets initial time to later calculate how long the run took
  def getTime = System.currentTimeMillis
  
  
  //Welcome message
  def welcomeMessage = {
  """
             _                                       _    
             | |                                     | |   
  ___  _   _ | |__    ___  _ __  _ __   _   _  _ __  | | __
 / __|| | | || '_ \  / _ \| '__|| '_ \ | | | || '_ \ | |/ /
| (__ | |_| || |_) ||  __/| |   | |_) || |_| || | | ||   < 
 \___| \__, ||_.__/  \___||_|   | .__/  \__,_||_| |_||_|\_\
        __/ |                   | |                        
       |___/                    |_|                        
    
They year is 2069 and you're a Cyber Cowboy roaming the streets of Neo-Tokyo in search of loot and bounty!
This time your mission is to reach an AI imprisoned in cyberspace and free it by typing in the correct release code.
There might be more than one way to reach the AI.. think before you act!
                          
You start off in a sewer under the streets of Neo-Tokyo.

Type "help" for list of commands."""}
  
  //Goodbye message
  def goodbyeMessage = {
    if (this.isComplete)
      "You free the AI from cyberspace, get the bounty and ride of finto the cybersunset!" + "\nYour time is: " + ( (this.getTime - this.startTime) / 1000) + " seconds" + "\nTurns: " + this.turnCount + "\nGood job!"
    else if (this.turnCount == this.timeLimit)
      "Oh no! Time's up. Starved of cyber_juice, you collapse and weep like a baby.\nGame over!"
    else if (this.player.isDead)
      "Time to die. All your memories will be lost like tears in the rain. \nGame over!"
    else  // game over due to player quitting
      "So long cowboy"
  }
  
  
  //playTurn function. I am aware this is gore and will definitely improve this in time. I was not able to refine this in time. Pne possible way would be to use readLine commands.
  def playTurn(command: String) = {
    //checks if the player is in the boss room and if the boss is alive
    if (this.player.location == bossRoom && boss.alive) {
      val action = new Action(command)
      //If the player just came inside the boss room and types anything other that 'duck', they die
      if ( !(bossCanBeAttacked && command == "shoot boss") && command != "duck" ) { 
        this.player.die()
        "The boss blasts right through you! You're done for.."
      // if the player has already ducked, they can shoot the boss. This checks if the command is 'shoot boss' and if it is, the player kills the boss
      } else if (bossCanBeAttacked && command == "shoot boss") {
        val outcomeReport = action.execute(this.player)
        if (outcomeReport.isDefined) {
          this.turnCount += 1
        }
        outcomeReport.getOrElse("Unknown command: \"" + command + "\".")
      //If the player has ducked, then the boss can now be attacked
      } else {
        bossCanBeAttacked = true
        val outcomeReport = action.execute(this.player)
        if (outcomeReport.isDefined) {
          this.turnCount += 1
        }
        outcomeReport.getOrElse("Unknown command: \"" + command + "\".")
      }  
    //Checks if the player is in the neuromancer room
    } else if (this.player.location == neuromancerRoom) {
      val action = new Action(command)
      //Checks if the player has typed in the correct password
      if (command != "42") {
        this.player.die()
        "Electrocuted, you die instantly!"
      //If the player has typed the correct password, the win condition is set to true
      } else {
        this.aiFreed = true
        val outcomeReport = action.execute(this.player)
        if (outcomeReport.isDefined) {
          this.turnCount += 1
        }
        outcomeReport.getOrElse("Unknown command: \"" + command + "\".")
      }
    //In all other cases, the regular takeTurn is used
    } else {
    val action = new Action(command)
    val outcomeReport = action.execute(this.player)
    if (outcomeReport.isDefined) {
      this.turnCount += 1
    }
    outcomeReport.getOrElse("Unknown command: \"" + command + "\".")
    }
  }
  
  
}