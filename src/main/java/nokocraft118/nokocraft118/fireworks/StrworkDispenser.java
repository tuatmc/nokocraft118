package nokocraft118.nokocraft118.fireworks;

import net.minecraft.core.BlockSource;
import net.minecraft.core.Direction;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.core.dispenser.DispenseItemBehavior;
import net.minecraft.world.entity.projectile.FireworkRocketEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.DispenserBlock;
import nokocraft118.nokocraft118.setup.Registration;

public class StrworkDispenser {

    //TUAT花火をディスペンサーに入れたときの挙動を設定するクラス
    //ディスペンサーは右クリック代行器ではないのでアイテムの挙動をいちいち登録する必要がある
    //新しい液体を追加したり投射物を追加するときは気をつけよう
    public static void registerBehavior() {
        DispenserBlock.registerBehavior(Registration.STRWORK_ITEM.get(), new DefaultDispenseItemBehavior() {
            public ItemStack execute(BlockSource block, ItemStack item) {
                Direction direction = block.getBlockState().getValue(DispenserBlock.FACING);
                StrworkEntity fireworkrocketentity = new StrworkEntity(block.getLevel(), item, block.x(), block.y() + 20, block.z(), true);
                DispenseItemBehavior.setEntityPokingOutOfBlock(block, fireworkrocketentity, direction);
                fireworkrocketentity.shoot((double) direction.getStepX(), (double) direction.getStepY(), (double) direction.getStepZ(), 0.5F, 1.0F);
                block.getLevel().addFreshEntity(fireworkrocketentity);
                item.shrink(1);
                return item;
            }

            //ここの数字をいじると音を変えられたりする
            protected void playSound(BlockSource block) {
                block.getLevel().levelEvent(1004, block.getPos(), 0);
            }
        });

    }

}
