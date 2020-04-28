import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.util.ArrayList;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;

public class SecondaryScreen {

    //private JFrame secondary;
    ArrayList<ReviewPiece> arvostelut = new ArrayList<>();

    public String selectedTitleName;
    public String selectedTitleCategory;
    //private JDialog secscreen;
    private JFrame mainScrFrame; //Frame, jotta nimikelistanäkymänä voidaan manipuloida
    private JFrame secondaryFrame; //pääikkuna arvostelujen hallinnoimiselle
    private JPanel toolsPanel; //paneeli hakutoiminnoille ja työkalupalkille
    private JPanel titlesPanel; //paneeli arvostelulistalle
    private JSplitPane mainSplitPane; //pääpaneeli, johon yllä olevat paneelit laitetaan

    public SecondaryScreen(JFrame mainScreenFrame, String title, String category){
        mainScrFrame = mainScreenFrame;
        selectedTitleName = title;
        selectedTitleCategory = category;
        loadReviews();
        MakeWindow();
        buildMenuBar();

    }
    private void MakeWindow() {
        secondaryFrame = new JFrame("Arvosteluohjelma");
        //JDialog secscreen = new JDialog(secondary, "Arvostelut");
        //secscreen.setBackground(Color.DARK_GRAY);
        //secondary.setBackground(Color.DARK_GRAY);
        //secscreen.setSize(1200, 800);
        //secscreen.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        secondaryFrame.setSize(1200,800);
        secondaryFrame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

        secondaryFrame.addWindowListener(new WindowAdapter()
        {
            @Override
            public void windowClosing(WindowEvent event){
                mainScrFrame.setVisible(true);
                secondaryFrame.dispose();
            }
        });

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
        //secscreen.add(titlesScrollPane);
        secondaryFrame.add(titlesScrollPane);
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
        JLabel kategoria = new JLabel ("Kategoria: " + selectedTitleCategory);
        toolsPanel.add(kategoria);
        toolsPanel.add(Box.createRigidArea(new Dimension(5,5)));
        toolsPanel.add(Box.createHorizontalGlue());


        //Tehdään hakemistoiminto
        JLabel hakuehdot = new JLabel("Rajoita hakua:");
        String[] hakutermit={"Uusin", "Arvosana"};
        JComboBox<String> sortby = new JComboBox<>(hakutermit);
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
        //toolsPanel.add(newReview);

        JButton backButton = new JButton("Palaa");
        backButton.setOpaque(false);
        backButton.setFocusPainted(false);
        backButton.setBorderPainted(false);
        backButton.setContentAreaFilled(false);
        backButton.setBorder(BorderFactory.createEmptyBorder(0,0,0,0));

        upperPanel.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 1;
        c.gridy = 0;
        c.weightx = 0.5;
        c.ipady = 10;
        upperPanel.add(title, c);

        c.gridx = 1;
        c.gridy = 1;
        upperPanel.add(kategoria, c);

        c.gridx = 3;
        c.gridy = 1;
        c.gridheight = 1;
        c.gridwidth = 1;
        c.ipady = 40;
        c.ipadx = 0;
        c.weightx = 0.5;
        c.weighty = 0.5;
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

        //secscreen.add(mainSplitPane);
        secondaryFrame.add(mainSplitPane);
        secondaryFrame.setLocationRelativeTo(null);
        secondaryFrame.setVisible(true);
        //secscreen.setVisible(true);
        mainSplitPane.setDividerLocation(0.07);
        //secscreen.setResizable(false);
        secondaryFrame.setResizable(false);
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

    private void buildMenuBar(){
        // Luodaan menupalkin eri menut ja itse palkki
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("Tiedosto");
        JMenu optionsMenu = new JMenu("Asetukset");
        JMenu helpMenu = new JMenu("Ohjeet");

        // Lisätään eri vaihtoehdoille toimintonsa
        JMenuItem exitItem = new JMenuItem("Lopeta ohjelma");
        exitItem.setToolTipText("Lopettaa ohjelman toiminnan.");
        // "Lopeta ohjelma" kohtaa painaessa ohjelma sammuu
        exitItem.addActionListener((event) -> System.exit(0));

        JMenuItem optionsItem = new JMenuItem("Asetukset");
        optionsItem.setToolTipText("Avaa asetukset ja muuta niitä.");
        optionsItem.addActionListener((event) -> openOptionsWindow());

        JMenuItem helpItem = new JMenuItem("Apua");
        helpItem.setToolTipText("Tietoja ohjelmasta ja käyttöohje.");
        helpItem.addActionListener((event) -> openHelpWindow());

        // Lisätään menuihin niille kuuluvat itemit
        fileMenu.add(exitItem);
        optionsMenu.add(optionsItem);
        helpMenu.add(helpItem);

        // Lisätään menupalkkiin sen menut
        menuBar.add(fileMenu);
        menuBar.add(optionsMenu);
        menuBar.add(helpMenu);

        // Lopuksi laitetaan menupalkki osaksi framea (ja näkyviin)
        secondaryFrame.setJMenuBar(menuBar);
    }

    // Avaa help dialogin, kun help nappia painetaan menussa
    private void openHelpWindow(){

        // Help -ikkunan sisältö tähän stringiin
        final  String helpContent = "Tähän tulee ohjeet ohjelman käyttöön ja selitys siitä, mikä tämä ohjelma on.";

        // frame help ikkunalle
        JFrame helpFrame = new JFrame("Apua ja infoa");
        helpFrame.setSize(600, 440);

        // Paneeli frameen. setLocationRelativeTo(null [object]) keskittää ikkunan
        JPanel helpPanel = new JPanel();
        helpFrame.setLocationRelativeTo(null);

        // JOptionPane dialogi, parempi kuin pelkkä frame/pane systeemi, koska vaatii fokuksen siihen asti että ikkuna suljetaan
        JOptionPane.showMessageDialog(helpFrame, helpContent, "Apua ja infoa", JOptionPane.PLAIN_MESSAGE);
    }

    // Avaa options dialogin kun options nappia painetaan menussa
    private void openOptionsWindow(){

        JLabel langLabel = new JLabel("Kieli:");
        JLabel ratingsLabel = new JLabel("Piilota arvosanat?");

        // Drop-down valikko
        String[] languages = {"English", "Suomi"};
        JComboBox<String> langChoice = new JComboBox<>(languages);
        // Defaulttina valittu indeksi 1 (alkaa nollasta)
        langChoice.setSelectedIndex(1);

        JCheckBox disableRatings = new JCheckBox();

        GridLayout grid = new GridLayout(3,2); //GridLayout, jossa 3 riviä, 2 pystyriviä
        JPanel optionsPanel = new JPanel();
        optionsPanel.setLayout(grid);

        optionsPanel.add(langLabel);
        optionsPanel.add(langChoice);
        optionsPanel.add(ratingsLabel);
        optionsPanel.add(disableRatings);

        Object[] optionsButtons = {"Tallenna", "Peruuta"};

        //Luodaan options dialogi
        JOptionPane.showOptionDialog(secondaryFrame, optionsPanel, "Options", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, optionsButtons, optionsButtons[1]);
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
        JLabel titleIs = new JLabel("Nimike: ");
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.LINE_AXIS));
        titlePanel.add(titleIs);
        titlePanel.add(title);
        titlePanel.setBackground(Color.LIGHT_GRAY);
        panel.add(titlePanel);
        //panel.add(title);

        JLabel category = new JLabel(selectedTitleCategory);
        JLabel categoryIs = new JLabel("Kategoria: ");
        JPanel categoryPanel = new JPanel();
        categoryPanel.setLayout(new BoxLayout(categoryPanel, BoxLayout.LINE_AXIS));
        categoryPanel.add(categoryIs);
        categoryPanel.add(category);
        categoryPanel.setBackground(Color.LIGHT_GRAY);
        panel.add(categoryPanel);

        JTextField headerfield = new JTextField(1);
        headerfield.setMaximumSize(new Dimension(150,15));
        headerfield.setAlignmentX(addwindow.CENTER_ALIGNMENT);
        JLabel headerIs = new JLabel("Otsikko: ");
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.LINE_AXIS));
        headerPanel.add(headerIs);
        headerPanel.add(headerfield);
        headerPanel.setBackground(Color.LIGHT_GRAY);
        panel.add(headerPanel);

        String[] grades={"1","2","3","4","5","6","7","8","9","10"};
        JComboBox<String> grade = new JComboBox<>(grades);
        grade.setMaximumSize(new Dimension(150,15));
        JLabel gradeIs = new JLabel("Arvosana: ");
        JPanel gradePanel = new JPanel();
        gradePanel.setLayout(new BoxLayout(gradePanel, BoxLayout.LINE_AXIS));
        gradePanel.add(gradeIs);
        gradePanel.add(grade);
        gradePanel.setBackground(Color.LIGHT_GRAY);
        panel.add(gradePanel);


        JTextArea reviewtext = new JTextArea(20,50);
        //reviewtext.setMaximumSize(new Dimension(350, 500)); //500 500
        //reviewtext.setPreferredSize(new Dimension(350,500));
        reviewtext.setAlignmentX(addwindow.CENTER_ALIGNMENT);
        reviewtext.setWrapStyleWord(true);
        reviewtext.setLineWrap(true);

        JScrollPane rullatirullaa = new JScrollPane(reviewtext);
        rullatirullaa.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        /*JPanel textArea = new JPanel();
        textArea.setLayout(new BoxLayout(textArea, BoxLayout.LINE_AXIS));
        textArea.add(rullatirullaa);*/
        //panel.add(textArea);
        panel.add(rullatirullaa);

        JButton plusmedia = new JButton("+Media");
        plusmedia.setAlignmentX(addwindow.CENTER_ALIGNMENT);

        //JButton savebutton = new JButton("Tallenna");
        //JButton cancelbutton = new JButton("Peru");

        //panel.add(category);
        //panel.add(headerfield);
        //panel.add(grade);
        //panel.add(reviewtext);
        //panel.add(rullatirullaa);

        panel.add(plusmedia);

        /*panel.setBorder(BorderFactory.createTitledBorder("demo"));
        addwindow.getContentPane().add(panel);
        addwindow.add(panel);*/
        //addwindow.setVisible(true);

        Object[] savecancelbuttons = {"Tallenna", "Peruuta"};
        int result = JOptionPane.showOptionDialog(secondaryFrame, panel,
                "Uusi arvostelu", JOptionPane.YES_NO_OPTION,
                JOptionPane.PLAIN_MESSAGE, null, savecancelbuttons, null);
        //NO_OPTION on oikealla puolella ruutua, joten Tallenna-nappi on oikeasti "EI" eli NO-nappi
        //Siksi "Tallenna"-nappi onkin ei-nappi koodin mukaan
        if(result == JOptionPane.YES_OPTION){
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDateTime reviewTime = LocalDateTime.now();
            String aika = reviewTime.format(dtf);

            if (headerfield.getText().isBlank()) {
                JPanel noHeader = new JPanel();
                JLabel noHeaderWarning = new JLabel("Et antanut otsikkoa");
                noHeader.add(noHeaderWarning);
                Object[] ok = {"Ok"};
                JOptionPane.showOptionDialog(secondaryFrame, noHeader,
                        "Virhe!", JOptionPane.YES_OPTION,
                        JOptionPane.PLAIN_MESSAGE, null, ok, null);
                addwindow.dispose();
                return;
            }
            if (reviewtext.getText().isBlank()) {
                JPanel noReviewtext = new JPanel();
                JLabel noReviewTextWarning = new JLabel("Et kirjoittanut arvostelutekstiä");
                noReviewtext.add(noReviewTextWarning);
                JOptionPane.showMessageDialog(secondaryFrame, noReviewtext,
                        "Virhe!", JOptionPane.ERROR_MESSAGE);
                addwindow.dispose();
                return;
            }

            ReviewPiece uusi = new ReviewPiece(
                    selectedTitleName,
                    selectedTitleCategory,
                    headerfield.getText(),
                    grade.getSelectedIndex()+1,
                    reviewtext.getText(),
                    aika);

            JPanel added = new JPanel();
            added.setSize(400,800);
            JLabel otsikko = new JLabel("Arvostelusi lisättiin onnistuneesti");
            //new JLabel(headerfield.getText());
            int a = uusi.getArvosana();
            String arvo = Integer.toString(a);
            JLabel arvosana = new JLabel(arvo);
            JLabel testi = new JLabel("hei");

            added.add(otsikko);
            //added.add(arvosana);
            //addwindow.add(added);
            /*Object[] ok = {"Ok"};
            JOptionPane.showOptionDialog(null, added,
                    "", JOptionPane.YES_OPTION,
                    JOptionPane.PLAIN_MESSAGE, null, ok, null);*/
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
            String directory = System.getProperty("user.dir") + File.separator + "object.ser";
            System.out.println(directory);
            FileOutputStream file = new FileOutputStream(directory);
            ObjectOutputStream out = new ObjectOutputStream(file);
            out.writeObject(arvostelut);

            JPanel added = new JPanel();
            JLabel addedConfirmation = new JLabel("Muutoksesi tallennettiin onnistuneesti");
            added.add(addedConfirmation);

            JOptionPane.showMessageDialog(titlesPanel, added,
                    "Muutokset onnistuivat", JOptionPane.PLAIN_MESSAGE);

        } catch (IOException e){
            e.printStackTrace();
            JPanel notAdded = new JPanel();
            JLabel notAddedWarning = new JLabel("Muutoksiasi ei voitu tehdä");
            notAdded.add(notAddedWarning);

            JOptionPane.showMessageDialog(secondaryFrame, notAdded,
                    "Virhe", JOptionPane.ERROR_MESSAGE);
        } //catch (FileNotFoundException e){}
    }

    public void loadReviews() {

        File userDirFile = new File(System.getProperty("user.dir"));
        String fullFile = File.separator + "object.ser";
        File fileToCheck = new File(userDirFile, fullFile);

        if(!fileToCheck.isFile()) {
            File objSer = new File("object.ser");
            try {
                objSer.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                FileInputStream file = new FileInputStream(System.getProperty("user.dir") +
                        File.separator + "object.ser");
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
            ReviewPiece temp = arvostelut.get(i);
            if (temp.getNimike().equals(selectedTitleName) &&
            temp.getKategoria().equals(selectedTitleCategory)) {

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
            }

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
        nimikealue.setBackground(Color.LIGHT_GRAY);

        JLabel category = new JLabel(selectedTitleCategory);
        category.setAlignmentX(addwindow.CENTER_ALIGNMENT);
        JLabel kategoria = new JLabel("Kategoria: ");
        JPanel kategoriaalue = new JPanel();
        kategoriaalue.setLayout(new BoxLayout(kategoriaalue, BoxLayout.LINE_AXIS));
        kategoriaalue.add(kategoria);
        kategoriaalue.add(category);
        kategoriaalue.setBackground(Color.LIGHT_GRAY);

        JTextField headerfield = new JTextField(temp.getOtsikko(), 1);
        headerfield.setMaximumSize(new Dimension(150,15));
        headerfield.setAlignmentX(addwindow.CENTER_ALIGNMENT);

        int arvotemp = temp.getArvosana();
        String arvosanatemp = Integer.toString(arvotemp);
        String[] grades={"1","2","3","4","5","6","7","8","9","10"};
        JComboBox<String> grade = new JComboBox<>(grades);
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
        textArea.setBackground(Color.LIGHT_GRAY);
        panel.add(textArea);
        //panel.add(reviewtext);
        //panel.add(rullatirullaa);

        panel.add(plusmedia);
        panel.add(delete);

        //panel.setBorder(BorderFactory.createTitledBorder("demo"));
        addwindow.getContentPane().add(panel);
        addwindow.add(panel);
        //addwindow.setVisible(true);

        Object[] savecancelbuttons = {"Tallenna", "Peruuta"};
        int result = JOptionPane.showOptionDialog(secondaryFrame, panel,
                "Muokkaa arvostelua", JOptionPane.YES_NO_OPTION,
                JOptionPane.PLAIN_MESSAGE, null, savecancelbuttons, null);
        //NO_OPTION on oikealla puolella ruutua, joten Tallenna-nappi on oikeasti "EI" eli NO-nappi
        //Siksi "Tallenna"-nappi onkin ei-nappi koodin mukaan
        if(result == JOptionPane.YES_OPTION){
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDateTime reviewtime = LocalDateTime.now();
            String aika = reviewtime.format(dtf);

            if (headerfield.getText().isBlank()) {
                JPanel noHeader = new JPanel();
                JLabel noHeaderWarning = new JLabel("Et antanut otsikkoa");
                noHeader.add(noHeaderWarning);

                JOptionPane.showMessageDialog(addwindow, noHeader,
                        "Virhe!", JOptionPane.ERROR_MESSAGE);

                addwindow.dispose();
                return;
            }
            if (reviewtext.getText().isBlank()) {
                JPanel noReviewtext = new JPanel();
                JLabel noReviewTextWarning = new JLabel("Et kirjoittanut arvostelutekstiä");
                noReviewtext.add(noReviewTextWarning);

                JOptionPane.showMessageDialog(addwindow, noReviewtext,
                        "Virhe!", JOptionPane.ERROR_MESSAGE);

                addwindow.dispose();
                return;
            }
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
            /*Object[] ok = {"Arvostelu muokattiin onnistuneesti"};
            JOptionPane.showOptionDialog(null, added,
                    "Vahvistus", JOptionPane.YES_OPTION,
                    JOptionPane.PLAIN_MESSAGE, null, ok, null);*/
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
        displayPanel.setBackground(Color.LIGHT_GRAY);

        JPanel headerAndGrade = new JPanel();
        //parentikkunan leveys
        //ota koko leveys saatavilla
        headerAndGrade.setMaximumSize(new Dimension(1000,50));
        headerAndGrade.setPreferredSize(new Dimension(1000,50));
        //headerAndGrade.setBorder(BorderFactory.createCompoundBorder(
                //BorderFactory.createLineBorder(Color.red), headerAndGrade.getBorder()));
        headerAndGrade.setLayout(new BoxLayout(headerAndGrade, BoxLayout.LINE_AXIS));
        ReviewPiece temp = arvostelut.get(i);
        JLabel grade = new JLabel(Integer.toString(temp.getArvosana()));
        JLabel header = new JLabel(temp.getOtsikko());
        headerAndGrade.add(Box.createRigidArea(new Dimension(5,0)));
        headerAndGrade.add(grade);
        headerAndGrade.add(Box.createRigidArea(new Dimension(15,0)));
        headerAndGrade.add(header);
        headerAndGrade.add(Box.createRigidArea(new Dimension(5,0)));
        headerAndGrade.setBackground(Color.LIGHT_GRAY);

        displayPanel.add(headerAndGrade);

        /*JLabel reviewtext = new JLabel(temp.getArvosteluteksti());
        displayPanel.add(reviewtext);
        reviewtext.setMaximumSize(new Dimension(500,500));
        reviewtext.setPreferredSize(new Dimension(500,500));*/

        JPanel test = new JPanel();
        test.setLayout(new BoxLayout(test, BoxLayout.PAGE_AXIS));
        //Multiline, GridLayout
        JTextArea reviewTextGoesHere = new JTextArea(temp.getArvosteluteksti(), 5,
                60);
                //displayPanel.getWidth());
        reviewTextGoesHere.setWrapStyleWord(true);
        reviewTextGoesHere.setLineWrap(true);
        reviewTextGoesHere.setEditable(false);
        JScrollPane rullatirullaa = new JScrollPane(reviewTextGoesHere);
        rullatirullaa.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        test.add(Box.createVerticalGlue());

        //test.add(hehe);
        JPanel textArea = new JPanel();
        textArea.setLayout(new BoxLayout(textArea, BoxLayout.LINE_AXIS));
        textArea.add(rullatirullaa);
        textArea.setBackground(Color.LIGHT_GRAY);

        //test.add(rullatirullaa);
        test.add(textArea);
        displayPanel.add(test);
        displayPanel.add(Box.createVerticalGlue());

        Object[] sulje = {"Sulje"};
        JOptionPane.showOptionDialog(displayPanel, displayPanel,
                "KLOÄppi", JOptionPane.YES_OPTION,
                JOptionPane.PLAIN_MESSAGE, null, sulje, null);
    }

    public void deleteReview(int i) {
        //ReviewPiece temp = arvostelut.get(i);

        JPanel deleteConfirmation = new JPanel();
        JLabel checkConsent = new JLabel("Haluatko varmasti poistaa arvostelun?");
        deleteConfirmation.add(checkConsent);

        Object[] savecancelbuttons = {"Kyllä", "Ei"};
        int result = JOptionPane.showOptionDialog(null, deleteConfirmation,
                "Oletko varma?", JOptionPane.YES_NO_OPTION,
                JOptionPane.PLAIN_MESSAGE, null, savecancelbuttons, null);

        if (result == JOptionPane.YES_OPTION) {
            arvostelut.remove(i);
            JPanel deletedReviewPanel = new JPanel();
            JLabel deletedMessage = new JLabel("Arvostelu poistettiin");
            deletedReviewPanel.add(deletedMessage);

            JOptionPane.showMessageDialog(deleteConfirmation, deletedReviewPanel,
                    "", JOptionPane.PLAIN_MESSAGE);

            //arvostelut.add(uusi);
            SaveReviews();
            loadReviews();
            InitializeReviews();
        }
    }

}
