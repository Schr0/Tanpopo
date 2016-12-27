package schr0.tanpopo.api;

import java.util.ArrayList;

public class TanpopoRegistry
{

	private static final ArrayList<EssenceCauldronCraft> LIST_ESSENCE_CAULDRON_CRAFT = new ArrayList<EssenceCauldronCraft>();

	public static void registerEssenceCauldronCraft(EssenceCauldronCraft value)
	{
		LIST_ESSENCE_CAULDRON_CRAFT.add(value);
	}

	public static ArrayList<EssenceCauldronCraft> getListEssenceCauldronCraft()
	{
		return (ArrayList<EssenceCauldronCraft>) LIST_ESSENCE_CAULDRON_CRAFT.clone();
	}

}
