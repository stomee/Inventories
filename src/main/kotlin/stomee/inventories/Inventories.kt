package stomee.inventories

import net.minestom.server.extensions.Extension;

class Inventories : Extension() {

    override fun initialize() {
        logger.info("[Inventories] has been enabled!")
    }

    override fun terminate() {
        logger.info("[Inventories] has been disabled!")
    }

}