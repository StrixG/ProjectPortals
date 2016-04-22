package com.gmail.trentech.pjp.portals;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import com.gmail.trentech.pjp.Main;
import com.gmail.trentech.pjp.utils.Rotation;
import com.gmail.trentech.pjp.utils.SQLUtils;
import com.gmail.trentech.pjp.utils.Utils;

public class Button extends SQLUtils{

	private String name;
	private String destination;
	private Rotation rotation;
	private double price;

	private static ConcurrentHashMap<String, Button> cache = new ConcurrentHashMap<>();
	
	private Button(String name, String destination, Rotation rotation, double price) {
		this.name = name;
		this.destination = destination;
		this.rotation = rotation;
		this.price = price;
	}
	
	public Button(String destination, Rotation rotation, double price) {
		this.destination = destination;
		this.rotation = rotation;
		this.price = price;
	}
	
	public String getName() {
		return name;
	}

	public double getPrice() {
		return price;
	}
	
	public void setPrice(double price){
		this.price = price;
		
		try {
		    Connection connection = getDataSource().getConnection();
		    PreparedStatement statement = connection.prepareStatement("UPDATE Buttons SET Price = ? WHERE Name = ?");

		    statement.setDouble(1, price);
			statement.setString(2, this.name);
			
			statement.executeUpdate();
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public Rotation getRotation() {
		return rotation;
	}

	public void setRotation(Rotation rotation) {
		this.rotation = rotation;
		
		try {
		    Connection connection = getDataSource().getConnection();
		    PreparedStatement statement = connection.prepareStatement("UPDATE Buttons SET Rotation = ? WHERE Name = ?");

		    statement.setString(1, rotation.getName());
			statement.setString(2, this.name);
			
			statement.executeUpdate();
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public Optional<Location<World>> getDestination() {
		String[] args = destination.split(":");
		
		Optional<World> optional = Main.getGame().getServer().getWorld(args[0]);
		
		if(!optional.isPresent()){
			return Optional.empty();
		}
		World world = optional.get();
		
		if(args[1].equalsIgnoreCase("random")){
			return Optional.of(Utils.getRandomLocation(world));
		}else if(args[1].equalsIgnoreCase("spawn")){
			return Optional.of(world.getSpawnLocation());
		}else{
			String[] coords = args[1].split("\\.");
			int x = Integer.parseInt(coords[0]);
			int y = Integer.parseInt(coords[1]);
			int z = Integer.parseInt(coords[2]);
			
			return Optional.of(world.getLocation(x, y, z));	
		}
	}
	
	public void setDestination(String destination){
		this.destination = destination;
		
		try {
		    Connection connection = getDataSource().getConnection();
		    PreparedStatement statement = connection.prepareStatement("UPDATE Buttons SET Destination = ? WHERE Name = ?");

		    statement.setString(1, destination);
			statement.setString(2, this.name);
			
			statement.executeUpdate();
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static Optional<Button> get(Location<World> location) {
		String name = location.getExtent().getName() + ":" + location.getBlockX() + "." + location.getBlockY() + "." + location.getBlockZ();
		
		Optional<Button> optional = Optional.empty();
		
		if(cache.containsKey(name)){
			optional = Optional.of(cache.get(name));
		}
		
		return optional;
	}
	
	public static void remove(Location<World> location) {
		String name = location.getExtent().getName() + ":" + location.getBlockX() + "." + location.getBlockY() + "." + location.getBlockZ();
		
		try {
		    Connection connection = getDataSource().getConnection();
		    
		    PreparedStatement statement = connection.prepareStatement("DELETE from Buttons WHERE Name = ?");
		    
			statement.setString(1, name);
			statement.executeUpdate();
			
			connection.close();
			
			cache.remove(name);
		}catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void save(Location<World> location){
		name = location.getExtent().getName() + ":" + location.getBlockX() + "." + location.getBlockY() + "." + location.getBlockZ();
		
		try {
		    Connection connection = getDataSource().getConnection();
		    
		    PreparedStatement statement = connection.prepareStatement("INSERT into Buttons (Name, Destination, Rotation, Price) VALUES (?, ?, ?, ?)");	
			
		    statement.setString(1, name);
		    statement.setString(2, destination);
		    statement.setString(3, rotation.getName());
		    statement.setDouble(4, price);
		    
			statement.executeUpdate();
			
			connection.close();
			
			cache.put(name, this);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static List<Button> list(){
		List<Button> list = new ArrayList<>();

		try {
		    Connection connection = getDataSource().getConnection();
		    
		    PreparedStatement statement = connection.prepareStatement("SELECT * FROM Buttons");
		    
			ResultSet result = statement.executeQuery();

			while (result.next()) {				
				String name = result.getString("Name");
		    	String destination = result.getString("Destination");
		    	String rotation = result.getString("Rotation");
		    	
		    	if(rotation == null){
		    		rotation = Rotation.EAST.getName();
		    	}

	    		double price = result.getDouble("Price");

				list.add(new Button(name, destination, Rotation.get(rotation).get(), price));
			}
			
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return list;
	}
	
	public static void init(){
		update();
		
		for(Button button : Button.list()){
			Rotation rotation = button.getRotation();
			String[] args = button.destination.split(":");
			
			if(args.length == 3){
				rotation = Rotation.get(args[2]).get();
				button.setDestination(args[0] + ":" + args[1]);
			}
			
			button.setRotation(rotation);
			button.setPrice(button.getPrice());
			
			button.setRotation(rotation);
			button.setPrice(0);
			
			cache.put(button.getName(), button);
		}
	}
	
	public static void update(){
		try {
			Connection connection = getDataSource().getConnection();
			
		    PreparedStatement statement = connection.prepareStatement("ALTER TABLE Buttons ADD IF NOT EXISTS Rotation TEXT");
		    statement.executeUpdate();
		    
		    statement = connection.prepareStatement("ALTER TABLE Buttons ADD IF NOT EXISTS Price DOUBLE");
		    statement.executeUpdate();
		    
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}	  
	}
}
