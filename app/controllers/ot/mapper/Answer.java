package controllers.ot.mapper;

/**
 * Created by amd on 11/27/15.
 */
public class Answer {

    private int index;
    private String value;
    private Boolean correct;

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Boolean getCorrect() {
        return correct;
    }

    public void setCorrect(Boolean correct) {
        this.correct = correct;
    }

    public boolean isValid(){
        if ( (index<=0) || (value==null || value.trim().equals("")) )
            return false;
        else return true;
    }

    public Boolean isCorrect(){
        return correct;
    }

    @Override
    public int hashCode() {
        int hashCode = this.value.hashCode();
        return hashCode;
    }

    @Override
    public boolean equals(Object obj){
        Answer answer = (Answer)obj ;
        return this.value.equals(answer.value);
    }
}
