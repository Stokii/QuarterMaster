package me.stokey.quartermaster.commands;

import me.stokey.quartermaster.commands.subcommands.HelpCommand;
import me.stokey.quartermaster.commands.subcommands.LockCommand;
import me.stokey.quartermaster.commands.subcommands.ManagerCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;

public class CommandManager implements CommandExecutor {
    ArrayList<SubCommand> subCommands = new ArrayList<>();
    public CommandManager() {
        this.subCommands.add(new LockCommand());
        this.subCommands.add(new ManagerCommand());
        this.subCommands.add(new HelpCommand());
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (sender instanceof Player player){
            if (args.length > 0){
                for (int i = 0; i < getSubCommands().size(); i++){
                    if (args[0].equalsIgnoreCase(getSubCommands().get(i).getName())){
                        getSubCommands().get(i).perform(player, args);
                    }
                }
            } else if (args.length == 0){
                HelpCommand helpCommand = new HelpCommand();
                helpCommand.perform(player, args);
            }
        }
        return true;
    }
    public ArrayList<SubCommand> getSubCommands(){
        return this.subCommands;
    }
}
