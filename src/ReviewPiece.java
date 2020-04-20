import javax.swing.*;

//Arvostelu nimikkeelle
public class ReviewPiece implements java.io.Serializable{
    private String nimike;
    private String kategoria;
    private String otsikko;
    private int arvosana;
    private String arvosteluteksti;

    public ReviewPiece(/*String title, String category,*/ String header, int grade, String reviewtext) {
        //nimike = title;
        //kategoria = category;
        otsikko = header;
        arvosana = grade;
        arvosteluteksti = reviewtext;
    }

    public String getNimike(){
        return nimike;
    }
    public void setNimike(String title){
        nimike = title;
    }
    public String getKategoria(){
        return kategoria;
    }
    public void setKategoria(String category){
        kategoria = category;
    }
    public String getOtsikko(){
        return otsikko;
    }
    public void setOtsikko(String header){
        otsikko = header;
    }
    public int getArvosana(){
        return arvosana;
    }
    public void setArvosana(int grade){
        arvosana = grade;
    }
    public String getArvosteluteksti(){
        return arvosteluteksti;
    }
    public void setArvosteluteksti(String reviewtext){
        arvosteluteksti = reviewtext;
    }
}
