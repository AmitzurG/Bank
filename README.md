
GENERAL

- The project desing is MVVM, using Jetpack ViewModel.
- Using Retrofit and gson to get data from the network and parsing json, respectively. 
- Using Kotlin coroutines to execute network operetions asynchronously. 
- The views request data from the network by the ViewModel and observe the results with LiveData.
- Using Glid library for images.
- Using MPAndroidChart third party library for drawing the stocks graphs

SCREENS DESCRIPTION

- The project contains three screens.
- The first screen contains grid of details of few banks (using RecyclerView and CardView). 
- The banks details in the file assets/banks.json.
- Clicking on one of the banks navigates to the second screen (using Jetpack Navigation).
- The second screen contains stocks information of the bank which has been clicked on.
- The stocks information is from the network, using Alphavantage API.
- Clicking on "INTERVAL" on the second screen actionbar enables to change the time interval of the information.
- Clicking on the chart icon on the second screen actionbar navigates ti the third screen.
- The third screen contains graphes of the stock.
- Clicking on "DATA" on the third screen actionbar enables to choose the graphs of which information to show.

- LAUNCH APP

- Open the project (contains code, resources, etc.) in Android Studio.  
- Compile, build and run the application with Android Studio.
- It's possible to run the ready apk (in this github).

