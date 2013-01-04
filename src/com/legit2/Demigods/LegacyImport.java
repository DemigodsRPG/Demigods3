package com.legit2.Demigods;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import com.legit2.Demigods.Serializable.WriteLocation;

public class LegacyImport
{
	 @SuppressWarnings({ "resource", "unused" })
	 public static boolean loadData(String p) throws FileNotFoundException
	 {
         try
         {
                 File toread = new File("plugins/Demigods/Legacy/"+p+".txt");
                 if ((toread == null) || !toread.exists()) return false;
                 Scanner s = new Scanner(toread);
                 
                 //check name
                 String sname = s.nextLine();
                 if (!sname.split(" ")[1].equals(p)) return false;
                 
                 //alliance
                 String alliance = s.nextLine();
                 DSave.saveData(p, "ALLIANCE", alliance.split(" ")[1]);

                 //favor
                 String favor = s.nextLine();
                 DSave.saveData(p, "FAVOR", Integer.parseInt(favor.split(" ")[1]));
                 
                 //maxfavor
                 String maxfavor = s.nextLine();
                 DSave.saveData(p, "FAVORCAP", Integer.parseInt(maxfavor.split(" ")[1]));
                 
                 //deities //TODO
                 String deity = s.nextLine();
                 String[] deities = deity.split(" ");
                 if (deities.length > 1) {
                         DSave.removeData(p, "DEITIES");
                         DSave.saveData(p, "DEITIES", new ArrayList<String>());
                         for (int i=1;i<deities.length;i++) {
                                 String[] info = deities[i].split(";");
                               //  DUtil.removeDeity(p, info[0]); 
                                 DUtil.setDevotion(p, info[0], Integer.parseInt(info[1]));
                         }
                 }
                 
                 //ascensions
                 String ascensions = s.nextLine();
                 DSave.saveData(p, "ASCENSIONS", Integer.parseInt(ascensions.split(" ")[1]));
                 
                 //kills
                 String kills = s.nextLine();              
                 DSave.saveData(p, "KILLS", Integer.parseInt(kills.split(" ")[1]));
                 
                 //deaths
                 String deaths = s.nextLine();
                 DSave.saveData(p, "DEATHS", Integer.parseInt(deaths.split(" ")[1]));
                 
                 //accessible (guest list)
                 if (s.nextLine().trim().equals("Accessible:"))
                 {
                         String in = s.nextLine();
                         while (!in.trim().equals("Shrines:"))
                         {
                                 String[] info = in.split(" ");
                                 WriteLocation shrine = new WriteLocation(info[4], Integer.parseInt(info[1]), Integer.parseInt(info[2]), Integer.parseInt(info[3]));
                                 //DUtil.removeGuest(shrine, p);
                                 //DUtil.addGuest(shrine, p);
                                 in = s.nextLine();
                         }
                 }
                 String in = s.nextLine();
                 while (in.trim().substring(0, 10).equals("All keys "))
                 {
                         String[] info = in.split(" ");
                         String name = info[0];
                         WriteLocation shrine = new WriteLocation(info[4], Integer.parseInt(info[1]),
                                         Integer.parseInt(info[2]), Integer.parseInt(info[3]));
                         //DUtil.removeShrine(shrine);
                         //DUtil.addShrine(p, name, shrine);
                         if (info.length > 5) {
                                 for (int i=5;i<info.length;i++)
                                 {
                                         //DUtil.addGuest(shrine, DUtil.getPlayer(info[i]));
                                 }
                         }
                         in = s.nextLine();
                 }
                 //shrines
                 DUtil.info("[Demigods] Loaded "+p+"'s data from legacy file.");
                 DDatabase.saveAllPlayerData();
         }
         catch (Exception e)
         {
                 DUtil.warning("[Demigods] Encountered a problem while loading "+p+"'s legacy file.");
                 e.printStackTrace();
                 DUtil.warning("[Demigods] End stack trace.");
         }
         return true;
 }
}