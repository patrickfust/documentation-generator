package dk.fust.docgen;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * User input
 */
public class UserInput {

    private String question;

    private String answer;

    /**
     * Constructor
     * @param question question to ask
     */
    public UserInput(String question) {
        this.question = question;
    }

    /**
     * Will ask the user the question
     * @return users answer
     */
    public String getAnswer() {
        if (answer == null) {
            try {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
                System.out.println(question + ":");
                answer = bufferedReader.readLine();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return answer;
    }
}
