import java.io.PrintStream;
import java.util.Scanner;

/**
 *By: Ryan Miller
 * Help from Andrew Cantrell
 * This class creates a BST that helps the user play a game of
 * 20 Questions.
 */
public class QuestionsGame {
    private QuestionNode tree;

    /**
     * initialize a new QuestionsGame object with a single node: object.
     * @param object the object the user is thinking of
     */
    public QuestionsGame(String object){
        tree = new QuestionNode(object);
    }

    /**
     * initialize a new QuestionsGame by reading from the scanner
     * which contains a tree of questions.
     * @param input input from the user
     */
    public QuestionsGame(Scanner input){
        tree = builder(input, tree);
    }

    /**
     * a helper method for the questions game constructor to build the BST.
     * @param input is the users input from the Scanner
     * @param root is the QuestionNode object
     */
    private QuestionNode builder(Scanner input, QuestionNode root){
        if(!input.hasNextLine()){
            return root;
        }
        String in = input.nextLine();
        if(in.equalsIgnoreCase("Q:")){
            root = new QuestionNode(input.nextLine());
            root.left = builder(input, root.left);
            root.right = builder(input, root.right);
        } else if(in.equalsIgnoreCase("A:")){
            root = new QuestionNode(input.nextLine());
        }
        return root;
    }

    /**
     * This method stores the current question in an output file which
     * uses PrintStream.
     */
    public void saveQuestions(PrintStream output){
        if(output == null){
            throw new IllegalArgumentException();
        }
        saveQuestions(output, tree);
    }

    /**
     * a private helper method to use recursion to manuver the tree.
     */
    private void saveQuestions(PrintStream output, QuestionNode root){
        if(root != null){
            String temp = root.data;
            if(temp.endsWith("?")){
                output.println("Q:");
                output.println(temp);
                saveQuestions(output, root.left);
                saveQuestions(output, root.right);
            } else{
                output.println("A:");
                output.println(temp);
            }
        }
    }

    /**
     * this method uses the current question tree to play the game until
     * reaching the answer on a leaf node
     * @param input is the users input from the Scanner
     */
    public void play(Scanner input){
        if(tree == null){
            throw new IllegalArgumentException("Root is Null!!!");
        }
        tree = play(input, tree);
    }

    /**
     * a helper method which uses recursion for the play public method
     * @param input is the users input from the Scanner
     * @param root is the QuestionNode object
     * @return the QuestionNode of the correct object
     */
    private QuestionNode play(Scanner input, QuestionNode root){
        if(root.data.endsWith("?")){
            System.out.print(root.data + " (y/n)? ");
            if(input.nextLine().trim().toLowerCase().startsWith("y")){
                root.left = play(input, root.left);
            } else {
                root.right = play(input, root.right);
            }
        } else{
            System.out.println("I guess that your object is " + root.data + "!");
            System.out.print("Am I right? (y/n)? ");
            if(input.nextLine().trim().toLowerCase().startsWith("y")){
                System.out.println("Awesome! I win!");
            } else {
                return newQuestion(input, root);
            }
        }
        return root;
    }

    /**
     * this will create a new question if the computer loses the game.
     * @param input is the users input from the Scanner
     * @param root is the QuestionNode object
     * @return the new QuestionNode for the new object
     */
    private QuestionNode newQuestion(Scanner input, QuestionNode root){
        System.out.println("Boo! I Lose.  Please help me get better!");
        System.out.print("What is your object? ");
        QuestionNode answer = new QuestionNode(input.next());
        input.nextLine();
        System.out.println("Please give me a yes/no question that distinguishes between " +
                answer.data + " and " + root.data + ".");
        System.out.print("Q: ");
        String newQ = input.nextLine();
        if(!newQ.endsWith("?")){
            throw new IllegalArgumentException();
        } else {
            System.out.print("Is the answer \"yes\" for " + answer.data + "? (y/n)? ");
            if (input.nextLine().trim().toLowerCase().startsWith("y")) {
                root.right = new QuestionNode(answer.data);
                root.left = new QuestionNode(root.data);
            } else{
                root.left = new QuestionNode(answer.data);
                root.right = new QuestionNode(root.data);
            }
        }
        return new QuestionNode(newQ, root.left, root.right);
    }

    /**
     * creates the private class for the Binary Tree, which will
     * allow us to navigate the questions.
     */
    private static class QuestionNode {
        public final String data;
        public QuestionNode right;
        public QuestionNode left;

        public QuestionNode(String data, QuestionNode right, QuestionNode left) {
            this.data = data;
            this.right = right;
            this.left = left;
        }

        public QuestionNode(String data) {this(data, null, null); }
    }
}