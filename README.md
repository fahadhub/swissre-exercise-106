Note: Checks and handlers can be added for assumptions made to make the code more robust, but in the interest of time and to keep the program simple as required by the problem statement, the assumptions mentioned below are made.

Assumptions:
1. If manager id is invalid skip it
2. If no ceo, throw error
3. Employee IDs are unique
4. Exactly one CEO
5. No circular managmenet chains
6. Employee has only one direct manager
7. Salaries are always positive numbers


How to run the application:
1. In the base directory (where pom.xml resides), run: mvn clean package
2. Once jar gets created, run java -jar target/employee-analyzer-1.0-SNAPSHOT.jar <path-to-csv-file>


Test cases:

CSV:
1. Check if csv is parsed correctly
2. Check if csv parser handles malformed data gracefully


Hierarchy Builder
1. Check if code handles no ceo gracefully
2. Check if code handles invalid manager id gracefully


Salary Analysis Tests
1. Check if everyone is correctly paid
2. Check if manager is underpaid
3. Check if manager is overpaid
4. Check when multilevel hierarchy with nested underpaid/overpaid managers
5. No subordinates
6. Null root


Reporting Depth Tests (checkReportingDepth)
1. Shallow hierarchy
2. Deep hierarchy
3. Multiple deep employees
4. Null root
