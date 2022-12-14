package nokocraft118.nokocraft118.fireworks;

import com.google.common.collect.Lists;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.FireworkRocketEntity;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import nokocraft118.nokocraft118.setup.Registration;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class StrworkItem extends Item {
    public static final String TAG_FIREWORKS = "Fireworks";
    public static final String TAG_EXPLOSION = "Explosion";
    public static final String TAG_EXPLOSIONS = "Explosions";
    public static final String TAG_FLIGHT = "Flight";
    public static final String TAG_EXPLOSION_TYPE = "Type";
    public static final String TAG_EXPLOSION_TRAIL = "Trail";
    public static final String TAG_EXPLOSION_FLICKER = "Flicker";
    public static final String TAG_EXPLOSION_COLORS = "Colors";
    public static final String TAG_EXPLOSION_FADECOLORS = "FadeColors";
    public static final double ROCKET_PLACEMENT_OFFSET = 0.15D;

    public StrworkItem(Item.Properties properties) {
        super(properties);
    }

    //useOnでアイテムを手に持って右クリックしたときの動作を指定できる
    //この部分をコマンドに変えるとコマンド実行アイテムを作れるし，インベントリが出るようにすればバックパックmod風になる
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        if(!level.isClientSide) {
            ItemStack itemstack = context.getItemInHand();
            Vec3 vec3 = context.getClickLocation();
            Direction direction = context.getClickedFace();
            StrworkEntity strworkentity = new StrworkEntity(level, vec3.x + (double) direction.getStepX() * 0.15D, vec3.y + (double) direction.getStepY() * 0.15D, vec3.z + (double) direction.getStepZ() * 0.15D, itemstack);
            level.addFreshEntity(strworkentity);
            itemstack.shrink(1);
        }

        return InteractionResult.sidedSuccess(level.isClientSide);
    }


    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        if(player.isFallFlying()) {
            ItemStack itemstack = player.getItemInHand(hand);
            if(!world.isClientSide) {
                StrworkEntity strworkentity = new StrworkEntity(world, itemstack, player);
                world.addFreshEntity(strworkentity);
                if(!player.getAbilities().instabuild) {
                    itemstack.shrink(1);
                }

                player.awardStat(Stats.ITEM_USED.get(this));
            }

            return InteractionResultHolder.sidedSuccess(player.getItemInHand(hand), world.isClientSide());
        } else {
            return InteractionResultHolder.pass(player.getItemInHand(hand));
        }
    }

    //カーソルを合わせたときのコメントを設定できる
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltip, TooltipFlag pFlag) {
        CompoundTag compoundtag = pStack.getTagElement("Fireworks");
        if(compoundtag != null) {
            if(compoundtag.contains("Flight", 99)) {
                pTooltip.add((new TranslatableComponent("item.minecraft.firework_rocket.flight")).append(" ").append(String.valueOf((int) compoundtag.getByte("Flight"))).withStyle(ChatFormatting.GRAY));
            }

            ListTag listtag = compoundtag.getList("Explosions", 10);
            if(!listtag.isEmpty()) {
                for(int i = 0; i < listtag.size(); ++i) {
                    CompoundTag compoundtag1 = listtag.getCompound(i);
                    List<Component> list = Lists.newArrayList();
                    FireworkStarItem.appendHoverText(compoundtag1, list);
                    if(!list.isEmpty()) {
                        for(int j = 1; j < list.size(); ++j) {
                            list.set(j, (new TextComponent("  ")).append(list.get(j)).withStyle(ChatFormatting.GRAY));
                        }

                        pTooltip.addAll(list);
                    }
                }
            }

        }
    }

    public ItemStack getDefaultInstance() {
        ItemStack itemstack = new ItemStack(this);
        itemstack.getOrCreateTag().putByte("Flight", (byte) 1);
        return itemstack;
    }

    public static enum Shape {
        //TUAの三種類の花火を追加
        T_STR(0, "t letter"),
        U_STR(1, "u letter"),
        A_STR(2, "a letter");

        private static final StrworkItem.Shape[] BY_ID = Arrays.stream(values()).sorted(Comparator.comparingInt((shape) -> {
            return shape.id;
        })).toArray((i) -> {
            return new StrworkItem.Shape[i];
        });
        private final int id;
        private final String name;

        private Shape(int id, String name) {
            this.id = id;
            this.name = name;
        }

        public int getId() {
            return this.id;
        }

        public String getName() {
            return this.name;
        }

        //名前からでも番号からでも花火の種類を選べる
        public static StrworkItem.Shape byId(int pIndex) {
            return pIndex >= 0 && pIndex < BY_ID.length ? BY_ID[pIndex] : T_STR;
        }
    }
}
