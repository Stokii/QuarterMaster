package me.stokey.quartermaster.commands.subcommands;

import me.stokey.quartermaster.commands.SubCommand;
import me.stokey.quartermaster.utils.LockMenuSystem;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ManagerCommand extends SubCommand {

    @Override
    public String getName() {
        return "manage";
    }

    @Override
    public String getDescription() {
        return "Manage your locks";
    }

    @Override
    public String getSyntax() {
        return "/qm manage";
    }

    @Override
    public void perform(Player player, String[] args) {
        LockMenuSystem lockMenuSystem = new LockMenuSystem(player);
        lockMenuSystem.showLockListGUI();
    }
}
