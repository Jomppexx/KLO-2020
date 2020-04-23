import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;

public class SecondaryScreen {

    //private JFrame secondary;
    ArrayList<ReviewPiece> arvostelut = new ArrayList<>();

    public String selectedTitleName;
    public String selectedTitleCategory;
    private JDialog secscreen;
    private JFrame secondary; // = new JFrame();
    private JPanel toolsPanel; //paneeli hakutoiminnoille ja työkalupalkille
    private JPanel titlesPanel; //paneeli nimikelistalle, josta pääsee arvosteluihin
    private JSplitPane mainSplitPane; //pääpaneeli, johon yllä olevat paneelit laitetaan

    public SecondaryScreen(String title, String category){
        selectedTitleName = title;
        selectedTitleCategory = category;
        loadReviews();
        MakeWindow();

    }
    private void MakeWindow() {
        JFrame secondary = new JFrame();
        JDialog secscreen = new JDialog(secondary, "Arvostelut");
        secscreen.setSize(1200, 800);
        secscreen.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        JPanel upperPanel = new JPanel();
        toolsPanel = new JPanel();
        //GridBagLayout gbLayout = new GridBagLayout();
        //toolsPanel.setLayout(gbLayout);
        //GridBagConstraints gbConstraint = new GridBagConstraints();
        toolsPanel.setLayout(new BoxLayout(toolsPanel, BoxLayout.LINE_AXIS));
        //toolsPanel.setMaximumSize(new Dimension(200, 50));
        titlesPanel = new JPanel();
        titlesPanel.setLayout(new BoxLayout(titlesPanel, BoxLayout.PAGE_AXIS));

        /*
         * Luodaan scrollPane ja liitetään se titlesPaneeliin, jotta title paneelia voi kelata
         * kun siihen tulee "liikaa" sisältöä, eikä se kaikki näy kerralla
         */
        // scrollPane, mahdollistaa ikkunan scrollaamisen...
        JScrollPane titlesScrollPane = new JScrollPane(titlesPanel);
        secscreen.add(titlesScrollPane);
        titlesScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        // "Pääpaneeli" on splitpane, jossa yläosaan tulee hakutyökalujen paneeli ja alaosaan eri nimikkeet
        //mainSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, toolsPanel, titlesScrollPane);
        mainSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, upperPanel, titlesScrollPane);
        mainSplitPane.setOneTouchExpandable(false);
        mainSplitPane.setResizeWeight(0.5);
        mainSplitPane.setDividerSize(0);

        ////toolsPanel.add(Box.createHorizontalGlue());

        //Title ja kategoria
        //Käytä oliota? EntertainmentPiece lotr = new EntertainmentPiece("Lord of the Rings", "Elokuvat");
        JLabel title = new JLabel (selectedTitleName);
        toolsPanel.add(title, BorderLayout.LINE_START);
        toolsPanel.add(Box.createRigidArea(new Dimension(5,5)));
        JLabel kategoria = new JLabel (selectedTitleCategory);
        toolsPanel.add(kategoria);
        toolsPanel.add(Box.createRigidArea(new Dimension(5,5)));
        toolsPanel.add(Box.createHorizontalGlue());


        //Tehdään hakemistoiminto
        JLabel hakuehdot = new JLabel("Rajoita hakua:");
        String hakutermit[]={"Uusin", "Tekijä", "Arvosana"};
        JComboBox sortby = new JComboBox(hakutermit);
        sortby.setSize(20,10);
        toolsPanel.add(hakuehdot);
        toolsPanel.add(sortby);

        JButton newReview = new JButton("+ Uusi arvostelu");
        newReview.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AddReview();
            }
        });
        toolsPanel.add(newReview);


        upperPanel.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 0.5;
        c.ipady = 10;
        upperPanel.add(title, c);

        c.gridx = 0;
        c.gridy = 1;
        upperPanel.add(kategoria, c);

        c.gridx = 3;
        c.gridy = 0;
        c.gridheight = 2;
        c.gridwidth = 1;
        c.ipady = 40;
        c.ipadx = 40;
        c.weightx = 0;
        c.weighty = 1;
        c.fill = GridBagConstraints.BOTH;
        upperPanel.add(newReview);


        c.anchor = GridBagConstraints.PAGE_END;
        c.fill = GridBagConstraints.VERTICAL;
        c.gridwidth = 1;
        c.gridheight = 1;
        c.weightx = 0.5;
        c.weighty = 0;
        c.ipady = 0;
        c.ipadx = 0;
        c.gridx = 5;
        c.gridy = 0;
        JLabel sorttext = new JLabel("Hakuehdot:");
        upperPanel.add(sorttext, c);

        c.gridx = 5;
        c.gridy = 1;
        upperPanel.add(sortby, c);

        secscreen.add(mainSplitPane);
        secondary.setVisible(true);
        secscreen.setVisible(true);
        mainSplitPane.setDividerLocation(0.07);
        secscreen.setResizable(false);
        //AddReview();
        InitializeReviews();

        //titlesPanelin elementtien lisäys

        /*for(int i=0;i<arvostelut.size();i++) {
            int arvosana = arvostelut.get(i).getArvosana();
            String arvo = Integer.toString(arvosana);

            JLabel grade = new JLabel("Ladattiin arvostelu arvosanalla " + arvo);
            JPanel testi = new JPanel();
            testi.add(grade);

            //secondary.add(testi);
            //testi.setVisible(true);
            //titlesScrollPane.add(testi);
            //secondary.revalidate();
            //secondary.repaint();

            titlesPanel.add(testi);
            testi.setVisible(true);
            titlesPanel.revalidate();
            titlesPanel.repaint();
            //secondary.pack();

            //Object[] ok = {"Ok"};
            //JOptionPane.showOptionDialog(null, testi,
                    //"ööh", JOptionPane.YES_OPTION,
                    //JOptionPane.PLAIN_MESSAGE, null, ok, null);
        }*/

        //titlesPanel.setVisible(true);

    }

    private void AddReview() {
        JFrame addwindow = new JFrame("Uusi Arvostelu");
        addwindow.setSize(400,800);
        //Panel panel = new Panel();
        JPanel panel = new JPanel();
        panel.setBackground(Color.CYAN);
        panel.setSize(400, 800);
        //GridBagLayout layout = new GridBagLayout();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel title = new JLabel(selectedTitleName);
        panel.add(title);
        JLabel category = new JLabel(selectedTitleCategory);
        JTextField headerfield = new JTextField("Otsikko", 1);
        headerfield.setMaximumSize(new Dimension(150,15));
        headerfield.setAlignmentX(addwindow.CENTER_ALIGNMENT);

        String grades[]={"1","2","3","4","5","6","7","8","9","10"};
        JComboBox grade = new JComboBox(grades);
        grade.setMaximumSize(new Dimension(150,15));
        JTextArea reviewtext = new JTextArea(30,50);
        reviewtext.setMaximumSize(new Dimension(500, 500));
        reviewtext.setAlignmentX(addwindow.CENTER_ALIGNMENT);

        //JButton savebutton = new JButton("Tallenna");
        //JButton cancelbutton = new JButton("Peru");

        panel.add(category);
        panel.add(headerfield);
        panel.add(grade);
        panel.add(reviewtext);

        panel.setBorder(BorderFactory.createTitledBorder("demo"));
        addwindow.getContentPane().add(panel);
        addwindow.add(panel);
        //addwindow.setVisible(true);

        Object[] savecancelbuttons = {"Peru", "Tallenna"};
        int result = JOptionPane.showOptionDialog(null, panel,
                "Uusi arvostelu", JOptionPane.YES_NO_OPTION,
                JOptionPane.PLAIN_MESSAGE, null, savecancelbuttons, null);
        //NO_OPTION on oikealla puolella ruutua, joten Tallenna-nappi on oikeasti "EI" eli NO-nappi
        //Siksi "Tallenna"-nappi onkin ei-nappi koodin mukaan
        if(result == JOptionPane.NO_OPTION){
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDateTime reviewtime = LocalDateTime.now();
            String aika = reviewtime.format(dtf);
            ReviewPiece uusi = new ReviewPiece(
                    selectedTitleName,
                    selectedTitleCategory,
                    headerfield.getText(),
                    grade.getSelectedIndex()+1,
                    reviewtext.getText(),
                    aika);

            JPanel added = new JPanel();
            added.setSize(400,800);
            JLabel otsikko = new JLabel(headerfield.getText());
            int a = uusi.getArvosana();
            String arvo = Integer.toString(a);
            JLabel arvosana = new JLabel(arvo);
            JLabel testi = new JLabel("hei");

            added.add(otsikko);
            added.add(arvosana);
            //addwindow.add(added);
            Object[] ok = {"Ok"};
            JOptionPane.showOptionDialog(null, added,
                    "Vahvistus", JOptionPane.YES_OPTION,
                    JOptionPane.PLAIN_MESSAGE, null, ok, null);
            arvostelut.add(uusi);
            SaveReviews();
            loadReviews();
            InitializeReviews();

            /*addwindow.add(otsikko);
            addwindow.add(arvosana);
            addwindow.add(testi);*/

            //addwindow.setVisible(true);

        }
    }

    public void SaveReviews () {
        try {
            //HUOMIO: INTELLIJ KIRJOITTAA OBJECT.SERIN SRC-KANSION YLEMPÄÄN KANSIOON
            String directory = System.getProperty("user.dir") + "\\object.ser";
            System.out.println(directory);
            FileOutputStream file = new FileOutputStream(directory);
            ObjectOutputStream out = new ObjectOutputStream(file);
            out.writeObject(arvostelut);
        } catch (IOException e){
            e.printStackTrace();
        } //catch (FileNotFoundException e){}
    }

    public void loadReviews() {
        try {
            FileInputStream file = new FileInputStream(System.getProperty("user.dir") +
                    "\\object.ser");
            ObjectInputStream in = new ObjectInputStream(file);
            ArrayList<ReviewPiece> temp = new ArrayList<>();
            try {
                temp = (ArrayList<ReviewPiece>) in.readObject();
            } catch (ClassNotFoundException c) {
                c.printStackTrace();
            }
            /*for(int i=0;i<temp.size();i++){
                temp.get(i).getvalues();
            }*/
            this.arvostelut = temp;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void InitializeReviews() {
        for(int i=0;i<arvostelut.size();i++) {
            /*int arvosana = arvostelut.get(i).getArvosana();
            String arvo = Integer.toString(arvosana);
            JPanel testi = new JPanel();
            JLabel grade = new JLabel("Ladattiin arvostelu arvosanalla " + arvo);
            testi.add(grade);*/

            /*Object[] ok = {"Ok"};
            JOptionPane.showOptionDialog(null, testi,
                    "Vahvistus", JOptionPane.YES_OPTION,
                    JOptionPane.PLAIN_MESSAGE, null, ok, null);*/

            //secondary.add(testi);

            int arvo = arvostelut.get(i).getArvosana();
            String arvosana = Integer.toString(arvo);
            String otsikko = arvostelut.get(i).getOtsikko();
            String aika = arvostelut.get(i).getDate();

            JLabel header = new JLabel(otsikko);
            JLabel grade = new JLabel(arvosana);
            JLabel date = new JLabel(aika);

            JPanel testi = new JPanel();
            testi.setLayout(new BoxLayout(testi, BoxLayout.LINE_AXIS));
            testi.add(grade);
            testi.add(header);
            testi.add(date);



            titlesPanel.add(testi);
            testi.setVisible(true);
            titlesPanel.revalidate();
            titlesPanel.repaint();

            //secondary.pack();

            /*secondary.add(testi);
            testi.setVisible(true);
            titlesScrollPane.add(testi);
            secondary.revalidate();
            secondary.repaint();*/

            /*Object[] ok = {"Ok"};
            JOptionPane.showOptionDialog(null, testi,
                    "ööh", JOptionPane.YES_OPTION,
                    JOptionPane.PLAIN_MESSAGE, null, ok, null);*/

        }
    }
}
