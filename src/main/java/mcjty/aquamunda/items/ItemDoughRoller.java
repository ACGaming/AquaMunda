package mcjty.aquamunda.items;

import mcjty.aquamunda.AquaMunda;
import mcjty.lib.compat.CompatItem;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemDoughRoller extends CompatItem {

    public ItemDoughRoller() {
        super();
        setMaxStackSize(1);
        setRegistryName("dough_roller");
        setUnlocalizedName(AquaMunda.MODID + ".dough_roller");
        setCreativeTab(AquaMunda.creativeTab);
        GameRegistry.register(this);
    }

    @SideOnly(Side.CLIENT)
    public void initModel() {
        ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(getRegistryName(), "inventory"));
    }
}
