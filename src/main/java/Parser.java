import java.time.LocalDateTime;

public class Parser {

    private static DateParser dp = new DateParser();

    /** Convert a string to an integer, throwing an exception if it is not a valid integer. */
    public int intFromString(String index) throws InvalidNomzArgumentException {
        try { 
            return Integer.parseInt(index); 
        } catch (NumberFormatException e) { 
            throw new InvalidNomzArgumentException(Messages.MESSAGE_INVALID_INTEGER_ARGUMENT); 
        }
    }

    public Task parseTaskFileContent(String f) throws NomzException{
            String[] args = f.split("[\\|]");
            TaskType type = TaskType.fromSymbol(args[0]);
            boolean done = args[1].equals("1");
            switch(type){
            case TODO:
                Todo todo = new Todo(args[2]);
                if (done) {
                    todo.mark();
                }
                return todo;

            case DEADLINE:
                LocalDateTime by = dp.parseDateTimeFlexible(args[3]);
                Deadline deadline;
                if (by == null) {
                    deadline = new Deadline(args[2], args[3]);
                } else {
                    deadline = new Deadline(args[2], by);
                }
                if (done) {
                    deadline.mark();
                }
                return deadline;
            case EVENT:
                LocalDateTime from = dp.parseDateTimeFlexible(args[3]);
                LocalDateTime to = dp.parseDateTimeFlexible(args[4]);
                Event event;
                if (from == null || to == null) {
                    event = new Event(args[2], args[3], args[4]);
                } else {
                    event = new Event(args[2], from, to);
                }
                if (done) {
                    event.mark();
                }
                return event;
            default:
                throw new InvalidNomzArgumentException(Messages.MESSAGE_INVALID_FORMAT);
            }
    }

}
