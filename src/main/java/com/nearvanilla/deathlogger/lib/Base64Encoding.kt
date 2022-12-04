package com.nearvanilla.deathlogger.lib

import org.bukkit.Bukkit
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import org.bukkit.util.io.BukkitObjectInputStream
import org.bukkit.util.io.BukkitObjectOutputStream
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.IOException

class Base64Encoding {

    companion object{

        /*

        Converts a players inventory to a base 64 string.

         */
        fun toBase64(inv : Inventory): String? { // Opening of function.

            try{ // Enter try.

                var outputStream = ByteArrayOutputStream() // Create new byte array output stream.
                var dataOutput = BukkitObjectOutputStream(outputStream) // Create new bukkit object output stream with outputStream.

                dataOutput.writeInt(inv.size) // Write inventory size to data output.

                for(item in inv.contents){ // For each item in inventory contents.

                    dataOutput.writeObject(item) // Write object to data output.

                } // Closing of for loop.

                dataOutput.close() // Close the bukkit object output stream.

                return Base64Coder.encodeLines(outputStream.toByteArray()) // Return the encoded lines from the output stream converted to a byte array.

            }catch(e : Exception){ // If error, catch error as exception and do something.

                throw IllegalStateException("Unable to save ItemStacks.", e) // Throw illegal state exception as unable to save item stacks, along with exception.

            } // Closing of try-catch.

        } // Closing of function.

        /*

        This function decodes a base64 string to a player inventory.

         */
        fun fromBase64(data : String) : Inventory? {

            try{ // Enter a try.

                val inputStream = ByteArrayInputStream(Base64Coder.decodeLines(data)) // Create a byte array input stream, decode the lines in the data string and pass it.
                var dataInput = BukkitObjectInputStream(inputStream) // Create a bukkit object input stream with the byte array input stream.
                var inv = Bukkit.getServer().createInventory(null, dataInput.readInt()) // Create an inventory with the size from the data.
                var itemCount = 0 // Create a variable that stores the index of the item in the array that the program is on.

                for(item in inv.contents){ // Enter a for loop for each item in the inventories contents.

                    inv.setItem(itemCount, dataInput.readObject() as ItemStack) // Set the item stored in the current index to the next object read in the datainput as an item stack.
                    itemCount += 1 // Increment item count by 1.

                } // Closing of for loop.

                dataInput.close() // Close the bukkit object input stream.
                return inv // Return the inventory.

            }catch(e : ClassNotFoundException){ // If error, catch error as class not found exception and do something.

                throw IOException("Unable to decode class type.", e) // Throw an IOException as class couldn't be decoded with the class not found exception caught.

            } // Closing of try-catch.

        } // Closing of function.


    } // Closing of companion object.

} // Closing of class.