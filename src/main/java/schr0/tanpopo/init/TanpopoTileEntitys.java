package schr0.tanpopo.init;

import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import schr0.tanpopo.tileentity.TileEntityEssenceCauldron;

public class TanpopoTileEntitys
{

	public static final String NAME_ESSENCE_CAULDRON = "essence_cauldron";

	public void init()
	{
		register();
	}

	private static void register()
	{
		GameRegistry.registerTileEntity(TileEntityEssenceCauldron.class, NAME_ESSENCE_CAULDRON);
	}

	@SideOnly(Side.CLIENT)
	public void initClient()
	{
		registerClient();
	}

	@SideOnly(Side.CLIENT)
	private static void registerClient()
	{
		GameRegistry.registerTileEntity(TileEntityEssenceCauldron.class, NAME_ESSENCE_CAULDRON);
	}

}
