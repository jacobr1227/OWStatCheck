import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.net.*;
import java.io.*;
class OWStatCheck implements ActionListener {
    final JFrame stWin = new JFrame("OW Stat Checker");
    JPanel panel = new JPanel(new BorderLayout());
    JPanel menuP = new JPanel(new BorderLayout());
    JMenuBar mb = new JMenuBar();
    JMenu mS = new JMenu("System");
    JMenu mH = new JMenu("Help");
    JMenuItem sN = new JMenuItem("Open new Profile");
    JMenuItem sS = new JMenuItem("Export Data (UNAVAILABLE)");
    JMenuItem sE = new JMenuItem("Exit");
    JMenuItem hH = new JMenuItem("How to...");
    JMenuItem hA = new JMenuItem("About");
    JTextArea ta = new JTextArea("Stats:");     //love the jmenu
    JScrollPane jsp = new JScrollPane(ta);
    String[] heroList=new String[] {"All Heroes", "Ana","Ashe","Baptiste","Bastion","Brigitte","D.Va","Doomfist","Echo","Genji","Hanzo","Junkrat","Lúcio","McCree","Mei","Mercy","Moira","Orisa","Pharah","Reaper","Reinhardt","Roadhog","Sigma","Soldier: 76","Sombra","Symmetra","Torbjörn","Tracer","Widowmaker","Winston","Wrecking Ball","Zarya","Zenyatta"};
    JComboBox<String> jcb = new JComboBox<String>(heroList);     
    JButton update = new JButton("Get Hero Info");
    JLabel jl = new JLabel("Mode: Quick Play");
    JLabel spacer = new JLabel("    ");
    JMenuItem cqp = new JMenuItem("Switch Stats");
    JFileChooser jfc = new JFileChooser();  //at some point my nomenclature became shortening the constructor name
    String bt,rgn,plat;
    ArrayList<String> vals = new ArrayList<String>();
    String[] SR;
    String[] typ = {"Tank", "DPS", "Support"};      //it seems like a lot of setup, right?
    int ct;
    boolean comp = false;
    public OWStatCheck() {
        stWin.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        panel.setPreferredSize(new Dimension(400,750)); //look at that, we have a size modifier, wow
        
        panel.add(jsp);
        ta.setEditable(false);
        ta.setOpaque(false);
        jfc.setDialogTitle("Specify a file to save:");
        jsp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        jsp.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        jsp.getVerticalScrollBar().setValue(jsp.getVerticalScrollBar().getMinimum());   //this actually doesn't work
        mb.add(mS);
        mb.add(mH);
        mb.add(spacer);
        mS.add(sN);     //love the meny setup
        mS.add(sS);
        mS.add(sE);
        mH.add(hH);
        mH.add(hA);
        mS.add(cqp);
        jcb.setPreferredSize(new Dimension(100,50));

        cqp.addActionListener(this);
        sN.addActionListener(this);
        sS.addActionListener(this);
        sE.addActionListener(this);
        hH.addActionListener(this);
        hA.addActionListener(this);
        mH.addActionListener(this);
        jcb.addActionListener(this);        //I forgot this listener for a while, that one hurt to find

        stWin.add(BorderLayout.NORTH,menuP);        //we use two panels here so that I can add two objects to NORTH without overlapping
        stWin.add(BorderLayout.CENTER,panel);
        menuP.add(BorderLayout.NORTH, mb);
        panel.add(BorderLayout.NORTH, jl);
        panel.add(BorderLayout.SOUTH, jcb);
        panel.add(BorderLayout.CENTER, jsp);
        stWin.setVisible(true);
        stWin.pack();
    }

    public void actionPerformed(ActionEvent e) {
        if(e.getSource()==sE) {
            stWin.dispose();
        }
        if(e.getSource()==sN) {     //menu: Open Profile
            ta.setText("");
            bt = (String)JOptionPane.showInputDialog(stWin, "Please enter your battletag:", "Example#1234");
            while(bt==null) {}
            rgn = (String)JOptionPane.showInputDialog(stWin, "Please enter your game region as \" **-** \" (language-region):", "en-us");
            while(rgn==null) {}
            plat = (String)JOptionPane.showInputDialog(stWin, "Please enter your platform:","pc");
            while(plat==null) {}    //horrible design mistake just to make sure nothing else breaks. Please never hit cancel...
            try {
                URL url=new URL(urlGen(bt,rgn,plat)); //create a url to use over on the other side
                infoGrabber ig = new infoGrabber(url);  //and make our constructor
                ig.execute();   //and carry it out.
                SR = ig.getComp();  //utilize out returns
                vals = ig.getReturns();
                comp = false;      //defaulting the UI so that no errors occur and there is no need for dual functionality
                for(int i=0;i<3;i++) {
                    ta.append(typ[i]+" SR: "+SR[i]+"\n");   //add in the Skill ratings
                }
                for(int i=0;i<60;i++) {
                    ta.append(vals.get(i)+"\n");    //add in all of our first 60 data pieces, which is exactly that of All Heroes (QP)
                }
                jsp.getVerticalScrollBar().setValue(jsp.getVerticalScrollBar().getMinimum());   //this still does nothing
            } catch(Exception malformedUrlException) {malformedUrlException.printStackTrace();} //and of course it won't work without this
        }
        if(e.getSource()==jcb) {    //welcome to loop hell
            ta.setText("");         //we have no cookies
            if(!comp) {             //only dyslexia
            for(int i=0;i<heroList.length;i++) {
                if(heroList[i]==jcb.getSelectedItem()) {
                    for(int a=0;a<vals.size();a++) {
                        if(vals.get(a)==heroList[i]) {
                            for(int z=0;z<3;z++) {
                                ta.append(typ[z]+" SR: "+SR[z]+"\n");   //adding in our SR before every character because why not
                            }
                            if(vals.get(a).equals(heroList[32])) {
                                while(vals.get(a)!="Competitive") { //run the loop until we hit this point in the data
                                    if(!vals.get(a).equals("All Heroes")) {
                                        ta.append(vals.get(a)+"\n");        //this is Zenyatta's special place, this allows the below function to be easier to read and write
                                    }
                                    a++;
                                }
                                break;
                            }
                            while(vals.get(a)!=heroList[i+1]&&vals.get(a)!="Competitive"&&a<vals.size()) { //this works for everyone from All Heroes to Zarya. And then the array freaks out at Zenyatta.
                                ta.append(vals.get(a)+"\n");
                                a++;    //now you may be wondering why I have a for loop over a and then a while loop instead.
                            }           //"It just works" - Todd Howard
                            jsp.getVerticalScrollBar().setValue(jsp.getVerticalScrollBar().getMinimum()); //yeah it still doesn't work
                            break;
                        }
                    }
                }
            }
            }
            if(comp) {  //okay now everything we just did? Do it again, but with one more conditional
            for(int i=0;i<heroList.length;i++) {
                System.out.println(i+"i");
                if(heroList[i]==jcb.getSelectedItem()) {
                    for(int a=0;a<vals.size();a++) {
                        if(vals.get(a)=="Competitive") {    //this time we're gonna skip to this value
                            ta.append(vals.get(a)+"\n");
                            for(int r=a+1;r<vals.size();r++) {
                                if(vals.get(r)==heroList[i]) {
                                    for(int z=0;z<3;z++) {
                                        ta.append(typ[z]+" SR: "+SR[z]+"\n");
                                    }
                                    if(vals.get(r).equals(heroList[32])) {  //and print everything much the same
                                        while(r<vals.size()) {
                                            ta.append(vals.get(r)+"\n");
                                            r++;
                                        }
                                        break;
                                    }
                                    while(vals.get(r)!=heroList[i+1]&&r<vals.size()) {
                                        ta.append(vals.get(r)+"\n");
                                        r++;
                                    }
                                    jsp.getVerticalScrollBar().setValue(jsp.getVerticalScrollBar().getMinimum());
                                    break;
                                }
                                if(i==0) {
                                    for(int z=0;z<3;z++) {
                                        ta.append(typ[z]+" SR: " + SR[z]+"\n");
                                    }
                                    while(vals.get(r)!=heroList[1]&&r<vals.size()) {    //we also have a compatibility layer for All Heroes (C)
                                        ta.append(vals.get(r)+"\n");
                                        r++;
                                    }
                                    break;
                                }
                            }
                        }
                    }
                }
            }
            }
        }
        if(e.getSource()==cqp) {
            if(plat ==null) {
                JOptionPane.showMessageDialog(panel, "Please open a profile first!"); //only one error message here.
            }
            else {
            if(!comp) {
                jl.setText("Mode: Competitive");
            }
            if(comp) {
                jl.setText("Mode: Quick Play");
            }
            comp = !comp;
            jcb.setSelectedIndex(0);
            jsp.getVerticalScrollBar().setValue(jsp.getVerticalScrollBar().getMinimum());   //lo and behold it stil doesn't do what it's supposed to.
            }
        }
        if(e.getSource()==sS) { //let's go, data exporting
            int saveSelect = jfc.showSaveDialog(stWin); //I almost excluded this but it really was a lot easier than i thought
            if(saveSelect == JFileChooser.APPROVE_OPTION) {
                try {
                    File toSave = jfc.getSelectedFile();
                    FileWriter fw = new FileWriter(toSave);
                    fw.write(ta.getText());
                    fw.close();
                }catch(Exception IOException){IOException.printStackTrace();}
            }   //I maybe should add an option for printing all of your data, not just the open page, but whatever.
        }
        if(e.getSource()==hH) { //help menu
            JOptionPane.showMessageDialog(stWin,"To open a profile, select [ System>Open new Profile ] and complete the dialogs.\nTo exit the application, please click the red X, or use [ System>Exit ]\nIf you see \"null\" in the SRs, you may have mistyped the name. Try reopening your profile.\nIf you see the wrong stats on heroes, or Zenyatta is broken, you haven't played a hero yet. This WILL cause the app to break.");
        }
        if(e.getSource()==hA) { //about menu
            JOptionPane.showMessageDialog(stWin, "This application was made by Jacob Rogers. I do hope you enjoy or find utility in it.\nIf you don't play Overwatch, and would like an example profile, use mine: Skjell#1659");
        }
    }

    public String urlGen(String battletag, String region, String platform) {    //wow, one whole function.
        battletag = battletag.replace("#","-");
        return "https://playoverwatch.com/" + region + "/career/" + platform + "/" + battletag + "/";
    }

    public static void main(String[] args) {    //shortest main in my life
            new OWStatCheck();
    }
}