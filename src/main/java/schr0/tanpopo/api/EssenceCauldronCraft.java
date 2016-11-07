package schr0.tanpopo.api;

import javax.annotation.Nullable;

import net.minecraft.item.ItemStack;

public class EssenceCauldronCraft
{

	public int getEssenceCost(ItemStack stack)
	{
		return 1;
	}

	public int getStackCost(ItemStack stack)
	{
		return 1;
	}

	public int getTime(ItemStack stack)
	{
		return 5 * 20;
	}

	@Nullable
	public ItemStack getResult(ItemStack stack)
	{
		return (ItemStack) null;
	}

}
