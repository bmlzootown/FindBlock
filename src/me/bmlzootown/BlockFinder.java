package me.bmlzootown;

//import java.util.ArrayList;
//import java.util.List;

import java.util.ArrayList;
import java.util.List;

import net.md_5.bungee.api.ChatColor;

import org.bukkit.Bukkit;
//import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import com.sk89q.minecraft.util.commands.CommandException;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.blocks.BlockType;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.bukkit.selections.CuboidSelection;
import com.sk89q.worldedit.bukkit.selections.Selection;

public class BlockFinder extends JavaPlugin implements Listener {
	public static List<String> blockValues;
	
	public void onEnable() {
		Bukkit.getServer().getPluginManager().registerEvents(this, this);
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		Player p = (Player) sender;
		
		if ((sender instanceof Player)) {
			
			if (cmd.getName().equalsIgnoreCase("/find")) {
				if (args.length == 1) {
					Selection sel = getWorldEdit().getSelection(p);
					if (sel != null) {
						if (sel instanceof CuboidSelection) {
							List<String> derpz = FindBlock(p,sel,args[0]);
							if (!(derpz.isEmpty())) {
								try {
									paginate(sender, derpz, 1);
								} catch (CommandException e) {
									e.printStackTrace();
								}	
							} else {
								p.sendMessage(ChatColor.LIGHT_PURPLE + "Block does not exist in the selection!");
							}
						} else {
							p.sendMessage(ChatColor.LIGHT_PURPLE + "Please use a cuboid selection!");
						}
					} else {
						p.sendMessage(ChatColor.LIGHT_PURPLE +  "Please select an area!");
					}
				} else if (args.length > 1) {
					Selection sel = getWorldEdit().getSelection(p);
					if (sel != null) {
						if (sel instanceof CuboidSelection) {
							List<String> derpz = FindBlock(p,sel,args[0]);
							if (!(derpz.isEmpty())) {
								if (isNumber(args[1])) {
									int page = Integer.parseInt(args[1]);
									try {
										paginate(sender, derpz, page);
									} catch (CommandException e) {
										e.printStackTrace();
									}
								} else {
									p.sendMessage(ChatColor.LIGHT_PURPLE + "Page does not exist!");
								}	
							} else {
								p.sendMessage(ChatColor.LIGHT_PURPLE + "Block does not exist in the selection!");
							}
						} else {
							p.sendMessage(ChatColor.LIGHT_PURPLE + "Please use a cuboid selection!");
						}
					} else {
						p.sendMessage(ChatColor.LIGHT_PURPLE +  "Please select an area!");
					}
				} else if (args.length == 0) {
					p.sendMessage(ChatColor.RED +  "Too few parameters!");
					p.sendMessage(ChatColor.RED +  "Usage: //find <block name> [page]");
				}
				
			} 
			
		} else if (!(sender instanceof Player)) {
			sender.sendMessage("You cannot run this command via the console!");
		}
		return true;
	}
	
	public static List<String> FindBlock(Player p, Selection sel, String block) {
		//List<Block> blocks = new ArrayList<Block>();
		
		Vector max = sel.getNativeMaximumPoint();
		Vector min = sel.getNativeMinimumPoint();
		List<String> output = new ArrayList<String>();
		
		int minX = min.getBlockX();
		int minY = min.getBlockY();
		int minZ = min.getBlockZ();
		int maxX = max.getBlockX();
		int maxY = max.getBlockY();
		int maxZ = max.getBlockZ();
		
		for (int x = minX; x <= maxX; ++x) {
			for (int y = minY; y <= maxY; ++y) {
				for (int z = minZ; z <= maxZ; ++z) {
					//String blockName = sel.getWorld().getBlockAt(x, y, z).getType().toString();
					String otherName = sel.getWorld().getBlockAt(x, y, z).getType().name();
					String blockData = sel.getWorld().getBlockAt(x, y, z).getState().getData().toString();
					if (BlockType.lookup(block).name().contains(otherName)) {
						output.add(blockData + ": "  + x + "x " + y + "y " + z + "z");
					}
				}
			}
		}
		return output;
	}
	
	public void paginate(CommandSender sender, List<String> results, int page) throws CommandException {
        if (results.size() == 0) throw new CommandException("No results match!");

        int maxPages = pages(results);
        if (page <= 0 || page > maxPages) {
        	sender.sendMessage(ChatColor.LIGHT_PURPLE + "Page does not exist!");
        	return;
        }

        sender.sendMessage(ChatColor.DARK_PURPLE + "-----" + ChatColor.LIGHT_PURPLE + " Page "  + page + " out of " + maxPages + ChatColor.DARK_PURPLE + " -----");

        for (int i = 9 * (page - 1); i < 9 * page && i < results.size(); i++) {
            sender.sendMessage(results.get(i));
        }
    }
	
	public static int pages(List<String> derpz) {
		double maxLines = 9;
		int lines = derpz.size();
		int output = (int) Math.ceil(lines / maxLines);
		return output;
	}
	
	public boolean isNumber(String input) {
		  try {
		    Integer.parseInt(input);
		    return true;
		  }
		  catch (NumberFormatException e) {
		    return false;
		  }
		}
	
	public WorldEditPlugin getWorldEdit() {
		Plugin p = Bukkit.getServer().getPluginManager().getPlugin("WorldEdit");
		if (p instanceof WorldEditPlugin) {
			return (WorldEditPlugin) p;
		} else {
			return null;
		}
	}
	
}
