import java.util.*;
import java.lang.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class KalahaMove{
  public KalahaControl con;

  public KalahaMove(KalahaControl c){
    con = c;
  }

  public int changeValues(int i){ //korrigerar värdena
    i = i%con.NLABELS;
    int k=i%con.NLABELS;
    int n=0;
    for (int j=1;j<=con.val[i];j++){
      k=(i-j)%con.NLABELS;
      if (k<0){
        k=(k+con.VALMAX)%con.NLABELS;
      }
      if (i>con.NLABHALF && k==0 || i<con.NLABHALF && k==con.NLABHALF){//korrigerar för om man går igenom motståndarens målskål
            n++;
            if (con.val[i]<n*(con.NLABELS-1) && con.val[i]>(n-1)*(con.NLABELS-1)){
                int l=(con.VALMAX+i-con.val[i]-1)%con.NLABELS;
                if (i>con.NLABHALF && k==0 && l==0 || i<con.NLABHALF && k==con.NLABHALF && l==con.NLABHALF){
                  l--;
                  l=(con.NLABELS+l)%con.NLABELS;
                }
                updateValues(l,con.val[l]+1);
            }
      }
      else{// det normala förfarandet
        updateValues(k,con.val[k]+1);
      }
    }
    con.val[i]=con.val[i]/(con.NLABELS-1);
    con.kal[i].setText(Integer.toString(con.val[i]));
    if (k!=0 && k!=con.NLABHALF){
      if (con.val[k]==1 && con.val[con.NLABELS-k]>0){ //testar om du slutat i en tom skål
        if (con.turn && k>con.NLABHALF){
          emptyBowl(k, con.NLABHALF);
        }
        else if (!con.turn && k<con.NLABHALF && k>0){
          emptyBowl(k, 0);
        }
      }
    }
    checkVictory();
    return k;
  }

  private void updateValues(int i, int v){
    con.val[i]=v;
    con.kal[i].setText(Integer.toString(v));
  }

  private void emptyBowl(int i, int j){//hjälpmetod om man slutat i en tom skål
    updateValues(j,con.val[j]+con.val[con.NLABELS-i]+1);
    updateValues(i,0);
    updateValues(con.NLABELS-i,0);
  }

  private void checkVictory(){//testar om spelet är slut
    boolean victory = true;
    for (int i=0; i<con.NLABELS; i+=con.NLABHALF){
      for (int j=1+i; j<con.NLABHALF+i; j++){
        if (con.val[j]!=0){
          victory = false;
          break;
        }
      }
      if (victory){//om spelet är slut
          int k=(i+con.NLABHALF)%con.NLABELS;
          for (int j=k+1; j<con.NLABELS-i; j++){//räknar slutpoäng för alla kvarvarande kulor
            con.val[k]+=con.val[j];
            updateValues(j,0);
          }
          con.kal[con.NLABHALF].setText(Integer.toString(con.val[con.NLABHALF]));
          con.kal[0].setText(Integer.toString(con.val[0]));
          con.printResult();
      }
      victory=true;
    }
  }
}
