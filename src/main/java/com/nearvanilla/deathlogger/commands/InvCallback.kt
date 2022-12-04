package com.nearvanilla.deathlogger.commands // Package name.

// Imports.
import com.nearvanilla.deathlogger.DeathLogger
import com.nearvanilla.deathlogger.lib.Base64Encoding
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.inventory.Inventory

class InvCallback : CommandExecutor {

    companion object{ // Declare companion object.

        private var callbackNames: MutableList<String> = mutableListOf() // Create empty list for callback names
        private var callbackInventories: MutableList<String> = mutableListOf() // Create empty list for callback inventory bases.
        private var callbackPlayers: MutableList<Player> = mutableListOf() // Create empty list for callback inventory owners.

        /*

        This function is used to add a callback to this class for later use.

         */
        fun addCallback(name: String, inv: Inventory, owner: Player){ // Opening of function.

            val instance = DeathLogger.instance // Get plugin instance.
            val pluginLogger = instance?.logger // Get plugin logger.

            callbackNames.add(name) // Add name to callback names list.

            val inventoryBase = Base64Encoding.toBase64(inv) // Encode input inventory to base64.

            if(inventoryBase == null){ // If inventory base is null.

                pluginLogger?.info("Add callbacks inventory base returns null.") // Send message to console indicating null inventory base.
                return // Return.

            } // Closing of if statement.

            callbackInventories.add(inventoryBase)
            callbackPlayers.add(owner)

        }

        /*

        This function will remove a callback when it is no longer needed.

         */
        fun removeCallback(name: String){ // Opening of function.

            val instance = DeathLogger.instance // Get plugin instance.
            val pluginLogger = instance?.logger // Get plugin logger.

            val nameIndex = callbackNames.indexOf(name) // Search for index of name in callback names.

            if(nameIndex == -1){ // If name index is -1 then no index found.

                pluginLogger?.info(name.plus(" was not found in the list.")) // Send message to console indicated that name couldn't be found in list.
                return // Return.

            }else{ // In any other case, assume index is found.

                // Remove element with index from all arrays.
                callbackNames.removeAt(nameIndex)
                callbackInventories.removeAt(nameIndex)
                callbackPlayers.removeAt(nameIndex)

            } // Closing of if statement.

        } // Closing of function.

        /*

        This function will get a callback when it is required.

         */
        fun getCallback(name: String): Array<Any>?{ // Opening of function.

            val instance = DeathLogger.instance // Get plugin instance.
            val pluginLogger = instance?.logger // Get plugin logger.

            val nameIndex = callbackNames.indexOf(name) // Search for index of name in callback names.

            return if(nameIndex == -1){ // If name index is -1 then no index found.

                pluginLogger?.info(name.plus(" was not found in the list.")) // Send message to console indicating that name wasn't found on list.
                null // Return null.

            }else{ // In any other case.

                val inventory = callbackInventories[nameIndex] // Get inventory from callbacks.
                val player = callbackPlayers[nameIndex] // Get player from callbacks.

                arrayOf(name, inventory, player) // Return array of data.

            } // End of if-else statement.

        } // End of function.

    } // Closing of companion object.

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>?): Boolean {

        val plugin = DeathLogger.instance // Get plugin instance.
        val pluginLogger = plugin?.logger // Get logger.

        if(sender !is Player){ // If sender is not player.

            pluginLogger?.info("Only players can execute this command!") // Send message back indicating only players can use command.

        } // Closing of if statement.

        val player = sender as Player // Get sender and convert to player.

        val callbackName = args?.get(0) // Get first argument, if one exists.

        if(callbackName != null){ // If callback name is not null.

            val callback = getCallback(callbackName) // Get callback data from the list, if it exists.

            if(callback != null){ // If callback is not null.

                val callbackInv = Base64Encoding.fromBase64(callback[1] as String) // Get inventory from callback inventory base.
                val callbackOwner = callback[2] as Player // Get owner from callback and convert to Player.

                if(callbackInv != null){ // If both inventory and owner are not null.

                    if(!callbackOwner.isOnline){ // If callback owner is not online.

                        val offlineComponent = Component.text("That player is currently offline!", NamedTextColor.RED, TextDecoration.BOLD) // Create component to tell player that owner is offline.
                        player.sendMessage(offlineComponent) // Send player offline component.

                    }else{

                        val ownerInv = callbackOwner.inventory // Get owners inventory.
                        ownerInv.clear() // Clear owners inventory

                        for(item in callbackInv.contents){ // For each item in the callback inventories contents.

                            if(item != null){ // If the item is not null.

                                ownerInv.addItem(item) // Add the item to the owners inventory.

                            } // Closing of if statement.

                        } // Closing of for loop.

                        val successComponent = Component.text("Inventory successfully restored!", NamedTextColor.GREEN, TextDecoration.BOLD) // Create component to say inventory restoration was successful.
                        player.sendMessage(successComponent) // Send success component to player.

                    } // Closing of if-else statement.

                }else{ // In any other case.

                    val invalidDataComponent = Component.text("There are one or more pieces of invalid data within this callback!", NamedTextColor.RED, TextDecoration.BOLD) // Create component that tells player that there is invalid data in the callback.
                    player.sendMessage(invalidDataComponent) // Send the component to the player.

                } // Closing of if-else statement.

            } // Closing of if statement.

        } // Closing of if statement.

        return false // Return false, meaning command was used incorrectly.

    } // Closing of override.

} // Closing of class.