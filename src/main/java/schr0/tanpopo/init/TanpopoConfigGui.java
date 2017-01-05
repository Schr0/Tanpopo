package schr0.tanpopo.init;

import java.util.List;

import com.google.common.collect.Lists;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.client.config.IConfigElement;
import schr0.tanpopo.Tanpopo;

public class TanpopoConfigGui extends GuiConfig
{

	public TanpopoConfigGui(GuiScreen parent)
	{
		super(parent, getListConfigElement(), Tanpopo.MOD_ID, false, false, new TextComponentTranslation(TanpopoConfig.LANG_CONFIG + "title").getFormattedText());
	}

	private static List<IConfigElement> getListConfigElement()
	{
		List<IConfigElement> list = Lists.newArrayList();

		for (String name : TanpopoConfig.config.getCategoryNames())
		{
			list.add(new ConfigElement(TanpopoConfig.config.getCategory(name)));
		}

		return list;
	}

}
