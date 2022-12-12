# atm-locator

## Description
The ATM service is responsible for managing ATM domain objects and exposes CRUD APIs for entity management as well as a search API to locate ATMs within a search radius given a set of geolocation coordinates (i.e. longitude and latitude).  ATM objects store the ATMs location using simple longitude and latitude coordinates, but also use geospacial data elements for the purpose of sophisticated searching.  Some database like postres require extensions to be installed on the database instance to enable geospacial capabilities while others like MySQL have these constructs built in out of the box.

## Entity Objects

The ATM service manages ATM objects which consist of three structures:
* A collection of ATM details
* A collection of ATM notes
* An ATM with an embedded collection of notes and details


```
record ATMDetail(@Id Long id, String detail, @Column("atmId") Long atmId)  
{

}
```

```
record ATMNote(@Id Long id, String note, @Column("atmId") Long atmId) 
{
}
```

```
record ATM(@Id Long id, String name, Float latitude, Float longitude, String addr, String city, String state, 
		@Column("postalCode") String postalCode, @Column("inDoors") boolean inDoors, @Column("branchId") Long branchId) 
{
}
```

The ATM also consists of geospactial data column with a type of POINT and an [SRID of 4326](https://www.cockroachlabs.com/docs/stable/srid-4326.html).  This column is used internally for searches by geolocational coordinates and is not exposed as part of any API.  For search APIs, a `distance` field is added to the returned API object to indicate the distance of the ATM from the search location.

## API

### Search ATMs By Location

  Searches for ATMs within a given location and search radius.  Location is provided as longitude/latitude.  
  
* **URL**

  /atm/locsearch
  
* **Method:**

  `GET`
  
*  **Query Params**

   **Required:**
 
   `longitude=[float]` 
   
   **Required:**
 
   `latitude=[float]`   
   
   **Optional:**
 
   `radius=[integer]` <br/><br/>
   Default value: 10

* **Success Response:**

  * **Code:** 200 <br />
    **Sample Content:** 
    ```
    [
      {
        "id": 2,
        "name": "Quick Trip Liberty North",
        "coordinates": {
          "latitude": 39.268436,
          "longitude": -94.5223
        },
        "addr": "10150 NE Cookingham Dr",
        "city": "Kansas City",
        "state": "MO",
        "postalCode": "64157",
        "distance": 3.4046943,
        "details": [],
        "notes": [],
        "inDoors": false,
        "branch": null
      }
    ]    
    ```
