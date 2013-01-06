package com.legit2.Demigods.Libraries.Serializable;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.craftbukkit.libs.com.google.gson.Gson;
import org.bukkit.craftbukkit.libs.com.google.gson.reflect.TypeToken;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/*
 * A serializable ItemStack
 */
public class CardboardBox implements Serializable
{
   private static final long serialVersionUID = 729890133797629668L;
   private static final String CLASS_KEY = "CARDBOARD-BOX-SERIAL-DESERIAL-CLASS";

   public static final ItemStack NO_ITEM = null;

   // JSON String of item
   private final String rawValue;

   public CardboardBox(ItemStack item)
   {
       // Serialize item into a map
       Map<String, Object> serial = item.serialize();

       // Check for item meta
       if(serial.get("meta") != null)
       {

           // We have item meta! Let's do our thing...

           ItemMeta itemmeta = item.getItemMeta(); // Fetch item meta object

           // Serialize item meta
           Map<String, Object> meta = itemmeta.serialize();

           // Copy item meta map so we can edit it (Map<String, Object> meta is IMMUTABLE)
           Map<String, Object> meta2 = new HashMap<String, Object>();
           for(String key : meta.keySet())
           {
               meta2.put(key, meta.get(key));
           }

           // We loop the keys in the item meta, looking for things we can serialize
           for(String key : meta2.keySet())
           {
               Object o = meta2.get(key); // The object

               if(o instanceof ConfigurationSerializable) // Can we serialize?
               {
                   // Yes! Let's do it.
                   ConfigurationSerializable serial1 = (ConfigurationSerializable) o; // Cast for ease of use

                   // Recursively serialize the object
                   Map<String, Object> serialed = recursizeSerialization(serial1);

                   meta2.put(key, serialed); // Re-insert the item, serialized
               }
           }
           serial.put("meta", meta2); // Re-insert the item meta
       }

       // Convert the item to JSON
       Gson gson = new Gson();
       this.rawValue = gson.toJson(serial);
   }

   @SuppressWarnings ("unchecked")
   public ItemStack unBox()
   {
       // Decode the item from JSON
       Gson gson = new Gson();
       Map<String, Object> keys = gson.fromJson(this.rawValue, new TypeToken<Map<String, Object>>() {}.getType());

       // Repair Gson thinking int == double
       if(keys.get("amount") != null)
       {
           Double d = (Double) keys.get("amount");
           Integer i = d.intValue();
           keys.put("amount", i);
       }

       // Create item
       ItemStack item;
       try
       {
           item = ItemStack.deserialize(keys);
       }
       catch(Exception e)
       {
           return NO_ITEM; // NO_ITEM == null, shouldn't happen
       }
       if(item == null)
       {
           return NO_ITEM; // NO_ITEM == null, shouldn't happen
       }

       // Handle item meta
       if(keys.containsKey("meta"))
       {
           Map<String, Object> itemmeta = (Map<String, Object>) keys.get("meta");
           // Convert doubles -> ints
           itemmeta = recursiveDoubleToInteger(itemmeta);

           // Deserilize everything
           itemmeta = recursiveDeserialization(itemmeta);

           // Create the Item Meta
           ItemMeta meta = (ItemMeta) ConfigurationSerialization.deserializeObject(itemmeta, ConfigurationSerialization.getClassByAlias("ItemMeta"));

           // Assign the item meta
           item.setItemMeta(meta);
       }

       return item;
   }

   // Recursive Methods

   private Map<String, Object> recursizeSerialization(ConfigurationSerializable o)
   {
       // Serialize the object to start off with
       Map<String, Object> map = o.serialize();

       // Copy map, as it's immutable
       Map<String, Object> map2 = new HashMap<String, Object>();
       for(String key : map.keySet())
       {
           Object o2 = map.get(key);
           if(o2 instanceof ConfigurationSerializable)
           {
               // We have a serializable object!
               ConfigurationSerializable serialObj = (ConfigurationSerializable) o2;

               // Recursively serialize that too
               Map<String, Object> newMap = recursizeSerialization(serialObj);

               // Insert deserialization class key
               newMap.put(CLASS_KEY, ConfigurationSerialization.getAlias(serialObj.getClass()));

               // Insert the map in place of 'o2'
               map2.put(key, newMap);
           }
           else
           {
               map2.put(key, o2); // Else: We can't serialize it, insert into the 'new' map
           }
       }
       // Used in deserialization, this is the class off the passed 'o' object
       map2.put(CLASS_KEY, ConfigurationSerialization.getAlias(o.getClass()));

       return map2; // return the 'new' map (fully serialized)
   }

   private Map<String, Object> recursiveDoubleToInteger(Map<String, Object> map)
   {
       // We copy the map
       Map<String, Object> map2 = new HashMap<String, Object>();
       for(String key : map.keySet())
       {
           Object o = map.get(key);
           if(o instanceof Double)
           {
               // Convert Double -> Int
               Double d = (Double) o;
               Integer i = d.intValue();
               map2.put(key, i);
           }
           else if(o instanceof Map) // We have a map, we assume it's a serialized object
           { 
               @SuppressWarnings ("unchecked")
               Map<String, Object> map3 = (Map<String, Object>) o;
               map2.put(key, recursiveDoubleToInteger(map3)); // Doubles -> Ints
           }
           else
           {
               map2.put(key, o); // Else: Insert the object as-is
           }
       }
       return map2;
   }

   private Map<String, Object> recursiveDeserialization(Map<String, Object> map)
   {
       // Copy map, immutable should be passed into this method
       Map<String, Object> map2 = new HashMap<String, Object>();
       for(String key : map.keySet())
       {
           Object o = map.get(key);
           if(o instanceof Map)
           {
               // Attempt to deserialize
               @SuppressWarnings ("unchecked")
               Map<String, Object> map3 = (Map<String, Object>) o;
               if(map3.containsKey(CLASS_KEY))
               {
                   String alias = (String) map3.get(CLASS_KEY);
                   Object deserialed = ConfigurationSerialization.deserializeObject(map3, ConfigurationSerialization.getClassByAlias(alias));
                   map2.put(key, deserialed);
               }// else: ignore, nothing we can do
           }
           else
           {
               map2.put(key, o); // Else: Insert the object as-is
           }
       }
       return map2;
   }

}