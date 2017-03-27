package com.XiaoHuiHui.minecraft.plugin.Attribute.GUI;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.XiaoHuiHui.minecraft.plugin.Attribute.Data.AAttr;
import com.XiaoHuiHui.minecraft.plugin.Attribute.Data.AData;

public class AGUI {
	private static AData data;
	private static boolean isInit=false;
	
	private static String titleGUI;
	private static String line;
	private static String style;
	private static String name;
	
	private AGUI(){
		throw new IllegalArgumentException("But nothing happened again!");
	}
	
	public static AData getData() {
		return data;
	}

	private static void setData(AData data) {
		AGUI.data = data;
	}

	public static void init(){
		setData(AData.getInstance());
		isInit=true;
	}
	
	public static String getTitleGUI() {
		return titleGUI;
	}

	private static void setTitleGUI(String titleGUI) {
		AGUI.titleGUI = titleGUI;
	}

	public static String getLine() {
		return line.replace('&', '§');
	}

	private static void setLine(String line) {
		AGUI.line = line;
	}

	public static String getStyle() {
		return style;
	}

	private static void setStyle(String style) {
		AGUI.style = style;
	}
	
	public static String getName(){
		return name;
	}
	
	private static void setName(String name){
		AGUI.name=name;
	}

	public static void load(){
		setTitleGUI(getData().getTitleGUI());
		setName(getData().getName());
		setLine(getData().getLine());
		setStyle(getData().getStyle());
	}
	
	public static void openUI(Player player){
		if(!isInit){
			throw new AssertionError();
		}
		Inventory inv=Bukkit.createInventory(null, InventoryType.CHEST, getTitleGUI());
		List<String> lores=new ArrayList<String>();
		lores.add(getLine());
		lores.add("                          ");
		AAttr[] attrs=AAttr.values();
		for(AAttr attr:attrs){
			String s;
			int value=data.getAttr(player, attr);
			s=getStyle().replaceAll("%ATTRNAME%",getData().getName(attr))
					.replaceAll("%ATTRVALUE%", ""+value);
			lores.add(s);
		} 
		lores.add("                          ");
		lores.add(getLine());
		putItem(Material.STAINED_GLASS_PANE,(short)5,getName(),lores,inv,0);
		putItem(Material.STAINED_GLASS_PANE,(short)14,
				"§2请将属性强化宝石放到中间!",new ArrayList<String>(),inv,1);
		putItem(Material.STAINED_GLASS_PANE,(short)5,
				"§2请将属性强化宝石放到中间!",new ArrayList<String>(),inv,2);
		putItem(Material.STAINED_GLASS_PANE,(short)4,"§6确定",new ArrayList<String>(),inv,8);
		putItem(Material.STAINED_GLASS_PANE,(short)5,
				"§2请将属性强化宝石放到中间!",new ArrayList<String>(),inv,9);
		putItem(Material.STAINED_GLASS_PANE,(short)5,
				"§2请将属性强化宝石放到中间!",new ArrayList<String>(),inv,11);
		putItem(Material.STAINED_GLASS_PANE,(short)3,"§6取消",new ArrayList<String>(),inv,17);
		putItem(Material.STAINED_GLASS_PANE,(short)5,
				"§2请将属性强化宝石放到中间!",new ArrayList<String>(),inv,18);
		putItem(Material.STAINED_GLASS_PANE,(short)14,
				"§2请将属性强化宝石放到中间!",new ArrayList<String>(),inv,19);
		putItem(Material.STAINED_GLASS_PANE,(short)5,
				"§2请将属性强化宝石放到中间!",new ArrayList<String>(),inv,20);
		getData().addInvs(inv);
		player.openInventory(inv);
	}
	
	private static void putItem(Material m,short d,String n,List<String> lore,Inventory inv,int l){
		ItemStack item=new ItemStack(m,1,d);
		ItemMeta itemm=item.getItemMeta();
		itemm.setLore(lore);
		itemm.setDisplayName(n);
		item.setItemMeta(itemm);
		inv.setItem(l, item);
	}
}
