package me.stokey.quartermaster.commands.subcommands;

import me.stokey.quartermaster.commands.CommandManager;
import me.stokey.quartermaster.commands.SubCommand;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class HelpCommand extends SubCommand {
    @Override
    public String getName() {
        return "help";
    }

    @Override
    public String getDescription() {
        return "Show all of the commands for QuarterMaster";
    }

    @Override
    public String getSyntax() {
        return "/qm help";
    }

    @Override
    public void perform(Player player, String[] args) {
        CommandManager commandManager = new CommandManager();

        player.sendMessage(ChatColor.DARK_RED + "======= " + ChatColor.BLUE + ChatColor.BOLD + "Quarter" + ChatColor.RED + ChatColor.BOLD + "Master " + ChatColor.YELLOW + "Commands " + ChatColor.DARK_RED + "=======");
        for (int i = 0; i < commandManager.getSubCommands().size(); i++){
            player.sendMessage(ChatColor.YELLOW + commandManager.getSubCommands().get(i).getSyntax() + " - " + ChatColor.GRAY + ChatColor.ITALIC + commandManager.getSubCommands().get(i).getDescription());
        }
        player.sendMessage(ChatColor.DARK_RED + "====================================");
    }
}
