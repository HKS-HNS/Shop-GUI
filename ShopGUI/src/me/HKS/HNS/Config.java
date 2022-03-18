package me.HKS.HNS;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import me.HKS.HNS.Gui.Gui;

/***
 * 
 * Config Class
 * 
 * Creates and manages the config 
 * 
 * @author HKS-HNS
 * 
 */

public class Config implements CommandExecutor {
    public static File configFile = new File("plugins/ShopGui", "config.yml");
    public static FileConfiguration Config = YamlConfiguration.loadConfiguration(configFile);
    public static HashMap < String, Inventory > inventorys = new HashMap < String, Inventory > ();
    public static HashMap < Inventory, HashMap < Integer, String > > inventorySlotsHas = new HashMap < Inventory, HashMap < Integer, String > > ();
    public static HashMap < Inventory, HashMap < Integer, ItemStack > > inventorySlotsHasI = new HashMap < Inventory, HashMap < Integer, ItemStack > > ();
    public static HashMap < String, String > messages = new HashMap < String, String > ();
    public static String mainMenu = "Menu";
    public static String prefix = "§4§lHNS:";

    public static void config() { // Refresh's the config and creates it if it does not exist

        Config = YamlConfiguration.loadConfiguration(configFile);
        if (!configFile.exists()) {
            Config.set("Shop.Prefix", String.valueOf(prefix));
            Config.set("Shop.messages.reload", String.valueOf("%prefix% §aReloaded config"));
            Config.set("Shop.messages.bought", String.valueOf("%prefix% §a%price%$ were deducted, your balance is now: §e%balance%$"));
            Config.set("Shop.messages.notMoney", String.valueOf("%prefix% §4Not enough Money"));
            Config.set("Shop.Shops", Arrays.asList("Menu", "BlockShop", "ItemShop"));
            Config.set("Shop.Main", "Menu");
            //MainMenu
            Config.set("Shop.Menu.Slots", Integer.valueOf(9));
            Config.set("Shop.Menu.ShopDisplayName", String.valueOf("§2MainMenu"));
            //1. Item
            Config.set("Shop.Menu.Slot.0.Item", String.valueOf("GRASS"));
            Config.set("Shop.Menu.Slot.0.Name", String.valueOf("§a§lBlockShop"));
            Config.set("Shop.Menu.Slot.0.Lore", Arrays.asList("§aThis is the block shop."));
            Config.set("Shop.Menu.Slot.0.Count", Integer.valueOf(1));
            Config.set("Shop.Menu.Slot.0.ShopMenu", String.valueOf("BlockShop"));
            //2. Item
            Config.set("Shop.Menu.Slot.1.Item", String.valueOf("DIAMOND"));
            Config.set("Shop.Menu.Slot.1.Name", String.valueOf("§a§lItemShop"));
            Config.set("Shop.Menu.Slot.1.Count", Integer.valueOf(1));
            Config.set("Shop.Menu.Slot.1.ShopMenu", String.valueOf("ItemShop"));
            //**BlockShop**
            Config.set("Shop.BlockShop.ShopDisplayName", String.valueOf("§2BlockShop"));
            Config.set("Shop.BlockShop.Slots", Integer.valueOf(9));
            Config.set("Shop.BlockShop.Slot.0.Item", String.valueOf("TNT"));
            Config.set("Shop.BlockShop.Slot.0.Name", String.valueOf("§4§lTNT"));
            Config.set("Shop.BlockShop.Slot.0.Lore", Arrays.asList("§aPrice 50.845$"));
            Config.set("Shop.BlockShop.Slot.0.Count", Integer.valueOf(4));
            Config.set("Shop.BlockShop.Slot.0.Buy.Item", String.valueOf("TNT"));
            Config.set("Shop.BlockShop.Slot.0.Buy.Name", String.valueOf("§4§lSpecial TNT"));
            Config.set("Shop.BlockShop.Slot.0.Buy.Count", Integer.valueOf(1));
            Config.set("Shop.BlockShop.Slot.0.Buy.Price", Double.valueOf(50.845));
            //Back
            Config.set("Shop.BlockShop.Slot.8.Item", String.valueOf("ARROW"));
            Config.set("Shop.BlockShop.Slot.8.Name", String.valueOf("§3§lBack"));
            Config.set("Shop.BlockShop.Slot.8.Count", Integer.valueOf(1));
            Config.set("Shop.BlockShop.Slot.8.ShopMenu", String.valueOf("Menu"));
            //**ItemShop**
            Config.set("Shop.ItemShop.ShopDisplayName", String.valueOf("§2ItemShop"));
            Config.set("Shop.ItemShop.Slots", Integer.valueOf(9));
            Config.set("Shop.ItemShop.Slot.0.Item", String.valueOf("DIAMOND_SWORD"));
            Config.set("Shop.ItemShop.Slot.0.Name", String.valueOf("§4§lBuy Lighting Sword"));
            Config.set("Shop.ItemShop.Slot.0.Lore", Arrays.asList("§aPrice 1000$", "Lore"));
            Config.set("Shop.ItemShop.Slot.0.Count", Integer.valueOf(4));
            Config.set("Shop.ItemShop.Slot.0.Buy.Item", String.valueOf("DIAMOND_SWORD"));
            Config.set("Shop.ItemShop.Slot.0.Buy.Name", String.valueOf("§4§lLighting Sword"));
            Config.set("Shop.ItemShop.Slot.0.Buy.Count", Integer.valueOf(1));
            Config.set("Shop.ItemShop.Slot.0.Buy.Price", Double.valueOf(1000));
            //Back
            Config.set("Shop.ItemShop.Slot.8.Item", String.valueOf("ARROW"));
            Config.set("Shop.ItemShop.Slot.8.Name", String.valueOf("§3§lBack"));
            Config.set("Shop.ItemShop.Slot.8.Count", Integer.valueOf(1));
            Config.set("Shop.ItemShop.Slot.8.ShopMenu", String.valueOf("Menu"));

        }
        // Adds the messages
        addMessage("reload");
        addMessage("bought");
        addMessage("notMoney");

        // Initializes the variables
        mainMenu = Config.getString("Shop.Main");
        prefix = Config.getString("Shop.Prefix");
        List < String > shops = Config.getStringList("Shop.Shops");
        for (int i = 0; i < shops.size(); i++) {

            String shopName = shops.get(i);

            HashMap < Integer, String > slots = new HashMap < Integer, String > ();
            HashMap < Integer, ItemStack > Items = new HashMap < Integer, ItemStack > ();

            for (int s = 0; s < Config.getInt("Shop." + shopName + ".Slots"); s++) {
            	
                // Tests if the slot exists
                if (Config.contains("Shop." + shopName + ".Slot." + s)) {

                    // Initializes the variables for the item
                    String item = Config.getString("Shop." + shopName + ".Slot." + s + ".Item");
                    String name = Config.getString("Shop." + shopName + ".Slot." + s + ".Name");
                    int invSlots = Config.getInt("Shop." + shopName + ".Slots");
                    int count = Config.getInt("Shop." + shopName + ".Slot." + s + ".Count");
                    String shopDisplayName = Config.getString("Shop." + shopName + ".ShopDisplayName");
                    List < String > lore = Config.getStringList("Shop." + shopName + ".Slot." + s + ".Lore");
                    String category = null;
                    Double buyPrice = null;
                    int buyCount = 1;
                    String buyName = "";
                    String buyItem = "";

                    // Tests if the item can direct to an inventory 
                    if (Config.contains("Shop." + shopName + ".Slot." + s + ".ShopMenu")) {
                        category = Config.getString("Shop." + shopName + ".Slot." + s + ".ShopMenu");
                    } else {
                        buyItem = Config.getString("Shop." + shopName + ".Slot." + s + ".Buy.Item");
                        buyName = Config.getString("Shop." + shopName + ".Slot." + s + ".Buy.Name");
                        buyCount = Config.getInt("Shop." + shopName + ".Slot." + s + ".Buy.Count");
                        buyPrice = Config.getDouble("Shop." + shopName + ".Slot." + s + ".Buy.Price");

                    }

                    // Creates the item
                    Gui.InitializeItemsGui(s, item, name, count, category, slots, shopName, buyPrice, invSlots, shopDisplayName, buyCount, buyItem, buyName, lore, Items);
                }

            }
            // Adds the Hashmap from the items to an Hashmap
            inventorySlotsHas.put(inventorys.get(shopName), slots);
            inventorySlotsHasI.put(inventorys.get(shopName), Items);

        }

        try {
            Config.save(configFile);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    // Adds the messages tho the message Hashmap
    static void addMessage(String messageTitle) {

        messages.put(messageTitle, Config.getString("Shop.messages." + messageTitle));
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("Shop")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage("§4You aren't a Player");
                return true;
            }

            Player p = (Player) sender;

            // Tests if the Player has the permission to reload
            if (args.length >= 1 && ((p.isOp() || p.hasPermission("shop.gui.rl")) && (args[0].equalsIgnoreCase("rl") || args[0].equalsIgnoreCase("reload")))) {
                inventorys.clear();
                inventorySlotsHas.clear();
                inventorySlotsHasI.clear();
                messages.clear();
                config();
                p.sendMessage(messages.get("reload").replace("%prefix%", prefix));
            } else {

                p.openInventory(inventorys.get("Menu"));
            }
        }
        return false;
    }

}