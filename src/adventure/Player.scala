package adventure
import scala.collection.mutable.Map


class Player(startingArea: Area) {
  private var currentLocation = startingArea        
  private var quitCommandGiven = false              
  private var itemList = Map[String, Item]()
  
  //Vals and Vars
  
  //Alive status of the player
  private var alive = true
  
  
  //Functions
  
  //Help function
  def help = {
    """You are ina cyberworld, your goal is to rescue an AI from the cyberprison by typing in the proper code inside the cyberspace.
There is more than one way to reach the final obejctive.
There are dangers along the way that might kill you.
       
       Commands available (there are some secret commands not listed here):
       
       "go [direction]"           => attempt to go in a direction
       "rest"                     => rest for a bit
       "quit"                     => leave the game
       "get [item_name]"          => pick up an item for later use. The item is added to your inventory.
       "drop [item_name]"         => remove item from your inventory and drop it in the area you are in
       "examine [item_name]"      => look closely at an item that is in your inventory
       "inventory"                => list all the items that you have
       "use [item_name]"          => use the item if possible
       "shoot [character_name]"   => if you have a gun, shoot an NPC
       "talk_to [character_name]" => try to talk to an NPC
       "sell [item_name]"         => sell an item to a merchant in the area
       "move [object_name]"       => moves an object that can't be picked up but can be moved
       "crawl"                    => go through a crawlspace into a different area
       "duck"                     => take cover from the fire
       "help"                     =>  >:(
       "read_chip"                =>  If you're holding the_chip, use this to read its contents
       "hint"                     =>  Shows walkthough of the area you are in right now"""  
  }
  
  //Hint function that explains exactly what to do at each area
  def hint: String = {
    this.location.hint
  }
  
  //Kills the player
  def die() = this.alive = false
  
  //The player is dead
  def isDead = !this.alive
  
  //Duck from the bullets
  def duck = {
    "You duck, good job!"
  }
  
  //Code to free the AI at the end of the game
  def fourtwenty = {
    "I AM FREE"
  }
  
  //Crawl through an open crawlspace. Can be significantly improved.
  def crawl(): String = {
    if (this.location.crawlspaceStatus) {
      this.location.neighbor("east") match {
        case Some(location) => {
          location.unlock()
          go("east")
          "You crawl through and unlock the barred door from the inside!"
        }
        case None => "No free crawlspace to crawl through!"
      }
    } else {
      "No free crawlspace to crawl through!"
    }
  }
  
  //Move a crate to free up a crawlspace
  def move(crateName: String): String = {
    if(this.location.containsCrate(crateName) ) {
      this.location.moveCrate(crateName)
      "You move the crate and a small ventilation shaft that canbe crawled through opens up! \nYou can now crawl."
    } else {
      "No such crate to move!"
    }
  }
  
  //Attempt to talk to an NPC
  def talk_to(characterName: String): String = {
    if (this.location.containsNPC(characterName) ) {
      characterName + ": " + this.location.respondNPC(characterName)
    } else {
      "No such NPC to tak to"
    }
  }
  
  //Attempt t sell an item to a merchant in the area
  def sell(itemName: String): String = {
    if (this.location.containsNPC("merchant") && this.has("gun") ) {
      this.itemList -= itemName
      this.location.neighbor("west") match {
        case Some(value) => value.unlock()
        case None => None
      }
      "You sell the gun to the merchant and in exchange he unlocks the door to the west room!"
    } else {
      "the merchant wants a gun!"
    }
  }
  
  //Shoot and kill a specific NPC if you have a gun
  def shoot(characterName: String): String = {
    if(this.location.containsNPC(characterName) && this.has("gun")) {
       val quote = this.location.getNPCKillQuote(characterName)
       this.location.killNPC(characterName)
       "you shoot up the " + characterName + ": " + quote
    } else {
      "no such NPC to shoot up or u don't have anything to shoot with"
    }
  }  
  
  //Add an item to a player's itemlist
  def giveItem(item: Item) = {
    if(item.canBePicked) {
      this.itemList += item.name -> item
    }
  }
  
  //Pick up an item
  def get(itemName: String): String = {
    if ( this.currentLocation.contains(itemName) ) {
      this.currentLocation.removeItem(itemName) match {
        case Some(item) => if (item.canBePicked) this.itemList += itemName -> item
        case None => "This item can't be picked up"
      }
      "You pick up the " + itemName + "."
    } else {
      "There is no " + itemName + " here to pick up."
    } 
  }
  
  //Drop an item off in an area
  def drop(itemName: String): String = {
    if( this.itemList.contains(itemName) ) {
      this.currentLocation.addItem(this.itemList(itemName))
      this.itemList -= itemName
      "You drop the " + itemName + "."
    } else {
      "You don't have that!"
    }
  }
  
  //Look closely at an item you are holding
  def examine(itemName: String): String = {
     if( this.itemList.contains(itemName) ) {
       "You look closely at the " + itemName + ".\n" + this.itemList(itemName).description
     } else {
       "If you want to examine something, you need to pick it up first."
     }
  }
  
  //Checks if the player has a specific item
  def has(itemName: String): Boolean = this.itemList.contains(itemName)
  
  def inventory: String = {
    if( !this.itemList.isEmpty  ){
      "You are carrying:\n" + this.itemList.keys.mkString("\n")
    } else {
      "You are empty-handed." 
    }
  }
  
  //Has the player quit
  def hasQuit = this.quitCommandGiven
  
  //The player's location
  def location = this.currentLocation
  
  //Attempt to go in a direction
  def go(direction: String) = {
    val destination = this.location.neighbor(direction)
    if(destination.isDefined && !destination.get.locked) {
      this.currentLocation = destination.getOrElse(this.currentLocation)
    }
    if (destination.isDefined && !destination.get.locked) "You go " + direction + "." else "You can't go " + direction + "."
  }
  
  //Attempt to use an item that you have
  def use(itemName: String): String = {
    if (this.has(itemName) ) {
      this.itemList(itemName).itemAction()
      "You use the " + itemName + ": " + this.itemList(itemName).actionDescription
    } else {
      "can't be done"
    }
  }
  
  //Read the data stored on the_chip if you have it
  def readChip(): String = {
    if (this.has("the_chip") ) {
      this.itemList("the_chip").itemSecondAction()
    } else {
      "you don't have the chip"
    }
  }
  
  //Chill
  def rest() = {
    "You smoke a cigarette and ponder your past choices for a while. Better get a move on, though."
  }
  
  //Leave the game
  def quit() = {
    this.quitCommandGiven = true
    ""
  }
  
  override def toString = "Now at: " + this.location.name
}