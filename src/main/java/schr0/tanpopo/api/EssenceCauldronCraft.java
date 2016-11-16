package schr0.tanpopo.api;

import javax.annotation.Nullable;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public abstract class EssenceCauldronCraft
{

	public abstract Item getKeyItem();

	@Nullable
	public abstract ItemStack getResultStack(ItemStack stackKeyItem);

	public int getEssenceCost(ItemStack stackKeyItem)
	{
		return 1;
	}

	public int getStackCost(ItemStack stackKeyItem)
	{
		return 1;
	}

	public int getTickTime(ItemStack stackKeyItem)
	{
		return (1 * 20);
	}

}
