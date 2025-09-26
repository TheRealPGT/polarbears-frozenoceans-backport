package com.example.frozenocean.command;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;

import java.util.Random;

public class CommandFindFrozenOcean extends CommandBase {
    
    @Override
    public String getCommandName() {
        return "findfrozenocean";
    }
    
    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/findfrozenocean [radius] - Finds the nearest frozen ocean biome";
    }
    
    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        if (!sender.getEntityWorld().isRemote) { // Only run on server side
            World world = sender.getEntityWorld();
            int centerX = (int) sender.getPlayerCoordinates().posX;
            int centerZ = (int) sender.getPlayerCoordinates().posZ;
            int radius = 1000; // Default search radius
            
            if (args.length > 0) {
                try {
                    radius = Integer.parseInt(args[0]);
                    radius = Math.min(radius, 5000); // Limit to 5000 blocks to prevent lag
                    radius = Math.max(radius, 100); // Minimum 100 blocks
                } catch (NumberFormatException e) {
                    sender.addChatMessage(new ChatComponentText(EnumChatFormatting.RED + "Invalid radius! Using default 1000 blocks."));
                }
            }
            
            sender.addChatMessage(new ChatComponentText(EnumChatFormatting.GREEN + "Searching for frozen ocean within " + radius + " blocks..."));
            
            // Search for frozen ocean biome
            int[] result = findNearestFrozenOcean(world, centerX, centerZ, radius);
            
            if (result != null) {
                int foundX = result[0];
                int foundZ = result[1];
                double distance = Math.sqrt(Math.pow(foundX - centerX, 2) + Math.pow(foundZ - centerZ, 2));
                int direction = (int) (Math.toDegrees(Math.atan2(foundZ - centerZ, foundX - centerX)) + 360) % 360;
                String directionName = getDirectionName(direction);
                
                sender.addChatMessage(new ChatComponentText(EnumChatFormatting.AQUA + "Found frozen ocean!"));
                sender.addChatMessage(new ChatComponentText(EnumChatFormatting.WHITE + "Coordinates: X=" + foundX + ", Z=" + foundZ));
                sender.addChatMessage(new ChatComponentText(EnumChatFormatting.WHITE + "Distance: " + (int)distance + " blocks"));
                sender.addChatMessage(new ChatComponentText(EnumChatFormatting.WHITE + "Direction: " + directionName + " (" + direction + "°)"));
                
                // Offer navigation help
                sender.addChatMessage(new ChatComponentText(EnumChatFormatting.YELLOW + "Run towards " + directionName + " to reach the frozen ocean"));
                
            } else {
                sender.addChatMessage(new ChatComponentText(EnumChatFormatting.RED + "No frozen ocean found within " + radius + " blocks."));
                sender.addChatMessage(new ChatComponentText(EnumChatFormatting.YELLOW + "Try increasing the search radius: /findfrozenocean 2000"));
            }
        }
    }
    
    private int[] findNearestFrozenOcean(World world, int centerX, int centerZ, int radius) {
        int foundX = 0;
        int foundZ = 0;
        double minDistance = Double.MAX_VALUE;
        boolean found = false;
        
        int stepSize = 16; // Search in chunk increments
        Random random = new Random();
        
        // Get our custom frozen ocean biome ID
        int frozenOceanBiomeId = com.example.frozenocean.FrozenOceanMod.frozenOcean.biomeID;
        
        for (int r = stepSize; r <= radius; r += stepSize) {
            for (int angle = 0; angle < 360; angle += 10) { // 10 degree steps
                double radians = Math.toRadians(angle);
                int checkX = centerX + (int) (r * Math.cos(radians));
                int checkZ = centerZ + (int) (r * Math.sin(radians));
                
                // Get the biome at this position
                BiomeGenBase biome = world.getBiomeGenForCoords(checkX, checkZ);
                
                // Check specifically for our custom frozen ocean biome
                if (biome != null && biome.biomeID == frozenOceanBiomeId) {
                    double distance = Math.sqrt(Math.pow(checkX - centerX, 2) + Math.pow(checkZ - centerZ, 2));
                    
                    if (distance < minDistance) {
                        minDistance = distance;
                        foundX = checkX;
                        foundZ = checkZ;
                        found = true;
                    }
                }
                
                // Random sampling to improve chances
                if (random.nextInt(20) == 0) {
                    int randX = centerX + random.nextInt(r * 2) - r;
                    int randZ = centerZ + random.nextInt(r * 2) - r;
                    BiomeGenBase randBiome = world.getBiomeGenForCoords(randX, randZ);
                    
                    // Check specifically for our custom frozen ocean biome
                    if (randBiome != null && randBiome.biomeID == frozenOceanBiomeId) {
                        double distance = Math.sqrt(Math.pow(randX - centerX, 2) + Math.pow(randZ - centerZ, 2));
                        
                        if (distance < minDistance) {
                            minDistance = distance;
                            foundX = randX;
                            foundZ = randZ;
                            found = true;
                        }
                    }
                }
            }
            
            // If we found something and we're getting far, break early
            if (found && r > 1000) {
                break;
            }
        }
        
        return found ? new int[]{foundX, foundZ} : null;
    }
    
    private String getDirectionName(int degrees) {
        String[] directions = {"North", "North-East", "East", "South-East", "South", "South-West", "West", "North-West"};
        int index = (int) Math.round(((degrees % 360) / 45.0)) % 8;
        return directions[index];
    }
    
    @Override
    public int getRequiredPermissionLevel() {
        return 0; // Anyone can use this command
    }
}
