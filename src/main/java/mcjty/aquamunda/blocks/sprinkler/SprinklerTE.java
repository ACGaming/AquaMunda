package mcjty.aquamunda.blocks.sprinkler;

import mcjty.aquamunda.blocks.generic.GenericTE;
import mcjty.aquamunda.cables.CableSubType;
import mcjty.aquamunda.chunkdata.GameData;
import mcjty.aquamunda.environment.EnvironmentData;
import mcjty.aquamunda.fluid.FluidSetup;
import mcjty.aquamunda.hosemultiblock.IHoseConnector;
import mcjty.aquamunda.varia.NBTHelper;
import mcjty.aquamunda.varia.Vector;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ITickable;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fluids.Fluid;

import java.util.EnumSet;
import java.util.Random;
import java.util.Set;

public class SprinklerTE extends GenericTE implements IHoseConnector, ITickable {

    public static final int MAX_MOISTNESS = 5;
    public static final int SPRINKLER_COUNTER = 10;
    public static final int INPUT_PER_TICK = 25;
    public static final int MAX_AMOUNT = SPRINKLER_COUNTER * INPUT_PER_TICK;

    public int counter = SPRINKLER_COUNTER;
    public int amount = 0;

    private Set<EnumFacing> connections = EnumSet.noneOf(EnumFacing.class);

    @Override
    public boolean canConnect(EnumFacing blockSide) {
        if (blockSide == EnumFacing.UP) {
            return false;
        }
        return !connections.contains(blockSide);
    }

    @Override
    public int connect(EnumFacing blockSide, int networkId, CableSubType subType) {
        markDirty();
        if (!connections.contains(blockSide)) {
            connections.add(blockSide);
            return blockSide.ordinal() * 4;
        }
        return -1;
    }

    @Override
    public Vector getConnectorLocation(int connectorId, EnumFacing rotation) {
        EnumFacing side = EnumFacing.values()[connectorId >> 2];
        int xCoord = getPos().getX();
        int yCoord = getPos().getY();
        int zCoord = getPos().getZ();
        switch (side) {
            case DOWN:
                return new Vector(xCoord+.5f, yCoord, zCoord+.5f);
            case UP:
                return new Vector(xCoord+.5f, yCoord+1, zCoord+.5f);
            case NORTH:
                return new Vector(xCoord+.5f, yCoord+.1f, zCoord);
            case SOUTH:
                return new Vector(xCoord+.5f, yCoord+.1f, zCoord+1);
            case WEST:
                return new Vector(xCoord, yCoord+.1f, zCoord+.5f);
            case EAST:
                return new Vector(xCoord+1, yCoord+.1f, zCoord+.5f);
            default:
                return new Vector(xCoord, yCoord, zCoord);
        }
    }

    @Override
    public void disconnect(int connectorId) {
        EnumFacing side = EnumFacing.values()[connectorId >> 2];
        connections.remove(side);
        markDirty();;
    }

    @Override
    public int extract(int amount) {
        return 0;
    }

    @Override
    public Fluid getSupportedFluid() {
        return FluidSetup.freshWater;
    }

    @Override
    public int getMaxExtractPerTick() {
        return 0;
    }

    @Override
    public int getMaxInsertPerTick() {
        return INPUT_PER_TICK;
    }

    @Override
    public int getEmptyLiquidLeft() {
        return MAX_AMOUNT - amount;
    }

    @Override
    public int insert(Fluid fluid, int a) {
        int inserted = Math.min(MAX_AMOUNT - amount, a);
        amount += inserted;
        return inserted;
    }

    @Override
    public float getFilledPercentage() {
        return 0;
    }

    @Override
    public void update() {
        if (!worldObj.isRemote) {
            counter--;
            if (counter > 0) {
                return;
            }
            counter = SPRINKLER_COUNTER;
            // Only sprinkle if we have enough water
            if (amount >= MAX_AMOUNT) {
                sprinkle();
                amount = 0;
            }
        }
    }

    private void sprinkle() {
        EnvironmentData environment = EnvironmentData.getEnvironmentData(worldObj);
        GameData data = environment.getData();
        boolean dirty = false;
        int xCoord = getPos().getX();
        int yCoord = getPos().getY();
        int zCoord = getPos().getZ();
        for (int x = xCoord-4 ; x <= xCoord+4; x++) {
            for (int y = yCoord-1 ; y <= yCoord+2; y++) {
                for (int z = zCoord-4 ; z <= zCoord+4; z++) {
                    BlockPos pos = new BlockPos(x, y, z);
                    Block block = worldObj.getBlockState(pos).getBlock();
                    if (!worldObj.isAirBlock(pos)) {
                        spawnParticles(EnumParticleTypes.WATER_SPLASH, 10, x, y+1, z);
                        // splash, dripWater
                    }

                    if (block == Blocks.farmland) {
                        byte moistness = data.get(worldObj.provider.getDimensionId(), pos);
                        if (moistness < MAX_MOISTNESS) {
                            moistness++;
                            data.set(worldObj.provider.getDimensionId(), pos, moistness);
                            dirty = true;
                        }
                    }
                }
            }
        }
        if (dirty) {
            environment.save(worldObj);
        }
    }

    private static Random random = new Random();

    private void spawnParticles(EnumParticleTypes type, int amount, int x, int y, int z) {
        if (amount <= 0) {
            return;
        }
        float vecX = (random.nextFloat() - 0.5F) * 1.0F;
        float vecY = (random.nextFloat()) * 1.0F;
        float vecZ = (random.nextFloat() - 0.5F) * 1.0F;
        ((WorldServer) worldObj).spawnParticle(type, x + 0.5f, y + 0.5f, z + 0.5f, amount, vecX, vecY, vecZ, 0.3f);
    }


    @Override
    public void readFromNBT(NBTTagCompound tagCompound) {
        super.readFromNBT(tagCompound);
        amount = tagCompound.getInteger("amount");
        connections.clear();
        for (EnumFacing direction : EnumFacing.VALUES) {
            if (tagCompound.hasKey("c" + direction.ordinal())) {
                connections.add(direction);
            }
        }
    }

    @Override
    public void writeToNBT(NBTHelper helper) {
        super.writeToNBT(helper);
        helper.set("amount", amount);
        for (EnumFacing direction : EnumFacing.VALUES) {
            if (connections.contains(direction)) {
                helper.set("c" + direction.ordinal(), true);
            }
        }
    }
}