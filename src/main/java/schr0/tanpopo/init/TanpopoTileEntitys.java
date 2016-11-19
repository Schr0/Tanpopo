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
		GameRegistry.registerTileEntity(TileEntityEssenceCauldron.class, NAME_ESSENCE_CAULDRON);
	}

	@SideOnly(Side.CLIENT)
	public void initClient()
	{
		GameRegistry.registerTileEntity(TileEntityEssenceCauldron.class, NAME_ESSENCE_CAULDRON);
	}

}
