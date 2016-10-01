package schr0.tanpopo;

import net.minecraft.item.Item;
import net.minecraftforge.common.util.EnumHelper;

public class TanpopoToolMaterial
{
	public static final Item.ToolMaterial IRON;

	public static final String NAME_IRON = "iron";

	static
	{
		IRON = EnumHelper.addToolMaterial(NAME_IRON, 2, 375, 9.0F, 3.0F, 21);
	}

}
