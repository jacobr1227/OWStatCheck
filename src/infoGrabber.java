import java.util.*;
import java.util.regex.*;
import java.io.*;
import java.net.*;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;
public class infoGrabber {
    String [] heroList;
    {       //listing all of our hero identifiers for HTML parsing
        heroList=new String[] {"All Heroes", "Ana","Ashe","Baptiste","Bastion","Brigitte","D.Va","Doomfist","Echo","Genji","Hanzo","Junkrat","Lúcio","McCree","Mei","Mercy","Moira","Orisa","Pharah","Reaper","Reinhardt","Roadhog","Sigma","Soldier: 76","Sombra","Symmetra","Torbjörn","Tracer","Widowmaker","Winston","Wrecking Ball","Zarya","Zenyatta"};
    }
    public ArrayList<String> values = new ArrayList<String>();
    public ArrayList<String> heroConfirm = new ArrayList<String>();
    public ArrayList<String> compConfirm = new ArrayList<String>();
    public String[] compRates;
    URL url;
    public infoGrabber(URL newrl) {
        url = newrl;
    }
    public void execute() throws IOException {
        //use URL info generated in regular class to find a player page
        if(new File("idump.txt").delete()){System.out.println("Cleared.");} //a safe way to make sure this file gets overwritten rather than appended
        values.clear();
        ReadableByteChannel rbc = Channels.newChannel(url.openStream());
        FileOutputStream fos = new FileOutputStream("idump.txt");
        FileChannel fc = fos.getChannel();
        fc.transferFrom(rbc, 0, Long.MAX_VALUE);
        fos.close();
        String line;
        Stream<String> lines = Files.lines(Paths.get("idump.txt"));
        line = lines.skip(1522).findFirst().get();
        lines.close();
        String nex = "";

        nex = line;
        Pattern pattern = Pattern.compile("(competitive-rank-level\">\\d{3,4})");
        Matcher matcher = pattern.matcher(nex);
        String[] srs = new String[3];
        int x=0;
        while(matcher.find() && x<3) {
            srs[x] = matcher.group(0);
            srs[x] = srs[x].replaceAll("competitive-rank-level\">", "");
            x++;
        }
        pattern = Pattern.compile("(0x02E\\d*\\w*)\"\\soption-id=\"([a-zA-Z]*\\s?.?\\s?[a-zA-Z0-9]*)");
        matcher = pattern.matcher(nex);
        int cycle = 0;
        int loca = 0;
        while(matcher.find()) {
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
        pattern = Pattern.compile("([a-zA-Z]*(-*\\s*\\d*[a-zA-z]*)*</td><[^>]*>(\\d*:?\\.?%?\\d*:?\\d*))");
        matcher = pattern.matcher(nex);
        String found = "";
        int z=0;
        boolean firstDamage = false; // a boolean that fixes Ana's stats appearing under All Heroes
        boolean ahnm = true; // a boolean that fixes the order of name addition and generation
        boolean c = false;
        boolean luciohs = false;
        boolean onemore = false;
        //goal is to replace anything that is NOT (number/word number/word...number/word</td><...>number/decimal/time</td></tr>) with nothing
        while(matcher.find()) {
            found = matcher.group(0);
            found = found.replace("</td><td class=\"DataTable-tableColumn\">", ": ");
            found = found.replace("</td></tr>", "");
            if(found.startsWith("rn Kills")) {
                found =found.replace("rn Kills","Torbjörn Kills");
            }
            if(found.startsWith("d:") || found.startsWith("d -")) {
                found = found.replace("d", "Enemies EMP'd");
            }
            if(found.contains("All Damage Done - Most in Game")) {
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
            }
            if(found.contains("All Damage Done:")&& firstDamage&&!c &&!luciohs) {
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
            if(found.contains("All Damage Done:") && firstDamage&&c&&!luciohs) {
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
            if(found.contains("All Damage Done:") && !firstDamage) {
                firstDamage = true;
            }
            if(luciohs && found.contains("All Damage Done:")) {
                luciohs = false;
            }
            if(z>=33){z=0;firstDamage=false;c=true;ahnm=true;onemore=false;}
            if(found!="" && found!= " ") {
                values.add(found);
            }
        }
        System.out.println("Done.");
    }
        

    public ArrayList<String> getReturns() {
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
