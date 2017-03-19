package com.XiaoHuiHui.minecraft.plugin.Attribute.Command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.XiaoHuiHui.minecraft.plugin.Attribute.Data.AData;
import com.XiaoHuiHui.minecraft.plugin.Attribute.GUI.AGUI;

public class ACmd implements CommandExecutor {
	
	private AData data;
	
	//Constructor
	public ACmd() {
		setData(AData.getInstance());
	}
	
	public AData getData() {
		return data;
	}

	private void setData(AData data) {
		this.data = data;
	}

	private void reload(CommandSender sender) {
		if(sender instanceof Player){
			if(!(sender.isOp()||sender.hasPermission("Attribute.reload"))){
				sender.sendMessage("§4You don't have premission to do this!");
				return;
			}
			sender.sendMessage("§4重要说明:本插件的载入信息只显示在控制台上!");
		}
		getData().load();
	}

	private void check(CommandSender sender) {
		if(!(sender instanceof Player)){
			sender.sendMessage("§4Only player can do this command!");
			return;
		}
		//TODO:如何去check呢？我还是先想想如何存储吧
	}
	                       
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		if(args.length!=1){
			return false;
		}else{
			if(args[0].equalsIgnoreCase("reload")){
				reload(sender);
			}else if(args[0].equalsIgnoreCase("check")){
				Player p=(Player)sender;
				AGUI.openUI(p);
				//check(sender);
			}
		}
		return false;
	}

}
