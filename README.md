# HornetInbox
Hi! Thanks for taking an interest in Hornet!
Here are the requirements for this coding interview:

We want to create an app that simulates a chat inbox.
To do this, a raw resource file has been created that acts as a data source for a service that returns data when a request is made to it. To request a page of data, call the getPage method in the DataFetcher object. Requesting a page of data will return a String with JSON formatting, containing a page number, and an array of Users which represent conversations in the clients inbox. Each page contains 10 User objects, and the data format can be seen in the data.txt file in the res/raw folder.

## Requirements
### Create an app that retrieves inbox information in the form of individual conversations, and displays it in an inbox list. 
- Each conversation in the list should be displayed with a profile picture, the user's name, and the last time a message was received.
- The profile picture should consist of only an upper case letter that the user's name begins with. Every profile picture should be uniquely identifiable, even when two users have the same Initial. Explain in comments any limitation that your solution may have in this regard.
- The last time a message was received should be in the format of how long ago it happened. Some examples: _10 seconds ago_, _30 minutes ago_, _1 hour ago_, _37 days ago_. You may round these units however you would like to. The list should be ordered from newest to oldest conversation.
- The conversation data is paginated, and a new page should only be requested when it is going to be viewed by the user.

### Simulate the process of receiving a new message and updating the inbox list accordingly
- Add a fab that updates the "last_message_at" value of a random User to the current time. This process only needs to be performed for Users already added to the list. No need to fetch every unique User from the DataFetcher.
- The list should animate smoothly during this process

The basic code needed is provided, but it might not all work 100% 😉. Make any changes required to make the code work correctly. Use any libraries you would like to.
Make a pull request once you are happy with your work.
