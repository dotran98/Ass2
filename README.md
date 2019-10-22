# Ass2



******Below is the requirement's list. It is not complete. There is still a great deal of editting that must be done. ***
Note/ask Tim: Worry about the testing part (provide students with the test and mark it) or are we just concerned with the results and the attendance? (just activities completed and attendance)
App design- what does Time mean by information about which group the students attend?
The three types of badges (are they gold, diamond and platinum)? How do the students get which colour badge- is it based on mark or how many topics they complete within the test? (it’s based on how many tests you complete. Thus, we have to set how many tests are required to be done for a student to get to a specific level - gold, diamond, platinum)  
Core badge and special badge- what’s the difference? (there is 2 different types of badges, same process to acquire them, the only difference is in the type of content learned)
3. Implementation- which colour six are they in? (disregard this part of the task)

Requirements Document

Students follow a curriculum and do groups of activities. Database needs to monitor, follow and provide “badges”.
Functional Requirements:
-The application should be able to add a student to the database, including at least their first name, last name, DOB and which class they attend.
-A student’s attendance must be recorded for each session (a session can be a weekly meeting or a weekend intensive course). 
-The app’s database must be able to record the parts, topics and tests completed. 
-The app’s user (the teacher) should be able to search for each student individually through a number of ways. A few such examples include, the student’s unique student number, name, age or date of birth. 
-Additionally, the teacher should be able to view the student’s personal details and achievements. 
-There is also the requirement that the app notifies the teaching staff after a student has successfully completed an entire badge. A badge consisted of seven compulsory tests and three optional tests.
-The teaching staff must be notified or permitted to search for which parts, topics or tests have not been widely completed. As such, this will enable the teacher to focus on these areas during the upcoming weekly meeting. 
-As the user’s details change, they must be given the authority to edit and update their personal information.
-A planned test, including the teacher who will be responsible for “running” the test must be able to be entered into the app.
-The database must include a schedule outlining when and where the “classes” will be held. Additionally, the previous lectures must also be available to enable any students who missed a lecture to study and catch up on the content that they missed. 
 
PORTECTED FROM SQL INJECTION. The database’s information will be secured and encoded to minimise the risk of someone hacking into the database. If this were to occur then there would be significant ethical repercussions if the performance of the students was released. 
Each student is required to be assigned a unique student number to enable quick and easy identification. This is essential as it can be very difficult to distinguish between two students if they both have the same name. 
Each test, topic and part will be assigned a unique code to enable quick and easy identification of them. ASSUMPTIONSSSSSSSSSSSS
An additional inclusion to the app will be a leaderboard. The leaderboard will be used to provide a visual illustration of how each of the students are performing. 
There will be two types of achievement badges. There are special badges that let students focus on a special interest or skill and core badges that focus on the core curriculum.
- Student finishes test, part or topic and inform teacher
There must be a “badge hierarchy” which will be used to differentiate the student’s on a merit-based system. 

It should, it must, etc. 
 
Students or teachers may choose 2-3 topics of their own preference out of the test

 
Non-functional:
One of the non-functional requirements that was identified was to have one consolidated database for the test, student information and attendance. One database for each trimester



Assumptions:
One session must be run by one teacher
Student has to do three parts to finish one topic
Three topics to finish one test
Any topic in any test in any part that is completed by less tan 20% of the number of studnets who completed the most popular course. 
FORMAT OF THE ID- 1 badge, 1.1 test, 1.1.1 topic within test, 1.1.1.1 part within a topic within a test
From 1000 badges to 3000 badges is gold. Diamond is from 3001-5000. Platinum is 5001+
 
 

