# QuakeReport <img src="app/src/main/res/mipmap-hdpi/ic_launcher.png"/>

<b> NOTE: loader branch uses Loader. To see the AsyncTask implementation, switch to master1 branch. This readme is for loader branch </b>
</br></br>App to fetch earthquake details using USGS API <b>(loader branch)</b>

The app uses Loader to communicate as you can't do Network calls on Main thread. Loader is better option than AsyncTask as it retains the data while configuration changes and listen to data source as well for any change. I have not used any networking library such as Retrofit or Volley (for implementation of Retrofit,see my InstaNyooz app https://github.com/deeps2/InstaNyooz). Inside Utils.java you can see methods for creating URL, making HTTP request, reading data from Input Stream and parsing JSON response to get the desired information. 

The app uses different colors for displaying the magnitude of earthquake(red color is for higher magnitude). Click on any list items to go
to USGS site and get detail information about that particular earthquake.

What's new in loader branch
---------------------------
(1) Unlike the master branch in which you can get only the earthquake between the magnitude 6-10, this branch has a Settings option where you can select the magnitude of earthquake and can sort it by time or strength of its magnitude.

(2) This branch uses Shared Preference for saving your settings so that when next time you launch the app, you will see the list of earthquakes corresponding to your last saved settings.

(3) When there is no earthquake found/or device is not connected to internet then a corresponding message will also be displayed
Components Used
----------------
ListView</br>
Custom ArrayAdapter</br>
USGS API</br>
Loader for Network Operations</br>
Methods for creating URL, making HTTP request and Parsing JSON response</br>
Custom circular TextView</br>
Shared Preferences</br>
Preference Fragment</br>

ScreenShot
-----------
<img src="https://firebasestorage.googleapis.com/v0/b/delhi06-31a81.appspot.com/o/quake1.jpg?alt=media&token=6fba1e11-0872-4a59-8e15-fa7912b13d25" width=280/>&nbsp;&nbsp;
<img src="https://firebasestorage.googleapis.com/v0/b/delhi06-31a81.appspot.com/o/quake3.jpg?alt=media&token=be145a79-2dec-4e49-8aec-6b7064b1eebf" width=280/>&nbsp;&nbsp;
<img src="https://firebasestorage.googleapis.com/v0/b/delhi06-31a81.appspot.com/o/quake4.jpg?alt=media&token=f142d21e-9ea8-4154-9287-c9fe7e87df14" width=280/></br>

Video Demo
-------------------------
https://www.youtube.com/watch?v=BQrjUuedYxc
