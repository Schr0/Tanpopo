package schr0.tanpopo.api;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class EssenceCauldronCraftSimple extends EssenceCauldronCraft
{

	private Item keyItem;
	private int metaKeyItem;
	private ItemStack resultStack;
	private int essenceCost;
	private int stackCost;
	private int tickTime;

	public EssenceCauldronCraftSimple(Item keyItem, int metaKeyItem, ItemStack resultStack, int essenceCost, int stackCost, int tickTime)
	{
		this.keyItem = keyItem;
		this.metaKeyItem = metaKeyItem;
		this.resultStack = resultStack;
		this.essenceCost = essenceCost;
		this.stackCost = stackCost;
		this.tickTime = tickTime;
	}

	public EssenceCauldronCraftSimple(Item keyItem, ItemStack resultStack, int essenceCost, int stackCost, int tickTime)
	{
		this(keyItem, -1, resultStack, essenceCost, stackCost, tickTime);
	}

	@Override
	public Item getKeyItem()
	{
		return this.keyItem;
	}

	@Override
	public ItemStack getResultStack(ItemStack stackKeyItem)
	{
		ItemStack resultStackCopy = this.resultStack.copy();

		if (this.metaKeyItem < 0)
		{
			return resultStackCopy;
		}
		else
		{
			if (this.metaKeyItem == stackKeyItem.getItemDamage())
			{
				return resultStackCopy;
			}
		}

		return (ItemStack) null;
	}

	@Override
	public int getEssenceCost(ItemStack stackKeyItem)
	{
		return this.essenceCost;
	}

	@Override
	public int getStackCost(ItemStack stackKeyItem)
	{
		return this.stackCost;
	}

	@Override
	public int getTickTime(ItemStack stackKeyItem)
	{
		return this.tickTime;
	}

}
