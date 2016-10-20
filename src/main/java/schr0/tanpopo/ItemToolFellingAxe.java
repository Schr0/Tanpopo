package schr0.tanpopo;

import java.util.LinkedHashSet;
import java.util.Set;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemToolFellingAxe extends ItemModeAttachedTool
{

	private static final Set<Material> EFFECTIVE_MATERIALS = Sets.newHashSet(new Material[]
	{
			Material.WOOD, Material.LEAVES, Material.VINE,
	});

	private static final int FELLING_MODE_BLOCK_LIMIT = TanpopoConfiguration.fellingModeBlockLimit;

	private static final int COOLDWON_TIME = (10 * 20);

	public ItemToolFellingAxe()
	{
		super(8.0F, -3.1F, TanpopoToolMaterials.TIER_0);
	}

	@Override
	public Set<String> getToolClasses(ItemStack stack)
	{
		return ImmutableSet.of("axe");
	}

	@Override
	public boolean canHarvestBlock(IBlockState blockIn)
	{
		for (Material material : EFFECTIVE_MATERIALS)
		{
			if (material == blockIn.getMaterial())
			{
				return (blockIn.getBlock().getHarvestLevel(blockIn) <= this.getToolMaterial().getHarvestLevel());
			}
		}

		return super.canHarvestBlock(blockIn);
	}

	@Override
	public boolean onBlockDestroyed(ItemStack stack, World worldIn, IBlockState state, BlockPos pos, EntityLivingBase entityLiving)
	{
		if (!this.canFellingAction(stack, worldIn, pos, entityLiving))
		{
			return super.onBlockDestroyed(stack, worldIn, state, pos, entityLiving);
		}

		EntityPlayer player = (EntityPlayer) entityLiving;
		Set<BlockPos> posSet = new LinkedHashSet<>();

		this.getFellingBlockPos(posSet, worldIn, pos);

		for (BlockPos posFelling : posSet)
		{
			for (BlockPos posAround : BlockPos.getAllInBox(posFelling.add(-1, -1, -1), posFelling.add(1, 1, 1)))
			{
				if (posAround.getY() < pos.getY())
				{
					continue;
				}

				if (this.isFellingBlocks(worldIn, posFelling))
				{
					IBlockState stateAround = worldIn.getBlockState(posFelling);
					Block blockAround = stateAround.getBlock();

					if (blockAround.isLeaves(stateAround, worldIn, posFelling))
					{
						blockAround.dropBlockAsItem(worldIn, posFelling, stateAround, 0);

						worldIn.setBlockToAir(posFelling);
					}
					else
					{
						blockAround.harvestBlock(worldIn, player, posFelling, stateAround, worldIn.getTileEntity(posFelling), stack);

						if (EnchantmentHelper.getEnchantmentLevel(Enchantments.SILK_TOUCH, stack) <= 0)
						{
							blockAround.dropXpOnBlockBreak(worldIn, posFelling, blockAround.getExpDrop(stateAround, worldIn, posFelling, EnchantmentHelper.getEnchantmentLevel(Enchantments.FORTUNE, stack)));
						}

						worldIn.destroyBlock(posFelling, false);

						if ((double) state.getBlockHardness(worldIn, pos) != 0.0D)
						{
							stack.damageItem(1, player);
						}
					}
				}
			}
		}

		return true;
	}

	@Override
	public EnumActionResult onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		if (this.canChageMode(playerIn) || !playerIn.canPlayerEdit(pos, facing, stack))
		{
			return super.onItemUse(stack, playerIn, worldIn, pos, hand, facing, hitX, hitY, hitZ);
		}

		IBlockState state = worldIn.getBlockState(pos);
		Block block = state.getBlock();

		if (block.canHarvestBlock(worldIn, pos, playerIn) && block.canSilkHarvest(worldIn, pos, state, playerIn))
		{
			ItemStack stackCopy = stack.copy();

			stackCopy.addEnchantment(Enchantments.SILK_TOUCH, 1);

			block.harvestBlock(worldIn, playerIn, pos, state, worldIn.getTileEntity(pos), stackCopy);

			worldIn.setBlockToAir(pos);

			stack.damageItem(1, playerIn);

			worldIn.playSound(playerIn, new BlockPos(playerIn), SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.BLOCKS, 1.0F, 1.0F);

			int coolDwonTime = COOLDWON_TIME - (EnchantmentHelper.getEnchantmentLevel(Enchantments.EFFICIENCY, stack) * 20);

			coolDwonTime = Math.max((5 * 20), coolDwonTime);

			playerIn.getCooldownTracker().setCooldown(this, coolDwonTime);

			return EnumActionResult.SUCCESS;
		}

		return super.onItemUse(stack, playerIn, worldIn, pos, hand, facing, hitX, hitY, hitZ);
	}

	// TODO /* ======================================== MOD START =====================================*/

	@Override
	@SideOnly(Side.CLIENT)
	public TextComponentTranslation getModeName()
	{
		return new TextComponentTranslation("item.tool_felling_axe.mode_name", new Object[0]);
	}

	private boolean canFellingAction(ItemStack stack, World world, BlockPos pos, EntityLivingBase entityLiving)
	{
		if (this.isMode(stack) && (entityLiving instanceof EntityPlayer))
		{
			return world.getBlockState(pos).getBlock().isWood(world, pos);
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
		if (FELLING_MODE_BLOCK_LIMIT < posSet.size())
		{
			return posSet;
		}

		for (EnumFacing facing : EnumFacing.VALUES)
		{
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
