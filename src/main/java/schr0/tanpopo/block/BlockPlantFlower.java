package schr0.tanpopo.block;

import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockBush;
import net.minecraft.block.IGrowable;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import schr0.tanpopo.init.TanpopoBlocks;

public class BlockPlantFlower extends BlockBush implements IGrowable
{

	private static final int META_MAX = TanpopoBlocks.META_PLANT_FLOWER;

	public static final PropertyInteger PROPERTY_AGE = PropertyInteger.create("age", 0, META_MAX);

	public static final AxisAlignedBB[] AABB = new AxisAlignedBB[]
	{
			new AxisAlignedBB(0.3D, 0.0D, 0.3D, 0.6D, 0.20D, 0.6D),
			new AxisAlignedBB(0.3D, 0.0D, 0.3D, 0.6D, 0.30D, 0.6D),
			new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.40D, 1.0D),
			new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.50D, 1.0D),
			new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.80D, 1.0D),
			new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.85D, 1.0D),
			new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.90D, 1.0D),
			new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.95D, 1.0D),
			new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.95D, 1.0D),
			new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 1.00D, 1.0D),
			new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 1.00D, 1.0D),
			new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 1.00D, 1.0D),
			new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 1.00D, 1.0D),
			new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 1.00D, 1.0D),
			new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 1.00D, 1.0D),
			new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 1.00D, 1.0D),
	};

	public BlockPlantFlower()
	{
		super(Material.PLANTS);
		this.setDefaultState(this.getBlockState().getBaseState().withProperty(this.getAgeProperty(), Integer.valueOf(0)));
		this.setSoundType(SoundType.PLANT);
		this.setHardness(0.0F);
		this.setTickRandomly(true);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void getSubBlocks(Item itemIn, CreativeTabs tab, List<ItemStack> list)
	{
		for (int meta = 0; meta <= META_MAX; meta++)
		{
			list.add(new ItemStack(itemIn, 1, meta));
		}
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
	{
		return AABB[((Integer) state.getValue(this.getAgeProperty())).intValue()];
	}

	@Override
	public int damageDropped(IBlockState state)
	{
		return (Integer) state.getValue(this.getAgeProperty()).intValue();
	}

	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		return this.withAge(meta);
	}

	@Override
	public int getMetaFromState(IBlockState state)
	{
		return this.getAge(state);
	}

	@Override
	protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, new IProperty[]
		{
				PROPERTY_AGE
		});
	}

	@Override
	protected boolean canSustainBush(IBlockState state)
	{
		return (state.getBlock() == TanpopoBlocks.PLANT_ROOTS);
	}

	@Override
	public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
	{
		super.updateTick(worldIn, pos, state, rand);

		if (this.canUpdateGrow(worldIn, pos))
		{
			if (this.hasDownRoots(worldIn, pos))
			{
				int chance = this.getGrowChance(worldIn, pos, this.getAge(state));

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
	public boolean canGrow(World worldIn, BlockPos pos, IBlockState state, boolean isClient)
	{
		return !this.isMaxAge(state);
	}

	@Override
	public boolean canUseBonemeal(World worldIn, Random rand, BlockPos pos, IBlockState state)
	{
		return true;
	}

	@Override
	public void grow(World worldIn, Random rand, BlockPos pos, IBlockState state)
	{
		if (this.hasDownRoots(worldIn, pos))
		{
			int age = this.getAge(state);

			if (age < this.getMaxAge())
			{
				worldIn.setBlockState(pos, this.withAge(age + 1), 2);
			}
		}
		else
		{
			BlockPos posDown = pos.down();
			IBlockState stateDown = worldIn.getBlockState(posDown);
			Block blockDown = stateDown.getBlock();

			worldIn.playEvent(2001, posDown, Block.getStateId(state));
			worldIn.setBlockState(posDown, TanpopoBlocks.PLANT_ROOTS.getDefaultState(), 2);

			if (!worldIn.isRemote)
			{
				blockDown.dropBlockAsItem(worldIn, pos, stateDown, 0);
			}
		}
	}

	// TODO /* ======================================== MOD START =====================================*/

	public PropertyInteger getAgeProperty()
	{
		return PROPERTY_AGE;
	}

	public int getMaxAge()
	{
		return META_MAX;
	}

	public int getAge(IBlockState state)
	{
		return ((Integer) state.getValue(this.getAgeProperty())).intValue();
	}

	public IBlockState withAge(int age)
	{
		return this.getDefaultState().withProperty(this.getAgeProperty(), Integer.valueOf(age));
	}

	public boolean isMaxAge(IBlockState state)
	{
		return (this.getMaxAge() <= ((Integer) state.getValue(this.getAgeProperty())).intValue());
	}

	private boolean hasDownRoots(World world, BlockPos pos)
	{
		return (world.getBlockState(pos.down()).getBlock() == TanpopoBlocks.PLANT_ROOTS);
	}

	private boolean canUpdateGrow(World world, BlockPos pos)
	{
		return world.isDaytime() && (8 < world.getLightFromNeighbors(pos.up()));
	}

	private int getCheckPosXyz()
	{
		return 1;
	}

	private int getGrowChance(World world, BlockPos pos, int age)
	{
		int chance = 2;
		int checkPosXyz = this.getCheckPosXyz();

		switch (age)
		{
			case 0 :
				chance = 4;
				break;

			case 5 :
				chance = 6;
				break;

			case 10 :
				chance = 8;
				break;
		}

		for (BlockPos posAround : BlockPos.getAllInBox(pos.add(-checkPosXyz, -checkPosXyz, -checkPosXyz), pos.add(checkPosXyz, checkPosXyz, checkPosXyz)))
		{
			if (world.getBlockState(posAround).getBlock() == TanpopoBlocks.PLANT_ROOTS)
			{
				--chance;
			}
		}

		chance = Math.max(2, chance);

		return chance;
	}

}
