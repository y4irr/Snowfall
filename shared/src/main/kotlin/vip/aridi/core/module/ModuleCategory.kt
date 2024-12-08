package vip.aridi.core.module

/*
 * This project can't be redistributed without
 * authorization of the developer
 *
 * Project @ Snowfall
 * @author Yair © 2024
 * Date: 15 - nov
 */

enum class ModuleCategory(val order: Int) {
    CORE(1),
    SYSTEM(2),
    FEATURE(3),
    UTILITY(4),
    CUSTOM(5);
}
