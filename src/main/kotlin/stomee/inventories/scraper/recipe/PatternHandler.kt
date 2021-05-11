package stomee.inventories.scraper.recipe

import net.minestom.server.item.ItemStack
import net.minestom.server.item.Material
import net.minestom.server.network.packet.server.play.DeclareRecipesPacket
import org.json.JSONArray
import org.json.JSONObject
import stomee.inventories.getItemByNamespace

internal class PatternHandler(private val array: JSONArray, private val key: JSONObject) {

    fun process(): List<DeclareRecipesPacket.Ingredient> {

        // Find all letters, ex a crafting table would be: ####
        val letters = (0 until array.length())
            .joinToString("") { array.getString(it) }
            .split("")
            .filter(String::isNotEmpty)

        // Find any unique letters and pair them to their respective material (crafting table: # to (tag:planks))
        val distinctLettersToMaterials: Map<String, List<Material>> = letters.distinct()
            .filter { it != " " }
            .map {

                // TODO handle tags

                return@map when (key.get(it)) {
                    is JSONObject -> {

                        if (!key.getJSONObject(it).has("item")) return@map null

                        it to listOf((getItemByNamespace(key.getJSONObject(it).getString("item")) ?: return@map null))
                    }

                    is JSONArray -> it to key.getJSONArray(it).mapNotNull { arr ->
                        val arrayEntryObject = arr as JSONObject

                        if (!arrayEntryObject.has("item")) return@map null

                        getItemByNamespace(arrayEntryObject.getString("item"))
                    }

                    else -> null
                }
            }.filterNotNull().toMap()

        // Map them to an Ingredient class
        return letters.map { value ->
            DeclareRecipesPacket.Ingredient().let {
                if (value == " ") it.items = arrayOf()
                else it.items = distinctLettersToMaterials[value]?.map { mat -> ItemStack.of(mat) }?.toTypedArray() ?: emptyArray()

                return@let it
            }
        }

    }

}