package schr0.tanpopo;

import javax.annotation.Nullable;

import net.minecraft.block.BlockColored;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class TanpopoColors
{

	private static final BlockColors COLORS_BLOCK = Tanpopo.proxy.getMinecraft().getBlockColors();
	private static final ItemColors COLORS_ITEM = Tanpopo.proxy.getMinecraft().getItemColors();

	public void init()
	{
		registerBlockFluffCushion();
	}

	private static void registerBlockFluffCushion()
	{
		COLORS_BLOCK.registerBlockColorHandler(new IBlockColor()
		{

			@Override
			public int colorMultiplier(IBlockState state, @Nullable IBlockAccess worldIn, @Nullable BlockPos pos, int tintIndex)
			{
				int colorValue = ((EnumDyeColor) state.getValue(BlockColored.COLOR)).getMapColor().colorValue;

				return colorValue;
			}

		}, TanpopoBlocks.FLUFF_CUSHION);

		COLORS_ITEM.registerItemColorHandler(new IItemColor()
		{

			@Override
			public int getColorFromItemstack(ItemStack stack, int tintIndex)
			{
				int colorValue = EnumDyeColor.byMetadata(stack.getItemDamage()).getMapColor().colorValue;

				return colorValue;
			}

		}, TanpopoBlocks.FLUFF_CUSHION);
	}

}
