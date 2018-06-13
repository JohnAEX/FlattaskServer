Narrative:
Etc.

Scenario: A premise is generated.
Given there exists a Community as follows:
|id         |name           |creationTime   |
|TestCom1   |SpaßAmStrand   |01.01.1970     |
Given there exist Participators as follows:
|username   |password       |firstname  |lastname       |balance    |role       |
|John       |John           |Jonathan   |Grenda         |1000       |user       |
|Lucas      |Lucas          |Lucas      |Merker         |1000       |user       |
|Marcel     |Marcel         |Marcel     |Sowada         |1000       |user       |
|Dominik    |Dominik        |Dominik    |Genz           |1000       |user       |
Given the Tasks in that Community are as follows:
|title      |description    |baseValue  |baseDuration   |
|title1     |desc1          |1          |10             |
|title2     |desc2          |10         |100            |
|title3     |desc3          |100        |1000           |
Given the Dotos in that Community are as follows:
|title      |description    |value      |duration       |assignedTo |assignedBy |
|doto1      |doto1-1        |2          |2              |John       |Lucas      |
|doto2      |doto2-1        |20         |20             |Lucas      |Marcel     |
|doto3      |doto3-1        |200        |200            |Marcel     |Dominik    |
|doto4      |doto4-1        |2000       |2000           |Dominik    |John       |
|doto5      |doto5-1        |22         |22             |           |John       |
|doto6      |doto6-1        |23         |23             |           |John       |
|doto7      |doto7-1        |24         |24             |           |John       |
|doto8      |doto8-1        |25         |25             |           |John       |
When the premise is persisted.

Scenario: John grabs all Dotos
When an OpenDotosManagedBean-Instance is generated for the user John.
When John grabs all open Dotos.
When the performed action(s) are confirmed.
Then all Dotos should be assigned to John.

Scenario: All Dotos are unassigned
When an OpenDotosManagedBean-Instance is generated for the user John.
When all Dotos are unassigned.
When the performed action(s) are confirmed.
Then all Dotos should be assigned to noone.