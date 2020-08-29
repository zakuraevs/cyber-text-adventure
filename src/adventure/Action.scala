package adventure

class Action(input: String) {
  
  private val commandText = input.trim.toLowerCase
  private val verb        = commandText.takeWhile( _ != ' ' )
  private val modifiers   = commandText.drop(verb.length).trim
  
  def execute(actor: Player) = this.verb match {
    case "go"    => Some(actor.go(this.modifiers))
    case "rest"  => Some(actor.rest())
    case "xyzzy" => Some("The grue tastes yummy.")
    case "quit"  => Some(actor.quit())
    case "get" => Some(actor.get(this.modifiers))
    case "drop" => Some(actor.drop(this.modifiers))
    case "examine" => Some(actor.examine(this.modifiers))
    case "inventory"  => Some(actor.inventory)
    case "use" => Some(actor.use(this.modifiers))
    case "shoot" => Some(actor.shoot(this.modifiers))
    case "talk_to" => Some(actor.talk_to(this.modifiers))
    case "sell" => Some(actor.sell(this.modifiers))
    case "move" => Some(actor.move(this.modifiers))
    case "crawl" => Some(actor.crawl())
    case "duck" => Some(actor.duck)
    case "42" => Some(actor.fourtwenty)
    case "help" => Some(actor.help)
    case "read_chip" => Some(actor.readChip() )
    case "hint" => Some(actor.hint)
    case other   => None
  }
  
  override def toString = this.verb + " (modifiers: " + this.modifiers + ")"
  
}