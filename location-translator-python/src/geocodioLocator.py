from geolocator.geolocatorinterface import GeoLocator, Location
import geocodio
import geocodio.exceptions


class AuthError(Exception):
    pass

class DataError(Exception):
    pass

class GeneralError(Exception):
    pass

class ServerError(Exception):
    pass



class GeocodioLocator(GeoLocator):

    __apiKey__      = ""
    __geoCodio_client = ""


    def __init__(self, apiKey: str):

        self.__apiKey__ = apiKey
        self.__geoCodio_client = geocodio.GeocodioClient(apiKey)


    def translateToLocation(self, address: str, city: str, state: str, postalCode: str) -> Location:
        
        geoL = Location()
        
        try:

            geocoded_location = self.__geoCodio_client.geocode(components_data={ "street": address, "city": city, "state": state, "postal_code": postalCode})
        
        except geocodio.exceptions.GeocodioAuthError as ae:
            raise AuthError(f'Auth Error - Validate API Key')
        except geocodio.exceptions.GeocodioDataError as de:
            raise DataError(f'Data Error - {str(de)}')
        except geocodio.exceptions.GeocodioServerError as se:
            raise ServerError(f'Server Error - {str(se)}')
        except geocodio.exceptions.GeocodioError as ge:
            raise GeneralError(f'General Error - {str(ge)}')
        

        geoL.latitude = geocoded_location.coords[0]
        geoL.longitude = geocoded_location.coords[1]

        return(geoL)