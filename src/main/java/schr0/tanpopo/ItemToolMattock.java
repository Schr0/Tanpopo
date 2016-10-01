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
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
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
		super(1.0F, -1.4F, TanpopoToolMaterial.IRON, EFFECTIVE_BLOCKS);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced)
	{
		TextComponentTranslation textMode = new TextComponentTranslation("item.tool_mattock.mode_name", new Object[0]);
		textMode.getStyle().setItalic(true);
		textMode.getStyle().setBold(true);

		TextComponentTranslation textSentence = new TextComponentTranslation("item.tool_mattock.mode_disabled", new Object[]
		{
				textMode
		});
		textSentence.getStyle().setColor(TextFormatting.DARK_RED);

		if (this.isRangeMode(stack))
		{
			textSentence = new TextComponentTranslation("item.tool_mattock.mode_enabled", new Object[]
			{
					textMode
			});
			textSentence.getStyle().setColor(TextFormatting.GREEN);
		}

		tooltip.add(textSentence.getFormattedText());
	}

	@Override
	public Set<String> getToolClasses(ItemStack stack)
	{
		return ImmutableSet.of("pickaxe");
	}

	@Override
	public boolean canHarvestBlock(IBlockState blockIn)
	{
		int harvestLevel = blockIn.getBlock().getHarvestLevel(blockIn);

		return (harvestLevel <= this.getToolMaterial().getHarvestLevel());
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
	public boolean getIsRepairable(ItemStack toRepair, ItemStack repair)
	{
		return false;
	}

	@Override
	public boolean onBlockDestroyed(ItemStack stack, World worldIn, IBlockState state, BlockPos pos, EntityLivingBase entityLiving)
	{
		if (!this.isRangeMode(stack))
		{
			return super.onBlockDestroyed(stack, worldIn, state, pos, entityLiving);
		}

		int side = ForgeHooks.rayTraceEyes(entityLiving, 5.0d).sideHit.getIndex();
		int range = 1;
		List<BlockPos> posArounds = Lists.newArrayList();

		switch (side)
		{
			case 2:

				for (BlockPos posAround : BlockPos.getAllInBox(pos.add(-range, -range, 0), pos.add(range, range, 0)))
				{
					posArounds.add(posAround);
				}

				break;

			case 3:

				for (BlockPos posAround : BlockPos.getAllInBox(pos.add(range, -range, 0), pos.add(-range, range, 0)))
				{
					posArounds.add(posAround);
				}

				break;

			case 4:

				for (BlockPos posAround : BlockPos.getAllInBox(pos.add(0, -range, -range), pos.add(0, range, range)))
				{
					posArounds.add(posAround);
				}

				break;

			case 5:

				for (BlockPos posAround : BlockPos.getAllInBox(pos.add(0, -range, range), pos.add(0, range, -range)))
				{
					posArounds.add(posAround);
				}

				break;

			default:

				for (BlockPos posAround : BlockPos.getAllInBox(pos.add(-range, 0, -range), pos.add(range, 0, range)))
				{
					posArounds.add(posAround);
				}
		}

		for (BlockPos posAround : posArounds)
		{
			double hardnessPos = state.getBlockHardness(worldIn, pos);
			double hardnessPosAround = worldIn.getBlockState(posAround).getBlockHardness(worldIn, posAround);

			if (!worldIn.isAirBlock(posAround) && (hardnessPosAround <= hardnessPos))
			{
				IBlockState stateAround = worldIn.getBlockState(posAround);
				Block blockAround = stateAround.getBlock();
				int level = EnchantmentHelper.getEnchantmentLevel(Enchantments.FORTUNE, stack);

				worldIn.destroyBlock(posAround, false);

				blockAround.dropBlockAsItem(worldIn, posAround, stateAround, level);

				if (hardnessPosAround != 0.0D)
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
				textItem.getStyle().setItalic(true);
				textItem.getStyle().setBold(true);
				textItem.appendText(" : ");
				textItem.getStyle().setColor(TextFormatting.WHITE);

				TextComponentTranslation textMode = new TextComponentTranslation("item.tool_mattock.mode_name", new Object[0]);
				textMode.getStyle().setItalic(true);
				textMode.getStyle().setBold(true);

				TextComponentTranslation textSentence = new TextComponentTranslation("item.tool_mattock.mode_enabled", new Object[]
				{
						textMode
				});
				textSentence.getStyle().setColor(TextFormatting.GREEN);

				if (this.isRangeMode(itemStackIn))
				{
					textSentence = new TextComponentTranslation("item.tool_mattock.mode_disabled", new Object[]
					{
							textMode
					});
					textSentence.getStyle().setColor(TextFormatting.DARK_RED);
				}

				playerIn.addChatComponentMessage(textItem.appendText(textSentence.getFormattedText()));

				this.setRangeMode(!isRangeMode, itemStackIn);
			}

			playerIn.swingArm(hand);

			worldIn.playSound(playerIn, new BlockPos(playerIn), SoundEvents.UI_BUTTON_CLICK, SoundCategory.BLOCKS, 1.0F, 1.0F);

			return new ActionResult(EnumActionResult.SUCCESS, itemStackIn);
		}

		return super.onItemRightClick(itemStackIn, worldIn, playerIn, hand);
	}

	// TODO /* ======================================== MOD START =====================================*/

	public boolean isRangeMode(ItemStack stack)
	{
		NBTTagCompound nbtStack = stack.getTagCompound();

		if (nbtStack != null && nbtStack.hasKey(TanpopoNBTTag.ITEM_TOOL_MATTOCK_MODE, 3))
		{
			int value = nbtStack.getInteger(TanpopoNBTTag.ITEM_TOOL_MATTOCK_MODE);

			return (value == 1);
		}

		return false;
	}

	public void setRangeMode(boolean isRangeMode, ItemStack stack)
	{
		NBTTagCompound nbtStack = stack.getTagCompound();

		if (nbtStack == null)
		{
			nbtStack = new NBTTagCompound();
		}

		int value = isRangeMode ? (1) : (0);

		nbtStack.setInteger(TanpopoNBTTag.ITEM_TOOL_MATTOCK_MODE, value);

		stack.setTagCompound(nbtStack);
	}

}