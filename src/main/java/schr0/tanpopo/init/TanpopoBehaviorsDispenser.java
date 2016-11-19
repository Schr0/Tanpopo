package schr0.tanpopo.init;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDispenser;
import net.minecraft.dispenser.BehaviorDefaultDispenseItem;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import schr0.tanpopo.block.BlockMassPlant;
import schr0.tanpopo.item.ItemMaterialMass;

public class TanpopoBehaviorsDispenser
{

	private static final BehaviorDefaultDispenseItem ITEM_MATERIAL_MASS = new BehaviorDefaultDispenseItem()
	{

		@Override
		public ItemStack dispenseStack(IBlockSource source, ItemStack stack)
		{
			if (8 <= stack.stackSize)
			{
				World world = source.getWorld();
				EnumFacing facing = (EnumFacing) source.func_189992_e().getValue(BlockDispenser.FACING);
				int meta = (ItemMaterialMass.isWet(stack)) ? 4 : 0;

				BehaviorDefaultDispenseItem.doDispense(world, new ItemStack(TanpopoBlocks.MASS_PLANT, 1, meta), 6, facing, BlockDispenser.getDispensePosition(source));

				stack.stackSize -= 8;
			}

			return stack;
		}

	};

	private static final BehaviorDefaultDispenseItem BLOCK_MASS_PLANT = new BehaviorDefaultDispenseItem()
	{

		@Override
		public ItemStack dispenseStack(IBlockSource source, ItemStack stack)
		{
			World world = source.getWorld();
			BlockPos posFacing = source.getBlockPos().offset((EnumFacing) source.func_189992_e().getValue(BlockDispenser.FACING));
			BlockMassPlant blockPlantMass = (BlockMassPlant) Block.getBlockFromItem(stack.getItem());

			if (blockPlantMass.isReplaceable(world, posFacing))
			{
				world.setBlockState(posFacing, blockPlantMass.getStateFromMeta(stack.getItemDamage()), 2);

				--stack.stackSize;
			}

			return stack;
		}

	};

	public void init()
	{
		BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(TanpopoItems.MATERIAL_MASS, ITEM_MATERIAL_MASS);
		BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(Item.getItemFromBlock(TanpopoBlocks.MASS_PLANT), BLOCK_MASS_PLANT);
	}

}