package schr0.tanpopo.block;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

public class BlockEssenceSolidFuelBlock extends Block
{

	public BlockEssenceSolidFuelBlock()
	{
		super(Material.ROCK);
		this.setSoundType(SoundType.STONE);
		this.setHardness(5.0F);
		this.setResistance(10.0F);
	}

}
