import java.util.*;
import java.io.*;
import java.net.*;
public class infoGrabber {
    Map<String,String> heroMap = new HashMap<String,String>();
    Map<String,String> statMap = new HashMap<String,String>();
    {       //listing all of our hero identifiers for HTML parsing
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
    }
    ArrayList<String> values = new ArrayList<String>();
    String[] compRates = new String[3];
    URL url;
    boolean comp;
    public infoGrabber(URL newrl, boolean ctf) {
        url = newrl;
        comp = ctf;
    }
    public void execute() throws IOException {
        //use URL info generated in regular class to find a player page
        values.clear();
        values.add("All Heroes");
        URLConnection uco = url.openConnection();
        InputStream is = uco.getInputStream();
        int c=0,x=0,index=0;
        BufferedReader re = new BufferedReader(new InputStreamReader(is));
        String nex = null;
        boolean breakout = false; //using this instead of break for more precision on which loops to drop out of
        //proceed to loop through and ignore all of the garbage
        //looking for a specific spot
        if(comp){
            while((nex=re.readLine())!= null && !breakout) {
                if(nex.contains("<div class=\"competitive-rank-level\">")) {
                    compRates[c] = nex.substring(35,nex.length()-5); //[0] contains tank, [1] contains DPS, [2] contains support
                    System.out.println(compRates[c]);
                    c++;
                }//at the top of the page, grabs the three Competitive scores.
                if(nex.contains("<div id=\"competitive\" data-js=\"career-category\" data-mode=\"competitive\"")) {
                    System.out.println("It worked!");
                    breakout=true;
                }//break out of this loop after we reach the information we want
            }// first loop ends upon reaching "competitive". Now we can run for a certain amount of lines and find our data
            breakout=false;
            while((nex=re.readLine()) != null) {
                //first we check for lines containing any of our StatIDs
                boolean there = false;
                Iterator smIt = statMap.entrySet().iterator();
                while(smIt.hasNext()&&!breakout) {
                    Map.Entry statElement = (Map.Entry)smIt.next();
                    String id = (String)statElement.getValue();
                    if(nex.contains(id)) {
                        there=true;
                        breakout=true;
                    }
                } //loops through the map of IDs and checks if any of them are present on this line. Slow, but effective.
                if(there) {
                    x++;
                    values.add(re.readLine().trim().substring(33)); 
                    //removing garbage...
                    index=values.get(x).indexOf("<");
                    values.set(x, values.get(x).substring(0,index)); //resets own value to not include the ending tags, cleans up HTML
                    x++;
                    values.add(re.readLine().trim().substring(33));
                    index=values.get(x).indexOf("<");
                    values.set(x,values.get(x).substring(0,index));
                } //should it find an ID, add the next two lines to our accumulator, which contain the number and an identifier, but also removes white space and all the data before the values
                if(nex.contains("<div class=\"row js-stats toggle-display gutter-18@md spacer-12 spacer-18@md\" data-group-id=\"stats\"")) {
                    index=nex.indexOf("0x02E");
                    String heroLocation = nex.substring(index, index+18);
                    values.add(heroMap.get(heroLocation));
                }//checks for hero names to make sure our data is identifiable later
                if(nex.contains("<section id=\"achievements-section\"")) {
                    break;
                } //break out when reaching the end of this section
            }// second loop grabs all the useful information using the Maps we made
        }
        breakout=false;
        if(!comp){
            while((nex=re.readLine()) != null&&!breakout) {
                if(nex.contains("<div id=\"quickplay\" data-js=\"career-category\" data-mode=\"quickplay\"")) {
                    breakout=true;
                } //once we reach the information we want, break this loop
            } //first loop simply chugs through the top of the page to reach the important stuff to save time
            breakout=false;
            while((nex=re.readLine()) != null) {
                boolean there = false;
                Iterator smIt = statMap.entrySet().iterator();
                while(smIt.hasNext()&&!breakout) {
                    Map.Entry statElement = (Map.Entry)smIt.next();
                    String id = (String)statElement.getValue();
                    if(nex.contains(id)) {
                        there=true;
                        breakout=true;
                    }
                } //loops through the map of IDs and checks if any of them are present on this line. Slow, but effective.
                if(there) {
                    x++;
                    values.add(re.readLine().trim().substring(33)); 
                    //removing garbage...
                    index=values.get(x).indexOf("<");
                    values.set(x, values.get(x).substring(0,index)); //resets own value to not include the ending tags, cleans up HTML
                    x++;
                    values.add(re.readLine().trim().substring(33));
                    index=values.get(x).indexOf("<");
                    values.set(x,values.get(x).substring(0,index));
                } //should it find an ID, add the next two lines to our accumulator, which contain the number and an identifier, but also removes white space and all the data before the values
                if(nex.contains("<div class=\"row js-stats toggle-display gutter-18@md spacer-12 spacer-18@md\" data-group-id=\"stats\"")) {
                    index=nex.indexOf("0x02E");
                    String heroLocation = nex.substring(index, index+18);
                    values.add(heroMap.get(heroLocation));
                }//checks for hero names to make sure our data is identifiable later

                if(nex.contains("<div id=\"competitive\" data-js=\"career-category\" data-mode=\"competitive\"")) {
                    break; //break out when reaching the end of this section
                }
            }//second loop grabs useful information for us using the Maps we made.
        }
        
    }

    public ArrayList<String> getReturns() { //TODO: implement error catch for nonexistent profiles
        return values;
    }
    public String[] getComp() {
        return compRates;
    }
    public Map<String,String> getStatMap() {
        return statMap;
    }

    public Map<String,String> getHeroMap() {
        return heroMap;
    }
}
