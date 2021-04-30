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
    JTextArea ta = new JTextArea("Stats:");
    JScrollPane jsp = new JScrollPane(ta);
    String[] heroList=new String[] {"All Heroes", "Ana","Ashe","Baptiste","Bastion","Brigitte","D.Va","Doomfist","Echo","Genji","Hanzo","Junkrat","Lúcio","McCree","Mei","Mercy","Moira","Orisa","Pharah","Reaper","Reinhardt","Roadhog","Sigma","Soldier: 76","Sombra","Symmetra","Torbjörn","Tracer","Widowmaker","Winston","Wrecking Ball","Zarya","Zenyatta"};
    JComboBox<String> jcb = new JComboBox<String>(heroList);
    ComboBoxModel<String> model = jcb.getModel();
    JButton update = new JButton("Get Hero Info");
    JLabel jl = new JLabel("Mode: Quick Play");
    JLabel spacer = new JLabel("    ");
    JMenuItem cqp = new JMenuItem("Switch Stats");
    JFileChooser jfc = new JFileChooser();
    String bt,rgn,plat;
    ArrayList<String> vals = new ArrayList<String>();
    String[] SR;
    String[] typ = {"Tank", "DPS", "Support"};
    int ct;
    boolean comp = false;
    public OWStatCheck() { //TODO: fix comp overextension from Zen, fix comp name association / Add comments
        stWin.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        panel.setPreferredSize(new Dimension(400,750));
        
        panel.add(jsp);
        ta.setEditable(false);
        ta.setOpaque(false);
        jfc.setDialogTitle("Specify a file to save:");
        jsp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        jsp.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        jsp.getVerticalScrollBar().setValue(jsp.getVerticalScrollBar().getMinimum());
        mb.add(mS);
        mb.add(mH);
        mb.add(spacer);
        mS.add(sN);
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
        jcb.addActionListener(this);

        stWin.add(BorderLayout.NORTH,menuP);
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
        if(e.getSource()==sN) {
            ta.setText("");
            bt = (String)JOptionPane.showInputDialog(stWin, "Please enter your battletag:", "Example#1234");
            while(bt==null) {}
            rgn = (String)JOptionPane.showInputDialog(stWin, "Please enter your game region as \" **-** \" (language-region):", "en-us");
            while(rgn==null) {}
            plat = (String)JOptionPane.showInputDialog(stWin, "Please enter your platform:","pc");
            while(plat==null) {}
            try {
                URL url=new URL(urlGen(bt,rgn,plat));
                infoGrabber ig = new infoGrabber(url);
                ig.execute();
                SR = ig.getComp();
                vals = ig.getReturns();
                comp = false;
                for(int i=0;i<3;i++) {
                    ta.append(typ[i]+" SR: "+SR[i]+"\n");
                }
                for(int i=0;i<60;i++) {
                    ta.append(vals.get(i)+"\n");
                }
                jsp.getVerticalScrollBar().setValue(jsp.getVerticalScrollBar().getMinimum());
            } catch(Exception malformedUrlException) {malformedUrlException.printStackTrace();}
        }
        if(e.getSource()==jcb) {
            ta.setText("");
            if(!comp) {
            for(int i=0;i<heroList.length;i++) {
                if(heroList[i]==jcb.getSelectedItem()) {
                    for(int a=0;a<vals.size();a++) {
                        if(vals.get(a)==heroList[i]) {
                            for(int z=0;z<3;z++) {
                                ta.append(typ[z]+" SR: "+SR[z]+"\n");
                            }
                            if(vals.get(a).equals(heroList[32])) {
                                while(vals.get(a)!="Competitive") {
                                    if(!vals.get(a).equals("All Heroes")) {
                                        ta.append(vals.get(a)+"\n");
                                    }
                                    a++;
                                }
                                break;
                            }
                            while(vals.get(a)!=heroList[i+1]&&vals.get(a)!="Competitive"&&a<vals.size()) {
                                ta.append(vals.get(a)+"\n");
                                System.out.println(i +" " + a + " " + vals.get(a));
                                a++;
                            }
                            jsp.getVerticalScrollBar().setValue(jsp.getVerticalScrollBar().getMinimum());
                            break;
                        }
                    }
                }
            }
            }
            if(comp) {
            for(int i=0;i<heroList.length;i++) {
                System.out.println(i+"i");
                if(heroList[i]==jcb.getSelectedItem()) {
                    for(int a=0;a<vals.size();a++) {
                        if(vals.get(a)=="Competitive") {
                            ta.append(vals.get(a)+"\n");
                            for(int r=a+1;r<vals.size();r++) {
                                if(vals.get(r)==heroList[i]) {
                                    for(int z=0;z<3;z++) {
                                        ta.append(typ[z]+" SR: "+SR[z]+"\n");
                                    }
                                    if(vals.get(r).equals(heroList[32])) {
                                        while(r<vals.size()) {
                                            ta.append(vals.get(r)+"\n");
                                            r++;
                                        }
                                        break;
                                    }
                                    while(vals.get(r)!=heroList[i+1]&&r<vals.size()) {
                                        ta.append(vals.get(r)+"\n");
                                        System.out.println(i + " " + a + " " + r + " " + vals.get(r));
                                        r++;
                                    }
                                    jsp.getVerticalScrollBar().setValue(jsp.getVerticalScrollBar().getMinimum());
                                    break;
                                }
                                if(i==0) {
                                    for(int z=0;z<3;z++) {
                                        ta.append(typ[z]+" SR: " + SR[z]+"\n");
                                    }
                                    while(vals.get(r)!=heroList[1]&&r<vals.size()) {
                                        ta.append(vals.get(r)+"\n");
                                        System.out.println(i + " " + a + " " + r + " " + vals.get(r));
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
                JOptionPane.showMessageDialog(panel, "Please open a profile first!");
                
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
            jsp.getVerticalScrollBar().setValue(jsp.getVerticalScrollBar().getMinimum());
            }
        }
        if(e.getSource()==sS) { //let's go, data exporting
            int saveSelect = jfc.showSaveDialog(stWin);
            if(saveSelect == JFileChooser.APPROVE_OPTION) {
                try {
                    File toSave = jfc.getSelectedFile();
                    FileWriter fw = new FileWriter(toSave);
                    fw.write(ta.getText());
                    fw.close();
                }catch(Exception IOException){IOException.printStackTrace();}
            }
        }
        if(e.getSource()==hH) {
            JOptionPane.showMessageDialog(stWin,"To open a profile, select [ System>Open new Profile ] and complete the dialogs.\nTo exit the application, please click the red X, or use [ System>Exit ]\nIf you see \"null\" in the SRs, you may have mistyped the name. Try reopening your profile.\nIf you see the wrong stats on heroes, or Zenyatta is broken, you haven't played a hero yet. This WILL cause the app to break.");
        }
        if(e.getSource()==hA) {
            JOptionPane.showMessageDialog(stWin, "This application was made by Jacob Rogers. I do hope you enjoy or find utility in it.\nIf you don't play Overwatch, and would like an example profile, use mine: Skjell#1659");
        }
    }

    public String urlGen(String battletag, String region, String platform) {
        battletag = battletag.replace("#","-");
        return "https://playoverwatch.com/" + region + "/career/" + platform + "/" + battletag + "/";
    }

    public static void main(String[] args) {
            new OWStatCheck();
    }
}