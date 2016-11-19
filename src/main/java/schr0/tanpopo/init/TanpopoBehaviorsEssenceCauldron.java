package schr0.tanpopo.init;

import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import schr0.tanpopo.api.EssenceCauldronCraft;
import schr0.tanpopo.api.EssenceCauldronCraftSimple;
import schr0.tanpopo.api.TanpopoRegistry;
import schr0.tanpopo.item.ItemModeToolAttachment;

public class TanpopoBehaviorsEssenceCauldron
{

	private static final EssenceCauldronCraft CRAFT_ITEM_ATTACHMENT_MATTOCK = new EssenceCauldronCraft()
	{

		@Override
		public Item getKeyItem()
		{
			return TanpopoItems.ATTACHMENT_MATTOCK;
		}

		@Override
		public ItemStack getResultStack(ItemStack stackKeyItem)
		{
			if ((stackKeyItem.getItem() instanceof ItemModeToolAttachment) && ((ItemModeToolAttachment) stackKeyItem.getItem()).isBroken(stackKeyItem))
			{
				stackKeyItem.setItemDamage(0);

				return stackKeyItem;
			}

			return (ItemStack) null;
		}

		@Override
		public int getEssenceCost(ItemStack stackKeyItem)
		{
			return 4;
		}

		@Override
		public int getTickTime(ItemStack stackKeyItem)
		{
			return (50 * 20);
		}

	};

	private static final EssenceCauldronCraft CRAFT_ITEM_ATTACHMENT_FELLING_AXE = new EssenceCauldronCraft()
	{

		@Override
		public Item getKeyItem()
		{
			return TanpopoItems.ATTACHMENT_FELLING_AXE;
		}

		@Override
		public ItemStack getResultStack(ItemStack stackKeyItem)
		{
			if ((stackKeyItem.getItem() instanceof ItemModeToolAttachment) && ((ItemModeToolAttachment) stackKeyItem.getItem()).isBroken(stackKeyItem))
			{
				stackKeyItem.setItemDamage(0);

				return stackKeyItem;
			}

			return (ItemStack) null;
		}

		@Override
		public int getEssenceCost(ItemStack stackKeyItem)
		{
			return 4;
		}

		@Override
		public int getTickTime(ItemStack stackKeyItem)
		{
			return (50 * 20);
		}

	};

	private static final EssenceCauldronCraft CRAFT_ITEM_ATTACHMENT_MOWING_HOE = new EssenceCauldronCraft()
	{

		@Override
		public Item getKeyItem()
		{
			return TanpopoItems.ATTACHMENT_MOWING_HOE;
		}

		@Override
		public ItemStack getResultStack(ItemStack stackKeyItem)
		{
			if ((stackKeyItem.getItem() instanceof ItemModeToolAttachment) && ((ItemModeToolAttachment) stackKeyItem.getItem()).isBroken(stackKeyItem))
			{
				stackKeyItem.setItemDamage(0);

				return stackKeyItem;
			}

			return (ItemStack) null;
		}

		@Override
		public int getEssenceCost(ItemStack stackKeyItem)
		{
			return 4;
		}

		@Override
		public int getTickTime(ItemStack stackKeyItem)
		{
			return (50 * 20);
		}

	};

	public void init()
	{
		TanpopoRegistry.registerEssenceCauldronCraft(new EssenceCauldronCraftSimple(Items.COAL, 1, new ItemStack(TanpopoItems.ESSENCE_SOLID_FUEL), 1, 1, (5 * 20)));
		TanpopoRegistry.registerEssenceCauldronCraft(new EssenceCauldronCraftSimple(Items.IRON_INGOT, new ItemStack(TanpopoItems.ESSENCE_IRON_INGOT), 1, 1, (10 * 20)));
		TanpopoRegistry.registerEssenceCauldronCraft(CRAFT_ITEM_ATTACHMENT_MATTOCK);
		TanpopoRegistry.registerEssenceCauldronCraft(CRAFT_ITEM_ATTACHMENT_FELLING_AXE);
		TanpopoRegistry.registerEssenceCauldronCraft(CRAFT_ITEM_ATTACHMENT_MOWING_HOE);
	}

}
