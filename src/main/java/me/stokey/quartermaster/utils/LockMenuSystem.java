package me.stokey.quartermaster.utils;

import me.stokey.quartermaster.QuarterMaster;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.UUID;

public class LockMenuSystem {
    Player player;
    private static Inventory menu;
    private String lockID;
    private Player playerToAdd;
    private Player playerToRemove;

    private Block lockToCreate;

    public LockMenuSystem(Player player) {
        this.player = player;
    }

    public void showAskGUI() {
        menu = Bukkit.createInventory(player, 9, ChatColor.DARK_AQUA + "Lock Detected. Lock Chest?");
        ItemStack yes = new ItemStack(Material.TOTEM_OF_UNDYING, 1);
        ItemStack no = new ItemStack(Material.BARRIER, 1);

        ItemMeta yes_meta = yes.getItemMeta();
        yes_meta.setDisplayName(ChatColor.GREEN + "Yes");
        yes.setItemMeta(yes_meta);

        ItemMeta no_meta = no.getItemMeta();
        no_meta.setDisplayName(ChatColor.RED + "No");
        no.setItemMeta(no_meta);

        menu.setItem(3, yes);
        menu.setItem(5, no);

        for (int i = 0; i < 9; i++) {
            if (menu.getItem(i) == null) {
                menu.setItem(i, new ItemStack(Material.GRAY_STAINED_GLASS_PANE));
            }
        }

        player.openInventory(menu);

    }

    public void showLockListGUI(){
        menu = Bukkit.createInventory(player, 54, ChatColor.RED + "Your locks: ");

        String uuid = player.getUniqueId().toString();
        Document filter = new Document("uuid", uuid);
        QuarterMaster.getDatabaseCollection().find(filter).forEach(document -> {
            ItemStack lock = new ItemStack(Material.CHEST, 1);
            ItemMeta lock_meta = lock.getItemMeta();

            lock_meta.setDisplayName(ChatColor.GREEN + "Chest Lock");

            ArrayList<String> lore = new ArrayList<>();

            lore.add(ChatColor.GOLD + "----------");
            lore.add(ChatColor.YELLOW + "Location:");

            Document location = (Document) document.get("location");
            lore.add(ChatColor.AQUA + " x: " + ChatColor.GREEN + location.getInteger("x"));
            lore.add(ChatColor.AQUA + " y: " + ChatColor.GREEN + location.getInteger("y"));
            lore.add(ChatColor.AQUA + " z: " + ChatColor.GREEN + location.getInteger("z"));
            lore.add("Date created: " + document.getDate("creation-date").toString());
            lore.add(ChatColor.GOLD + "----------");
            lore.add(document.getObjectId("_id").toString());

            lock_meta.setLore(lore);
            lock.setItemMeta(lock_meta);
            menu.addItem(lock);
        });
        for (int i = 0; i < 54; i++) {
            if (menu.getItem(i) == null) {
                menu.setItem(i, new ItemStack(Material.GRAY_STAINED_GLASS_PANE));
            }
        }

        player.openInventory(menu);
    }

    public void showLockMaganerGUI(){
        menu = Bukkit.createInventory(player, 9, ChatColor.GOLD + "Lock Manager");

        //MANAGE
        ItemStack manage_access = new ItemStack(Material.ARMOR_STAND, 1);
        ItemMeta access_meta = manage_access.getItemMeta();
        access_meta.setDisplayName(ChatColor.YELLOW + "Access Manager");
        ArrayList<String> access_lore = new ArrayList<>();
        access_lore.add(ChatColor.GREEN + "Manage who has access this lock");

        access_meta.setLore(access_lore);
        manage_access.setItemMeta(access_meta);

        //DELETE
        ItemStack delete_lock = new ItemStack(Material.WITHER_ROSE, 1);
        ItemMeta delete_meta = delete_lock.getItemMeta();
        delete_meta.setDisplayName(ChatColor.DARK_RED + "Delete lock");
        ArrayList<String> delete_lore = new ArrayList<>();
        delete_lore.add(ChatColor.GREEN + "Deleting the lock will ");
        delete_lore.add(ChatColor.GREEN + "make your chest totally unprotected");

        delete_meta.setLore(delete_lore);
        delete_lock.setItemMeta(delete_meta);

        //INFO
        ItemStack lock_info = new ItemStack(Material.WRITABLE_BOOK, 1);
        ItemMeta info_meta = lock_info.getItemMeta();
        info_meta.setDisplayName(ChatColor.GREEN + "Lock information");
        ArrayList<String> info_lore = new ArrayList<>();
        info_lore.add(ChatColor.GOLD + "----------");
        info_lore.add(ChatColor.YELLOW + "Location: ");

        Document lock = LockUtils.getLock(this.lockID);

        Document location = (Document) lock.get("location");
        info_lore.add(ChatColor.AQUA + " x: " + ChatColor.GREEN + location.getInteger("x"));
        info_lore.add(ChatColor.AQUA + " y: " + ChatColor.GREEN + location.getInteger("y"));
        info_lore.add(ChatColor.AQUA + " z: " + ChatColor.GREEN + location.getInteger("z"));
        info_lore.add("Date created: " + lock.getDate("creation-date").toString());
        info_lore.add(ChatColor.GOLD + "----------");

        info_meta.setLore(info_lore);
        lock_info.setItemMeta(info_meta);

        //CLOSE
        ItemStack close_menu = new ItemStack(Material.ARROW, 1);
        ItemMeta close_meta = close_menu.getItemMeta();
        close_meta.setDisplayName(ChatColor.DARK_RED + "Close");
        ArrayList<String> close_lore = new ArrayList<>();
        close_lore.add(ChatColor.GREEN + "Go back to lock list");

        close_meta.setLore(close_lore);
        close_menu.setItemMeta(close_meta);

        menu.setItem(0, manage_access);
        menu.setItem(1, delete_lock);
        menu.setItem(7, lock_info);
        menu.setItem(8, close_menu);

        player.openInventory(menu);

    }

    public void showConfirmDeleteMenu(){
        menu = Bukkit.createInventory(player, 9, ChatColor.RED + "Confirm: Delete lock?");

        ItemStack yes = new ItemStack(Material.EMERALD, 1);
        ItemMeta yes_meta = yes.getItemMeta();
        yes_meta.setDisplayName(ChatColor.GREEN + "Yes");

        ItemStack no = new ItemStack(Material.BARRIER, 1);
        ItemMeta no_meta = no.getItemMeta();
        no_meta.setDisplayName(ChatColor.RED + "No");

        yes.setItemMeta(yes_meta);
        no.setItemMeta(no_meta);

        menu.setItem(3, yes);
        menu.setItem(5, no);

        for (int i = 0; i < 9; i++) {
            if (menu.getItem(i) == null) {
                menu.setItem(i, new ItemStack(Material.GRAY_STAINED_GLASS_PANE));
            }
        }

        player.openInventory(menu);

    }

    public void showAccessManagerMenu(){
        menu = Bukkit.createInventory(player, 45, ChatColor.GREEN + "Access Manager");

        ItemStack remove = new ItemStack(Material.REDSTONE_BLOCK, 1);
        ItemMeta remove_meta = remove.getItemMeta();
        remove_meta.setDisplayName(ChatColor.DARK_RED + "Remove Player");
        ArrayList<String> remove_lore = new ArrayList<>();
        remove_lore.add(ChatColor.YELLOW + "Remove player from this chest");
        remove_meta.setLore(remove_lore);
        remove.setItemMeta(remove_meta);
        menu.setItem(13, remove);

        ItemStack players = new ItemStack(Material.PLAYER_HEAD, 1);
        ItemMeta players_meta = players.getItemMeta();
        players_meta.setDisplayName(ChatColor.AQUA + "View Players");
        ArrayList<String> players_lore = new ArrayList<>();
        players_lore.add(ChatColor.GREEN + "See who has access to your lock");
        players_meta.setLore(players_lore);
        players.setItemMeta(players_meta);
        menu.setItem(22, players);

        ItemStack add = new ItemStack(Material.ENDER_EYE, 1);
        ItemMeta add_meta = add.getItemMeta();
        add_meta.setDisplayName(ChatColor.GOLD + "Add Player to Lock");
        add.setItemMeta(add_meta);
        menu.setItem(31, add);

        ItemStack close = new ItemStack(Material.BARRIER, 1);
        ItemMeta close_meta = close.getItemMeta();
        close_meta.setDisplayName(ChatColor.RED + "Close");
        close.setItemMeta(close_meta);
        menu.setItem(44, close);

        for (int i = 0; i < 44; i++) {
            if (menu.getItem(i) == null) {
                menu.setItem(i, new ItemStack(Material.GRAY_STAINED_GLASS_PANE));
            }
        }

        player.openInventory(menu);

    }

    public void showPlayersWithAccess(){
        menu = Bukkit.createInventory(player, 45, ChatColor.YELLOW + "Players with Access to Lock");

        ArrayList<String> accessList = (ArrayList<String>) LockUtils.getLock(this.lockID).get("access");

        if (accessList.isEmpty()){
            player.sendMessage(ChatColor.GREEN + "You have not added anyone to this lock.");
        } else {
            for (int i = 0; i < accessList.size(); i++){
                UUID uuid = UUID.fromString(accessList.get(i));
                Player playerWithAccess = Bukkit.getPlayer(uuid);

                ItemStack player = new ItemStack(Material.PLAYER_HEAD, 1);
                ItemMeta player_meta = player.getItemMeta();
                player_meta.setDisplayName(playerWithAccess.getDisplayName());
                player.setItemMeta(player_meta);
                menu.addItem(player);
            }
        }

        ItemStack close = new ItemStack(Material.BARRIER, 1);
        ItemMeta close_meta = close.getItemMeta();
        close_meta.setDisplayName(ChatColor.DARK_RED + "Close");
        close.setItemMeta(close_meta);
        menu.setItem(44, close);

        for (int i = 0; i < 44; i++) {
            if (menu.getItem(i) == null) {
                menu.setItem(i, new ItemStack(Material.GRAY_STAINED_GLASS_PANE));
            }
        }

        player.openInventory(menu);

    }

    public void showPlayersToAddMenu(){
        menu = Bukkit.createInventory(player, 54, ChatColor.GREEN + "Choose a Player to Add:");

        ArrayList<Player> list = new ArrayList<>(player.getServer().getOnlinePlayers());
        for (int i = 0; i < list.size(); i++){
            if (!(list.get(i).equals(player))){
                ItemStack playerHead = new ItemStack(Material.PLAYER_HEAD, 1);
                ItemMeta playerHead_meta = playerHead.getItemMeta();
                playerHead_meta.setDisplayName(list.get(i).getDisplayName());
                playerHead.setItemMeta(playerHead_meta);

                menu.addItem(playerHead);
            }
        }

        ItemStack close = new ItemStack(Material.BARRIER, 1);
        ItemMeta close_meta = close.getItemMeta();
        close_meta.setDisplayName(ChatColor.DARK_RED + "Close");
        close.setItemMeta(close_meta);
        menu.setItem(53, close);

        for (int i = 0; i < 53; i++) {
            if (menu.getItem(i) == null) {
                menu.setItem(i, new ItemStack(Material.GRAY_STAINED_GLASS_PANE));
            }
        }

        player.openInventory(menu);

    }

    public void showPlayersToRemove(){
        menu = Bukkit.createInventory(player, 45, ChatColor.YELLOW + "Choose a Player to Remove");

        ArrayList<String> accessList = (ArrayList<String>) LockUtils.getLock(this.lockID).get("access");

        if (accessList.isEmpty()){
            player.sendMessage(ChatColor.GREEN + "You have not added anyone to this lock.");
        } else {
            for (int i = 0; i < accessList.size(); i++){
                UUID uuid = UUID.fromString(accessList.get(i));
                Player playerWithAccess = Bukkit.getPlayer(uuid);

                ItemStack player = new ItemStack(Material.PLAYER_HEAD, 1);
                ItemMeta player_meta = player.getItemMeta();
                player_meta.setDisplayName(playerWithAccess.getDisplayName());
                player.setItemMeta(player_meta);
                menu.addItem(player);
            }
        }

        ItemStack close = new ItemStack(Material.BARRIER, 1);
        ItemMeta close_meta = close.getItemMeta();
        close_meta.setDisplayName(ChatColor.DARK_RED + "Close");
        close.setItemMeta(close_meta);
        menu.setItem(44, close);

        for (int i = 0; i < 44; i++) {
            if (menu.getItem(i) == null) {
                menu.setItem(i, new ItemStack(Material.GRAY_STAINED_GLASS_PANE));
            }
        }

        player.openInventory(menu);

    }

    public void showConfirmAddPlayerMenu(){
        menu = Bukkit.createInventory(player, 9, ChatColor.GREEN + "Confirm: Add Player");

        ItemStack yes = new ItemStack(Material.EMERALD, 1);
        ItemMeta yes_meta = yes.getItemMeta();
        yes_meta.setDisplayName(ChatColor.GREEN + "Yes");
        ArrayList<String> yes_lore = new ArrayList<>();
        yes_lore.add(ChatColor.AQUA + "Would you like to add ");
        yes_lore.add(ChatColor.AQUA + "this player to your lock?");
        yes_meta.setLore(yes_lore);
        yes.setItemMeta(yes_meta);

        ItemStack no = new ItemStack(Material.BARRIER, 1);
        ItemMeta no_meta = no.getItemMeta();
        no_meta.setDisplayName(ChatColor.DARK_RED + "No");
        no.setItemMeta(no_meta);
        menu.setItem(3, yes);
        menu.setItem(5, no);

        for (int i = 0; i < 9; i++) {
            if (menu.getItem(i) == null) {
                menu.setItem(i, new ItemStack(Material.GRAY_STAINED_GLASS_PANE));
            }
        }

        player.openInventory(menu);

    }

    public void showConfirmRemoveMenu(){
        menu = Bukkit.createInventory(player, 9, ChatColor.RED + "Confirm: Remove Player");

        ItemStack yes = new ItemStack(Material.EMERALD, 1);
        ItemMeta yes_meta = yes.getItemMeta();
        yes_meta.setDisplayName(ChatColor.GREEN + "Yes");
        ArrayList<String> yes_lore = new ArrayList<>();
        yes_lore.add(ChatColor.AQUA + "Remove this player ");
        yes_lore.add(ChatColor.AQUA + "from the lock");
        yes_meta.setLore(yes_lore);
        yes.setItemMeta(yes_meta);

        ItemStack no = new ItemStack(Material.BARRIER, 1);
        ItemMeta no_meta = no.getItemMeta();
        no_meta.setDisplayName(ChatColor.DARK_RED + "No");
        no.setItemMeta(no_meta);
        menu.setItem(3, yes);
        menu.setItem(5, no);

        for (int i = 0; i < 9; i++) {
            if (menu.getItem(i) == null) {
                menu.setItem(i, new ItemStack(Material.GRAY_STAINED_GLASS_PANE));
            }
        }

        player.openInventory(menu);

    }

    public Inventory getMenu() {
        return menu;
    }

    public Block getLockToCreate() {
        return lockToCreate;
    }

    public void setLockToCreate(Block lockToCreate) {
        this.lockToCreate = lockToCreate;
    }

    public String getLockID() {
        return lockID;
    }

    public void setLockID(String lockID) {
        this.lockID = lockID;
    }

    public Player getPlayerToAdd() {
        return playerToAdd;
    }

    public void setPlayerToAdd(Player playerToAdd) {
        this.playerToAdd = playerToAdd;
    }

    public Player getPlayerToRemove() {
        return playerToRemove;
    }

    public void setPlayerToRemove(Player playerToRemove) {
        this.playerToRemove = playerToRemove;
    }
}
