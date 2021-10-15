# HornetInbox
Hi! Thanks for taking interest in Hornet!
Here are the requirements for this coding interview:

We want to create an app that simulates a chat inbox.
To do this, a raw resource file has been created that acts as a data source for a service that returns data when a request is made to it. To request a page of data, call the getPage method in the DataFetcher object. Requesting a page of data will return a String with JSON formatting, containing a page number, and an array of Users which represent conversations in the clients inbox. Each page contains 10 User objects, and the data format can be seen in the data.txt file in the res/raw folder.

## Requirements
### Create an app that retrieves inbox information in the form of individual conversations, and displays it in an inbox list. 
- Each conversation in the list should be displayed with a profile picture, the user's name, and the last time a message was received.
- The profile picture should consist of an upper case letter than the user's name begins with.
- The last time a message was received should be in the format of how long ago it happened. Some examples: _10 seconds ago_, _30 minutes ago_, _1 hour ago_, _37 days ago_. You may round these units however you would like to. The list should be ordered from newest to oldest conversation.
- These conversations should be divided into two lists: The first list should contain the 20 most recent conversations, and the second should contain all remaining conversations. These lists should be seperated by a pager that the user can swipe left and right on.
- The conversation data is paginated, and should be loaded appropriately. A new page should only be requested when the user tries to scroll the list, or when viewing an empty list.

### Simulate the process of receiving a new message and updating the inbox list appopriately
- Add a fab that updates the "last_message_at" value of a random User to the current time. This process only needs to be performed for Users already requested from the DataFetcher. No need to fetch every unique User from the DataFetcher.
- Both the inbox lists should be updated appropriately when this happens.

### Bonus requirements
- Insure that each User will always have a unique profile picture, even when the name of two Users starts with the same letter
- Update the inbox list smoothly when it is reordering

The basic code needed is provided, but it might not all work 100% 😉.
Make a pull request once you are happy with your work.
