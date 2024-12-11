package vip.aridi.core.module

import vip.aridi.core.module.core.DatabaseModule
import vip.aridi.core.module.system.GrantModule
import vip.aridi.core.module.system.RankModule
import java.util.logging.Logger

/*
 * This project can't be redistributed without
 * authorization of the developer
 *
 * Project @ Snowfall
 * @author YairSoto © 2024
 * Date: 08 - dic
 */

object SharedManager {

    @JvmStatic
    val modules: MutableList<IModule> = mutableListOf()
    val rankModule = RankModule()
    val grantModule = GrantModule()
    val databaseModule = DatabaseModule()

    const val UPDATE_PERMISSION = "IlgHL-G5aAjB-ADp1O9l-zDD"
    const val UPDATE_NAME = "U4WJ9-Ypw4XO-61cSk7t-N2R"

    const val EXECUTE_GRANT = "20EPQ-Jpkqt2-qw2Unwl-bx9"
    const val REMOVE_GRANT = "s5HBe-Xt3tUF-u3xk4s6-y1Q"

    const val CREATE_RANK = "zclKa-2nsSd9-hWO17fh-3kP"
    const val DELETE_RANK = "3D3QN-80ktLT-S7TJkbg-fO9"
    const val UPDATE_RANK = "1f8nU-KF7RVI-ARl1rKp-K1V"

    var mongoUri: String = ""
    var mongoDbName: String = ""
    var redisIp: String = ""
    var redisPort: Int = 6379
    var redisChannel: String = ""
    var redisPassword: String = ""

}