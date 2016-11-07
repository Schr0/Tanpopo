package schr0.tanpopo.api;

import net.minecraft.item.Item;
import net.minecraft.util.registry.RegistryDefaulted;

public class TanpopoRegistry
{

	private static final RegistryDefaulted<Item, EssenceCauldronCraft> REGISTRY_ESSENCE_CAULDRON_CRAFT = new RegistryDefaulted(null);

	public static void registerRegistryEssenceCauldronCraft(Item key, EssenceCauldronCraft value)
	{
		REGISTRY_ESSENCE_CAULDRON_CRAFT.putObject(key, value);
	}

	public static RegistryDefaulted<Item, EssenceCauldronCraft> getRegistryEssenceCauldronCraft()
	{
		return REGISTRY_ESSENCE_CAULDRON_CRAFT;
	}

}
