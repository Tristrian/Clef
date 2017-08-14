package me.ichun.mods.clef.common.block;

import me.ichun.mods.clef.client.gui.GuiPlayTrackBlock;
import me.ichun.mods.clef.common.Clef;
import me.ichun.mods.clef.common.tileentity.TileEntityInstrumentPlayer;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.network.internal.FMLNetworkHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

public class BlockInstrumentPlayer extends BlockContainer
{
    public BlockInstrumentPlayer()
    {
        super(Material.WOOD);
        this.setCreativeTab(Clef.creativeTabInstruments);
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta)
    {
        return new TileEntityInstrumentPlayer();
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, @Nullable ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        TileEntity te = worldIn.getTileEntity(pos);
        if(te instanceof TileEntityInstrumentPlayer)
        {
            TileEntityInstrumentPlayer player = (TileEntityInstrumentPlayer)te;
            boolean hasSlot = false;
            ItemStack is = playerIn.getHeldItemMainhand();
            if(is != null && is.getItem() == Clef.itemInstrument)
            {
                //Find a free slot
                for(int i = 0; i < player.getSizeInventory(); i++)
                {
                    ItemStack is1 = player.getStackInSlot(i);
                    if(is1 == null)
                    {
                        hasSlot = true;
                        worldIn.playSound(null, pos.getX() + 0.5D, pos.getY() + 1D, pos.getZ() + 0.5D, SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.PLAYERS, 0.2F, ((worldIn.rand.nextFloat() - worldIn.rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);
                        if(!worldIn.isRemote)
                        {
                            player.setInventorySlotContents(i, is);
                            player.markDirty();
                            playerIn.setHeldItem(EnumHand.MAIN_HAND, null);
                            playerIn.inventory.markDirty();
                            worldIn.notifyBlockUpdate(pos, state, state, 3);
                        }
                        break;
                    }
                }
            }
            if(!hasSlot)
            {
                FMLNetworkHandler.openGui(playerIn, Clef.instance, 0, worldIn, pos.getX(), pos.getY(), pos.getZ());
            }
            return true;
        }
        return false;
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
    {
        TileEntity tileentity = worldIn.getTileEntity(pos);

        if (tileentity instanceof TileEntityInstrumentPlayer)
        {
            InventoryHelper.dropInventoryItems(worldIn, pos, (TileEntityInstrumentPlayer)tileentity);
            worldIn.updateComparatorOutputLevel(pos, this);
        }

        super.breakBlock(worldIn, pos, state);
    }

    @Override
    public void dropBlockAsItemWithChance(World worldIn, BlockPos pos, IBlockState state, float chance, int fortune)
    {
        if (!worldIn.isRemote)
        {
            super.dropBlockAsItemWithChance(worldIn, pos, state, chance, 0);
        }
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state)
    {
        return EnumBlockRenderType.MODEL;
    }

    @SideOnly(Side.CLIENT)
    public void openGui(TileEntityInstrumentPlayer player)
    {
        FMLClientHandler.instance().displayGuiScreen(Minecraft.getMinecraft().thePlayer, new GuiPlayTrackBlock(player));
    }
}
