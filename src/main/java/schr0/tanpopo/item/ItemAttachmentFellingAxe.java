package schr0.tanpopo.item;

import net.minecraft.item.Item;
import schr0.tanpopo.init.TanpopoItems;

public class ItemAttachmentFellingAxe extends ItemModeToolAttachment
{

	public ItemAttachmentFellingAxe()
	{
		super();
	}

	@Override
	public Item getDefaultModeTool()
	{
		return TanpopoItems.TOOL_FELLING_AXE;
	}

}
