package stomee.inventories.scraper.recipe

enum class RecipeType {

    BLASTING,
    CAMPFIRE_COOKING,
    CRAFTING_SHAPED,
    CRAFTING_SHAPELESS,
    SMELTING,
    SMITHING,
    SMOKING,
    STONECUTTING;

    fun generateID() =
        "minecraft:${name.lowercase()}"

}