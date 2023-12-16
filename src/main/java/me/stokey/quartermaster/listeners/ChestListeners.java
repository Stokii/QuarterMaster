package me.stokey.quartermaster.listeners;

import me.stokey.quartermaster.utils.LockUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class ChestListeners implements Listener {
    @EventHandler
    public void openChestListener(PlayerInteractEvent e){
        if(e.getAction().equals(Action.RIGHT_CLICK_BLOCK)){

            Block b = e.getClickedBlock();

            if(b.getType().equals(Material.CHEST)){
                //Megvizsgálni, hogy zárva van e a láda
                if (LockUtils.isCurrentlyLocked(b)){
                    if (LockUtils.getWhoLocked(b) == e.getPlayer()){

                        e.getPlayer().sendMessage("You owned this chest");

                    } else if (!(LockUtils.getWhoLocked(b) == e.getPlayer())) {

                        e.setCancelled(true);
                        Bukkit.broadcastMessage("NONONO");

                    }
                }
            }
        }
    }

    @EventHandler
    public void breakChestListener(BlockBreakEvent event){
        if (event.getBlock().getType().equals(Material.CHEST)){
            if (LockUtils.isCurrentlyLocked(event.getBlock())){
                if (event.getPlayer().equals(LockUtils.getWhoLocked(event.getBlock()))){

                    LockUtils.deleteLock(event.getBlock());

                } else if (!(event.getPlayer().equals(LockUtils.getWhoLocked(event.getBlock())))){

                    event.setCancelled(true);

                }
            }
        }
    }

}
