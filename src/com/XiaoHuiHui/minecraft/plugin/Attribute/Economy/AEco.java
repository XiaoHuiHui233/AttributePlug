package com.XiaoHuiHui.minecraft.plugin.Attribute.Economy;

import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;

import com.XiaoHuiHui.minecraft.plugin.Attribute.AMain;
import com.XiaoHuiHui.minecraft.plugin.Attribute.Data.AData;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;

public class AEco {
	
	private static AData data;
	private static AMain main;
	private static boolean isInit=false;
	
	private static Economy eco;
	private static boolean isEco=false;
	
	private AEco() {
		 throw new IllegalArgumentException("But nothing happened!");
	}
	
	public static AMain getMain(){
		return main;
	}
	
	private static void setMain(AMain main){
		AEco.main=main;
	}
	
	public static AData getData(){
		return data;
	}
	
	private static void setData(AData data) {
		AEco.data = data;
	}

	public static Economy getEco() {
		return eco;
	}

	private static void setEco(Economy eco) {
		AEco.eco = eco;
	}

	public static boolean isEco() {
		return isEco;
	}

	private static void setEco(boolean isEco) {
		AEco.isEco = isEco;
	}

	public static void init(AMain main){
		setMain(main);
		setData(AData.getInstance());
		if(Bukkit.getPluginManager().getPlugin("Vault") == null){
			getData().outputError("未找到所需的Vault经济支持插件！");
			return;
		}
		setEco(setEconomy());
		if(!isEco()){
			getData().outputError("无法获取有效的经济插件！");
		}else{
			isInit=true;
			getData().outputInfo("经济插件支持已成功载入！");
		}
	}
	
	private static boolean setEconomy() {
		RegisteredServiceProvider<Economy> econ = getMain().getServer()
				.getServicesManager()
				.getRegistration(Economy.class);
		if (econ != null) {
			setEco(econ.getProvider());
		}
		return (getEco() != null);
    }
	
	public static double getEcoFromPlayer(String name){
		if(!isInit){
			throw new AssertionError();
		}
		return getEco().getBalance(name);
	}
	
	public static boolean giveEcoFromPlayer(String name,double money){
		if(!isInit){
			throw new AssertionError();
		}
		EconomyResponse er=getEco().depositPlayer(name, money);
		return er.transactionSuccess();
	}
	
	public static boolean removeEcoFromPlayer(String name,double money){
		if(!isInit){
			throw new AssertionError();
		}
		EconomyResponse er=getEco().withdrawPlayer(name, money);
		return er.transactionSuccess();
	}
}
