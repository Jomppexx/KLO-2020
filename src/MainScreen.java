import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

// Luokka päänäkymälle
public class MainScreen extends JFrame {

    private JFrame frame;
    private JPanel toolsPanel; //paneeli hakutoiminnoille ja työkalupalkille
    private JPanel titlesPanel; //paneeli nimikelistalle, josta pääsee arvosteluihin
    private JSplitPane mainSplitPane; //pääpaneeli, johon yllä olevat paneelit laitetaan

    //Initialisoidaan päänäkymä, luo elementit joista näkymä koostuu ja näyttää ne
    public void init(){

        // Rakentaa framen ja sen sisällä olevat paneelit
        buildWindow();

        // Rakentaa näkymän yläosan työkalupalkin ja sen valikot
        buildMenuBar();

        JLabel testText = new JLabel("Ei viddu mage :D"); //testaustarkoituksiin

        titlesPanel.add(testText);

        toolsPanel.setBackground(Color.CYAN);
        titlesPanel.setBackground(Color.GREEN);

        // Lopuksi freimi näkyviin ja ikkuna aukeaa
        frame.setVisible(true);
        mainSplitPane.setDividerLocation(0.07);
    }

    // Rakentaa framen ja sen sisällä olevat paneelit
    private void buildWindow(){
        frame = new JFrame("Arvosteluohjelma");
        frame.setSize(1200,800);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        toolsPanel = new JPanel();
        toolsPanel.setLayout(new BoxLayout(toolsPanel, BoxLayout.LINE_AXIS));
        titlesPanel = new JPanel();
        titlesPanel.setLayout(new BoxLayout(titlesPanel, BoxLayout.PAGE_AXIS));

        /*
        * Luodaan scrollPane ja liitetään se titlesPaneeliin, jotta title paneelia voi kelata
        * kun siihen tulee "liikaa" sisältöä, eikä se kaikki näy kerralla
        */
        // scrollPane, mahdollistaa ikkunan scrollaamisen...
        JScrollPane titlesScrollPane = new JScrollPane(titlesPanel);
        frame.add(titlesScrollPane);
        titlesScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        // Nappi uusien nimikkeiden (elokuvat, kirjat, ym.) lisäämistä varten
        JButton newTitleButton = new JButton("Lisää nimike");
        toolsPanel.add(Box.createRigidArea(new Dimension(15,0)));
        toolsPanel.add(newTitleButton);
        newTitleButton.addActionListener((event) -> addNewTitle());

        // "Pääpaneeli" on splitpane, jossa yläosaan tulee hakutyökalujen paneeli ja alaosaan eri nimikkeet
        mainSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, toolsPanel, titlesScrollPane);
        mainSplitPane.setOneTouchExpandable(false);
        mainSplitPane.setResizeWeight(0.5);
        mainSplitPane.setDividerSize(0);

        frame.add(mainSplitPane);
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
        frame.setJMenuBar(menuBar);
    }

    // Avaa help ikkunan, kun help nappia painetaan menussa
    public void openHelpWindow(){

        JTextArea helpText = new JTextArea(20,20);
        helpText.setEditable(false);
        helpText.setLineWrap(true);

        JButton helpBackButton = new JButton("Palaa");

        final  String helpContent = "Tähän tulee ohjeet ohjelman käyttöön ja selitys siitä, mikä tämä ohjelma on.";
        helpText.append(helpContent);

        JScrollPane helpScroll = new JScrollPane(helpText);
        helpScroll.setPreferredSize(new Dimension(550,350));
        helpScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        JFrame helpFrame = new JFrame("Apua ja infoa");
        helpFrame.setSize(600, 440);

        JPanel helpPanel = new JPanel();
        helpFrame.add(helpPanel);
        helpPanel.add(helpScroll);
        helpPanel.add(helpBackButton);
        helpFrame.setVisible(true);
        helpBackButton.addActionListener((event) -> helpFrame.dispose());
    }

    // Avaa options ikkunan kun options nappia painetaan menussa
    public void openOptionsWindow(){

        /*
        Helpoin layout: käytä kahta framea, laita molemmat ylhäältä alas box layout
        Box layout ei näemmä pakota asioita täyttämään niille varattua aluetta (kts toolsPanel nappi...)
         */
        JLabel langLabel = new JLabel("Kieli:");
        JLabel ratingsLabel = new JLabel("Piilota arvosanat?");

        String[] languages = {"English", "Suomi"};
        JComboBox langChoice = new JComboBox(languages);
        langChoice.setSelectedIndex(1);

        JCheckBox disableRatings = new JCheckBox();

        JButton saveButton = new JButton("Tallenna");
        saveButton.setPreferredSize(new Dimension(30,20));
        JButton backButton = new JButton("Palaa");

        //GridBagLayout gbl = new GridBagLayout();
        GridLayout grid = new GridLayout(3,2); //GridLayout, jossa 3 riviä, 2 pystyriviä
        JPanel optionsPanel = new JPanel();
        optionsPanel.setLayout(grid);

        /*GridBagConstraints gbc = new GridBagConstraints();

        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.FIRST_LINE_START;*/

        optionsPanel.add(langLabel);
        optionsPanel.add(langChoice);
        optionsPanel.add(ratingsLabel);
        optionsPanel.add(disableRatings);
        optionsPanel.add(saveButton);
        optionsPanel.add(backButton);

        JFrame optionsFrame = new JFrame("Asetukset");
        optionsFrame.setSize(600,440);
        optionsFrame.add(optionsPanel);
        optionsFrame.setVisible(true);

        //saveButton.addActionListener((event) -> saveOptions());
        backButton.addActionListener((event) -> optionsFrame.dispose());
    }

    public void addNewTitle(){


    }
}
