package io.github.scientiist.supersort;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.block.Sign;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerPressEvent implements Listener {
	
	
	public boolean hasEmptySlot(Chest c) {
		for (ItemStack stack : c.getInventory().getContents()) {
            if (stack == null) {
                return true;
            }
        }
		return false;
	}
	
	public void dumpItems(Player p, Chest chest, String group) {
		FileConfiguration config = SuperSort.pl.getConfig();
		
		
		if (config.contains(group)) {
			
			List<String> ids = config.getStringList(group);
			
			for (ItemStack i : p.getInventory().getContents()) {
				if ((i!= null) && (ids.contains(i.getType().toString()))) {
					if (!hasEmptySlot(chest)) {
				        break;	
				    }
					
				    chest.getInventory().addItem(i.clone());
				    p.getInventory().removeItem(i);
				}
	        }
		}
	}
	
    public List<Block> getRegionBlocks(World world, Location loc1, Location loc2) {
        List<Block> blocks = new ArrayList<Block>();
 
        for(double x = loc1.getX(); x <= loc2.getX(); x++) {
            for(double y = loc1.getY(); y <= loc2.getY(); y++) {
                for(double z = loc1.getZ(); z <= loc2.getZ(); z++) {
                    Location loc = new Location(world, x, y, z);
                    blocks.add(loc.getBlock());
                }
            }
        }
 
        return blocks;
    }
    
    @EventHandler
    public void signClicked(PlayerInteractEvent e) {
    	if ((e.getClickedBlock()!=null) && e.getClickedBlock().getState() instanceof Sign) {
    		Sign sign = (Sign) e.getClickedBlock().getState();
    		Player p = e.getPlayer();
    		
    		if (sign.getLines()[0].equalsIgnoreCase("#supersort")) {
    			
    			
    			org.bukkit.material.Sign s = (org.bukkit.material.Sign) e.getClickedBlock().getState().getData();
    			Block attached = e.getClickedBlock().getRelative(s.getAttachedFace());
    			
    			if (attached.getType() == Material.CHEST || attached.getType() == Material.TRAPPED_CHEST) {
    				Chest c = (Chest) attached.getState();
	    			
	    			dumpItems(p, c, sign.getLines()[0].toLowerCase());
				}
    		}
    	}
    }
    
	@EventHandler
	public void platePressed(PlayerInteractEvent e) {
		
		if (e.getAction().equals(Action.PHYSICAL)) {
			if (e.getClickedBlock().getType() == Material.GOLD_PLATE) {
				
				
				
				Player p = e.getPlayer();
				
				int radius = 5;
				
				Location loc1 = new Location(p.getWorld(), p.getLocation().getX()-radius, p.getLocation().getY()-radius, p.getLocation().getZ()-radius);
				Location loc2 = new Location(p.getWorld(), p.getLocation().getX()+radius, p.getLocation().getY()+radius, p.getLocation().getZ()+radius);
				List<Block> list = getRegionBlocks(p.getWorld(), loc1, loc2);
				
				for (Block b : list) {
					if (b.getType() == Material.CHEST || b.getType() == Material.TRAPPED_CHEST) {
						
						Sign signOnChest = null;
						// check if a sign is nearby
							
						if (b.getLocation().add(0, 0, 1).getBlock().getType() == Material.WALL_SIGN) {
							signOnChest = (Sign) b.getLocation().add(0, 0, 1).getBlock().getState();
						}
							
						if (b.getLocation().add(1, 0, 0).getBlock().getType() == Material.WALL_SIGN) {
							signOnChest = (Sign) b.getLocation().add(1, 0, 0).getBlock().getState();
						}
							
						if (b.getLocation().add(0, 0, -1).getBlock().getType() == Material.WALL_SIGN) {
							signOnChest = (Sign) b.getLocation().add(0, 0, -1).getBlock().getState();
						}
							
						if (b.getLocation().add(-1, 0, 0).getBlock().getType() == Material.WALL_SIGN) {
							signOnChest = (Sign) b.getLocation().add(-1, 0, 0).getBlock().getState();
						}
						
						if (signOnChest != null) {
							if (signOnChest.getLines()[0].equalsIgnoreCase("#supersort")) {
								
								p.sendMessage("SuperSort activated, sorting...");
								
								FileConfiguration config = SuperSort.pl.getConfig();
								if (config.contains(signOnChest.getLines()[1])) {
									
									Chest c = (Chest) b.getState();
									
									dumpItems(p, c, signOnChest.getLines()[1].toLowerCase());
											
								}
							}
						}
					}
				}	
			}
		}
	}
}
