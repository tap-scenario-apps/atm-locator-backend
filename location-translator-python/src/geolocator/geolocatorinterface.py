import abc

class Location(dict):
    latitude: float    
    longitude: float

class GeoLocator(abc.ABC):

    @abc.abstractmethod
    def translateToLocation( self, address: str, city: str, state: str, postalCode: str) -> Location:
        pass