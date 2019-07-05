import java.util.*;
import java.lang.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.Border;

public class KalahaControl implements MouseListener, ActionListener, KeyListener{
  public boolean turn = true;//kollar vems tur det är
  public JLabel[] kal;
  public int[] val;
  public int startValue=6;
  public final static int NLABELS=14, NLABHALF=NLABELS/2, VALMAX=6*NLABELS;
  public JButton newGame;
  public JRadioButton[] nPlayers, startPlayer;
  public JTextField player1, player2;
  public String name1, name2;
  public JTextArea outputText, resultText, rulesText;
  public JComboBox stValBox;
  public String res="";
  private String rules="Vardera spelare har varsin halva av spelplanen. Under din tur klickar du på en av dina sex skålar (rutor), den större skålen i moturs riktning är din målskål. Samtliga kulor (här endast representerad av ett nummer) förflyttas moturs en till varje skål tills kulorna tar slut. Motståndarens målskål hoppas över, istället går kulorna direkt över till din spelplan igen. Om du med sista kulan träffar din egen målskål får du ett nytt drag. Om du med sista kulan träffar en tom skål på din egen planhalva vinner du samtliga kulor från motståndarens motsatta skål och den kulan du lägger i din tomma skål, under förutsättning att motståndarens motsatta skål inte är tom. Spelet tar slut när en av spelarna får slut på kulor på sin egen spelplan. Samtliga kvarvarande kulor på motspelarens planhalva läggs i dennes målskål. Den spelare med flest kulor i sin målskål vid slutet vinner.";
  private static final Color BROWN = new Color(102,51,0);
  private boolean printed = false;//för att se om resultatet har skrivits ut

  public KalahaControl(){

  }

  public void initLabelValue(int i, int stValue){//nollställer lablar
    val[i] = stValue;
    kal[i].setText(Integer.toString(val[i]));
    if (i<NLABHALF){
      kal[i].setBackground(Color.lightGray);
    }
    else{
      kal[i].setBackground(Color.white);
    }
  }

  public void printResult(){//skriver ut resultat
    if (!printed){
        if (nPlayers[0].isSelected() && startPlayer[1].isSelected())
          res = player2.getText() + "-" + player1.getText() + " " + val[NLABHALF] + "-" + val[0] + "\r\n" + res;
        else{
          res = player1.getText() + "-" + player2.getText() + " " + val[NLABHALF] + "-" + val[0] + "\r\n" + res;
        }
      }
      outputText.setText(res);
      printed = true;
  }

  public void mouseClicked(MouseEvent e){
    setPlayEditable();
    if (e.getSource().equals(resultText)){//testar först om resultat har blivit klickad på
      outputText.setText(res);
      resultText.setForeground(BROWN);
      rulesText.setForeground(Color.BLACK);
      }
    else if (e.getSource().equals(rulesText)){
      rulesText.setForeground(BROWN);
      resultText.setForeground(Color.BLACK);
      outputText.setText(rules);
    }
    else{//testar vilken skål (om någon) som blivit klickad på
      KalahaMove move = move=new KalahaMove(this);
      for (int i=1;i<NLABHALF;i++){
        if (e.getSource().equals(kal[i]) && !turn && val[i]!=0){
          int k=move.changeValues(i);
          if (!(k==0)){
            changeColor(Color.lightGray,Color.white);
          }
        }
      }
      for (int i=NLABHALF+1;i<NLABELS;i++){
        if (e.getSource().equals(kal[i]) && turn && val[i]!=0){
          int k=move.changeValues(i);
          if (!(k==NLABHALF) && nPlayers[1].isSelected()){
            changeColor(Color.white,Color.lightGray);
          }
          else if (!(k==NLABHALF) && nPlayers[0].isSelected()){
            KalahaComputer computer = new KalahaComputer(this);
            computer.computersMove();
          }
        }
      }
    }
  }

  public void mouseExited(MouseEvent e0) {

  }

  public void mouseEntered(MouseEvent e) {

  }

  public void mousePressed(MouseEvent e) {

  }

  public void mouseReleased(MouseEvent e) {

  }

  private void changeColor(Color c1, Color c2){//hjälpmetod vid musklick
    turn = !turn;
    for (int l=0;l<NLABHALF;l++){
      kal[l].setBackground(c1);
    }
    for (int l=NLABHALF;l<NLABELS;l++){
      kal[l].setBackground(c2);
    }
  }

  public void actionPerformed(ActionEvent e) {//antingen nytt spel eller ändring av startvärden
    if (e.getSource().equals(newGame)){
      initLabelValue(0, 0);
      for (int i=1;i<NLABHALF;i++){
        initLabelValue(i, startValue);
      }
      initLabelValue(NLABHALF, 0);
      for (int i=NLABELS-1;i>NLABHALF;i--){
        initLabelValue(i, startValue);
      }
      if (nPlayers[0].isSelected() && startPlayer[1].isSelected()){
        KalahaComputer computer = new KalahaComputer(this);
        computer.computersMove();
      }
      printed = false;
    }
    else if (e.getSource().equals(stValBox)){
      startValue = Integer.parseInt((String)stValBox.getSelectedItem());
    }
    setPlayEditable();
  }

  public void keyPressed(KeyEvent e) {

  }

  public void keyReleased(KeyEvent e) {//om man ändrar namnet
    if (e.getSource().equals(player1)){
          name1=player1.getText();
    }
    else if (e.getSource().equals(player2)){
          name2=player2.getText();
    }
  }

  public void keyTyped(KeyEvent e) {
  }

  private void setPlayEditable(){//ser till att rätt textfält och radioknappar är tillgängliga
    if (nPlayers[1].isSelected()){
      player1.setEditable(true);
      player2.setEditable(true);
      startPlayer[0].setVisible(false);
      startPlayer[1].setVisible(false);
      if (!(name1==null)){
        player1.setText(name2);
      }
      else{
        player1.setText("Spelare 1");
      }
      if (!(name2==null)){
        player2.setText(name2);
      }
      else{
        player2.setText("Spelare 2");
      }
    }
    else{
      startPlayer[0].setVisible(true);
      startPlayer[1].setVisible(true);
      if (startPlayer[0].isSelected()){
        setPlayEditable2(player1, player2, name1, 1);
      }
      else{
        setPlayEditable2(player2, player1, name2, 2);
      }
    }
  }

  private void setPlayEditable2(JTextField t1, JTextField t2, String name, int i){//hjälpmetod för setPlayEditable
    t1.setEditable(true);
    if (!(name==null)){
      t1.setText(name);
    }
    else{
      t1.setText("Spelare " + i);
    }
    t2.setEditable(false);
    t2.setText("Datorn");
  }
}
