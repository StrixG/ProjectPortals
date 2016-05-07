package com.gmail.trentech.pjp.data;

import org.spongepowered.api.data.DataQuery;
import org.spongepowered.api.data.key.Key;
import org.spongepowered.api.data.key.KeyFactory;
import org.spongepowered.api.data.value.mutable.MapValue;
import org.spongepowered.api.data.value.mutable.Value;

import com.gmail.trentech.pjp.portals.Home;
import com.gmail.trentech.pjp.portals.Sign;

public class Keys {

	public static final Key<Value<Sign>> SIGN = KeyFactory.makeSingleKey(Sign.class, Value.class, DataQuery.of("SIGN"));
	public static final Key<MapValue<String, Home>> HOME_LIST = KeyFactory.makeMapKey(String.class, Home.class, DataQuery.of("HOMES"));
}
