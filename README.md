# Cox Code Challenge

Implementation Details:

Dependency Injection : Dagger

Used MVVM Design pattern along with ROOM database to store data for offline storage.

Retrofit for network layer

Implemented a custom recycler view and resued it to populate dealer list vertically and vehicle list horizontally. Creeated a ItemDecorator to add margins around the recyclerview items.

MainActivity has three fragments HomeFragment, DealersFragment and VehiclesFragment. The vehicle list is retrieved 
in the HomeFragment and stored in the vehicles table. Once we get back a notification from the live data then we 
hide the progress bar and show get dealers button on the home screen. Clicking on Get Dealers button will launch the 
DealersFragment and then the dealer ids from the vehicles table are used to retrieve all dealers information from the 
server. As we retrieve Dealer info, each dealer is stored in the dealers table. DealersFragment has a recycler view to
populate dealer list fetched from the database. Clicking on any dealer will launch the VehiclesFragment that has a 
recycler view with vehicle list fetched from the database for that dealer id.


