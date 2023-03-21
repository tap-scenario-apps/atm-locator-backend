require('dotenvrc')
const express = require('express')
const geocodelocator  = require('./geocodioLocator.js')
const bodyParser = require('body-parser')
const url = require('url')
const querystring = require('querystring')


const port = process.env.GEOCODIO_PORT || 8080
const apiKey = process.env.GEOCODIO_API_KEY


let app = express()
let gl = new geocodelocator(apiKey)


app.use(bodyParser.urlencoded({extended: false}))
app.use(bodyParser.json())

app.get('/loc', async function(req, res, next){

    let loc = {}
    let address = req.query.address
    let city = req.query.city
    let state = req.query.state
    let postalCode = req.query.postalCode

    try {
        loc = await gl.translateToLocation(address, city, state, postalCode)
    } catch(error) {
        next(error)
    }

    if (!res.headersSent) {
        res.status(200).json({latitude: loc.latitude, longitude: loc.longitude})
    }

})

app.use(function(err, req, res, next) {
	const errorCode = err?.code ?? 500;
	if (!res.headersSent) {
		 res.status(errorCode);
	}

	res.json({ error: err.message, status: errorCode })
})


let server = app.listen(port, '0.0.0.0', function() {
    console.log('Express started on port %d', port)
})
