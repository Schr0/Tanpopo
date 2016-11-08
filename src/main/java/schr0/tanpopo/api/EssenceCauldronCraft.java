package schr0.tanpopo.api;

import javax.annotation.Nullable;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public abstract class EssenceCauldronCraft
{

	public abstract Item getKeyItem();

	public int getEssenceCost(ItemStack stack)
	{
		return 1;
	}

	public int getStackCost(ItemStack stack)
	{
		return 1;
	}

	public int getTickTime(ItemStack stack)
	{
		return 1 * 20;
	}

	@Nullable
	public ItemStack getResult(ItemStack stack)
	{
		return (ItemStack) null;
	}

}
