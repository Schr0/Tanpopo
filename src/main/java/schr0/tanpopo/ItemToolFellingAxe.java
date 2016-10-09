package schr0.tanpopo;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Nullable;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Enchantments;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumAction;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemToolFellingAxe extends ItemTool
{

	private static final Set<Block> EFFECTIVE_BLOCKS = Sets.newHashSet(new Block[]
	{ Blocks.LOG, Blocks.LOG2, Blocks.LEAVES, Blocks.LEAVES2 });

	private static final List<Material> EFFECTIVE_MATERIALS = Lists.newArrayList(Material.WOOD, Material.PLANTS, Material.VINE);

	private int fellingBlockLimit;

	public ItemToolFellingAxe()
	{
		super(8.0F, -3.0F, TanpopoToolMaterials.TIER_0, EFFECTIVE_BLOCKS);

		this.addPropertyOverride(new ResourceLocation("blocking"), new IItemPropertyGetter()
		{
			@SideOnly(Side.CLIENT)
			public float apply(ItemStack stack, @Nullable World worldIn, @Nullable EntityLivingBase entityIn)
			{
				return entityIn != null && entityIn.isHandActive() && entityIn.getActiveItemStack() == stack ? 1.0F : 0.0F;
			}
		});

		this.fellingBlockLimit = TanpopoConfiguration.fellingBlockLimit;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced)
	{
		TextComponentTranslation textMode = new TextComponentTranslation("item.tool_felling_axe.mode_name", new Object[0]);
		TextComponentTranslation textCondition;

		if (this.isFellingMode(stack))
		{
			textCondition = new TextComponentTranslation("item.tool_felling_axe.mode_enabled", new Object[0]);
			textCondition.getStyle().setColor(TextFormatting.GREEN);
		}
		else
		{
			textCondition = new TextComponentTranslation("item.tool_felling_axe.mode_disabled", new Object[0]);
			textCondition.getStyle().setColor(TextFormatting.DARK_RED);
		}

		textMode.getStyle().setColor(TextFormatting.AQUA);
		textCondition.getStyle().setBold(true);

		tooltip.add(new TextComponentString(textMode.getFormattedText() + " : " + textCondition.getFormattedText()).getFormattedText());
	}

	@Override
	public boolean getIsRepairable(ItemStack toRepair, ItemStack repair)
	{
		return false;
	}

	@Override
	public Set<String> getToolClasses(ItemStack stack)
	{
		return ImmutableSet.of("axe");
	}

	@Override
	public float getStrVsBlock(ItemStack stack, IBlockState state)
	{
		for (Material material : EFFECTIVE_MATERIALS)
		{
			if (material == state.getMaterial())
			{
				return this.efficiencyOnProperMaterial;
			}
		}

		return 1.0F;
	}

	@Override
	public boolean onBlockDestroyed(ItemStack stack, World worldIn, IBlockState state, BlockPos pos, EntityLivingBase entityLiving)
	{
		if (this.canStartFelling(stack, worldIn, pos))
		{
			Set<BlockPos> posSet = new LinkedHashSet<>();
			List<ItemStack> dropItemList = Lists.newArrayList();
			int countFelling = 0;

			this.getFellingBlockPos(posSet, worldIn, pos);

			for (BlockPos posFelling : posSet)
			{
				for (BlockPos posAround : BlockPos.getAllInBox(posFelling.add(-1, -1, -1), posFelling.add(1, 1, 1)))
				{
					if (this.isFellingBlocks(worldIn, posAround))
					{
						IBlockState stateAround = worldIn.getBlockState(posAround);
						Block blockAround = stateAround.getBlock();

						if (blockAround.isLeaves(stateAround, worldIn, posAround))
						{
							blockAround.beginLeavesDecay(stateAround, worldIn, posAround);
						}
						else
						{
							dropItemList.addAll(blockAround.getDrops(worldIn, posAround, stateAround, EnchantmentHelper.getEnchantmentLevel(Enchantments.FORTUNE, stack)));

							worldIn.setBlockToAir(posAround);

							++countFelling;
						}
					}
				}
			}

			for (ItemStack stackDrop : dropItemList)
			{
				Block.spawnAsEntity(worldIn, pos, stackDrop);
			}

			int damage = Math.max(2, (countFelling / 8));

			stack.damageItem(damage, entityLiving);
		}

		return super.onBlockDestroyed(stack, worldIn, state, pos, entityLiving);

	}

	@Override
	public EnumAction getItemUseAction(ItemStack stack)
	{
		return EnumAction.BLOCK;
	}

	@Override
	public int getMaxItemUseDuration(ItemStack stack)
	{
		return 72000;
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand hand)
	{
		if (!playerIn.isSneaking())
		{
			playerIn.setActiveHand(hand);

			return new ActionResult(EnumActionResult.SUCCESS, itemStackIn);
		}

		if (!worldIn.isRemote)
		{
			boolean isFellingMode = this.isFellingMode(itemStackIn);
			TextComponentString textItem = new TextComponentString(itemStackIn.getDisplayName());
			TextComponentTranslation textMode = new TextComponentTranslation("item.tool_felling_axe.mode_name", new Object[0]);
			TextComponentTranslation textCondition;

			if (isFellingMode)
			{
				textCondition = new TextComponentTranslation("item.tool_felling_axe.mode_disabled", new Object[0]);
				textCondition.getStyle().setColor(TextFormatting.DARK_RED);
			}
			else
			{
				textCondition = new TextComponentTranslation("item.tool_felling_axe.mode_enabled", new Object[0]);
				textCondition.getStyle().setColor(TextFormatting.GREEN);
			}

			textItem.getStyle().setItalic(true);
			textMode.getStyle().setColor(TextFormatting.AQUA);
			textCondition.getStyle().setBold(true);

			playerIn.addChatComponentMessage(new TextComponentString(textItem.getFormattedText() + " -> " + textMode.getFormattedText() + " : " + textCondition.getFormattedText()));

			this.setFellingMode(itemStackIn, !isFellingMode);
		}

		playerIn.swingArm(hand);

		worldIn.playSound(playerIn, new BlockPos(playerIn), SoundEvents.UI_BUTTON_CLICK, SoundCategory.BLOCKS, 1.0F, 1.0F);

		return new ActionResult(EnumActionResult.SUCCESS, itemStackIn);
	}

	// TODO /* ======================================== MOD START =====================================*/

	private boolean isFellingMode(ItemStack stack)
	{
		NBTTagCompound nbtStack = stack.getTagCompound();

		if (nbtStack != null && nbtStack.hasKey(TanpopoNBTTags.ITEM_TOOL_FELLING_AXE_MODE, 3))
		{
			int value = nbtStack.getInteger(TanpopoNBTTags.ITEM_TOOL_FELLING_AXE_MODE);

			return (value == 1);
		}

		return false;
	}

	private void setFellingMode(ItemStack stack, boolean isMode)
	{
		NBTTagCompound nbtStack = stack.getTagCompound();

		if (nbtStack == null)
		{
			nbtStack = new NBTTagCompound();
		}

		int value = isMode ? (1) : (0);

		nbtStack.setInteger(TanpopoNBTTags.ITEM_TOOL_FELLING_AXE_MODE, value);

		stack.setTagCompound(nbtStack);
	}

	private boolean canStartFelling(ItemStack stack, World world, BlockPos pos)
	{
		if (this.isFellingMode(stack) && world.getBlockState(pos).getBlock().isWood(world, pos))
		{
			return true;
		}

		return false;
	}

	private boolean isFellingBlocks(World world, BlockPos pos)
	{
		IBlockState state = world.getBlockState(pos);

		if (state.getBlock().isWood(world, pos))
		{
			return true;
		}

		if (state.getBlock().isLeaves(state, world, pos))
		{
			return true;
		}

		return false;
	}

	private Set<BlockPos> getFellingBlockPos(Set<BlockPos> posSet, World world, BlockPos pos)
	{
		for (EnumFacing facing : EnumFacing.VALUES)
		{
			if (this.fellingBlockLimit < posSet.size())
			{
				return posSet;
			}

			BlockPos posFacing = pos.offset(facing);

			if (this.isFellingBlocks(world, posFacing))
			{
				if (posSet.add(posFacing))
				{
					this.getFellingBlockPos(posSet, world, posFacing);
				}
			}
		}

		return posSet;
	}

}