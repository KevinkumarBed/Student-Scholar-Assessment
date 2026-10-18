package course;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.ArrayList;
import java.util.List;

public class Course {
   private List<Assessment> assessments = new ArrayList<>();

   public List<Assessment> getAssessments() {
      return assessments;
   }

   public boolean createNewAssessment(String name, String description, int points,
                                      String submissionType, boolean group, String groupingName,
                                      String publishDateTime, String dueDateTime, String hideDateTime) {
      boolean valid = true;

      // conditions 1 to 9 are written by the other members

      // condition 10: date times must be a real date in the format DD/MM/YYYY hh:mm
      LocalDateTime publish = toDateTime(publishDateTime);
      LocalDateTime due = toDateTime(dueDateTime);
      LocalDateTime hide = toDateTime(hideDateTime);
      if (publish == null || due == null || hide == null) {
         valid = false;
      } else if (!publish.isBefore(due) || !due.isBefore(hide)) {
         // condition 11: publish must be before due, and due before hide
         valid = false;
      }

      // only create and store the assessment if everything was valid
      if (valid) {
         assessments.add(new Assessment(name, description, points, submissionType, group,
               groupingName, publishDateTime, dueDateTime, hideDateTime));
      }
      return valid;
   }

   // returns null if the date time is not in the right format or is not a real date
   private LocalDateTime toDateTime(String text) {
      if (text == null || text.length() != 16) {
         return null;
      }
      DateTimeFormatter format = DateTimeFormatter.ofPattern("dd/MM/uuuu HH:mm")
            .withResolverStyle(ResolverStyle.STRICT);
      try {
         return LocalDateTime.parse(text, format);
      } catch (DateTimeParseException e) {
         return null;
      }
   }
}
