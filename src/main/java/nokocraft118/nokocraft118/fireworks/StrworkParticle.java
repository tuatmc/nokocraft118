package nokocraft118.nokocraft118.fireworks;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.Util;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.FireworkRocketItem;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.Random;


@OnlyIn(Dist.CLIENT)
public class StrworkParticle {

    //パーティクルが呼ばれたとき
    public void createStrworks(ClientLevel level, double pX, double pY, double pZ, double pMotionX, double pMotionY, double pMotionZ, @Nullable CompoundTag pCompound) {
        Minecraft.getInstance().particleEngine.add(new Starter(level, pX, pY, pZ, pMotionX, pMotionY, pMotionZ, Minecraft.getInstance().particleEngine, pCompound));
    }

    @OnlyIn(Dist.CLIENT)
    public static class FlashProvider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprite;

        public FlashProvider(SpriteSet pSprites) {
            this.sprite = pSprites;
        }

        public Particle createParticle(SimpleParticleType pType, ClientLevel pLevel, double pX, double pY, double pZ, double pXSpeed, double pYSpeed, double pZSpeed) {
            StrworkParticle.OverlayParticle fireworkparticles$overlayparticle = new StrworkParticle.OverlayParticle(pLevel, pX, pY, pZ);
            fireworkparticles$overlayparticle.pickSprite(this.sprite);
            return fireworkparticles$overlayparticle;
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static class OverlayParticle extends TextureSheetParticle {
        OverlayParticle(ClientLevel p_106677_, double p_106678_, double p_106679_, double p_106680_) {
            super(p_106677_, p_106678_, p_106679_, p_106680_);
            this.lifetime = 4;
        }

        public ParticleRenderType getRenderType() {
            return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
        }

        public void render(VertexConsumer pBuffer, Camera pRenderInfo, float pPartialTicks) {
            this.setAlpha(0.6F - ((float) this.age + pPartialTicks - 1.0F) * 0.25F * 0.5F);
            super.render(pBuffer, pRenderInfo, pPartialTicks);
        }

        public float getQuadSize(float pScaleFactor) {
            return 7.1F * Mth.sin(((float) this.age + pScaleFactor - 1.0F) * 0.25F * (float) Math.PI);
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static class SparkParticle extends SimpleAnimatedParticle {
        private boolean trail;
        private boolean flicker;
        private final ParticleEngine engine;
        private float fadeR;
        private float fadeG;
        private float fadeB;
        private boolean hasFade;

        SparkParticle(ClientLevel pLevel, double pX, double pY, double pZ, double pXSpeed, double pYSpeed, double pZSpeed, ParticleEngine pEngine, SpriteSet pSprites) {
            super(pLevel, pX, pY, pZ, pSprites, 0.1F);
            this.xd = pXSpeed;
            this.yd = pYSpeed;
            this.zd = pZSpeed;
            this.engine = pEngine;
            this.quadSize *= 0.75F;
            this.lifetime = 48 + this.random.nextInt(12);
            this.setSpriteFromAge(pSprites);
        }

        @Override
        protected void setAlpha(float p_107272_) {
            this.alpha = p_107272_;
        }

        public void setTrail(boolean pTrail) {
            this.trail = pTrail;
        }

        public void setFlicker(boolean pTwinkle) {
            this.flicker = pTwinkle;
        }

        public void render(VertexConsumer pBuffer, Camera pRenderInfo, float pPartialTicks) {
            if(!this.flicker || this.age < this.lifetime / 3 || (this.age + this.lifetime) / 3 % 2 == 0) {
                super.render(pBuffer, pRenderInfo, pPartialTicks);
            }

        }

        public void tick() {
            super.tick();
            if(this.trail && this.age < this.lifetime / 2 && (this.age + this.lifetime) % 2 == 0) {
                StrworkParticle.SparkParticle fireworkparticles$sparkparticle = new StrworkParticle.SparkParticle(this.level, this.x, this.y, this.z, 0.0D, 0.0D, 0.0D, this.engine, this.sprites);
                fireworkparticles$sparkparticle.setAlpha(0.99F);
                fireworkparticles$sparkparticle.setColor(this.rCol, this.gCol, this.bCol);
                fireworkparticles$sparkparticle.age = fireworkparticles$sparkparticle.lifetime / 2;
                if(this.hasFade) {
                    fireworkparticles$sparkparticle.hasFade = true;
                    fireworkparticles$sparkparticle.fadeR = this.fadeR;
                    fireworkparticles$sparkparticle.fadeG = this.fadeG;
                    fireworkparticles$sparkparticle.fadeB = this.fadeB;
                }

                fireworkparticles$sparkparticle.flicker = this.flicker;
                this.engine.add(fireworkparticles$sparkparticle);
            }

        }
    }

    @OnlyIn(Dist.CLIENT)
    public static class SparkProvider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprites;

        public SparkProvider(SpriteSet p_106733_) {
            this.sprites = p_106733_;
        }

        public Particle createParticle(SimpleParticleType p_106744_, ClientLevel p_106745_, double p_106746_, double p_106747_, double p_106748_, double p_106749_, double p_106750_, double p_106751_) {
            StrworkParticle.SparkParticle fireworkparticles$sparkparticle = new StrworkParticle.SparkParticle(p_106745_, p_106746_, p_106747_, p_106748_, p_106749_, p_106750_, p_106751_, Minecraft.getInstance().particleEngine, this.sprites);
            fireworkparticles$sparkparticle.setAlpha(0.99F);
            return fireworkparticles$sparkparticle;
        }
    }


    @OnlyIn(Dist.CLIENT)
    public static class Starter extends NoRenderParticle {
        private int life;
        private final ParticleEngine engine;
        private ListTag explosions;
        private boolean twinkleDelay;
        private boolean isRotate = false;
        private double size = 1;

        public Starter(ClientLevel pLevel, double pX, double pY, double pZ, double pXSpeed, double pYSpeed, double pZSpeed, ParticleEngine pEngine, @Nullable CompoundTag pTag) {
            super(pLevel, pX, pY, pZ);
            this.xd = pXSpeed;
            this.yd = pYSpeed;
            this.zd = pZSpeed;
            this.engine = pEngine;
            this.lifetime = 8;
            if(pTag != null) {
                this.explosions = pTag.getList("Explosions", 10);
                if(this.explosions.isEmpty()) {
                    this.explosions = null;
                } else {
                    this.lifetime = this.explosions.size() * 2 - 1;

                    for(int i = 0; i < this.explosions.size(); ++i) {
                        CompoundTag compoundtag = this.explosions.getCompound(i);
                        if(compoundtag.getBoolean("Flicker")) {
                            this.twinkleDelay = true;
                            this.lifetime += 15;
                            break;
                        }
                    }
                }
            }

        }

        public void tick() {
            if(this.life == 0 && this.explosions != null) {
                boolean flag = this.isFarAwayFromCamera();
                boolean flag1 = false;
                if(this.explosions.size() >= 3) {
                    flag1 = true;
                } else {
                    for(int i = 0; i < this.explosions.size(); ++i) {
                        CompoundTag compoundtag = this.explosions.getCompound(i);
                        if(StrworkItem.Shape.byId(compoundtag.getByte("Type")) == StrworkItem.Shape.T_STR) {
                            flag1 = true;
                            break;
                        }
                    }
                }

                SoundEvent soundevent1;
                if(flag1) {
                    soundevent1 = flag ? SoundEvents.FIREWORK_ROCKET_LARGE_BLAST_FAR : SoundEvents.FIREWORK_ROCKET_LARGE_BLAST;
                } else {
                    soundevent1 = flag ? SoundEvents.FIREWORK_ROCKET_BLAST_FAR : SoundEvents.FIREWORK_ROCKET_BLAST;
                }

                this.level.playLocalSound(this.x, this.y, this.z, soundevent1, SoundSource.AMBIENT, 20.0F, 0.95F + this.random.nextFloat() * 0.1F, true);
            }

            if(this.life % 2 == 0 && this.explosions != null && this.life / 2 < this.explosions.size()) {
                int k = this.life / 2;
                CompoundTag compoundtag1 = this.explosions.getCompound(k);
                StrworkItem.Shape fireworkrocketitem$shape = StrworkItem.Shape.byId(compoundtag1.getByte("Type"));
                boolean flag4 = compoundtag1.getBoolean("Trail");
                boolean flag2 = compoundtag1.getBoolean("Flicker");
                String color = compoundtag1.getString("Color");
                isRotate = compoundtag1.getBoolean("isRotate");
                size = compoundtag1.getDouble("Size");
                //colorタグで色を付けられる，デフォルトは赤
                //カラーコードを入力してその色にすることもできるけど今回は不要なので実装していない
                int cint = DyeColor.RED.getFireworkColor();
                switch(color) {
                    case "BLUE":
                        cint = DyeColor.BLUE.getFireworkColor();
                        break;
                    case "GREEN":
                        cint = DyeColor.GREEN.getFireworkColor();
                        break;
                    case "BLACK":
                        cint = DyeColor.BLACK.getFireworkColor();
                        break;
                    case "WHITE":
                        cint = DyeColor.WHITE.getFireworkColor();
                        break;
                }


                //ここが花火の形状を作ってる部分
                //結構原始的な方法で形状を作ってる
                switch(fireworkrocketitem$shape) {
                    case T_STR:
                        this.createParticleShape(0.5D, new double[][]{{0.0D, 1.2D}, {1.2D, 1.2D}, {1.2D, 0.6D}, {0.3D, 0.6D}, {0.3D, -1.2D}, {0.0D, -1.2D}}, new int[]{cint}, new int[]{cint}, flag4, flag2, true);
                        break;
                    case U_STR:
                        this.createParticleShape(0.5D, new double[][]{{0.0D, -1.2D}, {0.8D, -1.2D}, {1.2D, -0.8D}, {1.2D, 1.2D}, {0.6D, 1.2D}, {0.6D, -0.6D}, {0.0D, -0.6D}}, new int[]{cint}, new int[]{cint}, flag4, flag2, true);
                        break;
                    case A_STR:
                        this.createParticleShape(0.5D, new double[][]{{0.0D, 1.2D}, {0.8D, 1.2D}, {1.2D, 0.8D}, {1.2D, -1.2D}, {0.6D, -1.2D}, {0.6D, -0.6D}, {0.0D, -0.6D}, {99.0D, 0.0D}, {0.0D, 0.0D}, {0.6D, 0.0D}, {0.6D, 0.6D}, {0.0D, 0.6D}}, new int[]{cint}, new int[]{cint}, flag4, flag2, true);
                }

                int j = cint;
                float f = (float) ((j & 16711680) >> 16) / 255.0F;
                float f1 = (float) ((j & '\uff00') >> 8) / 255.0F;
                float f2 = (float) ((j & 255) >> 0) / 255.0F;
                Particle particle = this.engine.createParticle(TuatParticleTypes.FLASH.get(), this.x, this.y, this.z, 0.0D, 0.0D, 0.0D);
                particle.setColor(f, f1, f2);
            }

            ++this.life;
            if(this.life > this.lifetime) {
                if(this.twinkleDelay) {
                    boolean flag3 = this.isFarAwayFromCamera();
                    SoundEvent soundevent = flag3 ? SoundEvents.FIREWORK_ROCKET_TWINKLE_FAR : SoundEvents.FIREWORK_ROCKET_TWINKLE;
                    this.level.playLocalSound(this.x, this.y, this.z, soundevent, SoundSource.AMBIENT, 20.0F, 0.9F + this.random.nextFloat() * 0.15F, true);
                }

                this.remove();
            }

        }

        private boolean isFarAwayFromCamera() {
            Minecraft minecraft = Minecraft.getInstance();
            return minecraft.gameRenderer.getMainCamera().getPosition().distanceToSqr(this.x, this.y, this.z) >= 256.0D;
        }


        //一個パーティクルをポンと呼ぶだけ
        private void createParticle(double pX, double pY, double pZ, double pMotionX, double pMotionY, double pMotionZ, int[] pSparkColors, int[] pSparkColorFades, boolean pHasTrail, boolean pHasTwinkle) {

            StrworkParticle.SparkParticle fireworkparticles$sparkparticle = (SparkParticle) this.engine.createParticle(TuatParticleTypes.SPARK.get(), pX, pY, pZ, pMotionX, pMotionY, pMotionZ);
            fireworkparticles$sparkparticle.setTrail(pHasTrail);
            fireworkparticles$sparkparticle.setFlicker(pHasTwinkle);
            int i = this.random.nextInt(pSparkColors.length);
            fireworkparticles$sparkparticle.setColor(pSparkColors[i]);
            if(pSparkColorFades.length > 0) {
                fireworkparticles$sparkparticle.setFadeColor(Util.getRandom(pSparkColorFades, this.random));
            }
        }


        private void createParticleShape(double pSpeed, double[][] pShape, int[] pColours, int[] pFadeColours, boolean pTrail, boolean pTwinkle, boolean pCreeper) {

            double x0 = size * pShape[0][0];
            double y0 = size * pShape[0][1];
            this.createParticle(this.x, this.y, this.z, x0 * pSpeed, y0 * pSpeed, 0.0D, pColours, pFadeColours, pTrail, pTwinkle);
            float f = this.random.nextFloat() * (float) Math.PI;
            double d2 = pCreeper ? 0.034D : 0.34D;

            Random rand = new Random();


            for(int i = 0; i < 3; ++i) {
                double d3 = (double) f + (double) ((float) i * (float) Math.PI) * d2;
                //d3によって向きが制御されている
                d3 = isRotate ? ((Math.PI) / 2) : 0.0f;
                double x0_copy = x0;
                double y0_copy = y0;

                for(int j = 1; j < pShape.length; ++j) {
                    double xlist = size * pShape[j][0];
                    double ylist = size * pShape[j][1];


                    for(double t = 0.25D; t <= 1.0D; t += 0.01D) {
                        if(xlist == -99.0 || x0_copy == -99.0) break;
                        double d9 = Mth.lerp(t, x0_copy, xlist) * pSpeed;
                        double d10 = Mth.lerp(t, y0_copy, ylist) * pSpeed;
                        double d11 = d9 * Math.sin(d3);
                        d9 *= Math.cos(d3);

                        for(double q = -1.0D; q <= 1.0D; q += 2.0D) {
                            this.createParticle(this.x + (1 - 2 * rand.nextDouble()) * 0.5, this.y + (1 - 2 * rand.nextDouble()) * 0.5, this.z + (1 - 2 * rand.nextDouble()) * 0.5, d9 * q, d10, d11 * q, pColours, pFadeColours, pTrail, pTwinkle);
                        }
                    }

                    x0_copy = xlist;
                    y0_copy = ylist;
                }
            }
        }
    }
}
