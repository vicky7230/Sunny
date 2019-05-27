![alt text](https://github.com/vicky7230/Sunny/blob/master/app/src/main/res/mipmap-xhdpi/ic_launcher.png "Logo")

# Sunny
![License](https://img.shields.io/badge/LICENSE-Apache%20License%202.0-blue.svg)
### A very simple app to show weather. 

### Featues
1. Beautifully designed.
2. Day and night theme based on time.
3. Add up to 4 cities.
4. Add widget to home screen

<p align="center">
  <img src="https://github.com/vicky7230/Sunny/blob/master/graphics/1.png" width="250">
  <img src="https://github.com/vicky7230/Sunny/blob/master/graphics/2.png" width="250">
  <img src="https://github.com/vicky7230/Sunny/blob/master/graphics/3.png" width="250">
</p>
<br>

<p align="center">
  <img src="https://github.com/vicky7230/Sunny/blob/master/graphics/widget.png" width="250">
</p>
<br>

# Get it on google play

[<img src="https://play.google.com/intl/en_us/badges/images/generic/en-play-badge.png" width="200">](https://play.google.com/store/apps/details?id=com.vicky7230.sunny)

## The Model-View-ViewModel Pattern
The main components in the MVVM pattern are:
* The View — that informs the ViewModel about the user’s actions
* The ViewModel — exposes streams of data relevant to the View
* The DataModel — abstracts the data source. The ViewModel works with the DataModel to get and save the data.

### MVVM pattern was created to simplify the event driven programming of user interfaces.
In MVVM, ViewModel exposes streams of events to which the Views can bind to. Because of this, the ViewModel does not need to hold a reference to the View anymore. This also means that all the interfaces that the MVP pattern requires, are now dropped. View has a reference to ViewModel but ViewModel has no information about the View. The consumer of the data should know about the producer, but the producer — the ViewModel — doesn’t know, and doesn’t care, who consumes the data.

<img src="https://github.com/vicky7230/Sunny/blob/master/graphics/mvvm.png">

### Libraries used:
1. Android-Iconics : <https://github.com/mikepenz/Android-Iconics>
2. html-textview: <https://github.com/SufficientlySecure/html-textview>
3. Android-Debug-Database: <https://github.com/amitshekhariitbhu/Android-Debug-Database>
4. EventBus: <https://github.com/greenrobot/EventBus>
5. Google Play Location Services: <https://developers.google.com/android/guides/setup>
6. Rxjava2 : <https://github.com/ReactiveX/RxJava>
7. RxAndroid : <https://github.com/ReactiveX/RxAndroid>
8. timber : <https://github.com/JakeWharton/timber>
9. Room : <https://developer.android.com/training/data-storage/room/index.html>
10. dagger2 : <https://github.com/google/dagger>
11. retrofit2 : <https://github.com/square/retrofit>
12. ViewModel : <https://developer.android.com/jetpack/androidx/releases/lifecycle>
13. Material Components for Android : <https://material.io/develop/android/docs/getting-started/>
14. AndroidX : <https://developer.android.com/jetpack/androidx>
15. ConstraintLayout : <https://developer.android.com/training/constraint-layout>

### License
```
   Copyright (C) 2019 VIPIN KUMAR

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
```
