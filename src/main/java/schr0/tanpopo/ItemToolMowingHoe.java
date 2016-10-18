package schr0.tanpopo;

import java.util.Set;

import javax.annotation.Nullable;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;

import net.minecraft.block.BlockDirt;
import net.minecraft.block.BlockFarmland;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemToolMowingHoe extends ItemModeAttachedTool
{

	private static final Set<Material> EFFECTIVE_MATERIALS = Sets.newHashSet(new Material[]
	{
			Material.GROUND, Material.GRASS, Material.SAND, Material.CLAY, Material.SNOW,
	});

	public ItemToolMowingHoe()
	{
		super(1.5F, -3.0F, TanpopoToolMaterials.TIER_0);
	}

	@Override
	public Set<String> getToolClasses(ItemStack stack)
	{
		return ImmutableSet.of("shovel");
	}

	@Override
	public boolean canHarvestBlock(IBlockState blockIn)
	{
		if (blockIn.getBlock() == Blocks.SNOW_LAYER || blockIn.getBlock() == Blocks.SNOW)
		{
			return true;
		}

		return super.canHarvestBlock(blockIn);
	}

	@Override
	public float getStrVsBlock(ItemStack stack, IBlockState state)
	{
		for (Material material : EFFECTIVE_MATERIALS)
		{
			if ((material == state.getMaterial()) && this.canHarvestBlock(state))
			{
				return this.efficiencyOnProperMaterial;
			}
		}

		if (state.getBlock() instanceof IPlantable)
		{
			return this.efficiencyOnProperMaterial;
		}

		return super.getStrVsBlock(stack, state);
	}

	@Override
	public EnumActionResult onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		if (playerIn.isSneaking() || !playerIn.canPlayerEdit(pos, facing, stack))
		{
			return super.onItemUse(stack, playerIn, worldIn, pos, hand, facing, hitX, hitY, hitZ);
		}

		if ((facing != EnumFacing.DOWN) && worldIn.isAirBlock(pos.up()))
		{
			IBlockState state = worldIn.getBlockState(pos);

			if (this.getTillBlockResult(state, worldIn, pos) != null)
			{
				worldIn.setBlockState(pos, this.getTillBlockResult(state, worldIn, pos));

				stack.damageItem(1, playerIn);

				worldIn.playSound(playerIn, pos, SoundEvents.ITEM_HOE_TILL, SoundCategory.BLOCKS, 1.0F, 1.0F);

				return EnumActionResult.SUCCESS;
			}
		}

		return super.onItemUse(stack, playerIn, worldIn, pos, hand, facing, hitX, hitY, hitZ);
	}

	// TODO /* ======================================== MOD START =====================================*/

	@Override
	@SideOnly(Side.CLIENT)
	public TextComponentTranslation getModeName()
	{
		return new TextComponentTranslation("item.tool_mowing_hoe.mode_name", new Object[0]);
	}

	@Nullable
	private IBlockState getTillBlockResult(IBlockState state, World world, BlockPos pos)
	{
		IBlockState stateFarmland = this.getMoistureFarmland(world, pos);

		if (state.getBlock() == Blocks.GRASS)
		{
			return Blocks.GRASS_PATH.getDefaultState();
		}

		if (state.getBlock() == Blocks.GRASS_PATH)
		{
			return stateFarmland;
		}

		if (state.getBlock() == Blocks.DIRT)
		{
			switch ((BlockDirt.DirtType) state.getValue(BlockDirt.VARIANT))
			{
				case DIRT :

					return stateFarmland;

				case COARSE_DIRT :

					return Blocks.DIRT.getDefaultState().withProperty(BlockDirt.VARIANT, BlockDirt.DirtType.DIRT);

				default :
					break;
			}
		}

		if (state.getBlock() == Blocks.FARMLAND)
		{
			if ((state.getBlock().getMetaFromState(state) < 7) && (stateFarmland.getBlock().getMetaFromState(stateFarmland) == 7))
			{
				return stateFarmland;
			}
		}

		return (IBlockState) null;
	}

	private IBlockState getMoistureFarmland(World world, BlockPos pos)
	{
		IBlockState state = Blocks.FARMLAND.getDefaultState().withProperty(BlockFarmland.MOISTURE, 6);

		for (BlockPos.MutableBlockPos blockpos$mutableblockpos : BlockPos.getAllInBoxMutable(pos.add(-4, 0, -4), pos.add(4, 1, 4)))
		{
			if (world.getBlockState(blockpos$mutableblockpos).getMaterial() == Material.WATER)
			{
				return state.withProperty(BlockFarmland.MOISTURE, 7);
			}
		}

		return state;
	}

}
