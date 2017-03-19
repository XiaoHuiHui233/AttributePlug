package com.XiaoHuiHui.minecraft.plugin.Attribute.Listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.XiaoHuiHui.minecraft.plugin.Attribute.Data.AData;

public class AGUIListener implements Listener{
	
	private AData data;
	
	public AGUIListener(){
		super();
		setData(AData.getInstance());
	}
	
	public AData getData(){
		return data;
	}
	
	private void setData(AData data){
		this.data=data;
	}
	
	@EventHandler
	public void onInventoryClick(InventoryClickEvent event){
		Inventory inv=event.getInventory();
		if(inv==null){
			return;
		}
		if(!(getData().getInvs().contains(inv))){
			return;
		}
		ItemStack item=event.getCurrentItem();
		if(item==null){
			event.setCancelled(true);
			return;
		}
		
		
	}
}
