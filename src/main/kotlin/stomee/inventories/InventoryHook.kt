package stomee.inventories

import net.minestom.server.entity.ItemEntity
import net.minestom.server.entity.Player
import net.minestom.server.event.item.ItemDropEvent
import net.minestom.server.event.item.PickupItemEvent
import net.minestom.server.utils.time.TimeUnit
import world.cepi.kstom.addEventCallback

fun hook(player: Player) {
    player.addEventCallback<ItemDropEvent> {

        val pos = player.position.clone().add(.0, player.eyeHeight, .0)

        val entity = ItemEntity(itemStack, pos, player.instance!!)

        entity.setInstance(player.instance!!, pos)

        entity.setPickupDelay(30, TimeUnit.TICK)

        entity.velocity.add(player.position.direction.clone().normalize().multiply(6))
    }

    player.addEventCallback<PickupItemEvent> {
        player.inventory.addItemStack(itemEntity.itemStack)
    }
}