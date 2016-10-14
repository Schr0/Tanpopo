package schr0.tanpopo;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.IGrowable;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockPlantRoots extends Block implements IPlantable, IGrowable
{

	public BlockPlantRoots()
	{
		super(Material.GRASS);
		this.setSoundType(SoundType.PLANT);
		this.setHardness(2.0F);
		this.setResistance(5.0F);
		this.setTickRandomly(true);
		this.setHarvestLevel("shovel", 0);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public BlockRenderLayer getBlockLayer()
	{
		return BlockRenderLayer.CUTOUT_MIPPED;
	}

	@Override
	public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
	{
		super.updateTick(worldIn, pos, state, rand);

		if (canUpdateGrow(worldIn, pos))
		{
			if (this.hasUpPlant(worldIn, pos))
			{
				int chance = getGrowChance(worldIn, pos);

				if (rand.nextInt(chance) == 0)
				{
					this.grow(worldIn, rand, pos, state);
				}
			}
			else
			{
				this.grow(worldIn, rand, pos, state);
			}
		}
	}

	@Override
	public EnumPlantType getPlantType(IBlockAccess world, BlockPos pos)
	{
		return EnumPlantType.Plains;
	}

	@Override
	public IBlockState getPlant(IBlockAccess world, BlockPos pos)
	{
		IBlockState state = world.getBlockState(pos);

		if (state.getBlock() != this)
		{
			return getDefaultState();
		}

		return state;
	}

	@Override
	public boolean canGrow(World worldIn, BlockPos pos, IBlockState state, boolean isClient)
	{
		return !this.hasUpPlant(worldIn, pos);
	}

	@Override
	public boolean canUseBonemeal(World worldIn, Random rand, BlockPos pos, IBlockState state)
	{
		return true;
	}

	@Override
	public void grow(World worldIn, Random rand, BlockPos pos, IBlockState state)
	{
		if (this.hasUpPlant(worldIn, pos))
		{
			for (BlockPos posAround : BlockPos.getAllInBox(pos.add(-1, -1, -1), pos.add(1, 1, 1)))
			{
				IBlockState stateAround = worldIn.getBlockState(posAround);
				Block blockAround = stateAround.getBlock();

				if ((posAround == pos) || (blockAround == TanpopoBlocks.PLANT_ROOTS))
				{
					continue;
				}

				if (blockAround.canSustainPlant(stateAround, worldIn, posAround, EnumFacing.UP, new BlockPlantFlower()))
				{
					Block blockUp = worldIn.getBlockState(posAround.up()).getBlock();

					if (blockUp == TanpopoBlocks.PLANT_FLOWER)
					{
						worldIn.playEvent(2001, posAround, Block.getStateId(state));
					}
					else
					{
						worldIn.destroyBlock(posAround, false);
					}

					worldIn.setBlockState(posAround, TanpopoBlocks.PLANT_ROOTS.getDefaultState(), 2);

					if (!worldIn.isRemote)
					{
						blockAround.dropBlockAsItem(worldIn, posAround.up(), stateAround, 0);
					}
				}
			}
		}
		else
		{
			BlockPos posUp = pos.up();

			if (worldIn.getBlockState(posUp).getBlock().isReplaceable(worldIn, posUp))
			{
				if (worldIn.isAirBlock(posUp))
				{
					worldIn.playEvent(2001, posUp, Block.getStateId(state));
				}
				else
				{
					worldIn.destroyBlock(posUp, true);
				}

				worldIn.setBlockState(posUp, TanpopoBlocks.PLANT_FLOWER.getDefaultState(), 2);
			}
		}
	}

	// TODO /* ======================================== MOD START =====================================*/

	private boolean hasUpPlant(World world, BlockPos pos)
	{
		return (world.getBlockState(pos.up()).getBlock() == TanpopoBlocks.PLANT_FLOWER);
	}

	private boolean canUpdateGrow(World world, BlockPos pos)
	{
		return world.isDaytime() && (8 < world.getLightFromNeighbors(pos.up()));
	}

	private int getCheckPosXyz()
	{
		return 4;
	}

	private int getGrowChance(World world, BlockPos pos)
	{
		int chance = 2;
		int checkPosXyz = this.getCheckPosXyz();

		for (BlockPos posAround : BlockPos.getAllInBox(pos.add(-checkPosXyz, -checkPosXyz, -checkPosXyz), pos.add(checkPosXyz, checkPosXyz, checkPosXyz)))
		{
			if (world.getBlockState(posAround).getBlock() == this)
			{
				++chance;
			}
		}

		chance = Math.min(8, chance);

		return chance;
	}

}
