package schr0.tanpopo;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TanpopoCreativeTabs
{

	public static final CreativeTabs BLOCK = new CreativeTabs(Tanpopo.MOD_ID + "." + "block")
	{

		@Override
		@SideOnly(Side.CLIENT)
		public int getIconItemDamage()
		{
			return 10;
		}

		@Override
		@SideOnly(Side.CLIENT)
		public Item getTabIconItem()
		{
			return Item.getItemFromBlock(TanpopoBlocks.PLANT_FLOWER);
		}

	};

	public static final CreativeTabs ITEM = new CreativeTabs(Tanpopo.MOD_ID + "." + "item")
	{

		@Override
		@SideOnly(Side.CLIENT)
		public Item getTabIconItem()
		{
			return TanpopoItems.ESSENCE_GLASS_BOTTLE;
		}

	};

}