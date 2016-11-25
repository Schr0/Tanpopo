package schr0.tanpopo.init;

import net.minecraft.block.BlockDispenser;
import net.minecraft.dispenser.BehaviorDefaultDispenseItem;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import schr0.tanpopo.item.ItemMaterialMass;

public class TanpopoBehaviorsDispenser
{

	private static final BehaviorDefaultDispenseItem ITEM_MATERIAL_MASS = new BehaviorDefaultDispenseItem()
	{
		private boolean succeeded = true;

		@Override
		public ItemStack dispenseStack(IBlockSource source, ItemStack stack)
		{
			if (ItemMaterialMass.isWet(stack))
			{
				World world = source.getWorld();
				BlockPos posFacing = source.getBlockPos().offset((EnumFacing) source.func_189992_e().getValue(BlockDispenser.FACING));

				if (!ItemMaterialMass.applyEssence(stack, world, posFacing))
				{
					this.succeeded = false;
				}

				return stack;
			}

			return super.dispenseStack(source, stack);
		}

		@Override
		protected void playDispenseSound(IBlockSource source)
		{
			if (this.succeeded)
			{
				source.getWorld().playEvent(1000, source.getBlockPos(), 0);
			}
			else
			{
				source.getWorld().playEvent(1001, source.getBlockPos(), 0);
			}
		}

	};

	public void init()
	{
		BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(TanpopoItems.MATERIAL_MASS, ITEM_MATERIAL_MASS);
	}

}
