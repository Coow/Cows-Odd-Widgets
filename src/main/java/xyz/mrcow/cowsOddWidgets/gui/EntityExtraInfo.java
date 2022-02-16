package xyz.mrcow.cowsOddWidgets.gui;

import net.minecraft.text.Text;

import java.util.ArrayList;

public class EntityExtraInfo {
    public ArrayList<Text> names;
    public Text health;

    public boolean isEmpty()
    {
        return(
                (namesEmpty()) &&
                health.getString().isEmpty()
                );
    }

    public boolean namesEmpty()
    {
        return (names == null || names.isEmpty() || names.stream().allMatch(val -> val == null || val.getString().isEmpty()));
    }
}