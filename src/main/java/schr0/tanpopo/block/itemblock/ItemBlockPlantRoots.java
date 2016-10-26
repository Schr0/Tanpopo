package schr0.tanpopo.block.itemblock;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import schr0.tanpopo.block.BlockPlantFlower;
import schr0.tanpopo.init.TanpopoBlocks;

public class ItemBlockPlantRoots extends ItemBlock
{

	public ItemBlockPlantRoots(Block block)
	{
		super(block);
	}

	@Override
	public EnumActionResult onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		IBlockState state = worldIn.getBlockState(pos);
		Block block = state.getBlock();

		if (playerIn.canPlayerEdit(pos, facing, stack) && block.canSustainPlant(state, worldIn, pos, EnumFacing.UP, new BlockPlantFlower()) && (block != this.getBlock()))
		{
			worldIn.destroyBlock(pos, true);

			worldIn.setBlockState(pos, TanpopoBlocks.PLANT_ROOTS.getDefaultState());

			--stack.stackSize;

			return EnumActionResult.SUCCESS;
		}

		return EnumActionResult.FAIL;
	}

}
