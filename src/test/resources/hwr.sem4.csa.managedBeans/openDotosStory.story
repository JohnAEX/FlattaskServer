Narrative:
Etc.

Scenario: A premise is generated.
Given there exists a Community as follows:
|id         |name           |creationTime   |
|TestCom1   |SpaßAmStrand   |01.01.1970     |
Given there exist Participators as follows:
|username   |password       |firstname  |lastname       |balance    |role       |
|John3      |John3          |Jonathan   |Grenda         |1000       |user       |
|Lucas3     |Lucas3         |Lucas      |Merker         |1000       |user       |
|Marcel3    |Marcel3        |Marcel     |Sowada         |1000       |user       |
|Dominik3   |Dominik3       |Dominik    |Genz           |1000       |user       |
Given the Tasks in that Community are as follows:
|title      |description    |baseValue  |baseDuration   |
|title1     |desc1          |1          |10             |
|title2     |desc2          |10         |100            |
|title3     |desc3          |100        |1000           |
Given the Dotos in that Community are as follows:
|title      |description    |value      |duration       |assignedTo |assignedBy |
|doto1      |doto1-1        |2          |2              |John3      |Lucas3     |
|doto2      |doto2-1        |20         |20             |Lucas3     |Marcel3    |
|doto3      |doto3-1        |200        |200            |Marcel3    |Dominik3   |
|doto4      |doto4-1        |2000       |2000           |Dominik3   |John3      |
|doto5      |doto5-1        |22         |22             |           |John3      |
|doto6      |doto6-1        |23         |23             |           |John3      |
|doto7      |doto7-1        |24         |24             |           |John3      |
|doto8      |doto8-1        |25         |25             |           |John3      |

Scenario: ManagedBeanInitialization-Check
When the premise is persisted.
When an OpenDotosManagedBean-Instance is generated for the user Lucas3.
Then the execution time for the last When-Statement should be less than 1000 milliseconds.
Then the OpenDotosManagedBean should be owned by Lucas3.
Then the OpenDotosManagedBean should refer to the Community TestCom1.
Then the OpenDotosManagedBean should have source Dotos as follows:
|title      |description    |value      |duration       |assignedTo |assignedBy |
|doto5      |doto5-1        |22         |22             |           |John3      |
|doto6      |doto6-1        |23         |23             |           |John3      |
|doto7      |doto7-1        |24         |24             |           |John3      |
|doto8      |doto8-1        |25         |25             |           |John3      |
Then the OpenDotosManagedBean should have target Dotos as follows:
|title      |description    |value      |duration       |assignedTo |assignedBy |
|doto2      |doto2-1        |20         |20             |Lucas3     |Marcel3    |

Scenario: all is assigned
When the premise is persisted.
When an OpenDotosManagedBean-Instance is generated for the user John3.
Then the execution time for the last When-Statement should be less than 1000 milliseconds.
When the Participator grabs all open Dotos.
Then the execution time for the last When-Statement should be less than 1500 milliseconds.
When the performed action(s) are confirmed.
Then the execution time for the last When-Statement should be less than 2000 milliseconds.
Then all Dotos should be assigned to John3.

Scenario: all is unassigned
When the premise is persisted.
When an OpenDotosManagedBean-Instance is generated for the user John3.
Then the execution time for the last When-Statement should be less than 1000 milliseconds.
When all Dotos are unassigned.
Then the execution time for the last When-Statement should be less than 1500 milliseconds.
When the performed action(s) are confirmed.
Then the execution time for the last When-Statement should be less than 2000 milliseconds.
Then all Dotos should be assigned to noone.

Scenario: No Dotos
Given the Dotos in that Community are as follows:
|title      |description    |value      |duration       |assignedTo |assignedBy |
When the premise is persisted.
When an OpenDotosManagedBean-Instance is generated for the user Marcel3.
Then the execution time for the last When-Statement should be less than 1000 milliseconds.
Then the OpenDotosManagedBean should be owned by Marcel3.
Then the OpenDotosManagedBean should refer to the Community TestCom1.
Then the OpenDotosManagedBean should have source Dotos as follows:
|title      |description    |value      |duration       |assignedTo |assignedBy |
Then the OpenDotosManagedBean should have target Dotos as follows:
|title      |description    |value      |duration       |assignedTo |assignedBy |