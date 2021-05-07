package stomee.inventories

import net.minestom.server.extensions.Extension
import world.cepi.kstom.Manager

class Inventories : Extension() {

    override fun initialize() {

        Manager.connection.addPlayerInitialization(::hook)

        logger.info("[Inventories] has been enabled!")
    }

    override fun terminate() {

        Manager.connection.removePlayerInitialization(::hook)

        logger.info("[Inventories] has been disabled!")
    }

}