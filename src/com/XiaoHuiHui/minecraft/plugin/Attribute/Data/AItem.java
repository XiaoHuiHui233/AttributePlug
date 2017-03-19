package com.XiaoHuiHui.minecraft.plugin.Attribute.Data;

public class AItem {
	private String name;
	private int id,chance,attr;
	private double money;

	//Constructor
	public AItem(String name,int id,int chance,int attr,double money){
		this.name=name;
		this.id=id;
		this.chance=chance;
		this.attr=attr;
		this.money=money;
	}
	
	//getter and setter
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public int getChance() {
		return chance;
	}
	
	public void setChance(int chance) {
		this.chance = chance;
	}
	
	public int getAttr() {
		return attr;
	}
	
	public void setAttr(int attr) {
		this.attr = attr;
	}
	
	public double getMoney() {
		return money;
	}
	
	public void setMoney(double money) {
		this.money = money;
	}
	
}
