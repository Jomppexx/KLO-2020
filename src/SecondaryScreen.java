import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;

public class SecondaryScreen {

    //private JFrame secondary;
    ArrayList<ReviewPiece> arvostelut = new ArrayList<>();

    private JDialog secscreen;
    private JFrame secondary; // = new JFrame();
    private JPanel toolsPanel; //paneeli hakutoiminnoille ja työkalupalkille
    private JPanel titlesPanel; //paneeli nimikelistalle, josta pääsee arvosteluihin
    private JSplitPane mainSplitPane; //pääpaneeli, johon yllä olevat paneelit laitetaan

    public SecondaryScreen(){
        MakeWindow();

    }
    private void MakeWindow() {
        JFrame secondary = new JFrame();
        JDialog secscreen = new JDialog(secondary, "Arvostelut");
        secscreen.setSize(1200, 800);
        secscreen.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

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
        secscreen.add(titlesScrollPane);
        titlesScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        // "Pääpaneeli" on splitpane, jossa yläosaan tulee hakutyökalujen paneeli ja alaosaan eri nimikkeet
        mainSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, toolsPanel, titlesScrollPane);
        mainSplitPane.setOneTouchExpandable(false);
        mainSplitPane.setResizeWeight(0.5);
        mainSplitPane.setDividerSize(0);

        ////toolsPanel.add(Box.createHorizontalGlue());

        //Title ja kategoria
        JLabel title = new JLabel ("Lord of the Rings");
        toolsPanel.add(title);
        toolsPanel.add(Box.createRigidArea(new Dimension(5,5)));
        JLabel kategoria = new JLabel ("Elokuva");
        toolsPanel.add(kategoria);
        toolsPanel.add(Box.createRigidArea(new Dimension(5,5)));


        //Tehdään hakemistoiminto
        JLabel hakuehdot = new JLabel("Rajoita hakua:");
        String hakutermit[]={"Uusin", "Tekijä", "Arvosana"};
        JComboBox sortby = new JComboBox(hakutermit);
        sortby.setSize(20,10);
        toolsPanel.add(hakuehdot);
        toolsPanel.add(sortby);

        JButton newReview = new JButton("+ Uusi arvostelu");
        toolsPanel.add(newReview);


        secscreen.add(mainSplitPane);
        secondary.setVisible(true);
        secscreen.setVisible(true);
        AddReview();
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

        JLabel title = new JLabel("Lord of the Rings");
        panel.add(title);
        JLabel category = new JLabel("Elokuva");
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
            ReviewPiece uusi = new ReviewPiece(headerfield.getText(),
                    grade.getSelectedIndex()+1,
                    reviewtext.getText());

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
            AddReview();
        } //catch (FileNotFoundException e){}
    }
}
