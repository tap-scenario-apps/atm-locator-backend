const Geocodio = require("geocodio-library-node")

module.exports = class GeoLocator {
    constructor(apiKey) {
        this.apiKey = apiKey
        this.geoCodioClient = new Geocodio(apiKey)

    }

    async translateToLocation(address, city, state, postalCode) {

        const geoL = {latitude: '', longitude: ''}

        let result = await this.geoCodioClient.geocode({
            street: address,
            city: city,
            state: state,
            postal_code: postalCode,

        })

        geoL.latitude = result.results[0].location.lat
        geoL.longitude = result.results[0].location.lng


        return geoL
    }

}
