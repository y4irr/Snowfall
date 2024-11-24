package vip.aridi.core.utils.menus;

import vip.aridi.core.utils.CC;
import vip.aridi.core.utils.menus.button.impl.PageButton;
import vip.aridi.core.utils.menus.button.Button;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public abstract class PaginatedMenu extends Menu {

    private int page = 1;
    private int previousSlot = 0;
    private int nextSlot = 8;

    public int getPage() {
        return page;
    }

    public int getPreviousSlot() {
        return previousSlot;
    }

    public void setPreviousSlot(int previousSlot) {
        this.previousSlot = previousSlot;
    }

    public int getNextSlot() {
        return nextSlot;
    }

    public void setNextSlot(int nextSlot) {
        this.nextSlot = nextSlot;
    }

    @Override
    public String getTitle(Player player) {
        return CC.translate(getPrePaginatedTitle(player) + " - " + page + "/" + getPages(player));
    }

    public final void modPage(Player player, int mod) {
        this.page += mod;
        this.getButtons().clear();
        this.openMenu(player);
    }

    public final int getPages(Player player) {
        final int buttonAmount = this.getAllPagesButtons(player).size();
        return buttonAmount == 0 ? 1 : (int) Math.ceil((double) buttonAmount / (double) this.getMaxItemsPerPage(player));
    }

    @Override
    public final Map<Integer, Button> getButtons(Player player) {
        final int minIndex = (int) ((double) (page - 1) * getMaxItemsPerPage(player));
        final int maxIndex = (int) ((double) (page) * getMaxItemsPerPage(player));

        final HashMap<Integer, Button> buttons = new HashMap<>();
        final Map<Integer, Button> global = getGlobalButtons(player);

        if (global != null) {
            buttons.putAll(global);
        }

        for (Map.Entry<Integer, Button> entry : getAllPagesButtons(player).entrySet()) {
            int ind = entry.getKey();
            if (ind >= minIndex && ind < maxIndex) {
                ind -= (int) ((double) (getMaxItemsPerPage(player)) * (page - 1)) - 9;
                buttons.put(ind, entry.getValue());
            }
        }

        buttons.put(previousSlot, new PageButton(-1, this));
        buttons.put(nextSlot, new PageButton(1, this));

        return buttons;
    }

    public int getMaxItemsPerPage(Player player) {
        return 18;
    }

    public Map<Integer, Button> getGlobalButtons(Player player) {
        return null;
    }

    public abstract String getPrePaginatedTitle(Player player);

    public abstract Map<Integer, Button> getAllPagesButtons(Player player);
}
