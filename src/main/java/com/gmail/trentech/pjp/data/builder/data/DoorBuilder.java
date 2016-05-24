package com.gmail.trentech.pjp.data.builder.data;

import static com.gmail.trentech.pjp.data.DataQueries.DESTINATION;
import static com.gmail.trentech.pjp.data.DataQueries.PRICE;
import static com.gmail.trentech.pjp.data.DataQueries.ROTATION;
import static com.gmail.trentech.pjp.data.DataQueries.BUNGEE;

import java.util.Optional;

import org.spongepowered.api.data.DataView;
import org.spongepowered.api.data.persistence.AbstractDataBuilder;
import org.spongepowered.api.data.persistence.InvalidDataException;

import com.gmail.trentech.pjp.data.object.Door;

public class DoorBuilder extends AbstractDataBuilder<Door> {

    public DoorBuilder() {
        super(Door.class, 1);
    }

    @Override
    protected Optional<Door> buildContent(DataView container) throws InvalidDataException {
        if (container.contains(DESTINATION, ROTATION, PRICE, BUNGEE)) {
        	String destination = container.getString(DESTINATION).get();
        	String rotation = container.getString(ROTATION).get();
        	Double price = container.getDouble(PRICE).get();
        	boolean bungee = container.getBoolean(BUNGEE).get();
        	
            return Optional.of(new Door(destination, rotation, price, bungee));
        }
        
        return Optional.empty();
    }
}