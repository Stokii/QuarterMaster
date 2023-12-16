package me.stokey.quartermaster.commands.subcommands;

import me.stokey.quartermaster.QuarterMaster;
import me.stokey.quartermaster.commands.SubCommand;
import me.stokey.quartermaster.utils.LockMenuSystem;
import me.stokey.quartermaster.utils.LockUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class LockCommand extends SubCommand {

    @Override
    public String getName() {
        return "lock";
    }

    @Override
    public String getDescription() {
        return "Allows you to lock a block";
    }

    @Override
    public String getSyntax() {
        return "/qm lock";
    }

    @Override
    public void perform(Player player, String[] args) {

        Block target;
        if (!(player.getTargetBlockExact(5) == null)){
            target = player.getTargetBlockExact(5);

            for (int i = 0; i < LockUtils.getLockadbleBlocks().size(); i++){
                if(target.getType().equals(Material.valueOf(LockUtils.getLockadbleBlocks().get(i)))){
                    if(LockUtils.isCurrentlyLocked(target)){
                        if(LockUtils.getWhoLocked(target).equals(player)){
                            player.sendMessage("You already locked this lock. It's locked!");
                        } else {
                            player.sendMessage(ChatColor.RED + "This block is already locked by: " + LockUtils.getWhoLocked(target).getName());

                        }
                    } else {
                        player.sendMessage("Would you like to lock this block?");

                        LockMenuSystem lockMenuSystem = QuarterMaster.getPlayerMenuSystem(player);
                        lockMenuSystem.setLockToCreate(target);

                        lockMenuSystem.showAskGUI();
                    }
                }
            }
        } else if (player.getTargetBlockExact(5) == null){
            player.sendMessage(ChatColor.GREEN + "Look at something nearby");
        }
    }
}
