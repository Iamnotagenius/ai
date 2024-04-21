package me.fzzyhmstrs.amethyst_imbuement.config

import me.fzzyhmstrs.amethyst_imbuement.AI
import me.fzzyhmstrs.fzzy_config.annotations.ConvertFrom
import me.fzzyhmstrs.fzzy_config.config.Config
import me.fzzyhmstrs.fzzy_config.validation.misc.ValidatedBoolean
import me.fzzyhmstrs.fzzy_config.validation.misc.ValidatedEnum
import me.fzzyhmstrs.fzzy_config.validation.number.ValidatedInt

@ConvertFrom("hud_v0.json", AI.MOD_ID)
class HudConfig: Config(AI.identity("hud_config")){

    fun getX(width: Int): Int{
        return hudCorner.get().getX(width, hudX.get())
    }

    fun getY(height: Int): Int{
        return hudCorner.get().getY(height, hudY.get())
    }

    var showHud = ValidatedBoolean(true)
    var hudCorner = ValidatedEnum(Corner.TOP_LEFT)
    var hudX = ValidatedInt(0,Int.MAX_VALUE, 0)
    var hudY = ValidatedInt(0,Int.MAX_VALUE, 0)
    var spellHudSpacing = ValidatedInt(80,145,30)
    var scrollChangesSpells = ValidatedBoolean(true)

    enum class Corner{
        TOP_LEFT{
            override fun getX(width: Int, x: Int): Int {
                return x + 6
            }
            override fun getY(height: Int, y: Int): Int {
                return y + 4
            }
        },
        BOTTOM_LEFT{
            override fun getX(width: Int, x: Int): Int {
                return x + 6
            }
            override fun getY(height: Int, y: Int): Int {
                return height - y - 35
            }
        },
        TOP_RIGHT{
            override fun getX(width: Int, x: Int): Int {
                return width - x - 93
            }
            override fun getY(height: Int, y: Int): Int {
                return y + 4
            }
        },
        BOTTOM_RIGHT{
            override fun getX(width: Int, x: Int): Int {
                return width - x - 93
            }
            override fun getY(height: Int, y: Int): Int {
                return height - y - 35
            }
        },
        BOTTOM_MIDDLE{
            override fun getX(width: Int, x: Int): Int {
                return width/2 - x
            }
            override fun getY(height: Int, y: Int): Int {
                return height - y - 35
            }

            override fun validate(x: Int, y: Int, width: Int, height: Int): Boolean {
                if (x < 0 || y < 0) return false
                if (x > width/2) return false
                return y <= height - 35
            }
        };

        abstract fun getX(width: Int, x: Int): Int
        abstract fun getY(height: Int, y: Int): Int

        open fun validate(x: Int, y: Int, width: Int, height: Int): Boolean {
            if (x < 0 || y < 0) return false
            if (x > width - 93) return false
            return y <= height - 35
        }
    }
}