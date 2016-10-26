package schr0.tanpopo.init;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootEntryItem;
import net.minecraft.world.storage.loot.LootPool;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraft.world.storage.loot.functions.LootFunction;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.event.entity.player.PlayerDestroyItemEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import schr0.tanpopo.Tanpopo;
import schr0.tanpopo.block.BlockFluffCushion;
import schr0.tanpopo.capabilities.FluidHandlerItemEssenceGlassBottle;
import schr0.tanpopo.item.ItemModeTool;
import schr0.tanpopo.item.ItemModeToolAttachment;

public class TanpopoEvent
{

	public void init()
	{
		MinecraftForge.EVENT_BUS.register(this);
	}

	@SubscribeEvent
	public void onLootTableLoadEvent(LootTableLoadEvent event)
	{
		Item item = Item.getItemFromBlock(TanpopoBlocks.PLANT_ROOTS);
		int weight = 100;
		int quality = 0;
		LootFunction[] lootFunctions = new LootFunction[0];
		LootCondition[] lootConditions = new LootCondition[0];
		String entryName = Tanpopo.MOD_DOMAIN + TanpopoBlocks.NAME_PLANT_ROOTS;

		if (event.getName().equals(LootTableList.CHESTS_ABANDONED_MINESHAFT))
		{
			final LootPool main = event.getTable().getPool("main");

			if (main != null)
			{
				main.addEntry(new LootEntryItem(item, weight, quality, lootFunctions, lootConditions, entryName));
			}
		}
	}

	@SubscribeEvent
	public void onItemAttachCapabilitiesEvent(AttachCapabilitiesEvent.Item event)
	{
		Item item = event.getItem();
		ItemStack stack = event.getItemStack();

		if (item == null || stack == null)
		{
			return;
		}

		if (item == Items.GLASS_BOTTLE)
		{
			event.addCapability(new ResourceLocation(Tanpopo.MOD_ID, "FluidHandlerItemEssenceGlassBottle"), new FluidHandlerItemEssenceGlassBottle(stack));
		}
	}

	@SubscribeEvent
	public void onLivingFallEvent(LivingFallEvent event)
	{
		EntityLivingBase livingBase = event.getEntityLiving();
		World world = livingBase.worldObj;
		BlockPos posDown = (new BlockPos(livingBase)).down();

		if (world.isAirBlock(posDown))
		{
			for (BlockPos posAround : BlockPos.getAllInBox(posDown.add(-1, 0, -1), posDown.add(1, 0, 1)))
			{
				Block blockAround = world.getBlockState(posAround).getBlock();

				if (blockAround == TanpopoBlocks.FLUFF_CUSHION)
				{
					((BlockFluffCushion) blockAround).onSpring(world, livingBase);

					event.setDamageMultiplier(0.0F);
				}
			}
		}
	}

	@SubscribeEvent
	public void onPlayerDestroyItemEvent(PlayerDestroyItemEvent event)
	{
		EntityPlayer player = event.getEntityPlayer();
		ItemStack stackOriginal = event.getOriginal();
		Item itemOriginal = stackOriginal.getItem();
		EnumHand hand = event.getHand();

		if (itemOriginal instanceof ItemModeTool)
		{
			ItemStack stackModeAttachment = new ItemStack(((ItemModeTool) itemOriginal).getModeAttachment(), 1, 1);
			ItemModeToolAttachment itemModeToolAttachment = (ItemModeToolAttachment) stackModeAttachment.getItem();

			itemModeToolAttachment.setModeToolItemstack(stackModeAttachment, stackOriginal);

			if (!player.worldObj.isRemote && !player.capabilities.isCreativeMode)
			{
				if (player.getHeldItem(hand) == null)
				{
					player.setHeldItem(hand, stackModeAttachment);
				}
				else
				{
					if (!player.inventory.addItemStackToInventory(stackModeAttachment))
					{
						player.dropItem(stackModeAttachment, false);
					}
				}
			}
		}
	}

}
