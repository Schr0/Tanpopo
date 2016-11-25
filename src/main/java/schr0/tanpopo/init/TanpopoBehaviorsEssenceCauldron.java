package schr0.tanpopo.init;

import java.util.ArrayList;

import com.google.common.collect.Lists;

import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import schr0.tanpopo.api.EssenceCauldronCraft;
import schr0.tanpopo.api.EssenceCauldronCraftSimple;
import schr0.tanpopo.api.TanpopoRegistry;
import schr0.tanpopo.item.ItemModeTool;
import schr0.tanpopo.item.ItemModeToolAttachment;

public class TanpopoBehaviorsEssenceCauldron
{

	public void init()
	{
		TanpopoRegistry.registerEssenceCauldronCraft(new EssenceCauldronCraftSimple(Items.COAL, 1, new ItemStack(TanpopoItems.ESSENCE_SOLID_FUEL), 1, 1, (5 * 20)));
		TanpopoRegistry.registerEssenceCauldronCraft(new EssenceCauldronCraftSimple(Items.IRON_INGOT, new ItemStack(TanpopoItems.ESSENCE_IRON_INGOT), 1, 1, (10 * 20)));

		ArrayList<Item> attachments = Lists.newArrayList(TanpopoItems.ATTACHMENT_MATTOCK, TanpopoItems.ATTACHMENT_FELLING_AXE, TanpopoItems.ATTACHMENT_MOWING_HOE);

		for (final Item attachment : attachments)
		{
			TanpopoRegistry.registerEssenceCauldronCraft(new EssenceCauldronCraft()
			{

				@Override
				public Item getKeyItem()
				{
					return attachment;
				}

				@Override
				public ItemStack getResultStack(ItemStack stackKeyItem)
				{
					if (ItemModeToolAttachment.isBroken(stackKeyItem))
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

			});
		}

		for (final Item item : Item.REGISTRY)
		{
			boolean isVanilla = item.getRegistryName().getResourceDomain().equals("minecraft");
			boolean isModeTool = (item instanceof ItemModeTool);

			if (item.isRepairable() && (isVanilla || isModeTool))
			{
				TanpopoRegistry.registerEssenceCauldronCraft(new EssenceCauldronCraft()
				{

					@Override
					public Item getKeyItem()
					{
						return item;
					}

					@Override
					public ItemStack getResultStack(ItemStack stackKeyItem)
					{
						if (stackKeyItem.isItemDamaged())
						{
							stackKeyItem.setItemDamage(0);

							return stackKeyItem;
						}

						return (ItemStack) null;
					}

					@Override
					public int getEssenceCost(ItemStack stackKeyItem)
					{
						int cost = (stackKeyItem.getItemDamage() / 100);

						cost = Math.min(cost, 4);
						cost = Math.max(cost, 1);

						return cost;
					}

					@Override
					public int getTickTime(ItemStack stackKeyItem)
					{
						int sec = (stackKeyItem.getItemDamage() / 10);

						sec = Math.min(sec, 50);
						sec = Math.max(sec, 10);

						return (sec * 20);
					}

				});
			}
		}

	}

}
