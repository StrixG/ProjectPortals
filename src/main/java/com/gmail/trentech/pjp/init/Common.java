package com.gmail.trentech.pjp.init;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.gmail.trentech.pjc.core.ConfigManager;
import com.gmail.trentech.pjc.core.SQLManager;
import com.gmail.trentech.pjc.help.Argument;
import com.gmail.trentech.pjc.help.Help;
import com.gmail.trentech.pjc.help.Usage;
import com.gmail.trentech.pjp.Main;

import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;

public class Common {

	public static void init() {
		initConfig();
		initHelp();
		initData();
	}
	
	public static void initData() {
		try {
			SQLManager sqlManager = SQLManager.get(Main.getPlugin());
			Connection connection = sqlManager.getDataSource().getConnection();

			PreparedStatement statement = connection.prepareStatement("CREATE TABLE IF NOT EXISTS " + sqlManager.getPrefix("PORTALS") + " (Name TEXT, Data MEDIUMBLOB)");
			statement.executeUpdate();

			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void initHelp() {
		ConfigurationNode modules = ConfigManager.get(Main.getPlugin()).getConfig().getNode("settings", "modules");
		
		Usage usagePortal = new Usage(Argument.of("<destination>", "Specifies a world or server if [-b] is supplied"))
				.addArgument(Argument.of("[-b]", "Specifies that <destination> is a bungee connected server"))
				.addArgument(Argument.of("[-f]", "Skips safe location check. Has no effect with '-c random' or '-c bed'"))
				.addArgument(Argument.of("[-c <x,y,z>]", "Specifies the coordinates to set spawn to. Other valid arguments are \"random\",\"bed\" and \"last\". x and z must fall within the range -30,000,000 to 30,000,000 "
						+ ", and y must be within the range -4096 to 4096 inclusive. This is ignored if [-b] is supplied"))
				.addArgument(Argument.of("[-d <direction>]", "Specifies the direction player will face upon teleporting. The following can be used: NORTH, NORTH_WEST, WEST, SOUTH_WEST, SOUTH, SOUTH_EAST, EAST, NORTH_EAST"));

		if (modules.getNode("buttons").getBoolean()) {
			Help button = new Help("button", "button", "Use this command to create a button that will teleport you to other worlds")
					.setPermission("pjp.cmd.button")
					.setUsage(usagePortal)
					.addExample("/button MyWorld -c random")
					.addExample("/button MyWorld -c -100,65,254 -d south")
					.addExample("/button MyWorld -d southeast")
					.addExample("/button MyWorld -c bed")
					.addExample("/button MyWorld");
			
			Help.register(button);
		}
		if (modules.getNode("doors").getBoolean()) {
			Help door = new Help("door", "door", "Use this command to create a door that will teleport you to other worlds")
				    .setPermission("pjp.cmd.door")
					.setUsage(usagePortal)
					.addExample("/door MyWorld -c random")
					.addExample("/door MyWorld -c -100,65,254 -d south")
					.addExample("/door MyWorld -d southeast")
					.addExample("/door MyWorld -c -100,65,254")
					.addExample("/door MyWorld");
			
			Help.register(door);				
		}
		if (modules.getNode("plates").getBoolean()) {
			Help plate = new Help("plate", "plate", "Use this command to create a pressure plate that will teleport you to other worlds")
				    .setPermission("pjp.cmd.plate")
					.setUsage(usagePortal)
					.addExample("/plate MyWorld -c random")
					.addExample("/plate MyWorld -c -100,65,254 -d south")
					.addExample("/plate MyWorld -d southeast")
					.addExample("/plate MyWorld -c -100,65,254")
					.addExample("/plate MyWorld");
			
			Help.register(plate);
		}
		if (modules.getNode("signs").getBoolean()) {
			Help sign = new Help("sign", "sign", "Use this command to create a sign that will teleport you to other worlds")
				    .setPermission("pjp.cmd.sign")
					.setUsage(usagePortal)
					.addExample("/sign MyWorld -c random")
					.addExample("/sign MyWorld -c -100,65,254 -d south")
					.addExample("/sign MyWorld -d southeast")
					.addExample("/sign MyWorld -c -100,65,254")
					.addExample("/sign MyWorld");
			
			Help.register(sign);
		}
		if (modules.getNode("levers").getBoolean()) {
			Help lever = new Help("lever", "lever", "Use this command to create a lever that will teleport you to other worlds")
				    .setPermission("pjp.cmd.lever")
					.setUsage(usagePortal)
					.addExample("/lever MyWorld -c random")
					.addExample("/lever MyWorld -c -100,65,254 -d south")
					.addExample("/lever MyWorld -d southeast")
					.addExample("/lever MyWorld -c -100,65,254")
					.addExample("/lever MyWorld");
			
			Help.register(lever);
		}			
		if (modules.getNode("portals").getBoolean()) {
			Usage usageCreate = new Usage(Argument.of("<name>", "Specifies the name of the new portal"))
					.addArgument(Argument.of("<destination>", "Specifies a world or server if argument [-b] is supplied"))
					.addArgument(Argument.of("[-b]", "Specifies that <destination> is a bungee connected server"))
					.addArgument(Argument.of("[-f]", "Skips safe location check. Has no effect with '-c random' or '-c bed'"))
					.addArgument(Argument.of("[-c <x,y,z>]", "Specifies the coordinates to set spawn to. x and z must fall within the range -30,000,000 to 30,000,000 (exclusive, without the "
							+ "commas), and y must be within the range -4096 to 4096 inclusive. This is ignored if [-b] is supplied"))
					.addArgument(Argument.of("[-d <direction>]", "Specifies the direction player will face upon teleporting. The following can be used: NORTH, NORTH_WEST, WEST, SOUTH_WEST, SOUTH, SOUTH_EAST, EAST, NORTH_EAST"));

			Help portalCreate = new Help("portal create", "create", "Use this command to create a portal that will teleport you to other worlds")
					.setPermission("pjp.cmd.portal.create")
					.setUsage(usageCreate)
					.addExample("/portal create MyPortal MyWorld -c -100,65,254")
					.addExample("/portal create MyPortal MyWorld -c C:random")
					.addExample("/portal create MyPortal MyWorld -c -100,65,254 -d south")
					.addExample("/portal create MyPortal MyWorld -d southeast")
					.addExample("/portal create MyPortal MyWorld");
			
			Usage usageDestination = new Usage(Argument.of("<name>", "Specifies the name of the targeted portal"))
					.addArgument(Argument.of("<destination>", "Specifies a world or server if is bungee portal"))
					.addArgument(Argument.of("[x,y,z]", "Specifies the coordinates to set spawn to. x and z must fall within the range -30,000,000 to 30,000,000 (exclusive, without the "
							+ "commas), and y must be within the range -4096 to 4096 inclusive. This is ignored if is bungee portal"));
			
			Help portalDestination = new Help("portal destination", "destination", "Change as existing portals destination")
					.setPermission("pjp.cmd.portal.destination")
					.setUsage(usageDestination)
					.addExample("/portal destination Skyland 100,65,400")
					.addExample("/portal destination Server1")
					.addExample("/portal destination MyPortal DIM1");
			
			Usage usageDirection = new Usage(Argument.of("<name>", "Specifies the name of the targeted portal"))
					.addArgument(Argument.of("<direction>", "Specifies the direction player will face upon teleporting. The following can be used: NORTH, NORTH_WEST, WEST, SOUTH_WEST, SOUTH, SOUTH_EAST, EAST, NORTH_EAST"));
			
			Help portalDirection = new Help("portal direction", "direction", "Change as existing portals spawn direction")
					.setPermission("simplyportals.cmd.portal.direction")
					.setUsage(usageDirection)
					.addExample("/portal direction Skyland NORTH");
			
			Help portalList = new Help("portal list", "list", "List all portals")
					.setPermission("pjp.cmd.portal.list");
			
			Usage usageParticle = new Usage(Argument.of("<name>", "Specifies the name of the targeted portal"))
					.addArgument(Argument.of("<particleType>", "Specifies the ParticleType"))
					.addArgument(Argument.of("<intensity>", "Specifies the intensity of the particles spawn. The lower the value the more particles spawn. WARNING: too low could produce client lag"))
					.addArgument(Argument.of("[<particleOption>", "Specifies a compatible particle option. color, block state, direction etc.."))
					.addArgument(Argument.of("<value>]", "Specifies the particle option value"));
			
			Help portalParticle = new Help("portal particle", "particle", "Change a portals particle effect.")
					.setPermission("pjp.cmd.portal.particle")
					.setUsage(usageParticle)
					.addExample("/portal particle MyPortal minecraft:redstone_dust 40 minecraft:color BLUE");
			
			Usage usageBlock = new Usage(Argument.of("<name>", "Specifies the name of the targeted portal"))
					.addArgument(Argument.of("<blockType>[:damageValue]", "Specifies the BlockType. Add unsafe damage value for block varients, such as colored wool."));
			
			Help portalBlock = new Help("portal block", "block", "Change a portals center block type. Useful for non-solid block types such as water, lava and portal blocks. This can be combined with particles")
					.setPermission("pjp.cmd.portal.block")
					.setUsage(usageBlock)
					.addExample("/portal block MyPortal minecraft:stained_glass_pane:1")
					.addExample("/portal block MyPortal minecraft:portal");
			
			Usage usagePrice = new Usage(Argument.of("<name>", "Specifies the name of the targeted portal"))
					.addArgument(Argument.of("<price>", "Specifies a price player will be charged for using portal"));
			
			Help portalPrice = new Help("portal price", "price", "Charge players for using portals. 0 to disable")
					.setPermission("pjp.cmd.portal.price")
					.setUsage(usagePrice)
					.addExample("/portal price MyPortal 0")
					.addExample("/portal price MyPortal 50");	
			
			Usage usageCommand = new Usage(Argument.of("<name>", "Specifies the name of the targeted portal"))
					.addArgument(Argument.of("<command>", "Specifies the command that will execute when using a portal, beginning with 'C:' or 'P:' to specify if command will run as Player or Console"));
			
			Help portalCommand = new Help("portal command", "command", "Run a command when using a portal.")
					.setPermission("pjp.cmd.portal.command")
					.setUsage(usageCommand)
					.addExample("/portal command MyPortal P:kill all")
					.addExample("/portal command MyPortal C:give Notch minecraft:apple");
			
			Usage usagePermission = new Usage(Argument.of("<name>", "Specifies the name of the targeted portal"))
					.addArgument(Argument.of("<permission>", "Specifies the permission that is required to use portal"));
			
			Help portalPermission = new Help("portal permission", "permission", "Sets a permission node that is required to use portal.")
					.setPermission("pjp.cmd.portal.permission")
					.setUsage(usagePermission)
					.addExample("/portal permission MyPortal perm.node");
			
			Usage usageRemove = new Usage(Argument.of("<name>", "Specifies the name of the targeted portal"));
					
			Help portalRemove = new Help("portal remove", "remove", "Remove an existing portal")
					.setPermission("pjp.cmd.portal.remove")
					.setUsage(usageRemove)
					.addExample("/portal remove MyPortal");
			
			Usage usageRename = new Usage(Argument.of("<oldName>", "Specifies the name of the targeted portal"))
					.addArgument(Argument.of("<newName>", "Specifies the new name of the portal"));
			
			Help portalRename = new Help("portal rename", "rename", "Rename portal")
					.setPermission("pjp.cmd.portal.rename")
					.setUsage(usageRename)
					.addExample("/portal rename MyPortal ThisPortal");
				
			Help portalSave = new Help("portal save", "save", "Saves generated portal")
					.setPermission("pjp.cmd.portal.save");
			
			Help portal = new Help("portal", "portal", " Top level portal command")
					.setPermission("pjp.cmd.portal")
					.addChild(portalSave)
					.addChild(portalPermission)
					.addChild(portalCommand)
					.addChild(portalDirection)
					.addChild(portalRename)
					.addChild(portalBlock)
					.addChild(portalRemove)
					.addChild(portalPrice)
					.addChild(portalParticle)
					.addChild(portalList)
					.addChild(portalDestination)
					.addChild(portalCreate);
			
			Help.register(portal);
		}
		
		if (modules.getNode("homes").getBoolean()) {
			Usage usageCreate = new Usage(Argument.of("<name>", "Specifies the name of the new home"))
					.addArgument(Argument.of("[-f]", "Skips safe location check. Has no effect with '-c random' or '-c bed'"));
			
			Help homeCreate = new Help("home create", "create", "Create a new home")
					.setPermission("pjp.cmd.home.create")
					.setUsage(usageCreate)
					.addExample("/home create MyHome");
			
			Help homeList = new Help("home list", "list", "List all homes")
					.setPermission("pjp.cmd.home.list");
			
			Usage usageRemove = new Usage(Argument.of("<name>", "Specifies the name of the targeted home"));
			
			Help homeRemove = new Help("home remove", "remove", "Remove an existing home")
					.setPermission("pjp.cmd.home.remove")
					.setUsage(usageRemove)
					.addExample("/home remove OldHome");
			
			Usage usageRename = new Usage(Argument.of("<oldName>", "Specifies the name of the targeted home"))
					.addArgument(Argument.of("<newName>", "Specifies the new name of the home"));
			
			Help homeRename = new Help("home rename", "rename", "Rename home")
					.setPermission("pjp.cmd.home.rename")
					.setUsage(usageRename)
					.addExample("/home rename MyHome Castle");
			
			Help home = new Help("home", "home", " Top level home command")
					.setPermission("pjp.cmd.home")
					.addChild(homeRename)
					.addChild(homeRemove)
					.addChild(homeList)
					.addChild(homeCreate);
			
			Help.register(home);
		}
		if (modules.getNode("warps").getBoolean()) {
			Usage usagecreate = new Usage(Argument.of("<name>", "Specifies the name of the new warp point"))
					.addArgument(Argument.of("<destination>", "Specifies a world or server if [-b] is supplied"))
					.addArgument(Argument.of("[-f]", "Skips safe location check. Has no effect with '-c random' or '-c bed'"))
					.addArgument(Argument.of("[-b]", "Specifies that <destination> is a bungee connected server"))
					.addArgument(Argument.of("[-c <x,y,z>]", "Specifies the coordinates to set spawn to. x and z must fall within the range -30,000,000 to 30,000,000 (exclusive, without the "
							+ "commas), and y must be within the range -4096 to 4096 inclusive. This is ignored if [-b] is supplied"))
					.addArgument(Argument.of("[-d <direction>]", "Specifies the direction player will face upon teleporting. The following can be used: NORTH, NORTH_WEST, WEST, SOUTH_WEST, SOUTH, SOUTH_EAST, EAST, NORTH_EAST"))
					.addArgument(Argument.of("[-p <price>]", "Specifies a price player will be charged for using this warp"))
					.addArgument(Argument.of("[-s <command>]", "Specifies a command to execute when using portal"))
					.addArgument(Argument.of("[n <permission>]", "Allow you to assign a custom permission node to a portal. If no permission is provided everyone will have access."));
			
			Help warpCreate = new Help("warp create", "create", "Use this command to create a warp that will teleport you to other worlds")
					.setPermission("pjp.cmd.warp.create")
					.setUsage(usagecreate)
					.addExample("/warp create Lobby MyWorld")
					.addExample("/warp create Lobby MyWorld -c -100,65,254")
					.addExample("/warp create Random MyWorld -c random")
					.addExample("/warp create Lobby MyWorld -c -100,65,254 -d south")
					.addExample("/warp create Lobby MyWorld -d southeast")
					.addExample("/warp create Lobby");
			
			Help warpList = new Help("warp list", "list", "List all warp points")
					.setPermission("pjp.cmd.warp.list");
			
			Usage usagePrice = new Usage(Argument.of("<name>", "Specifies the name of the targeted warp point"))
					.addArgument(Argument.of("<price>", "Specifies a price player will be charged for using this warp"));
			
			Help warpPrice = new Help("warp price", "price", "Charge players for using warps. 0 to disable")
					.setPermission("pjp.cmd.warp.price")
					.setUsage(usagePrice)
					.addExample("/warp price Lobby 0")
					.addExample("/warp price Lobby 50");
			
			Usage usageRemove = new Usage(Argument.of("<name>", "Specifies the name of the targeted warp point"));
					
			Help warpRemove = new Help("warp remove", "remove", "Remove an existing  warp point")
					.setPermission("pjp.cmd.warp.remove")
					.setUsage(usageRemove)
					.addExample("/warp remove OldSpawn");
			
			Usage usageRename = new Usage(Argument.of("<oldName>", "Specifies the name of the targeted warp point"))
					.addArgument(Argument.of("<newName>", "Specifies the new name of the warp point"));
			
			Help warpRename = new Help("warp rename", "rename", "Rename warp")
					.setPermission("pjp.cmd.warp.rename")
					.setUsage(usageRename)
					.addExample("/warp rename Spawn Lobby");
			
			Help warp = new Help("warp", "warp", " Top level warp command")
					.setPermission("pjp.cmd.warp")
					.addChild(warpRename)
					.addChild(warpRemove)
					.addChild(warpPrice)
					.addChild(warpList)
					.addChild(warpCreate);
			
			Help.register(warp);
		}
	}
	
	public static void initConfig() {
		ConfigManager configManager = ConfigManager.init(Main.getPlugin());
		CommentedConfigurationNode config = configManager.getConfig();

		if (config.getNode("options", "portal", "size").isVirtual()) {
			config.getNode("options", "portal", "size").setValue(100).setComment("Maximum number of blocks a portal can use");
		}
		if (config.getNode("options", "portal", "teleport_item").isVirtual()) {
			config.getNode("options", "portal", "teleport_item").setValue(true).setComment("Toggle if portals can teleport items");
		}
		if (config.getNode("options", "portal", "teleport_mob").isVirtual()) {
			config.getNode("options", "portal", "teleport_mob").setValue(true).setComment("Toggle if portals can teleport mobs");
		}
		if (config.getNode("options", "homes").isVirtual()) {
			config.getNode("options", "homes").setValue(5).setComment("Default number of homes a player can have");
		}
		if (config.getNode("options", "particles").isVirtual()) {
			config.getNode("options", "particles").setComment("Particle effect settings");
			config.getNode("options", "particles", "enable").setValue(true).setComment("Enable particle effects");
			config.getNode("options", "particles", "portal", "type").setValue("minecraft:witch_spell").setComment("Default particle type for portals");
			config.getNode("options", "particles", "portal", "option", "type").setValue("none").setComment("Default ParticleOption type if supported, otherwise set \"NONE\"");
			config.getNode("options", "particles", "portal", "option", "value").setValue("none").setComment("Default ParticleOption value if supported, otherwise set \"NONE\"");
			config.getNode("options", "particles", "teleport", "type").setValue("minecraft:redstone_dust").setComment("Default particle type when teleporting");
			config.getNode("options", "particles", "teleport", "option", "type").setValue("minecraft:color").setComment("Default ParticleOption type if supported, otherwise set \"NONE\"");
			config.getNode("options", "particles", "teleport", "option", "value").setValue("blue").setComment("Default ParticleOption value if supported, otherwise set \"NONE\"");
			config.getNode("options", "particles", "creation", "type").setValue("minecraft:witch_spell").setComment("Default particle type when creating any kind of portal");
			config.getNode("options", "particles", "creation", "option", "type").setValue("none").setComment("Default ParticleOption type if supported, otherwise set \"NONE\"");
			config.getNode("options", "particles", "creation", "option", "value").setValue("none").setComment("Default ParticleOption value if supported, otherwise set \"NONE\"");
		}
		if (config.getNode("options", "random_spawn_radius").isVirtual()) {
			config.getNode("options", "random_spawn_radius").setValue(5000).setComment("World radius for random spawn portals.");
		}
		if (config.getNode("options", "teleport_message").isVirtual()) {
			config.getNode("options", "teleport_message").setComment("Set message that displays when player teleports.");
			// UPDATE CONFIG
			if (config.getNode("options", "teleport_message", "enable").isVirtual()) {
				config.getNode("options", "teleport_message", "enable").setValue(true);
			}
			config.getNode("options", "teleport_message", "title").setValue("&2%WORLD%");
			config.getNode("options", "teleport_message", "sub_title").setValue("&bx: %X%, y: %Y%, z: %Z%");
		}
		if (config.getNode("settings", "modules").isVirtual()) {
			config.getNode("settings", "modules").setComment("Toggle on and off specific features");
		}
		if (config.getNode("settings", "modules", "portals").isVirtual()) {
			config.getNode("settings", "modules", "portals").setValue(true);
		}
		if (config.getNode("settings", "modules", "buttons").isVirtual()) {
			config.getNode("settings", "modules", "buttons").setValue(false);
		}
		if (config.getNode("settings", "modules", "doors").isVirtual()) {
			config.getNode("settings", "modules", "doors").setValue(false);
		}
		if (config.getNode("settings", "modules", "plates").isVirtual()) {
			config.getNode("settings", "modules", "plates").setValue(false);
		}
		if (config.getNode("settings", "modules", "levers").isVirtual()) {
			config.getNode("settings", "modules", "levers").setValue(false);
		}
		if (config.getNode("settings", "modules", "signs").isVirtual()) {
			config.getNode("settings", "modules", "signs").setValue(false);
		}
		if (config.getNode("settings", "modules", "warps").isVirtual()) {
			config.getNode("settings", "modules", "warps").setValue(false);
		}
		if (config.getNode("settings", "modules", "homes").isVirtual()) {
			config.getNode("settings", "modules", "homes").setValue(false);
		}
		if (config.getNode("settings", "modules", "back").isVirtual()) {
			config.getNode("settings", "modules", "back").setValue(true);
		}
		if (config.getNode("settings", "sql", "database").isVirtual()) {
			config.getNode("settings", "sql", "database").setValue(Main.getPlugin().getId());
		}
		
		configManager.save();
	}
}
