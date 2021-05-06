import java.util.*;
import java.util.regex.*;
import java.io.*;
import java.net.*;
import java.nio.channels.Channels; //importing the nio libraries for easy file acquisition and intake.
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;
public class infoGrabber {
    String [] heroList;
    {       //listing all of our hero identifiers for working thorough the HTML to get data separated
        heroList=new String[] {"All Heroes", "Ana","Ashe","Baptiste","Bastion","Brigitte","D.Va","Doomfist","Echo","Genji","Hanzo","Junkrat","Lúcio","McCree","Mei","Mercy","Moira","Orisa","Pharah","Reaper","Reinhardt","Roadhog","Sigma","Soldier: 76","Sombra","Symmetra","Torbjörn","Tracer","Widowmaker","Winston","Wrecking Ball","Zarya","Zenyatta"};
    }
    public ArrayList<String> values = new ArrayList<String>();  //our results list
    public ArrayList<String> heroConfirm = new ArrayList<String>(); //tools for backchecking data to make sure it's all there and skip unnecessary characters.
    public ArrayList<String> compConfirm = new ArrayList<String>();
    public String[] compRates;
    URL url;
    public infoGrabber(URL newrl) { //the constructor. Kept separate from the rest for better timing of the execution in the main
        url = newrl;
    }
    public void execute() throws IOException {  //our core function, this does most of the work here
        //use URL info generated in regular class to find a player page
        if(new File("idump.txt").delete()){System.out.println("Cleared.");} //a safe way to make sure this file gets overwritten rather than appended
        values.clear(); //emptying this just in case someone decides to reuse the program. May be a little unstable though.
        ReadableByteChannel rbc = Channels.newChannel(url.openStream());    //using nio to get our html file
        FileOutputStream fos = new FileOutputStream("idump.txt");   //and dumping it to local
        FileChannel fc = fos.getChannel();
        fc.transferFrom(rbc, 0, Long.MAX_VALUE);
        fos.close();
        String line;
        Stream<String> lines = Files.lines(Paths.get("idump.txt"));
        line = lines.skip(1522).findFirst().get();  //So the first 1,522 lines are entirely junk. The actual .site tag that hold all of our data is all on line 1523
        lines.close();
        String nex = "";

        nex = line; //we'll be redoing this a couple times, but this now holds ALL of line 1523. Which is really big. I do this more for comfort than necessity
        Pattern pattern = Pattern.compile("(competitive-rank-level\">\\d{3,4})"); //don't use regex for HTML, they said
        Matcher matcher = pattern.matcher(nex); //but did I listen?
        String[] srs = new String[3];   //no.
        int x=0;
        while(matcher.find() && x<3) {  //our first data: this is the player's competitive ranks. Hopefully they have some, because I feel this will break if they don't
            srs[x] = matcher.group(0);
            srs[x] = srs[x].replaceAll("competitive-rank-level\">", "");
            x++;
        }
        pattern = Pattern.compile("(0x02E\\d*\\w*)\"\\soption-id=\"([a-zA-Z]*\\s?.?\\s?[a-zA-Z0-9]*)"); //the worst regex of them all
        matcher = pattern.matcher(nex);
        int cycle = 0;
        int loca = 0;
        while(matcher.find()) { //this is the simpler of two regex issues
            // this mess finds hero names in the list, and adds them to a list that I double check against the master list so that empty heroes don't mess with the rest of the data. Looking at you, Lucio.
            if(matcher.group(0).contains("ALL HEROES")) {
                cycle++;
                loca=0;
            }
            if(cycle==1) {
                heroConfirm.add(matcher.group(0).replaceAll("(0x02E\\d*\\w*)\"\\soption-id=\"", ""));
                if(heroConfirm.get(loca).endsWith("\"")) {
                    heroConfirm.set(loca, heroConfirm.get(loca).substring(0,heroConfirm.get(loca).length()-1));
                }
            }
            if(cycle==2) {
                compConfirm.add(matcher.group(0).replaceAll("(0x02E\\d*\\w*)\"\\soption-id=\"", ""));
                if(compConfirm.get(loca).endsWith("\"")) {
                    compConfirm.set(loca, compConfirm.get(loca).substring(0,compConfirm.get(loca).length()-1));
                }
            }
            loca++;
        }
        compRates = new String[] {srs[0],srs[1],srs[2]}; //[0] contains tank, [1] contains DPS, [2] contains support
        pattern = Pattern.compile("([a-zA-Z]*(-*\\s*\\d*[a-zA-z]*)*</td><[^>]*>(\\d*:?\\.?%?\\d*:?\\d*))"); //did I say worst earlier? this one's far worse.
        matcher = pattern.matcher(nex);
        String found = "";
        int z=0;
        boolean firstDamage = false; // a boolean that fixes Ana's stats appearing under All Heroes
        boolean ahnm = true; // a boolean that fixes the order of name addition and generation
        boolean c = false;
        boolean luciohs = false;        //and here we have three booleans that are entirely for checking when heroes don't exist or have missing data or all that jazz
        boolean onemore = false;
        //goal is to replace anything that is NOT (number/word number/word...number/word</td><...>number/decimal/time</td></tr>) with nothing
        while(matcher.find()) {
            found = matcher.group(0);
            found = found.replace("</td><td class=\"DataTable-tableColumn\">", ": "); //cleanup
            found = found.replace("</td></tr>", "");    //cleanup
            if(found.startsWith("rn Kills")) {
                found =found.replace("rn Kills","Torbjörn Kills"); //have to replace the accent because I couldn't get regex to include this
            }
            if(found.startsWith("d:") || found.startsWith("d -")) {
                found = found.replace("d", "Enemies EMP'd");    //same with the apostrophe since it's just one case.
            }
            if(found.contains("All Damage Done - Most in Game")) {  //there were lots of shenanigans involving heroes on my example account not having enough playtime for the stats to show in the right order
                if(ahnm) { 
                            ahnm = false;
                            values.add(heroList[z]);
                            z++;
                }
                if(c) {
                    if(onemore) {
                        values.add("Competitive");
                        onemore = false;
                    }
                    else {
                        onemore = true;
                    }
                }
                else if(values.size()>0) {
                    if(values.get(values.size()-1).contains("Time Spent on Fire") || values.get(values.size()-1).contains("Competitive")) {
                        values.add(heroList[z]);
                        z++;
                        luciohs = true;
                    }
                }
            }   //and all of that is just to correctly make sure that low play time heroes show up where they should.
            if(found.contains("All Damage Done:")&& firstDamage&&!c &&!luciohs) {   // our first "normal" identifier
                while(!heroConfirm.contains(heroList[z]) && z<34) {
                    values.add(heroList[z]);
                    z++;
                }
                if(z<34) {
                values.add(heroList[z]);
                z++;
                onemore=false;
                }
            }
            if(found.contains("All Damage Done:") && firstDamage&&c&&!luciohs) { //its equivalent, but for comp. No, they aren't different. Yes, I'm too lazy to remove it.
                while(!compConfirm.contains(heroList[z]) && z<32) {
                    values.add(heroList[z]);
                    z++;
                }
                if(z<33) {
                values.add(heroList[z]);
                onemore=false;
                z++;
                }
            }
            if(found.contains("All Damage Done:") && !firstDamage) { //The BEST category always shows up before COMBAT on all heroes, this skips that occurrence.
                firstDamage = true;
            }
            if(luciohs && found.contains("All Damage Done:")) { //basically the same case as the above, but the trigger is done differently.
                luciohs = false;
            }
            if(z>=33){z=0;firstDamage=false;c=true;ahnm=true;onemore=false;}
            if(found!="" && found!= " ") { //for whatever reason our regex finds empty lines between every important bit of data, very cool.
                values.add(found);  //and casually do this over 3000 times or however many there are
            }
        }
        System.out.println("Done."); //hey maybe the user likes feedback. I know I do.
    }
        

    public ArrayList<String> getReturns() { //four returns, in which we only use two.
        return values;
    }
    public String[] getComp() {
        return compRates;
    }
    public ArrayList<String> getqpHeroes() {
        return heroConfirm;
    }
    public ArrayList<String> getcHeroes() {
        return compConfirm;
    }
}
