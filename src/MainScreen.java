import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;

// Luokka päänäkymälle
public class MainScreen extends JFrame {

    private ArrayList <String> entertainmentNameArray = new ArrayList<>();
    private JFrame mainFrame;
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

        String[] sortingOptions = {"Aakkosjärjestys", "Uusimmat", "Kategorioittain", "Vanhimmat"};
        JComboBox<String> sortingDrop = new JComboBox<>(sortingOptions);
        sortingDrop.setSize(20,10);

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

        JOptionPane.showOptionDialog(null, optionsPanel, "Options", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, optionsButtons, optionsButtons[1]);
    }

    private void addNewTitle(){

        JTextField titleName = new JTextField();

        JLabel nameLabel = new JLabel("Syötä nimi:");
        JLabel categoryLabel = new JLabel("Valitse nimikkeen kategoria:");
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

        int optionResult = JOptionPane.showOptionDialog(null, titlePanel, "Uusi nimike", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, optionsButtons, null);
        //JOptionPane.showInputDialog(null, "Syötä nimikkeeseen liittyvät tiedot", "Uusi nimike", JOptionPane.PLAIN_MESSAGE, null, categories, "Kirjat");
        if(optionResult == JOptionPane.YES_OPTION){
            String name = titleName.getText();
            String categ = (String) categoryChoice.getSelectedItem();
            if(name != null && !name.isEmpty()){
                createEntertainmentPiece(name, categ);
            }else{
                JOptionPane.showMessageDialog(null, "Et syöttänyt nimikkeelle nimeä!\nHaluamaasi nimikettä ei nyt luotu.", "Varoitus!", JOptionPane.WARNING_MESSAGE);
            }
        }
    }

    //Luo uuden viihdenimikkeen, jolle kysytään käyttäjältä nimi ja kategoria
    //Luo myös paneelin viihdenimikkeelle, joka lisätään sitten päänäkymään
    public void createEntertainmentPiece(String name, String category) {

        boolean hasNonAlpha = name.matches("^.*[^a-zA-Z0-9 ].*$");

        if (!hasNonAlpha) {
            if (!entertainmentNameArray.contains(name)) {
                entertainmentNameArray.add(name);
                EntertainmentPiece ep = new EntertainmentPiece(name, category);
                JOptionPane.showMessageDialog(null, "Haluamasi nimike luotiin onnistuneesti.");
                saveEntertainmentPiece(ep);
                buildEPBox(ep);
            } else {
                JOptionPane.showMessageDialog(null, "Samalla nimellä tehty nimike löytyy jo.\nNimikettä ei luotu.", "Varoitus!", JOptionPane.WARNING_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(null, "Nimikkeen nimessä merkkejä, jotka eivät kelpaa tiedostonimeen.\nKokeile uutta nimeä.", "Varoitus!", JOptionPane.WARNING_MESSAGE);
        }
    }

    //Rakentaa nimikkeen avaa/muokkaa/tiedot boksin päänäkymään
    public void buildEPBox(EntertainmentPiece ep){
        JPanel epPanel = new JPanel();
        JLabel epName = new JLabel();
        JLabel epCateg = new JLabel();
        JButton openButton = new JButton("Avaa");

        openButton.addActionListener(event -> {
            SecondaryScreen sec = new SecondaryScreen(ep.getEntertainmentName(),
                    ep.getCategory());
        });

        JButton editButton = new JButton("Muokkaa");
        //editButton.addActionListener(event -> editButtonPushed());

        //BoxLayout toistaiseksi
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

        titlesPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        titlesPanel.add(epPanel);
        titlesPanel.revalidate();
        titlesPanel.repaint();
    }

    public void saveEntertainmentPiece (EntertainmentPiece ep) {
        try {
            String entertainmentFile = "\\" + ep.getEntertainmentName() + ".ser";
            String directory = System.getProperty("user.dir") + entertainmentFile;
            System.out.println(directory);
            FileOutputStream file = new FileOutputStream(directory);
            ObjectOutputStream out = new ObjectOutputStream(file);
            out.writeObject(ep);

        } catch (IOException e){
            e.printStackTrace();
        } //catch (FileNotFoundException e){}

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

    public void loadEntertainmentPieces() {
        EntertainmentPiece temp = new EntertainmentPiece("","");
        try {
            BufferedReader br = new BufferedReader(new FileReader("epArray.txt"));
            String line;
            while ((line = br.readLine()) != null) {
                entertainmentNameArray.add(line);
            }
            br.close();

            for (String piece : entertainmentNameArray) {
                FileInputStream fis = new FileInputStream(System.getProperty("user.dir") + "\\" + piece + ".ser");
                ObjectInputStream objin = new ObjectInputStream(fis);
                try {
                    temp = (EntertainmentPiece) objin.readObject();
                } catch (ClassNotFoundException cnfe) {
                    cnfe.printStackTrace();
                }
                buildEPBox(temp);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

