#BlueGin#
BlueGin is a speedy minimalist Java game engine. No fancy physics or anything, but a nice rendering framework, asset management, etc. (maybe some gui stuff). It even handles all the dumb LWJGL extensions, versions, and annoying loading/binding/shit. 

Plus the deferred shading looks nice AND you can feel free to not use it.

<img src="http://i.imgur.com/xsFEm6e.png" alt="TEAPOTS" width="400">

Unfortunately, it is very very minimalist right now. I mean, parts of it exist in some of my other projects (see the above teapots), but the library is still in the works.

#Contributing#
Feel free.

##How to Build##
1. Open the command prompt of your choice
2. Type `gradlew build`
3. Press `ENTER`
5. PROFIT! (Your .jar file is in build/libs/)

##Eclipse Setup##
1. Create a workspace
2. Open the command prompt of your choice
3. Run `gradlew eclipse`
4. Import the now created project (`file->import->general->existing projects into workspace`)

The two main source sets are `src/main` which contains the main BlueGin code and `src/gen` which is used for automatically generating the boring classes. Gradle should resolve the dependencies just fine, but eclipse will complain at you for using the generated code until you run `gradlew genCode` and refresh your project.