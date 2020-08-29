package adventure
import scala.collection.mutable.Map
import scala.collection.mutable.Buffer

//Each area has a name, a description, a 'locked' status indication if it can be entered and a hint that can be called witha  hint command
class Area(var name: String, var description: String, var locked: Boolean, val hint: String) {
  
  
  
  //vals, vars
  
  //Neighbors of the area
  private val neighbors = Map[String, Area]()
  
  //Items inside the area
  private val items = Map[String, Item]()
  
  //Locks inside the area
  private val locks = Buffer[Lock]()
  
  //Crates inside the area
  private val crates = Buffer[Crate]()
  
  //NPCs inside the area
  private var NPCs = Buffer[NPC]()
  
  //Status of the crawlspace that can be crawled through ie can it be crawled through or is something blocking it
  private var crawlspaceOpen = false
  
  
  
  
  //Functions
  
  
  //Functions that get the value of the private var
  def crawlspaceStatus: Boolean = this.crawlspaceOpen
  def npcsList: Buffer[NPC] = this.NPCs
  
  
  //Functions connected to items
  
  //Adds an item to the area
  def addItem(item: Item): Unit = {
    this.items += item.name -> item
  }
  
  //Removes an item from the area
  def removeItem(itemName: String): Option[Item] = {
    val returnValue: Option[Item] = this.items.get(itemName)
    returnValue match {
      case Some(value) => this.items.remove(itemName)
      case None => None
    }
    returnValue
  }
  
  //Checks if an area contains a specific item
  def contains(itemName: String): Boolean = {
    this.items.contains(itemName)
  }
  
  
  //Functions that are connected to crates and crawlspaces
  
  //Opens up the crawlspace
  def openCrawlspace(): Unit = {
    this.crawlspaceOpen = true
  }
  
  //Adds a crate to the area
  def addCrate(crate: Crate): Unit = {
    this.crates += crate
  }
  
  //Checks if this area contains a crate
  def containsCrate(crateName: String): Boolean = {
     this.crates.exists( _.name == crateName)
  }
  
  //Moves the crate, opening up the crawlspace
  def moveCrate(crateName: String): Unit = {
    if( this.containsCrate(crateName) ) {
       this.crates.find( _.name == crateName) match {
         case Some(crate) => crate.itemAction()
         case None => None
       }
    }
  }
  
  
  //Functions that are connected to NPCs
  
  //Adds an NPC to an area
  def addNPC(character: NPC): Unit = {
    this.NPCs += character
  }
  
  //Checks if an area contains an NPC
  def containsNPC(characterName: String): Boolean = {
    this.NPCs.exists( _.name == characterName)
  }
  
  //Gets a string that an NPC responds with when the player talks to them
  def respondNPC(characterName: String): String = {
    this.NPCs.find( _.name == characterName) match {
      case Some(character) => character.response
      case None => "No such NPC to talk to!"
    }
  }
  
  //Gets a string that represents what happens whena  character dies
  def getNPCKillQuote(characterName: String): String = {
    this.NPCs.find( _.name == characterName) match {
      case Some(character) => character.deathText
      case None => "No one was shot"
    }
  }
  
  //Kills all the NPCs in the area
  def killNPCs(): Unit = {
    this.NPCs.foreach( _.killAction() )
    this.NPCs.foreach( _.kill() )
    this.NPCs = Buffer[NPC]()
  }
  
  //Kills a specific NPC in the area
  def killNPC(characterName: String): Unit = {
    this.NPCs.find( _.name == characterName) match {
      case Some(character) => {
        character.kill()
        character.killAction()
        this.NPCs -= character
      }
      case None => None
    }
  }

  
  //Functions connected to accessibility & neighbors of areas
  
  //Unlocks the area, allowing the player to go there
  def unlock(): Unit = {
    this.locked = false
  }
  
  //Adds a lock to the area
  def addLock(lock: Lock): Unit = {
    this.locks += lock
  }
  
  //Gets a neighbor in a specific direction wrapped in an option
  def neighbor(direction: String): Option[Area] = this.neighbors.get(direction)
  
  //Sets a neighbor for an area
  def setNeighbor(direction: String, neighbor: Area) = {
    this.neighbors += direction -> neighbor
  }
  
  //Sets multiple neighbors for an area
  def setNeighbors(exits: Vector[(String, Area)]) = {
    this.neighbors ++= exits
  }
  
  
  //Description of the area
  def fullDescription = {
    val areas = this.neighbors
    val areasWithNoSecret = areas -= "???"
    val npcNames = this.NPCs.map(_.name)
    val npcList = "\nNPCs: " + npcNames.mkString(", ")
    val objectList = "\nOther: " + this.crates.mkString(" ")
    val exitList = "\nExits available: " + areasWithNoSecret.keys.mkString(" ") //this.neighbors
    val itemList = "\n" + "You see here: " + this.items.keys.mkString(" ")
    val separator = "\n---------------------------------------------------------------------"
    //"\nlocks available: " + this.locks.mkString(" ")
    if (!this.crates.isEmpty && this.items.isEmpty) {
      this.description + "\n\n" + npcList + objectList + exitList + separator
    } else if (!this.crates.isEmpty && !this.items.isEmpty) {
      this.description + "\n\n" + npcList + itemList + objectList + exitList + separator
    }  else {
      if (this.NPCs.isEmpty && this.items.isEmpty) {
        this.description + "\n\n" + itemList + exitList + separator
      } else {
       this.description + npcList + itemList  + exitList + separator
      } 
    }
  }

  
  override def toString = this.name + ": " + this.description.replaceAll("\n", " ").take(150)
}