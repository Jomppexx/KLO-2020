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
        buildFrame();

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
    private void buildFrame(){
        frame = new JFrame("Arvosteluohjelma");
        frame.setSize(1200,800);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        toolsPanel = new JPanel();
        toolsPanel.setLayout(new BoxLayout(toolsPanel, BoxLayout.LINE_AXIS));
        titlesPanel = new JPanel();
        titlesPanel.setLayout(new BoxLayout(titlesPanel, BoxLayout.PAGE_AXIS));

        // Nappi uusien nimikkeiden (elokuvat, kirjat, ym.) lisäämistä varten
        JButton newTitleButton = new JButton("Lisää nimike");
        toolsPanel.add(Box.createRigidArea(new Dimension(15,0)));
        toolsPanel.add(newTitleButton);

        // "Pääpaneeli" on splitpane, jossa yläosaan tulee hakutyökalujen paneeli ja alaosaan eri nimikkeet
        mainSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, toolsPanel, titlesPanel);
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

        JFrame helpFrame = new JFrame("Apua ja infoa");
        helpFrame.setSize(600, 400);

        JPanel helpPanel = new JPanel();

        helpFrame.setVisible(true);
    }

    // Avaa options ikkunan kun options nappia painetaan menussa
    public void openOptionsWindow(){

    }
}
