package me.stokey.quartermaster;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import me.stokey.quartermaster.commands.CommandManager;
import me.stokey.quartermaster.commands.subcommands.ManagerCommand;
import me.stokey.quartermaster.commands.subcommands.LockCommand;
import me.stokey.quartermaster.listeners.MenuListeners;
import me.stokey.quartermaster.listeners.ChestListeners;
import me.stokey.quartermaster.utils.LockMenuSystem;
import org.bson.Document;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;

public final class QuarterMaster extends JavaPlugin {
    private static QuarterMaster plugin;
    private MongoClient mongoClient;
    private MongoDatabase database;
    private static MongoCollection<Document> collection;
    public static HashMap<Player, LockMenuSystem> playerLockMenuSystemHashMap = new HashMap<>();

    @Override
    public void onEnable() {

        plugin = this;

        //Setup Config
        getConfig().options().copyDefaults();
        saveDefaultConfig();

        // Plugin startup logic JZBqyJkAhfpa71fE 1:01:34
        if (getConfig().getString("connection-string").isEmpty()){
            System.out.println("[QM] - YOU NEED TO SPECIFY A MONGODB CONNECTION STRING IN THE CONFIG.YML");
        } else {
            mongoClient = MongoClients.create(getConfig().getString("connection-string"));
            database = mongoClient.getDatabase("quartermaster");
            collection = database.getCollection("locks");
        }

        getCommand("quartermaster").setExecutor(new CommandManager());

        getServer().getPluginManager().registerEvents(new MenuListeners(), this);
        getServer().getPluginManager().registerEvents(new ChestListeners(), this);



    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static MongoCollection<Document> getDatabaseCollection() {
        return collection;
    }

    public static LockMenuSystem getPlayerMenuSystem(Player player){
        LockMenuSystem lockMenuSystem = null;
        if (QuarterMaster.playerLockMenuSystemHashMap.containsKey(player)){
            return playerLockMenuSystemHashMap.get(player);
        } else {
            lockMenuSystem = new LockMenuSystem(player);
            playerLockMenuSystemHashMap.put(player, lockMenuSystem);

            return lockMenuSystem;
        }
    }

    public static QuarterMaster getPlugin(){
        return plugin;
    }
}
