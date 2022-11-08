# atm-locator-backend

Core backend services for ATM locator application.

## Description
The services contained in this repo make up the backend system of the ATM locator application.  It consists a set of polyglot micro-services that communicate
internally to implement a public search API as well as a protected management API.  The backend is comprosed of the following services:

* **atm** Internal micro-service that maintains a list of ATMs including their names, locations, and associations to a branch location.
* **branch** Internal micro-service that maintains a list of bank branches including their names, locations, and operating hours.
* **location-translator** Internal micro-service that translates a combination of a city/state pair or a zip code into global cooridinates (i.e. latitude and longitude).  This is essential for location and distance based searches.
* **atm-locator** Public facing service that implements the search API; it does not require authentication.  It communicates with the previous three services to execute search specific business logic. 
* **atm-mgmt** Public facing service that implements the ATM and Branch management APIs; it requires authentication.  The API exposes basic CRUD operations and including associations between ATMs and branches.

