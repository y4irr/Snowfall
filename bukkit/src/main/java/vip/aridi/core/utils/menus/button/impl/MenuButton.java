package vip.aridi.core.utils.menus.button.impl;

/*
 * Author: T4yrn © 2024
 * Project: nexus
 * Date: 6/3/2024 - 22:46
 */

import dev.ryu.core.bukkit.util.ItemUtils;
import dev.ryu.core.bukkit.util.enchantment.GlowEnchantment;
import vip.aridi.core.utils.menus.button.Button;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

public class MenuButton extends Button {

    private ItemStack icon;
    private Function<Player, ItemStack> getIcon;

    private String name;
    private Function<Player, String> getName;

    private Byte data;
    private Function<Player, Byte> getData;

    private Integer amount;
    private Function<Player, Integer> getAmount;

    private List<String> lore;
    private Function<Player, List<String>> getLore;

    private BiConsumer<Player, ClickType> action;
    private Map<ClickType, Consumer<Player>> actions = new EnumMap<>(ClickType.class);

    private boolean glow;
    private java.util.function.Predicate<Player> isGlow;

    public MenuButton icon(ItemStack itemStack) {
        this.icon = itemStack.clone();
        return this;
    }

    public MenuButton icon(Material material) {
        this.icon = new ItemStack(material);
        return this;
    }

    public MenuButton icon(java.util.function.Function<Player, ItemStack> getIcon) {
        this.getIcon = getIcon;
        return this;
    }

    public MenuButton texturedIcon(String texture) {
        this.icon = ItemUtils.buildHeadByValue(new ItemStack(Material.SKULL_ITEM, 1, (short) 3), texture);
        return this;
    }

    public MenuButton playerTexture(String owner) {
        this.icon = ItemUtils.getPlayerHeadItem(owner);
        return this;
    }

    public MenuButton name(String name) {
        this.name = name;
        return this;
    }

    public MenuButton name(java.util.function.Function<Player, String> getName) {
        this.getName = getName;
        return this;
    }

    public MenuButton amount(int amount) {
        this.amount = amount;
        return this;
    }

    public MenuButton amount(java.util.function.Function<Player, Integer> getAmount) {
        this.getAmount = getAmount;
        return this;
    }

    public final MenuButton setColor(Color color) {
        if (!this.icon.getType().name().startsWith("LEATHER_")) {
            throw new IllegalArgumentException("setColor is only applicable for leather armor!");
        }
        LeatherArmorMeta leatherArmorMeta = (LeatherArmorMeta) this.icon.getItemMeta();
        leatherArmorMeta.setColor(color);
        this.icon.setItemMeta(leatherArmorMeta);
        return this;
    }

    public MenuButton data(byte data) {
        this.data = data;
        return this;
    }

    public MenuButton data(java.util.function.Function<Player, Byte> getData) {
        this.getData = getData;
        return this;
    }

    public MenuButton lore(List<String> lore) {
        this.lore = new ArrayList<>(lore);
        return this;
    }

    public MenuButton lore(java.util.function.Function<Player, List<String>> getLore) {
        this.getLore = getLore;
        return this;
    }

    public MenuButton clearLore() {
        lore = new ArrayList<>();
        return this;
    }

    public MenuButton action(java.util.function.BiConsumer<Player, ClickType> action) {
        this.action = action;
        return this;
    }

    public MenuButton action(ClickType clickType, java.util.function.Consumer<Player> action) {
        actions.put(clickType, action);
        return this;
    }

    public MenuButton action(java.util.function.Consumer<Player> action, ClickType... clickTypes) {
        for (ClickType clickType : clickTypes) {
            actions.put(clickType, action);
        }
        return this;
    }

    public MenuButton glow(boolean glow) {
        this.glow = glow;
        return this;
    }

    public MenuButton glow(java.util.function.Predicate<Player> isGlow) {
        this.isGlow = isGlow;
        return this;
    }

    @Override
    public String getName(Player var1) {
        return null;
    }

    @Override
    public List<String> getDescription(Player var1) {
        return null;
    }

    @Override
    public Material getMaterial(Player var1) {
        return null;
    }

    @Override
    public ItemStack getButtonItem(Player player) {
        if (icon == null && getIcon == null) {
            return new ItemStack(Material.AIR);
        }

        if (getAmount != null) {
            icon.setAmount(getAmount.apply(player));
        } else if (amount != null) {
            icon.setAmount(amount);
        }

        ItemStack item = (getIcon != null) ? getIcon.apply(player) : icon.clone();

        if (getData != null) {
            item.setDurability(getData.apply(player));
        } else if (data != null) {
            item.setDurability(data);
        }

        ItemMeta itemMeta = item.getItemMeta();

        if (getName != null) {
            itemMeta.setDisplayName(getName.apply(player));
        } else if (name != null) {
            itemMeta.setDisplayName(name);
        }

        if (getLore != null) {
            itemMeta.setLore(getLore.apply(player));
        } else if (lore != null) {
            itemMeta.setLore(new ArrayList<>(lore));
        }

        item.setItemMeta(itemMeta);

        if (glow || (isGlow != null && isGlow.test(player))) {
            GlowEnchantment.addGlow(item);
        }

        return item;
    }

    @Override
    public void clicked(Player player, int slot, ClickType clickType) {
        if (action != null) {
            action.accept(player, clickType);
        } else {
            if (actions.containsKey(clickType)) {
                actions.get(clickType).accept(player);
            }
        }
    }

}