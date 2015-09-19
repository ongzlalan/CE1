import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class TextBuddy {
  
  private static final String MESSAGE_ADDED = "added to %s: \"%s\"\n";
  private static final String MESSAGE_CLEAR = "all content deleted from %s\n";
  private static final String MESSAGE_COMMAND = "command: ";
  private static final String MESSAGE_DELETE = "delete from %s: \"%s\"\n";
  private static final String MESSAGE_INVALID_FORMAT = "invalid command\n";
  private static final String MESSAGE_INVALID_ID = "invalid item ID\n";
  private static final String MESSAGE_EMPTY = "%s is empty\n";
  private static final String MESSAGE_DISPLAY = "%d. %s\n";
  private static final String MESSAGE_WELCOME = "Welcome to TextBuddy. %s is ready for use\n";
  
  private static final String FILENAME = "mytextfile";
  
  // These are the possible command types
  enum COMMANDS {
    DISPLAY, ADD, DELETE, CLEAR, EXIT
  };
  
  // Filename of output text file
  private String fileName;
  
  // This stores the inputs
  private final ArrayList<String> list = new ArrayList<String>();
  
  private final BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
  
  public static void main(String[] args) {
    TextBuddy textBuddy = new TextBuddy();
    textBuddy.run();
  }
  
  public TextBuddy() {
    fileName = FILENAME.concat(".txt");
    print(String.format(MESSAGE_WELCOME, fileName));
  }
  //For testing
  public TextBuddy(String args) {
	    fileName = args.concat(".txt");
	    print(String.format(MESSAGE_WELCOME, fileName));
	  }
 
  /**
   * Bulk of program. Program will terminate on exit command.
   */
  public void run() {
    
    while (true) {
      //print(MESSAGE_COMMAND);
      
      try {
        // Save the first input as the command and the second input as the parameter
        String[] cmd = in.readLine().trim().split(" ", 2);
        
        // Check for command according to the list of possible command types
        switch (COMMANDS.valueOf(cmd[0].toUpperCase())) {
          
          case DISPLAY :
            print(display());
            break;
            
          case ADD :
            print(add(cmd[1]));
            break;
            
          case DELETE :
            print(delete(cmd[1]));
            break;
            
          case CLEAR :
            print(clear());
            break;
            
          case EXIT :
            System.exit(0);
            break;
            
          default:
            print(MESSAGE_INVALID_FORMAT);
        }
      } catch (Exception e) {
        print(MESSAGE_INVALID_FORMAT);
      }
    }
  }
  
  /**
   * Writes the list to the file.
   * 
   */
  private void writeToFile() {
    try {
      FileWriter fileStream = new FileWriter(fileName);
      BufferedWriter file = new BufferedWriter(fileStream);
      
      for (String storedItem : list) {
        file.write(storedItem);
        file.newLine();
      }
      
      file.flush();
      file.close();
    } catch (IOException e) {
      System.out.println("Error during writing");
    }
  }
  
  /**
   * Prints out the list.
   * 
   * @return Returns the numbered list.
   */
  public String display() {
    StringBuffer output = new StringBuffer();
    if (list.isEmpty()) {
      output.append(String.format(MESSAGE_EMPTY, fileName));
    } else {
      for (int i = 0; i < list.size(); i++) {
        output.append(String.format(MESSAGE_DISPLAY, (i + 1),list.get(i)));
      }
    }
    return output.toString();
  }
  
  /**
   * Adds an item to the list and saves to file.
   * 
   * @return "item added" message
   */
  public String add(String item) {
    list.add(item);
    writeToFile();
    return String.format(MESSAGE_ADDED, fileName, item);
  }
  
  /**
   * Deletes an item from the list and saves to file. 
   * 
   * Checks if list is empty or if item index is out of bounds.
   * 
   * @return "message deleted" message
   */
  public String delete(String indexOfItem) {
    
    int id = Integer.parseInt(indexOfItem);
    
    if (id > 0 && list.size() >= id) { // Check if ID is valid
      int index = id - 1; // 0 based indexing list
      String item = list.get(index);
      list.remove(index);
      writeToFile();
      return String.format(MESSAGE_DELETE, fileName, item);
      
    } else if (list.isEmpty()) {
      return String.format(MESSAGE_EMPTY, fileName);
    } else {
      return String.format(MESSAGE_INVALID_ID);
    }
  }
  
  /**
   * Clears the list and saves to file.
   * 
   * @return cleared message
   */
  public String clear() {
    list.clear();
    writeToFile();
    return String.format(MESSAGE_CLEAR, fileName);
  }
  
  private void print(String str){
    System.out.print(str);
  }
  
}