package com.XiaoHuiHui.minecraft.plugin.Attribute.Data;

public enum AAttr {
	ATTACK("攻击加成"),//攻击加成
	DEFENSE("防御"),//防御
	CRIT("暴击几率"),//暴击几率
	CRIT_DMG("暴击伤害"),//暴击伤害
	DODGE("闪避"),//闪避几率
	BLOOD_DRINK("嗜血几率"),//嗜血几率
	BLOOD_DRINK_DMG("嗜血量"),//嗜血量
	PLAYER_DAMAGED("对玩家伤害加成"),//对玩家伤害加成
	ENTITY_DAMAGED("对怪物伤害加成"),//对怪物伤害加成
	EX_HEAL("额外生命恢复"),//额外生命恢复
	HEALTH("生命"),//生命加成
	BRAMBLES("荆棘");//荆棘
	
	private AAttr(){
	}

	private AAttr(String s){
	}
	
	public String getName(){
		switch(this){
		case ATTACK:
			return "攻击加成";
		case DEFENSE:
			return "防御";
		case CRIT:
			return "暴击几率";
		case CRIT_DMG:
			return "暴击伤害";
		case DODGE:
			return "闪避";
		case BLOOD_DRINK:
			return "嗜血几率";
		case BLOOD_DRINK_DMG:
			return "嗜血量";
		case PLAYER_DAMAGED:
			return "对玩家伤害加成";
		case ENTITY_DAMAGED:
			return "对怪物伤害加成";
		case EX_HEAL:
			return "额外生命恢复";
		case HEALTH:
			return "生命";
		case BRAMBLES:
			return "荆棘";
		default:
			return null;
		}
	}
}
