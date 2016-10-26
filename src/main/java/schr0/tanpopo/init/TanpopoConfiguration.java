package schr0.tanpopo.init;

import java.io.File;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.common.Loader;
import schr0.tanpopo.Tanpopo;

public class TanpopoConfiguration
{

	private static final String CATEGORY_TOOL = "tool";

	public static int fellingModeBlockLimit;
	public static int mowingModeBlockLimit;

	public void init()
	{
		build();
	}

	private static void build()
	{
		Configuration cfg = new Configuration(new File(Loader.instance().getConfigDir(), "schr0/" + Tanpopo.MOD_ID + ".cfg"));

		try
		{
			cfg.load();

			fellingModeBlockLimit = cfg.getInt("FELLING MODE BLOCK LIMIT", CATEGORY_TOOL, 1000, 2, Integer.MAX_VALUE, "The maximum number that can be destroyed by Felling Mode.");
			mowingModeBlockLimit = cfg.getInt("MOWING MODE BLOCK LIMIT", CATEGORY_TOOL, 1000, 2, Integer.MAX_VALUE, "The maximum number that can be destroyed by Mowing Mode.");

			// none
		}
		catch (Exception e)
		{
			FMLLog.severe("This Configuration Error was caused by " + Tanpopo.MOD_ID);
		}
		finally
		{
			cfg.save();
		}
	}

}
