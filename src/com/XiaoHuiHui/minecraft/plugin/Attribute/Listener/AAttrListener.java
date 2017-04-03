package com.XiaoHuiHui.minecraft.plugin.Attribute.Listener;

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

import com.XiaoHuiHui.minecraft.plugin.Attribute.Data.AAttr;
import com.XiaoHuiHui.minecraft.plugin.Attribute.Data.AData;

public class AAttrListener implements Listener {
	private AData data;
	//AMain main;

	//Constructor
	public AAttrListener(){
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
		return damager instanceof Player;
	}
	
	private double damageAdd(EntityDamageByEntityEvent event, double add) {
		double damage=event.getDamage();
		getData().outputDebug("原伤害: "+damage);
		add/=100;
		add+=1;
		damage*=add;
		event.setDamage(damage);
		getData().outputDebug("改后伤害: "+damage);
		return damage;
	}
	
	private void scaleHealth(HumanEntity p) {
		double add=getData().getAttr(p.getName(), AAttr.HEALTH);
		if(add>getData().getMaxLevel(AAttr.HEALTH)){
			add=getData().getMaxLevel(AAttr.HEALTH);
		}
		add/=100;
		add+=1;
		p.setMaxHealth(add*20);
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
		getData().outputDebug(event.getEventName()+" ATTACK");
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
		getData().outputDebug(event.getEventName()+" BLOOD_DRINK");
		getData().outputDebug("原伤害: "+damage);
		add/=100;
		damage*=add;
		getData().outputDebug("计算后伤害: "+damage);
		double heal=p.getHealth();
		getData().outputDebug("原血量: "+heal);
		//TODO:来个GUI看看
		heal+=damage;
		if(heal>p.getMaxHealth()){
			heal=p.getMaxHealth();
		}
		p.setHealth(heal);
		getData().outputDebug("改后血量: "+p.getHealth());
		if(getData().isWarn()){
			p.sendMessage(getData().getBdmsg().replaceAll("%VALUE%", ""+damage));
		}
	}
	
	//荆棘
	@EventHandler(priority = EventPriority.HIGH)
	public void onDamagedBrambles(EntityDamageByEntityEvent event){
		Entity damager=event.getEntity();
		if(!isPlayer(event,damager)){
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
		double damage=event.getFinalDamage();
		getData().outputDebug(event.getEventName()+" BRAMBLES");
		getData().outputDebug("原伤害: "+damage);
		add/=100;
		damage*=add;
		getData().outputDebug("反伤害: "+damage);
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
				name="§3"+p2.getType().name()+"§2";
			}
			if(damager2 instanceof Player){
				Player p3=(Player)damager2;
				name="§3"+p3.getName()+"§2";
				p3.sendMessage(getData().getHmmsg().replaceAll("%PLAYERNAME%", name));
			}
			p1.sendMessage(getData().getMmsg().replaceAll("%PLAYERNAME%", name));
		}
		event.setDamage(0);
		getData().outputDebug(event.getEventName()+" DODGE");
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
		getData().outputDebug(event.getEventName()+" CRIT");
		double damage=damageAdd(event, add);
		if(getData().isWarn()){
			p.sendMessage(getData().getCmsg().replaceAll("%VALUE%", ""+damage));
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
		getData().outputDebug(event.getEventName()+" DAMAGED");
		damageAdd(event,add);
	}
	
	//防御
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onDamagedDefense(EntityDamageByEntityEvent event){
		Entity damager=event.getEntity();
		if(!isPlayer(event,damager)){
			return;
		}
		Player p=(Player)damager;
		double def=getData().getAttr(p, AAttr.DEFENSE);
		if(def>getData().getMaxLevel(AAttr.DEFENSE)){
			def=getData().getMaxLevel(AAttr.DEFENSE);
		}
		double damage=event.getDamage();
		getData().outputDebug(event.getEventName()+" DEFENSE");
		getData().outputDebug("原伤害: "+damage);
		def/=100;
		def=1-def;
		damage*=def;
		event.setDamage(damage);
		getData().outputDebug("防御减免伤害: "+damage);
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
		double heal=event.getAmount();
		getData().outputDebug(event.getEventName());
		getData().outputDebug("原恢复生命: "+heal);
		add/=100;
		add+=1;
		heal*=add;
		event.setAmount(heal);
		
		getData().outputDebug("改后恢复生命: "+heal);
	}
	
	//生命加成
	@EventHandler(ignoreCancelled=true)
	public void onLogin(PlayerJoinEvent event){
		Player p=event.getPlayer();
		scaleHealth(p);
		getData().outputDebug(event.getEventName()+" JOIN");
		getData().outputDebug(p.getName()+" 的最大生命值:"+p.getMaxHealth());
	}
	
	//生命加成
	@EventHandler(ignoreCancelled=true)
	public void onLevelUp(PlayerLevelChangeEvent event){
		if(!getData().isUseLevelChangeHealth()){
			return;
		}
		Player p=event.getPlayer();
		scaleHealth(p);
		getData().outputDebug(event.getEventName()+" LEVELUP");
		getData().outputDebug(p.getName()+" 的最大生命值:"+p.getMaxHealth());
	}

	//生命加成
	@EventHandler(ignoreCancelled=true)
	public void onItemHeld(PlayerItemHeldEvent event){
		Player p=event.getPlayer();
		scaleHealth(p);
		getData().outputDebug(event.getEventName()+" HELD");
		getData().outputDebug(p.getName()+" 的最大生命值: "+p.getMaxHealth());
	}
	
	//生命加成
	@EventHandler(ignoreCancelled=true)
	public void onInventoryClose(InventoryCloseEvent event){
		HumanEntity p=event.getPlayer();
		if(p==null)return;
		scaleHealth(p);
		getData().outputDebug(event.getEventName()+" CLOSE");
		getData().outputDebug(p.getName()+" 的最大生命值:"+p.getMaxHealth());
	}
}
