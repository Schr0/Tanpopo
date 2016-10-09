package schr0.tanpopo;

import java.io.File;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.common.Loader;

public class TanpopoConfiguration
{

	public static int fellingBlockLimit;

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

			fellingBlockLimit = cfg.getInt("FELLING_BLOCK_LIMIT", Configuration.CATEGORY_GENERAL, 1000, 2, Integer.MAX_VALUE, "The maximum number that can be destroyed by FellingMode.");

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
