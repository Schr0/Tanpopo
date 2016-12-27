package schr0.tanpopo.init;

import java.util.Set;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.client.IModGuiFactory;

public class TanpopoConfigGuiFactory implements IModGuiFactory
{

	@Override
	public void initialize(Minecraft minecraftInstance)
	{
		// none
	}

	@Override
	public Class<? extends GuiScreen> mainConfigGuiClass()
	{
		return TanpopoConfigGui.class;
	}

	@Override
	public Set<RuntimeOptionCategoryElement> runtimeGuiCategories()
	{
		return null;
	}

	@Override
	public RuntimeOptionGuiHandler getHandlerFor(RuntimeOptionCategoryElement element)
	{
		return null;
	}

}
