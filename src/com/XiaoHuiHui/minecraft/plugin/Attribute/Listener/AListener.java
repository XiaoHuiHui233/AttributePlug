package com.XiaoHuiHui.minecraft.plugin.Attribute.Listener;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.bukkit.entity.Entity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent.RegainReason;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLevelChangeEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.XiaoHuiHui.minecraft.plugin.Attribute.Data.AAttr;
import com.XiaoHuiHui.minecraft.plugin.Attribute.Data.AData;

public class AListener implements Listener {
	private AData data;
	//AMain main;
	
	private Map<Player,Double> records=new HashMap<Player,Double>();

	//Constructor
	public AListener(){
		super();
		setData(AData.getInstance());
	}
	
	public AData getData() {
		return data;
	}

	private void setData(AData data) {
		this.data = data;
	}

	private boolean isPlayer(EntityDamageByEntityEvent event,Entity damager){
		if(event.isCancelled()){
			return false;
		}
		if(!(damager instanceof Player)){
			return false;
		}
		return true;
	}
	
	private void damageAdd(EntityDamageByEntityEvent event, double add) {
		add/=100;
		add+=1;
		double damage=event.getDamage();
		damage*=add;
		event.setDamage(damage);
	}
	
	private void scaleHealth(HumanEntity p,double temp) {
		double add=getData().getAttr(p.getName(), AAttr.HEALTH);
		if(add>getData().getMaxLevel(AAttr.HEALTH)){
			add=getData().getMaxLevel(AAttr.HEALTH);
		}
		add/=100;
		add+=1;
		temp*=add;
		p.setMaxHealth(temp);
	}
	
	//攻击加成
	@EventHandler(priority = EventPriority.NORMAL)
	public void onDamagedAttack(EntityDamageByEntityEvent event){
		Entity damager=event.getDamager();
		if(!isPlayer(event,damager)){
			return;
		}
		Player p=(Player)damager;
		double add=getData().getAttr(p, AAttr.ATTACK);
		if(add>getData().getMaxLevel(AAttr.ATTACK)){
			add=getData().getMaxLevel(AAttr.ATTACK);
		}
		damageAdd(event, add);
	}
	
	//嗜血
	@EventHandler(priority = EventPriority.HIGH)
	public void onDamagedDrinkBlood(EntityDamageByEntityEvent event){
		Entity damager=event.getDamager();
		if(!isPlayer(event,damager)){
			return;
		}
		Player p=(Player)damager;
		int prop=getData().getAttr(p, AAttr.BLOOD_DRINK);
		if(prop>getData().getMaxLevel(AAttr.BLOOD_DRINK)){
			prop=getData().getMaxLevel(AAttr.BLOOD_DRINK);
		}
		double add=getData().getAttr(p, AAttr.BLOOD_DRINK_DMG);
		if(add>getData().getMaxLevel(AAttr.BLOOD_DRINK_DMG)){
			add=getData().getMaxLevel(AAttr.BLOOD_DRINK_DMG);
		}
		Random rand=new Random(System.currentTimeMillis());
		int pro=rand.nextInt(100);
		if(pro>prop){
			return;
		}
		double damage=event.getFinalDamage();
		add/=100;
		damage*=add;
		double heal=p.getHealth();
		//TODO:来个GUI看看
		p.setHealth(heal+damage);
		if(getData().isWarn()){
			p.sendMessage("&a*你触发了嗜血*");
		}
	}
	
	//荆棘
	@EventHandler(priority = EventPriority.HIGH)
	public void onDamagedBrambles(EntityDamageByEntityEvent event){
		Entity damager=event.getEntity();
		if(isPlayer(event,damager)){
			return;
		}
		Player p=(Player)damager;
		Entity causer=event.getDamager();
		if(!(causer instanceof LivingEntity)){
			return;
		}
		LivingEntity le=(LivingEntity)causer;
		double add=getData().getAttr(p,AAttr.BRAMBLES);
		if(add>getData().getMaxLevel(AAttr.BRAMBLES)){
			add=getData().getMaxLevel(AAttr.BRAMBLES);
		}
		add/=100;
		double damage=event.getFinalDamage();
		damage*=add;
		le.damage(damage);
	}
	
	//闪避
	@EventHandler(priority = EventPriority.LOWEST)
	public void onDamagedDodge(EntityDamageByEntityEvent event){
		Entity damager=event.getEntity();
		Entity damager2=event.getDamager();
		if(!isPlayer(event,damager)){
			return;
		}
		Player p1=(Player)damager;
		int dodge=getData().getAttr(p1, AAttr.DODGE);
		if(dodge>getData().getMaxLevel(AAttr.DODGE)){
			dodge=getData().getMaxLevel(AAttr.DODGE);
		}
		Random rand=new Random(System.currentTimeMillis());
		int pro=rand.nextInt(100);
		if(pro>dodge){
			return;
		}
		if(getData().isWarn()){
			String name="§3"+damager2.getType().name()+"§2";
			if(damager2 instanceof LivingEntity){
				LivingEntity p2=(LivingEntity)damager2;
				name="§3"+p2.getCustomName()+"§2";
			}
			if(damager2 instanceof Player){
				Player p3=(Player)damager2;
				name="§3"+p3.getDisplayName()+"§2";
				p3.sendMessage("§3"+p1.getDisplayName()+"§aMISS§2了你的攻击！");
			}
			p1.sendMessage("§2你§aMISS§2了来自"+name+"的攻击!");
		}
		event.setDamage(0);
		event.setCancelled(true);
	}
	
	//暴击
	@EventHandler(priority = EventPriority.HIGH)
	public void onDamagedCrit(EntityDamageByEntityEvent event){
		Entity damager=event.getDamager();
		if(!isPlayer(event,damager)){
			return;
		}
		Player p=(Player)damager;
		int prop=getData().getAttr(p, AAttr.CRIT);
		if(prop>getData().getMaxLevel(AAttr.CRIT)){
			prop=getData().getMaxLevel(AAttr.CRIT);
		}
		double add=getData().getAttr(p, AAttr.CRIT_DMG);
		if(add>getData().getMaxLevel(AAttr.CRIT_DMG)){
			add=getData().getMaxLevel(AAttr.CRIT_DMG);
		}
		Random rand=new Random(System.currentTimeMillis());
		int pro=rand.nextInt(100);
		if(pro>prop){
			return;
		}
		damageAdd(event, add);
		if(getData().isWarn()){
			p.sendMessage("&a*你触发了暴击*");
		}
	}
	
	//对玩家或者生物的伤害加成
	@EventHandler(priority = EventPriority.HIGH)
	public void onDamagedOtherAttack(EntityDamageByEntityEvent event){
		Entity damager=event.getEntity();
		Entity damager2=event.getDamager();
		if(!isPlayer(event,damager2)){
			return;
		}
		Player p1=(Player)damager2;
		if(!(damager instanceof LivingEntity)){
			return;
		}
		double add;
		if(damager instanceof Player){
			add=getData().getAttr(p1, AAttr.PLAYER_DAMAGED);
			if(add>getData().getMaxLevel(AAttr.PLAYER_DAMAGED)){
				add=getData().getMaxLevel(AAttr.PLAYER_DAMAGED);
			}
		}else{
			add=getData().getAttr(p1, AAttr.ENTITY_DAMAGED);
			if(add>getData().getMaxLevel(AAttr.ENTITY_DAMAGED)){
				add=getData().getMaxLevel(AAttr.ENTITY_DAMAGED);
			}
		}
		damageAdd(event,add);
	}
	
	//防御
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onDamagedDefense(EntityDamageByEntityEvent event){
		Entity damager=event.getDamager();
		if(!isPlayer(event,damager)){
			return;
		}
		Player p=(Player)damager;
		int def=getData().getAttr(p, AAttr.DEFENSE);
		if(def>getData().getMaxLevel(AAttr.DEFENSE)){
			def=getData().getMaxLevel(AAttr.DEFENSE);
		}
		double damage=event.getDamage();
		damage-=def;
		event.setDamage(damage);
	}
	
	//额外回血
	@EventHandler(priority = EventPriority.LOWEST)
	public void onRegain(EntityRegainHealthEvent event){
		if(event.isCancelled())
			return;
		Entity entity=event.getEntity();
		if(!(entity instanceof Player))
			return;
		Player player=(Player)entity;
		if(!(event.getRegainReason().equals(RegainReason.SATIATED)))
			return;
		double add=getData().getAttr(player, AAttr.EX_HEAL);
		if(add>getData().getMaxLevel(AAttr.EX_HEAL)){
			add=getData().getMaxLevel(AAttr.EX_HEAL);
		}
		add/=100;
		add+=1;
		double heal=event.getAmount();
		heal*=add;
		event.setAmount(heal);
	}
	
	//生命加成
	@EventHandler(ignoreCancelled=true)
	public void onLogin(PlayerJoinEvent event){
		Player p=event.getPlayer();
		double temp=p.getMaxHealth();
		records.put(p,temp);
		scaleHealth(p,temp);
	}
	
	//生命加成
	@EventHandler(ignoreCancelled=true)
	public void onLevelUp(PlayerLevelChangeEvent event){
		if(!getData().isUseLevelChangeHealth()){
			return;
		}
		Player p=event.getPlayer();
		double temp=p.getMaxHealth();
		records.put(p, temp);
		scaleHealth(p,temp);
	}

	//生命加成
	@EventHandler(ignoreCancelled=true)
	public void onItemHeld(PlayerItemHeldEvent event){
		Player p=event.getPlayer();
		double temp=records.get(p);
		scaleHealth(p,temp);
	}
	
	//生命加成
	@EventHandler(ignoreCancelled=true)
	public void onInventoryClose(InventoryCloseEvent event){
		HumanEntity p=event.getPlayer();
		double temp=records.get(p);
		scaleHealth(p,temp);
	}
	
	//生命加成
	@EventHandler(ignoreCancelled=true)
	public void onPlayerLeave(PlayerQuitEvent event){
		HumanEntity p=event.getPlayer();
		records.remove(p);
	}
}
