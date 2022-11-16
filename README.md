# atm-locator-backend

## Description
The services contained in this repo make up the backend system of the ATM locator application.  It consists a set of polyglot micro-services that communicate
internally to implement a public search API as well as a protected management API.  The backend is comprosed of the following services:

* **atm** Internal micro-service that maintains a list of ATMs including their names, locations, and associations to a branch location.
* **branch** Internal micro-service that maintains a list of bank branches including their names, locations, and operating hours.
* **location-translator** Internal micro-service that translates a combination of a city/state pair or a zip code into global cooridinates (i.e. latitude and longitude).  This is essential for location and distance based searches.
* **atm-locator** Public facing service that implements the search API; it does not require authentication.  It communicates with the previous three services to execute search specific business logic. 
* **atm-mgmt** Public facing service that implements the ATM and Branch management APIs; it requires authentication.  The API exposes basic CRUD operations and including associations between ATMs and branches.

The public facing APIs are intended to be accessed by web UI front ends to create
* An interactive ATM location search application.  This application will use the `atm-locator` API.
* An application for managing ATM locations and branches.  This application will use the `atm-mgmt` API.  


## Functional Use Case

The backend services expose APIs that facilitate two primary functional use cases via web applications:

- An application for searching ATM locations based on zip code or city/state as well as a search radius.  The results include coordinates in latitude/longitude along with the distance (in miles) from the search location.  The results should allow for an interactive web app experience.

- An administrative application for performing management operations.  This includes adding and deleting ATM and branch locations.

## Application Architecture:

The high level architecture including the web apps looks like the following

![](doc/images/LocatorArch.png)

For the purpose of testing customer scenarios, some services have the capability of being deployed as a subset or in complete isolation.

## Customer Scenarios