import java.util.ArrayList;

public class Invoker {
    private ArrayList<Command> historyOfCommands = new ArrayList<Command>();

    public void invoke(Command command) throws Exception {
        // Add to the list of commands that are to be executed

        if (command != null) {
            this.historyOfCommands.add(command);
            command.execute();
        }

        // Keeping the size of history of commands in check
        int maxHistorySize = 4;
        if (historyOfCommands.size() >= maxHistorySize) {
            int overflowSize = historyOfCommands.size() - maxHistorySize;
            for (int i = 0; i < overflowSize; i++) {
                historyOfCommands.remove(i);
            }
        }
    }

    public int getCommandSize() {
        return historyOfCommands.size();
    }
}
