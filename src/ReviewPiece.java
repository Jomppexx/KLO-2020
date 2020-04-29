import javax.swing.*;

//Arvostelu nimikkeelle
public class ReviewPiece implements java.io.Serializable{
    private String nimike;
    private String kategoria;
    private String otsikko;
    private int arvosana;
    private String arvosteluteksti;
    public String date;
    //exTitle on vanhan nimikkeen nimi, jos nimiketietoja muutetaan. Käytetään
    //tietojen muuttamiseen MainScreenin changeassociatedeReviews-funktiossa.
    public String exTitle;
    //exCategory on vanhan nimikkeen kategoria, jos nimiketietoja muutetaan. Käytetään
    //tietojen muuttamiseen MainScreenin changeassociatedeReviews-funktiossa.
    public String exCategory;

    public ReviewPiece(String title, String category,
                       String header, int grade, String reviewtext,
                       String ddmmyy) {
        nimike = title;
        kategoria = category;
        otsikko = header;
        arvosana = grade;
        arvosteluteksti = reviewtext;
        date = ddmmyy;
    }

    public String getDate(){
        return date;
    }
    public void setDate(String ddmmyy){
        date = ddmmyy;
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
    public void setExTitle(String vanhanimike) {
        exTitle = vanhanimike;
    }
    public String getExTitle() {
        return exTitle;
    }
    public void setExCategory(String vanhakategoria) {
        exCategory = vanhakategoria;
    }
    public String getExCategory() {
        return exCategory;
    }
    public void changeTitleInfo(String title, String category) {
        exTitle = getNimike();
        exCategory = getKategoria();
        setNimike(title);
        setKategoria(category);
    }
}
