package com.nearvanilla.deathlogger.lib // Package name.

// Imports.
import com.nearvanilla.deathlogger.DeathLogger
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.entity.Player
import org.bukkit.inventory.PlayerInventory
import java.sql.DriverManager
import java.util.*

/*

My database library to simplify talking to plugin database.

 */
class PlayerDatabaseHandler {

    companion object{

        /*

        This function will set a table up for the UUID if one does not exist already.

         */
        fun setupPlayer(uuid: UUID){

            val logger = DeathLogger.instance?.logger // Get logger from plugin.

            try{ // Enter try.

                val connection = DriverManager.getConnection("jdbc:sqlite:".plus(DeathLogger.instance?.dataFolder?.absolutePath).plus("/data.db")) // Create connection to the database.
                val stmt = connection.createStatement() // Create statement with connection.
                val formattedUUID = uuid.toString().replace("-", "") // Remove dashes from UUID.

                logger?.info("Ensuring $formattedUUID has a table.") // Send message to console indicating that plugin is ensuring that UUID has a table.

                val command = "CREATE TABLE IF NOT EXISTS $formattedUUID (id INTEGER PRIMARY KEY AUTOINCREMENT, deathCoords TEXT, worldName TEXT, encodedInv TEXT);" // Create command with formatted UUID.
                stmt.execute(command) // Execute command.

                logger?.info("$formattedUUID now has a table.") // Send message to console indicating that plugin has ensured that UUID has a table.

            }catch(e : Exception){ // If error, catch error as exception and do something.

                logger?.info("Couldn't create table. Message: ${e.message}") // Send message to console indicating that table couldn't be created and provide error message.

            } // End of try-catch.

        } // End of function.

        /*

        This function inserts the location and world name of UUIDs death into their table.

         */
        fun insertDeathLocation(uuid: UUID, xCoord: String, yCoord: String, zCoord: String, worldName: String, inventory: PlayerInventory){

            val logger = DeathLogger.instance?.logger // Get logger from plugin.
            val formattedUUID = uuid.toString().replace("-", "") // Remove dashes from UUID.

            try{ // Enter try.

                logger?.info("Inserting $formattedUUID's death location...") // Send message to console indicating that formatted uuids death location is being inserted.
                val connection = DriverManager.getConnection("jdbc:sqlite:".plus(DeathLogger.instance?.dataFolder?.absolutePath).plus("/data.db")) // Create connection to the database.
                val stmt = connection.createStatement() // Create statement with connection.
                val fullCoords = "$xCoord, $yCoord, $zCoord" // Concatenate the input coords.
                val encodedInventory = Base64Encoding.toBase64(inventory)
                val command = "INSERT INTO $formattedUUID (deathCoords, worldName, encodedInv) VALUES('$fullCoords', '$worldName', '$encodedInventory');" // Create insert command from formatted UUID, concatenated coords and world name.
                stmt.execute(command) // Execute command.
                logger?.info("Inserted $formattedUUID's death location.") // Send message to console indicating that uuids death location inserted sucessfully.

            }catch(e : Exception){  // If error, catch error as exception and do something.

                logger?.info("Couldn't insert $formattedUUID's death location into their table. Message: ${e.message}") // Send message to console indicating that plugin failed to insert uuids death location into table with error message.

            } // End of try-catch.

        } // End of function.

        /*

        This function gets and displays the last 5 death locations of the input player.

         */
        fun getDeathLocations(player: Player){

            val logger = DeathLogger.instance?.logger // Get the logger from the plugin.
            val formattedUUID = player.uniqueId.toString().replace("-", "") // Remove dashes from UUID.

            try{ // Enter a try.

                logger?.info("Retrieving $formattedUUID's death locations...") // Send message to console indicating that uuids death locations is being retrieved.
                val connection = DriverManager.getConnection("jdbc:sqlite:".plus(DeathLogger.instance?.dataFolder?.absolutePath).plus("/data.db")) // Create connection to the database.
                val stmt = connection.createStatement() // Create statement with connection.
                val command = "SELECT * FROM $formattedUUID LIMIT 5;" // Create command with formatted UUID. Limit records returned to 5.
                val result = stmt.executeQuery(command) // Execute command as query with statement and store results.

                var count = 1 // Create count variable as death location ID from database can't be used.

                val listComponent = Component.text("${player.name}'s Death Locations:", NamedTextColor.YELLOW, TextDecoration.BOLD) // Make list component to indicate players death locations list.
                val separatorComponent = Component.text(" | ", NamedTextColor.GOLD, TextDecoration.BOLD)

                player.sendMessage(listComponent) // Send list component to player as message.

                while(result.next()){ // Enter while loop to iterate through results.

                    // Get variables from result.
                    val deathCoords = result.getString(1) // Get death coords from result.
                    val worldName = result.getString(2) // Get world name from result.

                    /*

                    Coord Component Setup

                     */

                    val formattedCoords = deathCoords.split(", ") // Split the coordinates into a list of three coordinates.
                    val coordsComponent = Component.text(deathCoords, NamedTextColor.GREEN) // Create a component for the coordinates.

                    if(player.hasPermission("deathlogger.mod")){

                        TODO("Add click events if player is moderator.")

                    }

                    count++ // Increment count.

                } // End of while loop.

                logger?.info("$formattedUUID's death locations retrieved.") // Send message to console indicating that uuids death locations retrieved successfully.

            }catch(e : Exception){ // If error, catch error as exception and do something.

                logger?.info("Couldn't retrieve $formattedUUID's death locations. Message: ${e.message}") // Send message to console indicating that uuids death locations couldn't be retrieved with error message.

            } // End of try-catch.

        } // End of function.

    } // End of companion object.

} // End of class.