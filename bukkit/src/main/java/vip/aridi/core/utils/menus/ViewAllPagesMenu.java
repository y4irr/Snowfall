package vip.aridi.core.utils.menus;

import vip.aridi.core.utils.menus.button.impl.BackButton;
import vip.aridi.core.utils.menus.button.Button;
import vip.aridi.core.utils.menus.button.impl.JumpToPageButton;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class ViewAllPagesMenu extends Menu {

    private final PaginatedMenu menu;

    public ViewAllPagesMenu(PaginatedMenu menu) {
        this.menu = menu;
    }

    public PaginatedMenu getMenu() {
        return menu;
    }

    @Override
    public String getTitle(Player player) {
        return "Jump to page";
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        HashMap<Integer, Button> buttons = new HashMap<>();
        buttons.put(0, new BackButton(menu));

        int index = 10;
        for (int i = 1; i <= menu.getPages(player); i++) {
            buttons.put(index++, new JumpToPageButton(i, this.menu));
            if ((index - 8) % 9 == 0) {
                index += 2;
            }
        }

        return buttons;
    }

    @Override
    public boolean isAutoUpdate() {
        return true;
    }
}
