package schr0.tanpopo;

import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLConstructionEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import schr0.tanpopo.proxy.ProxyServer;

@Mod(modid = Tanpopo.MOD_ID, name = Tanpopo.MOD_NAME, version = Tanpopo.MOD_VERSION, dependencies = Tanpopo.MOD_DEPENDENCIES, acceptedMinecraftVersions = Tanpopo.MOD_ACCEPTED_MC_VERSIONS, useMetadata = true)
public class Tanpopo
{

	@Mod.Instance(Tanpopo.MOD_ID)
	public static Tanpopo instance;

	@SidedProxy(clientSide = "schr0.tanpopo.proxy.ProxyClient", serverSide = "schr0.tanpopo.proxy.ProxyServer")
	public static ProxyServer proxy;

	/**
	 * ModのID.
	 */
	public static final String MOD_ID = "schr0tanpopo";

	/**
	 * Modの名称.
	 */
	public static final String MOD_NAME = "Tanpopo";

	/**
	 * Modのバージョン.
	 */
	public static final String MOD_VERSION = "1.1.0";

	/**
	 * Forgeのバージョン.
	 */
	public static final String MOD_DEPENDENCIES = "required-after:Forge@[1.10.2-12.18.2.2099,)";

	/**
	 * Minecraftのバージョン.
	 */
	public static final String MOD_ACCEPTED_MC_VERSIONS = "[1.10.2]";

	/**
	 * ModのDOMAIN.
	 */
	public static final String MOD_DOMAIN = MOD_ID + ":";

	/**
	 * modの構築時イベント.
	 */
	@Mod.EventHandler
	public void constructingEvent(FMLConstructionEvent event)
	{
		FluidRegistry.enableUniversalBucket();
	}

	/**
	 * modの事前・初期設定時イベント.
	 */
	@Mod.EventHandler
	public void preInitEvent(FMLPreInitializationEvent event)
	{
		(new TanpopoConfiguration()).init();

		(new TanpopoFluids()).init();

		(new TanpopoItems()).init();

		(new TanpopoBlocks()).init();

		this.proxy.preInitEventClient(event);
	}

	/**
	 * modの事中・初期設定時イベント.
	 */
	@Mod.EventHandler
	public void initEvent(FMLInitializationEvent event)
	{
		(new TanpopoDispenserBehaviors()).init();

		(new TanpopoFuelHandler()).init();

		(new TanpopoRecipe()).init();

		(new TanpopoEvent()).init();

		this.proxy.initEventClient(event);
	}

	/**
	 * modの事後・初期設定時イベント.
	 */
	@Mod.EventHandler
	public void postInitEvent(FMLPostInitializationEvent event)
	{
		this.proxy.postInitEventClient(event);
	}

}
