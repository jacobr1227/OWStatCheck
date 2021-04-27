import java.util.*;
import java.util.regex.*;
import java.io.*;
import java.lang.*;
import java.net.*;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;
public class infoGrabber {
    Map<String,String> heroMap = new HashMap<String,String>();
    Map<String,String> statMap = new HashMap<String,String>();
    String [] heroList;
    {       //listing all of our hero identifiers for HTML parsing
        heroList=new String[] {"All Heroes", "Ana","Ashe","Baptiste","Brigitte","D.Va","Doomfist","Echo","Genji","Hanzo","Junkrat","Lucio","McCree","Mei","Mercy","Moira","Orisa","Pharah","Reaper","Reinhardt","Roadhog","Sigma","Soldier: 76","Sombra","Symmetra","Torbjorn","Tracer","Widowmaker","Winston","Wrecking Ball","Zarya","Zenyatta"};
        heroMap.put("0x02E00000FFFFFFFF","ALL HEROES");
        heroMap.put("0X02E000000000013B","Ana");
        heroMap.put("0X02E0000000000200","Ashe");
        heroMap.put("0X02E0000000000221","Baptiste");
        heroMap.put("0X02E0000000000015","Brigitte");
        heroMap.put("0X02E0000000000195","D.va");
        heroMap.put("0X02E000000000007A","Doomfist");
        heroMap.put("0X02E000000000012F","Echo");
        heroMap.put("0X02E0000000000206","Genji");
        heroMap.put("0X02E0000000000029","Hanzo");
        heroMap.put("0X02E0000000000005","Junkrat");
        heroMap.put("0X02E0000000000065","Lucio");
        heroMap.put("0X02E0000000000079","McCree");
        heroMap.put("0X02E0000000000042","Mei");
        heroMap.put("0X02E00000000000DD","Mercy");
        heroMap.put("0X02E0000000000004","Moira");
        heroMap.put("0X02E00000000001A2","Orisa");
        heroMap.put("0X02E000000000013E","Pharah");
        heroMap.put("0X02E0000000000008","Reaper");
        heroMap.put("0X02E0000000000002","Reinhardt");
        heroMap.put("0X02E0000000000007","Roadhog");
        heroMap.put("0X02E0000000000040","Sigma");
        heroMap.put("0X02E000000000023B","Soldier: 76");
        heroMap.put("0X02E000000000006E","Sombra");
        heroMap.put("0X02E000000000012E","Symmetra");
        heroMap.put("0X02E0000000000006","Torbjorn");
        heroMap.put("0X02E0000000000003","Tracer");
        heroMap.put("0X02E000000000000A","Widowmaker");
        heroMap.put("0X02E0000000000009","Winston");
        heroMap.put("0X02E00000000001CA","Wrecking Ball");
        heroMap.put("0X02E0000000000068","Zarya");
        heroMap.put("0X02E0000000000020","Zenyatta");
                //listing all of our stat identifiers for HTML parsing
        //BEST tab (19 values / 38 pieces)
        statMap.put("All Damage - Most in Game","0x08600000000001E3");
        statMap.put("Barrier Damage - Most in Game","0x086000000000052A");
        statMap.put("Defensive Assists - Most in Game","0x0860000000000319");
        statMap.put("Eliminations - Most in Game","0x08600000000001B9");
        statMap.put("Environmental Kills - Most in Game","0x0860000000000495");
        statMap.put("Final Blows - Most in Game","0x08600000000001E2");
        statMap.put("Healing Done - Most in Game","0x08600000000001E4");
        statMap.put("Hero Damage Done - Most in Game","0x08600000000004BA");
        statMap.put("Kill Streak - Best","0x0860000000000497");
        statMap.put("Melee Final Blows - Most in Game","0x08600000000003ED");
        statMap.put("Multikill - Best","0x0860000000000348");
        statMap.put("Objective Kills - Most in Game","0x0860000000000322");
        statMap.put("Objective Time - Most in Game","0x0860000000000323");
        statMap.put("Offensive Assists - Most in Game","0x086000000000031A");
        statMap.put("Recon Assists - Most in Game","0x086000000000034B");
        statMap.put("Solo Kills - Most in Game","0x086000000000036B");
        statMap.put("Teleporter Pad Destroyed - Most in Game","0x0860000000000496");
        statMap.put("Time Spent on Fire - Most in Game","0x08600000000003CB");
        statMap.put("Turrets Destroyed - Most in Game","0x0860000000000494");
        //ASSISTS tab (4 values / 8 pieces)
        statMap.put("Defensive Assists","0x0860000000000317");
        statMap.put("Healing Done","0x08600000000001D1");
        statMap.put("Offensive Assists","0x0860000000000318");
        statMap.put("Recon Assists","0x086000000000034A");
        //COMBAT tab (14 values / 28 pieces)
        statMap.put("All Damage Done","0x08600000000000C9");
        statMap.put("Barrier Damage Done","0x0860000000000516");
        statMap.put("Damage Done","0x0860000000000621");
        statMap.put("Deaths","0x0860000000000029");
        statMap.put("Eliminations","0x0860000000000025");
        statMap.put("Environmental Kills","0x0860000000000363");
        statMap.put("Final Blows","0x086000000000002C");
        statMap.put("Hero Damage done","0x08600000000004B8");
        statMap.put("Melee Final Blows","0x0860000000000381");
        statMap.put("Multikills","0x0860000000000347");
        statMap.put("Objective Kills","0x0860000000000326");
        statMap.put("Objective Time","0x0860000000000327");
        statMap.put("Solo Kills","0x086000000000002E");
        statMap.put("Time Spent on Fire","0x08600000000003CD");
        //MATCH AWARDS tab (5 values / 10 pieces)
        statMap.put("Cards","0x0860000000000376");
        statMap.put("Medals - Gold","0x0860000000000372");
        statMap.put("Medals","0x0860000000000374");
        statMap.put("Medals - Bronze","0x0860000000000370");
        statMap.put("Medals - Silver","0x0860000000000371");
        //MISCELLANEOUS tab (2 values / 4 pieces)
        statMap.put("Teleporter Pads Destroyed","0x0860000000000329");
        statMap.put("Turrets Destroyed","0x0860000000000491");
        //GAME tab (4 values / 8 pieces)
        statMap.put("Games Lost","0x086000000000042E");
        statMap.put("Games Played","0x0860000000000385");
        statMap.put("Games Won","0x08600000000003F5");
        statMap.put("Time Played","0x0860000000000026");
        //AVERAGE tab (11 values / 22 pieces)
        statMap.put("All Damage Done - Avg per 10 min","0x0860000000000386");
        statMap.put("Barrier Damage Done - Avg per 10 min","0x0860000000000519");
        statMap.put("Deaths - Avg per 10 min","0x08600000000004C3");
        statMap.put("Eliminations - Avg per 10 min","0x08600000000004B0");
        statMap.put("Final Blows - Avg per 10 min","0x08600000000004B1");
        statMap.put("Healing Done - Avg per 10 min","0x08600000000004B2");
        statMap.put("Hero Damage Done - Avg per 10 min","0x08600000000004C1");
        statMap.put("Objective Kills - Avg per 10 min","0x08600000000004B3");
        statMap.put("Objective Time - Avg per 10 min","0x08600000000004B4");
        statMap.put("Solo Kills - Avg per 10 min","0x08600000000004B5");
        statMap.put("Time Spent on Fire - Avg per 10 min","0x08600000000004B6");
    } //both maps are now redundant, but rather than delete them I kept it here as a reference sheet if I needed it
    public ArrayList<String> values = new ArrayList<String>();
    String[] compRates;
    URL url;
    boolean comp;
    public infoGrabber(URL newrl, boolean ctf) {
        url = newrl;
        comp = ctf;
    }
    public void execute() throws IOException {
        //use URL info generated in regular class to find a player page
        if(new File("idump.txt").delete()){System.out.println("Cleared.");} //a safe way to make sure this file gets overwritten rather than appended
        values.clear();
        values.add("All Heroes");
        ReadableByteChannel rbc = Channels.newChannel(url.openStream());
        FileOutputStream fos = new FileOutputStream("idump.txt");
        FileChannel fc = fos.getChannel();
        fc.transferFrom(rbc, 0, Long.MAX_VALUE);
        fos.close();
        String line;
        Stream<String> lines = Files.lines(Paths.get("idump.txt"));
        line = lines.skip(1523).findFirst().get();
        System.out.println(line);
        lines.close();
        String nex = "";
        if(this.comp){
            System.out.println("Progress");
            nex = line;
            Pattern pattern = Pattern.compile("(competitive-rank-level\">\\d{3,4})");
            Matcher matcher = pattern.matcher(nex);
            String[] srs = new String[3];
            int x=0;
            while(matcher.find() && x<3) {
                srs[x] = matcher.group(0);
                x++;
            }
            compRates = new String[] {srs[0],srs[1],srs[2]}; //[0] contains tank, [1] contains DPS, [2] contains support
            nex=line;
            pattern = Pattern.compile("([a-zA-Z]*(-?\\s[a-zA-z]*)*</td><[^>]*>(\\d*:?\\d*))");
            matcher = pattern.matcher(nex);
            String found = "";
            //goal is to replace anything that is NOT (number/word number/word...number/word</td><...>number/decimal/time</td></tr>) with nothing
            while(matcher.find()) {
                found = matcher.group(0);
            }
            nex.replaceAll("</td><td class=\"DataTable-tableColumn\">", ": ");
            String[] stList = nex.split("</td></tr>"); //then we clean it up based on the remaining tags
            int z=0;
            for(int i=0;i<stList.length;i++) {
                System.out.println("Loop!");
                if(stList[i].contains("All Damage Done")) {
                    values.add(heroList[z]);
                    System.out.println(heroList[z]);
                    z++;
                }
                if(z==31) {values.add("Competitive");z=0;}
                values.add(stList[i]); //and add it all into our super array
                System.out.println(stList[i]);
            }
        }
        
    }

    public ArrayList<String> getReturns() { //TODO: implement error catch for nonexistent profiles
        System.out.println(values);
        return values;
    }
    public String[] getComp() {
        return compRates;
    }
    public String[] getHeroes() {
        return heroList;
    }
}
