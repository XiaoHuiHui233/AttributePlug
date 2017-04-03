package com.XiaoHuiHui.minecraft.plugin.Attribute.Listener;

import java.util.List;
import java.util.Random;

import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.XiaoHuiHui.minecraft.plugin.Attribute.Data.AAttr;
import com.XiaoHuiHui.minecraft.plugin.Attribute.Data.AData;
import com.XiaoHuiHui.minecraft.plugin.Attribute.Data.AItem;
import com.XiaoHuiHui.minecraft.plugin.Attribute.Economy.AEco;

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
		HumanEntity human=event.getWhoClicked();
		if(!(human instanceof Player)){
			return;
		}
		Player p=(Player)human;
		if(!(getData().getInvs().contains(inv))){
			return;
		}
		ItemStack item=event.getCurrentItem();
		if(item==null || item.getData().getItemType()==Material.AIR){
			ItemStack item1=event.getCursor();
			if(checkItem(item1)){
				if(event.getSlot()!=10){
					p.sendMessage("§4你不能放置到这个格子！");
					event.setCancelled(true);
					return;
				}
			}else{
				p.sendMessage("§4你不能放置这个物品！");
				event.setCancelled(true);
			}
		}else{
			if(event.getSlot()==8){
				update(inv,p);
				event.setCancelled(true);
				p.closeInventory();
			}else if(event.getSlot()==17){
				if(inv.getItem(10)!=null)
					p.getInventory().addItem(inv.getItem(10));
				event.setCancelled(true);
				p.closeInventory();
			}else if(!checkItem(item)){
				p.sendMessage("§4你不能拿起这个物品！");
				event.setCancelled(true);
			}
		}
		
	}

	@SuppressWarnings("deprecation")
	private void update(Inventory inv,Player p) {
		ItemStack item1=inv.getItem(10);
		if(item1==null || item1.getType().equals(Material.AIR)){
			p.sendMessage("§4你没有放入需要的物品！");
			return;
		}
		int amount=item1.getAmount();
		ItemMeta itemm=item1.getItemMeta();
		AItem aitem=new AItem(itemm.getDisplayName(),
				itemm.getLore(),
				item1.getTypeId(),
				item1.getData().getData()
				);
		List<AItem> list=getData().getItems();
		for(int i=0;i<list.size();++i){
			if(list.get(i).equals(aitem)){
				AItem ta=list.get(i);
				double money=ta.getMoney();
				boolean flag=AEco.removeEcoFromPlayer(p.getName(), money);
				if(!flag){
					p.sendMessage("§4你没有足够的金钱！");
					p.getInventory().addItem(inv.getItem(10));
					return;
				}
				Random rand=new Random(System.currentTimeMillis());
				int c=rand.nextInt(100);
				if(c>ta.getAttr()){
					p.sendMessage("§4升级失败！");
					return;
				}
				int r=rand.nextInt(12);
				AAttr[] attrs=AAttr.values();
				getData().setAttrFromPlayer(p, attrs[r], ta.getStrong()*amount);
				p.sendMessage("§3升级成功！您的 §2"+getData().getName(attrs[r])
							+" §3属性已升级到 §1"+getData().getAttr(p, attrs[r])+" §3%");
			}
		}
	}

	@SuppressWarnings("deprecation")
	private boolean checkItem(ItemStack item) {
		if(item==null || item.getData().getItemType()==Material.AIR){
			return false;
		}
		ItemMeta itemm=item.getItemMeta();
		if(itemm==null){
			return false;
		}
		AItem aitem=new AItem(itemm.getDisplayName(),
							itemm.getLore(),
							item.getTypeId(),
							item.getData().getData()
							);
		if(!(getData().getItems().contains(aitem))){
			return false;
		}
		return true;
	}
	
	@EventHandler
	public void onInventoryClose(InventoryCloseEvent event){
		Inventory inv=event.getInventory();
		if(inv==null){
			return;
		}
		if(!(getData().getInvs().contains(inv))){
			return;
		}
		getData().getInvs().remove(inv);
	}
}
