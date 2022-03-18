package me.HKS.HNS.Gui;

import java.util.HashMap;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.HKS.HNS.Config;
import me.HKS.HNS.Main;
import net.milkbowl.vault.economy.Economy;

public class Gui implements Listener {

    /***
     * 
     * Gui Class
     * 
     * Creates the inventorys for the categories
     * 
     * @author HKS-HNS
     * 
     */

    HashMap < String, Inventory > invs = Config.inventorys;
    public static void InitializeItemsGui(int slot, String item, String name, int count,
        String category, HashMap < Integer, String > slots,
        String shopName, Double price, int invSlots, String shopDisplayName, int buyCount, String buyItem, String buyName, List < String > lore, HashMap < Integer, ItemStack > items) {
        Inventory inv;
        // Tests if the Inventory already exists, If it does not exist it will create one.
        if (Config.inventorys.containsKey(shopName)) {
            inv = Config.inventorys.get(shopName);
        } else {
            GuiCreator gui = new GuiCreator();
            inv = gui.CreateInventory(shopDisplayName, invSlots);
            Config.inventorys.put(shopName, inv);
        }

        // Creates the itemStack for the item
        ItemStack itemStack = new ItemStack(Material.valueOf(item), count);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(name);
        if (lore != null)
            itemMeta.setLore(lore);
        itemStack.setItemMeta(itemMeta);
        inv.setItem(slot, itemStack);

        // Tests if an category exists to redirect to
        if (category != null) {
            slots.put(slot, "category§:" + category);
        } else {
            // Creates the itemStack of the item you can buy
            ItemStack buyItemStack = new ItemStack(Material.valueOf(buyItem), buyCount);

            ItemMeta buyItemMeta = buyItemStack.getItemMeta();
            if (buyName != "")
                buyItemMeta.setDisplayName(buyName);
            buyItemStack.setItemMeta(buyItemMeta);
            items.put(slot, buyItemStack);
            slots.put(slot, "price§:" + price);

        }

    }

    @EventHandler
    public void OnInventoryClick(InventoryClickEvent e) {
        // Tests if the inventory is an GUI inventory
        if (invs.containsValue(e.getInventory())) {

            // Cancles the event
            e.setCancelled(true);

            // Saves the slot where the player clicked
            int slot = e.getRawSlot();

            // Gets the HashMap with the value of the slot
            HashMap < Integer, String > invSlots = Config.inventorySlotsHas.get(e.getInventory());

            // Tests if the slot exists in the HashMap
            if (!invSlots.containsKey(slot))
                return;

            // Gets the category or price of the slot
            String slotValue = invSlots.get(slot);

            // Tests if the item is a redirector or if you can buy item's with it.
            if (slotValue.startsWith("category§:", 0)) {

                String Categorie = invSlots.get(slot).replaceFirst("category§:", "");

                // Tests if the Inv exists. If it exists, it will open it
                if (invs.containsKey(Categorie)) {
                    e.getWhoClicked().openInventory(invs.get(Categorie));
                }

            } else if (slotValue.startsWith("price§:", 0)) {
                // Gets the items of the buy iventory 
                HashMap < Integer, ItemStack > buyItems = Config.inventorySlotsHasI.get(e.getInventory());

                // Tests if the Item exists in the inventory
                if (!buyItems.containsKey(slot))
                    return;
                // Gets the values for the buy operation
                Double price = Double.valueOf(invSlots.get(slot).replaceFirst("price§:", ""));
                ItemStack item = buyItems.get(slot);
                Economy econemy = Main.getEconomy();

                // Gets the player
                Player p = (Player) e.getWhoClicked();
                OfflinePlayer op = (OfflinePlayer) p;

                // Tests if the player has enough money
                if (econemy.getBalance(op) >= price) {

                    econemy.withdrawPlayer(op, price);

                    p.getInventory().addItem(item);

                    p.sendMessage(Config.messages.get("bought").replace("%prefix%", Config.prefix).replace("%price%", price.toString()).replace("balance", String.valueOf(econemy.getBalance(op))));
                } else {

                    p.sendMessage(Config.messages.get("notMoney").replace("%prefix%", Config.prefix));
                }

            }

        }
    }

}