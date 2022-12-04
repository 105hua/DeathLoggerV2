package com.nearvanilla.deathlogger // Package name.

// Imports.
import com.nearvanilla.deathlogger.commands.InvCallback
import com.nearvanilla.deathlogger.commands.ShowDeaths
import com.nearvanilla.deathlogger.events.EntityDeath
import org.bukkit.plugin.java.JavaPlugin
import java.sql.DriverManager

// Class.
class DeathLogger : JavaPlugin() {

    companion object{ // Companion object so that other classes can access plugin.

        var instance: JavaPlugin? = null // Create JavaPlugin var for the instance.
        private set // Create a private set for reassignment.

    } // Closing of companion object.

    private val connection = DriverManager.getConnection("jdbc:sqlite:".plus(this.dataFolder.absolutePath).plus("/data.db")) // Create database connection. Will also ensure it exists.
    private val stmt = connection.createStatement() // Create statement from connection.

    override fun onEnable() { // Plugin Enable Logic.

        this.logger.info("DeathLogger is now initialising...")

        /*

        SET COMPANION OBJECTS

         */

        instance = this // Reassign instance variable to this.

        this.logger.info("Companion Objects set.")

        /*

        INIT CODE

         */

        val command = "CREATE TABLE IF NOT EXISTS inventories(uuid TEXT PRIMARY KEY, inventory TEXT NOT NULL);" // Create command to create inventories if one doesn't exist.
        stmt.execute(command)

        this.logger.info("Ensured inventories table exists in database.")

        /*

        REGISTER EVENTS

         */

        server.pluginManager.registerEvents(EntityDeath(), this)
        this.logger.info("Events registered.")

        /*

        REGISTER COMMANDS

         */

        this.getCommand("callback")?.setExecutor(InvCallback())
        this.getCommand("showdeaths")?.setExecutor(ShowDeaths())

        /*

        INIT COMPLETE

         */

        this.logger.info("DeathLogger is now enabled.") // Send a message indicating that the plugin is enabled.

    } // Closing of override.

    override fun onDisable() { // Plugin Disable Logic.

        this.logger.info("DeathLogger is now disabled.") // Send a message indicating that the plugin is disabled.

    } // Closing of override.

} // Closing of class.