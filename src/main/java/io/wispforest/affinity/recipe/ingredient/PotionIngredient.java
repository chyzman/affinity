package io.wispforest.affinity.recipe.ingredient;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.wispforest.affinity.Affinity;
import io.wispforest.affinity.object.AffinityIngredients;
import net.fabricmc.fabric.api.recipe.v1.ingredient.CustomIngredient;
import net.fabricmc.fabric.api.recipe.v1.ingredient.CustomIngredientSerializer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionUtil;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

import java.util.List;

public class PotionIngredient implements CustomIngredient {

    public static final Codec<PotionIngredient> CODEC = RecordCodecBuilder.create(instance -> instance
            .group(Registries.POTION.getCodec().fieldOf("potion").forGetter(ingredient -> ingredient.requiredPotion))
            .apply(instance, PotionIngredient::new));

    private final Potion requiredPotion;

    public PotionIngredient(Potion requiredPotion) {
        this.requiredPotion = requiredPotion;
    }

    @Override
    public boolean test(ItemStack stack) {
        return (stack.isOf(Items.POTION) || stack.isOf(Items.SPLASH_POTION) || stack.isOf(Items.LINGERING_POTION)) && PotionUtil.getPotion(stack) == requiredPotion;
    }

    @Override
    public List<ItemStack> getMatchingStacks() {
        return List.of(
                PotionUtil.setPotion(Items.POTION.getDefaultStack(), this.requiredPotion),
                PotionUtil.setPotion(Items.SPLASH_POTION.getDefaultStack(), this.requiredPotion),
                PotionUtil.setPotion(Items.LINGERING_POTION.getDefaultStack(), this.requiredPotion)
        );
    }

    @Override
    public boolean requiresTesting() {
        return true;
    }

    @Override
    public CustomIngredientSerializer<?> getSerializer() {
        return AffinityIngredients.POTION;
    }

    public static final class Serializer implements CustomIngredientSerializer<PotionIngredient> {

        @Override
        public Identifier getIdentifier() {
            return Affinity.id("potion");
        }

        @Override
        public Codec<PotionIngredient> getCodec(boolean allowEmpty) {
            return PotionIngredient.CODEC;
        }

        @Override
        public PotionIngredient read(PacketByteBuf buf) {
            return new PotionIngredient(buf.readRegistryValue(Registries.POTION));
        }

        @Override
        public void write(PacketByteBuf buf, PotionIngredient ingredient) {
            buf.writeRegistryValue(Registries.POTION, ingredient.requiredPotion);
        }
    }
}
