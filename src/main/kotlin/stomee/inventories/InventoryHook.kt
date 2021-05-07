package stomee.inventories

import net.minestom.server.entity.ItemEntity
import net.minestom.server.entity.Player
import net.minestom.server.event.item.ItemDropEvent
import world.cepi.kstom.addEventCallback

fun hook(player: Player) {
    player.addEventCallback<ItemDropEvent> {
        val entity = ItemEntity(itemStack, player.position, player.instance!!)

        entity.setInstance(player.instance!!, player.position)
    }
}