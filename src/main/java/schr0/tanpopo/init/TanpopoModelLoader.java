package schr0.tanpopo.init;

import java.util.List;

import com.google.common.collect.Lists;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import schr0.tanpopo.Tanpopo;

@SideOnly(Side.CLIENT)
public class TanpopoModelLoader
{

	public static void registerModel(Item item, int meta)
	{
		if (meta == 0)
		{
			ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(item.getRegistryName(), "inventory"));
			ModelBakery.registerItemVariants(item, item.getRegistryName());
		}
		else
		{
			List<ResourceLocation> models = Lists.newArrayList();

			for (int i = 0; i <= meta; i++)
			{
				ModelLoader.setCustomModelResourceLocation(item, i, new ModelResourceLocation(item.getRegistryName() + "_" + i, "inventory"));

				models.add(new ResourceLocation(item.getRegistryName() + "_" + i));
			}

			ModelBakery.registerItemVariants(item, models.toArray(new ResourceLocation[models.size()]));
		}
	}

	public static void registerModelFluid(Block fluid, String name)
	{
		ModelResourceLocation fluidModel = new ModelResourceLocation(Tanpopo.MOD_DOMAIN + name, "fluid");

		ModelLoader.setCustomMeshDefinition(Item.getItemFromBlock(fluid), new TanpopoModelLoader.FluidItemMeshDefinition(fluidModel));
		ModelLoader.setCustomStateMapper(fluid, new TanpopoModelLoader.FluidStateMapper(fluidModel));
	}

	private static class FluidItemMeshDefinition implements ItemMeshDefinition
	{
		private ModelResourceLocation model;

		public FluidItemMeshDefinition(ModelResourceLocation fluidModel)
		{
			this.model = fluidModel;
		}

		@Override
		public ModelResourceLocation getModelLocation(ItemStack stack)
		{
			return this.model;
		}
	}

	private static class FluidStateMapper extends StateMapperBase
	{
		private ModelResourceLocation model;

		public FluidStateMapper(ModelResourceLocation fluidModel)
		{
			this.model = fluidModel;
		}

		@Override
		protected ModelResourceLocation getModelResourceLocation(IBlockState state)
		{
			return this.model;
		}
	}

}
