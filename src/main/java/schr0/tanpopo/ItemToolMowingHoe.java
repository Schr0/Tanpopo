package schr0.tanpopo;

import java.util.Set;

import javax.annotation.Nullable;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;

import net.minecraft.block.Block;
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
import net.minecraft.util.registry.RegistryDefaulted;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemToolMowingHoe extends ItemModeAttachedTool
{

	public static class TillBlock
	{

		public IBlockState getResultBlockState(World world, BlockPos pos, IBlockState state)
		{
			return (IBlockState) null;
		}

	}

	private static final Set<Material> EFFECTIVE_MATERIALS = Sets.newHashSet(new Material[]
	{
			Material.GRASS, Material.GOURD, Material.CACTUS, Material.CORAL, Material.GROUND, Material.LEAVES, Material.PLANTS, Material.VINE
	});

	private static final RegistryDefaulted<Block, TillBlock> TILL_BLOCK_REGISTRY = new RegistryDefaulted((TillBlock) null);

	static
	{
		TILL_BLOCK_REGISTRY.putObject(Blocks.DIRT, new TillBlock()
		{

			@Override
			public IBlockState getResultBlockState(World world, BlockPos pos, IBlockState state)
			{
				switch ((BlockDirt.DirtType) state.getValue(BlockDirt.VARIANT))
				{
					case DIRT :

						return Blocks.FARMLAND.getDefaultState();

					case COARSE_DIRT :

						return Blocks.DIRT.getDefaultState().withProperty(BlockDirt.VARIANT, BlockDirt.DirtType.DIRT);

					default :
						break;
				}

				return super.getResultBlockState(world, pos, state);
			}

		});

		TILL_BLOCK_REGISTRY.putObject(Blocks.GRASS, new TillBlock()
		{

			@Override
			public IBlockState getResultBlockState(World world, BlockPos pos, IBlockState state)
			{
				return Blocks.GRASS_PATH.getDefaultState();
			}

		});

		TILL_BLOCK_REGISTRY.putObject(Blocks.GRASS_PATH, new TillBlock()
		{

			@Override
			public IBlockState getResultBlockState(World world, BlockPos pos, IBlockState state)
			{
				return Blocks.FARMLAND.getDefaultState();
			}

		});

		TILL_BLOCK_REGISTRY.putObject(Blocks.FARMLAND, new TillBlock()
		{

			@Override
			public IBlockState getResultBlockState(World world, BlockPos pos, IBlockState state)
			{
				if (state.getBlock().getMetaFromState(state) < 7 && this.hasWater(world, pos))
				{
					return Blocks.FARMLAND.getDefaultState().withProperty(BlockFarmland.MOISTURE, 7);
				}

				return super.getResultBlockState(world, pos, state);
			}

			private boolean hasWater(World worldIn, BlockPos pos)
			{
				for (BlockPos.MutableBlockPos blockpos$mutableblockpos : BlockPos.getAllInBoxMutable(pos.add(-4, 0, -4), pos.add(4, 1, 4)))
				{
					if (worldIn.getBlockState(blockpos$mutableblockpos).getMaterial() == Material.WATER)
					{
						return true;
					}
				}

				return false;
			}

		});

	}

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

		IBlockState state = worldIn.getBlockState(pos);
		Block block = state.getBlock();

		if ((facing != EnumFacing.DOWN) && worldIn.isAirBlock(pos.up()) && (this.getTillBlock(block) != null))
		{
			TillBlock tillBlock = this.getTillBlock(block);

			if (tillBlock.getResultBlockState(worldIn, pos, state) != null)
			{
				worldIn.setBlockState(pos, tillBlock.getResultBlockState(worldIn, pos, state));

				stack.damageItem(1, playerIn);

				worldIn.playSound(playerIn, pos, SoundEvents.ITEM_HOE_TILL, SoundCategory.BLOCKS, 1.0F, 1.0F);

				return EnumActionResult.SUCCESS;
			}
		}

		if (block instanceof IPlantable)
		{

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
	private TillBlock getTillBlock(Block block)
	{
		if (TILL_BLOCK_REGISTRY.getObject(block) == null)
		{
			return (TillBlock) null;
		}

		return (TillBlock) TILL_BLOCK_REGISTRY.getObject(block);
	}

}
