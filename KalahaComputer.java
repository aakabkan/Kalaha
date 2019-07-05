import java.util.*;
import java.lang.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class KalahaComputer{
  private KalahaControl con;

  public KalahaComputer(KalahaControl c){
    con = c;
  }

  public void computersMove(){//gör datorns drag
    con.turn = false;
    ArrayList<Integer> alt = new ArrayList<Integer>();//lista för möjliga skålar datorn väljer mellan
    ArrayList<Integer> alt2 = new ArrayList<Integer>();//hjälplista för möjliga skålar datorn väljer mellan
    alt = checkHitEmptyBowl(1, alt);//testar om man kan träffa en tom skål
    alt = checkHitEmptyBowl(con.NLABHALF+1, alt);//testar om motståndaren kan träffa en tom skål
    int highest=0;
    for (Integer itr : alt){//testar vilken av skålarna som ger mest poäng
      int i = itr.intValue();
      if (highest==0){
        highest=i;
      }
      else if (con.val[(con.VALMAX-(i-con.val[i]))%con.NLABELS]>con.val[(con.VALMAX-(highest-con.val[highest]))%con.NLABELS]){
        highest=i;
      }
      if (i<con.NLABHALF){
        for (Integer itr2 : alt){//testar om en skålen på motstående sida kommer träffa motsvarande skål
          int j = itr2.intValue();
          if (con.NLABELS-((con.VALMAX+j-con.val[j])%con.NLABELS)==i){
              alt2.add(new Integer(i));
          }
        }
      }
    }
    if (highest > 0){//om någon skål kan bli träffad
      for (Integer itr : alt2){
        int i = itr.intValue();
        if (con.val[i]+(con.val[(con.VALMAX-(i-con.val[i]))%con.NLABELS])>con.val[(con.VALMAX-(highest-con.val[highest]))%con.NLABELS]){
          highest=i;
        }
      }
      if (highest < con.NLABHALF){//om det är egen skål som ger mest poäng
        checkGoal(highest-con.val[highest],highest);
      }
      else{//om motståndaren kan få fler poäng
        int highest2=0;
        for (Integer itr : alt){//testar om man kan gå varvet runt
          int i = itr.intValue();
          if (i<con.NLABHALF && con.val[i]>con.NLABHALF){
            if (highest2>0){
              if (con.val[con.NLABELS-(i-con.val[i])]>con.val[con.NLABELS-(highest2-con.val[highest2])]){
                highest2=i;
              }
            }
            else{
              highest2=i;
            }
          }
        }
        if (highest2>0){//om det går att gå varvet runt med en av sina egna och förhindra motspelaren att träffa egen skål
          checkGoal(highest2,highest2);
        }
        else{//om man inte kan gå varvet runt får man istället skydda sina egna
          checkGoal(con.NLABHALF,con.NLABELS-(highest-con.val[highest]));
        }
      }
    }
    else{//om ingen skål blir träffad
      KalahaMove move = new KalahaMove(con);
      for (int i=1; i<con.NLABHALF; i++){//om målskålen kan bli träffad
        if (i-con.val[i]==0){
          move.changeValues(i);
          i=0;
        }
      }
      highest=1;
      for (int i=2; i<con.NLABHALF; i++){//testar vilken skål som har flest kulor
        if (con.val[i]>con.val[highest]){
          highest=i;
        }
      }
      move.changeValues(highest);
    }
    con.turn = true;
    //AI:n går att förbättra ytterligare genom att testa om motståndaren kan träffa sin egen skål och därmed få nya möjligheter att stjäla kulor, men tanken är inte att AI:n ska vara omöjlig att slå. Nästa steg kan annars vara att skapa olika svårighetsgrader av AI:n.
  }

  private ArrayList<Integer> checkHitEmptyBowl(int start, ArrayList<Integer> alt){//hjälpfunktion för att se om en tom skål kan bli träffad
    for (int i=start; i<con.NLABHALF+start-1; i++){
      if (con.val[i]>0){
        int j=(con.VALMAX+i-con.val[i])%con.NLABELS;//korrigerar om man går utanför indexet
        if (con.val[i]>=i+con.NLABHALF){//korrigerar om man går förbi motståndarens målskål, denna ska inte träffas
          j=(j-1+con.NLABELS)%con.NLABELS;
        }
        if (j>0){
          if (con.val[j]==0 && con.val[i]<=con.NLABELS && con.val[con.NLABELS-j]>0){
            if (j<con.NLABHALF && start==1 || j>con.NLABHALF && start==con.NLABHALF+1){
              alt.add(new Integer(i));
            }
          }
        }
      }
    }
    return alt;
  }

  private void checkGoal(int j, int k){//hjälpfunktion för att se om målskålen kan bli träffad
    boolean hit = false;
    KalahaMove move = new KalahaMove(con);
    for (int i=1; i<j; i++){//om målskålen kan bli träffad innan
      if (i-con.val[i]==0){
        move.changeValues(i);
        computersMove();//ty nytt läge, ny tom skål har uppstått
        hit = true;
        break;
      }
    }
    if (!hit){
      move.changeValues(k);
    }
  }
}
