package com.XiaoHuiHui.minecraft.plugin.Attribute.Listener;

import java.util.Random;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent.RegainReason;

import com.XiaoHuiHui.minecraft.plugin.Attribute.Data.AAttr;
import com.XiaoHuiHui.minecraft.plugin.Attribute.Data.AData;

public class AListener implements Listener {
	private AData data;
	//AMain main;

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
	
	//攻击加成
	@EventHandler(priority = EventPriority.NORMAL)
	public void onDamagedAttack(EntityDamageByEntityEvent event){
		Entity damager=event.getDamager();
		if(!isPlayer(event,damager)){
			return;
		}
		Player p=(Player)damager;
		double add=getData().getAttr(p, AAttr.ATTACK);
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
		double add=getData().getAttr(p, AAttr.BLOOD_DRINK_DMG);
		Random rand=new Random(System.currentTimeMillis());
		int pro=rand.nextInt(100);
		if(pro>prop){
			return;
		}
		double damage=event.getFinalDamage();
		damage*=add;
		double heal=p.getHealth();
		//TODO:来个GUI看看
		p.setHealth(heal+damage);
		if(getData().isWarn()){
			p.sendMessage("&a*你触发了嗜血*");
		}
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
		double add=getData().getAttr(p, AAttr.CRIT_DMG);
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
		}else{
			add=getData().getAttr(p1, AAttr.ENTITY_DAMAGED);
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
		add/=100;
		add+=1;
		double heal=event.getAmount();
		heal*=add;
		event.setAmount(heal);
	}
	
	//TODO:Lore
}
