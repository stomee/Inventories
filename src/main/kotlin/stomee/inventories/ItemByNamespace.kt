package stomee.inventories

import net.minestom.server.item.Material

fun getItemByNamespace(namespace: String) =
    Material.values().firstOrNull { material -> namespace == material.getName() }