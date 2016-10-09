package schr0.tanpopo;

import java.util.List;
import java.util.Set;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
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
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemToolMattock extends ItemTool
{

	private static final Set<Block> EFFECTIVE_BLOCKS = Sets.newHashSet(new Block[]
	{ Blocks.STONE });

	public ItemToolMattock()
	{
		super(1.0F, -2.8F, TanpopoToolMaterials.TIER_0, EFFECTIVE_BLOCKS);
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

		int num = 0;

		for (int slot = 0; slot < playerIn.inventory.getHotbarSize(); ++slot)
		{
			if (playerIn.inventory.getStackInSlot(slot) != null && playerIn.inventory.getStackInSlot(slot).getItem() instanceof ItemBlock)
			{
				ItemStack stackInv = (ItemStack) playerIn.inventory.getStackInSlot(slot);
				ItemBlock itemBlockInv = (ItemBlock) stackInv.getItem();

				++num;

				tooltip.add(new TextComponentString(num + " : " + itemBlockInv.getItemStackDisplayName(stackInv) + " x " + stackInv.stackSize).getFormattedText());
			}
		}
	}

	@Override
	public boolean getIsRepairable(ItemStack toRepair, ItemStack repair)
	{
		return false;
	}

	@Override
	public Set<String> getToolClasses(ItemStack stack)
	{
		return ImmutableSet.of("pickaxe");
	}

	@Override
	public boolean canHarvestBlock(IBlockState blockIn)
	{
		return ((blockIn.getBlock().getHarvestLevel(blockIn)) <= (this.getToolMaterial().getHarvestLevel()));
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
		if (!this.isRangeMode(stack))
		{
			return super.onBlockDestroyed(stack, worldIn, state, pos, entityLiving);
		}

		List<ItemStack> dropItemList = Lists.newArrayList();

		for (BlockPos posRange : this.getRangeBlockPos(pos, entityLiving))
		{
			if (worldIn.getBlockState(posRange) == state)
			{
				IBlockState stateRange = worldIn.getBlockState(posRange);
				Block blockRange = stateRange.getBlock();

				dropItemList.addAll(blockRange.getDrops(worldIn, posRange, stateRange, EnchantmentHelper.getEnchantmentLevel(Enchantments.FORTUNE, stack)));

				worldIn.destroyBlock(posRange, false);

				if ((double) stateRange.getBlockHardness(worldIn, posRange) != 0.0D)
				{
					stack.damageItem(1, entityLiving);
				}

				if (stack.getMaxDamage() <= stack.getItemDamage())
				{
					stack.stackSize = 0;

					break;
				}
			}
		}

		for (ItemStack stackDrop : dropItemList)
		{
			Block.spawnAsEntity(worldIn, pos, stackDrop);
		}

		return true;
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand hand)
	{
		if (playerIn.isSneaking())
		{
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

		return super.onItemRightClick(itemStackIn, worldIn, playerIn, hand);
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

				if (this.isRangeMode(stack))
				{
					boolean isSucces = false;

					for (BlockPos posAround : this.getRangeBlockPos(pos, playerIn))
					{
						if (worldIn.isAirBlock(posAround))
						{
							continue;
						}

						if (itemBlockInv.onItemUse(stackInv, playerIn, worldIn, posAround, hand, facing, hitX, hitY, hitZ) == EnumActionResult.SUCCESS)
						{
							isSucces = true;

							if (stackInv.stackSize <= 0)
							{
								playerIn.inventory.setInventorySlotContents(slot, (ItemStack) null);

								break;
							}
						}
					}

					if (isSucces)
					{
						return EnumActionResult.SUCCESS;
					}
				}
				else
				{
					if (itemBlockInv.onItemUse(stackInv, playerIn, worldIn, pos, hand, facing, hitX, hitY, hitZ) == EnumActionResult.SUCCESS)
					{
						if (stackInv.stackSize <= 0)
						{
							playerIn.inventory.setInventorySlotContents(slot, (ItemStack) null);
						}

						return EnumActionResult.SUCCESS;
					}
				}

				return super.onItemUse(stack, playerIn, worldIn, pos, hand, facing, hitX, hitY, hitZ);
			}
		}

		return super.onItemUse(stack, playerIn, worldIn, pos, hand, facing, hitX, hitY, hitZ);
	}

	// TODO /* ======================================== MOD START =====================================*/

	private boolean isRangeMode(ItemStack stack)
	{
		NBTTagCompound nbtStack = stack.getTagCompound();

		if (nbtStack != null && nbtStack.hasKey(TanpopoNBTTags.ITEM_TOOL_MATTOCK_MODE, 3))
		{
			int value = nbtStack.getInteger(TanpopoNBTTags.ITEM_TOOL_MATTOCK_MODE);

			return (value == 1);
		}

		return false;
	}

	private void setRangeMode(ItemStack stack, boolean isMode)
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

	private List<BlockPos> getRangeBlockPos(BlockPos pos, EntityLivingBase owner)
	{
		List<BlockPos> posList = Lists.newArrayList(pos);
		double length = 5.0D;
		int range = 1;

		if (ForgeHooks.rayTraceEyes(owner, length) == null)
		{
			return posList;
		}

		int side = ForgeHooks.rayTraceEyes(owner, length).sideHit.getIndex();

		switch (side)
		{
			case 2:

				for (BlockPos posAround : BlockPos.getAllInBox(pos.add(-range, -range, 0), pos.add(range, range, 0)))
				{
					posList.add(posAround);
				}

				break;

			case 3:

				for (BlockPos posAround : BlockPos.getAllInBox(pos.add(range, -range, 0), pos.add(-range, range, 0)))
				{
					posList.add(posAround);
				}

				break;

			case 4:

				for (BlockPos posAround : BlockPos.getAllInBox(pos.add(0, -range, -range), pos.add(0, range, range)))
				{
					posList.add(posAround);
				}

				break;

			case 5:

				for (BlockPos posAround : BlockPos.getAllInBox(pos.add(0, -range, range), pos.add(0, range, -range)))
				{
					posList.add(posAround);
				}

				break;

			default:

				for (BlockPos posAround : BlockPos.getAllInBox(pos.add(-range, 0, -range), pos.add(range, 0, range)))
				{
					posList.add(posAround);
				}
		}

		return posList;
	}

}