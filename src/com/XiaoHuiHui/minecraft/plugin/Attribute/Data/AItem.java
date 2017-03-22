package com.XiaoHuiHui.minecraft.plugin.Attribute.Data;

import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class AItem {
	private String name;
	private List<String> lore;
	private int id,chance,attr,strong;
	private short data;
	private double money;

	//Constructor
	public AItem(String name,List<String> lore,String id,int chance,int attr,int strong,double money){
		this.data=0;
		if(id.contains(":")){
			String[] str=id.split(":");
			this.id=Integer.parseInt(str[0]);
			this.data=Short.parseShort(str[1]);
		}else{
			this.id=Integer.parseInt(id);
		}
		if(name!=null)
			this.name=name.replace('&', '§');
		if(lore!=null){
			for(int i=0;i<lore.size();++i){
				lore.set(i, lore.get(i).replace('&', '§'));
			}
			this.lore=lore;
		}
		this.chance=chance;
		this.attr=attr;
		this.strong=strong;
		this.money=money;
	}
	
	public AItem(String name,List<String> lore,int id,short data,int chance,int attr,int strong,double money){
		this(name,lore,id,data);
		this.chance=chance;
		this.attr=attr;
		this.strong=strong;
		this.money=money;
	}
	
	public AItem(String name,List<String> lore,int id,short data){
		if(name!=null)
			this.name=name.replace('&', '§');
		if(lore!=null){
			for(int i=0;i<lore.size();++i){
				lore.set(i, lore.get(i).replace('&', '§'));
			}
			this.lore=lore;
		}
		this.id=id;
		this.data=data;
	}
	
	//getter and setter
	public String getName() {
		return name;
	}
	
	public List<String> getLore(){
		return lore;
	}
	
	public int getId() {
		return id;
	}
	
	public int getChance() {
		return chance;
	}
	
	public short getData(){
		return data;
	}
	
	public int getAttr() {
		return attr;
	}
	
	public int getStrong(){
		return strong;
	}
	
	public double getMoney() {
		return money;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(name)
									.append(id)
									.append(data)
									.append(lore)
									.toHashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if(obj==null)
			return false;
		if(obj.getClass()!=AItem.class)
			return false;
		AItem ano=(AItem)obj;
		return new EqualsBuilder().append(name, ano.name)
									.append(id,ano.id)
									.append(data,ano.data)
									.append(lore, ano.lore)
									.isEquals();
	}
	
	@SuppressWarnings("deprecation")
	public ItemStack toItem(){
		ItemStack item=new ItemStack(Material.getMaterial(getId()),1,getData());
		ItemMeta itemm=item.getItemMeta();
		itemm.setDisplayName(name);
		itemm.setLore(lore);
		item.setItemMeta(itemm);
		return item;
	}
}
