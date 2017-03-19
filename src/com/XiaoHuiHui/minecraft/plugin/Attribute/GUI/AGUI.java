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
		return line;
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
	
//	private static int getBits(int i){
//		int count=0;
//		while(i!=0){
//			i/=10;
//			++count;
//		}
//		return count;
//	}
	
	public static void openUI(Player player){
		if(!isInit){
			throw new AssertionError();
		}
		Inventory inv=Bukkit.createInventory(null, InventoryType.PLAYER, getTitleGUI());
		ItemStack item=new ItemStack(Material.STAINED_GLASS_PANE,1,(short)5);
		List<String> lores=new ArrayList<String>();
		lores.add(getLine());
		lores.add("                          ");
		AAttr[] attrs=AAttr.values();
		for(AAttr attr:attrs){
			String s;
			int value=0;//data.getAttr(player, attr);
			s=getStyle().replaceAll("%ATTRNAME%",attr.getName())
					.replaceAll("%ATTRVALUE%", ""+value);
//			int i=24-(s1.length()*2+getBits(value)+2);
//			if((i & 1)==0){
//				i/=2;
//				for(int j=0;j<i-1;++j){
//					s+=" ";
//				}
//				s+="§a"+s1;
//				s+="§3: §2";
//				s+=value;
//				for(int j=0;j<i-1;++j){
//					s+=" ";
//				}
//			}else{
//				i/=2;
//				for(int j=0;j<i;++j){
//					s+=" ";
//				}
//				s+="§a"+s1;
//				s+="§3: §2";
//				s+=value;
//				++i;
//				for(int j=0;j<i;++j){
//					s+=" ";
//				}
//			}			
//			s+="§3*";
			lores.add(s);
		} 
		lores.add("                          ");
		lores.add(getLine());
		ItemMeta itemm=item.getItemMeta();
		itemm.setLore(lores);
		itemm.setDisplayName(getName());
		item.setItemMeta(itemm);
		inv.setItem(0, item);
		getData().addInvs(inv);
		player.openInventory(inv);
	}
}
