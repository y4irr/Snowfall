package vip.aridi.core.utils

/*
 * This project can't be redistributed without
 * authorization of the developer
 *
 * Project @ Snowfall
 * @author Yair © 2024
 * Date: 22 - nov
 */

class Vector3FWrapper(val vector: Any) {

    constructor(x: Float, y: Float, z: Float) : this(wrap(x, y, z))

    fun x(x: Float): Vector3FWrapper {
        VECTOR3F_x.set(vector, x)
        return this
    }

    fun y(y: Float): Vector3FWrapper {
        VECTOR3F_y.set(vector, y)
        return this
    }

    fun z(z: Float): Vector3FWrapper {
        VECTOR3F_z.set(vector, z)
        return this
    }

    fun clone(): Vector3FWrapper {
        return Vector3FWrapper(
            wrap(
                VECTOR3F_x.get(vector) as Float,
                VECTOR3F_y.get(vector) as Float,
                VECTOR3F_z.get(vector) as Float
            )
        )
    }

    companion object {
        private val VECTOR3F_CLASS = MinecraftReflection.getNMSClass("Vector3f")!!
        private val VECTOR3F_x = Reflection.getDeclaredField(VECTOR3F_CLASS, "x")!!
        private val VECTOR3F_y = Reflection.getDeclaredField(VECTOR3F_CLASS, "y")!!
        private val VECTOR3F_z = Reflection.getDeclaredField(VECTOR3F_CLASS, "z")!!
        private val VECTOR3F_CONSTRUCTOR =
            Reflection.getConstructor(VECTOR3F_CLASS, Float::class.java, Float::class.java, Float::class.java)!!

        private fun wrap(x: Float, y: Float, z: Float): Any {
            return VECTOR3F_CONSTRUCTOR.newInstance(x, y, z)
        }
    }

}