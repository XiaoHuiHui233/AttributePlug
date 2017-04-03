package com.XiaoHuiHui.minecraft.plugin.Attribute.Listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import com.XiaoHuiHui.minecraft.plugin.Attribute.Data.AData;

public class AListener implements Listener{
	private AData data;
	//AMain main;
	
	//Constructor
	public AListener(){
		super();
		setData(AData.getInstance());
	}
	
	public AData getData() {
		return data;
	}

	private void setData(AData data) {
		this.data = data;
	}
	
	@EventHandler(priority=EventPriority.NORMAL,ignoreCancelled=true)
	public void onJoin(PlayerJoinEvent e){
		Player p=e.getPlayer();
		if(!getData().hasPlayer(p)){
			getData().addPlayer(p);
		}
	}
}
