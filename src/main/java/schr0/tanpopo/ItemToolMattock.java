package schr0.tanpopo;

import java.util.List;
import java.util.Set;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Enchantments;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemToolMattock extends ItemTool
{

	private static final Set<Block> EFFECTIVE_BLOCKS = Sets.newHashSet(new Block[]
	{
			Blocks.STONE
	});

	public ItemToolMattock()
	{
		super(1.0F, -2.8F, TanpopoToolMaterials.TIER_0, EFFECTIVE_BLOCKS);
	}

	@Override
	public Set<String> getToolClasses(ItemStack stack)
	{
		return ImmutableSet.of("pickaxe");
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced)
	{
		TextComponentTranslation textMode = new TextComponentTranslation("item.tool_mattock.mode_name", new Object[0]);
		TextComponentTranslation textCondition;

		if (this.isRangeMode(stack))
		{
			textCondition = new TextComponentTranslation("item.tool_mattock.mode_enabled", new Object[0]);
			textCondition.getStyle().setColor(TextFormatting.GREEN);
		}
		else
		{
			textCondition = new TextComponentTranslation("item.tool_mattock.mode_disabled", new Object[0]);
			textCondition.getStyle().setColor(TextFormatting.DARK_RED);
		}

		textMode.getStyle().setColor(TextFormatting.AQUA);
		textCondition.getStyle().setBold(true);

		tooltip.add(new TextComponentString(textMode.getFormattedText() + " : " + textCondition.getFormattedText()).getFormattedText());

		int priority = 0;

		for (int slot = 0; slot < playerIn.inventory.getHotbarSize(); ++slot)
		{
			if (playerIn.inventory.getStackInSlot(slot) != null && playerIn.inventory.getStackInSlot(slot).getItem() instanceof ItemBlock)
			{
				ItemStack stackInv = (ItemStack) playerIn.inventory.getStackInSlot(slot);
				ItemBlock itemBlockInv = (ItemBlock) stackInv.getItem();

				++priority;

				tooltip.add(new TextComponentString(priority + " : " + itemBlockInv.getItemStackDisplayName(stackInv) + " x " + stackInv.stackSize).getFormattedText());
			}
		}
	}

	@Override
	public boolean canHarvestBlock(IBlockState blockIn)
	{
		if (blockIn.getBlock().getHarvestLevel(blockIn) <= this.getToolMaterial().getHarvestLevel())
		{
			return true;
		}

		return false;
	}

	@Override
	public float getStrVsBlock(ItemStack stack, IBlockState state)
	{
		if (this.canHarvestBlock(state))
		{
			return this.efficiencyOnProperMaterial;
		}

		return 1.0F;
	}

	@Override
	public boolean onBlockDestroyed(ItemStack stack, World worldIn, IBlockState state, BlockPos pos, EntityLivingBase entityLiving)
	{
		if (!this.isRangeMode(stack) || !(entityLiving instanceof EntityPlayer))
		{
			return super.onBlockDestroyed(stack, worldIn, state, pos, entityLiving);
		}

		EntityPlayer player = (EntityPlayer) entityLiving;

		for (BlockPos posRange : this.getRangeBlockPos(pos, player))
		{
			if (worldIn.getBlockState(posRange) == state)
			{
				IBlockState stateRange = worldIn.getBlockState(posRange);
				Block blockRange = stateRange.getBlock();

				blockRange.harvestBlock(worldIn, player, posRange, stateRange, worldIn.getTileEntity(posRange), stack);

				if (EnchantmentHelper.getEnchantmentLevel(Enchantments.SILK_TOUCH, stack) <= 0)
				{
					blockRange.dropXpOnBlockBreak(worldIn, posRange, blockRange.getExpDrop(stateRange, worldIn, posRange, EnchantmentHelper.getEnchantmentLevel(Enchantments.FORTUNE, stack)));
				}

				worldIn.destroyBlock(posRange, false);

				if ((double) state.getBlockHardness(worldIn, pos) != 0.0D)
				{
					stack.damageItem(1, player);
				}
			}
		}

		return true;
	}

	@Override
	public EnumActionResult onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		if (playerIn.isSneaking())
		{
			return super.onItemUse(stack, playerIn, worldIn, pos, hand, facing, hitX, hitY, hitZ);
		}

		for (int slot = 0; slot < playerIn.inventory.getHotbarSize(); ++slot)
		{
			if (playerIn.inventory.getStackInSlot(slot) != null && playerIn.inventory.getStackInSlot(slot).getItem() instanceof ItemBlock)
			{
				ItemStack stackInv = (ItemStack) playerIn.inventory.getStackInSlot(slot);
				ItemBlock itemBlockInv = (ItemBlock) stackInv.getItem();

				if (itemBlockInv.onItemUse(stackInv, playerIn, worldIn, pos, hand, facing, hitX, hitY, hitZ) == EnumActionResult.SUCCESS)
				{
					if (stackInv.stackSize <= 0)
					{
						playerIn.inventory.setInventorySlotContents(slot, (ItemStack) null);
					}

					return EnumActionResult.SUCCESS;
				}
			}
		}

		return super.onItemUse(stack, playerIn, worldIn, pos, hand, facing, hitX, hitY, hitZ);
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand hand)
	{
		if (!playerIn.isSneaking())
		{
			return super.onItemRightClick(itemStackIn, worldIn, playerIn, hand);
		}

		boolean isRangeMode = this.isRangeMode(itemStackIn);

		if (!worldIn.isRemote)
		{
			TextComponentString textItem = new TextComponentString(itemStackIn.getDisplayName());
			TextComponentTranslation textMode = new TextComponentTranslation("item.tool_mattock.mode_name", new Object[0]);
			TextComponentTranslation textCondition;

			if (isRangeMode)
			{
				textCondition = new TextComponentTranslation("item.tool_mattock.mode_disabled", new Object[0]);
				textCondition.getStyle().setColor(TextFormatting.DARK_RED);
			}
			else
			{
				textCondition = new TextComponentTranslation("item.tool_mattock.mode_enabled", new Object[0]);
				textCondition.getStyle().setColor(TextFormatting.GREEN);
			}

			textItem.getStyle().setItalic(true);
			textMode.getStyle().setColor(TextFormatting.AQUA);
			textCondition.getStyle().setBold(true);

			playerIn.addChatComponentMessage(new TextComponentString(textItem.getFormattedText() + " -> " + textMode.getFormattedText() + " : " + textCondition.getFormattedText()));

			this.setRangeMode(itemStackIn, !isRangeMode);
		}

		playerIn.swingArm(hand);

		worldIn.playSound(playerIn, new BlockPos(playerIn), SoundEvents.UI_BUTTON_CLICK, SoundCategory.BLOCKS, 1.0F, 1.0F);

		return new ActionResult(EnumActionResult.SUCCESS, itemStackIn);
	}

	// TODO /* ======================================== MOD START =====================================*/

	public boolean isRangeMode(ItemStack stack)
	{
		NBTTagCompound nbtStack = stack.getTagCompound();

		if (nbtStack != null && nbtStack.hasKey(TanpopoNBTTags.ITEM_TOOL_MATTOCK_MODE, 3))
		{
			int value = nbtStack.getInteger(TanpopoNBTTags.ITEM_TOOL_MATTOCK_MODE);

			return (value == 1);
		}

		return false;
	}

	public void setRangeMode(ItemStack stack, boolean isMode)
	{
		NBTTagCompound nbtStack = stack.getTagCompound();

		if (nbtStack == null)
		{
			nbtStack = new NBTTagCompound();
		}

		int value = isMode ? (1) : (0);

		nbtStack.setInteger(TanpopoNBTTags.ITEM_TOOL_MATTOCK_MODE, value);

		stack.setTagCompound(nbtStack);
	}

	private Set<BlockPos> getRangeBlockPos(BlockPos pos, EntityPlayer player)
	{
		Set<BlockPos> posSet = Sets.newHashSet();
		RayTraceResult rayTraceResult = this.rayTrace(player.worldObj, player, false);
		// ForgeHooks.rayTraceEyes(player, 5.0D)

		if (rayTraceResult == null)
		{
			return posSet;
		}

		int side = rayTraceResult.sideHit.getIndex();
		int range = 1;

		switch (side)
		{
			case 2 :

				for (BlockPos posAround : BlockPos.getAllInBox(pos.add(-range, -range, 0), pos.add(range, range, 0)))
				{
					posSet.add(posAround);
				}

				break;

			case 3 :

				for (BlockPos posAround : BlockPos.getAllInBox(pos.add(range, -range, 0), pos.add(-range, range, 0)))
				{
					posSet.add(posAround);
				}

				break;

			case 4 :

				for (BlockPos posAround : BlockPos.getAllInBox(pos.add(0, -range, -range), pos.add(0, range, range)))
				{
					posSet.add(posAround);
				}

				break;

			case 5 :

				for (BlockPos posAround : BlockPos.getAllInBox(pos.add(0, -range, range), pos.add(0, range, -range)))
				{
					posSet.add(posAround);
				}

				break;

			default :

				for (BlockPos posAround : BlockPos.getAllInBox(pos.add(-range, 0, -range), pos.add(range, 0, range)))
				{
					posSet.add(posAround);
				}
		}

		return posSet;
	}

}
