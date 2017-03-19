package com.XiaoHuiHui.minecraft.plugin.Attribute.Data;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import com.XiaoHuiHui.minecraft.plugin.Attribute.AMain;
import com.XiaoHuiHui.minecraft.plugin.Attribute.GUI.AGUI;
import com.google.common.base.Charsets;

/*插件主体、命令处理类、事件监听器之间的信息交互使用本类对象
 * 采用信息独立机制，即本类专门负责与config.yml的数据交互
 * 所有数据设置在这里后统一写入
 * 并统一在这里读取，已保证时效性
 */
public class AData {
	//开启单例模式！
	private static AData data=new AData();
	
	//有没有初始化的flag
	private static boolean flag=false;
	
	public static AData getInstance(){
		if(!flag)throw new IllegalArgumentException("No initialization!");
		return data;
	}
	
	//真·Constructor
	public static void init(AMain main) {
		data.setMain(main);
		data.setConfigFile(new File(main.getDataFolder(),"config.yml"));
		data.setDataFile(new File(main.getDataFolder(),"data/attr.yml"));
		flag=true;
	}
	
	//配置文件要求的版本
	public static final String configVersion="1.0";
	
	//属性信息
	private Map<String,Map<AAttr,Integer>> playerData;
	
	private AMain main;
	
	/* 配置文件路径，因为主类的saveConfig()不抛错，
	  * 所以只能写点多余的辣鸡代码让它抛错，这才是我的风格！
	  * 这个对象就是多余的，因为还需要重新定义
	  * 本来主类定义了的，但是用的private
	  * 我真是x了狗了(误)
	  */
	private File dataFile;
	private File configFile;
	private FileConfiguration config;
	private FileConfiguration dataConfig;
	
	//GUI和Listener的交互信息
	private List<Inventory> invs=new ArrayList<Inventory>();
	
	//GUI配置文件信息
	private String titleGUI;
	private String name;
	private String line;
	private String style;
	
	//Items配置文件信息
	private int count;
	private List<AItem> items;
	
	//Database配置文件信息
	private boolean databaseEnable;
	private int port;
	private String host;
	private String username;
	private String password;
	
	//getter and setter
	public List<Inventory> getInvs(){
		return invs;
	}
	
	public void addInvs(Inventory inv){
		getInvs().add(inv);
	}
	
	public boolean isInInvs(Inventory inv){
		return getInvs().contains(inv);
	}
	
	public boolean isDatabaseEnable() {
		return databaseEnable;
	}
	
	private void setDatabaseEnable(boolean databaseEnable) {
		this.databaseEnable = databaseEnable;
	}
	
	public String getHost() {
		return host;
	}
	
	private void setHost(String host) {
		if(host==null){
			outputError("数据库主机名不能为空！");
			setDatabaseEnable(false);
		}
		this.host = host;
	}
	
	public int getPort() {
		return port;
	}
	
	private void setPort(int port) {
		if(port<=0){
			outputError("数据库端口数据无效！");
			setDatabaseEnable(false);
			return;
		}
		this.port = port;
	}
	
	public String getUsername() {
		return username;
	}
	
	private void setUsername(String username) {
		if(username==null){
			outputError("数据库用户名不能为空！");
			setDatabaseEnable(false);
			return;
		}
		this.username = username;
	}
	
	public String getPassword() {
		return password;
	}
	
	private void setPassword(String password) {
		if(password==null){
			outputError("数据库密码不能为空！");
			setDatabaseEnable(false);
			return;
		}
		this.password = password;
	}
	
	public int getCount() {
		return count;
	}
	
	private void setCount(int count) {
		if(count<0){
			outputError("count配置项不能小于0！");
			return;
		}
		this.count = count;
	}
	
	public List<AItem> getItems() {
		return items;
	}
	
	private void setItems(List<AItem> items) {
		this.items = items;
	}
	
	public String getTitleGUI() {
		return titleGUI;
	}
	
	private void setTitleGUI(String titleGUI) {
		if(titleGUI==null){
			outputError("GUI配置信息有误！");
			return;
		}
		this.titleGUI = titleGUI.replace('&', '§');
	}
	
	public String getName(){
		return name;
	}
	
	private void setName(String name){
		if(name==null){
			outputError("GUI配置信息有误！");
			return;
		}
		this.name=name.replace('&' , '§');
	}
	
	public String getLine() {
		return line;
	}

	private void setLine(String line) {
		if(line==null){
			outputError("GUI配置信息有误！");
			return;
		}
		this.line = line;
	}

	public String getStyle() {
		return style;
	}

	private void setStyle(String style) {
		if(style==null){
			outputError("GUI配置信息有误！");
			return;
		}
		this.style = style.replace('&', '§');
	}

	private Map<String,Map<AAttr,Integer>> getPlayerData(){
		return playerData;
	}
	
	private void setPlayerData(Map<String, Map<AAttr, Integer>> playerData) {
		this.playerData = playerData;
	}
	
	private AMain getMain() {
		return main;
	}
	
	private void setMain(AMain main) {
		this.main = main;
	}
	
	private void setDataFile(File dataFile) {
		this.dataFile = dataFile;
	}
	
	private void setConfigFile(File configFile) {
		this.configFile = configFile;
	}
	
	private FileConfiguration getConfig() {
		if (config == null) {
			config=reloadConfig("config.yml",configFile);
		}
		return config;
	}
	
	private FileConfiguration getDataConfig() {
		if (dataConfig == null) {
			dataConfig=reloadConfig("data/attr.yml",dataFile);
		}
		return dataConfig;
	}
	
	//获取一个玩家的所有属性
	private Map<AAttr,Integer> getAttrsFromPlayer(String name){
		if(name==null)
			throw new IllegalArgumentException("name cannot be null!");
		return getPlayerData().get(name);
	}
	
	//获取一个玩家的指定属性
	public int getAttr(OfflinePlayer player,AAttr attr){
		if(player==null)
			throw new IllegalArgumentException("player cannot be null!");
		return getAttr(player.getName(),attr);
	}
	
	//获取一个玩家的指定属性
	public int getAttr(String name,AAttr attr){
		if(name==null)
			throw new IllegalArgumentException("name cannot be null!");
		Map<AAttr,Integer> temp=getAttrsFromPlayer(name);
		if(temp==null){
			return 0;
		}
		Integer it=temp.get(attr);
		if(it==null){
			return 0;
		}
		return it;
	}
	
	//设置一个玩家的指定属性
	public void setAttrsFromPlayer(String name,AAttr attr,int value){
		if(name==null)
			throw new IllegalArgumentException("name cannot be null!");
		Map<AAttr,Integer> map=getAttrsFromPlayer(name);
		if(map==null){
			map=new HashMap<AAttr,Integer>();
			getPlayerData().put(name,map);
		}
		map.put(attr, value);
	}
	
	//设置一个玩家的指定属性
	public void setAttrsFromPlayer(OfflinePlayer player,AAttr attr,int value){
		if(player==null)
			throw new IllegalArgumentException("player cannot be null!");
		setAttrsFromPlayer(player.getName(),attr,value);
	}
	
	
    private void reloadConfig() {
    	dataConfig=reloadConfig("data/attr.yml",dataFile);
    	config=reloadConfig("config.yml",configFile);
    }
    
    private FileConfiguration reloadConfig(String local,File file){
		final InputStream defConfigStream = getMain().getResource(local);
		if (defConfigStream == null) {
			return null;
        }
		FileConfiguration defaultConfig=
				YamlConfiguration.loadConfiguration(
						new InputStreamReader(defConfigStream, Charsets.UTF_8));
		FileConfiguration temp;
    	try {
    		temp = YamlConfiguration.loadConfiguration(
    				new InputStreamReader(
    						new FileInputStream(file), StandardCharsets.UTF_8));
		} catch (FileNotFoundException e) {
			temp=defaultConfig;
			saveDefaultConfig(file,local);
			outputWarning("没有找到文件"+local+"，已试图重新生成并载入默认!");
		}
    	temp.setDefaults(defaultConfig);
    	return temp;
    }
    
	private void saveDefaultConfig() {
		saveDefaultConfig(dataFile,"data/attr.yml");
		saveDefaultConfig(configFile,"config.yml");
	}
	
	private void saveDefaultConfig(File file,String local){
		if (!file.exists()) {
			outputInfo("没有找到文件"+local+"，重新生成默认文件!");
			getMain().saveResource(local, false);
		}
	}
	
	//读取config的数据，出问题就抛异常
	public void load(){
		saveDefaultConfig();
		reloadConfig();
		updateVersion();
		getDatabaseData();
		//选择读取Attr信息
		if(isDatabaseEnable()){
			outputInfo("使用数据库记录属性信息...");
			loadDatabaseAttr();
		}else{
			outputInfo("使用Yaml记录属性信息...");
			loadYamlAttr();
		}
		getItemsData();
		getGUIData();
	}
	
	//读取GUI数据
	private void getGUIData(){
		setTitleGUI(getConfig().getString("GUI.title"));
		setName(getConfig().getString("GUI.name"));
		setLine(getConfig().getString("GUI.line"));
		setStyle(getConfig().getString("GUI.style"));
		AGUI.load();
	}
	
	//读取数据库数据
	private void getDatabaseData() {
		setDatabaseEnable(getConfig().getBoolean("database.enable"));
		setPort(getConfig().getInt("database.port"));
		setHost(getConfig().getString("database.host"));
		setUsername(getConfig().getString("database.username"));
		setPassword(getConfig().getString("database.password"));
		if(isDatabaseEnable()){
			ADatabase.load();
		}
	}

	//读取物品数据
	private void getItemsData() {
		setCount(getConfig().getInt("count"));
		List<AItem> temp=new ArrayList<AItem>(getCount());
		for(int i=0;i<getCount();++i){
			temp.add(new AItem(
					getConfig().getString("list."+(i+1)+".name").replace('&', '§'),
					getConfig().getInt("list."+(i+1)+".id"),
					getConfig().getInt("list."+(i+1)+".chance"),
					getConfig().getInt("list."+(i+1)+".attr"),
					getConfig().getDouble("list."+(i+1)+".money")
				));
		}
		setItems(temp);
	}
	
	public int getTempData(Player p,AAttr attr){
		PlayerInventory inv=p.getInventory();
		Iterator<ItemStack> it=inv.iterator();
		while(it.hasNext()){
			ItemStack item=it.next();
			List<String> lores=item.getItemMeta().getLore();
			for(String s:lores){
				s.getClass();
				//TODO:Lore
			}
		}
		return 0;
	}
	
	//检查配置文件的版本
	public boolean verifyVersion(String version) {
		return version.equalsIgnoreCase(configVersion);
	}

	//自动更新配置文件
	private void updateVersion(){
		String version=getConfig().getString("version");
		if(version==null){
			outputError("配置文件的版本信息无法获取！");
			return;
		}
		if(!verifyVersion(version)){
			//TODO:自动更新版本的实现
			throw new IllegalArgumentException("config.yml版本信息有误！");
		}
	}
	
	//读取attr.yml
	private void loadYamlAttr() {
		List<String> ps=getDataConfig().getStringList("players");
		if(ps==null){
			outputError("玩家属性Yaml数据无法读取！");
			return;
		}
		Map<String,Map<AAttr,Integer>> maps=new HashMap<String,Map<AAttr,Integer>>();
		for(int i=0;i<ps.size();++i){
			String name=ps.get(i);
			AAttr[] attrs=AAttr.values();
			Map<AAttr,Integer> map=new HashMap<AAttr,Integer>();
			for(int j=0;j<attrs.length;++j){
				int t=getConfig().getInt(name+"."+attrs[j].name());
				map.put(attrs[j], t);
			}
			maps.put(name, map);
		}
		setPlayerData(maps);
	}
	
	//读取数据库
	private void loadDatabaseAttr(){
		//ADatabase.getData("");
		//TODO:
	}
	
	public void outputError(String msg){
		getMain().getLogger().log(Level.SEVERE, msg);
		getMain().setError(true);
	}
	
	public void outputWarning(String msg){
		getMain().getLogger().log(Level.WARNING, msg);
	}
	
	public void outputInfo(String msg){
		getMain().getLogger().info(msg);
	}
}
