package me.stokey.quartermaster.listeners;

import me.stokey.quartermaster.QuarterMaster;
import me.stokey.quartermaster.utils.LockMenuSystem;
import me.stokey.quartermaster.utils.LockUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class MenuListeners implements Listener {
    @EventHandler
    public void onLockClick(InventoryClickEvent e){
        Player p = (Player) e.getWhoClicked();
        LockMenuSystem lockMenuSystem = QuarterMaster.getPlayerMenuSystem(p);

        if(e.getView().getTitle().equalsIgnoreCase(ChatColor.DARK_AQUA + "Lock Detected. Lock Chest?")){
            e.setCancelled(true);
            if(e.getCurrentItem().getType().equals(Material.TOTEM_OF_UNDYING)){
                p.sendMessage("Creating a new lock");

                //Create a new lock for the player
                LockUtils.createNewLock(p, lockMenuSystem.getLockToCreate());

            } else if(e.getCurrentItem().getType().equals(Material.BARRIER)){
                p.closeInventory();
            }
        } else if (e.getView().getTitle().equalsIgnoreCase(ChatColor.RED + "Your locks: ")){
            e.setCancelled(true);
            if (e.getCurrentItem().getType().equals(null)){
                return;
            } else if (e.getCurrentItem().getType().equals(Material.CHEST)){
                lockMenuSystem.setLockID(lockMenuSystem.getMenu().getItem(e.getSlot()).getItemMeta().getLore().get(7));

                lockMenuSystem.showLockMaganerGUI();
            }
        } else if (e.getView().getTitle().equalsIgnoreCase(ChatColor.GOLD + "Lock Manager")) {
            e.setCancelled(true);
            if (e.getCurrentItem().getType().equals(Material.ARROW)){
                //Vissza az előző menüre
                lockMenuSystem.showLockListGUI();
            } else if (e.getCurrentItem().getType().equals(Material.WITHER_ROSE)){
                //Megerősítő menü a törlésről
                lockMenuSystem.showConfirmDeleteMenu();
            }else if (e.getCurrentItem().getType().equals(Material.ARMOR_STAND)){
                //open the access manager
                lockMenuSystem.showAccessManagerMenu();
            }
        } else if (e.getView().getTitle().equalsIgnoreCase(ChatColor.RED + "Confirm: Delete lock?")){
            e.setCancelled(true);
            if (e.getCurrentItem().getType().equals(Material.BARRIER)){
                lockMenuSystem.showLockMaganerGUI();
            }else if(e.getCurrentItem().getType().equals(Material.EMERALD)){
                //Delete lock, since they confirm yes
                LockUtils.deleteLock(lockMenuSystem.getLockID());
                p.sendMessage(ChatColor.GREEN + "Your lock(" + ChatColor.GOLD + lockMenuSystem.getLockID() + ChatColor.GREEN + ") has been deleted.");
                lockMenuSystem.showLockListGUI();
            }
        } else if (e.getView().getTitle().equalsIgnoreCase(ChatColor.GREEN + "Access Manager")){
            e.setCancelled(true);
            if (e.getCurrentItem().getType().equals(Material.BARRIER)){
                lockMenuSystem.showLockMaganerGUI();
            } else if (e.getCurrentItem().getType().equals(Material.PLAYER_HEAD)){
                //Open player list GUI
                lockMenuSystem.showPlayersWithAccess();
            } else if (e.getCurrentItem().getType().equals(Material.ENDER_EYE)){
                //Open players to add menu
                lockMenuSystem.showPlayersToAddMenu();
            } else if (e.getCurrentItem().getType().equals(Material.REDSTONE_BLOCK)){
                lockMenuSystem.showPlayersToRemove();
            }
        } else if (e.getView().getTitle().equalsIgnoreCase(ChatColor.YELLOW + "Players with Access to Lock")){
            e.setCancelled(true);
            if (e.getCurrentItem().getType().equals(Material.BARRIER)){
                lockMenuSystem.showAccessManagerMenu();
            }
        }else if (e.getView().getTitle().equalsIgnoreCase(ChatColor.GREEN + "Choose a Player to Add:")){
            e.setCancelled(true);
            if (e.getCurrentItem().getType().equals(Material.PLAYER_HEAD)){
                //Open confirm menu for adding a player
                lockMenuSystem.showConfirmAddPlayerMenu();
                lockMenuSystem.setPlayerToAdd(Bukkit.getPlayer(e.getCurrentItem().getItemMeta().getDisplayName()));
            } else if (e.getCurrentItem().getType().equals(Material.BARRIER)){
                lockMenuSystem.showAccessManagerMenu();
            }
        } else if (e.getView().getTitle().equalsIgnoreCase(ChatColor.YELLOW + "Choose a Player to Remove")){
            e.setCancelled(true);
            if (e.getCurrentItem().getType().equals(Material.BARRIER)){
                lockMenuSystem.showAccessManagerMenu();
            } else if (e.getCurrentItem().getType().equals(Material.PLAYER_HEAD)){
                //show the confirm remove menu
                lockMenuSystem.showConfirmRemoveMenu();

                lockMenuSystem.setPlayerToRemove(Bukkit.getPlayer(e.getCurrentItem().getItemMeta().getDisplayName()));
            }
        } else if (e.getView().getTitle().equalsIgnoreCase(ChatColor.GREEN + "Confirm: Add Player")){
            e.setCancelled(true);
            if (e.getCurrentItem().getType().equals(Material.BARRIER)){
                lockMenuSystem.showAccessManagerMenu();
            } else if (e.getCurrentItem().getType().equals(Material.EMERALD)){
                //Add player to lock
                LockUtils.addPlayerToLock(lockMenuSystem.getLockID(), lockMenuSystem.getPlayerToAdd());

                p.sendMessage(ChatColor.GREEN + "Added " + ChatColor.YELLOW + lockMenuSystem.getPlayerToAdd().getName() + ChatColor.GREEN + " to your lock!");
                lockMenuSystem.getPlayerToAdd().sendMessage(ChatColor.YELLOW + p.getName() + " has justed granted you access to one of their lock.");

                lockMenuSystem.showAccessManagerMenu();
            }
        } else if (e.getView().getTitle().equalsIgnoreCase(ChatColor.RED + "Confirm: Remove Player")){
            e.setCancelled(true);
            if (e.getCurrentItem().getType().equals(Material.BARRIER)){
                lockMenuSystem.showAccessManagerMenu();
            } else if (e.getCurrentItem().getType().equals(Material.EMERALD)){
                //Remove player from lock
                LockUtils.removePlayerFromLock(lockMenuSystem.getLockID(), lockMenuSystem.getPlayerToRemove());

                p.sendMessage(ChatColor.GRAY + "Removed " + ChatColor.YELLOW + lockMenuSystem.getPlayerToRemove().getName() + ChatColor.GRAY + " from your lock.");
                lockMenuSystem.getPlayerToRemove().sendMessage(ChatColor.YELLOW + p.getName() + ChatColor.GRAY + " remove his lock.");

                lockMenuSystem.showAccessManagerMenu();
            }
        }
    }
}
