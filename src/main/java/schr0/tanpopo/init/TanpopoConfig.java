package schr0.tanpopo.init;

import java.io.File;
import java.util.List;

import com.google.common.collect.Lists;

import net.minecraft.client.resources.I18n;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.common.Loader;
import schr0.tanpopo.Tanpopo;

public class TanpopoConfig
{

	public static Configuration config;

	public static String CATEGORY_TOOL = "tool";

	public static String PROP_FELLING_MODE_BLOCK_LIMIT = "felling_mode_block_limit";
	public static String PROP_MOWING_MODE_BLOCK_LIMIT = "mowing_mode_block_limit";

	public static int fellingModeBlockLimit;
	public static int mowingModeBlockLimit;

	public static void syncConfig()
	{
		init();

		Property prop;
		List<String> propOrder = Lists.newArrayList();
		String langConfig = "config.";
		String langComment = ".comment";

		prop = config.get(CATEGORY_TOOL, PROP_FELLING_MODE_BLOCK_LIMIT, 1000).setMinValue(2).setMaxValue(Integer.MAX_VALUE);
		prop.setLanguageKey(langConfig + CATEGORY_TOOL + "." + prop.getName());
		prop.setComment(I18n.format(prop.getLanguageKey() + langComment));
		propOrder.add(prop.getName());
		fellingModeBlockLimit = prop.getInt(fellingModeBlockLimit);

		prop = config.get(CATEGORY_TOOL, PROP_MOWING_MODE_BLOCK_LIMIT, 1000).setMinValue(2).setMaxValue(Integer.MAX_VALUE);
		prop.setLanguageKey(langConfig + CATEGORY_TOOL + "." + prop.getName());
		prop.setComment(I18n.format(prop.getLanguageKey() + langComment));
		propOrder.add(prop.getName());
		mowingModeBlockLimit = prop.getInt(mowingModeBlockLimit);

		config.setCategoryLanguageKey(CATEGORY_TOOL, langConfig + CATEGORY_TOOL);
		config.setCategoryPropertyOrder(CATEGORY_TOOL, propOrder);

		if (config.hasChanged())
		{
			config.save();
		}
	}

	private static void init()
	{
		if (config != null)
		{
			return;
		}

		config = new Configuration(new File(Loader.instance().getConfigDir(), "schr0/" + Tanpopo.MOD_ID + ".cfg"));

		try
		{
			config.load();
		}
		catch (Exception e)
		{
			FMLLog.severe("This Configuration Error was caused by " + Tanpopo.MOD_ID);
		}
	}

}
