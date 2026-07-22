package tn.titullusse.betterfarmingradius.client;

import net.neoforged.fml.ModContainer;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

/**
 * Client-only registration of the in-game config screen.
 * <p>
 * Registering an {@link IConfigScreenFactory} is what makes the "config" button
 * appear next to this mod in the Mods list (vanilla NeoForge mod list as well as
 * Better ModList / Mod Menu-style screens). NeoForge's built-in
 * {@link ConfigurationScreen} auto-builds the GUI from the mod's config spec.
 * <p>
 * This class references client-only classes, so it must never be loaded on a
 * dedicated server.
 */
public final class ClientConfigScreen {

	private ClientConfigScreen() {
	}

	public static void register(ModContainer container) {
		container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
	}
}
