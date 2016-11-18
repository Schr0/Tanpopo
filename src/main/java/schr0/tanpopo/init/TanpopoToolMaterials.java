package schr0.tanpopo.init;

import net.minecraft.item.Item;
import net.minecraftforge.common.util.EnumHelper;

public class TanpopoToolMaterials
{

	public static final Item.ToolMaterial TIER_IRON;

	public static final String NAME_IRON = "tanpopo_iron";

	static
	{
		TIER_IRON = EnumHelper.addToolMaterial(NAME_IRON, 2, 500, 9.0F, 3.0F, 22);
	}

}
