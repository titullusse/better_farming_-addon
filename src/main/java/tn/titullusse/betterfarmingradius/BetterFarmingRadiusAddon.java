package tn.titullusse.betterfarmingradius;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;

import tn.titullusse.betterfarmingradius.config.RadiusAddonConfig;

/**
 * Addon for the "Better Farming ++" mod (better_farming_plus).
 * <p>
 * The base mod harvests crops around a Farming Crate using a radius that is
 * hard-coded inside {@code FarmingCrateOnTickUpdateProcedure}. This addon makes
 * that radius configurable: a Mixin replaces the hard-coded harvest loop with a
 * radius read from this mod's config file.
 */
@Mod(BetterFarmingRadiusAddon.MODID)
public class BetterFarmingRadiusAddon {
	public static final String MODID = "better_farming_radius";
	public static final Logger LOGGER = LogManager.getLogger(BetterFarmingRadiusAddon.class);

	public BetterFarmingRadiusAddon(IEventBus modEventBus, ModContainer modContainer) {
		modContainer.registerConfig(ModConfig.Type.COMMON, RadiusAddonConfig.SPEC);
		LOGGER.info("Better Farming ++ configurable crate radius addon loaded.");
	}
}
