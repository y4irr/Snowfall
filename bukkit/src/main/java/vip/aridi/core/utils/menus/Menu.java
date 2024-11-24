package vip.aridi.core.utils.menus;

import vip.aridi.core.Snowfall;
import vip.aridi.core.utils.CC;
import vip.aridi.core.utils.menus.button.Button;
import vip.aridi.core.utils.menus.listener.ButtonListener;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftHumanEntity;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public abstract class Menu {

    static {
        Snowfall.Companion.get().getServer().getPluginManager().registerEvents(new ButtonListener(), Snowfall.Companion.get());
        currentlyOpenedMenus = new HashMap<>();
        checkTasks = new HashMap<>();
    }

    private static Method openInventoryMethod;
    private ConcurrentHashMap<Integer, Button> buttons = new ConcurrentHashMap<>();
    private boolean autoUpdate = false;
    private boolean updateAfterClick = true;
    private boolean placeholder = false;
    private boolean noncancellingInventory = false;

    private static Map<UUID, Menu> currentlyOpenedMenus;
    private static Map<UUID, BukkitRunnable> checkTasks;

    public Menu() {
        this.buttons = new ConcurrentHashMap<>();
    }

    public ConcurrentHashMap<Integer, Button> getButtons() {
        return buttons;
    }

    public boolean isAutoUpdate() {
        return autoUpdate;
    }

    public void setAutoUpdate(boolean autoUpdate) {
        this.autoUpdate = autoUpdate;
    }

    public boolean isUpdateAfterClick() {
        return updateAfterClick;
    }

    public void setUpdateAfterClick(boolean updateAfterClick) {
        this.updateAfterClick = updateAfterClick;
    }

    public boolean isPlaceholder() {
        return placeholder;
    }

    public void setPlaceholder(boolean placeholder) {
        this.placeholder = placeholder;
    }

    public boolean isNoncancellingInventory() {
        return noncancellingInventory;
    }

    public void setNoncancellingInventory(boolean noncancellingInventory) {
        this.noncancellingInventory = noncancellingInventory;
    }

    public static Map<UUID, Menu> getCurrentlyOpenedMenus() {
        return currentlyOpenedMenus;
    }

    public static Map<UUID, BukkitRunnable> getCheckTasks() {
        return checkTasks;
    }

    private Inventory createInventory(Player player) {
        final Inventory inventory = Snowfall.Companion.get().getServer().createInventory(player, size(player), CC.translate(getTitle(player)));

        for (Map.Entry<Integer, Button> buttonEntry : getButtons(player).entrySet()) {
            this.buttons.put(buttonEntry.getKey(), buttonEntry.getValue());
            final ItemStack item = createItemStack(player, buttonEntry.getValue());
            inventory.setItem(buttonEntry.getKey(), item);
        }

        if (this.isPlaceholder()) {
            final Button placeholder = Button.placeholder(Material.STAINED_GLASS_PANE, (byte) 15);

            for (int index = 0; index < this.size(player); index++) {
                if (this.getButtons(player).get(index) == null) {
                    this.buttons.put(index, placeholder);
                    inventory.setItem(index, placeholder.getButtonItem(player));
                }
            }
        }

        return inventory;
    }

    private static Method getOpenInventoryMethod() {
        if (openInventoryMethod == null) {
            try {
                openInventoryMethod = CraftHumanEntity.class.getDeclaredMethod("openInventory", Inventory.class);
                openInventoryMethod.setAccessible(true);
            } catch (NoSuchMethodException var1) {
                var1.printStackTrace();
            }
        }
        return openInventoryMethod;
    }

    private ItemStack createItemStack(Player player, Button button) {
        ItemStack item = button.getButtonItem(player);

        if (item.getType() != Material.SKULL_ITEM) {
            ItemMeta meta = item.getItemMeta();
            if (meta != null && meta.hasDisplayName()) {
                meta.setDisplayName(meta.getDisplayName());
            }
            item.setItemMeta(meta);
        }
        return item;
    }

    public void openMenu(Player player) {
        final EntityPlayer entityPlayer = ((CraftPlayer) player).getHandle();
        final Inventory inventory = this.createInventory(player);

        try {
            getOpenInventoryMethod().invoke(entityPlayer.getBukkitEntity(), inventory);
            this.update(player);
        } catch (Exception var5) {
            var5.printStackTrace();
        }
    }

    private void update(Player player) {
        cancelCheck(player);
        currentlyOpenedMenus.put(player.getUniqueId(), this);
        this.onOpen(player);

        final BukkitRunnable runnable = new BukkitRunnable() {
            @Override
            public void run() {
                if (!player.isOnline()) {
                    Menu.cancelCheck(player);
                    Menu.currentlyOpenedMenus.remove(player.getUniqueId());
                }

                if (isAutoUpdate()) {
                    player.getOpenInventory().getTopInventory().setContents(createInventory(player).getContents());
                }
            }
        };

        runnable.runTaskTimer(Snowfall.Companion.get(), 10L, 10L);
        checkTasks.put(player.getUniqueId(), runnable);
    }

    public static void cancelCheck(Player player) {
        if (checkTasks.containsKey(player.getUniqueId())) {
            checkTasks.remove(player.getUniqueId()).cancel();
        }
    }

    public int size(Player player) {
        int highest = 0;

        for (int buttonValue : getButtons(player).keySet()) {
            if (buttonValue > highest) {
                highest = buttonValue;
            }
        }

        return (int) (Math.ceil((highest + 1) / 9D) * 9D);
    }

    public int getSlot(int x, int y) {
        return ((9 * y) + x);
    }

    public abstract String getTitle(Player player);

    public abstract Map<Integer, Button> getButtons(Player player);

    public void onOpen(Player player) {
    }

    public void onClose(Player player) {
    }
}