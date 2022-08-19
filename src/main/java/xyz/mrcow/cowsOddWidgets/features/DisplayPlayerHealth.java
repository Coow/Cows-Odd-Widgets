package xyz.mrcow.cowsOddWidgets.features;

import net.minecraft.entity.LivingEntity;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import xyz.mrcow.cowsOddWidgets.config.Configs;

public class DisplayPlayerHealth {
    public static Text addHealthText(LivingEntity entity, Text nametag)
    {
        //if(Configs.Settings.DISPLAY_PLAYER_HEALTH.getBooleanValue() == false)
        //    return nametag;

        int health = (int)entity.getHealth();

        MutableText formattedHealth = Text.literal(" ")
                .append(Integer.toString(health)).formatted(getColor(health));
        return ((MutableText)nametag).append(formattedHealth);
    }

    private static Formatting getColor(int health)
    {
        if(health <= 5)
            return Formatting.DARK_RED;

        if(health <= 10)
            return Formatting.GOLD;

        if(health <= 15)
            return Formatting.YELLOW;

        return Formatting.GREEN;
    }
}
