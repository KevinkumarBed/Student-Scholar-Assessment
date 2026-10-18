package course;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

// Kevin: tests for condition 10, condition 11 and the all valid case
class CourseKevinTest {

   private Course course;

   @BeforeEach
   void setUp() {
      course = new Course();
   }

   // precondition: the course has no assessments before the test runs
   // and the other values used in the tests are valid on their own
   private void checkPrecondition() {
      assertTrue(course.getAssessments().isEmpty());
      Course other = new Course();
      assertTrue(other.createNewAssessment("assignment 1", "build a simple web app", 20, "pdf",
            false, "", "01/11/2026 09:00", "15/11/2026 23:59", "30/11/2026 23:59"));
   }

   // Test case 1: condition 10, date time format and real calendar dates
   @ParameterizedTest
   @CsvSource({
         // 29/02/2026 does not exist because 2026 is not a leap year
         "29/02/2026 20:00, 15/03/2026 23:59, 30/03/2026 23:59",
         // hour 24 is not valid on a 24 hour clock
         "01/03/2026 09:00, 15/03/2026 24:00, 30/03/2026 23:59"
   })
   void testCondition10(String publish, String due, String hide) {
      checkPrecondition();

      boolean result = course.createNewAssessment("assignment 1", "build a simple web app", 20,
            "pdf", false, "", publish, due, hide);

      assertFalse(result);
      assertEquals(0, course.getAssessments().size());
   }

   // Test case 2: condition 11, publish before due before hide
   @ParameterizedTest
   @CsvSource({
         // due date is the same as publish date
         "10/05/2026 12:00, 10/05/2026 12:00, 20/05/2026 12:00",
         // hide date is the same as due date
         "10/05/2026 12:00, 20/05/2026 12:00, 20/05/2026 12:00"
   })
   void testCondition11(String publish, String due, String hide) {
      checkPrecondition();

      boolean result = course.createNewAssessment("assignment 1", "build a simple web app", 20,
            "pdf", false, "", publish, due, hide);

      assertFalse(result);
      assertEquals(0, course.getAssessments().size());
   }

   // Test case 3: everything is valid
   @ParameterizedTest
   @CsvSource({
         // normal individual assessment
         "assignment 1, build a simple web app, 20, pdf, false, '', 01/11/2026 09:00, 15/11/2026 23:59, 30/11/2026 23:59",
         // boundary values: 50 points, 3 word description, 1 minute between dates
         "group project, three word description, 50, zip, true, team a, 01/01/2026 00:00, 01/01/2026 00:01, 01/01/2026 00:02",
         // leap day in 2028 is a real date
         "final report, write the final report, 30, doc, false, '', 28/02/2028 08:00, 29/02/2028 23:59, 01/03/2028 00:00"
   })
   void testAllValid(String name, String description, int points, String type, boolean group,
         String groupingName, String publish, String due, String hide) {
      checkPrecondition();

      boolean result = course.createNewAssessment(name, description, points, type, group,
            groupingName, publish, due, hide);

      assertTrue(result);
      assertEquals(1, course.getAssessments().size());

      // check the attributes were set from the parameters
      Assessment a = course.getAssessments().get(0);
      assertEquals(name, a.getName());
      assertEquals(description, a.getDescription());
      assertEquals(points, a.getPoints());
      assertEquals(type, a.getSubmissionType());
      assertEquals(group, a.isGroup());
      assertEquals(groupingName, a.getGroupingName());
      assertEquals(publish, a.getPublishDateTime());
      assertEquals(due, a.getDueDateTime());
      assertEquals(hide, a.getHideDateTime());
   }
}
