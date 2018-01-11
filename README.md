# MONA
Mona is a puzzle game that I built several years ago.
The puzzle is essentially Masyu, which I first came into contact with via the Android game ["Necklace"](https://play.google.com/store/apps/details?id=com.niasoft.android.necklace&hl=en) by NIAsoft.
However, the visual style was mainly influenced by the excellent game ["Lyne"](http://www.lynegame.com/) by Thomas Bowker.
I remember putting a lot of effort into polishing the game, mainly motivated by my hopes to commercialize it at some point.
That's the reason why I haven't put it on github any time sooner.
But you know how things go: At some point, I moved my focus elsewhere and eventually completely lost track of it.
When I recently rediscovered some of those old games mentiones, I remembered having Mona in my [Dropbox [APK]](https://www.dropbox.com/s/ww3j6zaneilcxn9/Mona-v0.0.4.apk?dl=0) and decided to give it a try.
To my surprise, everything works pretty well and the amount of polish was also quite impressive (at least to my standards ;) ).
It would be a shame to let the game rot on my hard drive any longer, so now here it is.
I haven't tried building the project (although versioned maven dependencies plus gradle should make this quite simple, I'd think?) nor can I remember whether there were any other gotchas to get this going.
All I can tell is that from my now much more C++-accustomed eyes, the codebase looks horribly bloated.
I really had a thing for doing things more complicated than necessary 4 years ago...
It seems improbable that I'll ever come back to this project.
So feel free to do whatever you want with it :) Have fun!

## Some text that I don't even know what it's about any more:
Object types:

Singletons: 
  Globally accessible via static getter. Used when the class is either lightweigt enough to persist over the whole lifetime or when the program can never run without an instance.
Managed/Shared Singletons: 
  Available over a manager (ResourceConsumerManager, therefore 'managed') that manages lifetime and therefore memory consumption of many managed singletons. Used for resource heavy objects that usually can be used multiple times (using a set() method) and thus can be 'shared' between different places.

