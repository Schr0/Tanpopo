package schr0.tanpopo.init;

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
		int baseBurnTime = (64 * 200);

		if (ItemEssenceGlassBottle.isFill(fuel))
		{
			return baseBurnTime;// 8
		}

		if (ItemStack.areItemStackTagsEqual(fuel, UniversalBucket.getFilledBucket(ForgeModContainer.getInstance().universalBucket, TanpopoFluids.ESSENCE)))
		{
			return (baseBurnTime * 4);// 32
		}

		if (fuel.getItem() == TanpopoItems.ESSENCE_SOLID_FUEL)
		{
			return (int) (baseBurnTime * 1.5);// 12
		}

		return 0;
	}

}
