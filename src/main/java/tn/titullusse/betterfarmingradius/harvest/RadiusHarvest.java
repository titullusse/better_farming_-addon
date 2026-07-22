package tn.titullusse.betterfarmingradius.harvest;

import java.util.Comparator;
import java.util.List;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

/**
 * Re-implementation of the base mod's {@code FarmingCrateOnTickUpdateProcedure}
 * harvest logic, with the previously hard-coded radius made configurable.
 * <p>
 * Behaviour matches the original: fully grown {@link CropBlock}s in range are
 * harvested (their drops are collected), then reset to age 0, and any resulting
 * item entities near the crate are pulled into the crate's inventory.
 */
public final class RadiusHarvest {

	private RadiusHarvest() {
	}

	public static void execute(LevelAccessor world, double x, double y, double z, int radius) {
		int r = Math.max(0, radius);
		for (int sx = -r; sx <= r; sx++) {
			for (int sz = -r; sz <= r; sz++) {
				BlockPos cropPos = BlockPos.containing(x + sx, y, z + sz);
				BlockState cropState = world.getBlockState(cropPos);
				if (!(cropState.getBlock() instanceof CropBlock)) {
					continue;
				}
				if (!(cropState.getBlock().getStateDefinition().getProperty("age") instanceof IntegerProperty ageProp)) {
					continue;
				}
				int current = cropState.getValue(ageProp);
				int max = ageProp.getPossibleValues().stream().max(Integer::compareTo).orElse(-1);
				if (current != max) {
					continue;
				}

				// Drop the crop's resources (base mod trick: copy the crop above, drop, destroy).
				BlockPos abovePos = BlockPos.containing(x + sx, y + 1, z + sz);
				world.setBlock(abovePos, cropState, 3);
				Block.dropResources(world.getBlockState(abovePos), world, abovePos, null);
				world.destroyBlock(abovePos, false);

				// Reset the harvested crop back to age 0.
				world.setBlock(cropPos, cropState.setValue(ageProp, 0), 3);

				collectDropsIntoCrate(world, x, y, z, r);
			}
		}
	}

	private static void collectDropsIntoCrate(LevelAccessor world, double x, double y, double z, int radius) {
		// Center the pickup box on the crate block's center, and reach one block
		// past the harvest radius so drops at the far edge (which spawn around
		// edge + 0.5) and any that bounce outward are still collected.
		final Vec3 center = new Vec3(x + 0.5, y + 0.5, z + 0.5);
		double reach = radius + 1;
		List<Entity> entities = world
				.getEntitiesOfClass(Entity.class, new AABB(center, center).inflate(reach), e -> true).stream()
				.sorted(Comparator.comparingDouble(e -> e.distanceToSqr(center))).toList();

		BlockPos cratePos = new BlockPos((int) x, (int) y, (int) z);
		BlockEntity blockEntity = world.getBlockEntity(cratePos);
		if (!(blockEntity instanceof BaseContainerBlockEntity crate)) {
			return;
		}

		for (Entity entity : entities) {
			if (!(entity instanceof ItemEntity itemEntity)) {
				continue;
			}
			ItemStack stackToInsert = itemEntity.getItem().copy();
			int insertCount = stackToInsert.getCount();
			if (insertCount <= 0) {
				continue;
			}
			if (!entity.level().isClientSide()) {
				entity.discard();
			}

			int size = crate.getContainerSize();
			for (int slot = 0; slot < size && insertCount > 0; slot++) {
				ItemStack slotStack = crate.getItem(slot);
				if (slotStack.isEmpty()) {
					int amount = Math.min(insertCount, stackToInsert.getMaxStackSize());
					ItemStack toInsert = stackToInsert.copy();
					toInsert.setCount(amount);
					crate.setItem(slot, toInsert);
					insertCount -= amount;
					continue;
				}
				if (ItemStack.isSameItemSameComponents(slotStack, stackToInsert)
						&& slotStack.getCount() < slotStack.getMaxStackSize()) {
					int room = slotStack.getMaxStackSize() - slotStack.getCount();
					int amountToAdd = Math.min(room, insertCount);
					slotStack.grow(amountToAdd);
					insertCount -= amountToAdd;
				}
			}

			crate.setChanged();
			if (world instanceof Level level) {
				level.sendBlockUpdated(cratePos, world.getBlockState(cratePos), world.getBlockState(cratePos), 3);
			}
		}
	}
}
