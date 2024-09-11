package xyz.mrcow.cowsOddWidgets.gui;

import net.minecraft.text.Text;

import java.util.ArrayList;

public class EntityExtraInfo {
    public ArrayList<Text> names = new ArrayList<>();
    public Text health;

    public boolean isEmpty()
    {
        return(
                namesEmpty() &&
                healthEmpty()
                );
    }

    public boolean namesEmpty()
    {
        return (names == null || names.isEmpty() || names.stream().allMatch(val -> val == null || val.getString().isEmpty()));
    }

    public boolean healthEmpty()
    {
        return (health == null || health.getString().isEmpty());
    }
}