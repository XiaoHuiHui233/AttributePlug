package com.XiaoHuiHui.minecraft.plugin.Attribute.Data;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

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
	
	//ui配置
	private boolean warn;
	private String cmsg;
	private String bdmsg;
	private String hmmsg;
	private String mmsg;
	
	//test配置
	private boolean useLevelChangeHealth;
	private boolean debug;//TODO:Debug
	
	//attrs
	private Map<String,AAttr> unNames;
	private Map<AAttr,String> names;
	private Map<AAttr,Integer> maxLevels;
	private Map<AAttr,Integer> defaults;
	
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

	public boolean isWarn() {
		return warn;
	}

	public void setWarn(boolean warn) {
		this.warn = warn;
	}
	
	public String getCmsg() {
		return cmsg;
	}

	private void setCmsg(String cmsg) {
		this.cmsg = cmsg.replace('&', '§');
	}

	public String getBdmsg() {
		return bdmsg;
	}

	private void setBdmsg(String bdmsg) {
		this.bdmsg = bdmsg.replace('&', '§');
	}
	
	public String getHmmsg() {
		return hmmsg;
	}

	private void setHmmsg(String hmmsg) {
		this.hmmsg = hmmsg.replace('&', '§');
	}

	public String getMmsg() {
		return mmsg;
	}

	private void setMmsg(String mmsg) {
		this.mmsg = mmsg.replace('&', '§');
	}

	public boolean isUseLevelChangeHealth() {
		return useLevelChangeHealth;
	}

	private void setUseLevelChangeHealth(boolean useLevelChangeHealth) {
		this.useLevelChangeHealth = useLevelChangeHealth;
	}

	public boolean isDebug() {
		return debug;
	}

	private void setDebug(boolean debug) {
		this.debug = debug;
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
	
	private Map<AAttr,String> getNames(){
		return names;
	}
	
	private void setNames(Map<AAttr,String> names){
		this.names=names;
	}
	
	private Map<AAttr,Integer> getMaxLevels(){
		return maxLevels;
	}
	
	private void setMaxLevels(Map<AAttr,Integer> maxLevels){
		this.maxLevels=maxLevels;
	}
	
	private Map<String,AAttr> getUnNames(){
		return unNames;
	}
	
	private void setUnNames(Map<String,AAttr> unNames){
		this.unNames=unNames;
	}
	
	private Map<AAttr, Integer> getDefaults() {
		return defaults;
	}

	private void setDefaults(Map<AAttr, Integer> defaults) {
		this.defaults = defaults;
	}

	//获取一个名称对应的属性
	public AAttr getAttr(String name){
		return getUnNames().get(name);
	}
	
	//获取一个属性的名称
	public String getName(AAttr attr){
		return getNames().get(attr);
	}
	
	//获取一个属性的最大等级
	public int getMaxLevel(AAttr attr){
		return getMaxLevels().get(attr);
	}
	
	//获取一个属性的初始默认值
	public int getDefault(AAttr attr){
		return getDefaults().get(attr);
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
		it+=getTempData(name,attr);
		return it;
	}
	
	//获取一个玩家的指定属性在物品上的叠加
	@SuppressWarnings("deprecation")
	private int getTempData(String name,AAttr attr){
		return getTempData(getMain().getServer().getPlayer(name),attr);
	}
	
	//获取一个玩家的指定属性在物品上的叠加
	private int getTempData(Player p,AAttr attr){
		if(p==null){
			return 0;
		}
		PlayerInventory inv=p.getInventory();
		List<ItemStack> list=new ArrayList<ItemStack>();
		list.addAll(Arrays.asList(inv.getArmorContents()));
		list.add(inv.getItemInHand());
		list.add(inv.getItem(6));
		list.add(inv.getItem(7));
		list.add(inv.getItem(8));
		int cnt=0;
		for(int i=0;i<list.size();++i){
			ItemStack item=list.get(i);
			if(item==null || item.getType().equals(Material.AIR)){
				continue;
			}
			ItemMeta itemm=item.getItemMeta();
			if(itemm==null){
				continue;
			}
			List<String> lores=itemm.getLore();
			if(lores==null||lores.isEmpty()){
				continue;
			}
			for(String s:lores){
				if(!s.contains(":")){
					continue;
				}
				String str[]=s.split(":");
				if(str.length!=2){
					continue;
				}
				AAttr attr1=getAttr(str[0]);
				if(attr1==null || !attr.equals(attr1)){
					continue;
				}
				int temp;
				try{
					temp=Integer.parseInt(str[1]);
				}catch(NumberFormatException e){
					continue;
				}
				cnt+=temp;
			}
		}
		return cnt;
	}
	
	
	//设置一个玩家的指定属性
	public void setAttrFromPlayer(String name,AAttr attr,int value){
		if(name==null)
			throw new IllegalArgumentException("name cannot be null!");
		Map<AAttr,Integer> map=getAttrsFromPlayer(name);
		map.put(attr, map.get(attr)+value);
		if(isDatabaseEnable()){
			ADatabase.updateData(name, attr, map.get(attr));
		}else{
			getDataConfig().set("name."+name+"."+attr.name(), map.get(attr));
			try {
				getDataConfig().save(dataFile);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		getPlayerData().put(name,map);
	}
	
	//设置一个玩家的指定属性
	public void setAttrFromPlayer(OfflinePlayer player,AAttr attr,int value){
		if(player==null)
			throw new IllegalArgumentException("player cannot be null!");
		setAttrFromPlayer(player.getName(),attr,value);
	}
	
	//添加一个玩家
	public void addPlayer(Player p){
		if(isDatabaseEnable()){
			ADatabase.insertData(p.getName());
		}else{
			List<String> list=getDataConfig().getStringList("players");
			list.add(p.getName());
			getDataConfig().set("players",list);
		}
		Map<AAttr,Integer> map=new HashMap<AAttr,Integer>();
		AAttr attrs[]=AAttr.values();
		for(AAttr ta : attrs){
			map.put(ta,getDefault(ta));
			if(isDatabaseEnable()){
				ADatabase.updateData(p.getName(), ta,getDefault(ta));
			}else{
				getDataConfig().set("name."+p.getName()+"."+ta.name(),getDefault(ta));
			}
		}
		if(!isDatabaseEnable()){
			try {
				getDataConfig().save(dataFile);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	//判断记录中是否有某个玩家
	public boolean hasPlayer(Player p){
		return getPlayerData().containsKey(p.getName());
	}
	
	//重载config.yml和attr.yml
    private void reloadConfig() {
    	dataConfig=reloadConfig("data/attr.yml",dataFile);
    	config=reloadConfig("config.yml",configFile);
    }
    
  //重载yaml文件
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
		getTestData();
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
		getUIData();
		getAttrsData();
		outputInfo("载入完毕！");
	}
	
	//读取GUI数据
	private void getGUIData(){
		setTitleGUI(getConfig().getString("GUI.title"));
		setName(getConfig().getString("GUI.name"));
		setLine(getConfig().getString("GUI.line"));
		setStyle(getConfig().getString("GUI.style"));
		AGUI.load();
	}
	
	//读取UI数据
	private void getUIData(){
		setWarn(getConfig().getBoolean("ui.warn"));
		setBdmsg(getConfig().getString("ui.bdmsg"));
		setCmsg(getConfig().getString("ui.cmsg"));
		setHmmsg(getConfig().getString("ui.hmmsg"));
		setMmsg(getConfig().getString("ui.mmsg"));
	}
	
	//获取test数据
	private void getTestData(){
		setUseLevelChangeHealth(getConfig().getBoolean("test.useleveluphealthchange"));
		setDebug(getConfig().getBoolean("test.debug"));
	}
	
	//读取数据库数据
	private void getDatabaseData() {
		setDatabaseEnable(getConfig().getBoolean("database.enable"));
		setPort(getConfig().getInt("database.port"));
		setHost(getConfig().getString("database.host"));
		setUsername(getConfig().getString("database.username"));
		setPassword(getConfig().getString("database.password"));
		if(isDatabaseEnable()){
			ADatabase.init();
			ADatabase.load();
			ADatabase.createTable();
		}
	}

	//读取物品数据
	private void getItemsData() {
		setCount(getConfig().getInt("count"));
		List<AItem> temp=new ArrayList<AItem>(getCount());
		for(int i=0;i<getCount();++i){
			temp.add(new AItem(
					getConfig().getString("list."+(i+1)+".name"),
					getConfig().getStringList("list."+(i+1)+".lore"),
					getConfig().getString("list."+(i+1)+".id"),
					getConfig().getInt("list."+(i+1)+".chance"),
					getConfig().getInt("list."+(i+1)+".attr"),
					getConfig().getInt("list."+(i+1)+".strong"),
					getConfig().getDouble("list."+(i+1)+".money")
				));
		}
		setItems(temp);
	}
	
	//读取attrs设置数据
	private void getAttrsData(){
		setNames(new HashMap<AAttr,String>());
		setUnNames(new HashMap<String,AAttr>());
		setMaxLevels(new HashMap<AAttr,Integer>());
		setDefaults(new HashMap<AAttr,Integer>());
		AAttr attrs[]=AAttr.values();
		for(AAttr attr:attrs){
			String name=getConfig().getString("more."+attr.name()+".name").replace('&', '§');
			int maxLevel=getConfig().getInt("more."+attr.name()+".maxlevel");
			int defaultValue=getConfig().getInt("more."+attr.name()+".default");
			getNames().put(attr,name);
			getUnNames().put(name,attr);
			getMaxLevels().put(attr,maxLevel);
			getDefaults().put(attr,defaultValue);
		}
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
				int t=getDataConfig().getInt("name."+name+"."+attrs[j].name());
				map.put(attrs[j], t);
			}
			maps.put(name, map);
		}
		setPlayerData(maps);
	}
	
	//读取数据库
	private void loadDatabaseAttr(){
		Map<String,Map<AAttr,Integer>> maps=new HashMap<String,Map<AAttr,Integer>>();
		List<String> players=ADatabase.getPlayerList();
		for(int i=0;i<players.size();++i){
			String name=players.get(i);
			Map<AAttr,Integer> map=new HashMap<AAttr,Integer>();
			AAttr attrs[]=AAttr.values();
			for(int j=0;j<attrs.length;++j){
				map.put(attrs[j], ADatabase.getPlayerAttr(name,attrs[j]));
			}
			maps.put(name, map);
		}
		setPlayerData(maps);
	}
	
	//输出错误
	public void outputError(String msg){
		getMain().getLogger().log(Level.SEVERE, msg);
		getMain().setError(true);
	}
	
	//输出警告
	public void outputWarning(String msg){
		getMain().getLogger().log(Level.WARNING, msg);
	}
	
	//输出debug
	public void outputDebug(String msg){
		if(isDebug())
			outputInfo("[Debug] "+msg);
	}
	
	//输出信息
	public void outputInfo(String msg){
		getMain().getLogger().info(msg);
	}
	
	//写入config.yml
	public boolean fresh(){
		getConfig().set("ui.warn", isWarn());
		try {
			getConfig().save(configFile);
		} catch (IOException e) {
			return false;
		}
		return true;
	}
}
