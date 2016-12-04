package schr0.tanpopo.api;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class EssenceCauldronCraftSimple extends EssenceCauldronCraft
{

	private Item keyItem;
	private int metaKeyItem;
	private ItemStack stackResult;
	private int costEssence;
	private int costStack;
	private int tickTime;

	public EssenceCauldronCraftSimple(Item keyItem, int metaKeyItem, ItemStack stackResult, int costEssence, int costStack, int tickTime)
	{
		this.keyItem = keyItem;
		this.metaKeyItem = metaKeyItem;
		this.stackResult = stackResult;
		this.costEssence = costEssence;
		this.costStack = costStack;
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
		ItemStack stackResultCopy = this.stackResult.copy();

		if (this.metaKeyItem < 0)
		{
			return stackResultCopy;
		}
		else
		{
			if (this.metaKeyItem == stackKeyItem.getItemDamage())
			{
				return stackResultCopy;
			}
		}

		return (ItemStack) null;
	}

	@Override
	public int getEssenceCost(ItemStack stackKeyItem)
	{
		return this.costEssence;
	}

	@Override
	public int getStackCost(ItemStack stackKeyItem)
	{
		return this.costStack;
	}

	@Override
	public int getTickTime(ItemStack stackKeyItem)
	{
		return this.tickTime;
	}

}
