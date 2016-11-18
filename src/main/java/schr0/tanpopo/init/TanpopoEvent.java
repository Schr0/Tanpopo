package schr0.tanpopo.init;

import net.minecraft.block.Block;
import net.minecraft.block.BlockCauldron;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootEntryItem;
import net.minecraft.world.storage.loot.LootPool;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraft.world.storage.loot.functions.LootFunction;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.ForgeModContainer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.event.entity.player.PlayerDestroyItemEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fluids.UniversalBucket;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import schr0.tanpopo.Tanpopo;
import schr0.tanpopo.block.BlockEssenceCauldron;
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

			itemModeToolAttachment.setContainerModeTool(stackModeAttachment, stackOriginal);

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

	@SubscribeEvent
	public void onAnvilUpdateEvent(AnvilUpdateEvent event)
	{
		ItemStack stackLeft = event.getLeft();
		ItemStack stackRight = event.getRight();

		if ((stackLeft.getItem() instanceof ItemModeToolAttachment) && (stackRight.getItem() == TanpopoItems.MATERIAL_STALK))
		{
			ItemModeToolAttachment itemModeToolAttachment = (ItemModeToolAttachment) stackLeft.getItem();

			if (itemModeToolAttachment.isBroken(stackLeft))
			{
				return;
			}

			event.setCost(5);
			event.setMaterialCost(1);
			event.setOutput(itemModeToolAttachment.getContainerModeTool(stackLeft));
		}
	}

	@SubscribeEvent
	public void onRightClickBlockEvent(PlayerInteractEvent.RightClickBlock event)
	{
		if (event.getItemStack() == null)
		{
			return;
		}

		World world = event.getWorld();
		ItemStack stack = event.getItemStack();
		BlockPos pos = event.getPos();
		IBlockState state = world.getBlockState(pos);
		boolean isCauldronBlock = (state.getBlock() == Blocks.CAULDRON || state.getBlock() == TanpopoBlocks.ESSENCE_CAULDRON);

		if (isCauldronBlock && (stack.getItem() == TanpopoItems.ESSENCE_GLASS_BOTTLE))
		{
			boolean isSuccess = false;

			if (state.getBlock() == TanpopoBlocks.ESSENCE_CAULDRON)
			{
				int level = ((Integer) state.getValue(BlockCauldron.LEVEL)).intValue();

				if (level < 3)
				{
					isSuccess = true;

					((BlockEssenceCauldron) state.getBlock()).setEssenceLevel(world, pos, state, (level + 1));
				}
			}
			else
			{
				isSuccess = true;

				world.setBlockState(pos, TanpopoBlocks.ESSENCE_CAULDRON.getDefaultState(), 2);
			}

			if (isSuccess)
			{
				this.fillCauldronBlock(world, pos, event.getEntityPlayer(), event.getHand(), stack);

				world.playSound(null, pos, SoundEvents.BLOCK_BREWING_STAND_BREW, SoundCategory.BLOCKS, 1.0F, 1.0F);
			}
		}
	}

	@SubscribeEvent
	public void onRightClickItemEvent(PlayerInteractEvent.RightClickItem event)
	{
		if (event.getItemStack() == null)
		{
			return;
		}

		World world = event.getWorld();
		ItemStack stack = event.getItemStack();

		if (ItemStack.areItemStackTagsEqual(stack, UniversalBucket.getFilledBucket(ForgeModContainer.getInstance().universalBucket, TanpopoFluids.ESSENCE)))
		{
			EntityPlayer player = event.getEntityPlayer();
			RayTraceResult mop = ForgeHooks.rayTraceEyes(player, 5.0D);

			if (mop != null && mop.typeOfHit == RayTraceResult.Type.BLOCK)
			{
				BlockPos posMop = mop.getBlockPos();

				if (world.getBlockState(posMop).getBlock() == Blocks.CAULDRON)
				{
					event.setCanceled(true);

					world.setBlockState(posMop, TanpopoBlocks.ESSENCE_CAULDRON.getDefaultState().withProperty(BlockCauldron.LEVEL, TanpopoBlocks.META_ESSENCE_CAULDRON), 2);

					this.fillCauldronBlock(world, posMop, player, event.getHand(), stack);

					world.playSound(null, posMop, SoundEvents.BLOCK_BREWING_STAND_BREW, SoundCategory.BLOCKS, 1.0F, 1.0F);
				}
			}
		}
	}

	// TODO /* ======================================== MOD START =====================================*/

	private void fillCauldronBlock(World world, BlockPos pos, EntityPlayer player, EnumHand hand, ItemStack stack)
	{
		player.swingArm(hand);

		if (!player.capabilities.isCreativeMode)
		{
			ItemStack newHeldItem = stack.getItem().getContainerItem(stack);

			--stack.stackSize;

			if (stack.stackSize <= 0)
			{
				player.setHeldItem(hand, newHeldItem);
			}
			else if (!player.inventory.addItemStackToInventory(newHeldItem))
			{
				player.dropItem(newHeldItem, false);
			}
		}

		player.addStat(StatList.CAULDRON_USED);
	}

}
