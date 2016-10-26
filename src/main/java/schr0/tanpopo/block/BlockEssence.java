package schr0.tanpopo.block;

import java.util.Random;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fluids.BlockFluidClassic;
import schr0.tanpopo.init.TanpopoBlocks;
import schr0.tanpopo.init.TanpopoFluids;

public class BlockEssence extends BlockFluidClassic
{

	public BlockEssence()
	{
		super(TanpopoFluids.ESSENCE, TanpopoBlocks.MATERIAL_LIQUID_ESSENCE);
		this.setHardness(100.0F);
		this.setLightOpacity(3);
	}

	@Override
	public int getFireSpreadSpeed(IBlockAccess world, BlockPos pos, EnumFacing face)
	{
		return 300;
	}

	@Override
	public int getFlammability(IBlockAccess world, BlockPos pos, EnumFacing face)
	{
		return 300;
	}

	@Override
	public Boolean isEntityInsideMaterial(IBlockAccess world, BlockPos blockpos, IBlockState iblockstate, Entity entity, double yToTest, Material materialIn, boolean testingHead)
	{
		if (materialIn.isLiquid())
		{
			return Boolean.TRUE;
		}

		return super.isEntityInsideMaterial(world, blockpos, iblockstate, entity, yToTest, materialIn, testingHead);
	}

	@Override
	public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entityIn)
	{
		super.onEntityCollidedWithBlock(worldIn, pos, state, entityIn);

		if (this.canCatchFire(worldIn, pos))
		{
			this.onCatchFire(worldIn, pos, state);
		}
	}

	@Override
	public void onBlockAdded(World world, BlockPos pos, IBlockState state)
	{
		super.onBlockAdded(world, pos, state);

		if (this.canCatchFire(world, pos))
		{
			this.onCatchFire(world, pos, state);
		}
	}

	@Override
	public void neighborChanged(IBlockState state, World world, BlockPos pos, Block neighborBlock)
	{
		super.neighborChanged(state, world, pos, neighborBlock);

		if (this.canCatchFire(world, pos))
		{
			this.onCatchFire(world, pos, state);
		}
	}

	@Override
	public void updateTick(World world, BlockPos pos, IBlockState state, Random rand)
	{
		super.updateTick(world, pos, state, rand);

		if (this.canCatchFire(world, pos))
		{
			this.onCatchFire(world, pos, state);
		}
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, @Nullable ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ)
	{
		if (this.canCatchFire(worldIn, pos))
		{
			this.onCatchFire(worldIn, pos, state);

			return true;
		}

		return super.onBlockActivated(worldIn, pos, state, playerIn, hand, heldItem, side, hitX, hitY, hitZ);
	}

	// TODO /* ======================================== MOD START =====================================*/

	private int getCheckPosXyz()
	{
		return 4;
	}

	private boolean canCatchFire(World world, BlockPos pos)
	{
		int checkPosXyz = this.getCheckPosXyz();

		for (Entity entityAround : world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(pos).expandXyz((double) checkPosXyz)))
		{
			if (entityAround.isBurning())
			{
				return true;
			}
		}

		for (BlockPos posAround : BlockPos.getAllInBox(pos.add(-checkPosXyz, -checkPosXyz, -checkPosXyz), pos.add(checkPosXyz, checkPosXyz, checkPosXyz)))
		{
			if (world.getBlockState(posAround).getMaterial() == Material.FIRE)
			{
				return true;
			}
		}

		return false;
	}

	private void onCatchFire(World world, BlockPos pos, IBlockState state)
	{
		if (world.isRemote)
		{
			return;
		}

		float strength = (((Integer) state.getValue(LEVEL)).intValue() == 0) ? (6.0F) : (2.0F);

		world.createExplosion(null, pos.getX(), pos.getY(), pos.getZ(), strength, true);

		world.setBlockToAir(pos);

		int checkPosXyz = getCheckPosXyz();

		for (BlockPos posAround : BlockPos.getAllInBox(pos.add(-checkPosXyz, -checkPosXyz, -checkPosXyz), pos.add(checkPosXyz, checkPosXyz, checkPosXyz)))
		{
			if (world.isAirBlock(posAround))
			{
				world.setBlockState(posAround, Blocks.FIRE.getDefaultState(), 2);
			}
		}
	}

}
