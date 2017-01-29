package sidben.pogostick.main;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemModelMesher;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Achievement;
import net.minecraft.stats.StatBase;
import net.minecraft.stats.StatBasic;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.common.AchievementPage;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.ShapedOreRecipe;
import sidben.pogostick.capability.CapabilityPogostick;
import sidben.pogostick.enchantment.EnchantmentSpring;
import sidben.pogostick.item.ItemPogoStick;
import sidben.pogostick.reference.Reference;


/**
 * Handles blocks, items, commands and other features from this mod.
 */
public class Features
{


    // -----------------------------------------------------------------------
    // Items
    // -----------------------------------------------------------------------

    public static class Items
    {
        public static final ItemPogoStick POGOSTICK = new ItemPogoStick();
    }


    public static void registerItems()
    {
        GameRegistry.register(Items.POGOSTICK);
    }

    @SideOnly(Side.CLIENT)
    public static void registerItemModels()
    {
        final ItemModelMesher itemMesher = Minecraft.getMinecraft().getRenderItem().getItemModelMesher();

        itemMesher.register(Items.POGOSTICK, 0, new ModelResourceLocation("pogostick:pogo_stick", "inventory"));
    }



    // -----------------------------------------------------------------------
    // Blocks
    // -----------------------------------------------------------------------



    // -----------------------------------------------------------------------
    // Recipes
    // -----------------------------------------------------------------------

    private static String OREDIC_STICK = "stickWood";
    // private static String OREDIC_IRON_BLOCK = "blockIron";


    public static void registerRecipes()
    {
        final ItemStack slimeBlocks = new ItemStack(Blocks.SLIME_BLOCK);
        final ItemStack pistons = new ItemStack(Blocks.PISTON);

        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(Items.POGOSTICK, 1), "ttt", " p ", " s ", 't', OREDIC_STICK, 'p', pistons, 's', slimeBlocks));
    }



    // -----------------------------------------------------------------------
    // Achievements and Stats
    // -----------------------------------------------------------------------

    public static class Stats
    {
        public static final StatBase TIMES_BOUNCED         = new StatBasic("stat.times_bounced", new TextComponentTranslation(Reference.ModID + ":stat.times_bounced", new Object[0]));
        public static final StatBase BOUNCE_ONE_CM         = new StatBasic("stat.distance_bounced", new TextComponentTranslation(Reference.ModID + ":stat.distance_bounced", new Object[0]),
                StatBase.distanceStatType);

        public static Achievement    USE_POGOSTICK         = new Achievement(Reference.ModID + ":achievement.use_pogostick", "use_pogostick", 1, 0, Features.Items.POGOSTICK, null);
        public static Achievement    BIG_FALL              = new Achievement(Reference.ModID + ":achievement.big_fall", "big_fall", 4, -1, net.minecraft.init.Items.IRON_BOOTS, Stats.USE_POGOSTICK);
        public static Achievement    BOUNCE_ON_WATER       = new Achievement(Reference.ModID + ":achievement.bounce_on_water", "bounce_on_water", -2, 0, Blocks.ICE, Stats.USE_POGOSTICK).setSpecial();
        public static Achievement    BREAK_POGOSTICK       = new Achievement(Reference.ModID + ":achievement.break_pogostick", "break_pogostick", 4, -3, Blocks.BARRIER, Stats.USE_POGOSTICK);
        public static Achievement    KILL_MONSTER          = new Achievement(Reference.ModID + ":achievement.kill_monster", "kill_monster", 1, 2, net.minecraft.init.Items.IRON_SHOVEL,
                Stats.USE_POGOSTICK);
        public static Achievement    KILL_BOUNCING_MONSTER = new Achievement(Reference.ModID + ":achievement.kill_bouncing_monster", "kill_bouncing_monster", 3, 3,
                net.minecraft.init.Items.ROTTEN_FLESH, Stats.KILL_MONSTER).setSpecial();
    }

    public static class StatsParams
    {
        public static float DISTANCE_FOR_BIG_FALL = 50.0F;
    }


    public static void registerStats()
    {
        Stats.TIMES_BOUNCED.registerStat();
        Stats.BOUNCE_ONE_CM.registerStat();
        /** See {@link net.minecraft.entity.player#addMovementStat(double x, double y, double z)} */

        final AchievementPage feats = new AchievementPage(Reference.ModName, Stats.BIG_FALL, Stats.BOUNCE_ON_WATER, Stats.BREAK_POGOSTICK, Stats.USE_POGOSTICK, Stats.KILL_BOUNCING_MONSTER,
                Stats.KILL_MONSTER);
        AchievementPage.registerAchievementPage(feats);
    }



    // -----------------------------------------------------------------------
    // Capabilities
    // -----------------------------------------------------------------------

    public static void registerCapabilities()
    {
        CapabilityPogostick.register();
    }



    // -----------------------------------------------------------------------
    // Commands
    // -----------------------------------------------------------------------



    // -----------------------------------------------------------------------
    // Enchantments
    // -----------------------------------------------------------------------

    public static class Enchantments
    {
        public static final Enchantment SPRING = new EnchantmentSpring(Enchantment.Rarity.COMMON, new EntityEquipmentSlot[] { EntityEquipmentSlot.MAINHAND, EntityEquipmentSlot.OFFHAND });
    }


    public static void registerEnchantments()
    {
        GameRegistry.register(Enchantments.SPRING);
    }


}
