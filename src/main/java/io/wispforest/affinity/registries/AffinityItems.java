package io.wispforest.affinity.registries;

import io.wispforest.affinity.item.AetherWandItem;
import io.wispforest.owo.registration.reflect.ItemRegistryContainer;
import net.minecraft.item.Item;

public class AffinityItems implements ItemRegistryContainer {

    //    public static final Item SUNDIAL = new Item(new Item.Settings().maxCount(1).group(Affinity.AFFINITY_GROUP));
    public static final Item AETHER_WAND = new AetherWandItem();

}
