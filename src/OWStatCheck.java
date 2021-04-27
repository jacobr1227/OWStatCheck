import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.net.*;
class OWStatCheck implements ActionListener {
    final JFrame stWin = new JFrame("OW Stat Checker");
    JPanel panel = new JPanel(new BorderLayout());
    JMenuBar mb = new JMenuBar();
    JMenu mS = new JMenu("System");
    JMenu mH = new JMenu("Help");
    JMenuItem sN = new JMenuItem("Open new Profile");
    JMenuItem sS = new JMenuItem("Export Data (UNAVAILABLE)");
    JMenuItem sE = new JMenuItem("Exit");
    JMenuItem hH = new JMenuItem("How to...");
    JMenuItem hA = new JMenuItem("About");
    JTextArea ta = new JTextArea("Stats:");
    String[] heroList=new String[] {"All Heroes", "Ana","Ashe","Baptiste","Brigitte","D.Va","Doomfist","Echo","Genji","Hanzo","Junkrat","Lucio","McCree","Mei","Mercy","Moira","Orisa","Pharah","Reaper","Reinhardt","Roadhog","Sigma","Soldier: 76","Sombra","Symmetra","Torbjorn","Tracer","Widowmaker","Winston","Wrecking Ball","Zarya","Zenyatta"};
    JComboBox<String> jcb = new JComboBox<String>(heroList);
    JLabel jl = new JLabel("Mode: Quick Play");
    JMenuItem cqp = new JMenuItem("Switch Stats");
    String bt,rgn,plat;
    ArrayList<String> dataList = new ArrayList<String>();
    boolean comp = false;
    public OWStatCheck() {
        stWin.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ta.setEditable(false);
        ta.setOpaque(false);

        mb.add(mS);
        mb.add(mH);
        mS.add(sN);
        mS.add(sS);
        mS.add(sE);
        mH.add(hH);
        mH.add(hA);
        mS.add(cqp);

        cqp.addActionListener(this);
        sN.addActionListener(this);
        sS.addActionListener(this);
        sE.addActionListener(this);
        hH.addActionListener(this);
        hA.addActionListener(this);
        mH.addActionListener(this);

        stWin.setContentPane(panel);
        stWin.getContentPane().add(BorderLayout.NORTH, mb);
        stWin.getContentPane().add(BorderLayout.SOUTH, jl);
        stWin.getContentPane().add(BorderLayout.CENTER, ta);
        stWin.getContentPane().add(BorderLayout.CENTER, jcb);
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
                infoGrabber ig = new infoGrabber(url, comp);
                ig.execute();
                jcb.setSelectedIndex(0);
                ta.setText(ig.values.get(1));
                /*String[] SR = ig.getComp();
                String[] typ = {"Tank", "DPS", "Support"};
                for(int i=0;i<SR.length;i++) {
                    ta.append(typ[i]+" SR: "+SR[i]+"\n");
                }
                for(int i=0;i<ig.values.size();i++) {
                    if(i%60==0||i==0) { //find names to properly stylize
                        ta.append(ig.values.get(i)+"\n");
                    }
                    else { //clean html later
                        
                    }
                }*/
            } catch(Exception malformedUrlException) {malformedUrlException.printStackTrace();}
        }
        if(e.getSource()==jcb) {
            
        }
        if(e.getSource()==cqp) {
            if(!comp)
                jl.setText("Mode: Competitive");
            if(comp)
                jl.setText("Mode: Quick Play");
            comp=!comp;
        }
        if(e.getSource()==sS) { //potentially add a data export feature?

        }
        if(e.getSource()==hH) {
            JOptionPane.showMessageDialog(stWin,"To open a profile, select [ System>Open new Profile ] and complete the dialogs.\nTo exit the application, please click the red X, or use [ System>Exit ]\nExporting data is currently not implemented, but may be in the future.");
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