package com.gmail.trentech.pjp.utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.spongepowered.api.service.sql.SqlService;

import com.gmail.trentech.pjp.Main;

import ninja.leaping.configurate.ConfigurationNode;

public abstract class SQLUtils {

    protected static SqlService sql;

    protected static DataSource getDataSource() throws SQLException {
	    if (sql == null) {
	        sql = Main.getGame().getServiceManager().provide(SqlService.class).get();
	    }
	    
        return sql.getDataSource("jdbc:h2:./config/projectportals/data");
	}

	public static void createTables(ConfigurationNode modules) {
		
		try {
			Connection connection = getDataSource().getConnection();
			PreparedStatement statement;
			
			if(modules.getNode("portals").getBoolean()){
				statement = connection.prepareStatement("CREATE TABLE IF NOT EXISTS Portals (Name TEXT, Frame TEXT, Fill TEXT, Destination TEXT, Rotation TEXT, Particle TEXT, Price DOUBLE)");
				statement.executeUpdate();
			}
			if(modules.getNode("buttons").getBoolean()){
				statement = connection.prepareStatement("CREATE TABLE IF NOT EXISTS Buttons (Name TEXT, Destination TEXT, Rotation TEXT, Price DOUBLE)");
				statement.executeUpdate();
			}
			if(modules.getNode("doors").getBoolean()){
				statement = connection.prepareStatement("CREATE TABLE IF NOT EXISTS Doors (Name TEXT, Destination TEXT, Rotation TEXT, Price DOUBLE)");
				statement.executeUpdate();
			}
			if(modules.getNode("levers").getBoolean()){
				statement = connection.prepareStatement("CREATE TABLE IF NOT EXISTS Levers (Name TEXT, Destination TEXT, Rotation TEXT, Price DOUBLE)");
				statement.executeUpdate();
			}
			if(modules.getNode("plates").getBoolean()){
				statement = connection.prepareStatement("CREATE TABLE IF NOT EXISTS Plates (Name TEXT, Destination TEXT, Rotation TEXT, Price DOUBLE)");
				statement.executeUpdate();
			}
			if(modules.getNode("warps").getBoolean()){
				statement = connection.prepareStatement("CREATE TABLE IF NOT EXISTS Warps (Name TEXT, Destination TEXT, Rotation TEXT, Price DOUBLE)");
				statement.executeUpdate();
			}

			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}	
	}
}