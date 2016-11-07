package schr0.tanpopo.init;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import schr0.tanpopo.api.EssenceCauldronCraft;
import schr0.tanpopo.api.TanpopoRegistry;
import schr0.tanpopo.item.ItemModeToolAttachment;

public class TanpopoEssenceCauldronBehaviors
{

	private static final EssenceCauldronCraft ATTACHMENT_ESSENCE_CAULDRON_CRAFT = new EssenceCauldronCraft()
	{

		@Override
		public int getEssenceCost(ItemStack stack)
		{
			return 4;
		}

		@Override
		public int getTime(ItemStack stack)
		{
			return 60 * 20;
		}

		@Override
		public ItemStack getResult(ItemStack stack)
		{
			if (stack.getItem() instanceof ItemModeToolAttachment)
			{
				if (((ItemModeToolAttachment) stack.getItem()).isBroken(stack))
				{
					stack.setItemDamage(0);

					return stack;
				}
			}

			return super.getResult(stack);
		}

	};

	private static final EssenceCauldronCraft TOOL_ESSENCE_CAULDRON_CRAFT = new EssenceCauldronCraft()
	{

		@Override
		public int getEssenceCost(ItemStack stack)
		{
			int cost = stack.getItemDamage() / 100;

			cost = Math.max(1, cost);
			cost = Math.min(4, cost);

			return cost;
		}

		@Override
		public int getTime(ItemStack stack)
		{
			int sec = stack.getItemDamage() / 10;

			sec = Math.max(10, sec);
			sec = Math.min(50, sec);

			return (sec * 20);
		}

		@Override
		public ItemStack getResult(ItemStack stack)
		{
			if (stack.isItemDamaged())
			{
				stack.setItemDamage(0);

				return stack;
			}

			return super.getResult(stack);
		}

	};

	public void init()
	{
		register();
	}

	private static void register()
	{
		TanpopoRegistry.registerRegistryEssenceCauldronCraft(Items.COAL, new EssenceCauldronCraft()
		{

			@Override
			public ItemStack getResult(ItemStack stack)
			{
				if (stack.getItemDamage() == 1)
				{
					return new ItemStack(TanpopoItems.ESSENCE_SOLID_FUEL);
				}

				return super.getResult(stack);
			}

		});

		TanpopoRegistry.registerRegistryEssenceCauldronCraft(TanpopoItems.ATTACHMENT_MATTOCK, ATTACHMENT_ESSENCE_CAULDRON_CRAFT);
		TanpopoRegistry.registerRegistryEssenceCauldronCraft(TanpopoItems.ATTACHMENT_FELLING_AXE, ATTACHMENT_ESSENCE_CAULDRON_CRAFT);
		TanpopoRegistry.registerRegistryEssenceCauldronCraft(TanpopoItems.ATTACHMENT_MOWING_HOE, ATTACHMENT_ESSENCE_CAULDRON_CRAFT);

		TanpopoRegistry.registerRegistryEssenceCauldronCraft(TanpopoItems.TOOL_MATTOCK, TOOL_ESSENCE_CAULDRON_CRAFT);
		TanpopoRegistry.registerRegistryEssenceCauldronCraft(TanpopoItems.TOOL_FELLING_AXE, TOOL_ESSENCE_CAULDRON_CRAFT);
		TanpopoRegistry.registerRegistryEssenceCauldronCraft(TanpopoItems.TOOL_MOWING_HOE, TOOL_ESSENCE_CAULDRON_CRAFT);
	}

}
