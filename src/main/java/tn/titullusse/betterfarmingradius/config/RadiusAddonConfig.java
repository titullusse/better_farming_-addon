package tn.titullusse.betterfarmingradius.config;

import net.neoforged.neoforge.common.ModConfigSpec;

/**
 * Config for the configurable Farming Crate radius.
 * <p>
 * {@code harvest_radius} is the number of blocks scanned in every horizontal
 * direction around the crate. A value of {@code r} scans a {@code (2r + 1)}x
 * {@code (2r + 1)} square centred on the crate. The base mod used an equivalent
 * radius of 3, which is kept as the default here.
 */
public final class RadiusAddonConfig {
	public static final ModConfigSpec SPEC;
	public static final ModConfigSpec.IntValue HARVEST_RADIUS;

	private static final int DEFAULT_RADIUS = 3;
	private static final int MIN_RADIUS = 0;
	private static final int MAX_RADIUS = 32;

	static {
		ModConfigSpec.Builder builder = new ModConfigSpec.Builder();
		builder.comment("Better Farming ++ - Farming Crate radius settings").push("farming_crate");
		HARVEST_RADIUS = builder
				.comment(
						"Action radius (in blocks) of the Farming Crate.",
						"The crate scans a (2 * radius + 1) x (2 * radius + 1) square centered on itself.",
						"Default is 3 (a 7x7 area). Increase for a wider harvest area.")
				.defineInRange("harvest_radius", DEFAULT_RADIUS, MIN_RADIUS, MAX_RADIUS);
		builder.pop();
		SPEC = builder.build();
	}

	private RadiusAddonConfig() {
	}

	/**
	 * @return the configured harvest radius, or the default if the config has not
	 *         been loaded yet.
	 */
	public static int getRadius() {
		if (!SPEC.isLoaded()) {
			return DEFAULT_RADIUS;
		}
		return HARVEST_RADIUS.get();
	}
}
