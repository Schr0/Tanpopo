package schr0.tanpopo.init;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.ForgeModContainer;
import net.minecraftforge.fluids.UniversalBucket;
import net.minecraftforge.fml.common.IFuelHandler;
import net.minecraftforge.fml.common.registry.GameRegistry;
import schr0.tanpopo.item.ItemEssenceGlassBottle;

public class TanpopoFuelHandler implements IFuelHandler
{

	public void init()
	{
		GameRegistry.registerFuelHandler(this);
	}

	@Override
	public int getBurnTime(ItemStack fuel)
	{
		int essenceBurnTime = (1600 * 8);// (COAL * 8)

		if (ItemEssenceGlassBottle.isFill(fuel))
		{
			return essenceBurnTime;
		}

		if (ItemStack.areItemStackTagsEqual(fuel, UniversalBucket.getFilledBucket(ForgeModContainer.getInstance().universalBucket, TanpopoFluids.ESSENCE)))
		{
			return (essenceBurnTime * 4);
		}

		int fuelBurnTime = (int) (essenceBurnTime * 1.5);

		if (fuel.getItem() == TanpopoItems.ESSENCE_SOLID_FUEL)
		{
			return fuelBurnTime;
		}

		if (fuel.getItem() == Item.getItemFromBlock(TanpopoBlocks.ESSENCE_SOLID_FUEL_BLOCK))
		{
			return (fuelBurnTime * 10);
		}

		return 0;
	}

}
