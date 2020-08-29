package adventure

import scala.util.Random

//Original class Item
class Item(val name: String, val description: String, val canBePicked: Boolean, var actionDescription: String, val canBeUsedOn: Vector[Any] ) {
  
  //Something that an item does when used
  def itemAction(): Unit = {
  }
  
  //Alternative thing an item does when used
  def itemSecondAction(): String = {
    "test"
  }
  
  override def toString = this.name
  
}

//HackChip class used to creat chips that hacked locked areas
class HackChip(name: String, description: String, canBePicked: Boolean, actionDescription: String, canBeUsedOn: Vector[Lock]) extends Item (name, description, canBePicked, actionDescription, canBeUsedOn) {
  
  //Performs itemactions on Locks 
  override def itemAction(): Unit = {
    this.canBeUsedOn.foreach( _.itemAction  )
  }
  
}
  
//Lock class used to create locks that protect areas and that can be unlocked
class Lock(name: String, description: String, canBePicked: Boolean, actionDescription: String, canBeUsedOn: Vector[Item], val locationProtecting: Area) extends Item (name, description, canBePicked, actionDescription, canBeUsedOn) {
  
  //unlocks and area for passage
  override def itemAction(): Unit =  {
    this.locationProtecting.unlock()
  }
  
}

//CyberGun class is the class that the gun belongs to. Can be used to shoot NPCs
class CyberGun(name: String, description: String, canBePicked: Boolean, actionDescription: String, canBeUsedOn: Vector[NPC], val player: Player) extends Item (name, description, canBePicked, actionDescription, canBeUsedOn) {
  
  //Kills every NPC in the area
  override def itemAction(): Unit =  {
    this.player.location.killNPCs
  }
  
}

//Class of crates that block crawlspaces and that can be moved
class Crate (name: String, description: String, canBePicked: Boolean, actionDescription: String, canBeUsedOn: Vector[Item], val location: Area) extends Item (name, description, canBePicked, actionDescription, canBeUsedOn) {
  
  //When moved, a crate opens up a crawlspace
  override def itemAction(): Unit = {
    this.location.openCrawlspace()
  }
}

//Class for the Chip that the player wants to get. The chip stores a random structure of characters with a non-random key in the middle
class Chip(name: String, description: String, canBePicked: Boolean, actionDescription: String, canBeUsedOn: Vector[Any], val locationGuarding: Area) extends Item(name, description, canBePicked, actionDescription, canBeUsedOn) {
  override def itemAction(): Unit = {
    this.locationGuarding.unlock()
  }
  
  //Produces a string of random characters with the password in the middle
  override def itemSecondAction(): String = {  
    var header = "[-------------------------------------------------]\n"
    
    var textChunk1 = ""
    for(i <- 0 to 10){
      val streamOfData = Random.alphanumeric
      streamOfData.take(50).foreach(textChunk1 += _)
      textChunk1 += "\n"
    }

    var textChunk2 = ""
    for(i <- 0 to 10){
      val streamOfData = Random.alphanumeric
      streamOfData.take(50).foreach(textChunk2 += _)
      textChunk2 += "\n"
    }

    val finalChunk = header + textChunk1 + "WuEO8o7LGmTjwjaFyPBS|||42|||FzSDp2B4DZV7N5016jcaHd\n" + textChunk2 + header
    finalChunk
  }
}

//PasswordProtector that prompts the player to type in the password when used. Typing correct password opens up a locked area. Incorrect password kills the player.
class PasswordProtector(name: String, description: String, canBePicked: Boolean, actionDescription: String, canBeUsedOn: Vector[Any], val locationGuarding: Area, val code: String, val player: Player) extends Item(name, description, canBePicked, actionDescription, canBeUsedOn) {
  override def itemAction(): Unit = {
      val response = scala.io.StdIn.readLine("Enter the password: ")
      if (response == this.code.toString) {
        this.locationGuarding.unlock()
      } else {
        this.player.die()
      }
  }
}
  
  

