package schr0.tanpopo;

import net.minecraft.item.Item;
import net.minecraftforge.common.util.EnumHelper;

public class TanpopoToolMaterials
{
	public static final Item.ToolMaterial TIER_0;

	public static final String NAME_TIER_0 = "tier_0";

	static
	{
		TIER_0 = EnumHelper.addToolMaterial(NAME_TIER_0, 2, 500, 9.0F, 3.0F, 22);
	}

}
