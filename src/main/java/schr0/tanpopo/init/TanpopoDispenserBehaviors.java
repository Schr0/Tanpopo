package schr0.tanpopo.init;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDispenser;
import net.minecraft.dispenser.BehaviorDefaultDispenseItem;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.dispenser.IPosition;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import schr0.tanpopo.block.BlockMassPlant;
import schr0.tanpopo.item.ItemMaterialMass;

public class TanpopoDispenserBehaviors
{

	public void init()
	{
		register();
	}

	private static void register()
	{
		BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(TanpopoItems.MATERIAL_MASS, new BehaviorDefaultDispenseItem()
		{

			@Override
			public ItemStack dispenseStack(IBlockSource source, ItemStack stack)
			{
				if (8 <= stack.stackSize)
				{
					World world = source.getWorld();
					EnumFacing face = (EnumFacing) source.func_189992_e().getValue(BlockDispenser.FACING);
					IPosition iPos = BlockDispenser.getDispensePosition(source);
					int meta = (ItemMaterialMass.isWet(stack)) ? 4 : 0;

					BehaviorDefaultDispenseItem.doDispense(world, new ItemStack(TanpopoBlocks.MASS_PLANT, 1, meta), 6, face, iPos);

					stack.stackSize -= 8;
				}

				return stack;
			}

		});

		BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(Item.getItemFromBlock(TanpopoBlocks.MASS_PLANT), new BehaviorDefaultDispenseItem()
		{

			@Override
			public ItemStack dispenseStack(IBlockSource source, ItemStack stack)
			{
				World world = source.getWorld();
				BlockPos pos = source.getBlockPos().offset((EnumFacing) source.func_189992_e().getValue(BlockDispenser.FACING));
				BlockMassPlant blockPlantMass = (BlockMassPlant) Block.getBlockFromItem(stack.getItem());

				if (blockPlantMass.isReplaceable(world, pos))
				{
					world.setBlockState(pos, blockPlantMass.getStateFromMeta(stack.getItemDamage()));

					--stack.stackSize;
				}

				return stack;
			}

		});
	}

}
