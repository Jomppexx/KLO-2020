import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;

public class SecondaryScreen {

    //ArraList ladatuille arvosteluille.
    ArrayList<ReviewPiece> arvostelut = new ArrayList<>();


    public String selectedTitleName; //Valitun nimikkeen nimi.
    public String selectedTitleCategory; //Valitun nimikkeen kategoria.
    private JFrame mainScrFrame; //Frame, jotta nimikelistanäkymänä voidaan manipuloida.
    private JFrame secondaryFrame; //pääikkuna arvostelujen hallinnoimiselle.
    private JPanel toolsPanel; //paneeli hakutoiminnoille ja työkalupalkille.
    private JPanel titlesPanel; //paneeli arvostelulistalle.
    private JSplitPane mainSplitPane; //pääpaneeli, johon yllä olevat paneelit laitetaan.

    //Konstruktori luokalle SecondaryScreen. Referenssinä ovat mainScreenFrame manipulointia
    //varten, valitun nimikkeen nimi ja valitun nimikkeen kategoria.
    public SecondaryScreen(JFrame mainScreenFrame, String title, String category){
        //Normaali arvojen sijoitus
        mainScrFrame = mainScreenFrame;
        selectedTitleName = title;
        selectedTitleCategory = category;
        //Lataa arvostelut ArrayListiin nimeltä arvostelut
        loadReviews();
        //Rakentaa ikkunan
        MakeWindow();
        //Rakentaa esimerkiksi Optionsin ja Helpin sisältävän menupalkin
        buildMenuBar();

    }
    private void MakeWindow() {
        //luomme pääikkunan
        secondaryFrame = new JFrame("Arvosteluohjelma");
        secondaryFrame.setSize(1200,800);
        secondaryFrame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

        //Jotta pääikkunaa ei voi manipuloida samalla, kun pääikkunan
        //nimikkeen arvosteluja hallinnoidaan
        secondaryFrame.addWindowListener(new WindowAdapter()
        {
            @Override
            public void windowClosing(WindowEvent event){
                mainScrFrame.setVisible(true);
                secondaryFrame.dispose();
            }
        });

        //Paneeli nimikkeen tietojen näyttämiselle ja lisää/sortby/takaisin -toiminnoille.
        JPanel upperPanel = new JPanel();
        upperPanel.setBackground(Color.LIGHT_GRAY);
        //Paneeli työkaluille.
        toolsPanel = new JPanel();
        toolsPanel.setLayout(new BoxLayout(toolsPanel, BoxLayout.LINE_AXIS));
        //Paneeli arvosteluolioiden lataamiselle.
        titlesPanel = new JPanel();
        titlesPanel.setLayout(new BoxLayout(titlesPanel, BoxLayout.PAGE_AXIS));
        titlesPanel.setBackground(Color.LIGHT_GRAY);

        /*
         * Luodaan scrollPane ja liitetään se titlesPaneeliin, jotta title paneelia voi kelata
         * kun siihen tulee "liikaa" sisältöä, eikä se kaikki näy kerralla
         */
        // scrollPane, mahdollistaa ikkunan scrollaamisen...
        JScrollPane titlesScrollPane = new JScrollPane(titlesPanel);
        secondaryFrame.add(titlesScrollPane);
        titlesScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        // "Pääpaneeli" on splitpane, jossa yläosaan tulee hakutyökalujen paneeli ja alaosaan eri nimikkeet
        //mainSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, toolsPanel, titlesScrollPane);
        mainSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, upperPanel, titlesScrollPane);
        mainSplitPane.setOneTouchExpandable(false);
        mainSplitPane.setResizeWeight(0.5);
        mainSplitPane.setDividerSize(0);

        //Title ja kategoria
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

        //Tehdään nappi uusien arvostelujen lisäämiselle.
        JButton newReview = new JButton("+ Uusi arvostelu");
        newReview.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AddReview();
            }
        });

        //Tehdään nappi palaamiselle nimikelistaan.
        JButton backButton = new JButton("Palaa");

        backButton.addActionListener(event -> {
            mainScrFrame.setVisible(true);
            secondaryFrame.dispose();
        });

        backButton.setOpaque(false);
        backButton.setFocusPainted(false);
        backButton.setBorderPainted(false);
        backButton.setContentAreaFilled(false);
        backButton.setBorder(BorderFactory.createEmptyBorder(0,0,0,0));

        //Ladataan nappiin kuvake
        //Kuvan tulee olla samassa hakemistossa lähdekoodin kanssa
        try{
            Image img = ImageIO.read(getClass().getResource("resources/back-button.png"));
            backButton.setIcon(new ImageIcon(img));
        } catch(Exception ex){
            ex.printStackTrace();
        }

        //Asetetaan layoutmanager ja säädetään komponenttien olinpaikat.
        upperPanel.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        //x = 0, y = 0
        c.anchor = GridBagConstraints.LINE_START;
        c.gridheight = 2;
        c.gridwidth = 2;
        c.weightx = 0.1;
        c.insets = new Insets(0,20,0,0);
        upperPanel.add(backButton, c);

        c.anchor = GridBagConstraints.CENTER;
        c.weightx = 0.1;
        c.insets = new Insets(0,0,0,0);
        c.gridheight = 1;
        c.gridwidth = 1;
        c.gridx = 1;
        c.gridy = 0;
        upperPanel.add(title, c);

        c.gridy = 1;
        upperPanel.add(kategoria, c);
        c.insets = new Insets(0,0,0,0);

        c.fill = GridBagConstraints.NONE;
        c.weightx = 0.2;
        c.gridx = 3;
        c.gridy = 0;
        c.gridheight = 2;
        upperPanel.add(newReview, c);

        c.insets = new Insets(0,0,0,15);
        c.weightx = 0.1;
        c.gridx = 4;
        c.gridheight = 1;
        JLabel sorttext = new JLabel("Hakuehdot:");
        upperPanel.add(sorttext, c);

        c.gridy = 1;
        upperPanel.add(sortby, c);

        //Lisätään pääikkunaan sen komponentit.
        secondaryFrame.add(mainSplitPane);
        secondaryFrame.setLocationRelativeTo(null);
        secondaryFrame.setVisible(true);
        mainSplitPane.setDividerLocation(0.1);
        secondaryFrame.setResizable(false);
        //Renderöidään tallennetut arvostelut.
        InitializeReviews();

    }

    //buildMenuBar rakentaa menupalkin, joka lisätään pääikkunaan.
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

    //AddWindow on ikkuna uusien arvostelujen lisäämiselle.
    private void AddReview() {
        //Paneeli, johon lisäysikkunan komponentit säilötään.
        JPanel addReviewPanel = new JPanel();
        addReviewPanel.setBackground(Color.LIGHT_GRAY);
        addReviewPanel.setSize(400, 600);
        addReviewPanel.setMaximumSize(new Dimension(400,600));
        addReviewPanel.setPreferredSize(new Dimension(400,600));
        //Asetetaan BoxLayout elementtien laittamiseen Y-akselille.
        addReviewPanel.setLayout(new BoxLayout(addReviewPanel, BoxLayout.Y_AXIS));
        addReviewPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        //Lisätään nimikkeen nimi
        JLabel title = new JLabel(selectedTitleName);
        JLabel titleIs = new JLabel("Nimike: ");
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.LINE_AXIS));
        titlePanel.add(titleIs);
        titlePanel.add(title);
        titlePanel.setBackground(Color.LIGHT_GRAY);
        addReviewPanel.add(titlePanel);

        //Lisätään nimikkeen kategoria
        JLabel category = new JLabel(selectedTitleCategory);
        JLabel categoryIs = new JLabel("Kategoria: ");
        JPanel categoryPanel = new JPanel();
        categoryPanel.setLayout(new BoxLayout(categoryPanel, BoxLayout.LINE_AXIS));
        categoryPanel.add(categoryIs);
        categoryPanel.add(category);
        categoryPanel.setBackground(Color.LIGHT_GRAY);
        addReviewPanel.add(categoryPanel);

        //Lisätään otsikon tekstikenttä
        JTextField headerfield = new JTextField(1);
        headerfield.setMaximumSize(new Dimension(150,15));
        headerfield.setAlignmentX(addReviewPanel.CENTER_ALIGNMENT);
        JLabel headerIs = new JLabel("Otsikko: ");
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.LINE_AXIS));
        headerPanel.add(headerIs);
        headerPanel.add(headerfield);
        headerPanel.setBackground(Color.LIGHT_GRAY);
        addReviewPanel.add(headerPanel);

        //Lisätään arvosteludropdown
        String[] grades={"1","2","3","4","5","6","7","8","9","10"};
        JComboBox<String> grade = new JComboBox<>(grades);
        grade.setMaximumSize(new Dimension(150,15));
        JLabel gradeIs = new JLabel("Arvosana: ");
        JPanel gradePanel = new JPanel();
        gradePanel.setLayout(new BoxLayout(gradePanel, BoxLayout.LINE_AXIS));
        gradePanel.add(gradeIs);
        gradePanel.add(grade);
        gradePanel.setBackground(Color.LIGHT_GRAY);
        addReviewPanel.add(gradePanel);

        //Lisätään tekstikenttä arvostelun kirjoittamiselle.
        JTextArea reviewtext = new JTextArea(20,50);
        //reviewtext.setMaximumSize(new Dimension(350, 500)); //500 500
        //reviewtext.setPreferredSize(new Dimension(350,500));
        reviewtext.setAlignmentX(addReviewPanel.CENTER_ALIGNMENT);
        reviewtext.setWrapStyleWord(true);
        reviewtext.setLineWrap(true);

        //Lisätään scrollbar siltä varalta, että käyttäjä kirjoittaa paljon
        //ja kaikki teksti ei näy samalla kertaa
        JScrollPane rullatirullaa = new JScrollPane(reviewtext);
        rullatirullaa.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        addReviewPanel.add(rullatirullaa);

        //Lisätään "+Media" -nappi
        JButton plusmedia = new JButton("+Media");
        plusmedia.setAlignmentX(addReviewPanel.CENTER_ALIGNMENT);

        addReviewPanel.add(plusmedia);

        //Lisätään tallennusmekaniikka
        Object[] savecancelbuttons = {"Tallenna", "Peruuta"};
        int result = JOptionPane.showOptionDialog(secondaryFrame, addReviewPanel,
                "Uusi arvostelu", JOptionPane.YES_NO_OPTION,
                JOptionPane.PLAIN_MESSAGE, null, savecancelbuttons, null);
        //YES vastaa Tallenna-nappia!
        if(result == JOptionPane.YES_OPTION){
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDateTime reviewTime = LocalDateTime.now();
            String aika = reviewTime.format(dtf);

            //Ensin tarkistetaan puuttuvien tietojen varalta sekö otsikko
            //että arvosteluteksti
            if (headerfield.getText().isBlank()) {
                JPanel noHeader = new JPanel();
                JLabel noHeaderWarning = new JLabel("Et antanut otsikkoa");
                noHeader.add(noHeaderWarning);
                Object[] ok = {"Ok"};
                JOptionPane.showOptionDialog(secondaryFrame, noHeader,
                        "Virhe!", JOptionPane.YES_OPTION,
                        JOptionPane.PLAIN_MESSAGE, null, ok, null);
                //addwindow.dispose();
                return;
            }
            if (reviewtext.getText().isBlank()) {
                JPanel noReviewtext = new JPanel();
                JLabel noReviewTextWarning = new JLabel("Et kirjoittanut arvostelutekstiä");
                noReviewtext.add(noReviewTextWarning);
                JOptionPane.showMessageDialog(secondaryFrame, noReviewtext,
                        "Virhe!", JOptionPane.ERROR_MESSAGE);
                //addwindow.dispose();
                return;
            }

            //Luodaan uusi ReviewPiece tietojen tallentamiseksi.
            ReviewPiece uusi = new ReviewPiece(
                    selectedTitleName,
                    selectedTitleCategory,
                    headerfield.getText(),
                    grade.getSelectedIndex()+1,
                    reviewtext.getText(),
                    aika);

            //Lisätään uusi arvostelu ArrayListiin
            arvostelut.add(uusi);
            //Tallennetaan arvostelut kansioon
            SaveReviews();
            //Ladataan kansiosta arvostelut
            loadReviews();
            //Renderöidään arvosteluoliot näytölle
            InitializeReviews();
        }
    }

    //SaveReviews-funktio tallentaa arvosteluolioita sisältävän ArrayListin arvostelut
    //kansioon, josta koodi suoritettiin.
    public void SaveReviews () {
        try {
            //Sama kansio, josta koodi suoritettiin.
            //HUOMIO: INTELLIJ KIRJOITTAA OBJECT.SERIN SRC-KANSION YLEMPÄÄN KANSIOON
            String directory = System.getProperty("user.dir") + File.separator + "object.ser";
            System.out.println(directory);
            FileOutputStream file = new FileOutputStream(directory);
            ObjectOutputStream out = new ObjectOutputStream(file);
            out.writeObject(arvostelut);

            //tehdään vahvistusdialogi.
            JPanel added = new JPanel();
            JLabel addedConfirmation = new JLabel("Muutoksesi tallennettiin onnistuneesti");
            added.add(addedConfirmation);

            JOptionPane.showMessageDialog(titlesPanel, added,
                    "Muutokset onnistuivat", JOptionPane.PLAIN_MESSAGE);

        } catch (IOException e){
            e.printStackTrace();
            //Tehdään varoitusdialogi.
            JPanel notAdded = new JPanel();
            JLabel notAddedWarning = new JLabel("Muutoksiasi ei voitu tehdä");
            notAdded.add(notAddedWarning);

            JOptionPane.showMessageDialog(secondaryFrame, notAdded,
                    "Virhe", JOptionPane.ERROR_MESSAGE);
        } //catch (FileNotFoundException e){}
    }

    //loadReviews-funktio lataa arvosteluoliot kansiosta ja tallentaa oliot
    //arvostelut-ArrayListiin
    public void loadReviews() {

        //Sama kansio, josta koodi suoritettiin.
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
                } catch (ClassNotFoundException c) {
                    c.printStackTrace();
                }
                this.arvostelut = temp;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //InitializeReviews-funktio renderöi arvosteluoliot pääikkunaan arvostelut-ArrayListin
    //avulla.
    public void InitializeReviews() {
        //Poistamme kaikki "vanhat" eli aikaisemmat arvosteluoliot nätöltä,
        //ettemme saa duplikaattiolioita näkyviin.
        titlesPanel.removeAll();
        titlesPanel.add(Box.createRigidArea(new Dimension(7,7)));
        for(int i=0;i<arvostelut.size();i++) {
            //Oliota temp käytetään manipulointiin.
            ReviewPiece temp = arvostelut.get(i);
            if (temp.getNimike().equals(selectedTitleName) &&
                temp.getKategoria().equals(selectedTitleCategory)) {

                //Haetaan tarvitut arvostelun tiedot.
                int arvo = arvostelut.get(i).getArvosana();
                String arvosana = Integer.toString(arvo);
                String otsikko = arvostelut.get(i).getOtsikko();
                String aika = arvostelut.get(i).getDate();

                JLabel header = new JLabel(otsikko);
                JLabel grade = new JLabel(arvosana);
                JLabel date = new JLabel(aika);
                JButton open = new JButton("Avaa");
                int test = i;
                //Lisätään avausnapille kuuntelija.
                open.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                    openReview(test);
                }
                });
                JButton edit = new JButton("Muokkaa");
                //Lisätään muokkausnapille kuuntelija.
                edit.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                    editReview(test);
                }
                });

                //Luodaan itse näkyvä olio.
                JPanel testi = new JPanel();
                testi.setLayout(new BoxLayout(testi, BoxLayout.LINE_AXIS));
                testi.setBackground(Color.WHITE);
                testi.add(Box.createRigidArea(new Dimension(5,0)));
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

            //Koska muutimme käyttöliittymää koodin suorituksen aikana, joudumme
            //suorittamaan seuraavat komennot.
            titlesPanel.revalidate();
            titlesPanel.repaint();
        }
    }

    //editReview-funktio suorittaa arvostelun muokkauksen.
    public void editReview(int i) {
        //Väliaikinen olio manipulointia varten.
        ReviewPiece temp = arvostelut.get(i);
        //
        //POISTA ADDWINDOW
        //
        //JFrame addwindow = new JFrame("Muokkaa arvostelua");
        //addwindow.setSize(400,600);
        //addwindow.setBackground(Color.LIGHT_GRAY);
        //Tehdään JPanel muokattavan arvostelun tietojen esittämiseksi
        JPanel editReviewPanel = new JPanel();
        editReviewPanel.setBackground(Color.WHITE);
        editReviewPanel.setSize(400, 600);
        editReviewPanel.setMaximumSize(new Dimension(400,600));
        editReviewPanel.setPreferredSize(new Dimension(400,600));
        editReviewPanel.setLayout(new BoxLayout(editReviewPanel, BoxLayout.Y_AXIS));
        editReviewPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        //Alustetaan nimikkeen nimi ja laitetaan se JPaneeliin
        JLabel title = new JLabel(selectedTitleName);
        title.setAlignmentX(editReviewPanel.CENTER_ALIGNMENT);
        JLabel nimike = new JLabel("Nimike: ");
        JPanel nimikealue = new JPanel();
        nimikealue.setLayout(new BoxLayout(nimikealue, BoxLayout.LINE_AXIS));
        nimikealue.add(nimike);
        nimikealue.add(title);
        nimikealue.setBackground(Color.LIGHT_GRAY);

        //Alustetaan kategoriatiedot ja laitetaan ne JPaneeliin
        JLabel category = new JLabel(selectedTitleCategory);
        category.setAlignmentX(editReviewPanel.CENTER_ALIGNMENT);
        JLabel kategoria = new JLabel("Kategoria: ");
        JPanel kategoriaalue = new JPanel();
        kategoriaalue.setLayout(new BoxLayout(kategoriaalue, BoxLayout.LINE_AXIS));
        kategoriaalue.add(kategoria);
        kategoriaalue.add(category);
        kategoriaalue.setBackground(Color.LIGHT_GRAY);

        //Alustetaan otsikon tiedot
        JTextField headerfield = new JTextField(temp.getOtsikko(), 1);
        headerfield.setMaximumSize(new Dimension(150,15));
        headerfield.setAlignmentX(editReviewPanel.CENTER_ALIGNMENT);

        //Alustetaan arvosana ja arvosanat sisältävä dropdown
        int arvotemp = temp.getArvosana();
        String arvosanatemp = Integer.toString(arvotemp);
        String[] grades={"1","2","3","4","5","6","7","8","9","10"};
        JComboBox<String> grade = new JComboBox<>(grades);
        grade.setSelectedItem(arvosanatemp);
        grade.setMaximumSize(new Dimension(150,15));

        //Alustetaan arvosteluteksti
        JTextArea reviewtext = new JTextArea(temp.getArvosteluteksti(),
                20,50);
        reviewtext.setWrapStyleWord(true);
        reviewtext.setLineWrap(true);
        reviewtext.setAlignmentX(editReviewPanel.CENTER_ALIGNMENT);

        //Lisätään arvostelutekstiin scrollbar tarvittaessa
        JScrollPane rullatirullaa = new JScrollPane(reviewtext);
        rullatirullaa.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        //Alustetaan "+Media" -nappi
        JButton plusmedia = new JButton("+Media");
        plusmedia.setAlignmentX(editReviewPanel.CENTER_ALIGNMENT);

        //Alustetaan poistonappi ja lisätään sille kuuntelija
        JButton delete = new JButton("Poista arvostelu");
        delete.setAlignmentX(editReviewPanel.CENTER_ALIGNMENT);
        delete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteReview(i);
                Window w = SwingUtilities.getWindowAncestor(delete);
                if(w != null) { w.dispose(); }
            }
        });

        //Lisätään komponentit
        editReviewPanel.add(nimikealue);
        editReviewPanel.add(kategoriaalue);
        editReviewPanel.add(headerfield);
        editReviewPanel.add(grade);

        //Lisätään tekstikenttä ja scrollbar erilliseen JPaneliin, jotta
        //ne ovat vierekkäin
        JPanel textArea = new JPanel();
        textArea.setLayout(new BoxLayout(textArea, BoxLayout.LINE_AXIS));
        textArea.add(rullatirullaa);
        textArea.setBackground(Color.LIGHT_GRAY);
        editReviewPanel.add(textArea);

        editReviewPanel.add(plusmedia);
        editReviewPanel.add(delete);

        //addwindow.getContentPane().add(panel);
        //addwindow.add(panel);

        Object[] savecancelbuttons = {"Tallenna", "Peruuta"};
        int result = JOptionPane.showOptionDialog(secondaryFrame, editReviewPanel,
                "Muokkaa arvostelua", JOptionPane.YES_NO_OPTION,
                JOptionPane.PLAIN_MESSAGE, null, savecancelbuttons, null);
        //YES-nappi vastaa Tallenna-nappia!
        if(result == JOptionPane.YES_OPTION){
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDateTime reviewtime = LocalDateTime.now();
            String aika = reviewtime.format(dtf);

            //Tarkistetaan ensin, että kaikki vaaditut tiedot ovat täytettyinä
            //(otsikko ja arvosteluteksti)
            if (headerfield.getText().isBlank()) {
                JPanel noHeader = new JPanel();
                JLabel noHeaderWarning = new JLabel("Et antanut otsikkoa");
                noHeader.add(noHeaderWarning);

                JOptionPane.showMessageDialog(secondaryFrame, noHeader,
                        "Virhe!", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (reviewtext.getText().isBlank()) {
                JPanel noReviewtext = new JPanel();
                JLabel noReviewTextWarning = new JLabel("Et kirjoittanut arvostelutekstiä");
                noReviewtext.add(noReviewTextWarning);

                JOptionPane.showMessageDialog(secondaryFrame, noReviewtext,
                        "Virhe!", JOptionPane.ERROR_MESSAGE);

                return;
            }

            //Muutetaan eli muokataan tiedot käyttäjän haluamiksi tiedoiksi
            arvostelut.get(i).setOtsikko(headerfield.getText());
            arvostelut.get(i).setArvosana(grade.getSelectedIndex()+1);
            arvostelut.get(i).setDate(aika);
            arvostelut.get(i).setArvosteluteksti(reviewtext.getText());

            //Tallennetaan arvostelut
            SaveReviews();
            //Ladataan arvostelut
            loadReviews();
            //Renderöidään arvostelut
            InitializeReviews();


        }

    }

    public void openReview(int i) {
        JPanel displayPanel = new JPanel();
        displayPanel.setMaximumSize(new Dimension(600,300));
        displayPanel.setPreferredSize(new Dimension(600,300));
        displayPanel.setLayout(new BoxLayout(displayPanel, BoxLayout.PAGE_AXIS));
        displayPanel.setBackground(Color.LIGHT_GRAY);

        //Alustetaan arvostelun otsikko ja arvosana
        JPanel headerAndGrade = new JPanel();
        headerAndGrade.setMaximumSize(new Dimension(1000,50));
        headerAndGrade.setPreferredSize(new Dimension(1000,50));
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

        //Alustetaan arvostelutekstialue ja laitetaan sille tarvittaessa scrollbar
        JPanel reviewTextArea = new JPanel();
        reviewTextArea.setLayout(new BoxLayout(reviewTextArea, BoxLayout.PAGE_AXIS));
        JTextArea reviewTextGoesHere = new JTextArea(temp.getArvosteluteksti(), 5,
                60);
        reviewTextGoesHere.setWrapStyleWord(true);
        reviewTextGoesHere.setLineWrap(true);
        reviewTextGoesHere.setEditable(false);
        JScrollPane rullatirullaa = new JScrollPane(reviewTextGoesHere);
        rullatirullaa.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        reviewTextArea.add(Box.createVerticalGlue());

        //Lisätään arvosteluteksti textArea-JPanelin kautta
        JPanel textArea = new JPanel();
        textArea.setLayout(new BoxLayout(textArea, BoxLayout.LINE_AXIS));
        textArea.add(rullatirullaa);
        textArea.setBackground(Color.LIGHT_GRAY);

        reviewTextArea.add(textArea);
        displayPanel.add(reviewTextArea);
        displayPanel.add(Box.createVerticalGlue());

        //Tehdään itse ikkuna
        Object[] sulje = {"Sulje"};
        JOptionPane.showOptionDialog(displayPanel, displayPanel,
                "KLOÄppi", JOptionPane.YES_OPTION,
                JOptionPane.PLAIN_MESSAGE, null, sulje, null);
    }

    //deltedReview-funktio poistaa arvostelun.
    public void deleteReview(int i) {

        //Tehdään varoitustekstit
        JPanel deleteConfirmation = new JPanel();
        JLabel checkConsent = new JLabel("Haluatko varmasti poistaa arvostelun?");
        deleteConfirmation.add(checkConsent);

        //Tehdään varoitusikkuna
        Object[] savecancelbuttons = {"Kyllä", "Ei"};
        int result = JOptionPane.showOptionDialog(null, deleteConfirmation,
                "Oletko varma?", JOptionPane.YES_NO_OPTION,
                JOptionPane.PLAIN_MESSAGE, null, savecancelbuttons, null);

        //Poistetaan arvostelu
        if (result == JOptionPane.YES_OPTION) {
            arvostelut.remove(i);
            JPanel deletedReviewPanel = new JPanel();
            JLabel deletedMessage = new JLabel("Arvostelu poistettiin");
            deletedReviewPanel.add(deletedMessage);

            //Tallennetaan muutokset
            SaveReviews();
            //Vahvistusviesti poistolle
            JOptionPane.showMessageDialog(deleteConfirmation, deletedReviewPanel,
                    "", JOptionPane.PLAIN_MESSAGE);
            //Ladataan arvostelut
            loadReviews();
            //Renderöidään arvostelut
            InitializeReviews();
        }
    }

}
