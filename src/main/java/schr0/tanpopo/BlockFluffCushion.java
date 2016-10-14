package schr0.tanpopo;

import net.minecraft.block.BlockColored;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockFluffCushion extends BlockColored
{

	public BlockFluffCushion()
	{
		super(Material.CLOTH);
		this.setSoundType(SoundType.CLOTH);
		this.setHardness(0.4F);
	}

	@Override
	public int getFireSpreadSpeed(IBlockAccess world, BlockPos pos, EnumFacing face)
	{
		return 30;
	}

	@Override
	public int getFlammability(IBlockAccess world, BlockPos pos, EnumFacing face)
	{
		return 60;
	}

	@Override
	public void onFallenUpon(World worldIn, BlockPos pos, Entity entityIn, float fallDistance)
	{
		entityIn.fall(fallDistance, 0.0F);
	}

	@Override
	public void onLanded(World worldIn, Entity entityIn)
	{
		if (entityIn.isSneaking())
		{
			super.onLanded(worldIn, entityIn);
		}
		else
		{
			if (entityIn.motionY <= (-0.42D))
			{
				this.onSpring(worldIn, entityIn);
			}
			else
			{
				super.onLanded(worldIn, entityIn);
			}
		}
	}

	// TODO /* ======================================== MOD START =====================================*/

	public void onSpring(World world, Entity entity)
	{
		double boost = 0.5D;

		for (int down = 0; down <= 4; down++)
		{
			BlockPos posDownBlock = (new BlockPos(entity)).down().down(down);

			if (world.isAirBlock(posDownBlock))
			{
				break;
			}

			if (world.getBlockState(posDownBlock).getBlock() == this)
			{
				boost += 0.5D;
			}
		}

		boost = Math.min(3.0D, boost);

		entity.motionY = -(entity.motionY * boost);
	}

}
