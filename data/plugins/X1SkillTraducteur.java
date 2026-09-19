package data.plugins;

public class X1SkillTraducteur {

    public static String getNomLisible(String skillId) {
        if (skillId.equals("helmsmanship")) return "Helmsmanship";
        if (skillId.equals("combat_endurance")) return "Combat Endurance";
        if (skillId.equals("systems_expertise")) return "Systems Expertise";
        if (skillId.equals("field_modulation")) return "Field Modulation";
        if (skillId.equals("point_defense")) return "Point Defense";
        if (skillId.equals("target_analysis")) return "Target Analysis";
        if (skillId.equals("ballistic_mastery")) return "Ballistic Mastery";
        if (skillId.equals("energy_weapon_mastery")) return "Energy Weapon Mastery";
        if (skillId.equals("missile_specialization")) return "Missile Specialization";
        if (skillId.equals("damage_control")) return "Damage Control";
        if (skillId.equals("impact_mitigation")) return "Impact Mitigation";
        if (skillId.equals("gunnery_implants")) return "Gunnery Implants";
        if (skillId.equals("ordnance_expert")) return "Ordnance Expert";
        if (skillId.equals("polarized_armor")) return "Polarized Armor";
        if (skillId.equals("fighter_uplink")) return "Fighter Uplink";
        if (skillId.equals("carrier_group")) return "Carrier Group";
        if (skillId.equals("strike_commander")) return "Strike Commander";
        return skillId;
    }
}
