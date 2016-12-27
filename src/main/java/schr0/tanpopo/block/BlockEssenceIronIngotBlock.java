package schr0.tanpopo.block;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

public class BlockEssenceIronIngotBlock extends Block
{

	public BlockEssenceIronIngotBlock()
	{
		super(Material.IRON);
		this.setSoundType(SoundType.METAL);
		this.setHardness(3.0F);
		this.setResistance(10.0F);
	}

}
