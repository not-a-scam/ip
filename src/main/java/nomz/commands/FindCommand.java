package nomz.commands;

import static nomz.common.Messages.MESSAGE_FIND_NO_MATCH;
import static nomz.common.Messages.MESSAGE_FIND_RESULTS_HEADER;

import java.util.ArrayList;

import nomz.data.exception.NomzException;
import nomz.data.tasks.Task;
import nomz.data.tasks.TaskList;
import nomz.storage.Storage;

/**
 * Command to find tasks by description
 */
public class FindCommand extends Command {
    private String keyword;

    /**
     * Creates a FindCommand to search for tasks by description.
     * @param keyword the keyword to search for
     */
    public FindCommand(String keyword) {
        this.keyword = keyword;
    }

    @Override
    public String execute(TaskList tasks, Storage storage) throws NomzException {
        ArrayList<Task> matched = new ArrayList<>();
        for (Task t : tasks.getTasks()) {
            if (t.getDescription().contains(this.keyword)) {
                matched.add(t);
            }
        }

        if (matched.isEmpty()) {
            return MESSAGE_FIND_NO_MATCH.formatted(this.keyword);
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append(MESSAGE_FIND_RESULTS_HEADER);
            for (int i = 0; i < matched.size(); i++) {
                sb.append((i + 1) + ". " + matched.get(i).toString() + "\n");
            }
            return sb.toString().trim();

        }
    }
}
