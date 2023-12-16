package me.stokey.quartermaster.utils;

import me.stokey.quartermaster.QuarterMaster;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class LockUtils {

    public static void createNewLock(Player p, Block block){

        //Document representing the new lock
        Document lock = new Document("uuid", p.getUniqueId().toString())
                .append("type", block.getType().toString())
                .append("location", new Document("x", block.getX()).append("y", block.getY()).append("z", block.getZ()))
                .append("creation-date", new Date())
                .append("access", new ArrayList<String>());

        QuarterMaster.getDatabaseCollection().insertOne(lock);
        System.out.println("New lock created!");

        p.closeInventory();

    }

    public static boolean isCurrentlyLocked(Block block){

        int x = block.getX();
        int y = block.getY();
        int z = block.getZ();

        Document filter = new Document("location", new Document("x", x).append("y", y).append("z", z));

        if (QuarterMaster.getDatabaseCollection().countDocuments(filter) == 1){
            return true;
        }
        return false;
    }

    public static Player getWhoLocked(Block block){

        int x = block.getX();
        int y = block.getY();
        int z = block.getZ();

        Document filter = new Document("location", new Document("x", x).append("y", y).append("z", z));

        String uuidString = QuarterMaster.getDatabaseCollection().find(filter).first().getString("uuid");
        UUID uuid = UUID.fromString(uuidString);

        return Bukkit.getPlayer(uuid);

    }

    public static void deleteLock(Block block){

        int x = block.getX();
        int y = block.getY();
        int z = block.getZ();

        Document filter = new Document("location", new Document("x", x).append("y", y).append("z", z));

        // Ezzel töröljük az adatbázisból a cuccot.
        QuarterMaster.getDatabaseCollection().deleteOne(filter);
        System.out.println("Lock deleted!");
    }

    public static void deleteLock(String id){

        Document lock = LockUtils.getLock(id);
        QuarterMaster.getDatabaseCollection().deleteOne(lock);

    }

    public static Document getLock(String id){

        Document filter = new Document(new Document("_id", new ObjectId(id)));
        return QuarterMaster.getDatabaseCollection().find(filter).first();

    }

    public static void addPlayerToLock(String lockID, Player playerToAdd){
        Document lock = LockUtils.getLock(lockID);

        ArrayList<String> accessList = (ArrayList<String>) lock.get("access");
        accessList.add(playerToAdd.getUniqueId().toString());

        Document newDoc = new Document("access", accessList);
        Document newDoc2 = new Document("$set", newDoc);

        //Find the document from the database from the document stored in the lock
        Document filter = new Document(new Document("_id", new ObjectId(String.valueOf(lock.getObjectId("_id")))));
        QuarterMaster.getDatabaseCollection().updateOne(filter, newDoc2);
    }

    public static void removePlayerFromLock(String lockID, Player playerToRemove){
        Document lock = LockUtils.getLock(lockID);

        ArrayList<String> accessList = (ArrayList<String>) lock.get("access");
        accessList.remove(playerToRemove.getUniqueId().toString());

        //UPDATE rész
        Document newDoc = new Document("access", accessList);
        Document newDoc2 = new Document("$set", newDoc);

        //Find the document from the database from the document stored in the lock
        Document filter = new Document(new Document("_id", new ObjectId(String.valueOf(lock.getObjectId("_id")))));
        QuarterMaster.getDatabaseCollection().updateOne(filter, newDoc2);
    }

    public static List<String> getLockadbleBlocks(){
        List<String> lockable_blocks = QuarterMaster.getPlugin().getConfig().getStringList("lockable-block");
        return lockable_blocks;
    }
}
