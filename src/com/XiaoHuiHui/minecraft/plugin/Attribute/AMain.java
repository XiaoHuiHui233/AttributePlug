package com.XiaoHuiHui.minecraft.plugin.Attribute;

import java.util.logging.Level;

import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

import com.XiaoHuiHui.minecraft.plugin.Attribute.Command.ACmd;
import com.XiaoHuiHui.minecraft.plugin.Attribute.Data.AData;
import com.XiaoHuiHui.minecraft.plugin.Attribute.Data.ADatabase;
import com.XiaoHuiHui.minecraft.plugin.Attribute.Economy.AEco;
import com.XiaoHuiHui.minecraft.plugin.Attribute.GUI.AGUI;
import com.XiaoHuiHui.minecraft.plugin.Attribute.Listener.AGUIListener;
import com.XiaoHuiHui.minecraft.plugin.Attribute.Listener.AListener;

//Attribute属性插件主类
public class AMain extends JavaPlugin{
	//事件监听器
	AListener listener;
	//传递给监听器的数据
	AData data;
	//命令处理类
	ACmd cmd;
	//版本号
	AGUIListener guiListener;
	
	private boolean isError=false;
	
	public static final String version="v00.00.33 beta";

	//getter and setter
	public AGUIListener getGuiListener() {
		return guiListener;
	}

	private void setGuiListener(AGUIListener guiListener) {
		this.guiListener = guiListener;
	}
	
	public AListener getListener() {
		return listener;
	}

	private void setListener(AListener listener) {
		this.listener = listener;
	}

	public AData getData() {
		return data;
	}

	private void setData(AData data) {
		this.data = data;
	}

	public ACmd getCmd() {
		return cmd;
	}

	private void setCmd(ACmd cmd) {
		this.cmd = cmd;
	}
	
	public boolean isError(){
		return isError;
	}
	
	public void setError(boolean isError){
		this.isError=isError;
	}
	
	@Override
	public void onEnable(){
		AData.init(this);
		ADatabase.init();
		AEco.init(this);
		AGUI.init();
		setData(AData.getInstance());
		setListener(new AListener());
		setCmd(new ACmd());
		setGuiListener(new AGUIListener());
		if (!getDataFolder().exists()) {
			getDataFolder().mkdir();
		}
		getServer().getPluginManager().registerEvents(getListener(), this);
		getServer().getPluginManager().registerEvents(getGuiListener(), this);
		getServer().getPluginCommand("attribute").setExecutor(getCmd());
		getData().load();
		if(isError()){
			getLogger().log(Level.SEVERE,"插件启动异常！已自动关闭！");
			getServer().getPluginManager().disablePlugin(this);
		}else{
			getLogger().info("插件启动完毕！版本:" +version + " 制作:小灰灰");
		}
	}
	
	@Override
	public void onLoad(){
		getLogger().info("插件正在启动中... 版本:" +version + " 制作:小灰灰");
	}
	
	@Override
	public void onDisable(){
		HandlerList.unregisterAll(this);
		ADatabase.shutdown();
		getLogger().info("插件成功关闭！版本:" +version + " 制作:小灰灰");
	}
}
