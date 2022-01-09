package sample;

public class WordRecord {
    public String word;
    public String definition;

    public WordRecord(String word, String definition) {
        this.word = word;
        this.definition = definition;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getDefinition() {
        return definition;
    }

    public void setDefinition(String defintion) {
        this.definition = defintion;
    }
}
