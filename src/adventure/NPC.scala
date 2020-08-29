package adventure

import scala.collection.mutable.Buffer

//A standard class of an NPC
class NPC(val name: String, var alive: Boolean, val response: String, val deathText: String, val inventory: Map[String, Item], val location: Area )  {
  
  def kill() = {
    val itemBuffer = Buffer[Item]()
    this.inventory.toVector.foreach( itemBuffer += _._2 )
    itemBuffer.foreach(a => this.location.addItem(a) )
    this.alive = false
    
  }
  
  def killAction() = {
  }
  
  def action() = {
    
  }
  
}

//A thug that unlocks the location he's guarding when killed.
class Thug(name: String, alive: Boolean, response: String, val locationGuarding: Area, deathText: String, inventory: Map[String, Item], location: Area) extends NPC(name, alive, response, deathText, inventory, location) {
  
  override def killAction() = {
    this.locationGuarding.unlock()
  }
  
}