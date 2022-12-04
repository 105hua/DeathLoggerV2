package com.nearvanilla.deathlogger.events // Package name.

// Imports.
import com.nearvanilla.deathlogger.lib.PlayerDatabaseHandler
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.TextComponent
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.entity.EntityDeathEvent
import org.bukkit.event.Listener
import java.text.DecimalFormat

class EntityDeath : Listener{ // Class entitydeath will inherit listener, as it is an event class.

    private val formatter = DecimalFormat("#.###") // Create the decimal formatter.

    @EventHandler // Indicate an event handler.
    fun onEntityDeath(e : EntityDeathEvent){ // Entity Death function which takes entity death event.

        if(e.entity is Player){ // If entity that died is player.

            val player = e.entity as Player // Create player val with entity as player.

            val xCoord = formatter.format(player.location.x) // Format x coord.
            val yCoord = formatter.format(player.location.y) // Format y coord.
            val zCoord = formatter.format(player.location.z) // Format z coord.

            val diedComponent: TextComponent = Component.text("You Died!", NamedTextColor.RED, TextDecoration.BOLD) // Create died component.
            val coordComponent: TextComponent = Component.text("Coordinates: $xCoord, $yCoord, $zCoord", NamedTextColor.GOLD) // Create coord component.
            val worldComponent: TextComponent = Component.text("World: ${player.world.name}", NamedTextColor.AQUA) // Create world component.

            player.sendMessage(diedComponent) // Send died component to player.
            player.sendMessage(coordComponent) // Send coord component to player.
            player.sendMessage(worldComponent) // Send world component to player.

            PlayerDatabaseHandler.setupPlayer(player.uniqueId) // Setup player.
            PlayerDatabaseHandler.insertDeathLocation(player.uniqueId, player.location.x.toString(), player.location.y.toString(), player.location.z.toString(), player.world.name, player.inventory) // Insert players death location and world name.

        } // End of if statement.

    } // End of event function.

} // End of class.