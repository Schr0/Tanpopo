package schr0.tanpopo.init;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockColored;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import schr0.tanpopo.Tanpopo;

@SideOnly(Side.CLIENT)
public class TanpopoBehaviorsColors
{

	private static final BlockColors COLORS_BLOCK = Tanpopo.proxy.getMinecraft().getBlockColors();
	private static final ItemColors COLORS_ITEM = Tanpopo.proxy.getMinecraft().getItemColors();

	public void initClient()
	{
		registerBlockArrayColor(TanpopoBlocks.FLUFF_CUSHION);
	}

	// TODO /* ======================================== MOD START =====================================*/

	private static void registerBlockArrayColor(Block block)
	{
		COLORS_BLOCK.registerBlockColorHandler(new IBlockColor()
		{

			@Override
			public int colorMultiplier(IBlockState state, @Nullable IBlockAccess worldIn, @Nullable BlockPos pos, int tintIndex)
			{
				return ((EnumDyeColor) state.getValue(BlockColored.COLOR)).getMapColor().colorValue;
			}

		}, block);

		COLORS_ITEM.registerItemColorHandler(new IItemColor()
		{

			@Override
			public int getColorFromItemstack(ItemStack stack, int tintIndex)
			{
				return EnumDyeColor.byMetadata(stack.getItemDamage()).getMapColor().colorValue;
			}

		}, Item.getItemFromBlock(block));
	}

}
