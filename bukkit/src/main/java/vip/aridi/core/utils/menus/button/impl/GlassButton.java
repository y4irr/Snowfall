package vip.aridi.core.utils.menus.button.impl;

import vip.aridi.core.utils.menus.button.Button;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.List;

public class GlassButton extends Button {

    private final int data;

    public GlassButton(int data) {
        this.data = data;
    }

    @Override
    public String getName(Player player) {
        return translate("&7");
    }

    @Override
    public List<String> getDescription(Player player) {
        return null;
    }

    @Override
    public Material getMaterial(Player player) {
        return Material.STAINED_GLASS_PANE;
    }

    @Override
    public byte getDamageValue(Player player) {
        return (byte) data;
    }

    public static String translate(String in) {
        return ChatColor.translateAlternateColorCodes('&', in);
    }
}