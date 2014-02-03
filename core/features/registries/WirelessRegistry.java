package appeng.core.features.registries;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import appeng.api.features.IWirelessTermHandler;
import appeng.api.features.IWirelessTermRegistery;
import appeng.core.localization.PlayerMessages;
import appeng.core.sync.GuiBridge;
import appeng.util.Platform;

public class WirelessRegistry implements IWirelessTermRegistery
{

	List<IWirelessTermHandler> handlers;

	public WirelessRegistry() {
		handlers = new ArrayList();
	}

	@Override
	public void registerWirelessHandler(IWirelessTermHandler handler)
	{
		if ( handler != null )
			handlers.add( handler );
	}

	@Override
	public boolean isWirelessTerminal(ItemStack is)
	{
		for (IWirelessTermHandler h : handlers)
		{
			if ( h.canHandle( is ) )
				return true;
		}
		return false;
	}

	@Override
	public IWirelessTermHandler getWirelessTerminalHandler(ItemStack is)
	{
		for (IWirelessTermHandler h : handlers)
		{
			if ( h.canHandle( is ) )
				return h;
		}
		return null;
	}

	@Override
	public void OpenWirelessTermainlGui(ItemStack item, World w, EntityPlayer player)
	{
		if ( Platform.isClient() )
			return;

		IWirelessTermHandler handler = getWirelessTerminalHandler( item );
		if ( handler == null )
		{
			player.addChatMessage( "Item is not a wireless terminal." );
			return;
		}

		if ( handler.hasPower( player, 0.5, item ) )
		{
			Platform.openGUI( player, null, null, GuiBridge.GUI_WIRELESS_TERM );
		}
		else
			player.sendChatToPlayer( PlayerMessages.DeviceNotPowered.get() );

	}

}
