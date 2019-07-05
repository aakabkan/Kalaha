import java.util.*;
import java.lang.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.Border;

public class KalahaMain extends JFrame{
  private KalahaControl control;
  private Border border;
  private JPanel topPanel, headingPanel;
  private JPanel[] boardPanels;
  private ButtonGroup nPlayersGroup, startPlayerGroup;
  private static final Color BROWN = new Color(102,51,0);

  public KalahaMain(){
    control = new KalahaControl();

    JPanel bigPanel = new JPanel();//hela alltet
    topPanel = new JPanel();//ovanför spelplanen
    JPanel centralPanel = new JPanel();//spelplanen
    JLabel combLab = new JLabel("Antal startkulor: ");
    String[] stValAlt = {"3","4","5","6"};
    control.stValBox = new JComboBox(stValAlt);
    control.stValBox.setSelectedIndex(3);
    topPanel.add(combLab);
    control.stValBox.addActionListener(control);
    topPanel.add(control.stValBox);
    control.nPlayers = new JRadioButton[2];
    control.nPlayers[0] = new JRadioButton("1 spelare", true);
    control.nPlayers[1] = new JRadioButton("2 spelare");
    nPlayersGroup = new ButtonGroup();
    initRadBut(control.nPlayers[0],nPlayersGroup,topPanel);
    initRadBut(control.nPlayers[1],nPlayersGroup,topPanel);
    JLabel[] emptyLab = new JLabel[2];//skapar tomma lablar för att ge mellanrum
    for (int i=0;i<emptyLab.length;i++){
      emptyLab[i] = new JLabel();
      emptyLab[i].setPreferredSize(new Dimension(10,20));
    }
    topPanel.add(emptyLab[0]);
    control.newGame = new JButton("Nytt spel");
    control.newGame.addActionListener(control);
    topPanel.add(control.newGame);
    topPanel.add(emptyLab[1]);
    control.startPlayer = new JRadioButton[2];
    control.startPlayer[0] = new JRadioButton("",true);
    control.startPlayer[1] = new JRadioButton();
    startPlayerGroup = new ButtonGroup();
    control.player1=new JTextField("Spelare 1");
    control.player2=new JTextField("Datorn");
    initRadBut(control.startPlayer[0],startPlayerGroup,topPanel);
    initText(control.player1);
    initRadBut(control.startPlayer[1],startPlayerGroup,topPanel);
    initText(control.player2);
    control.player2.setEditable(false);//ty en spelare är från början markerad
    boardPanels = new JPanel[5];//div. paneler på spelplanen
    for (int i=0;i<boardPanels.length;i++){
      boardPanels[i] = new JPanel();
      boardPanels[i].setBackground(Color.gray);
      if (i==1){
        boardPanels[1].setLayout(new BoxLayout(boardPanels[1], BoxLayout.Y_AXIS));//paneler ska adderas
      }
      if (i>2){
        boardPanels[1].add(boardPanels[i]);
        boardPanels[i].setLayout(new FlowLayout());
      }
    }
    JPanel bottomPanel = new JPanel();//under spelplanen
    centralPanel.setBackground(Color.gray);
    control.kal = new JLabel[control.NLABELS];//skålarna
    control.val = new int[control.NLABELS];//skålarnas värden
    border = BorderFactory.createLineBorder(Color.BLACK, 2);
    //nedanstående rader skapar alla skålar för kulorna, i efterhand inser jag att det hade varit bättre att indexera dessa i omvänd ordning då spelet går moturs, men det känns jobbigt att ändra i efterhand.
    initLabel(0, 0, boardPanels[0], new Dimension(180,200), new Font("Serif", Font.BOLD, 150), border);
    for (int i=1;i<control.NLABHALF;i++){
      initLabel(i, control.startValue, boardPanels[3], new Dimension(90,100), new Font("Serif", Font.BOLD, 75), border);
    }
    initLabel(control.NLABHALF, 0, boardPanels[2], new Dimension(180,200), new Font("Serif", Font.BOLD, 150), border);
    for (int i=control.NLABELS-1;i>control.NLABHALF;i--){
      initLabel(i, control.startValue, boardPanels[4], new Dimension(90,100), new Font("Serif", Font.BOLD, 75), border);
    }
    headingPanel = new JPanel();
    control.resultText = new JTextArea("Resultat");
    control.rulesText = new JTextArea("Regler");
    initTextArea(control.resultText,BROWN);
    initTextArea(control.rulesText,Color.BLACK);
    headingPanel.setBackground(Color.lightGray);
    control.outputText = new JTextArea();
    control.outputText.setBackground(Color.lightGray);
    control.outputText.setPreferredSize(new Dimension(500,900));
    control.outputText.setFont(new Font("Serif", Font.PLAIN, 16));
    control.outputText.setLineWrap(true);
    control.outputText.setWrapStyleWord(true);
    control.outputText.setEditable(false);
    headingPanel.setLayout(new FlowLayout());
    bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.Y_AXIS));
    bottomPanel.add(headingPanel);
    bottomPanel.add(control.outputText);
    bottomPanel.setBackground(Color.lightGray);
    centralPanel.setLayout(new FlowLayout());
    for (int i=0;i<=2;i++){
      centralPanel.add(boardPanels[i]);
    }
    bigPanel.setLayout(new BoxLayout(bigPanel, BoxLayout.Y_AXIS));
    bigPanel.add(topPanel);
    bigPanel.add(centralPanel);
    bigPanel.setBackground(Color.lightGray);
    bigPanel.add(bottomPanel);
    add(bigPanel);
  }

  public void initLabel(int i, int stValue, JPanel panel, Dimension dim, Font f, Border border){//ger nya lablar deras rätta egenskaper
    control.kal[i] = new JLabel("", SwingConstants.CENTER);
    control.kal[i].setPreferredSize(dim);
    control.kal[i].setFont(f);
    control.kal[i].setBorder(border);
    control.kal[i].setOpaque(true);
    control.initLabelValue(i,stValue);
    if (!(i==0 || i==control.NLABHALF)){
      control.kal[i].addMouseListener(control);
    }
    panel.add(control.kal[i]);
  }

  public void initRadBut(JRadioButton rb, ButtonGroup bg, JPanel p){//ger radioknappar dess egenskaper
    rb.addActionListener(control);
    bg.add(rb);
    p.add(rb);
  }

  public void initText(JTextField t){//ger textfält dess egenskaper
    t.addKeyListener(control);
    t.setPreferredSize(new Dimension(110, 20));
    topPanel.add(t);
  }

  public void initTextArea(JTextArea t, Color c){//ger textareas dess egenskaper
    t.setPreferredSize(new Dimension(500,30));
    t.setBackground(Color.lightGray);
    t.setFont(new Font("Serif", Font.BOLD, 20));
    t.addMouseListener(control);
    t.setForeground(c);
    t.setEditable(false);
    headingPanel.add(t);
  }

  public static void main(String[] args) {
    JFrame frame = new KalahaMain();
    frame.pack();
    frame.setVisible(true);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
  }
}
