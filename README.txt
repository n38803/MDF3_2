Shaun Thompson
MDF3 1503


Github: https://github.com/n38803/MDF3_2


SPECIAL INSTRUCTIONS:
Please note that the video submission shows a publish button and a refresh button.  I had done this because I was having difficulty forcing the application to update automatically on its own.  I had to create a method within the onUpdate to correspond directly with the notifyAppWidgetViewDataChanged method.  Silly enough, all I had to do was move the same code to my onSave method within my form.. and that did the trick.