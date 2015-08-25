package com.demigodsrpg.demigods.greek.item;

import com.demigodsrpg.demigods.engine.item.DivineItem;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;

import java.util.Set;

public abstract class GreekItem implements DivineItem {
    private final String name;
    private final String description;
    private final Set<Flag> flags;
    private final Category category;
    private final ItemStack item;
    private final Recipe recipe;
    private final Listener listener;

    public GreekItem(String name, String description, Set<Flag> flags, Category category, ItemStack item, Recipe recipe, Listener listener) {
        this.name = name;
        this.description = description;
        this.flags = flags;
        this.category = category;
        this.item = item;
        this.recipe = recipe;
        this.listener = listener;
    }

    @Override
    public String toString() {
        return name;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Set<Flag> getFlags() {
        return flags;
    }

    public Category getCategory() {
        return category;
    }

    public ItemStack getItem() {
        return item;
    }

    public Recipe getRecipe() {
        return recipe;
    }

    public Listener getUniqueListener() {
        return listener;
    }
}
