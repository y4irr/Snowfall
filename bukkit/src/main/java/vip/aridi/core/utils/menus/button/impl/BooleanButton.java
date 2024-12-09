package vip.aridi.core.utils.menus.button.impl;

import vip.aridi.core.utils.Callback;
import vip.aridi.core.utils.menus.button.Button;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import java.util.ArrayList;
import java.util.List;

public class BooleanButton extends Button {

    private final boolean confirm;
    private final Callback<Boolean> callback;

    public BooleanButton(boolean confirm, Callback<Boolean> callback) {
        this.confirm = confirm;
        this.callback = callback;
    }

    @Override
    public void clicked(Player player, int i, ClickType clickType) {
        if (this.confirm) {
            player.playSound(player.getLocation(), Sound.NOTE_PIANO, 20.0F, 0.1F);
        } else {
            player.playSound(player.getLocation(), Sound.DIG_GRAVEL, 20.0F, 0.1F);
        }
        player.closeInventory();
        this.callback.callback(this.confirm);
    }

    @Override
    public String getName(Player player) {
        return this.confirm ? ChatColor.GREEN + "Confirm" : ChatColor.RED + "Cancel";
    }

    @Override
    public List<String> getDescription(Player player) {
        return new ArrayList<>();
    }

    @Override
    public byte getDamageValue(Player player) {
        return (byte) (this.confirm ? 5 : 14);
    }

    @Override
    public Material getMaterial(Player player) {
        return Material.WOOL;
    }
}