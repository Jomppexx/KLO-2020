
// Luokka, jolla luodaan erilaisia viihteen muotoja, joita voidaan sitten arvostella
public class EntertainmentPiece {

    private String entertainmentName;
    private String category;

    public EntertainmentPiece(String enternName, String categ){
        entertainmentName = enternName;
        category = categ;
    }

    public String getEntertainmentName(){
        return entertainmentName;
    }

    public void setEntertainmentName(String newName){
        entertainmentName = newName;
    }

    public String getCategory(){
        return category;
    }

    public void setCategory(String newCategory){
        category = newCategory;
    }
}
