package stomee.inventories.scraper

import net.minestom.server.entity.Player
import net.minestom.server.item.ItemStack
import net.minestom.server.item.Material
import net.minestom.server.network.packet.server.play.DeclareRecipesPacket
import net.minestom.server.recipe.Recipe
import net.minestom.server.recipe.ShapedRecipe
import net.minestom.server.recipe.ShapelessRecipe
import org.json.JSONObject
import stomee.inventories.getItemByNamespace
import world.cepi.kstom.Manager
import java.nio.file.Files
import kotlin.io.path.Path
import kotlin.io.path.isDirectory
import kotlin.io.path.name
import kotlin.io.path.readText

object RecipeScraper {

    val path = Path("minecraft_data/data/minecraft/recipes/")

    fun init() = Files.walk(path)
        .filter { !it.isDirectory() }
        .forEach { recipeFile ->
            val recipeOBJ = JSONObject(recipeFile.readText())

            val typeString = recipeOBJ.getString("type")

            val type = RecipeType.values().firstOrNull { it.generateID() == typeString } ?: return@forEach

            when (type) {
                RecipeType.CRAFTING_SHAPED -> {

                    val pattern = recipeOBJ.getJSONArray("pattern")
                    val result = recipeOBJ.getJSONObject("result")

                    Manager.recipe.addRecipe(
                        object : ShapedRecipe(
                            recipeFile.name.dropLast(5),
                            pattern.length(),
                            pattern.getString(0).length,
                            "group",
                            PatternHandler(pattern, recipeOBJ.getJSONObject("key")).process(),
                            ItemStack.of(
                                Material.PAPER,
                                if (result.has("count")) result.getInt("count") else 1
                            )
                        ) {
                            override fun shouldShow(player: Player) = true
                        }
                    )
                }

                RecipeType.CRAFTING_SHAPELESS -> {

                    val ingredients = recipeOBJ.getJSONArray("ingredients")
                    val result = recipeOBJ.getJSONObject("result")

                    Manager.recipe.addRecipe(
                        object: ShapelessRecipe(
                            recipeFile.name.dropLast(5),
                            "group",
                            ingredients.map { obj ->
                                val parsedObject = obj as? JSONObject ?: return@map null

                                DeclareRecipesPacket.Ingredient().let {
                                    if (!parsedObject.has("item")) return@map null
                                    it.items = arrayOf(ItemStack.of(
                                        getItemByNamespace(parsedObject.getString("item")) ?: return@map null
                                    ))

                                    return@let it
                                }
                            }.filterNotNull(),
                            ItemStack.of(
                                Material.PAPER,
                                if (result.has("count")) result.getInt("count") else 1
                            )
                        ) {
                            override fun shouldShow(player: Player) = true
                        }
                    )

                }
            }

    }

}
