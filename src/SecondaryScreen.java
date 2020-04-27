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
        //secscreen.setBackground(Color.DARK_GRAY);
        //secondary.setBackground(Color.DARK_GRAY);
        secscreen.setSize(1200, 800);
        secscreen.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        JPanel upperPanel = new JPanel();
        upperPanel.setBackground(Color.LIGHT_GRAY);
        toolsPanel = new JPanel();
        //GridBagLayout gbLayout = new GridBagLayout();
        //toolsPanel.setLayout(gbLayout);
        //GridBagConstraints gbConstraint = new GridBagConstraints();
        toolsPanel.setLayout(new BoxLayout(toolsPanel, BoxLayout.LINE_AXIS));
        //toolsPanel.setMaximumSize(new Dimension(200, 50));
        titlesPanel = new JPanel();
        titlesPanel.setLayout(new BoxLayout(titlesPanel, BoxLayout.PAGE_AXIS));
        titlesPanel.setBackground(Color.LIGHT_GRAY);

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
        addwindow.setSize(400,600);
        //Panel panel = new Panel();
        JPanel panel = new JPanel();
        panel.setBackground(Color.LIGHT_GRAY);
        panel.setSize(400, 600);
        panel.setMaximumSize(new Dimension(400,600));
        panel.setPreferredSize(new Dimension(400,600));
        //GridBagLayout layout = new GridBagLayout();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel title = new JLabel(selectedTitleName);
        panel.add(title);
        JLabel category = new JLabel(selectedTitleCategory);
        JTextField headerfield = new JTextField(1);
        headerfield.setMaximumSize(new Dimension(150,15));
        headerfield.setAlignmentX(addwindow.CENTER_ALIGNMENT);

        String grades[]={"1","2","3","4","5","6","7","8","9","10"};
        JComboBox grade = new JComboBox(grades);
        grade.setMaximumSize(new Dimension(150,15));
        JTextArea reviewtext = new JTextArea(30,50);
        //reviewtext.setMaximumSize(new Dimension(350, 500)); //500 500
        //reviewtext.setPreferredSize(new Dimension(350,500));
        reviewtext.setAlignmentX(addwindow.CENTER_ALIGNMENT);

        JScrollPane rullatirullaa = new JScrollPane(reviewtext);
        rullatirullaa.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        JPanel textArea = new JPanel();
        textArea.setLayout(new BoxLayout(textArea, BoxLayout.LINE_AXIS));
        textArea.add(rullatirullaa);

        JButton plusmedia = new JButton("+Media");
        plusmedia.setAlignmentX(addwindow.CENTER_ALIGNMENT);

        //JButton savebutton = new JButton("Tallenna");
        //JButton cancelbutton = new JButton("Peru");

        panel.add(category);
        panel.add(headerfield);
        panel.add(grade);
        //panel.add(reviewtext);
        //panel.add(rullatirullaa);
        panel.add(textArea);
        panel.add(plusmedia);

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
                /*for(int i=0;i<temp.size();i++) {
                    ReviewPiece candidant = temp.get(i);
                    if(candidant.getNimike()!=selectedTitleName ||
                            candidant.getKategoria()!=selectedTitleCategory) {
                        temp.remove(i);
                        //i--;
                    }
                }*/
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
        titlesPanel.removeAll();
        titlesPanel.add(Box.createRigidArea(new Dimension(7,7)));
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
            if (arvostelut.get(i).getNimike()!=selectedTitleName ||
            arvostelut.get(i).getKategoria()!=selectedTitleCategory) {



            int arvo = arvostelut.get(i).getArvosana();
            String arvosana = Integer.toString(arvo);
            String otsikko = arvostelut.get(i).getOtsikko();
            String aika = arvostelut.get(i).getDate();

            JLabel header = new JLabel(otsikko);
            JLabel grade = new JLabel(arvosana);
            JLabel date = new JLabel(aika);
            JButton open = new JButton("Avaa");
            int test = i;
            open.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    openReview(test);
                }
            });
            JButton edit = new JButton("Muokkaa");
            edit.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    editReview(test);
                }
            });

            JPanel testi = new JPanel();
            testi.setLayout(new BoxLayout(testi, BoxLayout.LINE_AXIS));
            testi.setBackground(Color.WHITE);
            testi.add(grade);
            testi.add(Box.createRigidArea(new Dimension(5, 0)));
            testi.add(header);
            testi.add(Box.createHorizontalGlue());
            testi.add(date);
            testi.add(Box.createRigidArea(new Dimension(5,0)));
            testi.add(open);
            testi.add(Box.createRigidArea(new Dimension(5,0)));
            testi.add(edit);
            testi.add(Box.createRigidArea(new Dimension(5,0)));



            titlesPanel.add(testi);
            testi.setVisible(true);
            titlesPanel.add(Box.createRigidArea(new Dimension(7,7)));
            titlesPanel.revalidate();
            titlesPanel.repaint();
            }
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

    public void editReview(int i) {
        ReviewPiece temp = arvostelut.get(i);
        JFrame addwindow = new JFrame("Muokkaa arvostelua");
        addwindow.setSize(400,600);
        addwindow.setBackground(Color.LIGHT_GRAY);
        //Panel panel = new Panel();
        JPanel panel = new JPanel();
        panel.setBackground(Color.WHITE);
        panel.setSize(400, 600);
        panel.setMaximumSize(new Dimension(400,600));
        panel.setPreferredSize(new Dimension(400,600));
        //GridBagLayout layout = new GridBagLayout();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel title = new JLabel(selectedTitleName);
        title.setAlignmentX(addwindow.CENTER_ALIGNMENT);
        JLabel nimike = new JLabel("Nimike: ");
        JPanel nimikealue = new JPanel();
        nimikealue.setLayout(new BoxLayout(nimikealue, BoxLayout.LINE_AXIS));
        nimikealue.add(nimike);
        nimikealue.add(title);

        JLabel category = new JLabel(selectedTitleCategory);
        category.setAlignmentX(addwindow.CENTER_ALIGNMENT);
        JLabel kategoria = new JLabel("Kategoria: ");
        JPanel kategoriaalue = new JPanel();
        kategoriaalue.setLayout(new BoxLayout(kategoriaalue, BoxLayout.LINE_AXIS));
        kategoriaalue.add(kategoria);
        kategoriaalue.add(category);

        JTextField headerfield = new JTextField(temp.getOtsikko(), 1);
        headerfield.setMaximumSize(new Dimension(150,15));
        headerfield.setAlignmentX(addwindow.CENTER_ALIGNMENT);

        int arvotemp = temp.getArvosana();
        String arvosanatemp = Integer.toString(arvotemp);
        String grades[]={"1","2","3","4","5","6","7","8","9","10"};
        JComboBox grade = new JComboBox(grades);
        grade.setSelectedItem(arvosanatemp);
        grade.setMaximumSize(new Dimension(150,15));
        JTextArea reviewtext = new JTextArea(temp.getArvosteluteksti(),
                20,50);
        reviewtext.setWrapStyleWord(true);
        reviewtext.setLineWrap(true);
        //reviewtext.setMaximumSize(new Dimension(350, 400)); //500 500
        //reviewtext.setPreferredSize(new Dimension(350,400));

        reviewtext.setAlignmentX(addwindow.CENTER_ALIGNMENT);

        JScrollPane rullatirullaa = new JScrollPane(reviewtext);
        rullatirullaa.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        JButton plusmedia = new JButton("+Media");
        plusmedia.setAlignmentX(addwindow.CENTER_ALIGNMENT);

        JButton delete = new JButton("Poista arvostelu");
        delete.setAlignmentX(addwindow.CENTER_ALIGNMENT);
        //delete.addActionListener((event) -> deleteReview(i));
        delete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteReview(i);
                Window w = SwingUtilities.getWindowAncestor(delete);
                if(w != null) { w.dispose(); }
            }
        });

        //JButton savebutton = new JButton("Tallenna");
        //JButton cancelbutton = new JButton("Peru");

        //panel.add(title);
        panel.add(nimikealue);
        //panel.add(category);
        panel.add(kategoriaalue);
        panel.add(headerfield);
        panel.add(grade);

        JPanel textArea = new JPanel();
        textArea.setLayout(new BoxLayout(textArea, BoxLayout.LINE_AXIS));
        //textArea.add(reviewtext);
        textArea.add(rullatirullaa);
        panel.add(textArea);
        //panel.add(reviewtext);
        //panel.add(rullatirullaa);

        panel.add(plusmedia);
        panel.add(delete);

        //panel.setBorder(BorderFactory.createTitledBorder("demo"));
        addwindow.getContentPane().add(panel);
        addwindow.add(panel);
        //addwindow.setVisible(true);

        Object[] savecancelbuttons = {"Peru", "Tallenna"};
        int result = JOptionPane.showOptionDialog(null, panel,
                "Muokkaa arvostelua", JOptionPane.YES_NO_OPTION,
                JOptionPane.PLAIN_MESSAGE, null, savecancelbuttons, null);
        //NO_OPTION on oikealla puolella ruutua, joten Tallenna-nappi on oikeasti "EI" eli NO-nappi
        //Siksi "Tallenna"-nappi onkin ei-nappi koodin mukaan
        if(result == JOptionPane.NO_OPTION){
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDateTime reviewtime = LocalDateTime.now();
            String aika = reviewtime.format(dtf);
            /*ReviewPiece uusi = new ReviewPiece(
                    selectedTitleName,
                    selectedTitleCategory,
                    headerfield.getText(),
                    grade.getSelectedIndex()+1,
                    reviewtext.getText(),
                    aika);*/
            arvostelut.get(i).setOtsikko(headerfield.getText());
            arvostelut.get(i).setArvosana(grade.getSelectedIndex()+1);
            arvostelut.get(i).setDate(aika);
            arvostelut.get(i).setArvosteluteksti(reviewtext.getText());

            JPanel added = new JPanel();
            added.setSize(400,800);
            JLabel otsikko = new JLabel(headerfield.getText());
            int a = arvostelut.get(i).getArvosana();
            String arvo = Integer.toString(a);
            JLabel arvosana = new JLabel(arvo);
            JLabel testi = new JLabel("hei");

            added.add(otsikko);
            added.add(arvosana);
            //addwindow.add(added);
            Object[] ok = {"Arvostelu muokattiin onnistuneesti"};
            JOptionPane.showOptionDialog(null, added,
                    "Vahvistus", JOptionPane.YES_OPTION,
                    JOptionPane.PLAIN_MESSAGE, null, ok, null);
            //arvostelut.add(uusi);
            SaveReviews();
            loadReviews();
            InitializeReviews();

            /*addwindow.add(otsikko);
            addwindow.add(arvosana);
            addwindow.add(testi);*/

            //addwindow.setVisible(true);

        }

    }

    public void openReview(int i) {
        JPanel displayPanel = new JPanel();
        displayPanel.setMaximumSize(new Dimension(600,300));
        displayPanel.setPreferredSize(new Dimension(600,300));
        displayPanel.setLayout(new BoxLayout(displayPanel, BoxLayout.PAGE_AXIS));

        JPanel headerAndGrade = new JPanel();
        //parentikkunan leveys
        //ota koko leveys saatavilla
        headerAndGrade.setMaximumSize(new Dimension(1000,50));
        headerAndGrade.setPreferredSize(new Dimension(1000,50));
        headerAndGrade.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.red), headerAndGrade.getBorder()));
        headerAndGrade.setLayout(new BoxLayout(headerAndGrade, BoxLayout.LINE_AXIS));
        ReviewPiece temp = arvostelut.get(i);
        JLabel grade = new JLabel(Integer.toString(temp.getArvosana()));
        JLabel header = new JLabel(temp.getOtsikko());
        headerAndGrade.add(Box.createRigidArea(new Dimension(5,0)));
        headerAndGrade.add(grade);
        headerAndGrade.add(Box.createRigidArea(new Dimension(15,0)));
        headerAndGrade.add(header);
        headerAndGrade.add(Box.createRigidArea(new Dimension(5,0)));

        displayPanel.add(headerAndGrade);

        /*JLabel reviewtext = new JLabel(temp.getArvosteluteksti());
        displayPanel.add(reviewtext);
        reviewtext.setMaximumSize(new Dimension(500,500));
        reviewtext.setPreferredSize(new Dimension(500,500));*/

        JPanel test = new JPanel();
        test.setLayout(new BoxLayout(test, BoxLayout.PAGE_AXIS));
        //Multiline, GridLayout
        JTextArea hehe = new JTextArea(temp.getArvosteluteksti(), 5,
                60);
                //displayPanel.getWidth());
        hehe.setWrapStyleWord(true);
        hehe.setLineWrap(true);
        JScrollPane rullatirullaa = new JScrollPane(hehe);
        rullatirullaa.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        test.add(Box.createVerticalGlue());

        //test.add(hehe);
        JPanel textArea = new JPanel();
        textArea.setLayout(new BoxLayout(textArea, BoxLayout.LINE_AXIS));
        textArea.add(rullatirullaa);

        //test.add(rullatirullaa);
        test.add(textArea);
        displayPanel.add(test);
        displayPanel.add(Box.createVerticalGlue());

        Object[] sulje = {"Sulje"};
        JOptionPane.showOptionDialog(null, displayPanel,
                "KLOÄppi", JOptionPane.YES_OPTION,
                JOptionPane.PLAIN_MESSAGE, null, sulje, null);
    }

    public void deleteReview(int i) {
        //ReviewPiece temp = arvostelut.get(i);


        JPanel deleteConfirmation = new JPanel();
        JLabel checkConsent = new JLabel("Haluatko varmasti poistaa arvostelun?");
        deleteConfirmation.add(checkConsent);

        Object[] savecancelbuttons = {"Ei", "Kyllä"};
        int result = JOptionPane.showOptionDialog(null, deleteConfirmation,
                "", JOptionPane.YES_NO_OPTION,
                JOptionPane.PLAIN_MESSAGE, null, savecancelbuttons, null);
        //NO_OPTION on oikealla puolella ruutua, joten Tallenna-nappi on oikeasti "EI" eli NO-nappi
        //Siksi "Tallenna"-nappi onkin ei-nappi koodin mukaan
        if (result == JOptionPane.NO_OPTION) {
            arvostelut.remove(i);
            JPanel deletedMessage = new JPanel();
            JLabel deleted = new JLabel("Arvostelu poistettiin");
            Object[] ok = {"Ok"};
            JOptionPane.showOptionDialog(null, deletedMessage,
                    "", JOptionPane.YES_OPTION,
                    JOptionPane.PLAIN_MESSAGE, null, ok, null);
            //arvostelut.add(uusi);
            SaveReviews();
            loadReviews();
            InitializeReviews();

            //
        }
    }

}
