# QuakeReport <img src="app/src/main/res/mipmap-hdpi/ic_launcher.png"/>

<b> NOTE: master1 branch uses AsyncTask. To see the Loader implementation, switch to Loader branch. This readme is for master1 branch </b>
</br></br>App to fetch earthquake details (magnitude between 6-10 & order them by time) using USGS API <b>(master1 branch)</b>

The app uses AsyncTask to communicate as you can't do Network calls on Main thread. Although AsyncTask has some disadvantages but it is 
idle for short operations(few seconds). I have not used any networking library such as Retrofit or Volley(for implementation of Retrofit,
see my InstaNyooz app https://github.com/deeps2/InstaNyooz). Inside Utils.java you can see methods for creating URL, making HTTP request,
reading data from Input Stream and parsing JSON response to get the desired information. 

The app uses different colors for displaying the magnitude of earthquake(red color is for higher magnitude). Click on any list items to go
to USGS site and get detail information about that particular earthquake.

Components Used
----------------
ListView</br>
Custom ArrayAdapter</br>
USGS API</br>
AsyncTask for Network Operations</br>
Methods for creating URL, making HTTP request and Parsing JSON response</br>
Custom circular TextView

ScreenShot
-----------
<img src="https://firebasestorage.googleapis.com/v0/b/delhi06-31a81.appspot.com/o/quake1.jpg?alt=media&token=6fba1e11-0872-4a59-8e15-fa7912b13d25" width=280/>&nbsp;&nbsp;

Video Demo(loader branch)
-------------------------
https://www.youtube.com/watch?v=BQrjUuedYxc
