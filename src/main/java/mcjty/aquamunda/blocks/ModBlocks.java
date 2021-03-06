package mcjty.aquamunda.blocks;

import mcjty.aquamunda.blocks.cooker.CookerBlock;
import mcjty.aquamunda.blocks.cuttingboard.CuttingBoardBlock;
import mcjty.aquamunda.blocks.grindstone.GrindStoneBlock;
import mcjty.aquamunda.blocks.hose.HoseBlock;
import mcjty.aquamunda.blocks.customblocks.BlockDeadCrop;
import mcjty.aquamunda.blocks.customblocks.CustomFarmLand;
import mcjty.aquamunda.blocks.desalination.DesalinationBoilerBlock;
import mcjty.aquamunda.blocks.desalination.DesalinationTankBlock;
import mcjty.aquamunda.blocks.sprinkler.SprinklerBlock;
import mcjty.aquamunda.blocks.tank.TankBlock;
import mcjty.aquamunda.fluid.BlockFreshWater;
import mcjty.aquamunda.fluid.FluidSetup;
import mcjty.aquamunda.varia.BlockReplacerHelper;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.ShapedOreRecipe;

public class ModBlocks {
    public static BlockFreshWater blockFreshWater;
    public static TankBlock tankBlock;
    public static HoseBlock hoseBlock;

    public static SprinklerBlock sprinklerBlock;
    public static DesalinationBoilerBlock desalinationBoilerBlock;
    public static DesalinationTankBlock desalinationTankBlock;
    public static CookerBlock cookerBlock;
    public static CuttingBoardBlock cuttingBoardBlock;
    public static GrindStoneBlock grindStoneBlock;

    public static CustomFarmLand customFarmLand;
    public static BlockDeadCrop deadCarrot;
    public static BlockDeadCrop deadWheat;

    public static void init() {
        blockFreshWater = new BlockFreshWater(FluidSetup.freshWater, Material.WATER);
        tankBlock = new TankBlock();
        hoseBlock = new HoseBlock();
        sprinklerBlock = new SprinklerBlock();
        desalinationBoilerBlock = new DesalinationBoilerBlock();
        desalinationTankBlock = new DesalinationTankBlock();
        cookerBlock = new CookerBlock();
        cuttingBoardBlock = new CuttingBoardBlock();
        grindStoneBlock = new GrindStoneBlock();

        customFarmLand = new CustomFarmLand();
        GameRegistry.register(customFarmLand);
        GameRegistry.register(new ItemBlock(customFarmLand), customFarmLand.getRegistryName());
//        try {
//            GameRegistry.addSubstitutionAlias("minecraft:farmland", GameRegistry.Type.BLOCK, customFarmLand);
//            GameRegistry.addSubstitutionAlias("minecraft:farmland", GameRegistry.Type.ITEM, new CustomFarmLandItemBlock(customFarmLand));
//        } catch (ExistingSubstitutionException e) {
//            throw new RuntimeException(e);
//        }
//        BlockReplacerHelper.replaceBlock(Blocks.farmland, customFarmLand);

        System.out.println("Blocks.farmland = " + Blocks.FARMLAND);
        System.out.println("Blocks.farmland.getClass() = " + Blocks.FARMLAND.getClass());

        deadCarrot = new BlockDeadCrop("dead_carrot");
        deadWheat = new BlockDeadCrop("dead_wheat");
    }

    public static void postInit() {
        BlockReplacerHelper.replaceBlock(Blocks.FARMLAND, customFarmLand);
    }

    public static void initCrafting() {
        Block rock = ForgeRegistries.BLOCKS.getValue(new ResourceLocation("immcraft", "rock"));
        GameRegistry.addShapedRecipe(new ItemStack(tankBlock), "r r", "i i", "iii", 'i', Items.IRON_INGOT, 'r', rock);
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(hoseBlock, 8), "www", "ggg", "www", 'w', Blocks.WOOL, 'g', "dyeGreen"));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(sprinklerBlock), " i ", " g ", "www", 'w', Blocks.WOOL, 'g', "dyeGreen", 'i', hoseBlock));
        GameRegistry.addShapedRecipe(new ItemStack(desalinationTankBlock), " g ", "g g", "iii", 'i', Items.IRON_INGOT, 'g', Blocks.GLASS);
        GameRegistry.addShapedRecipe(new ItemStack(desalinationBoilerBlock), "iii", "i i", "sgs", 'i', Items.IRON_INGOT, 'g', rock, 's', Blocks.STONE);
        GameRegistry.addShapedRecipe(new ItemStack(cookerBlock), "r r", "i i", " i ", 'i', Items.IRON_INGOT, 'r', rock);
        GameRegistry.addShapedRecipe(new ItemStack(cuttingBoardBlock), "   ", "sss", "www", 's', Items.STICK, 'w', Blocks.PLANKS);
        GameRegistry.addShapedRecipe(new ItemStack(grindStoneBlock), "  s", "ccc", "ccc", 's', Items.STICK, 'c', Blocks.STONE);
    }

    @SideOnly(Side.CLIENT)
    public static void initModels() {
        blockFreshWater.initModel();
        tankBlock.initModel();
        hoseBlock.initModel();
        sprinklerBlock.initModel();
        desalinationBoilerBlock.initModel();
        desalinationTankBlock.initModel();
        customFarmLand.initModel();
        cookerBlock.initModel();
        cuttingBoardBlock.initModel();
        grindStoneBlock.initModel();

        deadCarrot.initModel();
        deadWheat.initModel();
    }

    @SideOnly(Side.CLIENT)
    public static void initItemModels() {
        ModBlocks.tankBlock.initItemModel();
    }
}
