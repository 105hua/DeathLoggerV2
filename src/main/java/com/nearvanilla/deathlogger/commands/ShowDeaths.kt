package com.nearvanilla.deathlogger.commands // Package name.

// Imports.
import com.nearvanilla.deathlogger.DeathLogger
import com.nearvanilla.deathlogger.lib.PlayerDatabaseHandler
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

// Class code.
class ShowDeaths : CommandExecutor{

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>?): Boolean {

        val plugin = DeathLogger.instance
        val server = plugin?.server

        if(sender is Player){

            val target = args?.get(0) // Get first argument, if one exists.
            var targetPlayer: Player = sender // Create variable for target player and initially define as sender.

            if(target != null){ // If target is not null.

                val search = server?.getPlayer(target) // Attempt to find player by name.

                if(search != null){ // If search is not null.

                    targetPlayer = search // Reassign target player to player found in search.

                } // Closing of if statement.

            } // Closing of if statement.

            PlayerDatabaseHandler.getDeathLocations(targetPlayer) // Get and print death locations of target player.
            return true // Return true, indicating command was used properly.

        }else{ // In any other case.

            var invalidSenderComponent = Component.text("This command can only be ran by players!", NamedTextColor.RED, TextDecoration.BOLD) // Create component that tells sender that command is for players only.
            sender.sendMessage(invalidSenderComponent) // Send component to sender.
            return true // Return true, indicating command was used properly.

        } // Closing of if-else statement.

        return false // Return false, indicating command wasn't used properly.

    } // Closing of if statement.

} // Closing of class.