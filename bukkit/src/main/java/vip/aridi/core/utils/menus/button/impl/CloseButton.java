package vip.aridi.core.utils.menus.button.impl;

import vip.aridi.core.utils.menus.button.Button;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import java.util.ArrayList;
import java.util.List;

public class CloseButton extends Button {

    private String name;
    private List<String> lore;

    public CloseButton() {
        this.name = null;
        this.lore = null;
    }

    public CloseButton(String name, List<String> lore) {
        this.name = name;
        this.lore = lore;
    }

    @Override
    public Material getMaterial(Player player) {
        return Material.REDSTONE_BLOCK;
    }

    @Override
    public byte getDamageValue(Player player) {
        return 0;
    }

    @Override
    public String getName(Player player) {
        return this.name == null ? ChatColor.RED + "Close Menu" : this.name;
    }

    @Override
    public List<String> getDescription(Player player) {
        return this.lore == null ? new ArrayList<>() : this.lore;
    }

    @Override
    public void clicked(Player player, int i, ClickType clickType) {
        player.closeInventory();
    }
}
