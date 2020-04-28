import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.concurrent.atomic.AtomicReference;

// Luokka päänäkymälle
public class MainScreen extends JFrame {

    private ArrayList <String> entertainmentNameArray = new ArrayList<>();
    public ArrayList <EntertainmentPiece> epArray = new ArrayList<>();
    public JFrame mainFrame;
    private JPanel toolsPanel; //paneeli hakutoiminnoille ja työkalupalkille
    private JPanel titlesPanel; //paneeli nimikelistalle, josta pääsee arvosteluihin
    private JSplitPane mainSplitPane; //pääpaneeli, johon yllä olevat paneelit laitetaan

    //Initialisoidaan päänäkymä, luo elementit joista näkymä koostuu ja näyttää ne
    public MainScreen(){

        // Rakentaa framen ja sen sisällä olevat paneelit
        buildWindow();

        // Rakentaa näkymän yläosan työkalupalkin ja sen valikot
        buildMenuBar();

        loadEntertainmentPieces();

        toolsPanel.setBackground(Color.LIGHT_GRAY);
        titlesPanel.setBackground(Color.LIGHT_GRAY);

        mainFrame.setLocationRelativeTo(null);

        // Lopuksi freimi näkyviin ja ikkuna aukeaa
        mainFrame.setVisible(true);
        mainSplitPane.setDividerLocation(0.07);
    }

    // Rakentaa framen ja sen sisällä olevat paneelit
    private void buildWindow(){
        mainFrame = new JFrame("Arvosteluohjelma");
        mainFrame.setSize(1200,800);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        toolsPanel = new JPanel();
        //GridBagLayout gbLayout = new GridBagLayout();
        //toolsPanel.setLayout(gbLayout);
        //GridBagConstraints gbConstraint = new GridBagConstraints();
        toolsPanel.setLayout(new BoxLayout(toolsPanel, BoxLayout.LINE_AXIS));
        titlesPanel = new JPanel();
        titlesPanel.setLayout(new BoxLayout(titlesPanel, BoxLayout.PAGE_AXIS));

        /*
        * Luodaan scrollPane ja liitetään se titlesPaneeliin, jotta title paneelia voi kelata
        * kun siihen tulee "liikaa" sisältöä, eikä se kaikki näy kerralla
        */
        // scrollPane, mahdollistaa ikkunan scrollaamisen...
        JScrollPane titlesScrollPane = new JScrollPane(titlesPanel);
        mainFrame.add(titlesScrollPane);
        titlesScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        // Nappi uusien nimikkeiden (elokuvat, kirjat, ym.) lisäämistä varten
        JButton newTitleButton = new JButton("Lisää nimike");

        toolsPanel.add(Box.createRigidArea(new Dimension(15,0)));
        toolsPanel.add(newTitleButton, BorderLayout.LINE_START);
        newTitleButton.addActionListener((event) -> addNewTitle());

        JLabel sortLabel = new JLabel("Sorttaa:");
        JLabel searchPlaceholder = new JLabel("Hakupalkin placeholder");

        String[] sortingOptions = {"Aakkosjärjestys", "Kategorioittain"};
        JComboBox<String> sortingDrop = new JComboBox<>(sortingOptions);
        sortingDrop.setSize(20,10);

        sortingDrop.addActionListener((ActionListener) (event) -> {
            sortEPBoxes(sortingDrop.getSelectedItem());
        });

        toolsPanel.add(Box.createHorizontalGlue());
        toolsPanel.add(searchPlaceholder);
        toolsPanel.add(Box.createRigidArea(new Dimension(15,0)));
        toolsPanel.add(sortLabel);
        toolsPanel.add(Box.createRigidArea(new Dimension(15,0)));
        toolsPanel.add(sortingDrop);

        // "Pääpaneeli" on splitpane, jossa yläosaan tulee hakutyökalujen paneeli ja alaosaan eri nimikkeet
        mainSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, toolsPanel, titlesScrollPane);
        mainSplitPane.setOneTouchExpandable(false);
        mainSplitPane.setResizeWeight(0.5);
        mainSplitPane.setDividerSize(0);

        mainFrame.add(mainSplitPane);
        mainFrame.setResizable(false);
    }

    // Rakentaa näkymän yläosan työkalupalkin ja sen valikot
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
        mainFrame.setJMenuBar(menuBar);
    }

    // Avaa help dialogin, kun help nappia painetaan menussa
    private void openHelpWindow(){

        // Help -ikkunan sisältö tähän stringiin
        final  String helpContent = "Tähän tulee ohjeet ohjelman käyttöön ja selitys siitä, mikä tämä ohjelma on.";

        // frame help ikkunalle
        JFrame helpFrame = new JFrame("Apua ja infoa");
        helpFrame.setSize(600, 440);

        // JOptionPane dialogi, parempi kuin pelkkä frame/pane systeemi, koska vaatii fokuksen siihen asti että ikkuna suljetaan
        JOptionPane.showMessageDialog(mainFrame, helpContent, "Apua ja infoa", JOptionPane.PLAIN_MESSAGE);
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
        JOptionPane.showOptionDialog(mainFrame, optionsPanel, "Options", JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE, null, optionsButtons, optionsButtons[1]);
    }


    //Luo lisää nimike ikkunan
    //Ikkunassa luodaan uusi nimike, antamalla sille nimi ja kategoria
    //Tämä metodi ei luo uutta nimikettä itse vaan...
    //Kutsuu createEntertainmentPiece metodia nimikkeen luomiseksi
    private void addNewTitle(){

        JTextField titleName = new JTextField("Nimi...");

        JLabel nameLabel = new JLabel("Syötä nimi:");
        JLabel categoryLabel = new JLabel("Valitse kategoria:");
        JPanel titlePanel = new JPanel();

        String[] categories = {"Lautapelit", "Kirjat", "Elokuvat", "Roolipelit", "Videopelit"};
        JComboBox<String> categoryChoice = new JComboBox<>(categories);
        Object[] optionsButtons = {"Luo nimike", "Peruuta"};

        GridLayout grid = new GridLayout(3,2);
        titlePanel.setLayout(grid);

        titlePanel.add(nameLabel);
        titlePanel.add(titleName);
        titlePanel.add(categoryLabel);
        titlePanel.add(categoryChoice);

        int optionResult = JOptionPane.showOptionDialog(mainFrame, titlePanel, "Uusi nimike", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, optionsButtons, null);
        if(optionResult == JOptionPane.YES_OPTION){
            String name = titleName.getText();
            String categ = (String) categoryChoice.getSelectedItem();
            if(name != null && !name.isEmpty()){
                createEntertainmentPiece(name, categ);
            }else{
                JOptionPane.showMessageDialog(mainFrame, "Et syöttänyt nimikkeelle nimeä!\nHaluamaasi nimikettä ei nyt luotu.",
                        "Varoitus!", JOptionPane.WARNING_MESSAGE);
            }
        }
    }

    //Luo uuden viihdenimikkeen, jolle kysytään käyttäjältä nimi ja kategoria
    //Luo myös paneelin viihdenimikkeelle, joka lisätään sitten päänäkymään
    public void createEntertainmentPiece(String name, String category) {

        //regex joka sallii vain alphanumeriset merkit tiedostonimeen
        boolean hasNonAlpha = name.matches("^.*[^a-zA-Z0-9äöÄÖ ].*$");

        //Tarkastaa sisältääkä tiedostonimi ei-aplhanumerisia merkkejä
        //Jos ei sisällä, tarkastetaan ettei saman nimistä nimikettä ole jo listalla
        //Jos on, perutaan nimikkeen luominen. Muuten luodaan se ja lisätään näkyviin päänäkymään
        if (!hasNonAlpha) {
            if (!entertainmentNameArray.contains(name)) {
                entertainmentNameArray.add(name);
                EntertainmentPiece ep = new EntertainmentPiece(name, category);
                JOptionPane.showMessageDialog(mainFrame, "Haluamasi nimike luotiin onnistuneesti.");
                saveEntertainmentPiece(ep);
                buildEPBox(ep);
                epArray.add(ep);
            } else {
                JOptionPane.showMessageDialog(mainFrame, "Samalla nimellä tehty nimike löytyy jo.\nNimikettä ei luotu.", "Varoitus!", JOptionPane.WARNING_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(mainFrame, "Nimikkeen nimessä merkkejä, jotka eivät kelpaa tiedostonimeen.\nKokeile uutta nimeä.", "Varoitus!", JOptionPane.WARNING_MESSAGE);
        }
    }

    //Rakentaa nimikkeen avaa/muokkaa/tiedot boksin päänäkymään
    public void buildEPBox(EntertainmentPiece ep){
        JPanel epPanel = new JPanel();
        JLabel epName = new JLabel();
        JLabel epCateg = new JLabel();
        JButton openButton = new JButton("Avaa");

        //Avaa -napin action listener. Avaa kysyisen nimikkeen tiedoilla arvostelulistanäkymän
        openButton.addActionListener(event -> {
            SecondaryScreen sec = new SecondaryScreen(mainFrame, ep.getEntertainmentName(),
                    ep.getCategory());
            mainFrame.setVisible(false);
        });

        //Muokkaa nappi, avaa ikkunan, jossa voi muokata nimikkeen tietoja (nimi & kategoria)
        JButton editButton = new JButton("Muokkaa");
        editButton.addActionListener(event -> editButtonPushed(ep, epName, epCateg));

        //BoxLayout toistaiseksi, muutetaan jos deadlineen jää aikaa
        epPanel.setLayout(new BoxLayout(epPanel, BoxLayout.LINE_AXIS));
        //epPanel.setLayout(new GridBagLayout());
        //GridBagConstraints gbc = new GridBagConstraints();

        String fullCategory = ("Kategoria: " + ep.getCategory());
        epName.setText(ep.getEntertainmentName());
        epCateg.setText(fullCategory);

        epPanel.add(Box.createRigidArea(new Dimension(20, 0)));
        epPanel.add(epName);
        epPanel.add(Box.createRigidArea(new Dimension(20, 0)));
        epPanel.add(epCateg);
        epPanel.add(Box.createHorizontalGlue());
        epPanel.add(openButton);
        epPanel.add(Box.createRigidArea(new Dimension(20, 0)));
        epPanel.add(editButton);
        epPanel.add(Box.createRigidArea(new Dimension(20, 0)));

        //revalidate & repaint, jotta paneeli päivittyy runtime
        titlesPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        titlesPanel.add(epPanel);
        titlesPanel.revalidate();
        titlesPanel.repaint();
    }

    //Tallentaa nimikkeet omiin .ser tiedostoihinsa, jotta ne voidaan myöhemmin ladata uudelleen
    //Kirjoittaa myös epArray.txt tiedoston, johon tulee jokaisen nimikkeen nimi
    //epArray.txt avulla tiedetään myöhemmin minkä nimisiä tiedostoja pitäisi avata
    public void saveEntertainmentPiece (EntertainmentPiece ep) {
        //Yritetään kirjoittaa entertainmentPiece olion tiedot tiedostoon...
        try {
            String entertainmentFile = File.separator + ep.getEntertainmentName() + ".ser";
            String directory = System.getProperty("user.dir") + entertainmentFile;
            System.out.println(directory);
            FileOutputStream file = new FileOutputStream(directory);
            ObjectOutputStream out = new ObjectOutputStream(file);
            out.writeObject(ep);
            out.close();
            writeEPArrayTxt();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
        //Kirjoitetaan (ja luodaan) epArray.txt, johon lisätään edellä luotu EP
    private void writeEPArrayTxt(){
        try {
            FileWriter fw = new FileWriter("epArray.txt");
            BufferedWriter br = new BufferedWriter(fw);
            PrintWriter out = new PrintWriter(br);
            for (String str : entertainmentNameArray) {
                out.write(str + System.lineSeparator());
            }
            out.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    // Lukee nimikelistan ja listan avulla avaa nimikkeiden tiedostot
    // Luo nimikelistan päänäkymään createEPBox metodin avulla käyttämällä luettuja EP tiedostoja
    public void loadEntertainmentPieces() {

        //Luodaan epArray.txt jos sitä ei ole jo olemassa
        //Estetään virheilmoitus, joka syntyisi jos tiedostoa ei ole

        File file = new File("epArray.txt");
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Luetaan epArray.txt tiedostosta minkä nimisiä nimikkeitä ohjelmassa pitäisi näkyä
        EntertainmentPiece temp = new EntertainmentPiece("","");
        try {
            BufferedReader br = new BufferedReader(new FileReader("epArray.txt"));
            String line;
            while ((line = br.readLine()) != null) {
                entertainmentNameArray.add(line);
            }
            br.close();

            //Luetaan jokaisen nimikkeen mukainen .ser tiedosto, jossa on nimikkeeseen liittyvä olio
            //Luodaan jokaisesta nimikkeestä nimilistaan olio
            for (String piece : entertainmentNameArray) {
                FileInputStream fis = new FileInputStream(System.getProperty("user.dir") + File.separator + piece + ".ser");
                ObjectInputStream objin = new ObjectInputStream(fis);

                File EPfile = new File(System.getProperty("user.dir"));
                String fullFileName = File.separator + piece + ".ser";
                File fileToCheck = new File(EPfile, fullFileName);

                if(!fileToCheck.isFile()) {
                    JOptionPane.showMessageDialog(mainFrame, (piece + " nimistä .ser tiedostoa ei löytynyt... (loadEntertainmentPieces"),
                            "Varoitus", JOptionPane.WARNING_MESSAGE);
                } else {
                    try {
                        temp = (EntertainmentPiece) objin.readObject();
                        objin.close();
                        epArray.add(temp);
                    } catch (ClassNotFoundException cnfe) {
                        cnfe.printStackTrace();
                    }
                    buildEPBox(temp);
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Metodi, joka käsittelee edit buttonin painamisen päänäkymässä
    //Ottaa syötteenä ep:n, jonka edit buttonia painetaan.
    //Avaa ikkunan, jossa eptä voi muokata. Antaa myös mahdollisuuden poistaa ep.
    //Metodi antaa myös muuttaa epn nimen ja kategorian
    private void editButtonPushed(EntertainmentPiece ep, JLabel oldNameLabel, JLabel oldCategLabel ){

        AtomicReference<EntertainmentPiece> temp = new AtomicReference<>(new EntertainmentPiece("", ""));
        JTextField epName = new JTextField(ep.getEntertainmentName());
        JLabel nimiLabel = new JLabel("Syötä nimi:", SwingConstants.CENTER);
        JLabel categLabel = new JLabel("Valitse kategoria:", SwingConstants.CENTER);
        JPanel editPanel = new JPanel();
        String[] categories = {"Lautapelit", "Kirjat", "Elokuvat", "Roolipelit", "Videopelit"};
        JButton deleteButton = new JButton("Poista tämä nimike");
        String oldEPName = ep.getEntertainmentName();
        String oldEPCat = ep.getCategory();

        JComboBox<String> categBox = new JComboBox<>(categories);
        Object[] optionsButtons = {"Tallenna muutokset", "Peruuta"};

        String modString = ("Olet muokkaamassa nimikettä " + oldEPName + " (" + oldEPCat + ")");
        JLabel oldData = new JLabel(modString);

        editPanel.setLayout(new GridLayout(3,2));
        editPanel.add(nimiLabel);
        editPanel.add(epName);
        editPanel.add(categLabel);
        editPanel.add(categBox);
        editPanel.add(oldData);
        editPanel.add(deleteButton);

        //Delete napin toiminnallisuudelle lambda
        deleteButton.addActionListener(event -> {

            //Varmistetaan käyttäjältä että valittu nimike halutaan poistaa
            String[] warningOptions = {"Kyllä", "Ei"};
            int optionResult = JOptionPane.showOptionDialog(mainFrame, ("Haluatko varmasti poistaa nimikkeen " + oldEPName + "?"),
                    "Varoitus!", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE, null, warningOptions, null);

            //Jos hyväksytään, poistetaan
            if(optionResult == JOptionPane.YES_OPTION) {
                File file = new File(System.getProperty("user.dir"));
                String fullFileName = File.separator + oldNameLabel.getText() + ".ser";
                File fileToDelete = new File(file, fullFileName);

                if (!fileToDelete.delete()) {
                    JOptionPane.showMessageDialog(mainFrame, "Poistaminen ei onnistunut...",
                            "Varoitus", JOptionPane.ERROR_MESSAGE);
                } else {

                    //Jos tiedoston poistaminen onnistuu
                    //Poistetaan nimi myös nameArraylta ja epArraylta
                    entertainmentNameArray.remove(oldEPName);
                    epArray.remove(ep);
                    writeEPArrayTxt();
                    titlesPanel.removeAll();

                    //Luetaan nameArraylta nimikeiden nimet ja tehdään niitä vastaavilla tiedostoilla epBoxit päänäkymään uudelleen
                    //Estää turhien tyhjien välien syntymistä päänäkymän listassa
                    //Lista tyhjennetään ensin kokonaan, sitten tehdään uudelleen
                    for (String piece : entertainmentNameArray) {
                        try {
                            //Tarkistetaan että haluttu file on olemassa
                            String fil = File.separator + piece + ".ser";
                            File fileToCheck = new File(file, fil);

                            if(!fileToCheck.isFile()) {
                                JOptionPane.showMessageDialog(mainFrame, (piece + " nimistä .ser tiedostoa ei löytynyt... (editButtonPushed)"),
                                        "Varoitus", JOptionPane.WARNING_MESSAGE);
                            }else {

                                FileInputStream fis = new FileInputStream(System.getProperty("user.dir") + File.separator + piece + ".ser");
                                ObjectInputStream objin = new ObjectInputStream(fis);
                                try {
                                    temp.set((EntertainmentPiece) objin.readObject());
                                    objin.close();
                                } catch (ClassNotFoundException cnfe) {
                                    cnfe.printStackTrace();
                                }

                                buildEPBox(temp.get());
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                    titlesPanel.revalidate();
                    titlesPanel.repaint();
                    Window w = SwingUtilities.getWindowAncestor(deleteButton);
                    if (w != null) {
                        w.dispose();
                    }
                }
            }
        });

        int optionResult = JOptionPane.showOptionDialog(mainFrame, editPanel, "Muokkaa nimikettä", JOptionPane.YES_NO_OPTION,
                JOptionPane.PLAIN_MESSAGE, null, optionsButtons, null);

        //Jos käyttäjä painaa YES_OPTIOTA eli Tallenna muutokset nappia
        if (optionResult == JOptionPane.YES_OPTION){
            String newName = epName.getText();
            String newCateg = (String) categBox.getSelectedItem();
            if(newName != null && !newName.isEmpty()){

                //Tarkastetaan, että nimi käy tiedostonimeksi
                boolean hasNonAlpha = newName.matches("^.*[^a-zA-Z0-9äöÄÖ ].*$");
                if(!hasNonAlpha) {
                    //Poistetaan vanhan nimikkeen tiedot listalta ja lisätään uusi nimi
                    entertainmentNameArray.remove(oldEPName);
                    epArray.remove(ep);
                    entertainmentNameArray.add(newName);

                    //Muutetaan nimikeolion tiedot uusiksi
                    ep.setEntertainmentName(newName);
                    ep.setCategory(newCateg);
                    epArray.add(ep);

                    //Yritetään lukea "nimike".ser tiedostosta
                    //.serin tulee olla samassa hakemistossa kuin mistä ohjelma ajetaan
                    try{
                        File file = new File(System.getProperty("user.dir"));
                        String fullFileName = File.separator + oldEPName + ".ser";
                        File fileToDelete = new File(file, fullFileName);

                        if(!fileToDelete.delete()){
                            JOptionPane.showMessageDialog(mainFrame, "Vanhan nimikkeen tiedoston poistaminen ei onnistunut...",
                                    "Varoitus", JOptionPane.ERROR_MESSAGE);
                        }
                        saveEntertainmentPiece(ep);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    oldNameLabel.setText(newName);
                    oldCategLabel.setText("Kategoria: " + newCateg);

                    titlesPanel.revalidate();
                    titlesPanel.repaint();
                } else {
                    JOptionPane.showMessageDialog(mainFrame, "Nimikkeen nimessä merkkejä, jotka eivät kelpaa tiedostonimeen." +
                            "\nKokeile uutta nimeä.", "Varoitus!", JOptionPane.WARNING_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(mainFrame, "Et syöttänyt nimikkeelle nimeä!" +
                        "\nHaluamaasi muokkausta ei nyt tehty.", "Varoitus!", JOptionPane.WARNING_MESSAGE);
            }
        }
    }

    //Järjestelee päänäkymässä näkyvät nimikkeet erilaisiin järjestyksiin pudotusvalikon mukaisesti
    private void sortEPBoxes(Object selected){
        String selectedItem = selected.toString();
        EntertainmentPiece temp = new EntertainmentPiece("", "");


        switch(selectedItem){

            //Lajittelee ja tulostaa nimikkeet aakkosjärjestyksessä päänäkymään
            case "Aakkosjärjestys":

                entertainmentNameArray.sort(String.CASE_INSENSITIVE_ORDER);
                titlesPanel.removeAll();
                for (String piece : entertainmentNameArray) {
                    try {
                        FileInputStream fis = new FileInputStream(System.getProperty("user.dir") + File.separator + piece + ".ser");
                        ObjectInputStream objin = new ObjectInputStream(fis);
                        try {
                            temp = ((EntertainmentPiece) objin.readObject());
                            objin.close();
                        } catch (ClassNotFoundException cnfe) {
                            cnfe.printStackTrace();
                        }
                        buildEPBox(temp);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }

                titlesPanel.revalidate();
                titlesPanel.repaint();
                break;

                //Järjestää nimikkeet kategorian perusteella ja tulostaa ne uudessa järjestyksessä päänäkymään
            case "Kategorioittain":

                epArray.sort(Comparator.comparing(EntertainmentPiece::getCategory));
                titlesPanel.removeAll();
                for(EntertainmentPiece ep : epArray) {
                    buildEPBox(ep);
                }
                titlesPanel.revalidate();
                titlesPanel.repaint();
                break;
        }
    }
}