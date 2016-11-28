package schr0.tanpopo.init;

import net.minecraft.item.Item;
import net.minecraftforge.common.util.EnumHelper;
import schr0.tanpopo.Tanpopo;

public class TanpopoToolMaterials
{

	public static final Item.ToolMaterial IRON;

	public static final String NAME_IRON = Tanpopo.MOD_ID + "." + "iron";

	static
	{
		IRON = EnumHelper.addToolMaterial(NAME_IRON, 2, 500, 9.0F, 3.0F, 22);
	}

}
