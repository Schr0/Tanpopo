package schr0.tanpopo.block;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public class BlockEssenceCharcoalBlock extends Block
{

	public BlockEssenceCharcoalBlock()
	{
		super(Material.ROCK);
		this.setSoundType(SoundType.STONE);
		this.setHardness(5.0F);
		this.setResistance(10.0F);
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

}
