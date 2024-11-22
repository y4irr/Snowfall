package vip.aridi.core.utils.menus.button.impl;

import vip.aridi.core.utils.menus.Menu;
import vip.aridi.core.utils.menus.button.Button;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class JumpToMenuButton extends Button {

	private final Menu menu;
	private final ItemStack itemStack;

	public JumpToMenuButton(Menu menu, ItemStack itemStack) {
		this.menu = menu;
		this.itemStack = itemStack;
	}

	@Override
	public String getName(Player player) {
		return this.itemStack.getItemMeta().getDisplayName();
	}

	@Override
	public List<String> getDescription(Player player) {
		return this.itemStack.getItemMeta().getLore();
	}

	@Override
	public Material getMaterial(Player player) {
		return this.itemStack.getType();
	}

	@Override
	public byte getDamageValue(Player player) {
		return (byte) this.itemStack.getDurability();
	}

	@Override
	public void clicked(Player player, int i, ClickType clickType) {
		menu.openMenu(player);
	}
}
