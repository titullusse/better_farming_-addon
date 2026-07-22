package tn.titullusse.betterfarmingradius.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.world.level.LevelAccessor;

import tn.titullusse.betterfarmingradius.config.RadiusAddonConfig;
import tn.titullusse.betterfarmingradius.harvest.RadiusHarvest;

/**
 * Replaces the hard-coded harvest loop of the base mod's
 * {@code FarmingCrateOnTickUpdateProcedure} with a radius read from this addon's
 * config.
 * <p>
 * The target class belongs to another mod (better_farming_plus), so it is
 * referenced by name via {@code targets} and everything is left unmapped
 * ({@code remap = false}).
 */
@Mixin(targets = "tn.nightbeam.betterfarmingplus.procedures.FarmingCrateOnTickUpdateProcedure", remap = false)
public class FarmingCrateOnTickUpdateMixin {

	@Inject(method = "execute", at = @At("HEAD"), cancellable = true, remap = false)
	private static void betterFarmingRadius$configurableHarvest(LevelAccessor world, double x, double y, double z,
			CallbackInfo ci) {
		RadiusHarvest.execute(world, x, y, z, RadiusAddonConfig.getRadius());
		ci.cancel();
	}
}
